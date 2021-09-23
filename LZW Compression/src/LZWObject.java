
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.*;

/**
 * the rebuilding of the dictionary and the reinitializing of the dictionary works, 
 * however the way in which the decoder reads from the .txt file is completely broken and 
 * will sometimes read in certain characters in 16 bits and others in 8 (reader jank)
 * it will also output a .txt file which just has straight up nothing... I tried fixing this 
 * last night, but stuff kept hitting the fan, I am so sorry!
 * @author tylerdonovan
 *
 */

public class LZWObject {
	//get a string out of your original string; make a dictionary; convert the output string resultant from your output pattern to a byte array; print byte array to file using writeByteArrayToFile from apache
	//try to maybe write to a .bin might make things easier
	private static HashMap <String, Integer> dictionary;
	private static File message;
  private static HashMap <Integer, String> decoderDictionary;

	public LZWObject( File message) {
		super();
		this.message = message;
	}
	// encodes message as a byteArray and outputs said byte array to output.txt
	public static void encode() throws IOException {
		final long startTime = System.currentTimeMillis();
		dictionary = new HashMap<String, Integer>();
		FileReader reader = new FileReader(message);
		String current = "" + (char)reader.read();
		String next = "" + (char)reader.read();
		ArrayList<Integer >nums = new ArrayList<Integer>();
		int codeOn = 256;
		for(int i = 0; i < 256; i++)
		{
			dictionary.put("" + (char) i, i);
		}
		while (reader.ready()) {
			if (!dictionary.containsKey(current + next)) {
				dictionary.put(current + next, codeOn);
				nums.add(dictionary.get(current));
				codeOn++;
				current = next;
				next = "" + (char)reader.read();
			}
			else {
				current = current + next;
				next = "" + (char)reader.read();
			}
		}
		reader.close();
		String stringBits = allToBinary(nums);
		byte[] bitArray = stringToBinary(stringBits);
		FileOutputStream output = new FileOutputStream(new File ("output.txt"));
		output.write(bitArray);
		System.out.print(System.currentTimeMillis() - startTime); 
		output.close();
		}
		// converts a number to a binary string with len number of digits
	private static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s", Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
 
        return null;
    }
	// converts an ArrayList of ints to a String composed of the 9 bit binary representation of each of those ints 
	private static String allToBinary(ArrayList<Integer> ints) {
		StringBuilder str = new StringBuilder();
		for (int num : ints) {
			str.append(toBinary(num, 9));
		}
		return str.toString();
	}
	// converts a string representation of binary values to a byte array
	// puts extra 0's at the end of the String to ensure that it can be stored in a byte array (multiple of 8 bits)
	private static byte[] stringToBinary(String str) {
		int zeroes = (str.length() %8); 
		if (zeroes != 0)
		{
			String zero = "0000000"; 
			str += zero.substring(0, 8 - zeroes); 
		}
		
		byte[] bytes = new byte[str.length()/8];
		int counter = 0;
		for (int i = 0; i < str.length(); i+= 8) {
			bytes[counter] = (byte)Integer.parseInt(str.substring(i, i+8), 2);
			counter++;
		}
		return bytes;
	}
	// takes in a text file of binary and deciphers the binary
	// reverses the operations of encode, outputing the original message to a text file
	public void decode(String filename, int bitSize) throws IOException
	{
		final long startTime = System.currentTimeMillis();
		initializeDecoderDictionary();
		String masterString = readFromCompressedFile(filename, bitSize);
		int [] integerValues = convertStringToIntegers(masterString, bitSize);
		rebuildDictionary(integerValues);
		FileWriter output = new FileWriter(new File ("decompressed.txt"));
		for(int i = 0; i < integerValues.length; i++)
		{
			if(decoderDictionary.get(integerValues[i]) != null)
			{
			output.write(decoderDictionary.get(integerValues[i]));
			}
			else
			{
				output.write(("" + decoderDictionary.get(integerValues[i])).replaceAll("null", "@"));
			}
		}
		System.out.print(System.currentTimeMillis() - startTime); 
		
	}
	
	// initializes decoderDictionary with ASCII
	public void initializeDecoderDictionary()
	{
		decoderDictionary = new HashMap<Integer, String>();
		for(int i = 0; i < 256; i++)
		{
			decoderDictionary.put(i, "" + (char) i);
		}
	}
	// reads the file created by encode and returns a String of appended binary values
	public String readFromCompressedFile(String filename, int bitSize) throws IOException
	{
			FileReader reader = new FileReader(filename);
			String decoderMasterString = "";
			int character;
			BufferedReader br = new BufferedReader(reader);
            while ((character = br.read()) != -1) {
            decoderMasterString += toBinary(character, 8);
		}
        decoderMasterString = decoderMasterString.substring(0, decoderMasterString.length() - decoderMasterString.length()%bitSize);
		return decoderMasterString;
	}
	
	// converts a given binary to string to an integer representation and returns that int
	public int [] convertStringToIntegers(String binValueString, int bitSize)
	{
		int [] encodedValueArray = new int [binValueString.length()/bitSize];
		int counter = 0;
		for(int i = 0; i < binValueString.length() - bitSize; i += bitSize)
		{
			encodedValueArray[counter] = Integer.parseInt(binValueString.substring(i, i + bitSize),2);
			counter++;
		}
		return encodedValueArray;
	}
	
	
	// loops through array of integers representing Strings in the encoder dictionary
	// adds LZW combined ASCII values to the decoderDictionary to make it emthe encoder dict
	public void rebuildDictionary(int [] numberEntries)
	{
		String c = "";
		for(int i = 0; i < numberEntries.length; i++)
		{
			String cn = (c + decoderDictionary.get(numberEntries[i])).replaceAll("null", "@");
			if(decoderDictionary.containsValue(cn))
			{
				c = cn;
			}
			else
			{
				decoderDictionary.put(decoderDictionary.size(), cn);
				c = cn.substring(cn.length()-1);
			}
		}
	}
	
	public static void main (String [] args) throws IOException {
		LZWObject obj = new LZWObject(new File("lzw-file2.txt"));
		obj.encode();
		obj.decode("output.txt", 9);
	}
}
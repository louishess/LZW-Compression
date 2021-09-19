
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
	public static void encode() throws IOException {
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
				nums.add(codeOn);
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
		output.close();
		}
		
	private static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",
                            Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
 
        return null;
    }
	private static String allToBinary(ArrayList<Integer> ints) {
		StringBuilder str = new StringBuilder();
		for (int num : ints) {
			str.append(toBinary(num, 9));
		}
		return str.toString();
	}
	private static byte[] stringToBinary(String str) {
		while(str.length()%8 !=0)
		{
			str += 0;
		}
		byte[] bytes = new byte[str.length()/8];
		int counter = 0;
		for (int i = 0; i < str.length(); i+= 8) {
			bytes[counter] = (byte)Integer.parseInt(str.substring(i, i+8), 2);
			System.out.println(bytes[counter]);
			counter++;
		}
		return bytes;
	}
	public void decode(String filename, int bitSize) throws IOException
	{
		
		reinitializeDictionary();
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
		
	}
	
	public void reinitializeDictionary()
	{
		decoderDictionary = new HashMap<Integer, String>();
		for(int i = 0; i < 256; i++)
		{
			decoderDictionary.put(i, "" + (char) i);
		}
	}
	
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
	
	public int [] convertStringToIntegers(String binValueString, int bitSize)
	{
		int [] encodedValueArray = new int [binValueString.length()/bitSize];
		int counter = 0;
		for(int i = 0; i < binValueString.length() - bitSize; i += bitSize)
		{
			encodedValueArray[counter] = Integer.parseInt(binValueString.substring(i, i + bitSize),2);
			System.out.println(encodedValueArray[counter]);
			counter++;
		}
		return encodedValueArray;
	}
	
	public void rebuildDictionary(int [] numberEntries)
	{
		String c = "";
		for(int i = 0; i < numberEntries.length; i++)
		{
			String cn = (c + decoderDictionary.get(numberEntries[i])).replaceAll("null", "@");
			System.out.println(cn);
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
		for(int i = 0; i < numberEntries.length; i++)
		{
			System.out.println(numberEntries[i]);
		}
	}
	
	public static void main (String [] args) throws IOException {
		LZWObject obj = new LZWObject(new File("lzw-file1.txt"));
		obj.encode();
		obj.decode("output.txt", 9);
	}
}
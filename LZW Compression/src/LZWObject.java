
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.*;

public class LZWObject {
	//get a string out of your original string; make a dictionary; convert the output string resultant from your output pattern to a byte array; print byte array to file using writeByteArrayToFile from apache
	//try to maybe write to a .bin might make things easier
	private static HashMap <String, Integer> dictionary;
	private static File message;
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
		System.out.println(stringBits.length());
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
			System.out.println(str.length());
			str.append(toBinary(num, 15));
		}
		return str.toString();
	}
	private static byte[] stringToBinary(String str) {
		byte[] bytes = new byte[str.length()/8];
		int counter = 0;
		for (int i = 0; i < str.length(); i+= 8) {
			bytes[counter] = Byte.parseByte(str.substring(i, i+8), 2);
		}
		return bytes;
	}
	public static void main (String [] args) throws IOException {
		LZWObject obj = new LZWObject(new File("src/lzw-file3.txt"));
		System.out.println ("Binary rep of 00000001 is: " + new BigInteger ("00000001").toByteArray());
		obj.encode();
		
	}
}
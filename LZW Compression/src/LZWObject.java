
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
				current = "" + (char)reader.read();
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
		ArrayList<String> stringBits = allToBinary(nums);
		ArrayList<byte[]> bitArray = stringToBinary(stringBits);
		FileOutputStream output = new FileOutputStream(new File ("output.txt"));
		for (byte[] bits : bitArray) {
			output.write(bits);
		}
		
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
	private static ArrayList<String> allToBinary(ArrayList<Integer> ints) {
		ArrayList<String> ret = new ArrayList<String>(ints.size());
		for (int num : ints) {
			ret.add(toBinary(num, 9));
		}
		return ret;
	}
	private static ArrayList<byte[]> stringToBinary(ArrayList<String> nums) {
		int counter = 0;
		ArrayList<byte[]> ret = new ArrayList<byte[]>();
		for (String str : nums) {
			BigInteger number = new BigInteger(str);
			ret.add(number.toByteArray());
		}
		return ret;
	}
	public static void main (String [] args) throws IOException {
		LZWObject obj = new LZWObject(new File("lzw-file3.txt"));
		obj.encode();
	}
}
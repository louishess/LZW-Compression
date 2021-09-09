
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LZWObject {
	//get a string out of your original string; make a dictionary; convert the output string resultant from your output pattern to a byte array; print byte array to file using writeByteArrayToFile from apache
	//try to maybe write to a .bin might make things easier
	private static HashMap <String, Integer> dictionary;
	private static File message;
	public LZWObject(HashMap<String, Integer> dictionary, File message) {
		super();
		this.dictionary = dictionary;
		this.message = message;
	}
	public static void encode() throws IOException {
		FileReader reader = new FileReader(message);
		String current = "" + (char)reader.read();
		String next = "" + (char)reader.read();
		int codeOn = 256;
		StringBuilder output = new StringBuilder("");
		while (reader.ready()) {
			if (!dictionary.containsKey(current + next)) {
				output.append(current);
				current = "" + (char)reader.read();
				dictionary.put(current + next, codeOn);
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
		byte[] bytes = 
	}
}
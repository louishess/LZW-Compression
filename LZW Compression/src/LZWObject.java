
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LZWObject {
	//get a string out of your original string; make a dictionary; convert the output string resultant from your output pattern to a byte array; print byte array to file using writeByteArrayToFile from apache
	//try to maybe write to a .bin might make things easier
	private static HashMap <Integer, String> dictionary;
	private static File message;
	public LZWObject(HashMap<Integer, String> dictionary, File message) {
		super();
		this.dictionary = dictionary;
		this.message = message;
	}
	public static void encode() throws IOException {
		FileReader reader = new FileReader(message);
		String current = "" + (char)reader.read();
		int codeOn = 256;
		StringBuilder output = new StringBuilder("");
		while (reader.ready()) {
			if (!dictionary.containsKey(current)) {
				output.append(current);
				current = "" + (char)reader.read();
			}
			else {
				dictionary.put(codeOn, current);
				current = current + (char)reader.read();
				codeOn++;
			}
		}
		
	}
}

import java.io.File;
import java.util.HashMap;

public class LZWObject {
	private HashMap <Short, String> dictionary;
	private File message;
	public LZWObject(HashMap<Short, String> dictionary, File message) {
		super();
		this.dictionary = dictionary;
		this.message = message;
	}
	public static File encode() {
		File output = new File("output.txt");
		
		return output;
	}
}
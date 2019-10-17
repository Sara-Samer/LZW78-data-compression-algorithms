import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*; 


public class Main {
	public static void main(String[] args) throws IOException {
        String dicPath = System.getProperty("user.dir") + File.separatorChar + "dictionary.txt";
        String outString = "";
        int selection = 0;
		Scanner inStream = new Scanner(System.in);
        
		
		System.out.println("do you want to compress \n(1) file.txt \n(2) text\n");
		selection = Integer.parseInt(inStream.nextLine());
		switch (selection){
			case 1:
				System.out.println("Please enter File.txt Path: ");
				String p = inStream.nextLine();
				outString = Compress(dicPath, getTextFromFile(p)); 
				saveCompressedFile(p, outString);
				break;
			case 2:
				System.out.println("Please: Enter text: ");
				outString = Compress(dicPath, inStream.nextLine()); 
				break;
			default:
				System.out.println("Invalid choice re-enter ur choice plz\n");
		}
		inStream.close();
        System.out.println(outString);       
    }
	
	public static List<String> readAllLines(String path) throws IOException {
		List <String> lines = Collections.emptyList(); 
	    lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
	    return lines;
	}
	static void saveToDictionary(List<String> lines, String dicPath, int startPoint) throws IOException {
		PrintWriter toFile = new PrintWriter(new FileOutputStream(dicPath));
	    for (String line : lines)
	    	toFile.println(line);
	    toFile.close();
	}
	public static String getTextFromFile(String path) throws IOException {
		String data = ""; 
	    data = new String(Files.readAllBytes(Paths.get(path))); 
	    return data; 
		
	}
	public static String Compress(String dicPath, String inString) throws IOException {
		String outString = "", word  ="";
		List<String> lines = readAllLines(dicPath); //load dictionary in ArrayList
    	word = Character.toString( inString.charAt(0) );
    	Integer index = 0, lastIndex = 0, startPoint = lines.size();

        for(int i = 0; i < inString.length() - 1; ++i) {
      
        	index = lines.indexOf(word);
        	if(index == -1) {
    			lines.add(word);
    			outString = outString.concat(Integer.toString(lastIndex) + "," + 
    						Character.toString( word.charAt(word.length() - 1) )  + ",") ; 
    			/// add most recent index + last char added to word
    			
    			word =  Character.toString( inString.charAt(i+1) ); //reset word
    			lastIndex = 0;
        	}else {
    			word = word + Character.toString( inString.charAt(i + 1) ); // set word
    			lastIndex = index;
    		}
        }
        saveToDictionary(lines, dicPath, startPoint);
        return outString;
	}
	public static void saveCompressedFile(String path, String Content) throws IOException {
		//createFile(path + File.separatorChar +"Compressed File.txt");
		File f = new File(path);
		BufferedWriter writer = new BufferedWriter(new FileWriter(f.getParent() + File.separatorChar +"Compressed File.txt"));
	    writer.write(Content);
	    writer.close();
	}
	public static void createFile(String path) throws IOException {
		File file = new File(path);
		file.createNewFile(); // if file already exists will do nothing 
	}
	
}

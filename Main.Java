package lzw78;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*; 
import java.io.*; 


public class LZW78 {
	public static void main(String[] args) throws IOException {
        String inString =  "", word = "", dicPath = "E:\\eclipse\\eclipse-workspace\\lzw78\\src\\lzw78\\dic.txt";
											//Don't forget to change file path
        String outString = "";
        @SuppressWarnings("resource")
		Scanner inStream = new Scanner(System.in);
        
        List<String> lines = Collections.emptyList(); //Create empty list to store dictionary 
        lines = readAllLines(dicPath, lines); //load dictionary in ArrayList
        Integer index = 0, lastIndex = 0, startPoint = lines.size();
        
       // checkFile(dicPath);
     
        inString = inStream.nextLine();
    	word = Character.toString( inString.charAt(0) );

        for(int i = 0; i < inString.length() - 1; ++i) {
      
        	index = lines.indexOf(word);
        	if(index == -1) {
    			lines.add(word);
    			outString = outString.concat(Integer.toString(lastIndex) + "," + 
    						Character.toString( word.charAt(word.length() - 1) )  + ",") ; /// add most recent index + last char added to word
    			
    			word =  Character.toString( inString.charAt(i+1) ); //reset word
    			lastIndex = 0;
        	}else {
    			word = word + Character.toString( inString.charAt(i + 1) ); // set word
    			lastIndex = index;
    		}

        }
        
        System.out.println(outString);
        saveToDictionary(lines, dicPath, startPoint);
        show(lines, dicPath);
       
    }
	public static void checkFile(String path) throws IOException {
		File dic = new File(path);
		if(!dic.exists()) {
			FileWriter writer = new FileWriter(dic);
		}
	}
	public static List<String> readAllLines(String path, List<String> lines) throws IOException {
		lines = Collections.emptyList(); 
	    lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
	    return lines;
	}
	static void saveToDictionary(List<String> lines, String dicPath, int startPoint) throws IOException {
		
		//for(int i = startPoint; i < lines.size(); ++i )
			//Files.write(Paths.get(dicPath), (lines.get(i)+'\n').getBytes(), StandardOpenOption.APPEND);
		PrintWriter toFile = new PrintWriter(new FileOutputStream(dicPath));
	    for (String line : lines)
	    	toFile.println(line);
	    toFile.close();
	}
	
}

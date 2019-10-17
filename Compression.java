import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Compression{

    public static void main(String[] args){
        LZ78 lz = new LZ78();
        ArrayList<LZ78.EncodePair> out = lz.encode("ABAABABAABABBBBBBBBBBA");
        for(LZ78.EncodePair ep : out){
            System.out.println(ep.c + " " + String.valueOf(ep.i));
        }
        System.out.println(lz.decode(out));
        try {
            lz.encodeFile("compress_me.txt");
            lz.decodeFile("compress_me.txt.lz78");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

};


class LZ78{

    static class EncodePair{
        char c;
        int i;
        EncodePair(char c, int i) { this.c = c; this.i = i;}
    }

    HashMap<String, Integer> dict = new HashMap<>();
    ArrayList<String> addresses = new ArrayList<>();

    public ArrayList<EncodePair> encode(String input){
        dict.clear();
        addresses.clear();
        dict.put("", 0);
        addresses.add("");
        String curr = "";
        ArrayList<EncodePair> output = new ArrayList<>();
        for(int i = 0; i < input.length(); ++i){
            String tmp = curr + input.charAt(i);
            if(!dict.containsKey(tmp)){
                int prefix = dict.get(curr);
                EncodePair ep = new EncodePair(input.charAt(i), prefix);
                output.add(ep);
                addresses.add(tmp);
                dict.put(tmp, addresses.size()-1);
                curr = "";
            }else{
                curr = tmp;
            }
        }
        if(!curr.equals("")){
            int prefix = dict.get(curr.substring(0, curr.length()-1));
            EncodePair ep = new EncodePair(curr.charAt(curr.length()-1), prefix);
            output.add(ep);
        }
        return output;
    }

    public String decode(ArrayList<EncodePair> eps){
        String out = "";
        for(int i = 0; i < eps.size(); ++i){
            out += addresses.get(eps.get(i).i) + eps.get(i).c;
        }
        return out;
    }

    public void encodeFile(String fileName) throws IOException{
        dict.clear();
        addresses.clear();
        dict.put("", 0);
        addresses.add("");
        String curr = "";
        FileReader fr = new FileReader(fileName);
        DataOutputStream fw = new DataOutputStream(new FileOutputStream(fileName + ".lz78"));
        int a;
        while((a = fr.read()) != -1){
            char c = (char) a;
            String tmp = curr + c;
            if(!dict.containsKey(tmp)){
                int prefix = dict.get(curr);
                fw.writeChar(c);
                fw.writeInt(prefix);
                addresses.add(tmp);
                dict.put(tmp, addresses.size()-1);
                curr = "";
            }else{
                curr = tmp;
            }
        }
        fr.close();
        if(!curr.equals("")){
            int prefix = dict.get(curr.substring(0, curr.length()-1));
            fw.writeChar(curr.charAt(curr.length()-1));
            fw.writeInt(prefix);
        }
        fw.close();
    }

    public void decodeFile(String fileName) throws IOException{
        DataInputStream fr = new DataInputStream(new FileInputStream(fileName));
        FileWriter fw = new FileWriter(fileName + ".ext");
        while(true){
            try{
                char c = fr.readChar();
                int p = fr.readInt();
                fw.write(addresses.get(p) + c);
            }catch(EOFException e){
                break;
            }
        }
        fw.close();
        fr.close();
    }
};

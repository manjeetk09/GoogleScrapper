import net.didion.jwnl.data.Exc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by manjeet on 6/6/17.
 */
public class test1 {
    public static void main(String args[]) throws IOException{
        //code for running wikipedia api from this project
//        try {
//            String command = "mapWiki.py";
//            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("/home/manjeet/anaconda2/bin/python2.7", command, "debugger"));
//            Process proc = pb.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = "";
//            while((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//            proc.waitFor();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }

        //running wikiSegregate
//        wikiSegregate obj = new wikiSegregate();
//        try
//        {
//            obj.wikiFunc("debugger");
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }

        testCategory testCat = new testCategory();
        testCat.testCatFunc("debugger");


    }
}

import net.didion.jwnl.data.Exc;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class test1 {
    public static void main(String args[]) throws IOException
    {
        String source = "manjeet_(programming";
        String dest = "manjeet_\\(programmingies";
        String a = source.replaceAll("\\(","");
        System.out.println(a);
//        File sourceF = new File(source);
//        File destF = new File(dest);
//        if(!destF.exists()){
//            destF.mkdirs();
//        }
//        try {
//            boolean bool = destF.createNewFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        String files[] = sourceF.list();
//
//        //Iterate over all files and copy them to destinationFolder one by one
//        for (String file : files)
//        {
//            File srcFile = new File(source, file);
//            File destFile = new File(dest, file);
//
//            //Recursive function call
//            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//        }
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

//        testCategory testCat = new testCategory();
//        testCat.testCatFunc("debugger");


    }
}

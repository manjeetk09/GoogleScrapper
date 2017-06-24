import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ashutosh on 18/6/17.
 */
public class testCategory {
//    public static void main(String args[]) throws IOException {
//        GetCategory getCategory = new GetCategory();
//        for(int i = 0 ; i < 5 ; i++){
//            System.out.println("Template:: " + i);
//            getCategory.runCat(i);
//
//        }
//
//    }

    public void testCatFunc (String head_entity) throws IOException
    {
        System.out.println("entering into category part");

        //writing catInput files
        FileWriter fw = new FileWriter("Category/catInput.csv");
        BufferedWriter bw = new BufferedWriter(fw);

        FileReader fr1 = new FileReader("processedMappedWiki.csv");
        BufferedReader br1 = new BufferedReader(fr1);

        FileReader fr2 = new FileReader("processedUnmappedWiki.csv");
        BufferedReader br2 = new BufferedReader(fr2);

        String line = "";
        while((line = br1.readLine()) != null)
        {
            String []comp = line.split(";");
            bw.write(comp[3]+";"+head_entity+"\n");
        }
        while((line = br2.readLine()) != null)
        {
            String []comp = line.split(";");
            bw.write(comp[3]+";"+head_entity+"\n");
        }

        try
        {
            if(br2 != null)
                br2.close();
            if(fr2 != null)
                fr2.close();
            if(br1 != null)
                br1.close();
            if(fr1 != null)
                fr1.close();
            if(bw != null)
                bw.close();
            if(fw != null)
                fw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        GetCategory getCategory = new GetCategory();
        getCategory.runCat();

    }
}
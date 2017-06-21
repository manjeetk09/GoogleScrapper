import net.didion.jwnl.data.Exc;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TKBProcess
{
    public static void main (String args[]) throws IOException
    {
        String filename = "taxonomyRelations.txt";
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);

        List <String> list = new ArrayList<String>();

        String line = "";
        while((line = br.readLine()) != null)
        {
            list.add(line);
        }

        try
        {
            if(br!=null)
                br.close();
            if(fr!=null)
                fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //loading the list into a hashset
        Set<String> set = new HashSet<String>(list);

        //to check for it containing a string : set.contains(String)

        //processing the TKB
        String filename1 = "TeKnowbase.tsv";
        FileReader fr1 = new FileReader(filename1);
        BufferedReader br1 = new BufferedReader(fr1);

        String filewrite = "processedTeKnowbase.tsv";
        FileWriter fw = new FileWriter(filewrite);
        BufferedWriter bw = new BufferedWriter(fw);

        String line1 = "";

        while((line1 = br1.readLine()) != null)
        {
            String []compo = line1.split("\t");
            if(compo[2].contains("rdffreebasecom") || compo[2].contains("dbpedia.org"))
            {

            }
            else if(set.contains(compo[1]))
            {
                fw.write(line1 + "\n");
            }
            else if(compo[1].contains("is "))
            {
                fw.write(line1 + "\n");
            }
            else if(compo[1].contains("are "))
            {
                fw.write(line1 + "\n");
            }
            else if(compo[1].contains("can "))
            {
                fw.write(line1 + "\n");
            }
        }

        try
        {
            if(bw!=null)
                bw.close();
            if(fw!=null)
                fw.close();
            if(br1!=null)
                br1.close();
            if(fr1!=null)
                fr1.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

import net.didion.jwnl.data.Exc;
import scala.Int;
import scalaz.Category;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class CombineOllieRake {
    public void combineOllieRake(int temp_num) throws IOException
    {
        FileWriter fw = new FileWriter("final.csv");
        BufferedWriter bw = new BufferedWriter(fw);

        for(int i=0;i<temp_num;i++)
        {
            ArrayList<String> line_num = new ArrayList<String>();
            ArrayList<String> relation_score = new ArrayList<String>();

            FileReader fr = new FileReader("entity_rake" + i + ".csv");
            BufferedReader br = new BufferedReader(fr);

            FileReader fr1 = new FileReader("result" + i + ".csv");
            BufferedReader br1 = new BufferedReader(fr1);

            String line = "";
            String line1 = "";
            int previous = 0;
            Double sim = 0.0;

            while((line1 = br1.readLine()) != null)
            {
                String []components = line1.split(";");
                if(Integer.parseInt(components[0]) == previous)
                {
                    if(Double.parseDouble(components[2]) > sim)
                    {
                        relation_score.set(relation_score.size()-1,components[2]);
                        sim = Double.parseDouble(components[2]);
                    }
                }
                else
                {
                    line_num.add(components[0]);
                    relation_score.add(components[2]);
                    previous = Integer.parseInt(components[0]);
                    sim = Double.parseDouble(components[2]);
                }
            }

            try
            {

                if(br1!=null)
                    br1.close();
                if(fr1!=null)
                    fr1.close();
            }
            catch (Exception e){e.printStackTrace();}

            while((line = br.readLine()) != null)
            {
                System.out.println(line);
                String []components = line.split(";");
                int get_line = Integer.parseInt(components[0]);
                int j = 0;
                int flag = 0;


                for(;j<line_num.size();j++)
                {
                    if(get_line == Integer.parseInt(line_num.get(j)))
                    {
                        flag = 1;
                        break;
                    }
                }

                if(flag == 1)
                {
                    System.out.println("IF:" + components[2]);
                    bw.write(i+";"+get_line+";"+components[1]+";"+components[2]+";"+components[3]+";"+components[4]+";"+components[5]+";"+relation_score.get(j)+ ";" + components[6] +"\n");
                }
                else
                {
                    System.out.println("ELSE:" + components[2]);
                    bw.write(i+";"+get_line+";"+components[1]+";"+components[2]+";"+components[3]+";"+components[4]+";"+components[5]+";"+0+ ";" + components[6] +"\n");
                }

            }
            try
            {
                if(br!=null)
                    br.close();
                if(fr!=null)
                    fr.close();
            }
            catch (Exception e){e.printStackTrace();}
        }

        try
        {
            if(bw!=null)
                bw.close();
            if(fw!=null)
                fw.close();
        }
        catch (Exception e){e.printStackTrace();}
    }
}

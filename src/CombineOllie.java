import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import semantics.Compare;
import java.util.Comparator;
import java.util.*;
import java.lang.*;


public class CombineOllie
{
    public static class EntityScoreListSort implements Comparator<EntityScore>{

        public int compare(EntityScore s1, EntityScore s2) {
            if(s1.getScore() - s2.getScore() > 0.0)
            {
                return 1;
            }
            else if(s1.getScore() == s2.getScore())
            {
                return 0;
            }
            else
            {
                return -1;
            }
            //return s1.getScore() - s2.getScore();
        }
    }

    public static void combineOllieFunc (int tempNum) throws IOException
    {

        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter("combinedOllie.csv");
        bw = new BufferedWriter(fw);
        PrintWriter f = new PrintWriter(bw);

        ArrayList<EntityScore> ent_score_list_final = new ArrayList<EntityScore>();

        for(int i=0; i< tempNum;i++)
        {
            BufferedReader br = null;
            FileReader fr = null;
            System.out.println("i in combine is:: " + i);
            fr = new FileReader("result_similarity_ollie"+i+".csv");
            br = new BufferedReader(fr);

            ArrayList<EntityScore> ent_score_list = new ArrayList<EntityScore>();

            //read the csv
            String sCurrentLine = "";

            try
            {
                while((sCurrentLine = br.readLine()) != null)
                {
                    String []currentLine = sCurrentLine.split(";");
                    int url_index_temp = Integer.parseInt(currentLine[0]);
                    FileReader fs = new FileReader("urls"+i+".txt");
                    BufferedReader br1 = new BufferedReader(fs);
                    for(int k = 1; k < url_index_temp; ++k)
                        br1.readLine();
                    String url_temp = br1.readLine();

                    String entity_temp = currentLine[1];
                    double score_temp = Double.parseDouble(currentLine[5]);
                    EntityScore obj = new EntityScore(i, url_temp, entity_temp, score_temp);

                    ent_score_list.add(obj);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            EntityScoreListSort ss = new EntityScoreListSort();
            Collections.sort(ent_score_list, ss);

            try
            {
                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            //taking the top 20 results and storing them in the final arraylist
//            if(ent_score_list.size() < 20)
//            {
            for(int j=0;j<ent_score_list.size();j++)
            {
                if(ent_score_list.get(j).getEntity().matches(":"))
                {
                    continue;
                }
                else
                {
                    ent_score_list_final.add(ent_score_list.get(j));
                }

            }
//            }
//            else
//            {
//                for(int j=0;j<20;j++)
//                {
//                    if(ent_score_list.get(j).getEntity().matches(":"))
//                    {
//                        continue;
//                    }
//                    else
//                    {
//                        ent_score_list_final.add(ent_score_list.get(j));
//                    }
//
//                }
//            }

        }

        //processing of the final array list with similarity
        for(int i=0; i<(ent_score_list_final.size()-1);i++)
        {

            if(ent_score_list_final.get(i).getEntity().matches(":"))
            {
                continue;
            }

            for(int j=i+1; j<ent_score_list_final.size();j++)
            {
                if(ent_score_list_final.get(j).getEntity().matches(":"))
                {
                    continue;
                }

                Compare c = new Compare(ent_score_list_final.get(i).getEntity(),ent_score_list_final.get(j).getEntity());
                double similarity_value = c.getResult();
                Compare c1 = new Compare(ent_score_list_final.get(j).getEntity(),ent_score_list_final.get(i).getEntity());
                double similarity_value1 = c1.getResult();
                double sim_avg = ( similarity_value + similarity_value1 ) / 2.0;

                if(sim_avg > 0.8)
                {
                    if(ent_score_list_final.get(i).getTempIndex() != ent_score_list_final.get(j).getTempIndex())
                    {
                        double score1 = ent_score_list_final.get(i).getScore();
                        double score2 = ent_score_list_final.get(j).getScore();
                        ent_score_list_final.get(i).setScore(score1 + score2);
                        ent_score_list_final.get(j).setScore(score1 + score2);
                    }
                }
                if(sim_avg > 0.95)
                {
                    if(ent_score_list_final.get(i).getEntity().length() >= ent_score_list_final.get(j).getEntity().length())
                    {
                        ent_score_list_final.get(j).setEntity(":");
                    }
                    else
                    {
                        ent_score_list_final.get(i).setEntity(":");
                    }
                }

            }
        }

        for(int i=0;i<ent_score_list_final.size();i++)
        {
            if(ent_score_list_final.get(i).getEntity().matches(":"))
            {
                continue;
            }
            double scor_Temp = ent_score_list_final.get(i).getScore();
            double scor_temp = 1.0/( 1.0 + Math.pow(Math.E,(-1.0*scor_Temp)));
            f.println(ent_score_list_final.get(i).getTempIndex()+";"+ent_score_list_final.get(i).getUrl()+";"+ent_score_list_final.get(i).getEntity()+";"+ent_score_list_final.get(i).getScore()+";"+scor_temp);
        }

        try {

            if (bw != null)
                bw.close();

            if (fw != null)
                fw.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }


    }
}

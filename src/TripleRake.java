import net.didion.jwnl.data.Exc;
import semantics.Compare;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by manjeet on 21/5/17.
 */
public class TripleRake
{
    public void tripleRake(int temp_index) throws IOException
    {
        ArrayList<String> urls = new ArrayList<String>();

        FileReader fr1 = new FileReader("urls" + temp_index + ".txt");
        BufferedReader br1 = new BufferedReader(fr1);

        String line_url = "";
        while((line_url = br1.readLine()) != null)
        {
            urls.add(line_url);
        }

        try
        {
            if(br1 != null)
                br1.close();
            if(fr1 != null)
                fr1.close();
        }
        catch (Exception e){e.printStackTrace();}

        ArrayList<Integer> line_num = new ArrayList<Integer>();
        ArrayList<String> entity_name = new ArrayList<String>();
        ArrayList<Double> rake_score = new ArrayList<Double>();
        ArrayList<Integer> freq_in_doc = new ArrayList<Integer>();
        ArrayList<Integer> num_docs = new ArrayList<Integer>();

        FileReader fr = new FileReader("crawler" + temp_index + ".txt");
        BufferedReader br = new BufferedReader(fr);

        FileWriter fw1 = new FileWriter("entity_rake" + temp_index + ".csv");
        BufferedWriter bw1 = new BufferedWriter(fw1);
        //bw1.write("LINE NO." + ";" + "URLS" + ";" + "ENTITY" + ";"+"RAKE-SCORE"+";" + "TF"+ ";" + "IDF"+ "\n");

        int i = 0;
        String line2 = "";
        while((line2 = br.readLine()) != null)
        {
            i = i+1;
            //System.out.println(i);
            if(line2.length() >= 3)
            {
                FileWriter fw = new FileWriter("temp.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(line2);

                try
                {
                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();
                }
                catch(Exception e){e.printStackTrace();}

                try
                {
                    String command = "python rake_tutorial.py temp.txt";

                    Process proc = Runtime.getRuntime().exec(command);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                    String line = "";

                    while((line = reader.readLine()) != null) {

                        if(line.substring(0,9).matches("Candidate"))
                        {
                            String entity_temp = line.substring(12,line.indexOf(',')-1);
                            line = line.substring(line.indexOf(','));
                            String score_temp_string = line.substring( line.indexOf(':')+2);
                            Double score_temp = Double.parseDouble( score_temp_string );
//                        System.out.println(entity_temp + '\t' + score_temp);
//                        bw1.write(i+";"+entity_temp+";"+score_temp+"\n");
                            line_num.add(i);
                            entity_name.add(entity_temp);
                            rake_score.add(score_temp);
                            freq_in_doc.add(0);
                            num_docs.add(1);
                        }
                    }

                    proc.waitFor();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }



        }
        try
        {


            if (br != null)
                br.close();

            if (fr != null)
                fr.close();
        }
        catch(Exception e){e.printStackTrace();}

        //TF-IDF
        int total_docs = 1;
        ArrayList<Integer>  terms_in_doc = new ArrayList<Integer>();
        int previous_index = 0;
        System.out.println("LINE SIZE:" + line_num.size());
        for(int j=0;j<line_num.size()-1;j++)
        {
            System.out.println("LINE NUM:" + line_num.get(j) + ";" + line_num.get(j+1));
            if(urls.get(line_num.get(j)-1).matches(urls.get(line_num.get(j+1)-1)))
            {
                continue;
            }
            else
            {
                total_docs = total_docs + 1;
                int answer = (j-previous_index + 1);
                for(int k=previous_index;k<=j;k++)
                {
                    terms_in_doc.add(answer);
                }
                previous_index = j+1;
            }
        }
        int answer = line_num.size() - previous_index;
        for(int k = previous_index; k< line_num.size();k++)
        {
            terms_in_doc.add(answer);
        }


        for(int j=0;j<entity_name.size();j++)
        {
            for(int k=0;k<entity_name.size();k++)
            {
                Compare c = new Compare(entity_name.get(j), entity_name.get(k));
                Compare d = new Compare(entity_name.get(k), entity_name.get(j));
                Double sim = (c.getResult() + d.getResult()) / 2.0 ;
                if(sim > 0.95)
                {
                    if(urls.get(line_num.get(j)-1).matches(urls.get(line_num.get(k)-1)))
                    {
                        freq_in_doc.set(j, freq_in_doc.get(j) + 1);
                    }
                    else
                    {
                        num_docs.set(j, num_docs.get(j) + 1);
                    }
                }
            }
            double tf = 0.0;
            double idf = 0.0;
            tf = (freq_in_doc.get(j)*1.0) / (terms_in_doc.get(j)*1.0) ;
            idf = (total_docs*1.0) / (num_docs.get(j)*1.0);

            bw1.write(line_num.get(j) + ";" + urls.get(line_num.get(j)-1) + ";" + entity_name.get(j)+ ";"+rake_score.get(j)+";" + tf+ ";" + idf + "\n");
        }




        try
        {
            if (bw1 != null)
                bw1.close();

            if (fw1 != null)
                fw1.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

import semantics.Compare;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class relationSim
{
    public void relationSimilarity (int temp_index) throws IOException
    //public static void main (String args[]) throws  IOException
    {

        String filename = "csv_triples_ollie"+temp_index+".csv";
        //String filename = "csv_triples_ollie0.csv";

        BufferedReader br = null;
        FileReader fr = new FileReader(filename);
        br = new BufferedReader(fr);

        try
        {
            PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter("result"+temp_index+".csv")));
            //PrintWriter f = new PrintWriter(new BufferedWriter(new FileWriter("result0.csv")));

//            ArrayList<String> head_phrase = input_phrase;
//            System.out.println("------------------------------------------");
//            for(int i = 0; i < head_phrase.size(); i++)
//            {
//                System.out.println(head_phrase.get(i));
//            }
//            System.out.println("------------------------------------------");
            //String head_phrase = "algorithm";

            String line = "";

            while((line = br.readLine()) != null)
            {
                String []info = line.split(";");
                double max_sim = 0.0;

                //reading from relationSimMatch.txt for relation similarity
                FileReader fr1 = new FileReader("relationSimMatch.txt");
                BufferedReader br1 = new BufferedReader(fr1);
                String headPhrase = "";

                while((headPhrase = br1.readLine()) != null){       //TODO check the format in which string is given to compare function
//                for(int i = 0 ; i < head_phrase.size() ; i++){
//                    Compare c = new Compare(info[2], head_phrase.get(i));
//                    Compare d = new Compare(head_phrase.get(i), info[2]);
                    Compare c = new Compare(info[2], headPhrase);
                    Compare d = new Compare(headPhrase, info[2]);
                    double sim;
                    if(c.getResult() > d.getResult()){
                        sim = c.getResult();
                    }
                    else{
                        sim = d.getResult();
                    }
//                    if(sim < 0.0)
//                    {
//                        sim = 0.0;
//                    }
                    if(sim > max_sim){
                        max_sim = sim;
                    }
                        //System.out.println("Similarity:" + head_phrase + ":" + info[2] + ":" + sim);
                    /*for(int i=0;i<info.length;i++)
                    {
                        f.print(info[i] + ";");
                    }
                    f.println(sim);*/
                        //System.out.println("similarity:" + sim);

                }


                //System.out.println(line);
//                f.println(info[0]+";"+info[1]+";"+info[4]+";"+max_sim);
//                f.println(info[0]+";"+info[3]+";"+info[4]+";"+max_sim);
                    f.println(info[0] + ";" + info[2] + ";" + max_sim);

                try
                {
                    if(br1 != null)
                        br1.close();
                    if(fr1!= null)
                        fr1.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            f.close();
        }
        catch(Exception e){}
        finally
        {
            try
            {
                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();


            }
            catch(IOException e){ e.printStackTrace();}
        }
    }
}
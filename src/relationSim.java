import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import semantics.Compare;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class relationSim
{
    public void relationSimilarity (int temp_index, String input_phrase) throws IOException
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

            String head_phrase = input_phrase;
            //String head_phrase = "algorithm";

            String line = "";

            while((line = br.readLine()) != null)
            {
                //System.out.println(line);
                String []info = line.split(";");
                Compare c = new Compare(info[2], head_phrase);
                Compare d = new Compare(head_phrase, info[2]);
                double sim = 0.5*(c.getResult() + d.getResult());
                if(sim < 0.0)
                {
                    sim = 0.0;
                }
                //System.out.println("Similarity:" + head_phrase + ":" + info[2] + ":" + sim);
	 			/*for(int i=0;i<info.length;i++)
	 			{
	 				f.print(info[i] + ";");
	 			}
	 			f.println(sim);*/
                System.out.println("similarity:" + sim);
                f.println(info[0]+";"+info[1]+";"+info[4]+";"+sim);
                f.println(info[0]+";"+info[3]+";"+info[4]+";"+sim);
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
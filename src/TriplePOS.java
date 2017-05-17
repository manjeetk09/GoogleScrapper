import java.io.*;

public class TriplePOS
{
    public void triplePOS(int temp_index) throws IOException {
        //System.out.println("Hi");

        String FILENAME = "triples_POS"+temp_index+".txt";
        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter(FILENAME);
        bw = new BufferedWriter(fw);

        String command = "java -cp stanford-postagger.jar edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger -textFile crawler"+temp_index+".txt -outputFormat tsv";
        //String []command = {"java", "\"*\"","edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger","-textFile text.txt","-outputFormat tsv"};
        //String command = "ls -l -t -r";
        System.out.println("inside POS");
        try
        {
            Process proc = Runtime.getRuntime().exec(command);
            //int exitVal = proc.exitValue();
            System.out.println("command started");
            //System.out.println(exitVal + ":"+command);
            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {
                bw.write(line + "\n");

                //System.out.print(line + "\n");
            }

            proc.waitFor();
            System.out.print("ended command");

        }
        catch(Exception e){
            System.out.println("error:");
            e.printStackTrace();
        }

        try
        {
            if (bw != null)
                bw.close();

            if (fw != null)
                fw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("files closed");
    }
}


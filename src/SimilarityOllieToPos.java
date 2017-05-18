import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import semantics.Compare;
import java.lang.*;

public class SimilarityOllieToPos {

    public void similarityOllieToPosFunc() throws IOException{
        BufferedReader br = null;
        FileReader fr = null;
        fr = new FileReader("combinedOllie.csv");
        br = new BufferedReader(fr);

        BufferedReader br1 = null;
        FileReader fr1 = null;
        fr1 = new FileReader("combinedPOS.csv");
        br1 = new BufferedReader(fr1);


        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter("similarity_ollie_to_Pos.csv");
        bw = new BufferedWriter(fw);
        PrintWriter f = new PrintWriter(bw);


        try {

            //System.out.print("test started");
            String sCurrentLine;

            ArrayList<String> line_list_ollie = new ArrayList<String>();
            ArrayList<String> new_line_list_ollie = new ArrayList<String>();
            ArrayList<String> new_line_list_ollie_new = new ArrayList<String>();
            ArrayList<String> ent_list_ollie = new ArrayList<String>();
            ArrayList<String> line_list_POS = new ArrayList<String>();
            ArrayList<String> ent_list_POS = new ArrayList<String>();

            while ((sCurrentLine = br.readLine()) != null) {

                line_list_ollie.add(sCurrentLine);
            }

            while ((sCurrentLine = br1.readLine()) != null) {

                line_list_POS.add(sCurrentLine);
            }

            for(int i = 0 ; i < line_list_POS.size() ; i++){
                List<String> temp_pair = Arrays.asList(line_list_POS.get(i).split(";"));
                if(temp_pair.size() == 3){
                    ent_list_POS.add(temp_pair.get(1));
                }
            }

            for(int i = 0 ; i < line_list_ollie.size() ; i++){
                List<String> ent_list = Arrays.asList(line_list_ollie.get(i).split(";"));
                String new_line = ent_list.get(2);
                //System.out.println("new line is:: " + new_line);
                List<String> temp_list = Arrays.asList(new_line.split("_"));
                new_line = "";
                new_line = new_line + temp_list.get(0);
                for(int d = 1 ; d < temp_list.size() ; d++){
                    new_line = new_line + " " + temp_list.get(d);
                }
                //System.out.println("new line is modified:: " + new_line);

                FileWriter fw1 = new FileWriter("temp.txt");
                BufferedWriter bw1 = new BufferedWriter(fw1);
                bw1.write(new_line + "\n");
                try{
                    if(bw1!=null)
                        bw1.close();

                    if(fw1!= null)
                        fw1.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ArrayList<String> pos_out_lines = new ArrayList<String>();
                String command = "java -cp stanford-postagger.jar edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger -textFile temp.txt -outputFormat tsv";
                //String []command = {"java", "\"*\"","edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger","-textFile text.txt","-outputFormat tsv"};
                //String command = "ls -l -t -r";
                //System.out.println("inside POS");
                try
                {
                    Process proc = Runtime.getRuntime().exec(command);
                    //int exitVal = proc.exitValue();
                    //System.out.println("command started");
                    //System.out.println(exitVal + ":"+command);
                    // Read the output

                    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                    String line = "";

                    while((line = reader.readLine()) != null) {
                        //bw.write(line + "\n");
                        pos_out_lines.add(line);
                        //System.out.print("pos out is:: " + line + "\n");
                    }

                    proc.waitFor();
                    //System.out.print("ended command");

                }
                catch(Exception e){
                    //System.out.println("error:");
                    e.printStackTrace();
                }



                ArrayList<String> entities_all = new ArrayList<String>();
                int num_entities = 0;
                for (int k = 0; k < pos_out_lines.size() ; k++ ) {

                    List<String> entities = Arrays.asList(pos_out_lines.get(k).split("\\s+"));

                    if(entities.size() == 2){
                        if( entities.get(1).contains("NN") ) {
                            //System.out.println("found noun phrase");
                            String new_entity = "";
                            new_entity = new_entity + entities.get(0);
                            if( k < pos_out_lines.size() - 1){
                                List<String> entities_next = Arrays.asList(pos_out_lines.get(k+1).split("\\s+"));

                                int next_flag = 0;
                                if( entities_next.size() == 2 && entities_next.get(1).contains("NN")){ next_flag = 1;}
                                while(next_flag == 1){
                                    k++;
                                    new_entity = new_entity + "_" + entities_next.get(0);
                                    if( k < pos_out_lines.size() - 1){
                                        entities_next = Arrays.asList(pos_out_lines.get(k+1).split("\\s+"));
                                        if( entities_next.size() == 2 && entities_next.get(1).contains("NN")){ next_flag = 1;}
                                        else{next_flag = 0;}
                                    }
                                    else{
                                        break;
                                    }
                                }
                                entities_all.add(new_entity);
                                num_entities = num_entities + 1;
                            }
                            else{
                                entities_all.add(new_entity);
                                num_entities++;
                            }
                        }
                    }
                }

                for(int h = 0 ; h < entities_all.size() ; h++){
                    //System.out.print("test mid h is:: " + h);
                    List<String> new_line_entity_temp = Arrays.asList(line_list_ollie.get(i).split(";"));
                    new_line_entity_temp.set(2,entities_all.get(h));
                    String new_line_pos = "";
                    new_line_pos = new_line_pos + new_line_entity_temp.get(0);
                    for(int m = 1 ; m < new_line_entity_temp.size() ; m++){
                        new_line_pos = new_line_pos + ";" + new_line_entity_temp.get(m);
                    }
                    new_line_list_ollie_new.add(new_line_pos);
                }

            }
            //System.out.print("test mid");


            for(int i = 0 ; i < new_line_list_ollie_new.size() ; i++){
                List<String> temp_pair = Arrays.asList(new_line_list_ollie_new.get(i).split(";"));
                if(temp_pair.size() == 5){
                    ent_list_ollie.add(temp_pair.get(2));
                    //System.out.println(temp_pair.get(2));
                }
            }

            for(int i = 0 ; i < new_line_list_ollie_new.size() ; i++){
                //System.out.println(new_line_list_ollie_new.get(i));
            }




            for(int i = 0 ; i < ent_list_ollie.size() ; i++){
                for(int j = 0 ; j < ent_list_POS.size() ; j++){

                    Compare c = new Compare(ent_list_ollie.get(i),ent_list_POS.get(j));
                    double similarity_value = c.getResult();
                    Compare c1 = new Compare(ent_list_POS.get(j),ent_list_ollie.get(i));
                    double similarity_value1 = c1.getResult();
                    double sim_avg = ( similarity_value + similarity_value1 ) / 2.0;

                    if(sim_avg > 0.9){
                        new_line_list_ollie.add(new_line_list_ollie_new.get(i));
                        break;
                    }
                    else if(ent_list_ollie.get(i).contains(ent_list_POS.get((j)))){
                        new_line_list_ollie.add(new_line_list_ollie_new.get(i));
                        break;
                    }

                }
            }

            for(int i = 0 ; i < new_line_list_ollie.size() ; i++){
                f.println(new_line_list_ollie.get(i));
            }



        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

                if (br1 != null)
                    br1.close();

                if (fr1 != null)
                    fr1.close();

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }
}

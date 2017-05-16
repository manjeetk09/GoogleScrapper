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

public class SimilarityOllie
{

    public static class StringLengthListSort implements Comparator<String>{

        public int compare(String s1, String s2) {
            return s1.length() - s2.length();
        }
    }

    public void similarityollie(int temp_index) throws IOException{

        BufferedReader br = null;
        FileReader fr = null;
        fr = new FileReader("result"+temp_index+".csv");
        br = new BufferedReader(fr);


        BufferedWriter bw1 = null;
        FileWriter fw1 = null;
        fw1 = new FileWriter("result_similarity_ollie"+temp_index+".csv");
        bw1 = new BufferedWriter(fw1);
        PrintWriter f = new PrintWriter(bw1);


        try {


            String sCurrentLine;

            ArrayList<String> line_list = new ArrayList<String>();
            ArrayList<String> ent_list = new ArrayList<String>();
            ArrayList<String> url_index = new ArrayList<String>();
            //ArrayList<String> new_ent_list = new ArrayList<String>();
            ArrayList<String> conf_list = new ArrayList<String>();
            ArrayList<String> relation_list = new ArrayList<String>();
            ArrayList<String> doc_freq = new ArrayList<String>();

            int index = 0;
            int new_index = 0;

            while ((sCurrentLine = br.readLine()) != null) {

                line_list.add(sCurrentLine);
                //System.out.println(sCurrentLine);
            }

            for(int i = 0 ; i < line_list.size() ; i++){
                List<String> temp_pair = Arrays.asList(line_list.get(i).split(";"));
                if(temp_pair.size() == 4){
                    ent_list.add(temp_pair.get(1));
                    url_index.add(temp_pair.get(0));
                    conf_list.add(temp_pair.get(2));
                    relation_list.add(temp_pair.get(3));
                    index++;
                }
            }

		/*
	    StringLengthListSort ss = new StringLengthListSort();
	    Collections.sort(ent_list, ss);
	    //System.out.println(ent_list);
	    */

            //doc_freq initialise
            for(int i = 0 ; i < ent_list.size() ; i++){
                doc_freq.add(i,"1");
            }

            for(int i = 0 ; i < ent_list.size() ; i++ ){
                for(int j = i+1 ; j < ent_list.size() ; j++ ){

                    FileReader fs = new FileReader("urls"+temp_index+".txt");

                    BufferedReader br1 = new BufferedReader(fs);
                    BufferedReader br2 = new BufferedReader(fs);
                    for(int k = 1; k < Integer.parseInt(url_index.get(i)); ++k)
                        br1.readLine();
                    String line1 = br1.readLine();
                    for(int l = 1; l < Integer.parseInt(url_index.get(j)); ++l)
                        br2.readLine();
                    String line2 = br2.readLine();
                    //System.out.println(line1);
                    //System.out.println(line2);

                    Compare c = new Compare(ent_list.get(i),ent_list.get(j));
                    double similarity_value = c.getResult();
                    Compare c1 = new Compare(ent_list.get(j),ent_list.get(i));
                    double similarity_value1 = c1.getResult();
                    double sim_avg = ( similarity_value + similarity_value1 ) / 2.0;

                    try
                    {
                        if (br1 != null)
                            br1.close();

                        if (br2 != null)
                            br2.close();

                        if (fs != null)
                            fs.close();
                    }
                    catch (IOException ex) {

                        ex.printStackTrace();

                    }
                    if(ent_list.get(j).matches(":")){
                        continue;
                    }
                    if(sim_avg > 0.95){
                        if(line1!=null && line2!=null && line1.matches(line2))
                        {
                            ent_list.set(j,":");
						/*
						if(Double.parseDouble(conf_list.get(i)) < Double.parseDouble(conf_list.get(j)))
						{
							conf_list.set(i, conf_list.get(j));
						}
						if(Double.parseDouble(relation_list.get(i)) < Double.parseDouble(relation_list.get(j)))
						{
							relation_list.set(i, relation_list.get(j));
						}*/
                        }
                        else
                        {
                            doc_freq.set(j,Integer.toString(Integer.parseInt(doc_freq.get(j)) + 1));
                            ent_list.set(j,":");
						/*
						if(conf_list.get(i) < conf_list.get(j))
						{
							conf_list.set(i, conf_list.get(j));
						}
						if(relation_list.get(i) < relation_list.get(j))
						{
							relation_list.set(i, relation_list.get(j));
						}
						*/
						/*
						if(ent_list.get(i).length() > ent_list.get(j).length()){
							int len = ent_list.get(i).length() - ent_list.get(j).length();
							if( len < ent_list.get(j).length() ){
								ent_list.set(j,ent_list.get(i));	
							}
						}
						else{
							int len = ent_list.get(j).length() - ent_list.get(i).length();
							if( len < ent_list.get(i).length() ){
								ent_list.set(j,ent_list.get(i));	
							}
						}
						*/
                        }

                        //ent_list.set(i,ent_list.get(j));
                        //ent_list.set(j,":");
                    }
                    if(line1 == null || line2 == null){
                        continue;
                    }
                    else if( line1.matches(line2) ){
                        continue;
                        //ent_list.set(j,":");
                    }
                    else if(!ent_list.get(i).matches(":")){
                        //double similarity_value = similarity(ent_list.get(i),ent_list.get(j));
                        //double similarity_value1 = similarity(ent_list.get(j),ent_list.get(i));

                        if(sim_avg > 0.8 ){
                            //System.out.println("Ent1:: " + ent_list.get(i) + " Ent2:: " + ent_list.get(j));
                            if(ent_list.get(i).length() > ent_list.get(j).length()){
                                int len = ent_list.get(i).length() - ent_list.get(j).length();
                                if( len < ent_list.get(j).length() ){
                                    //ent_list.set(i,ent_list.get(j));
                                    int val = Integer.parseInt(doc_freq.get(i)) + 1;
                                    doc_freq.set(i,Integer.toString(val));
                                    int val1 = Integer.parseInt(doc_freq.get(j)) + 1;
                                    doc_freq.set(j,Integer.toString(val1));

                                }
                            }
                            else{
                                int len = ent_list.get(j).length() - ent_list.get(i).length();
                                if( len < ent_list.get(i).length() ){
                                    //ent_list.set(i,ent_list.get(j));
                                    int val = Integer.parseInt(doc_freq.get(i)) + 1;
                                    doc_freq.set(i,Integer.toString(val));
                                    int val1 = Integer.parseInt(doc_freq.get(j)) + 1;
                                    doc_freq.set(j,Integer.toString(val1));
                                }
                            }


                        }

                    }
                }
            }

		
		/*
		for(int i = 0 ; i < ent_list.size() ; i++){
			if(ent_list.get(i).matches(":")){continue;}
			for(int j = i+1 ; j < ent_list.size() ; j++ ){
				if( url_index.get(i).matches(url_index.get(j)) ){

				}
				else{
					if(ent_list.get(i).matches(ent_list.get(j))){
						int val = Integer.parseInt(doc_freq.get(i)) + 1;
						doc_freq.set(i,Integer.toString(val));
						ent_list.set(j,":");
						doc_freq.set(j,"0");
					}

				}
			}
		}
		*/

            for(int i = 0 ; i < ent_list.size() ; i++){
                //System.out.println("Entity:: " + ent_list.get(i));
                //System.out.println("Document_freq:: " + doc_freq.get(i) );
                if(ent_list.get(i).matches(":")){

                }
                else{
                    f.println(url_index.get(i) + ";" + ent_list.get(i) + ";" + conf_list.get(i) + ";" + relation_list.get(i) + ";" + doc_freq.get(i));

                }
            }

            f.close();


        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

                if (bw1 != null)
                    bw1.close();

                if (fw1 != null)
                    fw1.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
	/*
    printSimilarity("is the mother of", "mother");
    printSimilarity("types of algorithm", "type");
    printSimilarity("types of algorithm", "algorithm");
    */
    }
}
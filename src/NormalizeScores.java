import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by manjeet on 22/5/17.
 */
public class NormalizeScores {
    public void NormalizeFunc() throws IOException{

        FileReader f = new FileReader("final.csv");
        BufferedReader b = new BufferedReader(f);
        FileWriter fw = new FileWriter("final_normalize.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        ArrayList<Double> rake_score = new ArrayList<Double>();
        ArrayList<Double> tf_score = new ArrayList<Double>();
        ArrayList<Double> idf_score = new ArrayList<Double>();
        ArrayList<Double> quick_ans = new ArrayList<Double>();
        ArrayList<Double> relation_sim_score = new ArrayList<Double>();

        ArrayList<String> entity = new ArrayList<String>();
        ArrayList<String> line_num = new ArrayList<String>();
        ArrayList<String> url = new ArrayList<String>();
        ArrayList<String> template_num = new ArrayList<String>();
        String line = "";
        while((line = b.readLine()) != null){
            String[] components = line.split(";");
            template_num.add(components[0]);
            line_num.add(components[1]);
            url.add(components[2]);
            String new_ent = components[3];
            new_ent = new_ent.replaceAll(" ","_");
//            entity.add(components[3]);
            entity.add(new_ent);
            rake_score.add(Double.parseDouble(components[4]));
            tf_score.add(Double.parseDouble(components[5]));
            idf_score.add(Double.parseDouble(components[6]));
            relation_sim_score.add(Double.parseDouble(components[7]));
            quick_ans.add(Double.parseDouble(components[8]));



        }
        try{
            if(b != null)
                b.close();

            if(f != null)
                f.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        double min_rake = Collections.min(rake_score);
        double max_rake = Collections.max(rake_score);
        double min_tf = Collections.min(tf_score);
        double max_tf = Collections.max(tf_score);
        double min_idf = Collections.min(idf_score);
        double max_idf = Collections.max(idf_score);

        if(min_rake == max_rake)
        {
            min_rake = 0.0;
        }
        if(min_idf == max_idf)
        {
            min_idf = 0.0;
        }
        if(min_tf == max_tf)
        {
            min_tf = 0.0;
        }

        for(int i = 0 ; i < rake_score.size() ; i++){

            rake_score.set(i, (rake_score.get(i) - min_rake)/(max_rake-min_rake) );
            tf_score.set(i, (tf_score.get(i) - min_tf)/(max_tf-min_tf) );
            idf_score.set(i, (idf_score.get(i) - min_idf)/(max_idf-min_idf) );
            double sum = rake_score.get(i) + tf_score.get(i) + idf_score.get(i);
            bw.write( template_num.get(i) + ";" + line_num.get(i) + ";" + url.get(i) + ";" + entity.get(i) + ";" + rake_score.get(i) + ";" + tf_score.get(i) + ";" + idf_score.get(i) + ";" + sum + ";" + relation_sim_score.get(i) + ";" + quick_ans.get(i) +"\n");
        }

        try{
            if(bw != null)
                bw.close();

            if(fw != null)
                fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}

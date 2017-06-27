import java.io.*;
import java.util.*;

/**
 * Created by ashutosh on 25/6/17.
 */
public class MergeMappedUnmappedWiki {

    public static class LineSortScore implements Comparator<LineScore> {

        public int compare(LineScore s1, LineScore s2) {
            Double temp = s1.score - s2.score;
            if(temp > 0.0)
            {
                return -1;
            }
            else if(temp < 0.0)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }

    public void mergeFunc(String head) throws IOException{
        FileReader fr = new FileReader("processedMappedWiki.csv");
        BufferedReader br = new BufferedReader(fr);
        FileReader fr1 = new FileReader("processedUnmappedWiki.csv");
        BufferedReader br1 = new BufferedReader(fr1);
        FileWriter fw = new FileWriter("processedMergedWiki.csv");
        BufferedWriter bw = new BufferedWriter(fw);

        ArrayList<String> lineMapped = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null)
        {
            lineMapped.add(line);
        }
        ArrayList<String> lineUnmapped = new ArrayList<String>();
        while((line = br1.readLine()) != null)
        {
            lineUnmapped.add(line);
        }

        try{

            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
            if(br1 != null)
                br1.close();
            if(fr1 != null)
                fr1.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ArrayList<LineScore> all = new ArrayList<>();
        for(String s : lineMapped){
            String temp = "MappedWiki;"+s;
            LineScore lineScore = new LineScore();
            lineScore.line = temp;
            String[] s_split = s.split(";");
            Double score = Double.parseDouble(s_split[8]);
            lineScore.entity = s_split[3];
            lineScore.score = score;
            all.add(lineScore);
        }
        for(String s : lineUnmapped){
            String temp = "UnmappedWiki;"+s;
            LineScore lineScore = new LineScore();
            lineScore.line = temp;
            String[] s_split = s.split(";");
            Double score = Double.parseDouble(s_split[8]);
            lineScore.entity = s_split[3];
            lineScore.score = score;
            all.add(lineScore);
        }
        LineScore []arr_all = new LineScore[all.size()];
        all.toArray(arr_all);
        for(int i = 0 ; i < arr_all.length ; i++){
            for( int j = i + 1 ; j < arr_all.length ; j++){
                if(arr_all[j].entity.matches(arr_all[i].entity)){
                    if(arr_all[j].score > arr_all[i].score){
                        arr_all[i].line = arr_all[j].line;
                        arr_all[i].score = arr_all[j].score;
                        arr_all[j].score = -1;
                    }
                    else{
                        arr_all[j].score = -1;
                    }
                }
            }
        }
        ArrayList<LineScore> all_new = new ArrayList<>();
        for(int i = 0 ; i < arr_all.length ; i++){
            if(arr_all[i].score != -1){
                all_new.add(arr_all[i]);
            }
        }

        LineSortScore lineSortScore = new LineSortScore();
        Collections.sort(all_new,lineSortScore);

        for(LineScore lineScore : all_new){
            if(lineScore.entity.toLowerCase().matches(head.toLowerCase().replaceAll("_"," "))){
                continue;
            }
            bw.write(lineScore.line);
            bw.write("\n");
        }

        try{

            if(bw != null)
                bw.close();
            if(fw != null)
                fw.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

class LineScore {
    public LineScore() {
        this.description = "x";
    }

    public String line;
    public double score;
    public String entity;
    public String description;
    public double catScore;
}

import java.io.*;
import java.util.*;

/**
 * Created by ashutosh on 25/6/17.
 */
public class mergeMappedUnmappedWiki {

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

    public static void main(String args[]) throws IOException{
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
            lineScore.score = score;
            all.add(lineScore);
        }
        for(String s : lineUnmapped){
            String temp = "UnmappedWiki;"+s;
            LineScore lineScore = new LineScore();
            lineScore.line = temp;
            String[] s_split = s.split(";");
            Double score = Double.parseDouble(s_split[8]);
            lineScore.score = score;
            all.add(lineScore);
        }
        LineSortScore lineSortScore = new LineSortScore();
        Collections.sort(all,lineSortScore);

        for(LineScore lineScore : all){
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
    public String line;
    public double score;
}

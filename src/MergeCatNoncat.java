import java.io.*;
import java.util.*;

/**
 * Created by manjeet on 25/6/17.
 */
public class MergeCatNoncat {

    public class CatSortScore implements Comparator<LineScore> {

        public int compare(LineScore s1, LineScore s2) {
            Double temp = s1.catScore - s2.catScore;
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

    public void mergeCatsNoncats() throws IOException{
        FileReader fr = new FileReader("processedMergedWiki.csv");
        BufferedReader br = new BufferedReader(fr);
        FileReader fr1 = new FileReader("Category/catOutput.csv");
        BufferedReader br1 = new BufferedReader(fr1);
        FileWriter fw = new FileWriter("mergeCatNonCat.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        ArrayList<String> lineMerge = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null)
        {
            lineMerge.add(line);
        }
        ArrayList<String> lineCat = new ArrayList<String>();
        while((line = br1.readLine()) != null)
        {
            lineCat.add(line);
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
        List<LineScore> all = new ArrayList<>();
        List<String> all_string = new ArrayList<String>();
        for(String s : lineMerge){
            LineScore lineScore = new LineScore();
            lineScore.line = s;
            String[] s_split = s.split(";");
            lineScore.entity = s_split[4];
            all.add(lineScore);
            all_string.add(lineScore.entity.toLowerCase());
        }
        Set<String> set = new HashSet<String>(all_string);
        for(String s : lineCat){
            String[] s_split = s.split(";");
            String entity = s_split[2];
            if(set.contains(entity)){
                for(LineScore lineScore : all){
                    if(lineScore.entity.toLowerCase().matches(entity.toLowerCase())){
                        lineScore.line = lineScore.line + ";" + s;
                        lineScore.description = s_split[0];
                        lineScore.catScore = Double.parseDouble(s_split[1]);
                        break;
                    }
                }
            }
        }
        ArrayList<LineScore> all_cats_plural = new ArrayList<>();
        ArrayList<LineScore> all_cats_suffix = new ArrayList<>();
        ArrayList<LineScore> all_cats_match = new ArrayList<>();
        ArrayList<LineScore> all_remianing = new ArrayList<>();
        for(LineScore lineScore : all){
            if(lineScore.description.contains("plural")){
                all_cats_plural.add(lineScore);
            }
            else if(lineScore.description.contains("suffix")){
                all_cats_suffix.add(lineScore);
            }
            else if(lineScore.description.contains("Category Match")){
                all_cats_match.add(lineScore);
            }
            else {
                all_remianing.add(lineScore);
            }
        }
        MergeMappedUnmappedWiki.LineSortScore lineSortScore = new MergeMappedUnmappedWiki.LineSortScore();
        CatSortScore catSortScore = new CatSortScore();
        Collections.sort(all_cats_plural,catSortScore);
        Collections.sort(all_cats_suffix,catSortScore);
        Collections.sort(all_cats_match,catSortScore);
        Collections.sort(all_remianing,lineSortScore);
        for(LineScore lineScore : all_cats_plural){
            bw.write(lineScore.line);
            bw.write("\n");
        }
        for(LineScore lineScore : all_cats_suffix){
            bw.write(lineScore.line);
            bw.write("\n");
        }
        for(LineScore lineScore : all_cats_match){
            bw.write(lineScore.line);
            bw.write("\n");
        }
        for(LineScore lineScore : all_remianing){
            bw.write(lineScore.line);
            bw.write("\n");
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

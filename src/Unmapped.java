import net.didion.jwnl.data.Exc;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class InformationUnmap
{
    String template_number;
    String line_number;
    String url;
    String entity_name;
    double rake_score;
    double tf_score;
    double idf_score;
    double final_score;
    double relation_score;
    int quick_ans;

    public InformationUnmap(String template_number, String line_number, String url, String entity_name, double rake_score, double tf_score, double idf_score, double final_score, double relation_score, int quick_ans) {
        this.template_number = template_number;
        this.line_number = line_number;
        this.url = url;
        this.entity_name = entity_name;
        this.rake_score = rake_score;
        this.tf_score = tf_score;
        this.idf_score = idf_score;
        this.final_score = final_score;
        this.relation_score = relation_score;
        this.quick_ans = quick_ans;
    }

    public String getTemplate_number() {
        return template_number;
    }

    public void setTemplate_number(String template_number) {
        this.template_number = template_number;
    }

    public String getLine_number() {
        return line_number;
    }

    public void setLine_number(String line_number) {
        this.line_number = line_number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public double getRake_score() {
        return rake_score;
    }

    public void setRake_score(double rake_score) {
        this.rake_score = rake_score;
    }

    public double getTf_score() {
        return tf_score;
    }

    public void setTf_score(double tf_score) {
        this.tf_score = tf_score;
    }

    public double getIdf_score() {
        return idf_score;
    }

    public void setIdf_score(double idf_score) {
        this.idf_score = idf_score;
    }

    public double getFinal_score() {
        return final_score;
    }

    public void setFinal_score(double final_score) {
        this.final_score = final_score;
    }

    public double getRelation_score() {
        return relation_score;
    }

    public void setRelation_score(double relation_score) {
        this.relation_score = relation_score;
    }

    public int getQuick_ans() {
        return quick_ans;
    }

    public void setQuick_ans(int quick_ans) {
        this.quick_ans = quick_ans;
    }
}

public class Unmapped
{
    public static class RakeScoreListSort implements Comparator<InformationUnmap> {


        public int compare(InformationUnmap s1, InformationUnmap s2) {
            Double temp = s1.getFinal_score() - s2.getFinal_score();
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

    public String getTemplate(int num) throws IOException
    {
        FileReader fr = new FileReader("templates.txt");
        BufferedReader br = new BufferedReader(fr);

        int count = 0;
        String lineTemp = "";
        while((lineTemp = br.readLine()) != null)
        {
            if(count == num)
            {
                return lineTemp;
            }
            else
            {
                count = count + 1;
            }
        }
        try
        {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;


    }

    public String getLine(int temp_num, int line_num) throws IOException
    {
        FileReader fr = new FileReader("crawlerq" + temp_num + ".txt");
        BufferedReader br = new BufferedReader(fr);

        int count = 1;
        String lineTemp = "";
        while((lineTemp = br.readLine()) != null)
        {
            if(count == line_num)
            {
                return lineTemp;
            }
            else
            {
                count = count + 1;
            }
        }
        try
        {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;


    }
    public static void main (String args[]) throws IOException
    {
        FileReader f = new FileReader("notMappedInTKB.csv");
        BufferedReader b = new BufferedReader(f);

        FileWriter fw = new FileWriter("processedUnmapped.csv");
        BufferedWriter bw = new BufferedWriter(fw);

        FileWriter fw1 = new FileWriter("unmapWithWiki.csv");
        BufferedWriter bw1 = new BufferedWriter(fw1);

        ArrayList<InformationUnmap> obj = new ArrayList<InformationUnmap>();

        ArrayList<Double> rake_score = new ArrayList<Double>();
        ArrayList<Double> tf_score = new ArrayList<Double>();
        ArrayList<Double> idf_score = new ArrayList<Double>();
        ArrayList<Integer> quick_ans = new ArrayList<Integer>();
        ArrayList<Double> relation_sim_score = new ArrayList<Double>();
        ArrayList<Double> final_score = new ArrayList<Double>();

        ArrayList<String> entity = new ArrayList<String>();
        ArrayList<String> line_num = new ArrayList<String>();
        ArrayList<String> url = new ArrayList<String>();
        ArrayList<String> template_num = new ArrayList<String>();

        String line = "";
        while((line = b.readLine()) != null)
        {
            String []components = line.split(";");
            template_num.add(components[0]);
            line_num.add(components[1]);
            url.add(components[2]);
            entity.add(components[3]);
            rake_score.add(Double.parseDouble(components[4]));
            tf_score.add(Double.parseDouble(components[5]));
            idf_score.add(Double.parseDouble(components[6]));
            final_score.add(Double.parseDouble(components[7]));
            relation_sim_score.add(Double.parseDouble(components[8]));
            Double temp = Double.parseDouble(components[9]);
            quick_ans.add(temp.intValue());
        }

        //rake score cutoff
        for(int i=0; i<entity.size(); i++)
        {

            if(rake_score.get(i) < 0.5 && quick_ans.get(i)==0)
            {
                template_num.remove(i);
                line_num.remove(i);
                url.remove(i);
                entity.remove(i);
                rake_score.remove(i);
                tf_score.remove(i);
                idf_score.remove(i);
                final_score.remove(i);
                relation_sim_score.remove(i);
                quick_ans.remove(i);
                i--;
            }
            else
            {
                InformationUnmap info = new InformationUnmap(template_num.get(i), line_num.get(i),url.get(i),entity.get(i),rake_score.get(i),tf_score.get(i),idf_score.get(i),final_score.get(i),relation_sim_score.get(i),quick_ans.get(i));
                obj.add(info);
            }
        }

        //final score cutoff
        Unmapped.RakeScoreListSort ss = new Unmapped.RakeScoreListSort();
        Collections.sort(obj, ss);

        Double max_final = obj.get(0).getFinal_score();
        Double cutoff = max_final / 2.0;

//        System.out.println(cutoff);
        for(int i=0; i<obj.size(); i++)
        {
            if(obj.get(i).getFinal_score() < cutoff && obj.get(i).getQuick_ans() == 0)
            {
                obj.remove(i);
            }
        }

        //removing those that have wiki pages
        for(int i=0; i<obj.size();i++)
        {
            try
            {
                String command = "python mapWiki.py " + obj.get(i).getEntity_name();
//                String command = "echo hello";
                System.out.println(command);
                Process proc = Runtime.getRuntime().exec(command);

                BufferedReader reader2 = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                String line2 = "";
//                line2 = reader.readLine();
//                if(line2 == null){
//                    System.out.println("found null");
//                }
//                System.out.println("Hi");
                while((line2 = reader2.readLine()) != null) {
                    System.out.println("Helo");
                    System.out.println(line2);
                    if(line2.length()>3)
                    {
                        String temp = line2.substring(3,line2.length()-2).replaceAll("\\\\u2013"," ");
                        System.out.println(temp);
                        bw1.write(obj.get(i).getEntity_name() + ";" + temp);
                        obj.remove(i);
                        i--;
                        break;
                    }
                    else
                    {
                        System.out.println("null");
                    }
                }

                proc.waitFor();

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        //sorting
        int count_quick = 0;
        for(int i=0; i<obj.size(); i++)
        {
            if(obj.get(i).getQuick_ans() == 1)
            {
                InformationUnmap obj1 = obj.get(i);
                for(int j=i; j>count_quick; j--)
                {
                    obj.set(j, obj.get(j-1));
                }
                obj.set(count_quick, obj1);
                count_quick = count_quick + 1;
            }
        }

        //removing any redundancies
        for(int i=0; i<obj.size(); i++)
        {
            for(int j=i+1; j<obj.size();j++)
            {
                if(obj.get(i).getEntity_name().matches(obj.get(j).getEntity_name()))
                {
                    obj.remove(j);
                    j--;
                }
            }
        }

        //printing it back
        for(int i=0; i<obj.size();i++)
        {
            bw.write(obj.get(i).getEntity_name()+";"+obj.get(i).getFinal_score()+";"+obj.get(i).getTemplate_number()+";"+obj.get(i).getLine_number()+"\n");
//            InformationUnmap info_temp = obj.get(i);
//            bw.write(getTemplate(Integer.parseInt(info_temp.getTemplate_number())) +getLine(Integer.parseInt(info_temp.getTemplate_number()),Integer.parseInt(info_temp.getLine_number())) +";"+obj.get(i).getEntity_name()+";"+obj.get(i).getRake_score()+";"+info_temp.getTf_score()+";"+info_temp.getIdf_score()+";"+obj.get(i).getFinal_score()+";" + info_temp.getRelation_score()+";" +obj.get(i).getQuick_ans()+"\n");
        }

        //removing and storing separately those entities which have WIKI page

        try
        {
            if(b != null)
                b.close();

            if(f != null)
                f.close();

            if(bw != null)
                bw.close();

            if(fw != null)
                fw.close();

            if(bw1 != null)
                bw1.close();

            if(fw1 != null)
                fw1.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
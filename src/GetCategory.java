

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scala.util.parsing.combinator.testing.Str;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by manjeet on 6/6/17.
 */
public class GetCategory{

    private static String getUrlSource(String url) throws IOException {
        URL yahoo = new URL(url);
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder a = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            a.append(inputLine);
        in.close();

        return a.toString();
    }

    public static ArrayList<Category> getCategories(String url) throws Exception{
        //System.out.println("Start:: " + url);
        org.jsoup.nodes.Document doc = new Document("");

        if(url.contains("%"))
        {
            String temp_url = URLDecoder.decode(url,"UTF-8");
            //System.out.println("decoded::"+temp_url);
            doc = Jsoup.parse(getUrlSource(temp_url));
        }
        else
        {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
        }

        //System.out.println(doc.html());
        Elements results = doc.select("div.mw-normal-catlinks > ul");
        ArrayList<Category> catList = new ArrayList<Category>();
        for(Element e1 : results){
            Elements children = e1.children();
            for(Element e : children){
                //System.out.println("child:: "+ e.text());
                String catHref = "https://en.wikipedia.org";
                Element a = e.child(0);
                String temp = a.attr("href");
                catHref = catHref + temp ;
                String name = e.text();
                Category cat = new Category(name, catHref);

                // Get category hierarchy

                /*System.out.println("Cat:: " + cat.getName() + " url:: " + cat.getUrl() );

                int flag = 0;
                for(Category c_all : allCats){
                    if(c_all.getName().matches(cat.getName())){
                        flag = 1;
                        break;
                    }
                }
                if(flag == 1){
                    continue;
                }
                allCats.add(cat);
                ArrayList<Category> sub_cats = getCategories(cat.getUrl());
                int i = 0;
                if(sub_cats != null){
                    for(Category cat_dup : sub_cats){
                        if(allCats.contains(cat_dup)){
                            sub_cats.remove(cat_dup);
                        }
                        else{
                            i++;
                        }
                    }
                    if(i == 0){
                        System.out.println("EndNull:: " + url);
                        return null;
                    }
                    cat.setCategories(sub_cats);
                }
                cat.setCategories(sub_cats);
                */

                catList.add(cat);
            }
        }
        //System.out.println("End:: " + url);
        return catList;

    }

    public static ArrayList<Category> getCats (String title) throws Exception
    {
        String page_name = title;
        page_name = page_name.replaceAll(" ","_");
        String searchURL = "https://en.wikipedia.org/wiki/";
        searchURL = searchURL + page_name;

        ArrayList<Category> cats = getCategories(searchURL);

        for(Category c : cats){
            c.setCategories(getCategories(c.getUrl()));
            c.setlevelCategories(1);
            for(Category c1 : c.getCategories()){
                c1.setCategories(getCategories(c1.getUrl()));
                c1.setlevelCategories(2);
            }
//                System.out.println("Cat:: " + c.getName() + " url:: " + c.getUrl() );
        }

        ArrayList<Category> allCats = new ArrayList<Category>();

        for (Category c : cats){
            allCats.add(c);
            //System.out.println(c.getName() + " :: " + c.getLevel() + " :: " + c.getCategories());
            for(Category c1 : c.getCategories()){
                allCats.add(c1);
                //System.out.println("    " + c1.getName() + " :: " + c1.getLevel() + " :: " + c1.getCategories());
                for (Category c2 : c1.getCategories()){
                    allCats.add(c2);
                    //System.out.println("        " + c2.getName() + " :: " + c2.getLevel() + " :: " + c2.getCategories());
                }
            }
        }
        return allCats;
    }

    public ArrayList<String> catgoryScore (String head_phrase, String answer) throws Exception{
        String head = head_phrase;
        String head_s = head + "s";
        String head_es = head + "es";
        String head_ies = head.substring(0,head.length() - 1) + "ies";
        ArrayList<String> res = new ArrayList<String>();
        double sum = 0;
//        PorterStemmer p = new PorterStemmer();
//        String stemmed = p.stem(head);
//        System.out.println("stem: " +
        String[] head_split = answer.split(" ");
        ArrayList<Category> c1 = getCats(answer);
        ArrayList<Category> c2 = getCats(head_phrase);
        for (Category c : c1){
            if(c.getName().matches(head_s) || c.getName().matches((head_es)) || c.getName().matches(head) || c.getName().matches(head_ies) ){
                System.out.println("Found plural:: " + c.getName());
                if(c.getLevel() == 0){
                    System.out.println("plural at level 0");
                    if(c.getName().contains(head)){
                        System.out.println("Found typeof");
                        res.add("plural" + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                        sum++;
                    }
                }
                else{
                    System.out.println("plural at level " + c.getLevel());
                    if(c.getName().contains(head)){
                        System.out.println("Found typeof");
                        res.add("plural" + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                        sum = sum + (1.0/c.getLevel());
                    }
                }

            }

            List<String> c_split = Arrays.asList(c.getName().split(" "));

            if( c_split.size() < head_split.length ){
                int count = 0;
                for(int i = 0 ; i < c_split.size() ; i++){
//                    if(!c_split.contains(head_split[i])){
//                        flag = 1;
//                        break;
//                    }
                    for(String s : head_split){
                        if(s.contains(c_split.get(i))){
                            count++;
                            break;
                        }
                    }
                }
                if(count == c_split.size()){
                    System.out.println("Found suffix :: " + c.getName());
                    if(head.contains(c.getName())){
                        System.out.println("Found typeof");
                        sum = sum + 1;
                        res.add("suffix/prefix" + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                    }
                    else{
                        res.add("suffix/prefix/W" + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                    }
                }
            }
            for(Category c2_temp : c2){
                if(c2_temp.getName() == c.getName()){
                    //if(c2_temp.getLevel() < c.getLevel()){
                    res.add("Category Match" + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel() + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                    //}
                }
            }
        }

        for(Category c2_temp : c2){
            List<String> c_split = Arrays.asList(c2_temp.getName().split(" "));
            if( c_split.size() < head_split.length ){
                int count = 0;
                for(int i = 0 ; i < c_split.size() ; i++){
//                    if(!c_split.contains(head_split[i])){
//                        flag = 1;
//                        break;
//                    }
                    for(String s : head_split){
                        if(c_split.get(i).contains(s)){
                            count++;
                            break;
                        }
                    }
                }
                if(count == head_split.length){
                    System.out.println("Found suffix_2 :: " + c2_temp.getName());
                    if(head.contains(c2_temp.getName())){
                        System.out.println("Found typeof_2");
                        res.add("suffix/prefix" + ";" + answer + ";" + head + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                        sum = sum + 1;
                    }
                    else{
                        res.add("suffix/prefix/W" + ";" + answer + ";" + head + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                    }
                }
            }
        }

        return res;

    }

    public static void main(String args[]) throws IOException {

        //for proxy
        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");

        FileWriter fw = new FileWriter("category_validated.csv");
        BufferedWriter bw = new BufferedWriter(fw);

        FileReader fr = new FileReader("catInput.csv");
        BufferedReader br = new BufferedReader(fr);
        ArrayList<String> head_ent_pair = new ArrayList<String>();
        String line_url = "";
        while((line_url = br.readLine()) != null)
        {
            head_ent_pair.add(line_url);
        }

        try{

            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        GetCategory getCategory = new GetCategory();

        for(String s : head_ent_pair){
            String[] pairList = s.split(";");
            String head = pairList[1];
            String answer = pairList[0];
            try{
                ArrayList<String> output = getCategory.catgoryScore(head,answer);
                Thread.sleep(3000);
                //System.out.println(score);
                for(String t : output){
                    bw.write(t);
                    bw.write("\n");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
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

class Category{
    private String name;
    private String url;
    private ArrayList<Category> categories;
    private int level;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Category(String name, String url, ArrayList<Category> categories) {
        this.name = name;
        this.url = url;
        this.categories = categories;
        this.level = 0;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public Category(String name, String url) {
        this.name = name;
        this.url = url;
        this.categories = null;
        this.level = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setlevelCategories(int level){
        for(Category c : this.categories){
            c.setLevel(level);
        }
    }
}


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scala.util.parsing.combinator.testing.Str;
import semantics.Compare;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetCategory{

    ArrayList<String> res1 = new ArrayList<String>();

    private Document sendRequest(String url) {
        Document doc = null;
        try {
            Connection connect = Jsoup.connect(url);
            connect.request().followRedirects(false);
            URI u = new URI(url);
            doc = connect.url(new URI(u.getScheme(), u.getUserInfo(), u.getHost(), u.getPort(), URLDecoder.decode(u.getPath(), "UTF-8"), u.getQuery(), u.getFragment()).toURL()).get();
            if (connect.response().statusCode() == 301 && connect.response().header("Location") != null) {
                return sendRequest(connect.response().header("Location"));
            }
            return doc;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return doc;
    }

    private String getUrlSource(String url) throws IOException {
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

    public ArrayList<Category> getCategories(String url) throws Exception{
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
            //doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
            doc = sendRequest(url);
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

    public ArrayList<Category> getCats (String title) throws Exception
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

    public ArrayList<String> catgoryScore (String head_phrase1, String answer1) throws Exception{
        String head_phrase = head_phrase1.toLowerCase();
        String answer = answer1.toLowerCase();
        String head_plural = head_phrase.replaceAll("^[a-zA-Z0-9_]","");
        String head = head_phrase;
        String head_s = head_plural + "s";
        String head_es = head_plural + "es";
        String head_ies = head_plural.substring(0,head.length() - 1) + "ies";
        ArrayList<String> res = new ArrayList<String>();
        double sum = 0;
//        PorterStemmer p = new PorterStemmer();
//        String stemmed = p.stem(head);
//        System.out.println("stem: " +
        String[] ans_ent_split = answer.split(" ");
        ArrayList<Category> c1 = getCats(answer1);
        ArrayList<Category> c2 = getCats(head_phrase1);

        List<String> c_split_t = Arrays.asList(head.split(" "));

        if( c_split_t.size() < ans_ent_split.length || true ){
            int count = 0;
            int score_compare = 0;
            for(int i = 0 ; i < c_split_t.size() ; i++){
                int k = 0;
                for(String s : ans_ent_split){
                    if( ( s.contains(c_split_t.get(i)) && (s.length()-c_split_t.get(i).length() <= s.length()/2) ) || ( c_split_t.get(i).contains(s) && (c_split_t.get(i).length() - s.length() <= c_split_t.get(i).length()/2) ) ){
                        score_compare = score_compare + k - i;
                        count++;
                        break;
                    }
                    k++;
                }
            }
            if(count == c_split_t.size()){
                if(answer.contains(head)){
                    System.out.println("Found typeof");
                    sum = sum + 1;
                    res.add("1suffix/prefix" + ";" + score_compare + ";" + answer + ";" + head);
                }
                else{
                    res.add("1suffix/prefix/W" + ";" + score_compare + ";" + answer + ";" + head);
                }
            }
            else if(count >= ans_ent_split.length/2 && count > 0){
                double score_t = count/ans_ent_split.length;
                res.add("1suffix/prefix/W2" + ";" + score_t + ";" + answer + ";" + head);
            }
        }
        for (Category c : c1){
            if(c.getName().toLowerCase().matches(head_s) || c.getName().toLowerCase().matches((head_es)) || c.getName().toLowerCase().matches(head) || c.getName().toLowerCase().matches(head_ies) ){
                System.out.println("Found plural:: " + c.getName());
                if(c.getLevel() == 0){
                    System.out.println("plural at level 0");
                    if(c.getName().toLowerCase().contains(head_phrase)){
                        System.out.println("Found typeof");
                        res.add("plural" + ";" + 1 + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                        sum++;
                    }
                }
                else{
                    System.out.println("plural at level " + c.getLevel());
                    if(c.getName().toLowerCase().contains(head_phrase)){
                        System.out.println("Found typeof");
                        double score = 1.0/(2*c.getLevel());
                        res.add("plural" + ";" + score + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                        sum = sum + (1.0/c.getLevel());
                    }
                }

            }

            List<String> c_split = Arrays.asList(c.getName().split(" "));

            if( c_split.size() < ans_ent_split.length ){
                int count = 0;
                int score_compare = 0;
                for(int i = 0 ; i < c_split.size() ; i++){
                    int k = 0;
                    for(String s : ans_ent_split){
                        if( ( s.contains(c_split.get(i)) && (s.length()-c_split.get(i).length() <= s.length()/2) ) || ( c_split.get(i).contains(s) && (c_split.get(i).length() - s.length() <= c_split.get(i).length()/2) ) ){
                            score_compare = score_compare + k - i;
                            count++;
                            break;
                        }
                        k++;
                    }
                }
                if(count == c_split.size()){
                    System.out.println("Found suffix :: " + c.getName());
                    if(answer.contains(c.getName())){
                        System.out.println("Found typeof");
                        sum = sum + 1;
                        res1.add("2suffix/prefix" + ";" + score_compare + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                    }
                    else{
                        res1.add("2suffix/prefix/W" + ";" + score_compare + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                    }
                }
                else if(count >= ans_ent_split.length/2 && count > 0){
                    double score_t = count/ans_ent_split.length;
                    res1.add("2suffix/prefix/W2" + ";" + score_t + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel());
                }
            }
            for(Category c2_temp : c2){
                if(c2_temp.getName().matches(c.getName())){
                    //if(c2_temp.getLevel() < c.getLevel()){
                    double score;
                    if(c.getLevel() == 2 && c2_temp.getLevel() == 0){
                        score = 1.0;
                    }
                    else if(c.getLevel() == 1 && c2_temp.getLevel() == 0){
                        score = 8.0/9.0;
                    }
                    else if(c.getLevel() == 2 && c2_temp.getLevel() == 1){
                        score = 7.0/9.0;
                    }
                    else if(c.getLevel() == 0 && c2_temp.getLevel() == 0){
                        score = 6.0/9.0;
                    }
                    else if(c.getLevel() == 1 && c2_temp.getLevel() == 1){
                        score = 5.0/9.0;
                    }
                    else if(c.getLevel() == 2 && c2_temp.getLevel() == 2){
                        score = 4.0/9.0;
                    }
                    else if(c.getLevel() == 0 && c2_temp.getLevel() == 1){
                        score = 3.0/9.0;
                    }
                    else if(c.getLevel() == 0 && c2_temp.getLevel() == 2){
                        score = 2.0/9.0;
                    }
                    else if(c.getLevel() == 1 && c2_temp.getLevel() == 2){
                        score = 1.0/9.0;
                    }
                    else{
                        score = 0;
                    }
                    res.add("Category Match" + ";" + score + ";" + answer + ";" + head + ";" + c.getName() + ";" + c.getLevel() + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                    //}
                }
            }
        }

        for(Category c2_temp : c2){
            List<String> c_split = Arrays.asList(c2_temp.getName().split(" "));
            if( c_split.size() < ans_ent_split.length ){
                int count = 0;
                int score_compare = 0;
                for(int i = 0 ; i < c_split.size() ; i++){
                    int k = 0;
                    for(String s : ans_ent_split){
                        if( ( s.contains(c_split.get(i)) && (s.length()-c_split.get(i).length() <= s.length()/2) ) || ( c_split.get(i).contains(s) && (c_split.get(i).length() - s.length() <= c_split.get(i).length()/2) ) ){
                            score_compare = score_compare + k - i;
                            count++;
                            break;
                        }
                        k++;
                    }
                }
                if(count == c_split.size()){
                    System.out.println("Found suffix_2 :: " + c2_temp.getName());
                    if(answer.contains(c2_temp.getName())){
                        System.out.println("Found typeof_2");
                        res1.add("suffix/prefix" + ";" + score_compare + ";" + answer + ";" + head + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                        sum = sum + 1;
                    }
                    else{
                        res1.add("suffix/prefix/W" + ";" + score_compare + ";" + answer + ";" + head + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                    }
                }
                else if(count >= ans_ent_split.length/2 && count > 0){
                    double score_t = count/ans_ent_split.length;
                    res1.add("suffix/prefix/W2" + ";" + score_t + ";" + answer + ";" + head + ";" + c2_temp.getName() + ";" + c2_temp.getLevel());
                }
            }
        }

        return res;

    }

//    public void runCat(int i) throws IOException {

    public void runCat() throws IOException {
        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");

//        FileWriter fw = new FileWriter("Category/category_intersection" + i + ".csv");
//        BufferedWriter bw = new BufferedWriter(fw);
//        FileWriter fw1 = new FileWriter("Category/category_plural" + i + ".csv");
//        BufferedWriter bw1 = new BufferedWriter(fw1);
//        FileWriter fw2 = new FileWriter("Category/category_suffix_prefix" + i + ".csv");
//        BufferedWriter bw2 = new BufferedWriter(fw2);
//        FileWriter fw3 = new FileWriter("Category/final_intersection" + i + ".csv");
//        BufferedWriter bw3 = new BufferedWriter(fw3);
//
//        FileReader fr = new FileReader("Category/catInput" + i + ".csv");
//        BufferedReader br = new BufferedReader(fr);

        FileWriter fw = new FileWriter("Category/category_intersection.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        FileWriter fw1 = new FileWriter("Category/category_plural.csv");
        BufferedWriter bw1 = new BufferedWriter(fw1);
        FileWriter fw2 = new FileWriter("Category/category_suffix_prefix.csv");
        BufferedWriter bw2 = new BufferedWriter(fw2);
        FileWriter fw3 = new FileWriter("Category/final_intersection.csv");
        BufferedWriter bw3 = new BufferedWriter(fw3);
        FileWriter fw4 = new FileWriter("Category/new_triples.csv");
        BufferedWriter bw4 = new BufferedWriter(fw4);
        FileWriter fw5 = new FileWriter("Category/catOutput.csv");
        BufferedWriter bw5 = new BufferedWriter(fw5);

        FileReader fr = new FileReader("Category/catInput.csv");
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
                int k = 0;
                boolean flag_done_plural = false;
                double best_score = 0;
                //int index_of_best_score = -1;
                String write_best = new String();
                for(String t : output){
                    if(t.substring(0,6).contains("plural")){
                        bw1.write(t);
                        bw1.write("\n");
                        String[] line_split = t.split(";");
                        Double score = Double.parseDouble(line_split[1]);
                        if(score >= best_score){
                            best_score = score;
                            write_best = t;
                        }
                        flag_done_plural = true;
                    }
                    k++;
                }
                if(flag_done_plural == true){
                    bw5.write(write_best);
                    bw5.write("\n");
                }
                k = 0;
                best_score = 0;
                write_best = "";
                boolean flag_done_suffix = false;
                for(String t : output) {
                    if (t.substring(0, 7).contains("suffix")) {
                        bw2.write(t);
                        bw2.write("\n");
                        String[] line_split = t.split(";");
                        Double score = Double.parseDouble(line_split[1]);
                        if(score >= best_score){
                            best_score = score;
                            write_best = t;
                        }
                        flag_done_suffix = true;
                    }
                    k++;
                }
                if(flag_done_plural == false && flag_done_suffix == true){
                    bw5.write(write_best);
                    bw5.write("\n");
                }
                k = 0;
                best_score = 0;
                write_best = "";
                boolean flag_done_catMatch = false;
                double max_score_intersection = 0;
                int indexOfMaxScore = -1;
                for(String t : output){
                    if(t.substring(0,14).contains("Category Match")){
                        //System.out.println(t);
                        int secondIndex = t.indexOf(';', t.indexOf(';')+1);
                        String temp_score = t.substring(15,secondIndex);
                        //System.out.println(temp_score);
                        Double score = Double.parseDouble(temp_score);
                        if(score >= max_score_intersection){
                            max_score_intersection = score;
                            indexOfMaxScore = k;
                        }
                        bw.write(t);
                        bw.write("\n");
                        flag_done_catMatch = true;
                    }
                    k++;
                }
                if(indexOfMaxScore != -1){
                    System.out.println(indexOfMaxScore + " :: " + output.get(indexOfMaxScore));
                    bw3.write(output.get(indexOfMaxScore));
                    bw3.write("\n");
                    if(flag_done_plural == false && flag_done_suffix == false && flag_done_catMatch == true && max_score_intersection > 7.0/9.0){
                        bw5.write(output.get(indexOfMaxScore));
                        bw5.write("\n");
                    }
                }
                for(String new_triple : res1){
                    bw4.write(new_triple);
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
            if(bw1 != null)
                bw1.close();
            if(fw1 != null)
                fw1.close();
            if(bw2 != null)
                bw2.close();
            if(fw2 != null)
                fw2.close();
            if(bw3 != null)
                bw3.close();
            if(fw3 != null)
                fw3.close();
            if(bw4 != null)
                bw4.close();
            if(fw4 != null)
                fw4.close();
            if(bw5 != null)
                bw5.close();
            if(fw5 != null)
                fw5.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

}

class Category{
    private String name;
    private String cleanName;
    private String url;
    private ArrayList<Category> categories;
    private int level;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Category(String name, String url, ArrayList<Category> categories) {
        this.name = name;
        this.cleanName = name.replaceAll("^[a-zA-Z0-9_]","");
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

    public String getCleanName() {
        return cleanName;
    }

    public void setCleanName(String cleanName) {
        this.cleanName = cleanName;
    }
}
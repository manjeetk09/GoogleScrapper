
import opennlp.tools.stemmer.PorterStemmer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by manjeet on 6/6/17.
 */
public class GetCategory {

    public static ArrayList<Category> getCategories(String url) throws Exception{
        //System.out.println("Start:: " + url);
        org.jsoup.nodes.Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
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

    public int catgoryScore (String head_phrase, String answer) throws Exception{
        String head = head_phrase;
        String head_s = head + "s";
        String head_es = head + "es";
        int sum = 0;
//        PorterStemmer p = new PorterStemmer();
//        String stemmed = p.stem(head);
//        System.out.println("stem: " +
        String[] head_split = head.split(" ");
        ArrayList<Category> c1 = getCats(answer);
//        ArrayList<Category> c2 = getCats("Debuggers");
        for (Category c : c1){
            if(c.getName().matches(head_s) || c.getName().matches((head_es)) || c.getName().matches(head)){
                System.out.println("Found plural:: " + c.getName());
                if(c.getLevel() == 0){
                    System.out.println("plural at level 0");
                    if(c.getName().contains(head)){
                        System.out.println("Found typeof");
                        sum++;
                    }
                }

            }
            List<String> c_split = Arrays.asList(c.getName().split(" "));

            if( c_split.size() > head_split.length ){
                int count = 0;
                for(int i = 0 ; i < head_split.length ; i++){
//                    if(!c_split.contains(head_split[i])){
//                        flag = 1;
//                        break;
//                    }
                    for(String s : c_split){
                        if(s.contains(head_split[i])){
                            count++;
                            break;
                        }
                    }
                }
                if(count == head_split.length){
                    System.out.println("Found suffix :: " + c.getName());
                    if(c.getName().contains(head)){
                        System.out.println("Found typeof");
                        sum = sum + 1;
                    }
                }
            }
        }

        return sum;

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
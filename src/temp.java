import org.json.simple.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class temp
{
    public static final String GOOGLE_SEARCH_URL = "http://suggestqueries.google.com/complete/search?client=firefox&hl=en&q=";

    public static void main (String args[]) throws IOException
    {
        //number of templates
        int temp_num = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the search term.");
        String searchTerm = scanner.nextLine();
        System.out.println("Please enter the head phrase.");
        String phrase = scanner.nextLine();
        System.out.println("Please enter the number of searches.");
        int search = scanner.nextInt();
        JSONObject obj = new JSONObject();
        String searchURL = GOOGLE_SEARCH_URL +searchTerm;
        //System.out.println(searchURL);

        //for proxy
        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");

        org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        Elements results = doc.getElementsByTag("body");

        ArrayList<String> templates_final = new ArrayList<String>();

        for(Element result : results)
        {
            String answer = result.text();

            //System.out.println(answer);
            answer = answer.substring(1);
            //System.out.println(answer);
            answer = answer.substring(answer.indexOf("[")+1);
            //System.out.println(answer);
            String []templates = answer.split(",");
            temp_num = templates.length;
            //System.out.println(templates.length);
            for(int i=0;i<templates.length - 1;i++)
            {
                templates[i] = templates[i].substring(1,(templates[i].length() - 1));
                //System.out.println(templates[i]);
            }
            templates[templates.length - 1] = templates[templates.length -1].substring(1,(templates[templates.length -1].length()-3));
            //System.out.println(templates[templates.length-1]);
            for(int i=0; i<templates.length;i++)
            {
                templates_final.add(templates[i]);
            }
            //System.out.println(templates_final);
        }



        for(int i=0;i<templates_final.size();i++)
        {
            //String temp_1 = "" + i;
            //templates_final.set(i,temp_1);
            GoogleSearchJava googleSearchJava = new GoogleSearchJava();
            googleSearchJava.googleSearch(templates_final.get(i), search, i);

            //via Ollie
            parseOllie parseollie = new parseOllie();
            parseollie.parserOllie(i);
            relationSim relationsim = new relationSim();
            relationsim.relationSimilarity(i, phrase);
            SimilarityOllie similarityOllie = new SimilarityOllie();
            similarityOllie.similarityollie(i, search);
            System.out.println("i is:: " + i);

            //via POSTagger
            TriplePOS triplepos = new TriplePOS();
            triplepos.triplePOS(i);
            //System.out.println("triples created");
            parsePOS parsepos = new parsePOS();
            parsepos.parseFunc(i);
            similarity sim = new similarity();
            sim.similarityFunc(i, search);
            //System.out.println("similarity ended");

        }

        //CombineOllie combineOllie = new CombineOllie();
        //combineOllie.combineOllieFunc(temp_num);
        CombinePOS combinePOS = new CombinePOS();
        combinePOS.combinePOSFunc(temp_num);


    }
}

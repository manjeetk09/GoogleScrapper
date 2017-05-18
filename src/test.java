import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class test
{
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

    public static void main (String args[]) throws IOException
    {

        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");

        String searchURL = "https://en.wikipedia.org/wiki/MLPACK_(C%2B%2B_library)";
        Document doc = new Document("");
        doc = Jsoup.parse(getUrlSource(searchURL));


//        Elements e = doc.select("div");
//        for(int i = 0 ; i < e.size() ; i++){
//            Element p = e.get(i);
//        }

        Elements e = doc.select("p");
        Elements u = doc.select("ul");

        for(Element e1 : e){
            System.out.println(e1.siblingIndex());
            System.out.println(e1.nodeName());
        }

        for(Element u1 : u){
            Elements child_ul = u1.children();

            //System.out.println(u1.text());
            for(int h = 0 ; h < child_ul.size() ; h++){
                Element li = child_ul.get(h);
                System.out.println(li.text());
            }
        }



    }
}

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

        String str = "table 1: is about";
        System.out.println( "str:: " + str);
        String new_str = str.replaceAll(":"," ");
        System.out.println( "str:: " + new_str);

//        System.setProperty("http.proxyHost", "10.10.78.22");
//        System.setProperty("http.proxyPort", "3128");
//        System.setProperty("https.proxyHost", "10.10.78.22");
//        System.setProperty("https.proxyPort", "3128");
//
//        String searchURL = "https://www.google.com/search?q=example of artificial intelligence project&num=8";
//
//        Document doc = new Document("");
//        try{
//            String temp_url = URLDecoder.decode(searchURL,"UTF-8");
//            String html = getUrlSource(searchURL);
//            System.out.println(html);
//            String newURL = String.format(searchURL, URLEncoder.encode(searchTerm,"UTF-8"));
//            doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
//            //  doc = sendRequest(searchURL);
//            System.out.println(doc.html());
//        }catch(Exception e){
//            e.printStackTrace();
//        }


//        Elements e = doc.select("div");
//        for(int i = 0 ; i < e.size() ; i++){
//            Element p = e.get(i);
//        }

//        Elements e = doc.select("p");
//        Elements u = doc.select("ul");
//        String html = "<div class=\"content-text right-align bold-font\">foo</div>";
//        Document document = Jsoup.parse(html);
//        Element q = doc.select("div.g").first();
//        q.select("h3").remove();
//        q.select("cite").remove();
//        System.out.println(q.select("h3 > a").attr("href"));
//        System.out.println("----------------------");
//        System.out.println(q.text());
//        for(Element ele : q){
//            //String class1 = q.attr("class");
//            //System.out.println(class1);
//            Elements eles = ele.siblingElements();
//            for(Element yele : eles){
//                System.out.println("entered children");
//                Elements eless = yele.children();
//                for(Element e1 : eless){
//                    System.out.println(e1.attr("class"));
//                    System.out.println(e1.attr("href"));
//                    System.out.println(e1.html());
//                }
//            }
//            System.out.println(eles.size());
//        }
//        //Element q1 =
//        System.out.println(q.size());
//        System.out.println(q.text());

//        for(Element e1 : e){
//            System.out.println(e1.siblingIndex());
//            System.out.println(e1.nodeName());
//        }
//
//        for(Element u1 : u){
//            Elements child_ul = u1.children();
//
//            //System.out.println(u1.text());
//            for(int h = 0 ; h < child_ul.size() ; h++){
//                Element li = child_ul.get(h);
//                System.out.println(li.text());
//            }
//        }



    }
}

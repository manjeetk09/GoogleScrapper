import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;

public class ayush {
    public static void main(String args[]) throws IOException
    {
        //for proxy
        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");

        System.out.println("Enter the search term:");
        Scanner scanner = new Scanner(System.in);
        String search_Term = scanner.nextLine();
        String searchURL = "http://economictimes.indiatimes.com/topic/" + search_Term;
//        String searchURL= "https://www.reddit.com/r/compsci/";
        System.out.println(searchURL);
        String ecoURL = "http://economictimes.indiatimes.com";
        Document doc = new Document("");
        try{
//            String newURL = String.format(searchURL, URLEncoder.encode(searchTerm,"UTF-8"));
            doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
            //  doc = sendRequest(searchURL);
        }catch(Exception e){
            e.printStackTrace();
        }
//        System.out.println(doc.toString());
        Elements results = doc.select("div.topicstry > a");
        for(Element result : results)
        {
            String link = result.attr("href");
            System.out.println(ecoURL + link);
            Document doc1 = new Document("");
            try{
                //String newURL = String.format(searchURL, URLEncoder.encode(searchTerm,"UTF-8"));
                doc1 = Jsoup.connect(ecoURL+link).userAgent("Mozilla/5.0").get();
                //  doc = sendRequest(searchURL);
            }catch(Exception e){
                e.printStackTrace();
            }
            Elements authors = doc1.select("div.byline");
            for(Element author : authors)
            {
                System.out.print(author.text() + "::");
            }
            Elements authors_link = doc1.select("div.#auSec1");
            for(Element author : authors)
            {
                System.out.println(author);
//                System.out.println(author.attr("href"));
            }
        }
    }
}

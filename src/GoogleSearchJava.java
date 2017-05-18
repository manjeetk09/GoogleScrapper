//package com.journaldev.jsoup;

import net.didion.jwnl.data.Exc;
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

public class GoogleSearchJava {


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

    private static Document sendRequest(String url) {
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

    //these store the links and span
    public  ArrayList<String> link_data = new ArrayList<String>();
    public  ArrayList<String> span_data = new ArrayList<String>();

    public  String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    //public  void main(String[] args) throws IOException
    public void googleSearch(String input_url, int input_num, int temp_index) throws IOException
    {

        //text file for output
        String FILENAME = "crawler" + temp_index + ".txt";
        //String file_name = "triples_ollie.txt";
        String file_url = "urls" + temp_index + ".txt";
        String file_para = "para" + temp_index + ".txt";
        String li_file = "li" + temp_index + ".csv";

        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter(FILENAME);
        bw = new BufferedWriter(fw);

        /*
        BufferedWriter bw1 = null;
        FileWriter fw1 = null;
        fw1 = new FileWriter(file_name);
        bw1 = new BufferedWriter(fw1);
        */

        BufferedWriter bw2 = null;
        FileWriter fw2 = null;
        fw2 = new FileWriter(file_url);
        bw2 = new BufferedWriter(fw2);

        BufferedWriter bw3 = null;
        FileWriter fw3 = null;
        fw3 = new FileWriter(file_para);
        bw3 = new BufferedWriter(fw3);

        BufferedWriter bw4 = null;
        FileWriter fw4 = null;
        fw4 = new FileWriter(li_file);
        bw4 = new BufferedWriter(fw4);

        //Taking search term input from console
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Please enter the search term.");
        //String searchTerm = scanner.nextLine();
        String searchTerm = input_url;
        //System.out.println("Please enter the number of results:");
        //int num = scanner.nextInt();
        int num = input_num;
        //scanner.close();


        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm + "&num="+num;
        //without proper User-Agent, we will get 403 error
        System.out.println(searchURL);

        //for proxy
//        System.setProperty("http.proxyHost", "10.10.78.22");
//        System.setProperty("http.proxyPort", "3128");
//        System.setProperty("https.proxyHost", "10.10.78.22");
//        System.setProperty("https.proxyPort", "3128");
//        System.setProperty("http.proxyHost", "41.231.120.118"); // set proxy server
//        System.setProperty("http.proxyPort", "8888");  //set proxy port

        Document doc = new Document("");
        try{
            //String newURL = String.format(searchURL, URLEncoder.encode(searchTerm,"UTF-8"));
            doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
            //  doc = sendRequest(searchURL);
        }catch(Exception e){
            e.printStackTrace();
        }

        //Document doc0 = Jsoup.connect("https://wordpress.com/read/blogs/110825788/posts/1601").userAgent("Mozilla/5.0").get();

        //below will print HTML data, save it to a file and open in browser to compare

        //System.out.println(doc.html());



        //If google search results HTML change the <h3 class="r" to <h3 class="r1"
        //we need to change below accordingly
        Elements results = doc.select("h3.r > a");
        Elements results_1 = doc.select("span.st");

        for (Element result : results) {
            String linkHref = result.attr("href");
            System.out.println("check:: " + linkHref);
            String linkText = result.text();

            link_data.add(linkHref.substring(7, linkHref.indexOf("&")));

            System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(7, linkHref.indexOf("&")));

        }

        for (Element result_1: results_1) {
            span_data.add(result_1.text());
            System.out.println("Span:" + result_1.text());
        }

        //now interation for individual searches

        for(int i=0;i<link_data.size();i++)
        {
            if(link_data.get(i).charAt(0)!='h')
            {
                link_data.remove(i);
            }
        }

        System.out.println("Sizes:" + span_data.size() + ":" + link_data.size());



        for(int i=0; i<link_data.size();i++)
        {
            System.out.println(i);
            System.out.println("link:" + link_data.get(i));
            System.out.println("span:" + span_data.get(i)+":"+span_data.get(i).length());

            if(span_data.get(i).length() <= 0)
            {
                continue;
            }

            /*
            if(link_data.get(i).contains("%")){
                String temp = link_data.get(i);
                System.out.println(temp);
                int z = temp.indexOf("%");
                link_data.set(i,link_data.get(i).substring(0,z+1) + link_data.get(i).substring(z+3,link_data.get(i).length()));
                System.out.println(link_data.get(i));
                //continue;
            }
            */
            if(link_data.get(i).contains("pdf") || link_data.get(i).contains("wordpress") || link_data.get(i).contains("slideshare"))
            {
                System.out.println("HIGH!");
                continue;
            }


            //System.out.println("size::" + span_data.size());
            //if(span_data.get(i).length()>=1)
            {

                //filter span data
                System.out.println("dying:" + i);
                /*
                if(i == span_data.size()-1)
                {
                    System.out.println(span_data.get(i));
                }
                */
                /*
                if(span_data.get(i) == null)
                {
                    System.out.println("continued");
                    continue;
                }
                */
                String span_temp = span_data.get(i);
                List<String> span_data_1 = Arrays.asList(span_temp.split("\\.\\.\\."));

                /*for(String span_print : span_data_1)
                {
                    System.out.println(span_print);
                }*/

                //search
//                URI url_temp;
//                try
//                {
//                    url_temp = new URI(linkHref.substring(7, linkHref.indexOf("&")));
//                    URL url_temp1 = new URI(url_temp.getScheme(),url_temp.getUserInfo(),url_temp.getHost(),url_temp.getPort(),URLDecoder.decode(url_temp.getPath(),"UTF-8"), url_temp.getQuery(), url_temp.getFragment()).toURL();
//                }
//                catch(Exception e)
//                {
//                    e.printStackTrace();
//                }


                Document doc_1 = new Document("");
                try{
                    //doc_1 = Jsoup.connect(link_data.get(i)).userAgent("Mozilla/5.0").get();
                    if(link_data.get(i).contains("%"))
                    {
                        String temp_url = URLDecoder.decode(link_data.get(i),"UTF-8");
                        //System.out.println("decoded::"+temp_url);
                        doc_1 = Jsoup.parse(getUrlSource(temp_url));
                    }
                    else
                    {
                        doc_1 = sendRequest(link_data.get(i));
                    }



                    Thread.sleep(4000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(doc_1 == null)
                {
                    continue;
                }
                Elements para = doc_1.select("p");

                Elements ul = doc_1.select("ul");

                for(String span_data_temp : span_data_1) {

                    //System.out.println("Span::" + span_data_temp);

                    int flag = 0;

                    for (Element para_temp : para) {

                        String para_text = para_temp.text();
                        para_text = para_text.replaceAll("\\[(.*?)\\]", "");


                        /*if(i==1 && flag==0)
                        {
                            System.out.println(para_text);
                            flag = 1;
                        }*/
                        //System.out.println(para_text);
                        if ((para_text.replaceAll("[^a-zA-Z0-9.]" , "" )).contains(span_data_temp.replaceAll("[^a-zA-Z0-9.]" , "" )) || (span_data_temp.replaceAll("[^a-zA-Z0-9.]" , "" ).contains(para_text.replaceAll("[^a-zA-Z0-9.]" , "" )))) {

                            List<String> lines = Arrays.asList(para_text.split("\\."));

                            try {

                                //String content = "This is the content to write into file\n";


                                for (String lines_itr : lines) {
                                    //System.out.println("Done:: " + lines_itr);
                                    /*
                                    if(lines_itr.contains(";"))
                                    {
                                        System.out.println(lines_itr);
                                    }*/
                                    lines_itr = lines_itr.replaceAll("[^a-zA-Z0-9,]"," ");
                                    /*
                                    if(lines_itr.contains(";"))
                                    {
                                        System.out.println(lines_itr);
                                    }
                                    */
                                    bw.write(lines_itr);
                                    bw.write(".");
                                    bw.write("\n");
                                    bw2.write(link_data.get(i));
                                    bw2.write("\n");
                                    bw3.write(para_text);
                                    bw3.write("\n");
                                }
                                System.out.println("Done Writing");

                            } catch (IOException e) {

                                e.printStackTrace();

                            }

                            for(Element ul1 : ul){
                                int index = ul1.siblingIndex();
                                int index_1 = para_temp.siblingIndex();
                                Node parent_a = ul1.parentNode();
                                Node parent_b = para_temp.parentNode();

                                //System.out.println("parent_a:" + parent_a.nodeName());
                                //System.out.println("parent_b:" + parent_b.nodeName());
                                //System.out.println("list match found");
                                if(index == index_1 + 1)
                                {
                                    //System.out.println("inside first if");
                                    if(parent_a.hasSameValue(parent_b)){
                                        //System.out.println("inside second if");
                                        Elements child_ul = ul1.children();

                                        //System.out.println(u1.text());
                                        for(int h = 0 ; h < child_ul.size() ; h++){
                                            Element li = child_ul.get(h);
                                            //System.out.println(li.text());
                                            String line_li = li.text();

                                            //System.out.println(line_li);

                                            bw.write(line_li);
                                            bw.write(".");
                                            bw.write("\n");
                                            bw2.write(link_data.get(i));
                                            bw2.write("\n");
                                            bw3.write(para_text);
                                            bw3.write("\n");
                                            bw4.write(line_li);
                                            bw4.write("\n");
                                        }
                                    }
                                }

                            }

                        }
                    }
                }
            }
            /*else
            {
                bw.write("\n");
                bw2.write(link_data.get(i));
                bw2.write("\n");
                bw3.write("\n");
            }*/
        }
        try {

            if (bw != null)
                bw.close();

            if (fw != null)
                fw.close();

            if (bw2 != null)
                bw2.close();

            if (fw2 != null)
                fw2.close();

            if (bw3 != null)
                bw3.close();

            if (fw3 != null)
                fw3.close();

            if(bw4 != null)
                bw4.close();

            if(fw4 != null)
                fw4.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

        //entity extraction from Ollie

        try
        {
            String file_name = "triples_ollie" + temp_index + ".txt";
            BufferedWriter bw1 = null;
            FileWriter fw1 = new FileWriter(file_name);
            bw1 = new BufferedWriter(fw1);

            String command = "java -Xmx512m -jar ollie-app-latest.jar crawler" + temp_index + ".txt";
            //String command = "echo 'hello'";

            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {

                bw1.write(line);
                bw1.write("\n");
                //System.out.print(line + "\n");
            }

            proc.waitFor();

            try
            {
                if (bw1 != null)
                    bw1.close();

                if (fw1 != null)
                    fw1.close();
            }
            catch(Exception e){e.printStackTrace();}

        }
        catch(Exception e){}


        //entity extraction from posTagger

        // run the following command
        /*
        String command = "java -cp \"*\" edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger -textFile crawler.txt -outputFormat tsv -outputFile triples_post.tag\n";

        try
        {
            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {

                bw1.write(line);
                bw1.write("\n");
                //System.out.print(line + "\n");
            }

            proc.waitFor();

        }
        catch(Exception e){}
    */

    }

}
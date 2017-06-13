//package com.journaldev.jsoup;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import semantics.Compare;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RelationScrapper {

    public  ArrayList<String> link_data = new ArrayList<String>();

    public  String GOOGLE_SEARCH_URL = "https://www.google.com/search";

    public String getUrlSource(String url) throws IOException {
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

    public Document sendRequest(String url) {
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

    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (Character.toLowerCase(c1) == Character.toLowerCase(c2)) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[len1][len2];
    }

    public boolean isRelation (String relation) throws IOException
    {
        FileReader frr = new FileReader("relations.txt");
        BufferedReader brr = new BufferedReader(frr);

        String lineT = "";

        while((lineT = brr.readLine()) != null)
        {
            String line = lineT.replaceAll("_", " ");
            if(minDistance(relation, line) < 5)
            {
                try
                {
                    if(brr!=null)
                        brr.close();
                    if(frr!=null)
                        frr.close();
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }
                return  true;
            }
        }

        try
        {
            if(brr!=null)
                brr.close();
            if(frr!=null)
                frr.close();
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }
        return false;
    }

    public void relationFunc (String searchTerm, int temp_index, int num) throws IOException
    {
        //arguments : String searchTerm, int temp_index, int num

        String filename = "scrapper" + temp_index + ".txt";
        String file_rel = "scrapperq" + temp_index + ".txt";
//        String file_noRel = "noRelation.txt";
        String file_url = "urlsScrap" + temp_index + ".txt";
//        String file_para = "paraScrap" + temp_index + ".txt";
        String file_date = "timeDateScrap" + temp_index + ".txt";

        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter(filename);
        bw = new BufferedWriter(fw);

        BufferedWriter bw1 = null;
        FileWriter fw1 = null;
        fw1 = new FileWriter(file_rel);
        bw1 = new BufferedWriter(fw1);

        BufferedWriter bw2 = null;
        FileWriter fw2 = null;
        fw2 = new FileWriter(file_url);
        bw2 = new BufferedWriter(fw2);

//        BufferedWriter bw3 = null;
//        FileWriter fw3 = null;
//        fw3 = new FileWriter(file_para);
//        bw3 = new BufferedWriter(fw3);

        BufferedWriter bw4 = null;
        FileWriter fw4 = null;
        fw4 = new FileWriter(file_date);
        bw4 = new BufferedWriter(fw4);

        String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm + "&num="+num;

        //for proxy
        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");
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
//        Elements results_1 = doc.select("span.st");
//        Elements results_2 = doc.select("div.kp-blk");
//
//        Elements results_2_a = doc.select("div.kp-blk > a");

        for (Element result : results) {
            String linkHref = result.attr("href");
            System.out.println("check:: " + linkHref);
            String linkText = result.text();

            link_data.add(linkHref.substring(7, linkHref.indexOf("&")));

            System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(7, linkHref.indexOf("&")));

        }
//
//        for (Element result_1: results_1) {
//            span_data.add(result_1.text());
//            System.out.println("Span:" + result_1.text());
//        }

        //now interation for individual searches

        for(int i=0;i<link_data.size();i++)
        {
            if(link_data.get(i).charAt(0)!='h')
            {
                link_data.remove(i);
            }
        }

//        System.out.println("Sizes:" + span_data.size() + ":" + link_data.size());



        for(int i=0; i<link_data.size();i++)
        {
            if(link_data.get(i).contains("pdf") || link_data.get(i).contains("wordpress") || link_data.get(i).contains("slideshare"))
            {
                System.out.println("HIGH!");
                continue;
            }

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

                for (Element para_temp : para) {
                    Element para_parent = para_temp.parent();
//                        System.out.println("para_text:: " + para_temp.text());
                    int para_index = para_temp.siblingIndex();
                    String para_text = para_temp.text();

                    para_text = para_text.replaceAll("\\[(.*?)\\]", "");
                    ArrayList<Element> ul_appended_para = new ArrayList<Element>();
                    for(Element ul1 : ul){
                        int index = ul1.siblingIndex();
                        int index_1 = para_temp.siblingIndex();
//                            System.out.println("para_index:: " + index_1 + "::" + index );
                        Node parent_a = ul1.parentNode();
                        Node parent_b = para_temp.parentNode();

                        //System.out.println("parent_a:" + parent_a.nodeName());
                        //System.out.println("parent_b:" + parent_b.nodeName());
                        //System.out.println("list match found");

                        if(parent_a == null){
                            continue;
                        }
                        if(index == index_1 + 1 || index == index_1 + 2)
                        {

                            if(parent_a.hasSameValue(parent_b)) {
                                Elements child_ul = ul1.children();
                                ul_appended_para.add(ul1);
//                                System.out.println(child_ul.text());
                                for(int h = 0 ; h < child_ul.size() ; h++) {
                                    Element li = child_ul.get(h);
                                    //System.out.println(li.text());
                                    String line_li = li.text();
                                    para_text =  para_text.concat(" " + line_li + ".");
                                }
                            }
                        }
                    }

//                        System.out.println("appended para_ul:: "+para_text);

                    //System.out.println("inside first if");
                    //System.out.println("inside second if");


                    /////////////////////////////



                    /*if(i==1 && flag==0)
                    {
                        System.out.println(para_text);
                        flag = 1;
                    }*/
                    //System.out.println(para_text);
                    if(para_text.length() <= 2) {
                        continue;
                    }
//                    if ( check_contain(para_text,span_data_temp) || check_contain(span_data_temp,para_text) ) {

                        for(Element e_ul : ul_appended_para){

                            for(Element e_u : ul){
                                if(e_u.hasSameValue(e_ul)){
                                    e_u.remove();
                                }
                            }
                        }

                        List<String> lines = Arrays.asList(para_text.split("[.:]+"));

                        for (String lines_itr : lines)
                        {

                            lines_itr = lines_itr.replaceAll("-","_").replaceAll("\\(.*?\\) ?", "");
                            lines_itr = lines_itr.replaceAll("[^a-zA-Z0-9,_]"," ");

//                            System.out.println(lines_itr + "::");

                            FileWriter fwr = new FileWriter("tempR.txt");
                            BufferedWriter bwr = new BufferedWriter(fwr);

                            bwr.write(lines_itr + "\n");
//                            System.out.println(lines_itr + ":");
//                            Date date = new Date();

                            try
                            {
                                if(bwr!=null)
                                    bwr.close();
                                if(fwr!=null)
                                    fwr.close();
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }

                            try
                            {
                                String command = "java -Xmx512m -jar ollie-app-latest.jar tempR.txt";

                                Process proc = Runtime.getRuntime().exec(command);

                                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                                String line = "";

                                while((line = reader.readLine()) != null) {
//                                    System.out.println(lines_itr + ":" + line);
                                    if(line.contains("No extractions found."))
                                    {
                                        Date date = new Date();
                                        bw.write("1;" + lines_itr + "\n");
                                        bw2.write(link_data.get(i) + "\n");
                                        bw4.write(date.toString() + "\n");
                                    }
                                    else if(line.charAt(0)=='0' && line.charAt(1)=='.' && Character.isDigit(line.charAt(2)))
                                    {
                                        String temp = line.substring(line.indexOf(';')+2);
                                        String rel = temp.substring(0,temp.indexOf(';'));
                                        if(isRelation(rel))
                                        {
                                            Date date = new Date();
                                            bw1.write(lines_itr + "\n");
                                            bw.write("1;" + lines_itr + "\n");
                                            bw2.write(link_data.get(i) + "\n");
                                            bw4.write(date.toString() + "\n");

                                        }
                                    }
                                }

                                proc.waitFor();

                            }
                            catch(Exception e){}


                        }



//                    }
                }

                Elements ul1 = doc_1.select("ul");

                for(Element u : ul1) {
                    String ul_text = u.text();
                    ul_text = ul_text.replaceAll("\\[(.*?)\\]", "");
//
                    Elements child_ul = u.children();
//
//                        //System.out.println(u1.text());
                    for (int h = 0; h < child_ul.size(); h++) {
                        Element li = child_ul.get(h);
//                            //System.out.println(li.text());
                        String line_li = li.text();
                        line_li = line_li.replaceAll("-","_").replaceAll("\\(.*?\\) ?", "");
                        line_li = line_li.replaceAll("[^a-zA-Z0-9,_]"," ");

                        FileWriter fwr = new FileWriter("tempR.txt");
                        BufferedWriter bwr = new BufferedWriter(fwr);

                        bwr.write(line_li + "\n");
//                            System.out.println(lines_itr + ":");
//                            Date date = new Date();

                        try
                        {
                            if(bwr!=null)
                                bwr.close();
                            if(fwr!=null)
                                fwr.close();
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                        try
                        {
                            String command = "java -Xmx512m -jar ollie-app-latest.jar tempR.txt";

                            Process proc = Runtime.getRuntime().exec(command);

                            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

                            String line = "";

                            while((line = reader.readLine()) != null) {
//                                    System.out.println(lines_itr + ":" + line);
                                if(line.contains("No extractions found."))
                                {
                                    Date date = new Date();
                                    bw.write("1;" + line_li + "\n");
                                    bw2.write(link_data.get(i) + "\n");
                                    bw4.write(date.toString() + "\n");
                                }
                                else if(line.charAt(0)=='0' && line.charAt(1)=='.' && Character.isDigit(line.charAt(2)))
                                {
                                    String temp = line.substring(line.indexOf(';')+2);
                                    String rel = temp.substring(0,temp.indexOf(';'));
                                    if(isRelation(rel))
                                    {
                                        Date date = new Date();
                                        bw1.write(line_li + "\n");
                                        bw.write("1;" + line_li + "\n");
                                        bw2.write(link_data.get(i) + "\n");
                                        bw4.write(date.toString() + "\n");

                                    }
                                }
                            }

                            proc.waitFor();

                        }
                        catch(Exception e){}
                    }
                }


            }
        try {

            if (bw != null)
                bw.close();

            if (fw != null)
                fw.close();

            if (bw1 != null)
                bw1.close();

            if (fw1 != null)
                fw1.close();

            if (bw2 != null)
                bw2.close();

            if (fw2 != null)
                fw2.close();

//            if (bw3 != null)
//                bw3.close();
//
//            if (fw3 != null)
//                fw3.close();

            if(bw4 != null)
                bw4.close();

            if(fw4 != null)
                fw4.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

        //entity extraction from Ollie
//
        try
        {
            String file_name = "triples_ollie" + temp_index + ".txt";
            BufferedWriter bww = null;
            FileWriter fww = new FileWriter(file_name);
            bw1 = new BufferedWriter(fw1);

            String command = "java -Xmx512m -jar ollie-app-latest.jar scrapperq" + temp_index + ".txt";
            //String command = "echo 'hello'";

            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {

//                String line_temp = line.replaceAll(":"," ");
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


    }

}
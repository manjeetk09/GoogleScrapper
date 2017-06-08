import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class temp
{
    public static final String GOOGLE_SEARCH_URL = "http://suggestqueries.google.com/complete/search?client=firefox&hl=en&q=";

    public static void main (String args[]) throws IOException
    {

        String filename = "templates.txt";
        BufferedWriter bw = null;
        FileWriter fw = null;
        fw = new FileWriter(filename);
        bw = new BufferedWriter(fw);

        //number of templates
        int temp_num = 0;
        ArrayList<String> head_phrase_list = new ArrayList<String>();
        ArrayList<String> query_term_list = new ArrayList<String>();
        //ArrayList<String>
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the search term.");
        String searchTerm = scanner.nextLine();

//        int num_query_terms = scanner.nextInt();
//        System.out.println("Please enter the list of query terms (one in each line)");
        System.out.println("Please enter the main entity terms (as given in TKB).");
        Scanner scanner1 = new Scanner(System.in);
        String head_entity = scanner1.nextLine();
//        for(int i = 0 ; i < num_query_terms ;i++){
//            String new_head = scanner1.nextLine();
//            query_term_list.add(new_head);
////            System.out.println(new_head + " " + query_term_list.size());
//        }

        System.out.println("Please enter the number of head phrase.");
        int num_head = scanner.nextInt();
        System.out.println("Please enter the list of head phrase (one in each line)");
        Scanner scanner2 = new Scanner(System.in);
        for(int i = 0 ; i < num_head ;i++){
            String new_head = scanner2.nextLine();
            head_phrase_list.add(new_head);
            //System.out.println(new_head + " " + head_phrase_list.size());
        }
        //System.out.println(head_phrase_list);
        System.out.println("Please enter the number of searches.");
        int search = scanner.nextInt();
        JSONObject obj = new JSONObject();
        String searchURL = GOOGLE_SEARCH_URL +searchTerm;
        //System.out.println(searchURL);

        //for proxy
//        System.setProperty("http.proxyHost", "10.10.78.22");
//        System.setProperty("http.proxyPort", "3128");
//        System.setProperty("https.proxyHost", "10.10.78.22");
//        System.setProperty("https.proxyPort", "3128");

        org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();

        Elements results = doc.getElementsByTag("body");

        ArrayList<String> templates_final = new ArrayList<String>();
        int flag = 1;
        for(Element result : results)
        {
            String answer = result.text();

            //System.out.println(answer);
            answer = answer.substring(1);
            //System.out.println(answer);
            answer = answer.substring(answer.indexOf("[")+1);
            //System.out.println(answer);
            if(!answer.contains(",")){
                flag = 0;
                break;
            }
            String []templates = answer.split(",");
            temp_num = templates.length;
            //System.out.println("length is:: " + templates.length);

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
        if(flag == 0){
            bw.write(searchTerm + "\n");
            boolean is_quick_ans_present = false;
            GoogleSearchJava googleSearchJava = new GoogleSearchJava();
            is_quick_ans_present = googleSearchJava.googleSearch(searchTerm, search, 0, head_phrase_list);
            System.out.println("found quick answer:: " + is_quick_ans_present);

            //via Ollie
            parseOllie parseollie = new parseOllie();
            parseollie.parserOllie(0);
            relationSim relationsim = new relationSim();
            relationsim.relationSimilarity(0, head_phrase_list);
//            SimilarityOllie similarityOllie = new SimilarityOllie();
//            similarityOllie.similarityollie(0, search);
            //System.out.println("i is:: " + 0);

            //via POSTagger
//            TriplePOS triplepos = new TriplePOS();
//            triplepos.triplePOS(0);
//            //System.out.println("triples created");
//            parsePOS parsepos = new parsePOS();
//            parsepos.parseFunc(0);
//            similarity sim = new similarity();
//            sim.similarityFunc(0, search);

            //RAKE
            TripleRake triplerake = new TripleRake();
            triplerake.tripleRake(0);
            temp_num = 1;
        }
        else{
            for(int i=0;i<templates_final.size();i++)
            {
                bw.write(templates_final.get(i) + "\n");
                boolean is_quick_ans_present = false;
                //String temp_1 = "" + i;
                //templates_final.set(i,temp_1);
                GoogleSearchJava googleSearchJava = new GoogleSearchJava();
                is_quick_ans_present = googleSearchJava.googleSearch(templates_final.get(i), search, i, head_phrase_list);
                System.out.println("found quick answer:: " + is_quick_ans_present);

                //via Ollie
                parseOllie parseollie = new parseOllie();
                parseollie.parserOllie(i);
                relationSim relationsim = new relationSim();
                relationsim.relationSimilarity(i, head_phrase_list);
//                SimilarityOllie similarityOllie = new SimilarityOllie();
//                similarityOllie.similarityollie(i, search);
                //System.out.println("i is:: " + i);

                //via POSTagger
//                TriplePOS triplepos = new TriplePOS();
//                triplepos.triplePOS(i);
//                //System.out.println("triples created");
//                parsePOS parsepos = new parsePOS();
//                parsepos.parseFunc(i);
//                similarity sim = new similarity();
//                sim.similarityFunc(i, search);
                //System.out.println("similarity ended");

                //RAKE
                TripleRake triplerake = new TripleRake();
                triplerake.tripleRake(i);


             }
        }

        try
        {
            if(bw!=null)
                bw.close();
            if(fw!=null)
                fw.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

//        CombineOllie combineOllie = new CombineOllie();
//        combineOllie.combineOllieFunc(temp_num);
//        CombinePOS combinePOS = new CombinePOS();
//        combinePOS.combinePOSFunc(temp_num);

//        SimilarityOllieToPos similarityOllieToPos = new SimilarityOllieToPos();
//        similarityOllieToPos.similarityOllieToPosFunc();
        CombineOllieRake combineollierake = new CombineOllieRake();
        combineollierake.combineOllieRake(temp_num);

        NormalizeScores normalizeScores = new NormalizeScores();
        normalizeScores.NormalizeFunc();

        test_map test_map_ins = new test_map();
        test_map_ins.test_map_func(head_entity);

        Mapped mapped = new Mapped();
        mapped.mappedFunc(head_entity);

        Unmapped unmapped = new Unmapped();
        unmapped.unmappedFunc(head_entity);

        Path path = new Path();
        path.pathFunc(head_entity);

        System.out.println("num of templates:: " + temp_num);
    }
}

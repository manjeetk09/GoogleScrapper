import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class test
{
    public static void main (String args[]) throws Exception
    {

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the search:");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Enter the number of searches:");
//        int num = scanner.nextInt();
//        int temp_index = 0;

//        RelationScrapper relationScrapper = new RelationScrapper();
////        relationScrapper.relationFunc(searchTerm, temp_index, num);
//        if(relationScrapper.isRelation("some debugger offer"))
//        {
//            System.out.println("success");
//        }
//        else
//        {
//            System.out.println("fail");
//        }

        wikiSegregate wikiSegregate = new wikiSegregate();
        wikiSegregate.wikiFunc("hash_function","Hash function");

//        try
//        {
//             String t = "sdjklfgbvsdlhkg";
//            String command = "mapWikiNew.py";
//            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("/home/manjeet/anaconda2/bin/python2.7", command, t));
//            Process proc = pb.start();
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//
//            String line = "";
//
//            while((line = reader.readLine()) != null) {
//                System.out.println("line::");
//                if(line.length() > 4)
//                {
//                    // System.out.println(line);
//                    String temp = line.replaceAll("<WikipediaPage \'","");
//                    String temp1 = temp.replaceAll("\'>","");
//                    // System.out.println(temp1);
//                    String ent_temp = temp1.replaceAll(" " , "_");
////                        ArrayList<String> ent_cat = get_categories(get_cats(ent_temp));
////                        String matched_cats = map_categories(head_cat, ent_cat);
////                        int num_cats = 0;
////                        if(matched_cats.length() < 2){
////                            num_cats = 0;
////                        }
////                        else{
////                            num_cats = matched_cats.split("::").length;
////                        }
////                    bw2.write(component[0]+";"+component[1]+";"+component[2]+";"+temp1+";"+component[3]+";"+component[4]+";"+component[5]+";"+component[6]+";"+component[7]+";"+component[8]+";"+component[9]+";"+component[10]+ "\n");
//                }
//                else
//                {
//                     System.out.println("null");
////                    bw3.write(component[0]+";"+component[1]+";"+component[2]+";"+component[3]+";"+component[4]+";"+component[5]+";"+component[6]+";"+component[7]+";"+component[8]+";"+component[9]+";"+component[10]+"\n");
//                }
//            }
//            System.out.println("out");
//
//            proc.waitFor();
//
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
    }
}

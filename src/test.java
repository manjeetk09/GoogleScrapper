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
    public static void main (String args[]) throws IOException
    {
        String t = "relational_model";
        try
        {
            String command = "python mapWiki.py " + t;

            Process proc = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {
                if(line.length()>3)
                {
                    String temp = line.substring(3,line.length()-2).replaceAll("\\\\u2013"," ");
                    System.out.println(temp);
                }
                else
                {
                    System.out.println("null");
                }
            }

            proc.waitFor();

        }
        catch(Exception e){}
    }
}

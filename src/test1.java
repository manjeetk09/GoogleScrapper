import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by manjeet on 6/6/17.
 */
public class test1 {
    public static void main(String args[]) throws IOException{

        try {
//            String command = "echo 'hey'";
            String command = "mapWiki.py";
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList("/home/manjeet/anaconda2/bin/python2.7", command, "debugger"));
//            Process proc = Runtime.getRuntime().exec(command);
            Process proc = pb.start();

            // Read the output
//            InputStream is = proc.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader reader = new BufferedReader(isr);

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";

            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            proc.waitFor();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
//        GetCategory getCategory = new GetCategory();
//        String head = "Database model";
//        String answer = "Flat file database";
//        try{
//            int score = getCategory.catgoryScore(head,answer);
//            System.out.println(score);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }

    }
}

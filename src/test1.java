import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by manjeet on 6/6/17.
 */
public class test1 {
    public static void main(String args[]) throws IOException{

        try {
            String command = "python mapWiki.py debugger";
            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

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

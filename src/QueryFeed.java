import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class QueryFeed
{
    public static void main(String args[]) throws IOException
    {
        //if you want to do manually
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please enter the search term.");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Please enter the main entity terms (as given in TKB).");
//        Scanner scanner1 = new Scanner(System.in);
//        String head_entity = scanner1.nextLine();
//        System.out.println("Please enter the number of searches.");
//        int search = scanner.nextInt();
//        temp obj = new temp();
//        obj.tempFunc(searchTerm, head_entity, search);

        FileReader fr = new FileReader("queryInput.csv");
        BufferedReader br = new BufferedReader(fr);

        String line = "";

        while((line=br.readLine()) != null)
        {
            String []comp = line.split(";");
            temp obj = new temp();
            obj.tempFunc(comp[0],comp[1],Integer.parseInt(comp[2]));
        }

        try
        {
            if(br!=null)
                br.close();
            if(fr!=null)
                fr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

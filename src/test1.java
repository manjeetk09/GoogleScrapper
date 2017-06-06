import java.io.IOException;

/**
 * Created by manjeet on 6/6/17.
 */
public class test1 {
    public static void main(String args[]) throws IOException{

        GetCategory getCategory = new GetCategory();
        String head = "Database model";
        String answer = "Flat file database";
        try{
            int score = getCategory.catgoryScore(head,answer);
            System.out.println(score);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

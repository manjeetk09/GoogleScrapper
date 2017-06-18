import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ashutosh on 18/6/17.
 */
public class testCategory {
    public static void main(String args[]) throws IOException {
        GetCategory getCategory = new GetCategory();
        for(int i = 0 ; i < 5 ; i++){
            System.out.println("Template:: " + i);
            getCategory.runCat(i);

        }

    }
}
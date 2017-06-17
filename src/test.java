import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class test
{
    public static void main (String args[]) throws IOException
    {

//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the search:");
//        String searchTerm = scanner.nextLine();
//        System.out.println("Enter the number of searches:");
//        int num = scanner.nextInt();
//        int temp_index = 0;

        RelationScrapper relationScrapper = new RelationScrapper();
//        relationScrapper.relationFunc(searchTerm, temp_index, num);
        if(relationScrapper.isRelation("some debugger offer"))
        {
            System.out.println("success");
        }
        else
        {
            System.out.println("fail");
        }

    }
}

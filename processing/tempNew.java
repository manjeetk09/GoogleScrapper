import java.io.*;
import java.util.*;

public class tempNew
{
	public static void main (String args[])
	{
		String temp = "algebra 'algebriac function' math is fun 'app' ";
		String[] arr = temp.split("'([^']+)'");
		for(int i = 0 ; i < arr.length ; i++){
			System.out.println(arr[i]);
		}
		
	// 	try
	// 	{
	// 		// String t = "singular_point";
	// 		String command = "python mapWikiNew.py " + t;

	// 		Process proc = Runtime.getRuntime().exec(command);

	// 		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

	// 		String line = "";

	// 		while((line = reader.readLine()) != null) {
	// 			if(line.length() > 4)
	// 			{
	// 				System.out.println(line);
	// 				String temp = line.replaceAll("<WikipediaPage \'","");
	// 				String temp1 = temp.replaceAll("\'>","");
	// 				System.out.println(temp1);	
	// 			}
	// 			else
	// 			{
	// 				System.out.println("null");
	// 			}
	// 		}

	// 		proc.waitFor();

	// 	}
	// 	catch(Exception e)
	// 	{
	// 		e.printStackTrace();
	// 	}
	}
}
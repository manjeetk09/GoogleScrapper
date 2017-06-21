import java.io.*;
import java.util.*;

public class tempNew
{
	public static void main (String args[]) throws IOException
	{
		String filename_output = "wikiRelation.csv";
		FileWriter fw = new FileWriter(filename_output);
		BufferedWriter bw = new BufferedWriter(fw);

		Scanner scanner = new Scanner(System.in);
		System.out.println("please enter the number of templates");
		String num = scanner.nextLine();
		for(int i=0; i<Integer.parseInt(num); i++)
		{
			String filename = "csv_triples_ollie" + i + ".csv";
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while((line = br.readLine()) != null)
			{
				String []compo = line.split(";");
				int flag = 0;
				try
				{					
					// String t = "singular_point";
					String command = "python mapWikiNew.py " + compo[1];

					Process proc = Runtime.getRuntime().exec(command);

					BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

					String line1 = "";

					while((line1 = reader.readLine()) != null) {
						if(line1.length() > 4)
						{
							flag = 1;
						}
						else
						{
							// flag = 0;
						}
					}

					proc.waitFor();

					command = "python mapWikiNew.py " + compo[3];

					proc = Runtime.getRuntime().exec(command);

					BufferedReader reader1 = new BufferedReader(new InputStreamReader(proc.getInputStream()));

					line1 = "";

					while((line1 = reader1.readLine()) != null) {
						if(line1.length() > 4)
						{
							flag = 1;
						}
						else
						{
							// flag = 0;
						}
					}

					proc.waitFor();

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				if(flag == 1)
				{
					bw.write(line + "\n");
				}

			}

			try
			{
				if(br!=null)
					br.close();
				if(fr!=null)
					fr.close();
			}	
			catch(Exception e)
			{
				e.printStackTrace();
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
	}
}
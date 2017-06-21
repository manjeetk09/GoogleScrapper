import java.io.*;
import java.util.*;

public class testRelation
{
	public static String getEntity(String startEnt, String entity) throws IOException
	{
		FileReader fr = new FileReader("processedTeKnowbase.tsv");
		BufferedReader br = new BufferedReader(fr);

		String line = "";
		while((line = br.readLine()) != null)
		{
			String []compo = line.split("\t");
			if(compo[0].matches(startEnt))
			{
				if(compo[2].matches(entity))
				{
					return compo[1];
					// System.out.println(line);
					// break;
				}
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
		return null;
	}

	public static ArrayList<String> getPath(String begin, String end, int c) throws IOException
	{
		ArrayList<String> path = new ArrayList<String>();
		if(c > 0)
		{
			String resu = getEntity(begin, end);
			if(resu != null)
			{
				path.add(begin);
				path.add(resu);
				path.add(end);
				return path;
			}
			else
			{
				FileReader fr1 = new FileReader("processedTeKnowbase.tsv");
				BufferedReader br1 = new BufferedReader(fr1);

				String line1 = "";
				while((line1 = br1.readLine()) != null)
				{
					String []compos = line1.split("\t");
					if(compos[0].matches(begin))
					{
						ArrayList<String> temp = new ArrayList<String>();
						int t = c-1;
						temp = getPath(compos[2], end, t);
						if(temp.size() > 1)
						{
							path.add(begin);
							path.add(compos[1]);
							for(int i=0; i<temp.size();i++)
							{
								path.add(temp.get(i));
							}
							return path;
						}	
					}
				}


				try
				{
					if(br1!=null)
						br1.close();
					if(fr1!=null)
						fr1.close();
					return path;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}

				
			}
		}
		else
		{
			return path;
		}
		return path;
	}

	public static void main (String args[]) throws IOException
	{
		FileReader fr2 = new FileReader("processedMapped.csv");
		BufferedReader br2 = new BufferedReader(fr2);

		FileWriter fw = new FileWriter("processedMappedPathRelation.csv");
		BufferedWriter bw = new BufferedWriter(fw);
		// String begin = "j_sharp";
		System.out.println("Enter the head entity:");
		Scanner scanner = new Scanner(System.in);
		String end = scanner.nextLine();

		String line2 = "";
		while((line2 = br2.readLine()) != null)
		{
			String []compon = line2.split(";");
			ArrayList<String> result = new ArrayList<String>();
			result = getPath(compon[3], end, 6);
			bw.write(line2);
			if(result.size() > 0)
			{
				for(int i=0; i<result.size();i++)
				{
					bw.write(";"+result.get(i));
					// System.out.println(result.get(i));
				}
			}
			bw.write("\n");
		}

		try
		{
			if(br2!=null)
				br2.close();
			if(fr2!=null)
				fr2.close();
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
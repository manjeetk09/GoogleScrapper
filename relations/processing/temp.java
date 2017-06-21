import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.*;

public class temp
{

	public static ArrayList<String> get_categories (String arg) throws java.lang.Exception {

        ArrayList<String> categories = new ArrayList<String>();

        Pattern p = Pattern.compile("(?:^|\\s)'([^']*?)'(?:$|\\s)", Pattern.MULTILINE);
        
        arg = arg.replaceAll("\'", " \' ");
        // System.out.print("Input: "+arg+" -> Matches: ");
        Matcher m = p.matcher(arg);
        if (m.find()) {
            String temp = m.group().substring(3,m.group().length() -3);
            categories.add(temp);
            // System.out.println("string:: " +  temp);
            // System.out.print(m.group());
            while (m.find()){
                temp = m.group().substring(3,m.group().length() -3);
                categories.add(temp);
                // System.out.print(", "+m.group());
            }
            // System.out.println();
        } else {
            System.out.println("NONE");
        }
        
        // for(String each : categories)
        //     System.out.println(each); 

        return categories;
    }

    public static String get_cats(String inp) throws Exception{
    	try
		{
			// String t = "singular_point";
			String command = "python mapWikiCat.py " + inp;

			Process proc = Runtime.getRuntime().exec(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			String line = "";

			while((line = reader.readLine()) != null) {
				return line;
			}

			proc.waitFor();



		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return null;
    }

    public static String map_categories(ArrayList<String> head_cats , ArrayList<String> ent_cats){
    	String cats_matches = "";
    	int flag = 0;
    	for(int i = 0 ; i < ent_cats.size() ; i++){
    		for(int j = 0 ; j < head_cats.size() ; j++){
    			if(ent_cats.get(i).matches(head_cats.get(j))){
    				if(flag == 0){
    					flag = 1;
    					cats_matches = cats_matches.concat(ent_cats.get(i));
    				}
    				else{
	    				cats_matches = cats_matches.concat("::");
	    				cats_matches = cats_matches.concat(ent_cats.get(i));
    				}
    			}	

    		}
    	}

    	return cats_matches;
    }

	public static void main (String args[]) throws Exception
	{
		System.out.println("Please enter the main entity terms (as given in TKB).");
        Scanner scanner1 = new Scanner(System.in);
        String head_entity = scanner1.nextLine();
        String head_wiki = "";



		try
		{
			// String t = "singular_point";
			String command = "python mapWikiNew.py " + head_entity;

			Process proc = Runtime.getRuntime().exec(command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			String line = "";

			while((line = reader.readLine()) != null) {
				if(line.length() > 4)
				{
					// System.out.println(line);
					String temp = line.replaceAll("<WikipediaPage \'","");
					String temp1 = temp.replaceAll("\'>","");
					// System.out.println(temp1);
					head_wiki = temp1;
				}
				else
				{
					System.out.println("there does not exist Wikipedia page for the entity");
					System.exit(0);	
				}
			}

			proc.waitFor();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String head_temp = head_wiki.replaceAll(" " , "_");
        ArrayList<String> head_cat = get_categories(get_cats(head_temp));

		FileReader fr = new FileReader("processedMapped.csv");
		BufferedReader br = new BufferedReader(fr);

		FileReader fr1 = new FileReader("processedUnmapped.csv");
		BufferedReader br1 = new BufferedReader(fr1);

		FileWriter fw = new FileWriter("processedMappedWiki.csv");
		BufferedWriter bw = new BufferedWriter(fw);

		FileWriter fw1 = new FileWriter("processedMappedNotWiki.csv");
		BufferedWriter bw1 = new BufferedWriter(fw1);

		FileWriter fw2 = new FileWriter("processedUnmappedWiki.csv");
		BufferedWriter bw2 = new BufferedWriter(fw2);

		FileWriter fw3 = new FileWriter("processedUnmappedNotWiki.csv");
		BufferedWriter bw3 = new BufferedWriter(fw3);

		//dividing the file into those containing wiki pages and those that dont
		String line2 = "";
		while((line2 = br.readLine()) != null)
		{
			String []component = line2.split(";");
			String t = component[3];
		
			try
			{
				// String t = "singular_point";
				String command = "python mapWikiNew.py " + t;

				Process proc = Runtime.getRuntime().exec(command);

				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				String line = "";

				while((line = reader.readLine()) != null) {
					if(line.length() > 4)
					{
						// System.out.println(line);
						String temp = line.replaceAll("<WikipediaPage \'","");
						String temp1 = temp.replaceAll("\'>","");
						// System.out.println(temp1);
						String ent_temp = temp1.replaceAll(" " , "_");
       					ArrayList<String> ent_cat = get_categories(get_cats(ent_temp));
       					String matched_cats = map_categories(head_cat, ent_cat);
       					int num_cats = 0;
       					if(matched_cats.length() < 2){
       						num_cats = 0;
       					}	
       					else{
       						num_cats = matched_cats.split("::").length;
       					}
						bw.write(component[0]+";"+component[1]+";"+component[2]+";"+temp1+";"+component[3]+";"+component[4]+";"+component[5]+";"+component[6]+";"+component[7]+";"+component[8]+";"+component[9]+";"+component[10]+ ";" + num_cats + ";" + matched_cats + "\n");
					}
					else
					{
						// System.out.println("null");
						bw1.write(component[0]+";"+component[1]+";"+component[2]+";"+component[3]+";"+component[4]+";"+component[5]+";"+component[6]+";"+component[7]+";"+component[8]+";"+component[9]+";"+component[10]+"\n");
					}
				}

				proc.waitFor();

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}

		String line1 = "";
		while((line1 = br1.readLine()) != null)
		{
			String []component = line1.split(";");
			String t = component[3];			
			try
			{
				// String t = "singular_point";
				String command = "python mapWikiNew.py " + t;

				Process proc = Runtime.getRuntime().exec(command);

				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

				String line = "";

				while((line = reader.readLine()) != null) {
					if(line.length() > 4)
					{
						// System.out.println(line);
						String temp = line.replaceAll("<WikipediaPage \'","");
						String temp1 = temp.replaceAll("\'>","");
						// System.out.println(temp1);	
						String ent_temp = temp1.replaceAll(" " , "_");
       					ArrayList<String> ent_cat = get_categories(get_cats(ent_temp));
       					String matched_cats = map_categories(head_cat, ent_cat);
       					int num_cats = 0;
       					if(matched_cats.length() < 2){
       						num_cats = 0;
       					}	
       					else{
       						num_cats = matched_cats.split("::").length;
       					}
						bw2.write(component[0]+";"+component[1]+";"+component[2]+";"+temp1+";"+component[3]+";"+component[4]+";"+component[5]+";"+component[6]+";"+component[7]+";"+component[8]+";"+component[9]+";"+component[10]+ ";" + num_cats + ";" + matched_cats + "\n");
					}
					else
					{
						// System.out.println("null");
						bw3.write(component[0]+";"+component[1]+";"+component[2]+";"+component[3]+";"+component[4]+";"+component[5]+";"+component[6]+";"+component[7]+";"+component[8]+";"+component[9]+";"+component[10]+"\n");
					}
				}

				proc.waitFor();

			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

		}
		
		try
		{
			if(br!=null)
				br.close();
			if(fr!=null)
				fr.close();
			if(br1!=null)
				br1.close();
			if(fr1!=null)
				fr1.close();
			if(bw!=null)
				bw.close();
			if(fw!=null)
				fw.close();
			if(bw1!=null)
				bw1.close();
			if(fw1!=null)
				fw1.close();
			if(bw2!=null)
				bw2.close();
			if(fw2!=null)
				fw2.close();
			if(bw3!=null)
				bw3.close();
			if(fw3!=null)
				fw3.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}
}
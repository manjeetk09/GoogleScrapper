import java.io.*;
import java.util.*;

class Entity implements Serializable
{
    String name;
    String template;
    String timestamp;
    String para;
    String url;

    public Entity(String name, String template, String timestamp, String para, String url) {
        this.name = name;
        this.template = template;
        this.timestamp = timestamp;
        this.para = para;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

public class test_map
{
    public static void saveStatus(Serializable object){
        try {
            FileOutputStream saveFile = new FileOutputStream("duplicates.dat");
            ObjectOutputStream out = new ObjectOutputStream(saveFile);
            out.writeObject(object);
            out.close();
            saveFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadStatus(){
        Object result = null;
        try {
            FileInputStream saveFile = new FileInputStream("duplicates.dat");
            ObjectInputStream in = new ObjectInputStream(saveFile);
            result = in.readObject();
            in.close();
            saveFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getTemplate(int num) throws IOException
    {
        FileReader fr = new FileReader("templates.txt");
        BufferedReader br = new BufferedReader(fr);

        int count = 0;
        String lineTemp = "";
        while((lineTemp = br.readLine()) != null)
        {
            if(count == num)
            {
                return lineTemp;
            }
            else
            {
                count = count + 1;
            }
        }
        try
        {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;


    }

    public String getLine(int temp_num, int line_num) throws IOException
    {
        FileReader fr = new FileReader("crawlerq" + temp_num + ".txt");
        BufferedReader br = new BufferedReader(fr);

        int count = 1;
        String lineTemp = "";
        while((lineTemp = br.readLine()) != null)
        {
            if(count == line_num)
            {
                return lineTemp;
            }
            else
            {
                count = count + 1;
            }
        }
        try
        {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;


    }

	public void test_map_func( String head_entity) throws IOException
	{
		FileReader f = new FileReader("final_normalize.csv");
        BufferedReader b = new BufferedReader(f);
        FileWriter fw = new FileWriter("mappedInTKB.csv");
        BufferedWriter bw = new BufferedWriter(fw);
        FileWriter fw1 = new FileWriter("notMappedInTKB.csv");
        BufferedWriter bw1 = new BufferedWriter(fw1);
        FileWriter fw2 = new FileWriter("duplicatesRemoved.csv");
        BufferedWriter bw2 = new BufferedWriter(fw2);
        FileWriter fw3 = new FileWriter("entity.txt");
        BufferedWriter bw3 = new BufferedWriter(fw3);

        ArrayList<Double> rake_score = new ArrayList<Double>();
        ArrayList<Double> tf_score = new ArrayList<Double>();
        ArrayList<Double> idf_score = new ArrayList<Double>();
        ArrayList<Integer> quick_ans = new ArrayList<Integer>();
        ArrayList<Double> relation_sim_score = new ArrayList<Double>();
        ArrayList<Double> final_score = new ArrayList<Double>();

        ArrayList<String> entity = new ArrayList<String>();
        ArrayList<String> line_num = new ArrayList<String>();
        ArrayList<String> url = new ArrayList<String>();
        ArrayList<String> template_num = new ArrayList<String>();

        String line1 = "";
        while((line1 = b.readLine()) != null){
            // System.out.println(entity.size());
            String[] components = line1.split(";");
            template_num.add(components[0]);
            line_num.add(components[1]);
            url.add(components[2]);
            entity.add(components[3]);
            rake_score.add(Double.parseDouble(components[4]));
            tf_score.add(Double.parseDouble(components[5]));
            idf_score.add(Double.parseDouble(components[6]));
            final_score.add(Double.parseDouble(components[7]));
            relation_sim_score.add(Double.parseDouble(components[8]));
            Double temp = Double.parseDouble(components[9]);
            quick_ans.add(temp.intValue());
        }
        try{
            if(b != null)
                b.close();

            if(f != null)
                f.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        //storing duplicates in hashtable
        Hashtable hashtable = new Hashtable(1000);


        //removing duplicates

        for(int i=0;i<entity.size();i++)
        {
        	for(int j = i+1; j<entity.size() ; j++)
        	{
                // System.out.println(entity.get(i) + ":" + entity.get(j));
                // System.out.println(quick_ans.get(i) + ":" + quick_ans.get(j));
                // System.out.println(entity.size() + ":" + i +":" + j);
        		if(entity.get(i).matches(entity.get(j)))
        		{
                    // System.out.println("hello");
        			if(quick_ans.get(i) != quick_ans.get(j))
        			{
        				if(quick_ans.get(j) == 1)
        				{
        				    Date date = new Date();
                            Entity obj = new Entity(entity.get(i),getTemplate(Integer.parseInt(template_num.get(i))),date.toString(),getLine(Integer.parseInt(template_num.get(i)),Integer.parseInt(line_num.get(i))) , url.get(i));
                            ArrayList<Entity> list = (ArrayList<Entity>)hashtable.get(entity.get(i));
                            if(list != null)
                            {
                                list.add(obj);
                                hashtable.put(entity.get(i),list);
                            }
                            else
                            {
                                ArrayList<Entity> list1 = new ArrayList<Entity>();
                                list1.add(obj);
                                hashtable.put(entity.get(i),list1);
                            }
        					template_num.remove(i);
        					line_num.remove(i);
        					url.remove(i);
        					entity.remove(i);
        					rake_score.remove(i);
        					tf_score.remove(i);
        					idf_score.remove(i);
        					final_score.remove(i);
        					relation_sim_score.remove(i);
        					quick_ans.remove(i);
        					i--;
        					j--;
                            break;
        				}
        				else
        				{
                            Date date = new Date();
                            Entity obj = new Entity(entity.get(j),getTemplate(Integer.parseInt(template_num.get(j))),date.toString(),getLine(Integer.parseInt(template_num.get(j)),Integer.parseInt(line_num.get(j))) , url.get(j));
                            ArrayList<Entity> list = (ArrayList<Entity>)hashtable.get(entity.get(j));
                            if(list != null)
                            {
                                list.add(obj);
                                hashtable.put(entity.get(j),list);
                            }
                            else
                            {
                                ArrayList<Entity> list1 = new ArrayList<Entity>();
                                list1.add(obj);
                                hashtable.put(entity.get(j),list1);
                            }
        					template_num.remove(j);
        					line_num.remove(j);
        					url.remove(j);
        					entity.remove(j);
        					rake_score.remove(j);
        					tf_score.remove(j);
        					idf_score.remove(j);
        					final_score.remove(j);
        					relation_sim_score.remove(j);
        					quick_ans.remove(j);
        					j--;
        				}	
        			}
        			else
        			{
        				if(final_score.get(i) >= final_score.get(j))
        				{
                            Date date = new Date();
                            Entity obj = new Entity(entity.get(j),getTemplate(Integer.parseInt(template_num.get(j))),date.toString(),getLine(Integer.parseInt(template_num.get(j)),Integer.parseInt(line_num.get(j))) , url.get(j));
                            ArrayList<Entity> list = (ArrayList<Entity>)hashtable.get(entity.get(j));
                            if(list != null)
                            {
                                list.add(obj);
                                hashtable.put(entity.get(j),list);
                            }
                            else
                            {
                                ArrayList<Entity> list1 = new ArrayList<Entity>();
                                list1.add(obj);
                                hashtable.put(entity.get(j),list1);
                            }
        					template_num.remove(j);
        					line_num.remove(j);
        					url.remove(j);
        					entity.remove(j);
        					rake_score.remove(j);
        					tf_score.remove(j);
        					idf_score.remove(j);
        					final_score.remove(j);
        					relation_sim_score.remove(j);
        					quick_ans.remove(j);
        					j--;
        				}
        				else
        				{
                            Date date = new Date();
                            Entity obj = new Entity(entity.get(i),getTemplate(Integer.parseInt(template_num.get(i))),date.toString(),getLine(Integer.parseInt(template_num.get(i)),Integer.parseInt(line_num.get(i))) , url.get(i));
                            ArrayList<Entity> list = (ArrayList<Entity>)hashtable.get(entity.get(i));
                            if(list != null)
                            {
                                list.add(obj);
                                hashtable.put(entity.get(i),list);
                            }
                            else
                            {
                                ArrayList<Entity> list1 = new ArrayList<Entity>();
                                list1.add(obj);
                                hashtable.put(entity.get(i),list1);
                            }
        					template_num.remove(i);
        					line_num.remove(i);
        					url.remove(i);
        					entity.remove(i);
        					rake_score.remove(i);
        					tf_score.remove(i);
        					idf_score.remove(i);
        					final_score.remove(i);
        					relation_sim_score.remove(i);
        					quick_ans.remove(i);
        					i--;
        					j--;
                            break;	
        				}
        			}
        		}
        	}
        }
        saveStatus(hashtable);
        
        //writing into duplicates file
        // System.out.println(entity.size() + ":" + entity.get(entity.size() -1));
        for(int i=0;i<entity.size(); i++)
        {
            bw2.write(template_num.get(i) + ";" + line_num.get(i) + ";" + url.get(i) + ";" + entity.get(i) + ";" + rake_score.get(i) + ";" + tf_score.get(i) + ";" + idf_score.get(i) + ";" + final_score.get(i) + ";" + relation_sim_score.get(i) + ";" + quick_ans.get(i) + "\n");
        }
        // System.out.println("out again");
        //writing in a txt
        for(int i=0;i<entity.size(); i++)
        {

            bw3.write(entity.get(i) + "\n");

        }        
        // System.out.println("out");
        try
        {
        	if(bw2 != null)
                bw2.close();

            if(fw2 != null)
                fw2.close();

            if(bw3 != null)
                bw3.close();

            if(fw3 != null)
                fw3.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }

        // System.out.println("duplicates done");


        //mapping into the TKB
        // System.out.println(entity.size() + ":" + entity.get(entity.size() - 1));
		// for(int i=0; i<entity.size();i++)
		// {
			// String command = "python trie_sim.py " + entity.get(i) + " 3";
			// System.out.println(command);
		int i = 0;
        ArrayList<String> similar_entity = new ArrayList<String>();
        ArrayList<String> similar_entity_int = new ArrayList<String>();
        try
		{
            String command = "python trie_sim.py entity.txt";
            // System.out.println("entered in triw");
			Process proc = Runtime.getRuntime().exec(command);

	        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

	        String line = "";

	        while((line = reader.readLine()) != null) 
			{
                if(line.matches(":"))
                {
                    if(similar_entity.size() > 0)
                    {
                        int min_entity = 4;
                        int min_index = -1;
                        for(int j=0; j<similar_entity.size(); j++)
                        {
                            if(Integer.parseInt(similar_entity_int.get(j)) < min_entity)
                            {
                                min_entity = Integer.parseInt(similar_entity_int.get(j));
                                min_index = j;
                            }
                        }
                        bw.write( template_num.get(i) + ";" + line_num.get(i) + ";" + url.get(i) + ";" + entity.get(i) + ";" + similar_entity.get(min_index) + ";" + rake_score.get(i) + ";" + tf_score.get(i) + ";" + idf_score.get(i) + ";" + final_score.get(i) + ";" + relation_sim_score.get(i) + ";" + quick_ans.get(i) +"\n");
   
                    }
                    else
                    {
                        bw1.write( template_num.get(i) + ";" + line_num.get(i) + ";" + url.get(i) + ";" + entity.get(i) + ";" + rake_score.get(i) + ";" + tf_score.get(i) + ";" + idf_score.get(i) + ";" + final_score.get(i) + ";" + relation_sim_score.get(i) + ";" + quick_ans.get(i) +"\n");
                    }

                    i = i+1;
                    similar_entity.clear();
                    similar_entity_int.clear();
                }
                else
                {
                    String get_entity = line.substring(2);
                    similar_entity.add(get_entity.substring(0,get_entity.indexOf('\'')));
                    String get_entity_temp = get_entity.substring(get_entity.indexOf(','));
                    similar_entity_int.add(get_entity_temp.substring(2, 3));
                    // System.out.println(similar_entity.get(similar_entity.size() -1) + ":" + similar_entity_int.get(similar_entity_int.size()-1));
                }
	   //      	bw.write( template_num.get(i) + ";" + line_num.get(i) + ";" + url.get(i) + ";" + entity.get(i) + ";" + rake_score.get(i) + ";" + tf_score.get(i) + ";" + idf_score.get(i) + ";" + final_score.get(i) + ";" + relation_sim_score.get(i) + ";" + quick_ans.get(i) +"\n");
	   //      	template_num.remove(i);
				// line_num.remove(i);
				// url.remove(i);
				// entity.remove(i);
				// rake_score.remove(i);
				// tf_score.remove(i);
				// idf_score.remove(i);
				// final_score.remove(i);
				// relation_sim_score.remove(i);
				// quick_ans.remove(i);
				// i--;
				// System.out.println("removed");
	        }
	        proc.waitFor();	
		}
		
		catch(Exception e)
        {
            e.printStackTrace();
        }	
		// }       

		// System.out.println("Done removing");
        //writing in the file
		// System.out.println(entity.size() + ":" + entity.get(entity.size() -1));
        // for(int i=0;i<entity.size(); i++)        	
        // {
        // 	System.out.println(i);
        //     bw1.write( template_num.get(i) + ";" + line_num.get(i) + ";" + url.get(i) + ";" + entity.get(i) + ";" + rake_score.get(i) + ";" + tf_score.get(i) + ";" + idf_score.get(i) + ";" + final_score.get(i) + ";" + relation_sim_score.get(i) + ";" + quick_ans.get(i) +"\n");
        // }
        // System.out.println("Done everything");

        try{
            if(bw != null)
                bw.close();

            if(fw != null)
                fw.close();

            if(bw1 != null)
                bw1.close();

            if(fw1 != null)
                fw1.close();

            

        }catch(Exception e){
            e.printStackTrace();
        }
	}
}
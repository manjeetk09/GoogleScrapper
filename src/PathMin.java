import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class PathMin
{
    public String getEntity(String startEnt, String entity) throws IOException
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

    public ArrayList<String> getPath(String begin, String end, int c) throws IOException
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

                boolean flag1 = false;
                int index = -1;
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
                            if(!flag1)
                            {
                                index = path.size();
                                flag1 = true;
                                path.add(begin);
                                path.add(compos[1]);
                                for(int i=0; i<temp.size();i++)
                                {
                                    path.add(temp.get(i));
                                }
                            }
                            else
                            {
                                if(temp.size() < (path.size() - index - 2))
                                {
                                    for(int i = path.size(); i>=index; i--)
                                    {
                                        path.remove(i);
                                    }
                                    path.add(begin);
                                    path.add(compos[1]);
                                    for(int i=0; i<temp.size();i++)
                                    {
                                        path.add(temp.get(i));
                                    }
                                }

                            }

                            // return path;
                        }
                    }
                }
                if(flag1)
                {
                    return path;
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

    public void pathMinFunc (String head_entity) throws IOException
    {
         FileReader fr2 = new FileReader("processedMapped.csv");
         BufferedReader br2 = new BufferedReader(fr2);

         FileWriter fw = new FileWriter("processedMappedPathRelationMin.csv");
         BufferedWriter bw = new BufferedWriter(fw);
         // String begin = "j_sharp";
//         System.out.println("Enter the head entity:");
//         Scanner scanner = new Scanner(System.in);
         String end = head_entity;

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


         //writing the updated mapped csv
        FileWriter fw1 = new FileWriter("processedMappedUpdated.csv");
        BufferedWriter bw1 = new BufferedWriter(fw1);

        FileWriter fw2 = new FileWriter("removedListing.csv");
        BufferedWriter bw2 = new BufferedWriter(fw2);

        FileReader fr = new FileReader("processedMappedPathRelationMin.csv");
        BufferedReader br = new BufferedReader(fr);

        String reader = "";
        while((reader = br.readLine()) != null)
        {
            String []compo = reader.split(";");
            if(compo.length <=11)
            {
                bw1.write(reader + "\n");
            }
            else
            {
                bw2.write(reader + "\n");
            }
        }

        try
        {
            if(bw1 != null)
                bw1.close();
            if(fw1 != null)
                fw1.close();
            if(bw2 != null)
                bw2.close();
            if(fw2 != null)
                fw2.close();
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//        Scanner scannerT = new Scanner(System.in);
//        System.out.println("Enter the begining entity:");
//        String beginT = scannerT.nextLine();
//        System.out.println("Enter the ending entity:");
//        String endT = scannerT.nextLine();
//        ArrayList<String> resultT = new ArrayList<String>();
//        resultT = getPath(beginT, endT, 6);
//        if(resultT.size() > 0)
//        {
//            for(int i=0; i<resultT.size(); i++)
//            {
//                System.out.print(resultT.get(i) + ":");
//            }
//            System.out.println("");
//        }
    }
}
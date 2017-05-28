import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


class EntityTKB implements Serializable
{
    String entity1;
    String relation;
    String entity2;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EntityTKB(String entity1, String relation, String entity2, String url) {
        this.entity1 = entity1;
        this.relation = relation;
        this.entity2 = entity2;
        this.url = url;
    }

    public String getEntity1() {
        return entity1;
    }

    public void setEntity1(String entity1) {
        this.entity1 = entity1;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEntity2() {
        return entity2;
    }

    public void setEntity2(String entity2) {
        this.entity2 = entity2;
    }
}

/* Class HashTablesChainingListHeadsTest */
public class hashTeKnowBase
{
    public static void saveStatus(Serializable object){
        try {
            FileOutputStream saveFile = new FileOutputStream("teknowbase.dat");
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
            FileInputStream saveFile = new FileInputStream("teknowbase.dat");
            ObjectInputStream in = new ObjectInputStream(saveFile);
            result = in.readObject();
            in.close();
            saveFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws IOException
    {
        //loading code for TKB
//        Hashtable hashtable = new Hashtable(1000);

//        FileReader f = new FileReader("TeKnowbase.tsv");
//        BufferedReader b = new BufferedReader(f);
//        String line = "";
//        while((line = b.readLine()) != null)
//        {
//            String []component = line.split("\t");
//            System.out.println(component[0]+";"+component[1]+";"+component[2]+";"+component[3]);
//            EntityTKB a = new EntityTKB(component[0],component[1],component[2],component[3]);
//            ArrayList<EntityTKB> list = (ArrayList<EntityTKB>)hashtable.get(component[2]);
//            if(list != null)
//            {
//                list.add(a);
//                hashtable.put(component[2],list);
//            }
//            else
//            {
//                ArrayList<EntityTKB> list1 = new ArrayList<EntityTKB>();
//                list1.add(a);
//                hashtable.put(component[2],list1);
//            }
//        }
//        saveStatus(hashtable);
//        try
//        {
//            if(b != null)
//                b.close();
//            if(f!=null)
//                f.close();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }


        //practise loading code
//        EntityTKB a = new EntityTKB("linear", "type of", "algebraic function");
//        EntityTKB b = new EntityTKB("Subtraction", "type of", "algebraic function");
//        EntityTKB c = new EntityTKB("Addition", "type of", "algebraic function");
//        EntityTKB d = new EntityTKB("Division", "example of", "algebraic function");
//        EntityTKB e = new EntityTKB("Remainder", "type of", "database models");
//        EntityTKB f = new EntityTKB("Root", "example of", "database models");
//        ArrayList<EntityTKB> list = new ArrayList<EntityTKB>();
//        list.add(a);
//        list.add(b);
//        list.add(c);
//        list.add(d);
//        ArrayList<EntityTKB> list1 = new ArrayList<EntityTKB>();
//        list1.add(e);
//        list1.add(f);
//        Hashtable hashtable = new Hashtable(1000);
//        hashtable.put("algebraic function", list);
//        hashtable.put("database models", list1);
//        saveStatus(hashtable);


      Hashtable hashtable = (Hashtable)loadStatus();
      ArrayList<EntityTKB> entityTKB = (ArrayList<EntityTKB>)hashtable.get("algebraic_function");
      for(EntityTKB entityTKB1 : entityTKB)
      {
            if(entityTKB1.getRelation().matches("typeOf"))
            {
                System.out.println(entityTKB1.getEntity1());
            }
      }

    }
}
/*
 *    Java Program to Implement Hash Tables Chaining with List Heads
 */

import java.util.ArrayList;
import java.util.Scanner;


class Entity
{
    String name;
    String template;
    String timestamp;
    String para;
    String url;

    public Entity(String name) {
        this.name = name;
        this.template = null;
        this.timestamp = null;
        this.para = null;
        this.url = null;
    }

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

/* Class LinkedHashEntry */
class LinkedHashEntry
{
    String key;
    ArrayList<Entity> duplicates;
    LinkedHashEntry next;

    /* Constructor */
    LinkedHashEntry(String key, Entity value)
    {
        this.key = key;
        if(this.duplicates == null){
            this.duplicates = new ArrayList<Entity>();
            this.duplicates.add(value);
        }
        else {
            this.duplicates.add(value);
        }
        this.next = null;
    }
}

/* Class HashTable */
class HashTable
{
    private int TABLE_SIZE;
    private int size;
    private LinkedHashEntry[] table;

    /* Constructor */
    public HashTable(int ts)
    {
        size = 0;
        TABLE_SIZE = ts;
        table = new LinkedHashEntry[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
    }
    /* Function to get number of key-value pairs */
    public int getSize()
    {
        return size;
    }
    /* Function to clear hash table */
    public void makeEmpty()
    {
        for (int i = 0; i < TABLE_SIZE; i++)
            table[i] = null;
    }
    /* Function to get value of a key */
    public ArrayList<Entity> get(String key)
    {
//        System.out.println("reached func get");
        int hash = (myhash( key ) % TABLE_SIZE);
        if (table[hash] == null)
            return null;
        else
        {
            LinkedHashEntry entry = table[hash];
            while (entry != null && !entry.key.equals(key))
                entry = entry.next;
            if (entry == null)
                return null;
            else
//                System.out.println("returning arraylist");

                return entry.duplicates;
        }
    }
    /* Function to insert a key value pair */
    public void insert(String key, Entity value)
    {
        int hash = (myhash( key ) % TABLE_SIZE);
        if (table[hash] == null)
            table[hash] = new LinkedHashEntry(key, value);
        else
        {
            LinkedHashEntry entry = table[hash];
            while (entry.next != null && !entry.key.equals(key))
                entry = entry.next;
            if (entry.key.equals(key))
                entry.duplicates.add(value);
            else
                entry.next = new LinkedHashEntry(key, value);
        }
        size++;
    }

    public void remove(String key)
    {
        int hash = (myhash( key ) % TABLE_SIZE);
        if (table[hash] != null)
        {
            LinkedHashEntry prevEntry = null;
            LinkedHashEntry entry = table[hash];
            while (entry.next != null && !entry.key.equals(key))
            {
                prevEntry = entry;
                entry = entry.next;
            }
            if (entry.key.equals(key))
            {
                if (prevEntry == null)
                    table[hash] = entry.next;
                else
                    prevEntry.next = entry.next;
                size--;
            }
        }
    }
    /* Function myhash which gives a hash value for a given string */
    private int myhash(String x )
    {
        int hashVal = x.hashCode( );
        hashVal %= TABLE_SIZE;
        if (hashVal < 0)
            hashVal += TABLE_SIZE;
        return hashVal;
    }
    /* Function to print hash table */
    public void printHashTable()
    {
        for (int i = 0; i < TABLE_SIZE; i++)
        {
            System.out.print("\nBucket "+ (i + 1) +" : ");
            LinkedHashEntry entry = table[i];
            while (entry != null)
            {
                System.out.print(entry.duplicates +" ");
                entry = entry.next;
            }
        }
    }
}

/* Class HashTablesChainingListHeadsTest */
public class hash
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hash Table Test\n\n");
        System.out.println("Enter size");
        /* Make object of HashTable */
        HashTable ht = new HashTable(scan.nextInt() );

        Entity a = new Entity("linear");
        Entity b = new Entity("Subtraction");
        Entity c = new Entity("Addition");
        Entity d = new Entity("Multiplication");
        Entity e = new Entity("Division");
        Entity f = new Entity("algebra");
        Entity g = new Entity("functions");
        Entity h = new Entity("mathematics");
        Entity i = new Entity("programming");



        ht.insert("algebraic_functions", c);
        ht.insert("algebraic_functions", b);
        ht.insert("algebraic_functions", d);
        ht.insert("algebraic_functions", e);
        ht.insert("maths", h);
        ht.insert("maths", a);
        ht.insert("maths", f);
        ht.insert("maths", g);
        ht.insert("computer_Science", i);

        ArrayList<Entity> ans = new ArrayList<Entity>();
        ans = ht.get("maths");
        for(int j = 0 ; j < ans.size() ; j++){
            System.out.println(ans.get(j).name);
        }



//        Scanner scan = new Scanner(System.in);
//        System.out.println("Hash Table Test\n\n");
//        System.out.println("Enter size");
//        /* Make object of HashTable */
//        HashTable ht = new HashTable(scan.nextInt() );
//
//        char ch;
//        /*  Perform HashTable operations  */
//        do
//        {
//            System.out.println("\nHash Table Operations\n");
//            System.out.println("1. insert ");
//            System.out.println("2. remove");
//            System.out.println("3. get");
//            System.out.println("4. clear");
//            System.out.println("5. size");
//
//            int choice = scan.nextInt();
//            switch (choice)
//            {
//                case 1 :
//                    System.out.println("Enter key and value");
//                    ht.insert(scan.next(), scan.nextInt() );
//                    break;
//                case 2 :
//                    System.out.println("Enter key");
//                    ht.remove( scan.next() );
//                    break;
//                case 3 :
//                    System.out.println("Enter key");
//                    System.out.println("Value = "+ ht.get( scan.next() ));
//                    break;
//                case 4 :
//                    ht.makeEmpty();
//                    System.out.println("Hash Table Cleared\n");
//                    break;
//                case 5 :
//                    System.out.println("Size = "+ ht.getSize() );
//                    break;
//                default :
//                    System.out.println("Wrong Entry \n ");
//                    break;
//            }
//            /* Display hash table */
//            ht.printHashTable();
//
//            System.out.println("\nDo you want to continue (Type y or n) \n");
//            ch = scan.next().charAt(0);
//        } while (ch == 'Y'|| ch == 'y');

    }
}
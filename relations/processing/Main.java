import java.util.*;
import java.lang.*;
import java.util.regex.*;

class Main {

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

    public static void main(String[] args) throws Exception {
        ArrayList<String> categories_new = get_categories("[u'Analytic functions', u'Functions and mappings', u'Meromorphic functions', u'Special functions', u'Types of functions']");
        for(String each : categories_new)
            System.out.println(each);

    }
}
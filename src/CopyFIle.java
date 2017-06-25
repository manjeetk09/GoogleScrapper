import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class CopyFIle
{
    public void copyFunc (int temp_num, String temp_name, String file_name, String exten) throws IOException
    {
        if(temp_num == -1)
        {
            String source = file_name + "." + exten;
            String dest = temp_name + "/" + file_name + "." + exten;
            File sourceF = new File(source);
            File destF = new File(dest);
            if(!destF.exists()){
                destF.mkdirs();
            }
            try {
                boolean bool = destF.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Files.copy(sourceF.toPath(), destF.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        else
        {
            for(int i=0 ; i<temp_num; i++)
            {
                String source = file_name + i + "." + exten;
                String dest = temp_name + "/" + file_name + "/" + file_name + i + "." + exten;
                File sourceF = new File(source);
                File destF = new File(dest);
                if(!destF.exists()){
                    destF.mkdirs();
                }
                try {
                    boolean bool = destF.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Files.copy(sourceF.toPath(), destF.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
    public void copyFunc(String template, int temp_num) throws IOException
    {
//        int temp_num = 0;
//        String template = "";
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the query:");
//        template = scanner.nextLine();
//        System.out.println("Enter the number of templates:");
//        temp_num = scanner.nextInt();

        //copying the files
        copyFunc(temp_num, template, "crawler", "txt");
        copyFunc(temp_num, template, "crawlerq", "txt");
        copyFunc(temp_num, template, "csv_triples_ollie", "csv");
        copyFunc(-1, template, "duplicates", "dat");
        copyFunc(-1, template, "duplicatesRemoved", "csv");

        copyFunc(temp_num, template, "entity_rake", "csv");
        copyFunc(-1, template, "final", "csv");
        copyFunc(-1, template, "final_normalize", "csv");
        copyFunc(temp_num, template, "li", "csv");
        copyFunc(-1, template, "mappedInTKB", "csv");

        copyFunc(-1, template, "mergeCatNonCat", "csv");
        copyFunc(-1, template, "notMappedInTKB", "csv");
        copyFunc(temp_num, template, "para", "txt");
        copyFunc(-1, template, "processedMapped", "csv");
        copyFunc(-1, template, "processedMappedNotWiki", "csv");

        copyFunc(-1, template, "processedMappedPath", "csv");
        copyFunc(-1, template, "processedMappedPathRelationMin", "csv");
        copyFunc(-1, template, "processedMappedUpdated", "csv");
        copyFunc(-1, template, "processedMappedWiki", "csv");
        copyFunc(-1, template, "processedMergedWiki", "csv");

        copyFunc(-1, template, "processedUnmapped", "csv");
        copyFunc(-1, template, "processedUnmappedNotWiki", "csv");
        copyFunc(-1, template, "processedUnmappedWiki", "csv");
        copyFunc(-1, template, "removedListing", "csv");
        copyFunc(temp_num, template, "result", "csv");

        copyFunc(temp_num, template, "result_similarity_ollie", "csv");
        copyFunc(-1, template, "teknowbase", "dat");
        copyFunc(-1, template, "templates", "txt");
        copyFunc(temp_num, template, "timeDate", "txt");
        copyFunc(temp_num, template, "triples_ollie", "txt");

        copyFunc(temp_num, template, "urls", "txt");

        String source = "Category";
        String dest = template + "/Category";
        File sourceF = new File(source);
        File destF = new File(dest);
        if(!destF.exists()){
            destF.mkdirs();
        }
        String files[] = sourceF.list();
        for (String file : files)
        {
            File srcFile = new File(source, file);
            File destFile = new File(dest, file);
            Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

    }
}

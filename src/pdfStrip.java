import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class pdfStrip {

    public static void main(String[] args) throws IOException {

        System.setProperty("http.proxyHost", "10.10.78.22");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "10.10.78.22");
        System.setProperty("https.proxyPort", "3128");

        URL url1 =
                new URL("https://sydney.edu.au/stuserv/documents/maths_learning_centre/functions_graphs.pdf");

        byte[] ba1 = new byte[1024];
        int baLength;
        FileOutputStream fos1 = new FileOutputStream("download.pdf");

        try {
            // Contacting the URL
            System.out.print("Connecting to " + url1.toString() + " ... ");
            URLConnection urlConn = url1.openConnection();

            // Checking whether the URL contains a PDF
            if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
                System.out.println("FAILED.\n[Sorry. This is not a PDF.]");
            } else {
                try {

                    // Read the PDF from the URL and save to a local file
                    InputStream is1 = url1.openStream();
                    while ((baLength = is1.read(ba1)) != -1) {
                        fos1.write(ba1, 0, baLength);
                    }
                    fos1.flush();
                    fos1.close();
                    is1.close();

                    // Load the PDF document and display its page count
                    System.out.print("DONE.\nProcessing the PDF ... ");
//                    PdfDocument doc = new PdfDocument()
//                    try {
//                        doc.load("download.pdf");
//                        System.out.println("DONE.\nNumber of pages in the PDF is " +
//                                doc.getPageCount());
//                        doc.close();
//                    } catch (Exception e) {
//                        System.out.println("FAILED.\n[" + e.getMessage() + "]");
//                    }

                } catch (ConnectException ce) {
                    System.out.println("FAILED.\n[" + ce.getMessage() + "]\n");
                }
            }

        } catch (NullPointerException npe) {
            System.out.println("FAILED.\n[" + npe.getMessage() + "]\n");
        }

        ArrayList<String> paragraphs_from_pdf = new ArrayList<String>();
        try
        {
            String file_name = "pdf.txt";
            BufferedWriter bw1 = null;
            FileWriter fw1 = new FileWriter(file_name);
            bw1 = new BufferedWriter(fw1);

            String command = "python pdf2txt.py -Y normal 1.pdf";
            //String command = "echo 'hello'";

            Process proc = Runtime.getRuntime().exec(command);


            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String paragraph_temp = "";
            String line = "";

            while((line = reader.readLine()) != null) {

//                String line_temp = line.replaceAll(":"," ");
//                bw1.write(line);
//                bw1.write("\n");

                if(line.matches("\n") || line.matches("")){
//                    System.out.println("new paragraph");
                    paragraph_temp = paragraph_temp.concat(line);
                    String new_string = paragraph_temp;
                    paragraphs_from_pdf.add(new_string);
                    paragraph_temp = "";
//                    System.out.println();
//                    System.out.println("new paragraph");
                }
                else{
                    if(line.charAt(line.length() - 1) == '-'){
                        line = line.substring(0,line.length() - 1);
                        line = line.concat(" ");
                    }
                    else{
                        line = line.concat(" ");
                    }
                    paragraph_temp = paragraph_temp.concat(line);
                }
//                System.out.print(line + "\n");
            }

            proc.waitFor();

            for(String each: paragraphs_from_pdf){
                bw1.write(each);
                bw1.write("\n");
//                System.out.println("para:: " + each);

            }

            try
            {
                if (bw1 != null)
                    bw1.close();

                if (fw1 != null)
                    fw1.close();
            }
            catch(Exception e){e.printStackTrace();}

        }
        catch(Exception e){}





    }
}
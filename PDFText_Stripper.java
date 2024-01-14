import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import com.opencsv.CSVWriter;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/**
 * PDFText_Stripper
 */
public class PDFText_Stripper extends PDFTextStripper{
    public static void main(String[] args) {
        
//        Scanner sc=new Scanner(System.in);
        try {

            System.out.println("Fetching files from Path...");

            //File path is specified

            String currentPath = Paths.get("").toAbsolutePath().toString();

            File file=new File(currentPath+"/Files/JioInvoice_Final.pdf");

            System.out.println("Loading PDF...");

            //Loader loads pdf using filePath
            PDDocument pd=Loader.loadPDF(file);

            //Instantiation of textStripper class
            PDFTextStripper textStripper=new PDFTextStripper();

/*
            **************User choice for starting page***********

              * System.out.println("Enter page number to begin with");
              * int choice=Integer.parseInt(sc.nextLine());
              * sc.close();
              * textStripper.setStartPage(choice);
*/
            //Setting page limits
            textStripper.setStartPage(0);

            System.out.println("Stripping text out of PDF...");

            //Extracting text out of PDF
            String text=textStripper.getText(pd);

            //Creates new text file in the given location
            Path path=Paths.get(currentPath+"/Files/JioInvoice.txt");

            try
            {
                //Writing the extracted text to file 
                Files.writeString(path, text, StandardCharsets.UTF_8);
                System.out.println("Converting to text file...");
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }

            //FileReader to read text file that has extracted data
            FileReader fr=new FileReader(currentPath+"/Files/JioInvoice.txt");
            BufferedReader br=new BufferedReader(fr);

            System.out.println("Finding right data...");
            System.out.println("Converting to csv file...");

            String filePath=currentPath+"/Files/Jio_Invoice_Data.csv";
            File file1=new File(filePath);

            try
            {    
                FileWriter fw=new FileWriter(file1);
                CSVWriter cw=new CSVWriter(fw,',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,CSVWriter.DEFAULT_LINE_END);
                String line;
                List<String[]> data= new ArrayList<>();
                String[] rowHead={"No.","Start Date","Start Time","End Date","End Time","Destination","Total Usage(MB)","Billed Usage(MB)","Free Usage(MB)","Chargeable Usage(MB)","Amount"};
                data.add(rowHead);
                //Read every line and find all strings that match with regex
                while((line=br.readLine())!=null)
                {
                    PatternMatcher patternMatcher = new PatternMatcher();
                    if(patternMatcher.getMatchedString(line))
                    {
                        String[] rowData=line.split(" ");
                        data.add(rowData);
                    }
                }

                System.out.println("Completed.");

                cw.writeAll(data);
                cw.close();
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }

            fr.close();
            /*
            String txt_1=text.substring(5531, 5609);
            System.out.println("***********************Running*********************");
            PatternMatcher patternMatcher = new PatternMatcher();
            patternMatcher.getMatchedString(txt_1);
            System.out.println(txt_1);
            //Writing stripped text
            // System.out.println(text);
            */

            //Close document to prevent resource leakage
            pd.close();
        }

        catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }       
    }
}
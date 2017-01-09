package gov.nih.nci.evs.app.neopl;

import com.opencsv.CSVReader;
//import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2016 NGIS. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIS and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIS" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIS
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIS, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class ExcelToCSV {
    private Workbook workbook = null;
    private ArrayList<ArrayList<String>> csvData = null;
    private int maxRowWidth = 0;
    private int formattingConvention = 0;
    private DataFormatter formatter = null;
    private FormulaEvaluator evaluator = null;
    private String separator = null;

    private static final String CSV_FILE_EXTENSION = ".csv";
    private static final String DEFAULT_SEPARATOR = ",";
    public static final int EXCEL_STYLE_ESCAPING = 0;

    public static final int UNIX_STYLE_ESCAPING = 1;

    private String header = null;

    public void setHeader(String header) {
		this.header = header;
	}


    public void convertExcelToCSV(String strSource, String strDestination)
                       throws FileNotFoundException, IOException,
                              IllegalArgumentException, InvalidFormatException {
        this.convertExcelToCSV(strSource, strDestination,
                DEFAULT_SEPARATOR, EXCEL_STYLE_ESCAPING);
    }

    public void convertExcelToCSV(String strSource, String strDestination,
                                  String separator)
                       throws FileNotFoundException, IOException,
                              IllegalArgumentException, InvalidFormatException {

        this.convertExcelToCSV(strSource, strDestination,
                separator, EXCEL_STYLE_ESCAPING);
    }


    public ArrayList readExcelFile(String strSource)
                       throws FileNotFoundException, IOException,
                              IllegalArgumentException, InvalidFormatException {
        return readExcelFile(strSource,
                DEFAULT_SEPARATOR, EXCEL_STYLE_ESCAPING);
    }

    public ArrayList readExcelFile(String strSource,
                                  String separator, int formattingConvention)
                       throws FileNotFoundException, IOException,
                              IllegalArgumentException, InvalidFormatException {

        File source = new File(strSource);
        File[] filesList = null;

        if(!source.exists()) {
            throw new IllegalArgumentException("The source for the Excel " +
                    "file(s) cannot be found.");
        }
        if(formattingConvention != EXCEL_STYLE_ESCAPING &&
           formattingConvention != UNIX_STYLE_ESCAPING) {
            throw new IllegalArgumentException("The value passed to the " +
                    "formattingConvention parameter is out of range.");
        }
        this.separator = separator;
        this.formattingConvention = formattingConvention;

		File excelFile = source;
		this.openWorkbook(excelFile);
		this.convertToCSV();
		return this.csvData;
	}

    public void convertExcelToCSV(String strSource, String strDestination,
                                  String separator, int formattingConvention)
                       throws FileNotFoundException, IOException,
                              IllegalArgumentException, InvalidFormatException {

        File source = new File(strSource);
        File destination = new File(strDestination);
        File[] filesList = null;
        String destinationFilename = null;

        if(!source.exists()) {
            throw new IllegalArgumentException("The source for the Excel " +
                    "file(s) cannot be found.");
        }
        if(formattingConvention != EXCEL_STYLE_ESCAPING &&
           formattingConvention != UNIX_STYLE_ESCAPING) {
            throw new IllegalArgumentException("The value passed to the " +
                    "formattingConvention parameter is out of range.");
        }
        this.separator = separator;
        this.formattingConvention = formattingConvention;

		File excelFile = source;
		this.openWorkbook(excelFile);
		this.convertToCSV();


		destinationFilename = excelFile.getName();
		int n = destinationFilename.lastIndexOf(".");
		destinationFilename = destinationFilename.substring(0, n) + CSV_FILE_EXTENSION;
		System.out.println("(*) destinationFilename: " + destinationFilename);
		this.saveCSVFile(new File(destinationFilename));
    }

    private void openWorkbook(File file) throws FileNotFoundException,
                                           IOException, InvalidFormatException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            this.workbook = WorkbookFactory.create(fis);
            this.evaluator = this.workbook.getCreationHelper().createFormulaEvaluator();
            this.formatter = new DataFormatter(true);
        }
        finally {
            if(fis != null) {
                fis.close();
            }
        }
    }

    private void convertToCSV() {
        Sheet sheet = null;
        Row row = null;
        int lastRowNum = 0;
        this.csvData = new ArrayList<ArrayList<String>>();

        System.out.println("Converting files contents to CSV format.");

        int numSheets = this.workbook.getNumberOfSheets();
        for(int i = 0; i < numSheets; i++) {
            sheet = this.workbook.getSheetAt(i);
            if(sheet.getPhysicalNumberOfRows() > 0) {
                lastRowNum = sheet.getLastRowNum();
                /*
                for(int j = 0; j <= lastRowNum; j++) {
                    row = sheet.getRow(j);
                    this.rowToCSV(row);
                }
                */
                for(int j = 1; j <= lastRowNum; j++) {
                    row = sheet.getRow(j);
                    this.rowToCSV(row);
                }
            }
            //sheet.createFreezePane(0,1); // this will freeze the header row
        }

    }

    private void saveCSVFile(File file) throws FileNotFoundException, IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        ArrayList<String> line = null;
        StringBuffer buffer = null;
        String csvLineElement = null;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            if (header != null) {
				bw.write(header);
				bw.newLine();
			}
            for(int i = 0; i < this.csvData.size(); i++) {
                buffer = new StringBuffer();
                line = this.csvData.get(i);
                for(int j = 0; j < this.maxRowWidth; j++) {
                    if(line.size() > j) {
                        csvLineElement = line.get(j);
                        if(csvLineElement != null) {
                            buffer.append(this.escapeEmbeddedCharacters(
                                    csvLineElement));
                        }
                    }
                    if(j < (this.maxRowWidth - 1)) {
                        buffer.append(this.separator);
                    }
                }
                bw.write(buffer.toString().trim());
                if(i < (this.csvData.size() - 1)) {
                    bw.newLine();
                }
            }
        }
        finally {
            if(bw != null) {
                bw.flush();
                bw.close();
            }
        }
    }

    private void rowToCSV(Row row) {
        Cell cell = null;
        int lastCellNum = 0;
        ArrayList<String> csvLine = new ArrayList<String>();
        if(row != null) {
            lastCellNum = row.getLastCellNum();
            for(int i = 0; i <= lastCellNum; i++) {
                cell = row.getCell(i);
                if(cell == null) {
                    csvLine.add("");
                }
                else {
                    csvLine.add(this.formatter.formatCellValue(cell));
                }
            }
            if(lastCellNum > this.maxRowWidth) {
                this.maxRowWidth = lastCellNum;
            }
        }
        this.csvData.add(csvLine);
    }

    private String escapeEmbeddedCharacters(String field) {
        StringBuffer buffer = null;
        if(this.formattingConvention == EXCEL_STYLE_ESCAPING) {
            if(field.contains("\"")) {
                buffer = new StringBuffer(field.replaceAll("\"", "\\\"\\\""));
                buffer.insert(0, "\"");
                buffer.append("\"");
            }
            else {
                buffer = new StringBuffer(field);
                if((buffer.indexOf(this.separator)) > -1 ||
                         (buffer.indexOf("\n")) > -1) {
                    buffer.insert(0, "\"");
                    buffer.append("\"");
                }
            }
            return(buffer.toString().trim());
        }
        else {
            if(field.contains(this.separator)) {
                field = field.replaceAll(this.separator, ("\\\\" + this.separator));
            }
            if(field.contains("\n")) {
                field = field.replaceAll("\n", "\\\\\n");
            }
            return(field);
        }
    }



    class ExcelFilenameFilter implements FilenameFilter {
        public boolean accept(File file, String name) {
            return(name.endsWith(".xls") || name.endsWith(".xlsx"));
        }
    }

	public static String getOutputFile(String inputfile, String format) {
		int n = inputfile.lastIndexOf(".");
		String outputfile = inputfile.substring(0, n) + "." + format;
		return outputfile;
	}

    public static Vector<String> parseData(String line) {
		if (line == null) return null;
        String tab = "|";
        return parseData(line, tab);
    }

    public static Vector<String> parseData(String line, String tab) {
		if (line == null) return null;
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = "";
            data_vec.add(value);
        }
        return data_vec;
    }

    public static Vector extractColumnDataFromCSV(String csvfile, int column_index) {
		boolean skip_heading = true;
		return extractColumnDataFromCSV(csvfile, column_index, skip_heading);
	}

    public static Vector extractColumnDataFromCSV(String csvfile, int column_index, boolean skip_heading) {
		Vector w = new Vector();
		String[] values = null;
		try {
			CSVReader reader = new CSVReader(new FileReader(csvfile));
			if (skip_heading) {
				values = reader.readNext();
			}
			while ((values = reader.readNext()) != null) {
				String value = (String) values[column_index];
				w.add(value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return w;
	}

    public void toDelimitedFile(String excelfile, String outputfile, String heading) {
		PrintWriter pw = null;

        FileWriter fw = null;
        BufferedWriter bw = null;
		try {
            //fw = new FileWriter(new File(outputfile));
            bw = new BufferedWriter
                 (new OutputStreamWriter(new FileOutputStream(outputfile), "UTF-8"));

            //bw = new BufferedWriter(fw);

            List list = readExcelFile(excelfile);
            if (heading != null && heading.length() > 0) {
				bw.write(heading);
				bw.newLine();
			}

            for (int i=0; i<list.size(); i++) {
				ArrayList<String> a = (ArrayList<String>) list.get(i);
				StringBuffer buf = new StringBuffer();
				for (int j=0; j<a.size(); j++) {
					String t = (String) a.get(j);
					buf.append(t).append("\t");
				}
				String line = buf.toString();
				bw.write(line);
				bw.newLine();

				//pw.println(line);
			}


		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			try {
				//if (fin != null) fin.close();
				//if (fout != null) fout.close();
				//pw.close();
				bw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


    public static List readCSV(String csvfile, boolean skip_heading) {
		ArrayList list = new ArrayList();
		int n = csvfile.lastIndexOf(".");
		String nextLine = null;
		//String outputfile = getOutputFile(csvfile, "txt");

		Vector w = new Vector();
		String[] values = null;
		BufferedReader in = null;
		try {
			//CSVReader reader = new CSVReader(new FileReader(csvfile));
			in = new BufferedReader(
			   new InputStreamReader(
						  new FileInputStream(csvfile), "UTF-8"));

			CSVReader reader = new CSVReader(in);

			if (skip_heading) {
				values = reader.readNext();
			}
			while ((values = reader.readNext()) != null) {
				list.add(values);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;

		} finally {
			try {
				if (in != null) in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}

/*
    public static void main(String[] args) {
        ExcelToCSV converter = null;
        boolean converted = true;
        long startTime = System.currentTimeMillis();
        try {
            converter = new ExcelToCSV();
            if(args.length == 2) {
                converter.convertExcelToCSV(args[0], args[1]);
            }
            else if(args.length == 3){
                converter.convertExcelToCSV(args[0], args[1], args[2]);
            }
            else if(args.length == 4) {
                converter.convertExcelToCSV(args[0], args[1],
                                            args[2], Integer.parseInt(args[3]));
            }
            else {
                System.out.println("Usage: java ToCSV [Source File/Folder] " +
                    "[Destination Folder] [Separator] [Formatting Convention]\n" +
                    "\tSource File/Folder\tThis argument should contain the name of and\n" +
                    "\t\t\t\tpath to either a single Excel workbook or a\n" +
                    "\t\t\t\tfolder containing one or more Excel workbooks.\n" +
                    "\tDestination Folder\tThe name of and path to the folder that the\n" +
                    "\t\t\t\tCSV files should be written out into. The\n" +
                    "\t\t\t\tfolder must exist before running the ToCSV\n" +
                    "\t\t\t\tcode as it will not check for or create it.\n" +
                    "\tSeparator\t\tOptional. The character or characters that\n" +
                    "\t\t\t\tshould be used to separate fields in the CSV\n" +
                    "\t\t\t\trecord. If no value is passed then the comma\n" +
                    "\t\t\t\twill be assumed.\n" +
                    "\tFormatting Convention\tOptional. This argument can take one of two\n" +
                    "\t\t\t\tvalues. Passing 0 (zero) will result in a CSV\n" +
                    "\t\t\t\tfile that obeys Excel's formatting conventions\n" +
                    "\t\t\t\twhilst passing 1 (one) will result in a file\n" +
                    "\t\t\t\tthat obeys UNIX formatting conventions. If no\n" +
                    "\t\t\t\tvalue is passed, then the CSV file produced\n" +
                    "\t\t\t\twill obey Excel's formatting conventions.");
                converted = false;
            }
        }
        catch(Exception ex) {
            System.out.println("Caught an: " + ex.getClass().getName());
            System.out.println("Message: " + ex.getMessage());
            System.out.println("Stacktrace follows:.....");
            ex.printStackTrace(System.out);
            converted = false;
        }

        if (converted) {
            System.out.println("Conversion took " +
                  (int)((System.currentTimeMillis() - startTime)/1000) + " seconds");
        }
    }
*/

    public static void main(String[] args) {
		String excelfile = args[0];
		String outputfile = null;
		int n = excelfile.lastIndexOf(".");
		outputfile = excelfile.substring(0, n) + ".txt";
		System.out.println(excelfile);
		System.out.println(outputfile);
		String 	heading = "Code\tPreferred Term\tSynonyms\tDefinition\tNeoplastic Status";
		new ExcelToCSV().toDelimitedFile(excelfile, outputfile, heading);

	}

}

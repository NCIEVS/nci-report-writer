package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import java.awt.Color;
import java.io.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;


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


public class CSVtoExcel {
	HashMap localNameMap = null;
	//Hyperlinks in a worksheet: 66,530 hyperlinks
    static int PAGE_SIZE = 22176;
    static boolean NCIT_LINK = true;
    static boolean NCIM_LINK = true;
    static boolean SOURCE_LINK = true;
    static String SHEET_LABEL = "Neoplasm Core Mapping";
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";

    static final int LINK_NCIT = 1;
    static final int LINK_NCIM = 2;
    static final int LINK_OTHER = 3;


/*
NCIt Code
NCIt Preferred Term
NCIm CUI
NCIm Preferred Name
NCIm Source
Term Type
Source Code
Source Term
*/

	public static String[] HEADINGS = new String[] {
		"NCIt Code",
		"NCIt Preferred Term",
		"NCIm CUI",
		"NCIm Preferred Name",
		"NCIm Source",
		"Term Type",
		"Source Code",
        "Source Term"
	};

	int SOURCE_INDEX = 4;

	int[] codeColumn = null;
	//	"NCIt Code","NCIt Name","CUI","NCIm Name","Source","Source Term","Source Code","Term Type" };

	public CSVtoExcel() {
        //initialize();
	}

	public void setHeadings(String[] headings) {
		this.HEADINGS = headings;
	}

	public void setCodeColumn(int[] codeColumn) {
		this.codeColumn = codeColumn;
	}

	public void initialize() {
		localNameMap = loadLocalNameMap("localNameMap.txt");
	}

	public void initialize(String localNameMapFile) {
		localNameMap = loadLocalNameMap(localNameMapFile);
	}

	public void setSOURCE_INDEX(int index) {
		this.SOURCE_INDEX = index;
	}

	public String[] extractHeadings(String filename) {
		Vector v = FileUtils.readFile(filename);
		String heading = (String) v.elementAt(0);
		Vector u = parseData(heading, ",");
		String[] headings = new String[u.size()];
		for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
			t = t.substring(1, t.length()-1);
			headings[i] = t;
			System.out.println(t);
		}
		return headings;
	}

    public Vector<String> parseData(String line) {
		if (line == null) return null;
        String tab = "|";
        return parseData(line, tab);
    }

    public Vector<String> parseData(String line, String tab) {
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

    public void setLocalNameMap(HashMap hmap) {
		localNameMap = hmap;
	}

	public HashMap loadLocalNameMap(String filename) {
		HashMap map = new HashMap();
		Vector v = FileUtils.readFile(filename);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			Vector u = parseData(t);
			String key = (String) u.elementAt(0);
			String value = (String) u.elementAt(1);
			map.put(key, value);
			System.out.println(key + " --> " + value);
		}
		return map;
	}

	public String getNCItHyperlink(String code) {
        String line = "https://nciterms.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI_Thesaurus&code=" + code;
        //System.out.println(line);
        return line;
    }

	public String getNCImHyperlink(String code) {
        String line = "https://ncim.nci.nih.gov/ncimbrowser/ConceptReport.jsp?dictionary=NCI%20Metathesaurus&code=" + code;
        //System.out.println(line);
        return line;
	}

	public String getSourceHyperlink(String source, String code) {
        String line = "https://nciterms.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=" + source + "&code=" + code;
        //System.out.println(line);
        line = line.replace(" ", "%20");
        return line;
    }

    public String removeSpecialCharacters(String line) {
		String t = "";
		String str = null;
		for (int j=0; j<line.length(); j++) {
			char c = line.charAt(j);
			if (c > 127) {
				str = str + "?";
			} else {
				str = str + c;
			}
		}
        return str;
	}

    public int checkSpecialCharacters(String inputfile) {
		BufferedReader br = null;
		String line = "";
		int lcv = 0;
		int n = 0;
		Vector v = new Vector();
		try {
			br = new BufferedReader(new FileReader(inputfile));
			int k = 0;
			while ((line = br.readLine()) != null) {
				for (int j=0; j<line.length(); j++) {
					char c = line.charAt(j);
					if (c > 127) {
						lcv++;
						System.out.println("\n(" + lcv + ") " + line);
					}
				}
				n++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\nNumber of rows in " + inputfile + ": " + n);
		return n;
	}


	public void run(String inputfile) {
		//runHSSF(inputfile);
	}


    public void runHSSF(String inputfile) {
		int size = checkSpecialCharacters(inputfile);

		int n = inputfile.lastIndexOf(".");
		String outputfile = getOutputFile(inputfile, "xls");

		try {
            HSSFWorkbook wb = new HSSFWorkbook();

			HSSFCellStyle cellStyle = wb.createCellStyle();
			//cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
			//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			//font.setColor(HSSFColor.WHITE.index);
			cellStyle.setFont(font);


			HSSFCellStyle linkCellStyle = wb.createCellStyle();
			//cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
			//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont linkfont = wb.createFont();
			linkfont.setColor(HSSFColor.LIGHT_BLUE.index);
			linkCellStyle.setFont(linkfont);

			//CreationHelper helper = wb.getCreationHelper();
			CSVReader reader = new CSVReader(new FileReader(inputfile));//CSV file
			String[] line;
			int r = 0;
			Cell cell = null;

			//"C7419","Acanthoma","C0846967","Acanthoma","MDR","Acanthoma","10059394","LT"
			// skip heading
			HSSFHyperlink url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
			HSSFSheet sheet = null;
			int page_num = 1;
			Row row = null;
			line = reader.readNext();
			int lcv = 0;
			int row_count = 0;
			Vector w = new Vector();
			while ((line = reader.readNext()) != null) {
                if (lcv % PAGE_SIZE == 0) {
					r = 0;
					String sheetLabel = SHEET_LABEL;
					if (size > PAGE_SIZE) {
					    sheetLabel = sheetLabel + " (Page " + page_num + ")";
					}
					sheet = wb.createSheet(sheetLabel);
					for (int j = 0; j < HEADINGS.length; j++) {
                    	sheet.autoSizeColumn(j);
                	}
					row = sheet.createRow((short) r);
					for (int i=0; i<HEADINGS.length; i++) {
						String heading = HEADINGS[i];
						cell = row.createCell(i);
						cell.setCellValue(heading);
						cell.setCellStyle(cellStyle);
					}
					sheet.createFreezePane(0,1); // this will freeze the header row
					page_num++;
				}

				String s4 = (String) line[4];
				if (s4.compareTo("NCI") == 0) {
					w.add("NCI Line number: " + r);
				} else if (s4.compareTo("NCI") != 0) {
					r++;
					row = sheet.createRow((short) r);
					row_count++;

					row = sheet.createRow((short) r);
					row_count++;

					for (int i=0; i<HEADINGS.length; i++) {
						cell = row.createCell(i);
						int codeCol = codeColumn[i];
						cell.setCellValue(line[i]);
						if (NCIT_LINK && codeCol == LINK_NCIT) {
							url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							String code = line[i];
							url_link.setAddress(getNCItHyperlink(code));
							cell.setHyperlink(url_link);
							cell.setCellStyle(linkCellStyle);
						} else if (NCIM_LINK && codeCol == LINK_NCIM) {
							url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							String code = line[i];
							url_link.setAddress(getNCImHyperlink(code));
							cell.setHyperlink(url_link);
							cell.setCellStyle(linkCellStyle);
						} else if (SOURCE_LINK && codeCol == LINK_OTHER) {
							url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							String code = line[i];
							String source = line[SOURCE_INDEX];
							if (localNameMap.containsKey(source)) {
								url_link.setAddress(getSourceHyperlink(source, code));
								cell.setHyperlink(url_link);
								cell.setCellStyle(linkCellStyle);
							}
						}
					}
				}
			    lcv++;
			}


			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(outputfile);
			wb.write(fileOut);
			fileOut.close();
			System.out.println("Output file " + outputfile + " generated.");
			System.out.println("row_count: " + row_count);
			System.out.println("NCI: " + w.size());
			for (int i=0; i<w.size(); i++) {
				String t = (String) w.elementAt(i);
				System.out.println(t);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

	public String getOutputFile(String inputfile, String format) {
		int n = inputfile.lastIndexOf(".");
		String outputfile = inputfile.substring(0, n) + "." + format;
		return outputfile;
	}

    public void runXSSF(String inputfile) {
		int size = checkSpecialCharacters(inputfile);

		int n = inputfile.lastIndexOf(".");
		//String outputfile = inputfile.substring(0, n) + ".xlsx";
		String outputfile = getOutputFile(inputfile, ".xlsx");

		try {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFCreationHelper helper = null;

			XSSFCellStyle cellStyle = wb.createCellStyle();
			XSSFFont font = wb.createFont();
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			cellStyle.setFont(font);

			XSSFCellStyle linkCellStyle = wb.createCellStyle();
			XSSFFont linkfont = wb.createFont();

			//XSSFColor color = new XSSFColor(Color.LIGHT_BLUE);
			XSSFColor color = new XSSFColor(Color.BLUE);
            linkfont.setColor(color);

			//linkfont.setColor(XSSFColor.LIGHT_BLUE.index);
			linkCellStyle.setFont(linkfont);
			CSVReader reader = new CSVReader(new FileReader(inputfile));//CSV file
			String[] line;
			int r = 0;
			Cell cell = null;

			XSSFHyperlink url_link = null;
			XSSFSheet sheet = null;

			int page_num = 1;
			Row row = null;
			int lcv = 0;
			int row_count = 0;

			try {

				while ((line = reader.readNext()) != null) {
					if (lcv % PAGE_SIZE == 0) {
						r = 0;
						String sheetLabel = SHEET_LABEL;
						if (size > PAGE_SIZE) {
							sheetLabel = sheetLabel + " (Page " + page_num + ")";
						}
						//System.out.println("Creating " + sheetLabel);
						sheet = wb.createSheet(sheetLabel);
						helper = sheet.getWorkbook().getCreationHelper();
						url_link = helper.createHyperlink(XSSFHyperlink.LINK_URL);

						row = sheet.createRow((short) r);
						for (int i=0; i<HEADINGS.length; i++) {
							String heading = HEADINGS[i];
							cell = row.createCell(i);
							cell.setCellValue(heading);
							cell.setCellStyle(cellStyle);
						}
						page_num++;

					} else {
						String s4 = (String) line[4];
						s4 = s4.trim();
						r++;
						row = sheet.createRow((short) r);
						row_count++;
						cell = row.createCell(0);
						String ncit_code = line[0];
						cell.setCellValue(ncit_code);
						if (NCIT_LINK) {
							url_link = helper.createHyperlink(XSSFHyperlink.LINK_URL);
							url_link.setAddress(getNCItHyperlink(ncit_code));
							cell.setHyperlink(url_link);
							cell.setCellStyle(linkCellStyle);
						}

						cell = row.createCell(1);
						String name = line[1];
						cell.setCellValue(line[1]);

						cell = row.createCell(2);
						cell.setCellValue(line[2]);
						if (NCIM_LINK) {
							String s2 = line[2];
							s2 = s2.trim();
							if (s2.length() > 0) {
								url_link = helper.createHyperlink(XSSFHyperlink.LINK_URL);
								url_link.setAddress(getNCImHyperlink(s2));
								cell.setHyperlink(url_link);
								cell.setCellStyle(linkCellStyle);
							}
						}

						cell = row.createCell(3);
						String ncim_name = line[3];
						cell.setCellValue(line[3]);

						cell = row.createCell(4);
						cell.setCellValue(line[4]);

						cell = row.createCell(5);
						String atom_name = (String) line[5];
						cell.setCellValue(line[5]);

						cell = row.createCell(6);
						cell.setCellValue(line[6]);

						if (SOURCE_LINK) {
							if (s4.length() > 0) {
								String s6 = (String) line[6];
								if (localNameMap.containsKey(s4)) {
									url_link = helper.createHyperlink(XSSFHyperlink.LINK_URL);
									s4 = (String) localNameMap.get(s4);
									url_link.setAddress(getSourceHyperlink(s4, s6));
									cell.setHyperlink(url_link);
									cell.setCellStyle(linkCellStyle);
								}
							}
						}
						cell = row.createCell(7);
						cell.setCellValue(line[7]);
					}
					lcv++;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(outputfile);
			wb.write(fileOut);
			fileOut.close();
			System.out.println("Output file " + outputfile + " generated.");
			System.out.println("row_count: " + row_count);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    public String[] toArray(String line) {
		if (line == null) return null;
		Vector v = parseData(line);
		String[] a = new String[v.size()];
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			a[i] = t;
		}
		return a;
	}


    public void setDefault() {
	    setHeadings(HEADINGS);
	    int[] codeColumn = new int[8];
	    codeColumn[0] = 1;
	    codeColumn[1] = 0;
	    codeColumn[2] = 2;
	    codeColumn[3] = 0;
	    codeColumn[4] = 0;
	    codeColumn[5] = 0;
	    codeColumn[6] = 3;
	    codeColumn[7] = 0;
	    setCodeColumn(codeColumn);
	    setSOURCE_INDEX(4);
	    initialize();
	}

    public void runHSSF(Vector data_vec, String outputfile) {
		if (codeColumn == null) {
			setDefault();
		}

		//int size = 0;//checkSpecialCharacters(inputfile);
		int size = data_vec.size();
		String[] line = null;
		try {
            HSSFWorkbook wb = new HSSFWorkbook();

			HSSFCellStyle cellStyle = wb.createCellStyle();
			//cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
			//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			//font.setColor(HSSFColor.WHITE.index);
			cellStyle.setFont(font);

			HSSFCellStyle linkCellStyle = wb.createCellStyle();
			//cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
			//cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			HSSFFont linkfont = wb.createFont();
			linkfont.setColor(HSSFColor.LIGHT_BLUE.index);
			linkCellStyle.setFont(linkfont);

			//CreationHelper helper = wb.getCreationHelper();
			//CSVReader reader = new CSVReader(new FileReader(inputfile));//CSV file

			int r = 0;
			Cell cell = null;

			//"C7419","Acanthoma","C0846967","Acanthoma","MDR","Acanthoma","10059394","LT"
			// skip heading
			HSSFHyperlink url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
			HSSFSheet sheet = null;
			int page_num = 1;
			Row row = null;
			//line = reader.readNext();
			int lcv = 0;
			int row_count = 0;
			Vector w = new Vector();
			String nextLine = null;
			Vector values = null;
			for (int k=0; k<data_vec.size(); k++) {
				nextLine = (String) data_vec.elementAt(k);
				int k1 = k+1;
				line = toArray(nextLine);
                if (lcv % PAGE_SIZE == 0) {
					r = 0;
					String sheetLabel = SHEET_LABEL;
					if (size > PAGE_SIZE) {
					    sheetLabel = sheetLabel + " (Page " + page_num + ")";
					}
					sheet = wb.createSheet(sheetLabel);

					for (int j = 0; j < HEADINGS.length; j++) {
                    	sheet.autoSizeColumn(j);
                	}

					row = sheet.createRow((short) r);
					for (int i=0; i<HEADINGS.length; i++) {
						String heading = HEADINGS[i];
						cell = row.createCell(i);
						cell.setCellValue(heading);
						cell.setCellStyle(cellStyle);
					}
					sheet.createFreezePane(0,1); // this will freeze the header row
					page_num++;

				} //else {
				String s4 = line[4];
				if (s4.compareTo("NCI") == 0) {
					w.add("NCI Line number: " + r);
				} else { //if (s4.compareTo("NCI") != 0) {
					r++;
					row = sheet.createRow((short) r);
					row_count++;

					row = sheet.createRow((short) r);
					row_count++;

					for (int i=0; i<HEADINGS.length; i++) {
						cell = row.createCell(i);
						int codeCol = codeColumn[i];
						cell.setCellValue(line[i]);
						if (NCIT_LINK && codeCol == LINK_NCIT) {
							url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							String code = line[i];
							url_link.setAddress(getNCItHyperlink(code));
							cell.setHyperlink(url_link);
							cell.setCellStyle(linkCellStyle);
						} else if (NCIM_LINK && codeCol == LINK_NCIM) {
							url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							String code = line[i];
							url_link.setAddress(getNCImHyperlink(code));
							cell.setHyperlink(url_link);
							cell.setCellStyle(linkCellStyle);
						} else if (SOURCE_LINK && codeCol == LINK_OTHER) {
							url_link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
							String code = line[i];
							String source = line[SOURCE_INDEX];
							if (localNameMap.containsKey(source)) {
								url_link.setAddress(getSourceHyperlink(source, code));
								cell.setHyperlink(url_link);
								cell.setCellStyle(linkCellStyle);
							}
						}
					}
				}
			    //}
			    lcv++;
			}


			// Write the output to a file
			FileOutputStream fileOut = new FileOutputStream(outputfile);
			wb.write(fileOut);
			fileOut.close();
			System.out.println("Output file " + outputfile + " generated.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }


    public static void main(String[] args) throws IOException {
		CSVtoExcel CSVtoExcel = new CSVtoExcel();
		String inputfile = args[0];
		String format = "xls";
		if (args.length == 2) {
		    format = args[1];
	    }
	    if (format.compareTo(CSVtoExcel.XLSX) == 0) {
			CSVtoExcel.runXSSF(inputfile);
		} else {
			//CSVtoExcel.runHSSF(inputfile);
		}
	}

}

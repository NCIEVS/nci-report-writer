package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.text.*;
import java.util.*;


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


public class HyperlinkAppender {
	static String TITLE = "title";
	static String H1 = "h1";
	static String SCRIPT = "script";
	static String IMAGE_SRC = "<img src=";
	static String ANCHOR = "a";
	HashMap cuiMap = null;
	HashMap neoplasticStatusMap = null;


	public HyperlinkAppender() {

	}


    // final.csv
	public void setCUIMap(HashMap hmap) {
		this.cuiMap = hmap;
	}

	public void setNeoplasticStatusMap(HashMap hmap) {
		this.neoplasticStatusMap = hmap;
	}

    //"C7419","Acanthoma","C0846967","Acanthoma","NDFRT","FN","N0000011094","Acanthoma [Disease/Finding]"
	public HashMap loadCUIMap(String mappingFile) {
        HashMap hmap = new HashMap();
        try {
			CSVReader reader = new CSVReader(new FileReader(mappingFile));//CSV file
			String[] values;
			values = reader.readNext();
			while ((values = reader.readNext()) != null) {
				String code = values[0];
				String cui = values[2];
				hmap.put(code, cui);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.cuiMap = hmap;
		return hmap;
	}

    public String getLabel(String status) {
		if (status.compareTo("M") == 0) {
			return "Malignant";
		} else if (status.compareTo("B") == 0) {
			return "Benign";
		} else {
			return "Undetermined";
		}
	}

	public HashMap loadNeoplasticStatusMap(String csvfile) {
        HashMap hmap = new HashMap();
        try {
			CSVReader reader = new CSVReader(new FileReader(csvfile));//CSV file
			String[] values;
			values = reader.readNext();
			while ((values = reader.readNext()) != null) {
				String code = values[0];
				String status = values[4];
				if (status.compareTo("Malignant") == 0) {
					hmap.put(code, "M");
				} else if (status.compareTo("Benign") == 0) {
					hmap.put(code, "B");
				} else {
					if (status.compareTo("Undetermined") != 0) {
						System.out.println("status: " + status);
					}
					hmap.put(code, "U");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.neoplasticStatusMap = hmap;
		return hmap;
    }

    public String openTag(String tag) {
		return "<" + tag;
	}

    public String closeTag(String tag) {
		return "</" + tag + ">";
	}



	public static void printStyle(PrintWriter out) {
		out.println("<style>");
		out.println(".icon_blue {");
		out.println("    border: 0px;");
		out.println("    background-color: #1B5C92;");
		out.println("    background-image: url(/images/bg_rounded.gif);");
		out.println("    background-repeat: no-repeat;");
		out.println("    vertical-align:text-bottom;");
		out.println("}");
		out.println("</style>");
	}

	public static void printViewInHierarchyFunction(PrintWriter out) {
		out.println("    function view_in_hierarcy(code) {");
		out.println("        window.open('https://nciterms.nci.nih.gov/ncitbrowser/ajax?action=search_hierarchy&ontology_node_id='");
		out.println("           + code + '&ontology_display_name=NCI_Thesaurus', '_blank','top=100, left=100, height=740, width=680, status=no, menubar=no, resizable=yes, scrollbars=yes, toolbar=no, location=no, directories=no');");
		out.println("    }");
	}

    public void addJavaScript(PrintWriter out) {
		out.println("	function on_cui_clicked(code) {");
		out.println("	    var url = \"https://ncim.nci.nih.gov/ncimbrowser/ConceptReport.jsp?dictionary=NCI%20Metathesaurus&code=\" + code;");
		out.println("		window.open(url, '_blank', 'top=100, left=100, height=740, width=780, status=no, menubar=yes, resizable=yes, scrollbars=yes, toolbar=yes, location=no, directories=no');");
		out.println("	}");
		out.println("");
		out.println("	function on_source_code_clicked(source, code) {");
		out.println("	    var url = \"https://nciterms.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=\" + source + \"&code=\" + code;");
		out.println("		window.open(url, '_blank', 'top=100, left=100, height=740, width=780, status=no, menubar=yes, resizable=yes, scrollbars=yes, toolbar=yes, location=no, directories=no');");
		out.println("	}");
		out.println("");
		out.println("	function on_vocabulary_home_clicked(source) {");
		out.println("	    var url = \"https://nciterms.nci.nih.gov/ncitbrowser/pages/vocabulary.jsf?dictionary=\" + source;");
		out.println("		window.open(url, '_blank', 'top=100, left=100, height=740, width=780, status=no, menubar=yes, resizable=yes, scrollbars=yes, toolbar=yes, location=no, directories=no');");
		out.println("	}");
	}




	public void appendHyperlinks(PrintWriter out, String code) {
		String cui = (String) cuiMap.get(code);
		String status = (String) neoplasticStatusMap.get(code);

		if (cui == null && status == null) {
			return;
		}

		out.println("&nbsp;");
		out.println("(");
		if (cui != null) {
			String cui_link = getHyperlink("on_cui_clicked", cui);
			out.println(cui_link);
	    }

		if (status != null) {
			out.println(",&nbsp;");
			String title = getLabel(status);
			out.println("<a title=" + title + ">" + status + "</a>");
			//out.println(",&nbsp;");
	    }
		out.println(")");
	}

    public static String getHyperlink(String methodName, String t1) {
        return "<a href=\"#\" onclick=\"" + methodName + "('" + t1 + "');return false;\" >" + t1 + "</a>";
	}

    public void printNote(PrintWriter pw) {
		pw.println("<div>");
		pw.println("<ul>");
		pw.println("<li>	Concept Data Format: NCIt Preferred Term NCIt Code (NCIm CUI , Neoplastic_Status)</li>");
		pw.println("<li>	Neoplastic Status Codes: M: Malignant; B: Benign; U: Undetermined</li>");
		pw.println("</ul>");
		pw.println("</div>");
	}

	public void runHTML(String htmlfile, String outputfile) {
		PrintWriter pw = null;


/*
<title>Core Hierarchy: Neoplastic Status</title>
<title>Core Hierarchy Plus: Neoplastic Status</title>
<title>Core Hierarchy: Site + Morphology</title>
<title>Core Hierarchy: Site + Morphology</title>
*/


		try {
			FileReader a = new FileReader(htmlfile);
			BufferedReader br = new BufferedReader(a);
			pw = new PrintWriter(outputfile, "UTF-8");

			String line;
			line = br.readLine();
			String code = null;
			String label = null;
			int level = 0;
			while(line != null){
				String s = line;
				s = s.trim();
				if (s.startsWith(openTag(TITLE))) {
					if (s.indexOf("Neoplastic Status") != -1) {
						s = "<title>Core Hierarchy Plus: Neoplastic Status</title>";
					} else {
						//09132016
						s = "<title>Core Hierarchy Plus: Site + Morphology</title>";
					}
					line = s;

					//printStyle(pw);
					//s = s.replaceAll("Core Hierarchy:", "Core Hierarchy Core:");
					//pw.println(s);

				} else if (s.startsWith(openTag(H1))) {
					//s = s.replaceAll("NCIt Neoplasm Core Hierarchy", "NCIt Neoplasm Core Hierarchy Plus");
					//pw.println(s);

					if (s.indexOf("Neoplastic Status") != -1) {
						s = "<h1>NCIt Neoplasm Core Hierarchy Plus by Neoplastic Status</h1>";
					} else {
						s = "<h1>NCIt Neoplasm Core Hierarchy Plus by Site and Morphology</h1>";
					}
					line = s;


				} else if (s.startsWith(closeTag(SCRIPT))) {
					printViewInHierarchyFunction(pw);
					addJavaScript(pw);
				} else if (s.indexOf("expandcontractdiv") != -1) {
					printNote(pw);
				}

				pw.println(line);

				if (s.startsWith(IMAGE_SRC) && s.endsWith(closeTag(ANCHOR))) {
					int n = line.lastIndexOf("<");
					String t = line.substring(0, n);
					n = t.lastIndexOf(">");
					code = t.substring(n+1, t.length());
					appendHyperlinks(pw, code);
				}
				line = br.readLine();
			}

		} catch (Exception ex) {
            ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	public void runASCII(String tree_file, String outputfile) {
		Vector tree_data = FileUtils.readFile(tree_file);
        PrintWriter pw = null;
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			for (int i=0; i<tree_data.size(); i++) {
				String t = (String) tree_data.elementAt(i);
				String code = getCode(t);
				String status = (String) neoplasticStatusMap.get(code);
				if (status != null) {
					t = t + " (" + status + ")";
				}
				pw.println(t);
			}
		} catch (Exception ex) {

		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
		    } catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public String getCode(String line) {
		//	B Lymphoblastic Leukemia/Lymphoma (C8936)
        int n = line.lastIndexOf("(");
        String t = line.substring(n+1, line.length()-1);
        return t;
	}
}


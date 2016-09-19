package gov.nih.nci.evs.app.neopl;


import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;


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


public class MappingHyperlinkUtils {
    Vector replaced_from_vec = null;
    Vector replaced_by_vec = null;

    public MappingHyperlinkUtils() {
		replaced_from_vec = new Vector();
		replaced_by_vec = new Vector();
    }

    public static String getHyperlink(String methodName, String t1) {
        return "<a href=\"#\" onclick=\"" + methodName + "('" + t1 + "');return false;\" >" + t1 + "</a>";
	}

    public static String getHyperlink(String methodName, String t1, String t2) {
        return "<a href=\"#\" onclick=\"" + methodName + "('" + t1 + "', '" + t2 + "');return false;\" >" + t1 + ":" + t2 + "</a>";
	}


//<img src="images/dot.gif" id="IMG_N_1_582_583" alt="show_hide" onclick="show_hide('DIV_N_1_582_583');" >		Acute Myeloid Leukemia with inv(16)(p13q22)(CBFb/MYH11)&nbsp;<a href="#" onclick="on_node_clicked('NCI:C9018, SY, C153317');return false;" tabindex="612">NCI:C9018, SY, C153317</a>


    public static Vector getParameters(String t) {
        Vector w = new Vector();
		int n = t.indexOf("&nbsp;");
		String s = t.substring(n+6, t.length());

		n = s.indexOf("(");
		s = s.substring(n+2, s.length());

		n = s.indexOf(";");
		s = s.substring(0, n-2);
		Vector u = StringUtils.parseData(s, ",");
		String s1 = (String) u.elementAt(0);
		s1 = s1.trim();

		if (u.size() == 1) {
			return null;
		}

		Vector v = StringUtils.parseData(s1, ":");
		String source = (String) v.elementAt(0);
		String source_code = (String) v.elementAt(1);

		w.add(source);
		w.add(source_code);


		String s2 = (String) u.elementAt(1);
		s2 = s2.trim();
		w.add(s2);
		String s3 = (String) u.elementAt(2);
		s3 = s3.trim();
		w.add(s3);

		return w;
    }


	public Vector getAvailableSources() {
		Vector w = new Vector();
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		CodingSchemeUtils codingSchemeUtils = new CodingSchemeUtils(lbSvc);
		HashMap localNameMap = codingSchemeUtils.getLocalName2CodingSchemeNameHashMap();
		Iterator it = localNameMap.keySet().iterator();
		while (it.hasNext()) {
			String localName = (String) it.next();
			String codingSchemeName = (String) localNameMap.get(localName);
			w.add(localName + "|"+ codingSchemeName);
		}
		return w;
	}


	public static Vector getAvailableSources(String inputfile) {
		Vector w = new Vector();
		Vector v = FileUtils.readFile(inputfile);
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		CodingSchemeUtils codingSchemeUtils = new CodingSchemeUtils(lbSvc);
		HashMap localNameMap = codingSchemeUtils.getLocalName2CodingSchemeNameHashMap();

		for (int i=0; i<v.size(); i++) {
			String line = (String) v.elementAt(i);
			if (line.startsWith("<img src")) {
				int n = line.indexOf("&nbsp;");
				if (n != -1) {

					String s1 = line.substring(0, n);
					Vector params = getParameters(line);
					if (params != null) {
						String source = (String) params.elementAt(0);
						String code = (String) params.elementAt(1);
						String type = (String) params.elementAt(2);
						String cui = (String) params.elementAt(3);

						if (localNameMap.containsKey(source)) {
							String value = (String) localNameMap.get(source);
							String t = source + " (" + value + ")";
							if (!w.contains(t)) {
								w.add(t);
							}
						}
					}

				}
			}
		}
		w = SortUtils.quickSort(w);
		return w;
	}

//</script>
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

    public void addContent(PrintWriter pw, Vector avail_sources) {
		pw.println("<div>");
		pw.println("<h2>Includes These EVS Standalone Sources</h2>");
		pw.println("<ul>");
		for (int i=0; i<avail_sources.size(); i++) {
			String t = (String) avail_sources.elementAt(i);
			pw.println("<li><a href=\"#\" onclick=\"on_vocabulary_home_clicked('" + t + "');return false;\" >" + t + "</a></li>");
		}
		pw.println("</ul>");

		pw.println("<p>Other sources shown are only available in EVS as part of NCIm.");
		pw.println("<br>NCIt Core concepts with no mappings do not expand.</p>");

		pw.println("</div>");
	}

	public void setStringReplacement(String from, String by) {
		replaced_from_vec.add(from);
		replaced_by_vec.add(by);
	}

	private String replaceAll(String line) {
		if (line == null) return null;
		if (line.length() == 0) return line;
		if (replaced_from_vec.size() == 0) return line;
		for (int i=0; i<replaced_from_vec.size(); i++) {
			String from = (String) replaced_from_vec.elementAt(i);
			String by = (String) replaced_by_vec.elementAt(i);
			line = line.replaceAll(from, by);
		}
		return line;
	}


	public void run(String inputfile, String outputfile) {
		//String inputfile  = "neoplasm_core_mapping.html";
		Vector avail_sources = getAvailableSources(inputfile);
		PrintWriter pw = null;
		Vector v = FileUtils.readFile(inputfile);
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		CodingSchemeUtils codingSchemeUtils = new CodingSchemeUtils(lbSvc);

		HashMap localNameMap = codingSchemeUtils.getLocalName2CodingSchemeNameHashMap();
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			for (int i=0; i<v.size(); i++) {
				String line = (String) v.elementAt(i);
				line = replaceAll(line);
				if (line.indexOf("expandcontractdiv") != -1) {
					addContent(pw, avail_sources);
				} else if (line.startsWith("</script>")) {
					addJavaScript(pw);
				} else if (line.startsWith("<img src")) {
					int n = line.indexOf("&nbsp;");
					if (n != -1) {

						String s1 = line.substring(0, n);
						Vector params = getParameters(line);
						if (params != null) {
							String source = (String) params.elementAt(0);
							String code = (String) params.elementAt(1);
							String type = (String) params.elementAt(2);
							String cui = (String) params.elementAt(3);

							if (localNameMap.containsKey(source)) {
								String value = (String) localNameMap.get(source);
								line = s1 + "&nbsp;(" + getHyperlink("on_source_code_clicked", source, code)
								   + ", " + type + ", " + getHyperlink("on_cui_clicked", cui) + ")";
							} else {
								line = s1 + "&nbsp;(" + source + ":" + code
								   + ", " + type + ", " + getHyperlink("on_cui_clicked", cui) + ")";
							}
					    }

				    }
				}
				pw.println(line);
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


	public static void main(String[] args) {
		String inputfile = args[0];
		String outputfile = args[1];
		new MappingHyperlinkUtils().run(inputfile, outputfile);
	}

}


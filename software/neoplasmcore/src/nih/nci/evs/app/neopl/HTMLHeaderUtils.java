package gov.nih.nci.evs.app.neopl;


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


public class HTMLHeaderUtils {
    //static String Constants.DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	public void writePageContent(PrintWriter out, String treefile) {
		Vector v = FileUtils.readFile(treefile);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			out.println(t);
		}
	}


	public Vector getPageContent(String contentFile) {
		Vector v = FileUtils.readFile(contentFile);
        return v;
	}



	public void writePageContent(PrintWriter out, Vector v) {
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			out.println(t);
		}
	}

	public void run(String contentFile, String outputfile, String title, String ncit_version, Vector content_vec) {
	    run(contentFile, outputfile, title, title, ncit_version, content_vec);
    }

	public void run(String contentFile, String outputfile, String title, String label, String ncit_version, Vector content_vec) {
        PrintWriter out = null;
        try {
			Vector tree_data = getPageContent(contentFile);
			out = new PrintWriter(outputfile, "UTF-8");
			writeHeader(out, title);
            writeScript(out);
            writeBody(out, label, ncit_version, content_vec);
            writeTreeActions(out);
            //writePageContent(out, contentfile);
            writePageContent(out, tree_data);

		} catch (Exception ex) {

		} finally {
			try {
				out.close();
				System.out.println("Output file " + outputfile + " generated.");
		    } catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	public void writeHeader(PrintWriter out, String title) {
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
		out.println("<html xmlns:c=\"http://java.sun.com/jsp/jstl/core\">");
		out.println("<head>");
		out.println("<title>" + title + "</title>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
	}



   public void writeScript(PrintWriter out) {
		out.println("<script type=\"text/javascript\">");
		out.println("");
		out.println("	function show_hide(div_id) {");
		out.println("		var img_id = \"IMG_\" + div_id.substring(4, div_id.length);");
		out.println("		var img_obj = document.getElementById(img_id);");
		out.println("		if (img_obj.getAttribute(\"src\").indexOf(\"minus\") != -1) {");
		out.println("			document.getElementById(div_id).style.display = \"none\";");
		out.println("		} else if (img_obj.getAttribute(\"src\").indexOf(\"plus\") != -1) {");
		out.println("			document.getElementById(div_id).style.display = \"block\";");
		out.println("		}");
		out.println("		changeImage(img_id);");
		out.println("	}");
		out.println("");
		out.println("	function changeImage(img_id) {");
		out.println("		var img_obj = document.getElementById(img_id);");
		out.println("		if (img_obj.getAttribute(\"src\").indexOf(\"minus\") != -1) {");
		out.println("			var s = img_obj.getAttribute(\"src\");");
		out.println("			s = s.replace(\"minus\", \"plus\");");
		out.println("			img_obj.setAttribute(\"src\", s);");
		out.println("		} else if (img_obj.getAttribute(\"src\").indexOf(\"plus\") != -1) {");
		out.println("			var s = img_obj.getAttribute(\"src\");");
		out.println("			s = s.replace(\"plus\", \"minus\");");
		out.println("			img_obj.setAttribute(\"src\", s);");
		out.println("		}");
		out.println("	}");
		out.println("");
		out.println("	function show(div_id) {");
		out.println("		var img_id = \"IMG_\" + div_id.substring(4, div_id.length);");
		out.println("		var img_obj = document.getElementById(img_id);");
		out.println("		if (img_obj.getAttribute(\"src\").indexOf(\"plus\") != -1) {");
		out.println("			document.getElementById(div_id).style.display = \"block\";");
		out.println("			changeImage(img_id);");
		out.println("		}");
		out.println("	}");
		out.println("");
		out.println("	function hide(div_id) {");
		out.println("		var img_id = \"IMG_\" + div_id.substring(4, div_id.length);");
		out.println("		var img_obj = document.getElementById(img_id);");
		out.println("		if (img_obj.getAttribute(\"src\").indexOf(\"minus\") != -1) {");
		out.println("			document.getElementById(div_id).style.display = \"none\";");
		out.println("			changeImage(img_id);");
		out.println("		}");
		out.println("	}");
		out.println("");
		out.println("	function expand_node(prefix) {");
		out.println("		var div = \"DIV_\";");
		out.println("		var child_cnt = 1;");
		out.println("		child_id = prefix.concat(child_cnt.toString());");
		out.println("		while (document.getElementById(div.concat(child_id)) != null) {");
		out.println("			show(div.concat(child_id));");
		out.println("			expand_node(child_id.concat(\"_\"));");
		out.println("			child_cnt++;");
		out.println("			child_id = prefix.concat(child_cnt.toString());");
		out.println("		}");
		out.println("	}");
		out.println("");
		out.println("	function getLevel(div) {");
		out.println("		var n = div.length;");
		out.println("		var c;");
		out.println("		var knt = 0;");
		out.println("		for (var i=0; i<n; i++) {");
		out.println("		   c = div.charAt(i);");
		out.println("		   if (c == \"_\") {");
		out.println("			knt++;");
		out.println("		   }");
		out.println("		}");
		out.println("		return knt-1;");
		out.println("	}");
		out.println("");
		out.println("");
		out.println("	function collapse_all() {");
		out.println("		var divTags = document.getElementsByTagName('div');");
		out.println("		for (var i=0;i<divTags.length;i++) {");
		out.println("			if (divTags[i].id.indexOf(\"DIV_N_\") == 0) {");
		out.println("				hide(divTags[i].id);");
		out.println("			}");
		out.println("		}");
		out.println("	}");
		out.println("	function expand_all() {");
		out.println("		var divTags = document.getElementsByTagName('div');");
		out.println("		for (var i=0;i<divTags.length;i++) {");
		out.println("			if (divTags[i].id.indexOf(\"DIV_N_\") == 0) {");
		out.println("				show(divTags[i].id);");
		out.println("			}");
		out.println("		}");
		out.println("	}");
		out.println("");
      out.println("    function expand(level) {");
      out.println("        expand_all();");
      out.println("        collapse_all();");
      out.println("        var divTags = document.getElementsByTagName('div');");
      out.println("		   for (var i=0;i<divTags.length;i++) {");
      out.println("		       var div_id = divTags[i].id;");
      out.println("		       if (divTags[i].id.indexOf(\"DIV_N_\") >= 0) {");
      out.println("				   if (divTags[i].id.indexOf(\"DIV_N_\") == 0) {");
      out.println("					   if (getLevel(div_id) <= level) {");
      out.println("						   document.getElementById(div_id).style.display = \"block\";");
      out.println("						   var img_id = \"IMG_\" + div_id.substring(4, div_id.length);");
      out.println("						   changeImage(img_id);");
      out.println("					   }");
      out.println("				   }");
      out.println("		       }");
      out.println("		   }");
      out.println("    }");
		out.println("");
		out.println("	function on_node_clicked(code) {");
		out.println("	    var url = \"https://nciterms.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus&code=\" + code;");
		out.println("		window.open(url, '_blank', 'top=100, left=100, height=740, width=780, status=no, menubar=yes, resizable=yes, scrollbars=yes, toolbar=yes, location=no, directories=no');");
		out.println("	}");
		out.println("");
		out.println("	function expand_tree() {");
		out.println("	    var level = document.getElementById('level').value;");
		out.println("	    expand(level);");
		out.println("	}");
		out.println("");
		out.println("");
		out.println("</script>");
		out.println("</head>");
	}


    public void writeBody(PrintWriter out, String title, String ncit_version, Vector content_vec) {
		out.println("<body onload=\"collapse_all();\">");
		out.println("<center>");
		out.println("<h1>" + title + "</h1>");
		out.println("<p>");

		//NCIt Version: 16.04d (2016-06-06)
		//out.println("<h2>" + getToday() + "</h2>");
		if (ncit_version != null) {
			out.println("<h2>" + "NCIt Version: " + ncit_version + " (" + getToday() + ")</h2>");
		} else {
			out.println("<h2>" + "(" + getToday() + ")</h2>");
		}

		out.println("</p>");
		out.println("</center>");

		if (content_vec != null) {
			out.println("<div>");
			out.println("<h2>Contents</h2>");
			out.println("<ul>");
			for (int i=0; i<content_vec.size(); i++) {
				String content = (String) content_vec.elementAt(i);
				out.println("<li>\t" + content + "</li>");
			}
			out.println("</ul>");
			out.println("</div>");
	    }
	}

    public void writeEndTags(PrintWriter out) {
		out.println("<!-- contents -->");
		out.println("</body>");
		out.println("</html>");
	}


    public void writeTreeActions(PrintWriter out) {
		out.println("<div id=\"expandcontractdiv\">");
		out.println("");
		out.println("<a href=\"#\" onclick=\"expand_all();\" tabindex=\"1\" >Expand all</a>");
		out.println("&nbsp;");
		out.println("Expand&nbsp;");
		out.println("<select id=\"level\" onchange=\"expand_tree()\">");
		out.println("    <option value=\"0\" selected>0</option>");
		out.println("    <option value=\"1\">1</option>");
		out.println("    <option value=\"2\">2</option>");
		out.println("    <option value=\"3\">3</option>");
		out.println("    <option value=\"4\">4</option>");
		out.println("    <option value=\"5\">5</option>");
		out.println("</select>");
		out.println("&nbsp;");
		out.println("Levels");
		out.println("&nbsp;");
		out.println("<a href=\"#\" onclick=\"collapse_all();\" tabindex=\"2\">Collapse all</a>");
		out.println("</div>");
	}

	public String getToday() {
		return getToday(Constants.DEFAULT_DATE_FORMAT);
	}

	public String getToday(String format) {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(today);
	}
/*
//title: NCIt Core Neoplasm Terminology - Simple Draft Hierarchy
//contents: Neoplasm by Site, Neoplasm by Morphology
	public static void main(String[] args) {
		String title = "NCIt Neoplasm Core Hierarchy by Neoplastic Status";
		Vector contents = new Vector();
		//contents.add("Neoplasm by Site");
		//contents.add("Neoplasm by Morphology");

		contents.add("Malignant Neoplasm");
		contents.add("Benign Neoplasm");
		contents.add("Neoplasm of Intermediate or Uncertain Behavior, or Comprising Mixed Subtypes");

		String outputfile = "test.html";
		String contentFile = "neoplasm_core_mapping.html";
		new HTMLHeaderUtils().run(contentFile, outputfile, title, "16.04d", contents);
	}
*/
}


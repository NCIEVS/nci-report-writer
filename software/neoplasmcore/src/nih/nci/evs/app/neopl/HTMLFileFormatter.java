package gov.nih.nci.evs.app.neopl;


import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.sql.*;
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


public class HTMLFileFormatter {
    Vector replaced_from_vec = null;
    Vector replaced_by_vec = null;

    public HTMLFileFormatter() {
		replaced_from_vec = new Vector();
		replaced_by_vec = new Vector();
    }

	String getFileName(File f) {
		return f.getAbsolutePath();
	}

	public void dumpVector(Vector v) {
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
		}
	}

	public Vector getFileNamesInDir(String dir_name) {
		Vector v = new Vector();
		try {
			List<File> list = getFileList(new File(dir_name));
			for (int i=0; i<list.size(); i++) {
				File f = (File) list.get(i);
				v.add(getFileName(f));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v;
	}

	public List<File> getFileList(File file)
			throws FileNotFoundException {
		List<File> result = getUnsortedFileList(file);
		Collections.sort(result);
		return result;
	}

	private List<File> getUnsortedFileList(File file)
			throws FileNotFoundException {
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = file.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		int dirKey = 0;

		for (File fileList : filesDirs) {
			result.add(fileList);
			if (!fileList.isFile()) {

				List<File> deeperList = getUnsortedFileList(fileList);
				result.addAll(deeperList);
			}
		}
		return result;
	}


//<img src="images/dot.gif" id="IMG_N_1_582_583" alt="show_hide" onclick="show_hide('DIV_N_1_582_583');" >		Acute Myeloid Leukemia with inv(16)(p13q22)(CBFb/MYH11)&nbsp;<a href="#" onclick="on_node_clicked('NCI:C9018, SY, C153317');return false;" tabindex="612">NCI:C9018, SY, C153317</a>

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

	public void run(String inputfile) {
		run(inputfile, inputfile);
	}

	public void run(String inputfile, String outputfile) {
		PrintWriter pw = null;
		Vector v = FileUtils.readFile(inputfile);
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			for (int i=0; i<v.size(); i++) {
				String line = (String) v.elementAt(i);
				line = replaceAll(line);
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
		String user_dir = System.getProperty("user.dir");
		System.out.println("user_dir: " + user_dir);
		HTMLFileFormatter formatter = new HTMLFileFormatter();
		String from = "images/";
		String to = "";
		formatter.setStringReplacement(from, to);
		//formatter.run(inputfile, outputfile);

		String dir_name = "dir";
		Vector v = formatter.getFileNamesInDir(dir_name);
		//formatter.dumpVector(v);

		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
			formatter.run(t);
		}
	}

}


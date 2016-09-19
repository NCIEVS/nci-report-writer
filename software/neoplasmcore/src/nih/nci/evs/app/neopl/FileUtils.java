package gov.nih.nci.evs.app.neopl;


import java.io.*;
import java.text.DecimalFormat;
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


public class FileUtils {

    public static PrintWriter openPrintWriter(String outputfile) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pw;
	}

    public static void deleteFiles(String index_location) {
        File indexDir = new File(index_location);
        if (indexDir.exists()) {
			System.out.println("Deleting " + index_location);
			emptyAndDeleteDirectory(indexDir);
		}
	}

    public static void emptyAndDeleteDirectory(File dir) {
        File[] contents = dir.listFiles() ;
        if (contents != null) {
            for (File content : contents) {
                if (content.isDirectory()) {
                    emptyAndDeleteDirectory(content) ;
                } else {
                    content.delete() ;
                }
            }
        }
        dir.delete() ;
    }

    public static void closePrintWriter(PrintWriter pw) {
		try {
			pw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void dumpVector(Vector v) {
		dumpVector(null, "\n", v);
	}

	public static void dumpVector(String label, Vector v) {
		dumpVector(null, label, v);
	}

	public static void dumpVector(PrintWriter pw, String label, Vector v) {
		if (pw != null) {
			pw.println("\n" + label);
		} else {
			System.out.println("\n" + label);
		}
		for (int k=0; k<v.size(); k++) {
			String s = (String) v.elementAt(k);
			if (pw != null) {
				pw.println(s);
		    } else {
				System.out.println(s);
			}
		}
	}

	public static Properties loadProperties(String propertyFile) {
		try{
			Properties properties = new Properties();
			if(propertyFile != null && propertyFile.length() > 0){
				FileInputStream fis = new FileInputStream(new File(propertyFile));
				properties.load(fis);
			}
			for(Iterator i = properties.keySet().iterator(); i.hasNext();){
				String key = (String)i.next();
				String value  = properties.getProperty(key);
			}
			return properties;
		} catch (Exception e){
			System.out.println("Error reading properties file");
			e.printStackTrace();
			return null;
		}
	}

	public static String getPropertyValue(Properties properties, String key) {
	    return (String) properties.getProperty(key);
	}



	public static Vector readFile(String filename)
	{
		Vector v = new Vector();
		try {
			BufferedReader in = new BufferedReader(
			   new InputStreamReader(
						  new FileInputStream(filename), "UTF8"));
			String str;
			while ((str = in.readLine()) != null) {
				v.add(str);
			}
            in.close();
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return v;
	}

	public static String getStringSizeLengthFile(long size) {
		DecimalFormat df = new DecimalFormat("0.00");
		float sizeKb = 1024.0f;
		float sizeMo = sizeKb * sizeKb;
		float sizeGo = sizeMo * sizeKb;
		float sizeTerra = sizeGo * sizeKb;
		if(size < sizeMo)
			return df.format(size / sizeKb)+ " KB";
		else if(size < sizeGo)
			return df.format(size / sizeMo) + " MB";
		else if(size < sizeTerra)
			return df.format(size / sizeGo) + " GB";
		return "";
	}


	public static String getFileSize(String filename) {
		File file = new File(filename);
		if (!file.exists()) return "";
		//return getFileSize(file.length());
		return getStringSizeLengthFile(file.length());
	}


    public static List listFilesInFolder(File folder, String extension) {
		List list = new ArrayList();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				//listFilesForFolder(fileEntry);
			} else {
				if (extension != null) {
					String ext = getFileExtension(fileEntry.getName());
					if (ext.compareTo(extension) == 0) {
						list.add(fileEntry.getName());
					}

			    } else {
					list.add(fileEntry.getName());
				}
			}
		}
		return list;
	}

    public static List listFilesInFolder(File folder) {
		List list = new ArrayList();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				//listFilesForFolder(fileEntry);
			} else {
				list.add(fileEntry.getName());
			}
		}
		return list;
	}

    public static String getFileExtension(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (i > p) {
			extension = fileName.substring(i+1);
		}
		return extension;
	}

    public static String getFileContent(String filename) {
		if (filename == null) return null;
	    StringBuffer buf = new StringBuffer();
	    Vector v = readFile(filename);
	    for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			buf.append(t).append("\n");
		}
		return buf.toString();
	}

    public static String loadQuery(String filename) {
	    StringBuffer buf = new StringBuffer();
	    Vector sparql_data = readFile(filename);
	    for (int i=0; i<sparql_data.size(); i++) {
			String t = (String) sparql_data.elementAt(i);
			if (!t.startsWith("#")) {
			    buf.append(t).append("\n");
		    }
		}
		return buf.toString();
	}

    public static void query2Method(String filename) {
		PrintWriter out = new PrintWriter(System.out, true);
		query2Method(out, filename, null);
	}

    public static void query2Method(PrintWriter out, String filename, String method) {
		String s1 = "buf.append(\"";
		String s2 = "\").append(\"\\n\");";

		if (method == null) {
			method = "<method signature>";
		}
		Vector v = readFile(filename);
		out.println("\tpublic String " + method + " {");
		out.println("\t\tString prefixes = getPrefixes();");
		out.println("\t\tStringBuffer buf = new StringBuffer();");
		out.println("\t\tbuf.append(prefixes);");
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			if (!t.startsWith("PREFIX")) {
				t.replaceAll("\"", "\\\"");
				out.println("\t\t" + s1 + t + s2);
		    }
		}
		out.println("\t\treturn buf.toString();");
		out.println("\t}");
    }

    public static String loadPrefixes(String filename) {
		return getFileContent(filename);
	}

	public static void copyFile(String sourcefile, String targetfile) {
    	InputStream inStream = null;
	    OutputStream outStream = null;

    	try{
    	    File afile = new File(sourcefile);
    	    File bfile = new File(targetfile);

    	    inStream = new FileInputStream(afile);
    	    outStream = new FileOutputStream(bfile);

    	    byte[] buffer = new byte[1024];
     	    int length;

    	    while ((length = inStream.read(buffer)) > 0){
     	    	outStream.write(buffer, 0, length);
     	    }

    	    inStream.close();
    	    outStream.close();

    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }

}

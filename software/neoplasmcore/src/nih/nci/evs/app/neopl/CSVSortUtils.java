package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
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


public class CSVSortUtils {

    /**
     * Performs quick sort of a List by a specified sort option.
     *
     * @param list an instance of List
     * @param sort_option, an integer; 1, if sort by name; 2: if sort by code
     */
    public static void quickSort(List list, int sort_option) {
        if (list == null)
            return;
        if (list.size() <= 1)
            return;
        try {
            Collections.sort(list, new CSVSortComparator(sort_option));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void quickSort(List list, int[] sort_options) {
        if (list == null)
            return;
        if (list.size() <= 1)
            return;
        try {
            Collections.sort(list, new CSVSortComparator(sort_options));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Performs quick sort of a Vector by a specified sort option.
     *
     * @param v an instance of Vector
     * @param sort_option, an integer; 1, if sort by name; 2: if sort by code
     */

    public static Vector quickSort(Vector v, int sort_option) {
        if (v == null)
            return v;
        if (v.size() <= 1)
            return v;
        try {
            Collections.sort((List) v, new CSVSortComparator(sort_option));
            return v;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector quickSort(Vector v, int[] sort_options) {
        if (v == null)
            return v;
        if (v.size() <= 1)
            return v;
        try {
            Collections.sort((List) v, new CSVSortComparator(sort_options));
            return v;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static Vector quickSort(Vector v, int[] sort_options, String delim) {
        if (v == null)
            return v;
        if (v.size() <= 1)
            return v;
        try {
            Collections.sort((List) v, new CSVSortComparator(sort_options, delim));
            return v;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Enumeration<?> sort(Enumeration<?> enumeration) {
        if (enumeration == null)
            return enumeration;
        List keyList = Collections.list(enumeration);
        Collections.sort(keyList);
        enumeration = Collections.enumeration(keyList);
        return enumeration;
    }

    public static void sortCSV(String inputfile, int[] sort_options) {
		 sortCSV(inputfile, sort_options, true);
	}

    public static void sortCSV(String inputfile, int[] sort_options, boolean skip_heading) {
		Vector v = readCSV(inputfile, skip_heading, "$");
		String header = null;
		if (skip_heading) {
			String[] a = extractHeadings(inputfile);
			header = convertToDelimitedValue(a, "$");
		}
        v = quickSort(v, sort_options, "$");
        if (skip_heading) {
            v.add(0, header);
		}
        PrintWriter pw = null;
        try {
 			pw = new PrintWriter(inputfile, "UTF-8");
 			for (int i=0; i<v.size(); i++) {
 				String t = (String) v.elementAt(i);
 				pw.println(t);
 			}
  		} catch (Exception ex) {
            ex.printStackTrace();
 		} finally {
 			try {
 				pw.close();
 				System.out.println("Output file " + inputfile + " generated.");
 		    } catch (Exception ex) {
 				ex.printStackTrace();
 			}
 		}
	}

    public static List readCSV(String csvfile, boolean skip_heading) {
		ArrayList list = new ArrayList();
		int n = csvfile.lastIndexOf(".");
		String nextLine = null;
		Vector w = new Vector();
		String[] values = null;
		try {
			CSVReader reader = new CSVReader(new FileReader(csvfile));
			if (skip_heading) {
				values = reader.readNext();
			}
			while ((values = reader.readNext()) != null) {
				list.add(values);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return list;
	}

    public static Vector readCSV(String csvfile, boolean skip_heading, String to_delim) {
		List list_a = readCSV(csvfile, skip_heading);
        Vector v = convertToDelimitedValues(list_a, "$");
        return v;
	}

    public static Vector convertToDelimitedValues(List list_a, String delim) {
		Vector v = new Vector();
		int istart = 0;
		for (int i=0; i<list_a.size(); i++) {
			String[] a = (String[]) list_a.get(i);
			String s = convertToDelimitedValue(a, delim);
			v.add(s);
		}
		return v;
	}

    public static String convertToDelimitedValue(String[] a, String delim) {
		StringBuffer buf = new StringBuffer();
		String s = null;
		for (int j=0; j<a.length; j++) {
			s = (String) a[j];
			buf.append(s).append(delim);
		}
		s = buf.toString();
		s = s.substring(0, s.length()-1);
		return s;
	}

	public static String[] extractHeadings(String filename) {
		Vector v = FileUtils.readFile(filename);
		String heading = (String) v.elementAt(0);
		Vector u = StringUtils.parseData(heading, ",");
		String[] headings = new String[u.size()];
		for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
			t = t.substring(1, t.length()-1);
			headings[i] = t;
			System.out.println(t);
		}
		return headings;
	}

}




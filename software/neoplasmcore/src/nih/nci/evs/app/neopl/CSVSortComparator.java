package gov.nih.nci.evs.app.neopl;


import java.util.*;
import org.apache.log4j.*;


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


public class CSVSortComparator implements Comparator<Object> {
    private static Logger _logger = Logger.getLogger(CSVSortComparator.class);
    private static final int SORT_BY_NAME = 1;
    private static final int SORT_BY_CODE = 2;
    private int _sort_option = SORT_BY_NAME;
    private int[] _sort_options = null;
    private String delim = ",";

    public CSVSortComparator() {

    }

    public CSVSortComparator(int sort_option) {
        _sort_option = sort_option;
    }

    public CSVSortComparator(int[] sort_options) {
        _sort_options = sort_options;
    }

    public CSVSortComparator(int sort_option, String delim) {
        _sort_option = sort_option;
        this.delim = delim;
    }

    public CSVSortComparator(int[] sort_options, String delim) {
        _sort_options = sort_options;
        this.delim = delim;
    }

    public void debug(int option, String s1, String s2, String key1, String key2) {
		//System.out.println("s1: " + s1);
		//System.out.println("s2: " + s2);
		System.out.println("\toption: " + option);
		System.out.println("\tkey1: " + key1);
		System.out.println("\tkey2: " + key2);
		System.out.println("\t: " + key1.compareToIgnoreCase(key2));
		System.out.println("\n");
	}

    public Vector extractValuesFromCSV(String line, String delim) {
		if (delim.compareTo(",") == 0) {
			return extractValuesFromCSV(line);
		}
		return StringUtils.parseData(line, delim);
	}

    public Vector extractValuesFromCSV(String line) {
		/*
        if (line == null) {
	 	   line = "\"C3173\",\"Accelerated Phase Chronic Myelogenous Leukemia, BCR-ABL1 Positive\",\"C0023472\",\"Accelerated Phase Chronic Myelogenous Leukemia, BCR-ABL1 Positive\",\"NDFRT\",\"SY\",\"N0000003262\",\"Myeloid Leukemia, Chronic, Aggressive-Phase\"";
        }
        */
        if (line == null) return null;
        StringBuffer buf = new StringBuffer();
        Vector v = new Vector();
        for (int i=1; i<line.length(); i++) {
			char c = line.charAt(i);
			if (c == '\"') {
				String t = buf.toString();
				if (t.length() > 0 && t.compareTo(",") != 0) {
					v.add(t);
				}
				buf = new StringBuffer();
			} else {
				buf.append(c);
			}
		}
		String t = buf.toString();
		if (t.length() > 0 && t.compareTo(",") != 0) {
			v.add(t);
		}
		return v;
	}

    public int compare(Object object1, Object object2) {
        int sort_option = -1;
        String key1 = null;
        String key2 = null;
        String s1 = (String) object1;
        String s2 = (String) object2;
        Vector u1 = extractValuesFromCSV(s1, delim);
        Vector u2 = extractValuesFromCSV(s2, delim);

        for (int i=0; i<_sort_options.length; i++) {
            sort_option = _sort_options[i];
			key1 = (String) u1.elementAt(sort_option);
			key2 = (String) u2.elementAt(sort_option);
			if (key1.compareToIgnoreCase(key2) != 0) {
				return key1.compareToIgnoreCase(key2);
			}
		}
		return key1.compareToIgnoreCase(key2);
    }
}


/*
"NCIt Code","NCIt Preferred Term","NCIm CUI","NCIm Preferred Name","NCIm Source","Term Type","Source Code","Source Term"
"C7419","Acanthoma","C0846967","Acanthoma","NDFRT","FN","N0000011094","Acanthoma [Disease/Finding]"
"C7419","Acanthoma","C0846967","Acanthoma","NDFRT","PT","N0000011094","Acanthoma"
"C7419","Acanthoma","C0846967","Acanthoma","GARD","PT","8604","Acanthoma"
"C7419","Acanthoma","C0846967","Acanthoma","MDR","LLT","10059394","Acanthoma"
"C7419","Acanthoma","C0846967","Acanthoma"," "," "," "," "
"C7419","Acanthoma","C0846967","Acanthoma","MSH","PM","D049309","Acanthomas"
"C7419","Acanthoma","C0846967","Acanthoma","MSH","MH","D049309","Acanthoma"
"C7419","Acanthoma","C0846967","Acanthoma","MDR","PT","10059394","Acanthoma"
*/

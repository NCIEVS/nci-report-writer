package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
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


public class RVStoNCImMapping {

	private static String[] HEADINGS = new String[] {
		"NCIt Code",
		"NCIt Preferred Term",
		"NCIm CUI",
		"NCIm Preferred Name",
		"NCIm Source",
		"Term Type",
		"Source Code",
        "Source Term"
	};

	private String HEADING_LINE ="\"NCIt Code\",\"NCIt Preferred Term\",\"NCIm CUI\",\"NCIm Preferred Name\",\"NCIm Source\",\"Term Type\",\"Source Code\",\"Source Term\"";

    String value_set_ascii_file = null;
    String entire_mapping_file = null;
    String mapping_file = null;
    String outputfile = null;
    private Vector value_set_key_vec = null;
    private HashMap mapping_file_map = null;
    private HashMap ncit2ncim_map = null;

    public RVStoNCImMapping(String value_set_ascii_file, String entire_mapping_file, String mapping_file, String outputfile) {
        this.value_set_ascii_file = value_set_ascii_file;
        this.entire_mapping_file = entire_mapping_file;
        this.mapping_file = mapping_file;
        this.outputfile = outputfile;
        initialize();
    }

    private void initialize() {
		System.out.println("load_value_set_ascii_file: " + value_set_ascii_file);
		value_set_key_vec = load_value_set_ascii_file(value_set_ascii_file);
		dumpVector("value_set_key_vec", value_set_key_vec);
		System.out.println("loadEntireMappingFile: " + entire_mapping_file);
		ncit2ncim_map = loadEntireMappingFile(entire_mapping_file);
		System.out.println("loadMappingFile: " + mapping_file);
		mapping_file_map = loadMappingFile(mapping_file);
	}

    //resolved_NCIt_Neoplasm_Core_Value_Set

	public Vector load_value_set_ascii_file(String value_set_ascii_file) {
		Vector w = new Vector();
        try {
			CSVReader reader = new CSVReader(new FileReader(value_set_ascii_file));//CSV file
			String[] values;
			values = reader.readNext();
			while ((values = reader.readNext()) != null) {
				String ncit_code = values[0];
				String ncit_name = values[1];
				String key = ncit_name + "$" + ncit_code;
				if (!w.contains(key)) {
					w.add(key);
				}
			}
			w = SortUtils.quickSort(w);
			return w;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


/*
"NCIt Code","NCIt Preferred Term","NCIm CUI","NCIm Preferred Name","NCIm Source","Term Type","Source Code","Source Term"
"C7419","Acanthoma","C0846967","Acanthoma","NDFRT","FN","N0000011094","Acanthoma [Disease/Finding]"
*/


	public HashMap loadEntireMappingFile(String entire_mapping_file) {
		HashMap hmap = new HashMap();
        try {
			CSVReader reader = new CSVReader(new FileReader(entire_mapping_file));//CSV file
			String[] fields;
			while ((fields = reader.readNext()) != null) {
		        String ncit_code = fields[0];
		        String ncit_name = fields[1];
		        String key = ncit_name + "$" + ncit_code;

				String ncim_cui = fields[2];
				String ncim_name = fields[3];

				String ncim_src = fields[4];
				String term_type = fields[5];

				if (ncim_src.compareTo("NCI") == 0 && term_type.compareTo("PT") == 0) {
					hmap.put(key, ncim_name+"$"+ncim_cui);
				}
			}
			return hmap;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }

    public void dumpHashMap(String label, HashMap hmap) {
		System.out.println(label);
		Iterator it = hmap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			System.out.println(key + " --> " + value);
		}
	}

    public void dumpVector(String label, Vector v) {
		System.out.println(label);
		for (int i=0; i<v.size(); i++) {
			String key = (String) v.elementAt(i);
			System.out.println(key);
		}
	}

/*


"NCIt Code","NCIt Preferred Term","NCIm CUI","NCIm Preferred Name","NCIm Source","Term Type","Source Code","Source Term"
"C7419","Acanthoma","C0846967","Acanthoma","NDFRT","FN","N0000011094","Acanthoma [Disease/Finding]"

*/

    public HashSet findNCItCodesWithCUIContainingNonNCIAtoms(String mapping_file) {
		HashSet hset = new HashSet();
        try {
			int knt = 0;
			CSVReader reader = new CSVReader(new FileReader(mapping_file));//CSV file
			String[] fields;
			while ((fields = reader.readNext()) != null) {
		        String ncit_code = fields[0];
		        String ncit_name = fields[1];
		        String key = ncit_name + "$" + ncit_code;
				String ncim_cui = fields[2];
				String ncim_name = fields[3];
				String ncim_src = fields[4];
				String term_type = fields[5];
				String term_code = fields[6];
                String term_name = fields[7];
                StringBuffer buf = new StringBuffer();
                buf.append("\"").append(ncit_code).append("\"").append(",");
                buf.append("\"").append(ncit_name).append("\"").append(",");
                buf.append("\"").append(ncim_cui).append("\"").append(",");
                buf.append("\"").append(ncim_name).append("\"").append(",");

                ncim_src = ncim_src.trim();
                if (ncim_src.length() > 0 && ncim_src.compareTo("NCI") != 0) {
					if (!hset.contains(ncit_code)) {
						hset.add(ncit_code);
					}
				}
			}
			return hset;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public HashMap loadMappingFile(String mapping_file) {
		HashSet hset = findNCItCodesWithCUIContainingNonNCIAtoms(mapping_file);
		HashMap hmap = new HashMap();
        try {
			int knt = 0;
			CSVReader reader = new CSVReader(new FileReader(mapping_file));//CSV file
			String[] fields;
			while ((fields = reader.readNext()) != null) {
		        String ncit_code = fields[0];
		        String ncit_name = fields[1];
		        String key = ncit_name + "$" + ncit_code;
				String ncim_cui = fields[2];
				String ncim_name = fields[3];
				String ncim_src = fields[4];
				String term_type = fields[5];
				String term_code = fields[6];
                String term_name = fields[7];
                StringBuffer buf = new StringBuffer();
                buf.append("\"").append(ncit_code).append("\"").append(",");
                buf.append("\"").append(ncit_name).append("\"").append(",");
                buf.append("\"").append(ncim_cui).append("\"").append(",");
                buf.append("\"").append(ncim_name).append("\"").append(",");
                if (ncim_src.compareTo("NCI") != 0) {
	                 buf.append("\"").append(ncim_src).append("\"").append(",");
					 buf.append("\"").append(term_type).append("\"").append(",");
					 buf.append("\"").append(term_code).append("\"").append(",");
					 buf.append("\"").append(term_name).append("\"");
			    } else {
	                 buf.append("\"").append(" ").append("\"").append(",");
					 buf.append("\"").append(" ").append("\"").append(",");
					 buf.append("\"").append(" ").append("\"").append(",");
					 buf.append("\"").append(" ").append("\"");
				}
				String line = buf.toString();
				Vector w = new Vector();
				if (hmap.containsKey(key)) {
					w = (Vector) hmap.get(key);
				}
				if (!w.contains(line)) {
					if (ncim_src.compareTo("NCI") != 0) {
						w.add(line);
					//findNCItCodesWithCUIContainingNonNCIAtoms
					} else if (!hset.contains(ncit_code)) {
						w.add(line);
					}
					hmap.put(key, w);
				}
			}
			return hmap;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }


    public void run() {
        PrintWriter pw = null;
        int no_match = 0;
        Vector no_ncim_match_vec = new Vector();
        Vector no_non_ncit_atom_vec = new Vector();
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			pw.println(HEADING_LINE);
			for (int i=0; i<value_set_key_vec.size(); i++) {
				String key = (String) value_set_key_vec.elementAt(i);

				if (mapping_file_map.containsKey(key)) {
					Vector lines = (Vector) mapping_file_map.get(key);
					for (int j=0; j<lines.size(); j++) {
						String t = (String) lines.elementAt(j);
						pw.println(t);
					}
				} else {
					Vector u = StringUtils.parseData(key, "$");
					String ncit_name = (String) u.elementAt(0);
					String ncit_code = (String) u.elementAt(1);
					String ncim_nm_cui = (String) ncit2ncim_map.get(key);
					if (ncim_nm_cui != null) {
						u = StringUtils.parseData(ncim_nm_cui, "$");
						String ncim_name = (String) u.elementAt(0);
						String ncim_cui = (String) u.elementAt(1);
						StringBuffer buf = new StringBuffer();
						buf.append("\"").append(ncit_code).append("\"").append(",");
						buf.append("\"").append(ncit_name).append("\"").append(",");
						buf.append("\"").append(ncim_cui).append("\"").append(",");
						buf.append("\"").append(ncim_name).append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"");
						String line = buf.toString();
						no_non_ncit_atom_vec.add(line);
						pw.println(line);
		        	} else {
						StringBuffer buf = new StringBuffer();
						buf.append("\"").append(ncit_code).append("\"").append(",");
						buf.append("\"").append(ncit_name).append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"").append(",");
						buf.append("\"").append(" ").append("\"");
						String line = buf.toString();
						pw.println(line);
						no_ncim_match_vec.add(ncit_code + "|" + ncit_name);
					}
					no_match++;
				}
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
		//no_non_ncit_atom_vec

		System.out.println("\nno_non_ncit_atom_vec: " + no_non_ncit_atom_vec.size());
		for (int i=0; i<no_non_ncit_atom_vec.size(); i++) {
			String t = (String) no_non_ncit_atom_vec.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
		}

		System.out.println("\n\nno_ncim_match_vec: " + no_ncim_match_vec.size());
		for (int i=0; i<no_ncim_match_vec.size(); i++) {
			String t = (String) no_ncim_match_vec.elementAt(i);
			if (t.compareTo("Code|Preferred Term") != 0) {
				int j = i+1;
				System.out.println("(" + j + ") " + t);
			}
		}
	}


    public static void main(String[] args) {
		String value_set_ascii_file = args[0];
		String entire_mapping_file = args[1];
		String mapping_file = args[2];
		String outputfile = args[3];
        new RVStoNCImMapping(value_set_ascii_file, entire_mapping_file, mapping_file, outputfile).run();
    }
}




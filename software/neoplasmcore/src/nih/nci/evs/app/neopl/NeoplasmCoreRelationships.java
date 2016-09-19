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


public class NeoplasmCoreRelationships {
    //client client = null;
    //SPARQLClient client = null;
    OWLRestrictionParser parser = null;
    String value_set_ascii_file = null;
    //String sparql_service = null;
    Vector value_set_name_code_vec = null;
    HashMap roleName2CodeHashMap = null;
    //HierarchyHelper hierarchyHelper = null;
    Vector applicable_roles = null;
    //static int DIRECTION_INBOUND = 0;
    //static int DIRECTION_OUTBOUND = 1;
    static String[] DIRECTION = new String[] {"inbound", "outbound"};
    HashMap inverseRoleNameMap = null;
    Vector selected_roles = null;
    String owlfile = null;
    Vector code_vec = null;

    //static String INVERSE_ROLE_FILE = "inverse_roles.txt";
    //static String SELECTED_ROLE_FILE = "roles.txt";
    //static String OWL_VERSION_INFO = "owl:versionInfo";

    String ncit_version = null;

    private String[] HEADINGS = new String[] {
			"Code",
			"Preferred Term",
			"Relationship",
			"Code",
			"Preferred Term"
	};

	//private String HEADING_STR = "\"Code\",\"Preferred Term\",\"Relationship\",\"Code\",\"Preferred Term\"";
	//static String Constants.RELATIONSHIP_FILE_PREFIX = "Neoplasm_Core_Rels_NCIt_Molecular_";

    public OWLRestrictionParser getOWLRestrictionParser() {
		return parser;
	}

    public NeoplasmCoreRelationships(String value_set_ascii_file, String owlfile) {
		this.value_set_ascii_file = value_set_ascii_file;
		this.owlfile = owlfile;
		initialize();
	}

	public String getLabel(String line) {
		int n = line.lastIndexOf("<");
		String t = line.substring(0, n);
		n = line.indexOf(">");
		t = line.substring(n+1, t.length());
		return t;
	}

    public String openTag(String tag) {
		return "<" + tag;
	}

    public String closeTag(String tag) {
		return "</" + tag + ">";
	}

	public String getVersion() {
		String version = null;
		BufferedReader br = null;
		try {
            FileReader a = new FileReader(owlfile);
            br = new BufferedReader(a);
            String line;
            line = br.readLine();
            while(line != null){
				line = line.trim();
				if (line.startsWith(openTag(Constants.OWL_VERSION_INFO)) && line.endsWith(closeTag(Constants.OWL_VERSION_INFO))) {
					version = getLabel(line);
					break;
				}
				line = br.readLine();
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return version;
	}

	private void initialize() {
		//String sparql_service = "http://localhost:8080/jena-fuseki/ncit1605e/query";
		//String prefixes = "PREFIX :&lt;http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#&gt;$PREFIX base:&lt;http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl&gt;$PREFIX rdf:&lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt;$PREFIX owl:&lt;http://www.w3.org/2002/07/owl#&gt;$PREFIX owl2xml:&lt;http://www.w3.org/2006/12/owl2-xml#&gt;$PREFIX protege:&lt;http://protege.stanford.edu/plugins/owl/protege#&gt;$PREFIX xsd:&lt;http://www.w3.org/2001/XMLSchema#&gt;$PREFIX rdfs:&lt;http://www.w3.org/2000/01/rdf-schema#&gt;$PREFIX ncicp:&lt;http://ncicb.nci.nih.gov/xml/owl/EVS/ComplexProperties.xsd#&gt;$PREFIX dc:&lt;http://purl.org/dc/elements/1.1/&gt;$PREFIX text:&lt;http://jena.apache.org/text#&gt;";
        //String prefixString = StringUtils.createPrefixString(prefixes);
		//client = new SPARQLClient(sparql_service, prefixString);
		//code_vec = get_value_set_codes(value_set_ascii_file);
		ncit_version = getVersion();
		parser = new OWLRestrictionParser(owlfile);

		String filename = "all_roles_" + ncit_version + ".txt";
		File f = new File(filename);
		if(f.exists() && !f.isDirectory()) {
			parser.load_restriction_vec(filename);
		} else {
			parser.generate_restriction_vec();
			parser.save_restriction_vec(filename);
		}

		System.out.println("Loading " + value_set_ascii_file);
		value_set_name_code_vec = load_value_set_ascii_file(value_set_ascii_file);
		System.out.println("value_set_ascii_file.size(): " + value_set_name_code_vec.size());
		System.out.println("Loading " + Constants.INVERSE_ROLE_FILE);
		inverseRoleNameMap = loadInverseRoleNameMap(Constants.INVERSE_ROLE_FILE);
		System.out.println("Loading " + Constants.SELECTED_ROLE_FILE);
		selected_roles = loadSelectedRoles(Constants.SELECTED_ROLE_FILE);
	}

	private Vector loadSelectedRoles(String filename) {
		return FileUtils.readFile(filename);
	}

	private HashMap loadInverseRoleNameMap(String filename) {
		HashMap hmap = new HashMap();
		Vector v = FileUtils.readFile(filename);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			Vector u = StringUtils.parseData(t);
			String role_name = (String) u.elementAt(0);
			String inv_role_name = (String) u.elementAt(1);
			hmap.put(role_name, inv_role_name);
		}
		return hmap;
	}

	public Vector get_value_set_codes(String value_set_ascii_file) {
		Vector w = new Vector();
        try {
			CSVReader reader = new CSVReader(new FileReader(value_set_ascii_file));//CSV file
			String[] values;
			values = reader.readNext();
			while ((values = reader.readNext()) != null) {
				String code = values[0];
				w.add(code);
			}
			w = SortUtils.quickSort(w);
			return w;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Vector load_value_set_ascii_file(String value_set_ascii_file) {
		Vector w = new Vector();
        try {
			CSVReader reader = new CSVReader(new FileReader(value_set_ascii_file));//CSV file
			String[] values;
			values = reader.readNext();
			while ((values = reader.readNext()) != null) {
				String code = values[0];
				String name = values[1];
				String key = name + "$" + code;
				if (!w.contains(key)) {
					w.add(key);
				}
			}
			w = SortUtils.quickSort(w);
			System.out.println("key size: " + w.size());
			return w;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String getParentChildLine(String name, String code, String rel, int directionId) {
		String role_name = null;
		String related_concept_name = null;
		String related_concept_code = null;

		StringBuffer buf = new StringBuffer();
		//Disease_Excludes_Finding|Ineffective Hematopoiesis|C36252
		Vector u = StringUtils.parseData(rel);
		if (directionId == Constants.DIRECTION_INBOUND) {
			related_concept_name = (String) u.elementAt(0);
			related_concept_code = (String) u.elementAt(1);
			role_name = (String) u.elementAt(2);
	    } else {
			role_name = (String) u.elementAt(0);
			related_concept_name = (String) u.elementAt(1);
			related_concept_code = (String) u.elementAt(2);
		}
		//String role_code = (String) roleName2CodeHashMap.get(role_name);
		String direction = Constants.DIRECTION[directionId];
        if (directionId == Constants.DIRECTION_INBOUND) {
			buf.append(direction).append("|");
			buf.append(related_concept_name).append("|");
			buf.append(related_concept_code).append("|");
			buf.append(role_name).append("|");
			buf.append(name).append("|");
			buf.append(code);
		} else {
			buf.append(direction).append("|");
			buf.append(name).append("|");
			buf.append(code).append("|");
			buf.append(role_name).append("|");
			buf.append(related_concept_name).append("|");
			buf.append(related_concept_code);
		}
		return buf.toString();
	}

    public void generateHTMLTree(String treeFile) {
        String title = "Neoplasm Core Relationships";
        String label = "NCI Neoplasm Core Relationships";
        String ncit_version = "16.08e";
        int n = treeFile.lastIndexOf(".");
        String outputfile = treeFile.substring(0, n) + "_" + getToday("MMddYYYY") + ".html";
        //new ASCII2HTMLTreeConverter().run(treeFile, outputfile, 0, title, label, ncit_version, null);
        //MappingHyperlinkUtils mappingHyperlinkUtils = new MappingHyperlinkUtils();
        //mappingHyperlinkUtils.setStringReplacement("src=\"images/", "src=\"");
        //mappingHyperlinkUtils.run(outputfile, "final_" + outputfile);
	}

	public static String getToday() {
		return getToday("MM-dd-yyyy");
	}

	public static String getToday(String format) {
		java.util.Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public void generateCSVFile(String inputfile, String selected_role_file, String outputfile) {
        Vector role_not_selected = new Vector();
		String today = getToday("MMddYYYY");
		int n = outputfile.lastIndexOf(".");
		String ext = outputfile.substring(n, outputfile.length());
		outputfile = outputfile.substring(0, n) + "_" + today + ext;

		Vector v = FileUtils.readFile(inputfile);

int line_count = v.size();
line_count = line_count-1;
System.out.println("Totol number of records: " + line_count);

		Vector selected_roles = FileUtils.readFile(selected_role_file);

System.out.println("Totol number of selected_roles: " + selected_roles.size());


		//outbound|Accelerated Phase Chronic Myelogenous Leukemia, BCR-ABL1 Positive|C3173|Disease_Excludes_Finding|Ineffective Hematopoiesis|C36252
        PrintWriter pw = null;
        Vector w = new Vector();
        HashMap hmap = new HashMap();
        String key = null;
        Vector key_vec = new Vector();
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			pw.println(Constants.HEADING_STR);
			for (int i=0; i<v.size(); i++) {
				String t = (String) v.elementAt(i);
				Vector u = StringUtils.parseData(t);
				String direction = (String) u.elementAt(0);
				String name1 = (String) u.elementAt(1);
				String code1 = (String) u.elementAt(2);
				String role = (String) u.elementAt(3);
				String name2 = (String) u.elementAt(4);
				String code2 = (String) u.elementAt(5);
				if (selected_roles.contains(role)) {
					StringBuffer buf = new StringBuffer();
					if (direction.compareTo("inbound") == 0) {
						role = (String) inverseRoleNameMap.get(role);
						buf.append("\"").append(code2).append("\"").append(",");
						buf.append("\"").append(name2).append("\"").append(",");
						buf.append("\"").append(role).append("\"").append(",");
						buf.append("\"").append(code1).append("\"").append(",");
						buf.append("\"").append(name1).append("\"");

						key = name2+"$"+code2+"$"+role+"$"+code1+"$"+name1;

					} else {
						buf.append("\"").append(code1).append("\"").append(",");
						buf.append("\"").append(name1).append("\"").append(",");
						buf.append("\"").append(role).append("\"").append(",");
						buf.append("\"").append(code2).append("\"").append(",");
						buf.append("\"").append(name2).append("\"");

						key = name1+"$"+code1+"$"+role+"$"+code2+"$"+name2;
					}
					String s = buf.toString();
					hmap.put(key, s);
					key_vec.add(key);
				} else {
					if (!role_not_selected.contains(role)) {
						role_not_selected.add(role);
					}
				}
			}
			key_vec = SortUtils.quickSort(key_vec);
			for (int k=0; k<key_vec.size(); k++) {
				key = (String) key_vec.elementAt(k);
				pw.println((String) hmap.get(key));
				System.out.println("key: " + key);
			}

            System.out.println("Roles not in the selected role list.");
			for (int k=0; k<role_not_selected.size(); k++) {
				String role = (String) role_not_selected.elementAt(k);
				int k1 = k + 1;
				System.out.println("(" + k1 + ")" + role);
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

	public void getRestrictions(String outputfile) {
		Vector w = new Vector();
		Vector code_vec = get_value_set_codes(value_set_ascii_file);
		System.out.println("get_value_set_codes returns: " + code_vec.size());
        HashMap map = parser.getObjectPropertyName2CodeMap();
        for (int i=0; i<selected_roles.size(); i++) {
			String role_name = (String) selected_roles.elementAt(i);
			String role_code = (String) map.get(role_name);
			w.add(role_code);
		}
		parser.getRestrictions(code_vec, w, outputfile);
	}


	public String getOutputfileName(String inputfile) {
		String today = getToday("MMddYYYY");
		int n = inputfile.lastIndexOf(".");
		String ext = inputfile.substring(n, inputfile.length());
		String outputfile = inputfile.substring(0, n) + "_" + today + ext;
		return outputfile;
	}

	public boolean compare(String t1, String t2) {
		Vector v1 = StringUtils.parseData(t1, "$");
		Vector v2 = StringUtils.parseData(t2, "$");

		for (int i=0; i<v1.size(); i++) {
			String s1 = (String) v1.elementAt(i);
			String s2 = (String) v2.elementAt(i);
			if (s1.compareToIgnoreCase(s2) < 0) {
				return true;
			} else if (s1.compareToIgnoreCase(s2) > 0) {
				return false;
			}
		}
		return true;
	}


	public Vector sortDelimitedStrings(Vector v) {



		for (int i=1; i<v.size(); i++) {
			for (int j=0; j<i; j++) {
				String t1 = (String) v.elementAt(i);
				String t2 = (String) v.elementAt(j);
				if (compare(t1, t2)) {
					//tring temp = t1;
					v.setElementAt(t1, j);
					v.setElementAt(t2, i);
					//t1 = t2;
					//t2 = temp;
				}
		    }
		}
		return v;
	}

	public void generateCSVFile(String inputfile, String csvfile) {
		//String outputfile = getOutputfileName(inputfile);
		Vector v = FileUtils.readFile(inputfile);
		int line_count = v.size();
		line_count = line_count-1;
		System.out.println("Totol number of records: " + line_count);

		HashMap code2NameMap = parser.getCode2NameMap();
		HashMap objectPropertyCode2NameMap = parser.getObjectPropertyCode2NameMap();
		HashMap objectPropertyName2CodeMap = parser.getObjectPropertyName2CodeMap();

        //inbound|C101046|R175|C37193
        PrintWriter pw = null;
        Vector w = new Vector();
        HashMap hmap = new HashMap();
        String key = null;
        Vector key_vec = new Vector();
		String name1 = null;
		String code1 = null;

		String name2 = null;
		String code2 = null;
        try {
			pw = new PrintWriter(csvfile, "UTF-8");
			//	private String Constants.HEADING_STR = "\"Code\",\"Preferred Term\",\"Relationship\",\"Code\",\"Preferred Term\"";
			pw.println(Constants.HEADING_STR);
			for (int i=0; i<v.size(); i++) {
				String t = (String) v.elementAt(i);
				Vector u = StringUtils.parseData(t);
			    String direction = (String) u.elementAt(0);
			    String src_code = (String) u.elementAt(1);
			    String role_code = (String) u.elementAt(2);
			    String role_name = (String) objectPropertyCode2NameMap.get(role_code);
			    String target_code = (String) u.elementAt(3);
			    if (direction.compareTo("outbound") == 0) {
					code1 = src_code;
					name1 = (String) code2NameMap.get(code1);
					code2 = target_code;
					name2 = (String) code2NameMap.get(code2);
				} else {
					code1 = target_code;
					name1 = (String) code2NameMap.get(code1);
					code2 = src_code;
					name2 = (String) code2NameMap.get(code2);
					role_name = (String) inverseRoleNameMap.get(role_name);
				}
				StringBuffer buf = new StringBuffer();
				buf.append("\"").append(code1).append("\"").append(",");
				buf.append("\"").append(name1).append("\"").append(",");
				buf.append("\"").append(role_name).append("\"").append(",");
				buf.append("\"").append(code2).append("\"").append(",");
				buf.append("\"").append(name2).append("\"");
				//key = name1+"$"+code1+"$"+role_name+"$"+code2+"$"+name2;
				key = name1+"$"+code1+"$"+role_name+"$"+name2+"$"+code2;

				String s = buf.toString();
				hmap.put(key, s);
				key_vec.add(key);
			}
			//key_vec = SortUtils.quickSort(key_vec);
//dumpVector("BEFORE SORT ", key_vec);
			key_vec = sortDelimitedStrings(key_vec);
//dumpVector("AFTER SORT ", key_vec);
			for (int k=0; k<key_vec.size(); k++) {
				key = (String) key_vec.elementAt(k);
				pw.println((String) hmap.get(key));
			}

		} catch (Exception ex) {

		} finally {
			try {
				pw.close();
				System.out.println("Output file " + csvfile + " generated.");
		    } catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	public void dumpVector(String label, Vector v) {
		System.out.println(label);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
		}
	}

    public void convertToExcel(String csvfile) {
		System.out.println(csvfile);

		CSVtoExcel csvtoExcel = new CSVtoExcel();
	    String[] headings = csvtoExcel.extractHeadings(csvfile);
	    csvtoExcel.setHeadings(headings);

	    int[] codeColumn = new int[5];
	    codeColumn[0] = 1;
	    codeColumn[1] = 0;
	    codeColumn[2] = 0;
	    codeColumn[3] = 1;
	    codeColumn[4] = 0;
	    csvtoExcel.setCodeColumn(codeColumn);

	    System.out.println("csvtoExcel.runHSSF: " + csvfile);
	    csvtoExcel.runHSSF(csvfile);
	    /*
	    boolean skip_heading = true;
	    List list = NeoplasmCoreApplication.readCSV(csvfile, skip_heading);
		csvtoExcel.runHSSF(list, csvfile);
		*/

	}


    public static void main(String [ ] args) {
		long ms = System.currentTimeMillis();

		String value_set_ascii_file = args[0];//"value set file";
		String owlfile = args[1]; //ncit file
		NeoplasmCoreRelationships neoplasmCoreRelationships = new NeoplasmCoreRelationships(value_set_ascii_file, owlfile);

		String date = StringUtils.getToday("YYYY-MM-dd");
		String csvfile = Constants.RELATIONSHIP_FILE_PREFIX + date + ".csv";

		int n = value_set_ascii_file.lastIndexOf(".");
		String outputfile = value_set_ascii_file.substring(0, n) + "_rel.csv";

		System.out.println("getRestrictions...");
		neoplasmCoreRelationships.getRestrictions(outputfile);
		System.out.println(outputfile + " generated.");

		System.out.println("generateCSVFile...");
		neoplasmCoreRelationships.generateCSVFile(outputfile, csvfile);

		System.out.println("convertToExcel...");
		//String csvfile = neoplasmCoreRelationships.getOutputfileName(outputfile);
		neoplasmCoreRelationships.convertToExcel(csvfile);
	}
}


package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import gov.nih.nci.evs.browser.bean.*;
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


public class ValueSetTreeGenerator {
	Stack stack = null;
    //static String Constants.INVERSE_IS_A = "Constants.INVERSE_IS_A";
    //static String NCI_THESAURUS = "NCI_Thesaurus";

    String parent_child_file = null;
    String value_set_ascii_file = null;
    HierarchyHelper hierarchyHelper = null;
    Vector valueSet = null;
    Vector valueSetCodes = null;
    Vector parent_child_vec = null;
    HashMap categoryMap = null;
    HashMap categoryName2LabelMap = null;
    HashMap categoryLabel2CodeMap = null;
    Vector level2_roots = null;
    HashMap level2_root2Root = null;
    PathToRootsFinder pathToRootsFinder = null;
    HashMap link2Path = null;
    /*
	static int SITE_MORPH_FORMAT = 0;
	static int NEOPLASTIC_STATUS_FORMAT = 1;

	static String Constants.HIERARCHY_FILE_PREFIX = "Neoplasm_Core_Constants.HIERARCHY_";
	static String Constants.HIERARCHY_FILE_PREFIX_PLUS = "Neoplasm_Core_Constants.HIERARCHY_Plus_";

	static String Constants.HIERARCHY_FILE_PREFIX_2 = "Neoplasm_Core_Constants.HIERARCHY_By_Neoplastic_Status_";
	static String Constants.HIERARCHY_FILE_PREFIX_2_PLUS = "Neoplasm_Core_Constants.HIERARCHY_By_Neoplastic_Status_Plus_";
    */

    public ValueSetTreeGenerator(String parent_child_file, String value_set_ascii_file) {
		this.parent_child_file = parent_child_file;
		this.value_set_ascii_file = value_set_ascii_file;

		System.out.println("parent_child_file: " + parent_child_file);
		System.out.println("value_set_ascii_file: " + value_set_ascii_file);

        initialize();
    }


    public HashMap getCategoryName2LabelMap() {
		HashMap hmap = new HashMap();
		hmap.put("Malignant", "Malignant Neoplasm");
		hmap.put("Benign", "Benign Neoplasm");
		hmap.put("Undetermined", "Neoplasm of Intermediate or Uncertain Behavior, or Comprising Mixed Subtypes");
		return hmap;
	}

	private void set_level2_roots(Vector v) {
		this.level2_roots = v;
	}

	private void set_level2_root2Root(HashMap hmap) {
		this.level2_root2Root = hmap;
	}

	public HashMap getCategoryLabel2CodeMap() {
		HashMap hmap = new HashMap();
		hmap.put("Malignant Neoplasm", "R_1");
		hmap.put("Benign Neoplasm", "R_2");
		hmap.put("Neoplasm of Intermediate or Uncertain Behavior, or Comprising Mixed Subtypes", "R_3");
		return hmap;
	}


	public void initialize() {
		categoryMap = new HashMap();
		stack = new Stack();
		parent_child_vec = new Vector();
		Vector v = FileUtils.readFile(parent_child_file);
		hierarchyHelper = new HierarchyHelper(v);
		valueSet = loadValueSetData(value_set_ascii_file);
		categoryMap = loadCategoryMap(value_set_ascii_file);
		categoryName2LabelMap = getCategoryName2LabelMap();
		categoryLabel2CodeMap = getCategoryLabel2CodeMap();
		link2Path = new HashMap();

		initialize_stack();
	}


	public void initialize_stack() {
		initialize_stack(null);
	}


	public void initialize_stack(Vector v) {
		stack = new Stack();
		if (v == null) {
			////Neoplasm (Code C3262)
			String code = "C3262";
			String name = "Neoplasm";
			valueSetCodes.add(code);
			ConceptLink c = new ConceptLink(
				code,
				name,
				null,
				null,
				Constants.INVERSE_IS_A);
			stack.push(c);
		} else {
			level2_roots = new Vector();
			level2_root2Root = new HashMap();
			for (int i=0; i<v.size(); i++) {
				String t = (String) v.elementAt(i);
				Vector u = StringUtils.parseData(t);

				String s1 = (String) u.elementAt(1); //sourceConceptCode
				String s2 = (String) u.elementAt(0); //sourceConceptName
				String s3 = (String) u.elementAt(3); //targetConceptCode
				String s4 = (String) u.elementAt(2); //targetConceptName

				if (!valueSetCodes.contains(s1)) {
					valueSetCodes.add(s1);
				}

				if (!valueSetCodes.contains(s3)) {
					valueSetCodes.add(s3);
					level2_roots.add(s3);
					level2_root2Root.put(s3, s1);
				}

				ConceptLink c = new ConceptLink(
					s1,
					s2,
					s3,
					s4,
					Constants.INVERSE_IS_A);
				c.setPath(s1 + "|" + s3);
				stack.push(c);
			}
		}
	}

	private boolean checkAdjacency(String key, String path) {
		Vector u = StringUtils.parseData(key);
		String sourceCode = (String) u.elementAt(1);
		String targetCode = (String) u.elementAt(3);
		//if (!path.endsWith(targetCode)) return false;
        path = path + "|";
		int n = path.indexOf(targetCode + "|");
		int len = sourceCode.length();
		int m = path.indexOf(sourceCode + "|");
		if (m == n - len - 1) return true;
		return false;
	}

	private boolean validatePath(String key, String path) {
		Vector u = StringUtils.parseData(key);
		String sourceCode = (String) u.elementAt(1);
		String targetCode = (String) u.elementAt(3);
        path = path + "|";
		int n = path.indexOf(targetCode + "|");
		int len = sourceCode.length();
		int m = path.indexOf(sourceCode + "|");
		if (m != -1 && n != -1) return true;
		return false;
	}


	public void extend_parent_child_vec(Vector root_data) {
		if (root_data == null) return;

		Vector w = new Vector();
	    for (int i=0; i<parent_child_vec.size(); i++) {
			String t = (String) parent_child_vec.elementAt(i);
			if (!t.startsWith("Neoplasm|")) {
				w.add(t);
			}
		}

	    for (int i=0; i<root_data.size(); i++) {
			String t = (String) root_data.elementAt(i);
			Vector u = StringUtils.parseData(t);
			String code = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			String category = (String) u.elementAt(2);

		    String parent_name = (String) categoryName2LabelMap.get(category);
            String parent_code = (String) categoryLabel2CodeMap.get(parent_name);
            w.add(parent_name + "|" + parent_code + "|" + name + "|" + code);
		}
		parent_child_vec = SortUtils.quickSort(w);
	}


	public void run(PrintWriter pw) {
		parent_child_vec = generate_parent_child_relationships();
		parent_child_vec = SortUtils.quickSort(parent_child_vec);
		for (int i=0; i<parent_child_vec.size(); i++) {
			String line = (String) parent_child_vec.elementAt(i);
			pw.println(line);
		}
	}

	public Vector savePathData(String outputfile) {
		Vector v = new Vector();
		PrintWriter pw = null;
		Vector adjacent_vec = new Vector();
		Vector path_vec = new Vector();
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			Vector key_vec = new Vector();
			Iterator it = link2Path.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				key_vec.add(key);
			}
			key_vec = SortUtils.quickSort(key_vec);
			for (int i=0; i<key_vec.size(); i++) {
				String key = (String) key_vec.elementAt(i);
				Vector w = (Vector) link2Path.get(key);
				for (int j=0; j<w.size(); j++) {
					String path = (String) w.elementAt(j);
					path_vec.add(path);
			    }
			}
			pw.println("Number of paths: " + path_vec.size());
			for (int i=0; i<key_vec.size(); i++) {
				String key = (String) key_vec.elementAt(i);
				Vector w = (Vector) link2Path.get(key);
				for (int j=0; j<w.size(); j++) {
					String path = (String) w.elementAt(j);

					String line = key + " --> " + path;

					if (checkAdjacency(key, path)) {
						pw.println("(*) " + line);
						String longer_path = null;
						for (int k=0; k<path_vec.size(); k++) {
							String next_path = (String) path_vec.elementAt(k);
							if (validatePath(key, next_path)) {
								//pw.println("\t\tvalidatePath next_path: " + next_path);
								if (!checkAdjacency(key, next_path)) {
									pw.println("\t\t(*) NOT ADJ: " + next_path);
									longer_path = next_path;
									pw.println("\t-- longer_path: " + longer_path);
									break;
								}
							}
						}
						if (longer_path != null) {
							adjacent_vec.add(line);
							line = line + " (*)";
							pw.println(line);
							pw.println("\tlonger_path: " + longer_path);
							v.add(key);
						}
					} else {
						pw.println(line);
					}
				}
			}

			pw.println("\n\n");
			for (int i=0; i<adjacent_vec.size(); i++) {
				String t = (String) adjacent_vec.elementAt(i);
				int j = i+1;
				pw.println("(" + j + ") " + t);
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
		return v;
	}

	public Vector generate_parent_child_relationships() {
		boolean include_extern = false;
		int lcv = 0;
		link2Path = new HashMap();
		parent_child_vec = new Vector();
		while (!stack.isEmpty()) {
            ConceptLink c = (ConceptLink) stack.pop();
            String parent_code = c.getSourceConceptCode();
            String parent_name = c.getSourceConceptName();
            String child_code = c.getTargetConceptCode();
            String child_name = c.getTargetConceptName();
            if (child_code == null) {
				Vector codes = hierarchyHelper.getSubclassCodes(parent_code);
				if (codes != null && codes.size() > 0) {
					for (int i=0; i<codes.size(); i++) {
						child_code = (String) codes.elementAt(i);
						child_name = hierarchyHelper.getLabel(child_code);
						ConceptLink c2 = new ConceptLink(
							parent_code,
							parent_name,
							child_code,
							child_name,
							Constants.INVERSE_IS_A);
						if (include_extern && !valueSetCodes.contains(child_code)) {
							c2.setPath(c.getPath() + "|" + child_code);
						} else if (valueSetCodes.contains(child_code)) {
							c2.setPath(c.getPath() + "|" + child_code);
						}
						stack.push(c2);
					}
				}
			} else {
				if (valueSetCodes.contains(parent_code) && valueSetCodes.contains(child_code)) {
					String s = parent_name + "|" + parent_code + "|" + child_name + "|" + child_code;
					String path = c.getPath();

					Vector w = new Vector();
					if (link2Path.containsKey(s)) {
						w = (Vector) link2Path.get(s);
					}
					if (!w.contains(path)) {
						w.add(path);
					}
					link2Path.put(s, w);

					if (!parent_child_vec.contains(s)) {
						parent_child_vec.add(s);
					}

					Vector codes = hierarchyHelper.getSubclassCodes(child_code);
					if (codes != null && codes.size() > 0) {
						for (int i=0; i<codes.size(); i++) {
							String child_code2 = (String) codes.elementAt(i);
							String child_name2 = hierarchyHelper.getLabel(child_code2);
							ConceptLink c2 = new ConceptLink(
								child_code,
								child_name,
								child_code2,
								child_name2,
								Constants.INVERSE_IS_A);

							if (include_extern && !valueSetCodes.contains(child_code)) {
								c2.setPath(c.getPath() + "|" + child_code2);
							} else if (valueSetCodes.contains(child_code2)) {
								c2.setPath(c.getPath() + "|" + child_code2);
							}
							stack.push(c2);
						}
					}

				} else if (valueSetCodes.contains(parent_code)) {
					Vector codes = hierarchyHelper.getSubclassCodes(child_code);
					if (codes != null && codes.size() > 0) {
						for (int i=0; i<codes.size(); i++) {
							String child_code2 = (String) codes.elementAt(i);
							String child_name2 = hierarchyHelper.getLabel(child_code2);

							ConceptLink c2 = new ConceptLink(
								parent_code,
								parent_name,
								child_code2,
								child_name2,
								Constants.INVERSE_IS_A);

							if (include_extern && !valueSetCodes.contains(child_code)) {
								c2.setPath(c.getPath() + "|" + child_code2);
							} else if (valueSetCodes.contains(child_code2)) {
								c2.setPath(c.getPath() + "|" + child_code2);
							}

							stack.push(c2);
						}
					}
				} else if (valueSetCodes.contains(child_code)) {
					Vector codes = hierarchyHelper.getSubclassCodes(child_code);
					if (codes != null && codes.size() > 0) {
						for (int i=0; i<codes.size(); i++) {
							String child_code2 = (String) codes.elementAt(i);
							String child_name2 = hierarchyHelper.getLabel(child_code2);
							ConceptLink c2 = new ConceptLink(
								child_code,
								child_name,
								child_code2,
								child_name2,
								Constants.INVERSE_IS_A);

							if (include_extern && !valueSetCodes.contains(child_code)) {
								c2.setPath(c.getPath() + "|" + child_code2);
							} else if (valueSetCodes.contains(child_code2)) {
								c2.setPath(c.getPath() + "|" + child_code2);
							}

							stack.push(c2);
						}
					}
				} else {
					ConceptLink c2 = new ConceptLink(
						child_code,
						child_name,
						null,
						null,
						Constants.INVERSE_IS_A);
					stack.push(c2);
				}
			}

		}

		if (parent_child_vec == null) {
			System.out.println("generate_parent_child_relationships yields parent_child_vec == null???");
		}
		parent_child_vec = SortUtils.quickSort(parent_child_vec);
		return parent_child_vec;
	}

    public Vector loadValueSetData(String csvFile) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Vector v = new Vector();
		valueSetCodes = new Vector();
		try {
            boolean skip_heading = true;
			List list = readCSV(value_set_ascii_file, skip_heading);
			int k = 0;
			for (int i=0; i<list.size(); i++) {
				String[] values = (String[]) list.get(i);
				String field_1 = (String) values[0];
				String field_2 = (String) values[1];
				v.add(field_1 + "|" + field_2);
				valueSetCodes.add(field_1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}


	public String getCategory(String code) {
		return (String) categoryMap.get(code);
	}

	public HashMap loadCategoryMap(String value_set_ascii_file) {
		HashMap categoryMap = new HashMap();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Vector v = new Vector();
		int category_benign = 0;
		int category_malignant = 0;
		int category_undetermined = 0;
		int num_lines = 0;

		boolean skip_heading = true;
		try {
			List list = readCSV(value_set_ascii_file, skip_heading);
			int k = 0;
			for (int i=0; i<list.size(); i++) {
				String[] values = (String[]) list.get(i);
				String field_1 = (String) values[0];
				String category = (String) values[4];
				if (category.compareTo("Malignant") == 0) {
					category_malignant++;
					categoryMap.put(field_1, category);
				} else if (category.compareTo("Benign") == 0) {
					category_benign++;
					categoryMap.put(field_1, category);
				} else {
					category_undetermined++;
					categoryMap.put(field_1, "Undetermined");
				}
				num_lines++;
			}
			System.out.println("category_malignant: " + category_malignant);
			System.out.println("category_benign: " + category_benign);
			System.out.println("category_undetermined: " + category_undetermined);
			System.out.println("num_lines: " + num_lines);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryMap;
    }

    public Vector get_root_data(Vector parent_child_vec) {
		if (parent_child_vec == null) {
			System.out.println("(*) get_root_data parent_child_vec == null???");
			return null;
		}

		hierarchyHelper = new HierarchyHelper(parent_child_vec);
		Vector root_codes = hierarchyHelper.getSubclassCodes("C3262");

		if (root_codes == null) {
			System.out.println("(*) get_root_data root_codes == null???");
			return null;
		}

		Vector w = new Vector();

		for (int i=0; i<root_codes.size(); i++) {
			String code = (String) root_codes.elementAt(i);
			String name = hierarchyHelper.getLabel(code);
			int j = i+1;
			String category = getCategory(code);
			w.add(code + "|" + name + "|" + category);
		}
		return w;
	}

	public void dumpVector(String label, Vector w) {
		if (w == null) return;
	    System.out.println(label);
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
		}
	}

	public Vector get_parent_child_vec() {
		return parent_child_vec;
	}


	public void dumpVector(PrintWriter pw, Vector w, Vector exclusion) {
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			if (!exclusion.contains(t)) {
				pw.println(t);
			}
		}
	}

/*
(631) C27087|Verruca Vulgaris|Benign
(632) C6380|Vulvar Adenocarcinoma|Malignant
(633) C4866|Vulvar Carcinoma|Malignant
(634) C4052|Vulvar Squamous Cell Carcinoma|Malignant
(635) C2854|Warthin Tumor|Benign
(636) C3267|Wilms Tumor|Malignant
*/

    public void regenerate_parent_child_file(String inputfile) {
		Vector link_to_remove_vec = new Vector();
		Vector v = FileUtils.readFile(inputfile);
		HierarchyHelper hierarchyHelper = new HierarchyHelper(v);

		pathToRootsFinder = new PathToRootsFinder(v);

		if (level2_roots == null) {
			level2_roots = new Vector();
			level2_root2Root = new HashMap();
			hierarchyHelper.findRootAndLeafNodes();
            Vector roots = hierarchyHelper.getRoots();
            for (int i=0; i<roots.size(); i++) {
				String Rcode = (String) roots.elementAt(i);
				Vector subclass_vec = (Vector) hierarchyHelper.getSubclassCodes(Rcode);
				for (int j=0; j<subclass_vec.size(); j++) {
					String code = (String) subclass_vec.elementAt(j);
					level2_roots.add(code);
					level2_root2Root.put(code, Rcode);
				}
			}


			for (int i=0; i<level2_roots.size(); i++) {
				String code = (String) level2_roots.elementAt(i);
				System.out.println("level 2: " + code);
				String Rcode = (String) level2_root2Root.get(code);

				System.out.println("looking for: " + code + "|" + Rcode);
						Vector paths = pathToRootsFinder.findPathToRoots(code);
						int path_cnt = 0;
						for (int j1=0; j1<paths.size(); j1++) {
							String path = (String) paths.elementAt(j1);
							System.out.println("path: " + path);
							//if (path.contains(code + "|" + Rcode)) {
							if (path.endsWith(Rcode)) {
								System.out.println("(*) path: " + path);
								path_cnt++;
							}
						}
						//if (path_cnt > 1) {
						if (path_cnt > 1) {
							String name_Rcode = pathToRootsFinder.getHierarchyHelper().getLabel(Rcode);
							String name = pathToRootsFinder.getHierarchyHelper().getLabel(code);
							String line_to_remove = name_Rcode + "|" + Rcode + "|" + name + "|" + code;
							if (!link_to_remove_vec.contains(line_to_remove)) {
								link_to_remove_vec.add(line_to_remove);
							}
						}
			}

		} else {
			for (int i=0; i<level2_roots.size(); i++) {
				String code = (String) level2_roots.elementAt(i);
				System.out.println("level 2: " + code);
				String Rcode = (String) level2_root2Root.get(code);
				String name = hierarchyHelper.getLabel(code);
				Vector subclass_vec = (Vector) hierarchyHelper.getSubclassCodes(code);
				if (subclass_vec != null) {
					for (int j=0; j<subclass_vec.size(); j++) {
						String sub_code = (String) subclass_vec.elementAt(j);
						String sub_name = hierarchyHelper.getLabel(sub_code);
						Vector paths = pathToRootsFinder.findPathToRoots(sub_code);
						int path_cnt = 0;
						for (int j1=0; j1<paths.size(); j1++) {
							String path = (String) paths.elementAt(j1);
							if (path.contains("|" + code + "|" + Rcode)) {
								System.out.println("\tpath: " + path);
								path_cnt++;
							}
						}
						if (path_cnt > 1) {
							String line_to_remove = name + "|" + code + "|" + sub_name + "|" + sub_code;
							if (!link_to_remove_vec.contains(line_to_remove)) {
								link_to_remove_vec.add(line_to_remove);
							}
						}
					}
				}
			}
	    }

        System.out.println("LINE TO REMOVE: " + link_to_remove_vec.size());
		for (int i=0; i<link_to_remove_vec.size(); i++) {
			String line = (String) link_to_remove_vec.elementAt(i);
			System.out.println("LINE TO REMOVE: " + line);
		}

		PrintWriter pw = null;
		int knt = 0;
		try {
			pw = new PrintWriter(inputfile, "UTF-8");
			for (int k=0; k<v.size(); k++) {
				String t = (String) v.elementAt(k);
				if (!link_to_remove_vec.contains(t)) {
					knt++;
					pw.println(t);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + inputfile + " generated.");
				System.out.println("Number of records (Before): " + v.size());
				System.out.println("Number of records (After): " + knt);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void setPathToRootsFinder(PathToRootsFinder pathToRootsFinder) {
		this.pathToRootsFinder = pathToRootsFinder;
	}


	public static String getToday() {
		return getToday("MM-dd-yyyy");
	}

	public static String getToday(String format) {
		java.util.Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

//C:\jdk1.7.0_05\bin\java -Xmx1300m -classpath %CLASSPATH% gov.nih.nci.evs.browser.utils.ASCII2HTMLTreeConverter "tree_site_v1.txt" "Neoplasm Core Tree site+morph 2016-06-30.html" 0 16.06d


    public void generateHTMLTree(String inputfile, String outputfile, int lines_to_skip, String ncit_version, int format) {
		ASCII2HTMLTreeConverter generator = new ASCII2HTMLTreeConverter();
        System.out.println("inputfile: " + inputfile);
        System.out.println("outputfile: " + outputfile);
        System.out.println("lines_to_skip: " + lines_to_skip);
        System.out.println("ncit_version: " + ncit_version);
        System.out.println("format: " + format);
        generator.asciiTree2HTMLTree(inputfile, lines_to_skip, outputfile);

		String title = "Core Constants.HIERARCHY: Site + Morphology";
		String label = "NCIt Neoplasm Core Constants.HIERARCHY by Site and Morphology";

		Vector contents = new Vector();
		contents.add("Neoplasm by Site");
		contents.add("Neoplasm by Morphology");

		if (format == 1) {
			title = "Core Constants.HIERARCHY: Neoplastic Status";
			label = "NCIt Neoplasm Core Constants.HIERARCHY by Neoplastic Status";

			contents = new Vector();
			contents.add("Malignant Neoplasm");
			contents.add("Benign Neoplasm");
			contents.add("Neoplasm of Intermediate or Uncertain Behavior, or Comprising Mixed Subtypes");
		}
		String contentFile = outputfile;
		generator.addHTMLContent(contentFile, outputfile, title, label, ncit_version, contents);
    }


	public void run_appender(String valuesetfile, String mappingfile, String htmlfile, String treefile) {
		HyperlinkAppender appender = new HyperlinkAppender();
		HashMap map1 = appender.loadCUIMap(mappingfile);
		HashMap map2 = appender.loadNeoplasticStatusMap(valuesetfile);
        /*
		int n = htmlfile.lastIndexOf(".");
		String outputfile = htmlfile.substring(0, n) + "_v2.html";

		appender.runHTML(htmlfile, outputfile);
		n = treefile.lastIndexOf(".");
		outputfile = treefile.substring(0, n) + "_v2.txt";
        */


 /*
         String date = ValueSetTreeGenerator.getToday("yyyy-MM-dd");
         int format = 0;
         String htmlfile = Constants.HIERARCHY_FILE_PREFIX + date + ".html";
         String treefile = Constants.HIERARCHY_FILE_PREFIX + date + ".txt";
         if (retree_using_neoplastic_categories) {
 			htmlfile = Constants.HIERARCHY_FILE_PREFIX + "By_Neoplastic_Status_" + date + ".html";
 			treefile = Constants.HIERARCHY_FILE_PREFIX + "By_Neoplastic_Status_" + date + ".txt";
 			format = 1;
		}
*/
        int n = htmlfile.indexOf("By_Neoplastic_Status_");
        String date = ValueSetTreeGenerator.getToday("yyyy-MM-dd");
        if (n == -1) {
			String outputfile = Constants.HIERARCHY_FILE_PREFIX_PLUS + date + ".html";
 		    appender.runHTML(htmlfile, outputfile);
 		    outputfile = Constants.HIERARCHY_FILE_PREFIX_PLUS + date + ".txt";
		    appender.runASCII(treefile, outputfile);
	    } else {
			String outputfile = Constants.HIERARCHY_FILE_PREFIX_2_PLUS + date + ".html";
 		    appender.runHTML(htmlfile, outputfile);
 		    outputfile = Constants.HIERARCHY_FILE_PREFIX_2_PLUS + date + ".txt";
		    appender.runASCII(treefile, outputfile);
		}
	}

    public List readCSV(String csvfile, boolean skip_heading) {
		ArrayList list = new ArrayList();
		int n = csvfile.lastIndexOf(".");
		String nextLine = null;
		String outputfile = getOutputFile(csvfile, "txt");
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

	public String getOutputFile(String inputfile, String format) {
		int n = inputfile.lastIndexOf(".");
		String outputfile = inputfile.substring(0, n) + "." + format;
		return outputfile;
	}


	public static void main(String[] args) {

System.out.println("Step 1");

		FileFormatter formatter = new FileFormatter();
		String from = "images/";
		String to = "";
		formatter.setStringReplacement(from, to);

		boolean retree_using_neoplastic_categories = false;
		String parent_child_file = args[0];

		Vector parent_child_file_v = FileUtils.readFile(parent_child_file);
		String value_set_ascii_file = args[1];

System.out.println("Step 2");


		String retree = args[2];
		if (retree.compareTo("true") == 0) {
			retree_using_neoplastic_categories = true;
		}

        String date = ValueSetTreeGenerator.getToday("yyyy-MM-dd");
        int format = 0;
        String htmlfile = Constants.HIERARCHY_FILE_PREFIX + date + ".html";
        String treefile = Constants.HIERARCHY_FILE_PREFIX + date + ".txt";
        if (retree_using_neoplastic_categories) {
			htmlfile = Constants.HIERARCHY_FILE_PREFIX + "By_Neoplastic_Status_" + date + ".html";
			treefile = Constants.HIERARCHY_FILE_PREFIX + "By_Neoplastic_Status_" + date + ".txt";
			format = 1;
		}
		String final_outputfile = treefile;

System.out.println("Step 3");


		String ncit_version = args[3];

		//parent_child_file: NCIt all distance-1 relationships
		//value_set_ascii_file: core neoplasm value set
		System.out.println("Initializing value set tree generator ...");
		ValueSetTreeGenerator generator = new ValueSetTreeGenerator(parent_child_file, value_set_ascii_file);

        if (!retree_using_neoplastic_categories) {
			System.out.println("Loading neoplasm_roots ...");
			Vector roots = FileUtils.readFile("neoplasm_roots.out");
			generator.initialize_stack(roots);
	    }

System.out.println("Step 4");

		//Apply flooding algorithm to generate all links of the core neoplasm tree stemming from the root concept, Neoplasm.
		System.out.println("generate_parent_child_relationships ...");
		Vector parent_child_vec = generator.generate_parent_child_relationships();
		//Get all roots
		if (retree_using_neoplastic_categories) {
			System.out.println("get_root_data...");
			Vector root_data = generator.get_root_data(parent_child_vec);
			generator.dumpVector("Roots: ", root_data);
			generator.extend_parent_child_vec(root_data);
	    }

System.out.println("Step 5");

	    Vector exclusion_vec = generator.savePathData("path.txt");

		System.out.println("get_parent_child_vec ...");
		Vector w = generator.get_parent_child_vec();


System.out.println("Step 5 w.size() " + w.size());

        PrintWriter pw = null;
		String outputfile = "parent_child.txt";
		System.out.println("output parent_child_vec ..." + outputfile);
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			generator.dumpVector(pw, w, exclusion_vec);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

System.out.println("Step 6");


        System.out.println("regenerate_parent_child_file ..." + outputfile);
		generator.regenerate_parent_child_file(outputfile);

		w = FileUtils.readFile(outputfile);

System.out.println("Step 6 w.size() " + w.size());


		System.out.println("validate_parent_child_relationships -- " + outputfile);
		Vector siblings_to_remove = new Vector();
		boolean cont = true;
		while (cont) {
			try {
				pw = new PrintWriter(outputfile, "UTF-8");
				siblings_to_remove = new PathToRootsValidator(w).validate_parent_child_relationships();
				System.out.println("\tBefore: " + w.size());
				generator.dumpVector(pw, w, siblings_to_remove);
				System.out.println("\t" + siblings_to_remove.size() + " removed.");
				int size = w.size() - siblings_to_remove.size();
				System.out.println("\tAfter: " + size);
				pw.close();

				if (siblings_to_remove.size() == 0) {
					cont = false;
					break;
				} else {
					w = FileUtils.readFile(outputfile);

					System.out.println("w.size(): " + w.size());
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

System.out.println("Step 7 outputfile " + outputfile);

		w = FileUtils.readFile(outputfile);
		siblings_to_remove = new Vector();

		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			siblings_to_remove = new PathToRootsValidator(w).validate_sibling_relationships();
			System.out.println("\tBefore: " + w.size());
			generator.dumpVector(pw, w, siblings_to_remove);
			System.out.println("\t" + siblings_to_remove.size() + " removed.");
			int size = w.size() - siblings_to_remove.size();
			System.out.println("\tAfter: " + size);
			pw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

System.out.println("Step 8 " + outputfile);

		try {
			pw = new PrintWriter(final_outputfile, "UTF-8");
			w = FileUtils.readFile(outputfile);
			HierarchyHelper hierarchyHelper = new HierarchyHelper(w);
			hierarchyHelper.set_show_code(true);
			hierarchyHelper.printTree(pw);

hierarchyHelper.printTree(new PrintWriter(System.out));

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + final_outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		System.out.println("generateHTMLTree: " + htmlfile);
        generator.generateHTMLTree(final_outputfile, htmlfile, 0, ncit_version, format);

        formatter.run(htmlfile);

        String mappingfile = "mapping.csv"; //from MetathesaurusUtils
        generator.run_appender(value_set_ascii_file, mappingfile, htmlfile, treefile);
	}
}


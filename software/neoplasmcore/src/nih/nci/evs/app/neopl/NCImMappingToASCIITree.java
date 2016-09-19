package gov.nih.nci.evs.app.neopl;


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


public class NCImMappingToASCIITree {
    String inputfile = null;
    String outputfile = null;
    Vector targets = null;
    static String ASSOCIATION_NAME = "has_child";
    private CodingSchemeUtils codingSchemeUtils = null;
    private HashMap localName2CodingSchemeNameHashMap = null;

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

	private static String HEADING_LINE = "\"NCIt Code\",\"NCIt Preferred Term\",\"NCIm CUI\",\"NCIm Preferred Name\",\"NCIm Source\",\"Term Type\",\"Source Code\",\"Source Term\"";

    public NCImMappingToASCIITree() {
        this.inputfile = null;
        this.outputfile = null;
    }

    public NCImMappingToASCIITree(String inputfile, String outputfile) {
        this.inputfile = inputfile;
        this.outputfile = outputfile;
        initialize();
    }

    private void initialize() {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        this.codingSchemeUtils = new CodingSchemeUtils(lbSvc);
		this.localName2CodingSchemeNameHashMap = codingSchemeUtils.getLocalName2CodingSchemeNameHashMap();
	}

    public void set_targets(Vector targets) {
        this.targets = targets;

    }

	public boolean toSave(String line) {
		if (line.length() == 0) return true;
        for (int i=0; i<targets.size(); i++) {
			String t = (String) targets.elementAt(i);
			Vector u = StringUtils.parseData(t, "$");
			String target1 = (String) u.elementAt(0);
			if (u.size() == 1) {
				if (line.startsWith(target1)) return true;
			} else {
				String target2 = (String) u.elementAt(1);
				if (line.startsWith(target1) && line.endsWith(target2)) return true;
			}
		}
        return false;
	}

    private String delimed2CSV(String line) {
		if (line == null) return null;
		Vector v = StringUtils.parseData(line);
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			//t = t.escapseHtml(t);
			buf.append("\"" + t + "\"");
			if (i<v.size()-1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	public String extractLabel(String line) {
        //Carcinoma (C2916)
        int n = line.lastIndexOf("(");
        if (n == -1) return line;
        String s = line.substring(0, n-1);
        return s;
	}

	public String extractCode(String line) {
        //Carcinoma (C2916)
        int n = line.lastIndexOf("(");
        if (n == -1) return null;
        String s = line.substring(n+1, line.length()-1);
        return s;
	}

    //CUI: C0846967 Name: Acanthoma
    public String extractNCImName(String line) {
		int n = line.indexOf("Name:");
		String t = line.substring(n+5, line.length());
		t = t.trim();
		return t;
	}

    public String extractCUI(String line) {
		int n = line.indexOf("CUI:");
		//line = line.substring(0, n);
        n = line.lastIndexOf(":");
        if (n == -1) return null;
        String s = line.substring(n+1, line.length());
        s = s.trim();
        return s;
	}

	public String extractKey(String line) {
        int n = line.lastIndexOf("=");
        if (n == -1) return null;
        String s = line.substring(0, n);//n+1, line.length()-1);
        s = s.trim();
        return s;
	}

	public String extractValue(String line) {
        int n = line.lastIndexOf("=");
        if (n == -1) return null;
        String s = line.substring(n+1, line.length());
        s = s.trim();
        return s;
	}

	public String sortStr(String line) {
        Vector w = StringUtils.parseData(line);
        String t0 = (String) w.elementAt(0);
        String t1 = (String) w.elementAt(1);
        String t2 = (String) w.elementAt(2);
        String t3 = (String) w.elementAt(3);
        String t4 = (String) w.elementAt(4);
        String t5 = (String) w.elementAt(5);
        String t6 = (String) w.elementAt(6);
        return t1 + "|" + t0 + "|" + t6 + "|" + t3 + "|" + t4 + "|" + t5 + "|" + t2;
	}

    public void dump_key2dataMap(PrintWriter pw, HashMap hmap) {
		if (hmap == null) return;
		Iterator it = hmap.keySet().iterator();
		Vector key_vec = new Vector();
		while (it.hasNext()) {
			String key = (String) it.next();
			key_vec.add(key);
		}
		key_vec = SortUtils.quickSort(key_vec);
		for (int i=0; i<key_vec.size(); i++) {
			String key = (String) key_vec.elementAt(i);
			pw.println("\n" + key);
			Vector u = (Vector) hmap.get(key);
			u = SortUtils.quickSort(u);
			for (int j=0; j<u.size(); j++) {
				String value = (String) u.elementAt(j);
			    pw.println("\t" + value);
			}
		}
	}

    public void dump_key2dataMap(HashMap hmap) {
		if (hmap == null) return;
		Iterator it = hmap.keySet().iterator();
		Vector key_vec = new Vector();
		while (it.hasNext()) {
			String key = (String) it.next();
			key_vec.add(key);
		}
		key_vec = SortUtils.quickSort(key_vec);
		for (int i=0; i<key_vec.size(); i++) {
			String key = (String) key_vec.elementAt(i);
			System.out.println("\n" + key);
			Vector u = (Vector) hmap.get(key);
			u = SortUtils.quickSort(u);
			for (int j=0; j<u.size(); j++) {
				String value = (String) u.elementAt(j);
			    System.out.println("\t" + value);
			}
		}
	}


	public TreeItem buildTree(PrintWriter debug_pw, String filename, String value_set_ascii_file) {
		Vector valueSetCodes = new ResolvedValueSetMappingUtils().loadValueSetData(value_set_ascii_file);
		System.out.println("valueSetCodes: " + valueSetCodes.size());
		Vector tree_item_vec = new Vector();

		HashMap code2nameMap = new HashMap();
		Vector vs_key_vec = new Vector();
		for (int i=0; i<valueSetCodes.size(); i++) {
			String t = (String) valueSetCodes.elementAt(i);
			Vector u = StringUtils.parseData(t);
			String code = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			code2nameMap.put(code, name);
			vs_key_vec.add(code);
		}
		vs_key_vec = SortUtils.quickSort(vs_key_vec);
		System.out.println("vs_key_vec: " + vs_key_vec.size());

		HashMap hmap = create_key2dataMap(filename);
		if (hmap == null) return null;

		TreeItem root = new TreeItem("R_1", "NCIt Neoplasm Core");
		root._expandable = false;

		Iterator it = hmap.keySet().iterator();
		Vector key_vec = new Vector();
		while (it.hasNext()) {
			String key = (String) it.next();
			key_vec.add(key);
		}
		key_vec = SortUtils.quickSort(key_vec);
		System.out.println("key_vec: " + key_vec.size());

		//int lcv = 0;
		for (int i=0; i<vs_key_vec.size(); i++) {
			String vs_code = (String) vs_key_vec.elementAt(i);
			String vs_name = (String) code2nameMap.get(vs_code);
			String key = vs_name + " (" + vs_code + ")";

			if (key_vec.contains(key)) {
				String label = extractLabel(key);
				String code = extractCode(key);
				TreeItem item = new TreeItem(code, key);
				item._expandable = false;
				Vector v = (Vector) hmap.get(key);
				Vector sub_tree_item_vec = new Vector();
				for (int j=0; j<v.size(); j++) {
					String s = (String) v.elementAt(j);
					Vector u = StringUtils.parseData(s);
					String sub_source = (String) u.elementAt(0);
					String sub_name = (String) u.elementAt(1);

					String sub_code = (String) u.elementAt(2);
					String sub_type = (String) u.elementAt(3);
					String sub_cui = (String) u.elementAt(4);
					String sub_label = sub_name + " (" + sub_source + ":" + sub_code + ", " + sub_type + ", " + sub_cui + ")";
					TreeItem sub_item = new TreeItem(sub_source + ":" + sub_code + ":" + sub_type + ":" + sub_cui, sub_label);
					sub_item._expandable = false;
					sub_tree_item_vec.add(sub_item);
					//item.addChild(ASSOCIATION_NAME, sub_item);
					//item._expandable = true;
				}
                if (sub_tree_item_vec.size() > 0) {
					sub_tree_item_vec = SortUtils.quickSort(sub_tree_item_vec);
					for (int j=0; j<sub_tree_item_vec.size(); j++) {
						TreeItem sub_item = (TreeItem) sub_tree_item_vec.elementAt(j);
						item.addChild(ASSOCIATION_NAME, sub_item);
						item._expandable = true;
					}
				}
				tree_item_vec.add(item);

				//root.addChild(ASSOCIATION_NAME, item);
				//root._expandable = true;
		    } else {
				String label = (String) code2nameMap.get(vs_code);
				String code = vs_code;
				label = label + " (" + code + ")";
				TreeItem item = new TreeItem(code, label);
				item._expandable = false;
				//root.addChild(ASSOCIATION_NAME, item);
				//root._expandable = true;

				tree_item_vec.add(item);
			}
		}

		tree_item_vec = SortUtils.quickSort(tree_item_vec);

		for (int i=0; i<tree_item_vec.size(); i++) {
			TreeItem item = (TreeItem) tree_item_vec.elementAt(i);
			root.addChild(ASSOCIATION_NAME, item);
			root._expandable = true;
		}
        return root;
    }





    public void exportTree(String filename, TreeItem root) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(filename, "UTF-8");
		    exportTree(pw, root);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + filename + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

    public void exportTree(PrintWriter pw, TreeItem ti) {
        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            if (children != null && children.size() > 0) {
				for (int i=0; i<children.size(); i++) {
					TreeItem childItem = (TreeItem) children.get(i);
					//String name = escapeHtml(ti._text);
					String name = ti._text;
					//testing:
					//String child_name = escapeHtml(childItem._text);
					String child_name = childItem._text;
					pw.println(name + "|" + ti._code + "|" + child_name + "|" + childItem._code);
					exportTree(pw, childItem);
				}
		    }
        }
	}


	public HashMap create_key2dataMap(String inputfile) {
		String key = null;
		//PrintWriter pw = null;
		BufferedReader br = null;
		//int n = inputfile.lastIndexOf(".");
		//String outputfile = inputfile.substring(0, n) + "_tree.txt";

		String name = null;
		String code = null;
		String CUI = null;

		String assocText = ASSOCIATION_NAME;
		HashMap key2dataMap = new HashMap();

		try {
			//pw = new PrintWriter(outputfile, "UTF-8");

			br = new BufferedReader(
			   new InputStreamReader(
						  new FileInputStream(inputfile), "UTF8"));
			/*
            FileReader a = new FileReader(inputfile);
            br = new BufferedReader(a);
            */
            String line;
            String id = null;

            line = br.readLine();
            while (line != null) {
				Vector w = StringUtils.parseData(line);
				try {
					//Acanthoma|C7419|GARD|Acanthoma|8604|PT|C084696
					name = (String) w.elementAt(0);
					code = (String) w.elementAt(1);
					key = name + " (" + code + ")";
					String source = (String) w.elementAt(2);
					String atom_name = (String) w.elementAt(3);
					String atom_code = (String) w.elementAt(4);
					String atom_type = (String) w.elementAt(5);
					String cui = (String) w.elementAt(6);
					String value = source + "|" + atom_name + "|" + atom_code + "|" + atom_type + "|" + cui;
					Vector u = new Vector();
					if (key2dataMap.containsKey(key)) {
						u = (Vector) key2dataMap.get(key);
					}
					if (!u.contains(value)) {
						u.add(value);
					}
					key2dataMap.put(key, u);
				} catch (Exception ex) {
					//ex.printStackTrace();
					System.out.println("ERROR create_key2dataMap: " + line);
					System.out.println("w.size() " + w.size());
					for (int j=0; j<w.size(); j++) {
						String t = (String) w.elementAt(j);
						System.out.println("(" + j + ") " + t);
					}
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
		return key2dataMap;
	}


    public String escapeHtml(String str) {
		return StringEscapeUtils.escapeHtml4(str);
	}

    public Vector toVector(String[] a) {
		if (a == null) return null;
		Vector w = new Vector();
		for (int i=0; i<a.length; i++) {
			String t = (String) a[i];
			w.add(t);
		}
		return w;
	}

    public Vector toVector(ArrayList a) {
		if (a == null) return null;
		Vector w = new Vector();
		for (int i=0; i<a.size(); i++) {
			String t = (String) a.get(i);
			w.add(t);
		}
		return w;
	}

//????????????????????????????????????????????????????????????????????????????????????

	public TreeItem buildTree(String mappingCSVFile) {
		Vector tree_item_vec = new Vector();
		HashMap code2nameMap = new HashMap();
		Vector vs_key_vec = new Vector();
		HashMap hmap = new HashMap();
		String[] a = null;
		//Vector w = FileUtils.readFile(mappingCSVFile);

		List list = ExcelToCSV.readCSV(mappingCSVFile, true);
		for (int i=0; i<list.size(); i++) {
			a = (String[]) list.get(i);
			Vector u = toVector(a);
			//Vector u = StringUtils.parseData(t, ",");
			//System.out.println("\tu.size(): " + u.size());
			String code = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			String cui = (String) u.elementAt(2);
			String ncim_pt = (String) u.elementAt(3);
			String source = (String) u.elementAt(4);
			String atom_type = (String) u.elementAt(5);
			String atom_code = (String) u.elementAt(6);
			String atom_name = (String) u.elementAt(7);
			String key = name + " (" + code + ")";

			atom_name = atom_name.trim();
			if (atom_name.length() > 0) {
				String value = source + "|" + atom_name + "|" + atom_code + "|" + atom_type + "|" + cui;
				Vector u2 = new Vector();
				if (hmap.containsKey(key)) {
					u2 = (Vector) hmap.get(key);
				}
				if (!u2.contains(value)) {
					u2.add(value);
				}
				hmap.put(key, u2);
		    }
			code2nameMap.put(code, name);
			vs_key_vec.add(code);
		}
		vs_key_vec = SortUtils.quickSort(vs_key_vec);
		System.out.println("vs_key_vec: " + vs_key_vec.size());
		if (hmap == null) return null;
		TreeItem root = new TreeItem("R_1", "NCIt Neoplasm Core");
		root._expandable = false;
		Iterator it = hmap.keySet().iterator();
		Vector key_vec = new Vector();
		while (it.hasNext()) {
			String key = (String) it.next();
			key_vec.add(key);
		}
		key_vec = SortUtils.quickSort(key_vec);
		System.out.println("key_vec: " + key_vec.size());

		//int lcv = 0;
		for (int i=0; i<vs_key_vec.size(); i++) {
			String vs_code = (String) vs_key_vec.elementAt(i);
			String vs_name = (String) code2nameMap.get(vs_code);
			String key = vs_name + " (" + vs_code + ")";

			if (key_vec.contains(key)) {
				String label = extractLabel(key);
				String code = extractCode(key);
				TreeItem item = new TreeItem(code, key);
				item._expandable = false;
				Vector v = (Vector) hmap.get(key);
				Vector sub_tree_item_vec = new Vector();

				for (int j=0; j<v.size(); j++) {
					String s = (String) v.elementAt(j);

					//System.out.println("(*) " + s);

					Vector u = StringUtils.parseData(s);

					String sub_source = (String) u.elementAt(0);
					String sub_name = (String) u.elementAt(1);
					String sub_code = (String) u.elementAt(2);
					String sub_type = (String) u.elementAt(3);
					String sub_cui = (String) u.elementAt(4);
					sub_name = sub_name.trim();
					if (sub_name.length() > 0) {
						String sub_label = sub_name + " (" + sub_source + ":" + sub_code + ", " + sub_type + ", " + sub_cui + ")";
						TreeItem sub_item = new TreeItem(sub_source + ":" + sub_code + ":" + sub_type + ":" + sub_cui, sub_label);
						sub_item._expandable = false;
						sub_tree_item_vec.add(sub_item);
				    }
				}
                if (sub_tree_item_vec.size() > 0) {
					sub_tree_item_vec = SortUtils.quickSort(sub_tree_item_vec);
					for (int j=0; j<sub_tree_item_vec.size(); j++) {
						TreeItem sub_item = (TreeItem) sub_tree_item_vec.elementAt(j);
						item.addChild(ASSOCIATION_NAME, sub_item);
						item._expandable = true;
					}
				}
				tree_item_vec.add(item);
		    } else {
				String label = (String) code2nameMap.get(vs_code);
				String code = vs_code;
				label = label + " (" + code + ")";
				TreeItem item = new TreeItem(code, label);
				item._expandable = false;
				tree_item_vec.add(item);
			}
		}
		tree_item_vec = SortUtils.quickSort(tree_item_vec);
		for (int i=0; i<tree_item_vec.size(); i++) {
			TreeItem item = (TreeItem) tree_item_vec.elementAt(i);
			root.addChild(ASSOCIATION_NAME, item);
			root._expandable = true;
		}
        return root;
    }

	public TreeItem buildTree(String debugfile, String mappingCSVFile) {
		Vector tree_item_vec = new Vector();
		HashMap code2nameMap = new HashMap();
		Vector vs_key_vec = new Vector();
		HashMap hmap = new HashMap();
		String[] a = null;
		TreeItem root = null;
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(debugfile, "UTF-8");

			List list = ExcelToCSV.readCSV(mappingCSVFile, true);

			for (int i=0; i<list.size(); i++) {
				a = (String[]) list.get(i);
				Vector u = toVector(a);
				//Vector u = StringUtils.parseData(t, ",");
				//System.out.println("\tu.size(): " + u.size());
				String code = (String) u.elementAt(0);
				String name = (String) u.elementAt(1);
				String cui = (String) u.elementAt(2);
				String ncim_pt = (String) u.elementAt(3);
				String source = (String) u.elementAt(4);
				String atom_type = (String) u.elementAt(5);
				String atom_code = (String) u.elementAt(6);
				String atom_name = (String) u.elementAt(7);
				String key = name + " (" + code + ")";

				atom_name = atom_name.trim();
				if (atom_name.length() > 0) {
					String value = source + "|" + atom_name + "|" + atom_code + "|" + atom_type + "|" + cui;
					Vector u2 = new Vector();
					if (hmap.containsKey(key)) {
						u2 = (Vector) hmap.get(key);
					}
					if (!u2.contains(value)) {
						u2.add(value);
					}

					pw.println(key + " --> " + value);

					hmap.put(key, u2);
				}
				code2nameMap.put(code, name);
				vs_key_vec.add(code);
			}
			vs_key_vec = SortUtils.quickSort(vs_key_vec);
			System.out.println("vs_key_vec: " + vs_key_vec.size());
			if (hmap == null) return null;
			root = new TreeItem("R_1", "NCIt Neoplasm Core");
			root._expandable = false;
			Iterator it = hmap.keySet().iterator();
			Vector key_vec = new Vector();
			while (it.hasNext()) {
				String key = (String) it.next();
				key_vec.add(key);
			}
			key_vec = SortUtils.quickSort(key_vec);
			System.out.println("key_vec: " + key_vec.size());

			//int lcv = 0;
			for (int i=0; i<vs_key_vec.size(); i++) {
				String vs_code = (String) vs_key_vec.elementAt(i);
				String vs_name = (String) code2nameMap.get(vs_code);
				String key = vs_name + " (" + vs_code + ")";

				if (key_vec.contains(key)) {
					String label = extractLabel(key);
					String code = extractCode(key);
					TreeItem item = new TreeItem(code, key);
					item._expandable = false;
					Vector v = (Vector) hmap.get(key);
					Vector sub_tree_item_vec = new Vector();

					for (int j=0; j<v.size(); j++) {
						String s = (String) v.elementAt(j);

						//System.out.println("(*) " + s);

						Vector u = StringUtils.parseData(s);

						String sub_source = (String) u.elementAt(0);
						String sub_name = (String) u.elementAt(1);
						String sub_code = (String) u.elementAt(2);
						String sub_type = (String) u.elementAt(3);
						String sub_cui = (String) u.elementAt(4);
						sub_name = sub_name.trim();
						if (sub_name.length() > 0) {
							String sub_label = sub_name + " (" + sub_source + ":" + sub_code + ", " + sub_type + ", " + sub_cui + ")";

pw.println("\n" + sub_label);
pw.println(sub_source + ":" + sub_code + ":" + sub_type + ":" + sub_cui);

							TreeItem sub_item = new TreeItem(sub_source + ":" + sub_code + ":" + sub_type + ":" + sub_cui, sub_label);
							sub_item._expandable = false;
							sub_tree_item_vec.add(sub_item);
						}
					}
					if (sub_tree_item_vec.size() > 0) {
						sub_tree_item_vec = SortUtils.quickSort(sub_tree_item_vec);
						for (int j=0; j<sub_tree_item_vec.size(); j++) {
							TreeItem sub_item = (TreeItem) sub_tree_item_vec.elementAt(j);
							item.addChild(ASSOCIATION_NAME, sub_item);
							item._expandable = true;
						}
					}
					tree_item_vec.add(item);
				} else {
					String label = (String) code2nameMap.get(vs_code);
					String code = vs_code;
					label = label + " (" + code + ")";
					TreeItem item = new TreeItem(code, label);
					item._expandable = false;
					tree_item_vec.add(item);
				}
			}
			tree_item_vec = SortUtils.quickSort(tree_item_vec);
			for (int i=0; i<tree_item_vec.size(); i++) {
				TreeItem item = (TreeItem) tree_item_vec.elementAt(i);
				root.addChild(ASSOCIATION_NAME, item);
				root._expandable = true;
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


        return root;
    }


	public TreeItem buildTreeFromExcel(String mappingExcelFile) {
		Vector tree_item_vec = new Vector();
		HashMap code2nameMap = new HashMap();
		Vector vs_key_vec = new Vector();
		HashMap hmap = new HashMap();
		ArrayList<String> a = null;
		List list = null;
		try {
		    list = new ExcelToCSV().readExcelFile(mappingExcelFile);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		System.out.println("readExcelFile returns: " + list.size() + " records.");
		for (int i=0; i<list.size(); i++) {
			a = (ArrayList<String>) list.get(i);
			Vector u = toVector(a);
			String code = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			String cui = (String) u.elementAt(2);
			String ncim_pt = (String) u.elementAt(3);
			String source = (String) u.elementAt(4);
			String atom_type = (String) u.elementAt(5);
			String atom_code = (String) u.elementAt(6);
			String atom_name = (String) u.elementAt(7);
			String key = name + " (" + code + ")";

			System.out.println(key);

			atom_name = atom_name.trim();
			if (atom_name.length() > 0) {
				String value = source + "|" + atom_name + "|" + atom_code + "|" + atom_type + "|" + cui;
				Vector u2 = new Vector();
				if (hmap.containsKey(key)) {
					u2 = (Vector) hmap.get(key);
				}
				if (!u2.contains(value)) {
					u2.add(value);
				}
				hmap.put(key, u2);
		    }
			code2nameMap.put(code, name);
			vs_key_vec.add(code);
		}
		vs_key_vec = SortUtils.quickSort(vs_key_vec);
		System.out.println("vs_key_vec: " + vs_key_vec.size());

		if (hmap == null) return null;
		TreeItem root = new TreeItem("R_1", "NCIt Neoplasm Core");
		root._expandable = false;
		Iterator it = hmap.keySet().iterator();
		Vector key_vec = new Vector();
		while (it.hasNext()) {
			String key = (String) it.next();
			key_vec.add(key);
		}
		key_vec = SortUtils.quickSort(key_vec);
		System.out.println("key_vec: " + key_vec.size());

		//int lcv = 0;
		for (int i=0; i<vs_key_vec.size(); i++) {
			String vs_code = (String) vs_key_vec.elementAt(i);
			String vs_name = (String) code2nameMap.get(vs_code);

			System.out.println(vs_code + " --> " + vs_name);

			String key = vs_name + " (" + vs_code + ")";

			if (key_vec.contains(key)) {
				String label = extractLabel(key);
				String code = extractCode(key);
				TreeItem item = new TreeItem(code, key);
				item._expandable = false;
				Vector v = (Vector) hmap.get(key);
				Vector sub_tree_item_vec = new Vector();

				for (int j=0; j<v.size(); j++) {
					String s = (String) v.elementAt(j);
					Vector u = StringUtils.parseData(s);

					String sub_source = (String) u.elementAt(0);
					String sub_name = (String) u.elementAt(1);
					String sub_code = (String) u.elementAt(2);
					String sub_type = (String) u.elementAt(3);
					String sub_cui = (String) u.elementAt(4);
					sub_name = sub_name.trim();
					if (sub_name.length() > 0) {
						String sub_label = sub_name + " (" + sub_source + ":" + sub_code + ", " + sub_type + ", " + sub_cui + ")";
						TreeItem sub_item = new TreeItem(sub_source + ":" + sub_code + ":" + sub_type + ":" + sub_cui, sub_label);
						sub_item._expandable = false;
						sub_tree_item_vec.add(sub_item);
				    }
				}
                if (sub_tree_item_vec.size() > 0) {
					sub_tree_item_vec = SortUtils.quickSort(sub_tree_item_vec);
					for (int j=0; j<sub_tree_item_vec.size(); j++) {
						TreeItem sub_item = (TreeItem) sub_tree_item_vec.elementAt(j);
						item.addChild(ASSOCIATION_NAME, sub_item);
						item._expandable = true;
					}
				}
				tree_item_vec.add(item);
		    } else {
				String label = (String) code2nameMap.get(vs_code);
				String code = vs_code;
				label = label + " (" + code + ")";
				TreeItem item = new TreeItem(code, label);
				item._expandable = false;
				tree_item_vec.add(item);
			}
		}
		tree_item_vec = SortUtils.quickSort(tree_item_vec);
		for (int i=0; i<tree_item_vec.size(); i++) {
			TreeItem item = (TreeItem) tree_item_vec.elementAt(i);
			root.addChild(ASSOCIATION_NAME, item);
			root._expandable = true;
		}
        return root;
    }

/*
    public void generateASCIITreeFromExcel(String mappingExcelFile, String treeFile) {
		long ms = System.currentTimeMillis();

System.out.println("generateASCIITreeFromExcel Step 1 buildTreeFromExcel");

        //TreeItem ti = buildTree(mappingCSVFile);

        TreeItem ti = buildTreeFromExcel(mappingExcelFile);

        String relationship_file = 	"parent_child.txt";
System.out.println("generateASCIITreeFromExcel Step 2 exportTree");

		//exportTree(relationship_file, ti);
		generate_mapping_parent_child_relationship_file(mappingExcelFile, treeFile);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(treeFile, "UTF-8");
			Vector v = FileUtils.readFile(relationship_file);

System.out.println("generateASCIITreeFromExcel Step 3 hh.printTree");


			HierarchyHelper hh = new HierarchyHelper(v);
			hh.set_show_code(false);
			hh.printTree(pw);


            for (int i=0; i<v.size(); i++) {
				String t = (String) v.elementAt(i);
				pw.println(t);
			}


 		} catch (Exception ex) {
 			ex.printStackTrace();
 		} finally {
 			try {
 				pw.close();
 				System.out.println("Tree file " + treeFile + " generated.");
 			} catch (Exception ex) {
 				ex.printStackTrace();
 			}
		}
		System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	}
*/

/*
    public void generate_mapping_parent_child_relationship_file(String excelfile, String outputfile) {
		System.out.println("excelfile: " + excelfile);
		PrintWriter pw = null;

        FileWriter fw = null;
        BufferedWriter bw = null;

		try {
            fw = new FileWriter(new File(outputfile));//"parent_child.txt"));
            bw = new BufferedWriter(fw);
            List list = new ExcelToCSV().readExcelFile(excelfile);
            String curr_code = "";
            for (int i=0; i<list.size(); i++) {
				ArrayList<String> a = (ArrayList<String>) list.get(i);
				String code = (String) a.get(0);
				String name = (String) a.get(1);
				String label = name + " (" + code + ")";

				if (code.compareTo(curr_code) != 0) {
					bw.write("NCIt Neoplasm Core|R_1|" + label + "|" + code);
					bw.newLine();
					curr_code = code;
					//Acute Lymphoblastic Leukemia by Gene Expression Profile (C121973)|C121973
				}
				String cui = (String) a.get(2);
				String ncim_pt = (String) a.get(3);
				String source = (String) a.get(4);
				source = source.trim();
				if (source.length() > 0) {
					String atom_type = (String) a.get(5);
					String atom_code = (String) a.get(6);
					String atom_name = (String) a.get(7);
					//Yolk Sac Tumor (C3011)|C3011|Yolk sac tumour site unspecified (MDR:10048251, PT, C0014145)|MDR:10048251:PT:C0014145
					String sub_code  = source + ":" + atom_code + ":" + atom_type + ":" + cui;
					String sub_label = atom_name + " (" + sub_code + ")";
					bw.write(label + "|" + code + "|" + sub_label + "|" + sub_code);
					bw.newLine();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			try {
				bw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
*/

    public void generate_ascii_tree(String excelfile, String outputfile) {
		System.out.println("excelfile: " + excelfile);
		PrintWriter pw = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
		try {
            fw = new FileWriter(new File(outputfile));//"parent_child.txt"));
            bw = new BufferedWriter(fw);

            bw.write("NCIt Neoplasm Core");
            bw.newLine();
            String subnode = null;

            List list = new ExcelToCSV().readExcelFile(excelfile);
            String curr_code = "";
            Vector subnode_data = new Vector();
            for (int i=0; i<list.size(); i++) {
				ArrayList<String> a = (ArrayList<String>) list.get(i);
				String code = (String) a.get(0);
				String name = (String) a.get(1);
				String label = name + " (" + code + ")";

				if (code.compareTo(curr_code) != 0) {
					if (subnode_data != null && subnode_data.size() > 0) {
						subnode_data = SortUtils.quickSort(subnode_data);
						for (int k=0; k<subnode_data.size(); k++) {
							subnode = (String) subnode_data.elementAt(k);
							bw.write("\t\t" + subnode);
							bw.newLine();
						}
						subnode_data = new Vector();
					}

					bw.write("\t" + label);
					bw.newLine();
					curr_code = code;
					//Acute Lymphoblastic Leukemia by Gene Expression Profile (C121973)|C121973
				}
				String cui = (String) a.get(2);
				String ncim_pt = (String) a.get(3);
				String source = (String) a.get(4);
				source = source.trim();
				if (source.length() > 0) {
					String atom_type = (String) a.get(5);
					String atom_code = (String) a.get(6);
					String atom_name = (String) a.get(7);
					//Yolk Sac Tumor (C3011)|C3011|Yolk sac tumour site unspecified (MDR:10048251, PT, C0014145)|MDR:10048251:PT:C0014145
					String sub_code  = source + ":" + atom_code + ", " + atom_type + ", " + cui;
					//Acanthomas (MSH:D049309, PM, C0846967)

					subnode = atom_name + " (" + sub_code + ")";
					subnode_data.add(subnode);

					//bw.write("\t\t" + subnode);
					//bw.newLine();
				}
			}

			if (subnode_data != null && subnode_data.size() > 0) {
				subnode_data = SortUtils.quickSort(subnode_data);
				for (int k=0; k<subnode_data.size(); k++) {
					subnode = (String) subnode_data.elementAt(k);
					bw.write("\t\t" + subnode);
					bw.newLine();
				}
				subnode_data = new Vector();
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			try {
				bw.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

    public Vector load_ascii_tree_from_excel(String excelfile) {
		System.out.println("excelfile: " + excelfile);
		Vector v = new Vector();
		try {
            v.add("NCIt Neoplasm Core");
            String subnode = null;
            List list = new ExcelToCSV().readExcelFile(excelfile);
            String curr_code = "";
            Vector subnode_data = new Vector();
            for (int i=0; i<list.size(); i++) {
				ArrayList<String> a = (ArrayList<String>) list.get(i);
				String code = (String) a.get(0);
				String name = (String) a.get(1);
				String label = name + " (" + code + ")";

				if (code.compareTo(curr_code) != 0) {
					if (subnode_data != null && subnode_data.size() > 0) {
						subnode_data = SortUtils.quickSort(subnode_data);
						for (int k=0; k<subnode_data.size(); k++) {
							subnode = (String) subnode_data.elementAt(k);
							v.add("\t\t" + subnode);
						}
						subnode_data = new Vector();
					}

					v.add("\t" + label);
					curr_code = code;
					//Acute Lymphoblastic Leukemia by Gene Expression Profile (C121973)|C121973
				}
				String cui = (String) a.get(2);
				String ncim_pt = (String) a.get(3);
				String source = (String) a.get(4);
				source = source.trim();
				if (source.length() > 0) {
					String atom_type = (String) a.get(5);
					String atom_code = (String) a.get(6);
					String atom_name = (String) a.get(7);
					//Yolk Sac Tumor (C3011)|C3011|Yolk sac tumour site unspecified (MDR:10048251, PT, C0014145)|MDR:10048251:PT:C0014145
					String sub_code  = source + ":" + atom_code + ", " + atom_type + ", " + cui;
					//Acanthomas (MSH:D049309, PM, C0846967)
					subnode = atom_name + " (" + sub_code + ")";
					subnode_data.add(subnode);
				}
			}

			if (subnode_data != null && subnode_data.size() > 0) {
				subnode_data = SortUtils.quickSort(subnode_data);
				for (int k=0; k<subnode_data.size(); k++) {
					subnode = (String) subnode_data.elementAt(k);
					v.add("\t\t" + subnode);
				}
				subnode_data = new Vector();
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return v;
	}
}




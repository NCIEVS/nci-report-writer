package gov.nih.nci.evs.app.neopl;


import gov.nih.nci.evs.browser.utils.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.lang3.StringEscapeUtils;


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


public class ASCII2HTMLTreeConverter {

	//static String Constants.ASSOCIATION_NAME = "has_child";
	static int lines_to_skip = 0;


    public ASCII2HTMLTreeConverter() {

	}

	public void set_lines_to_skip(int num) {
		this.lines_to_skip = num;
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
		try {
			br = new BufferedReader(new FileReader(value_set_ascii_file));
			line = br.readLine();
			int k = 0;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(cvsSplitBy);
				String field_1 = (String) values[0];
				field_1 = field_1.substring(1, field_1.length()-1);
				String field_2 = (String) values[1];
				field_2 = field_2.substring(1, field_2.length()-1);
				int n = line.lastIndexOf(",");
				String category = line.substring(n+1, line.length());
				category = category.substring(1, category.length()-1);
				categoryMap.put(field_1, category);

				if (category.compareTo("Malignant") == 0) {
					category_malignant++;
				} else if (category.compareTo("Benign") == 0) {
					category_benign++;
				} else {
					category_undetermined++;
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return categoryMap;
    }

	public int getLevel(String t) {
		int level = 0;
		for (int i=0; i<t.length(); i++) {
			char c = t.charAt(i);
			if (c == '\t') {
				level++;
			} else {
				break;
			}
		}
		return level;
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

    public int findMaximumLevel(String filename) {
		Vector v = FileUtils.readFile(filename);
		int maxLevel = 0;
		for (int i=lines_to_skip; i<v.size(); i++) {
			int j=i+1;
			String t = (String) v.elementAt(i);
			int n = getLevel(t);
			String level = new Integer(n).toString();
			if (maxLevel < n) {
				maxLevel = n;
			}
		}
		return maxLevel;
	}


	public Vector getRoots(TreeItem ti) {
		Vector w = new Vector();
        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
                w.add(childItem);
			}
        }
        return w;
	}


	public TreeItem getChild(TreeItem ti, int index) {
        int n = 0;
        for (String association : ti._assocToChildMap.keySet()) {
            List<TreeItem> children = ti._assocToChildMap.get(association);
            for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				n++;
				if (n == index) {
					return childItem;
				}
			}
        }
        return null;
	}


    public TreeItem createTreeItem(String filename) {
		//Vector v = FileUtils.readFile(filename);
		Vector v = new NCImMappingToASCIITree().load_ascii_tree_from_excel(filename);
		int maxLevel = 2;//findMaximumLevel(filename);

		System.out.println("createTreeItem maxLevel: " + maxLevel);
		return createTreeItem(v, maxLevel);
	}


	public void dumpVector(String label, Vector v) {
		System.out.println("\n" + label);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}
	}


    public void saveToFile(Vector v, String outputfile) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			for (int i=0; i<v.size(); i++) {
				String t = (String) v.elementAt(i);
				pw.println(t);
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

    public TreeItem createTreeItem(Vector v, int maxLevel) {
		//saveToFile(v, "echo_tree.txt");

		String assocText = Constants.ASSOCIATION_NAME;
		int root_count = 0;
		TreeItem[] treeItem_array = new TreeItem[maxLevel+1];
		for (int k=0; k<treeItem_array.length; k++) {
			treeItem_array[k] = null;
		}

		TreeItem root = new TreeItem("<Root>", "Root node");
		root._expandable = false;
		int child_count = 0;
		try {
			for (int i=lines_to_skip; i<v.size(); i++) {
				String t = (String) v.elementAt(i);
				int level = getLevel(t);
				String code = extractCode(t);
				String name = extractLabel(t);

    			if (code == null || code.startsWith("R_")) {
					root_count++;
					code = "ROOT_" + root_count;

				}
				if (level == 0) {
					TreeItem item = treeItem_array[0];
					if (item != null) {
						root.addChild(Constants.ASSOCIATION_NAME, item);
						root._expandable = true;
					}
					TreeItem new_item = new TreeItem(code, name);
					new_item._id = "N_" + root_count;
					treeItem_array[0] = new_item;
					child_count = 0;

				} else {
                    TreeItem parent_ti = treeItem_array[level-1];
					TreeItem new_item = new TreeItem(code, name);
					String parent_id = parent_ti._id;
					child_count++;
					new_item._id = parent_id + "_" + child_count;
					parent_ti.addChild(Constants.ASSOCIATION_NAME, new_item);
					parent_ti._expandable = true;
					treeItem_array[level] = new_item;
				}
			}
			TreeItem item = treeItem_array[0];
			root.addChild(Constants.ASSOCIATION_NAME, item);
			root._expandable = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return root;
	}

	private static String trim(String t) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<t.length(); i++) {
			char c = t.charAt(i);
			if (c != '\t') {
				buf.append(c);
			}
		}
		String s = buf.toString();
		s = s.trim();
		return s;
	}


    public Vector getContents(Vector v) {
		boolean start = false;
		int k = 0;
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			t = t.trim();
			if (t.compareTo("Contents") == 0) {
				start = true;
				k = i;
				break;
			}
		}
		Vector contents = new Vector();
		for (int i=k+1; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			if (t.startsWith("\t")) {
				contents.add(t);
			} else {
				break;
			}
		}
		return contents;
	}

	public String getToday() {
		java.util.Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		return sdf.format(date);
	}


	public void generateHTMLTree(String filename, String outputfile) {

		System.out.println("generateHTMLTree filename: " + filename);
		System.out.println("generateHTMLTree outputfile: " + outputfile);

//KLO (testing)
		//Vector v = FileUtils.readFile(filename);
		Vector v = new NCImMappingToASCIITree().load_ascii_tree_from_excel(filename);

		String title = null;
		String date = null;
		Vector contents = new Vector();
		if (lines_to_skip > 0) {
		    title = (String) v.elementAt(0);
		    date = (String) v.elementAt(1);
		    contents = getContents(v);
		}

		System.out.println("generateHTMLTree createTreeItem: " + filename);
	    TreeItem root = createTreeItem(filename);
	    System.out.println("Done generateHTMLTree createTreeItem: " + filename);


	    HashSet hset = new HashSet();
	    if (root == null) {
			System.out.println("root == null");
		} else {
			List<TreeItem> children = root._assocToChildMap.get(Constants.ASSOCIATION_NAME);
			if (children == null) {
				System.out.println("children is null???");
			} else {
				for (TreeItem child_item : children) {
					hset.add(child_item._text);
				}
			}
	    }
	    SimpleTreeUtils simpleTreeUtils = new SimpleTreeUtils(hset);
	    simpleTreeUtils.set_CHECKBOX_OPTION(false);
	    simpleTreeUtils.setBasePath("");
	    simpleTreeUtils.set_collapse_all_at_initialization(true);
        simpleTreeUtils.set_node_clicked_method_name("on_node_clicked");
        simpleTreeUtils.set_hyperlink_code(true);
        PrintWriter out = null;
        try {
			out = new PrintWriter(outputfile, "UTF-8");
			HashMap tree_map = new HashMap();
			tree_map.put("<Root>", root);
			simpleTreeUtils.printTree(out, tree_map);
			simpleTreeUtils.writeFooter(out);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				out.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }


    public void asciiTree2HTMLTree(String inputfile, int lines_to_skip, String outputfile) {
        set_lines_to_skip(lines_to_skip);
        System.out.println("asciiTree2HTMLTree calling generateHTMLTree");
		generateHTMLTree(inputfile, outputfile);
	}


    public void addHTMLContent(String htmltreefile, String outputfile, String title, String ncit_version, Vector contents) {
		new HTMLHeaderUtils().run(htmltreefile, outputfile, title, ncit_version, contents);
	}

    public void addHTMLContent(String htmltreefile, String outputfile, String title, String label, String ncit_version, Vector contents) {
		new HTMLHeaderUtils().run(htmltreefile, outputfile, title, label, ncit_version, contents);
	}

    public void run(String asciiTree, String outputfile, int lines_to_skip, String title, String label, String ncit_version, Vector contents) {

		System.out.println("ASCIIHTMLTreeConverter run...");

		int n = asciiTree.lastIndexOf(".");
		String contentFile = asciiTree.substring(0, n) + "_ctn.txt";

		System.out.println("ASCIIHTMLTreeConverter asciiTree..." + asciiTree);
		System.out.println("ASCIIHTMLTreeConverter contentFile..." + contentFile);

		System.out.println("ASCIIHTMLTreeConverter asciiTree2HTMLTree...lines_to_skip: " + lines_to_skip);
		asciiTree2HTMLTree(asciiTree, lines_to_skip, contentFile);
		addHTMLContent(contentFile, outputfile, title, label, ncit_version, contents);
	}

    public void run(String asciiTree, String outputfile, String title, String ncit_version) {
		run(asciiTree, outputfile, 0, title, title, ncit_version, null);
	}

    public void run(String asciiTree, String outputfile, String title) {
		run(asciiTree, outputfile, 0, title, title, null, null);
	}

 	public static void main(String[] args) {
		ASCII2HTMLTreeConverter generator = new ASCII2HTMLTreeConverter();
        String inputfile = args[0];
        String outputfile = args[1];
        String num_str = args[2];
        String ncit_version = args[3];//"16.05e";
        int lines_to_skip = Integer.parseInt(num_str);
        generator.asciiTree2HTMLTree(inputfile, lines_to_skip, outputfile);

		String title = "NCIt Neoplasm Core Hierarchy by Neoplastic Status";
		title = "NCIt Neoplasm Core Hierarchy by Morphology and Site";
		Vector contents = new Vector();
		contents.add("Neoplasm by Site");
		contents.add("Neoplasm by Morphology");

		//contents.add("Malignant Neoplasm");
		//contents.add("Benign Neoplasm");
		//contents.add("Neoplasm of Intermediate or Uncertain Behavior, or Comprising Mixed Subtypes");


		String contentFile = outputfile;//"neoplasm_core_mapping.html";
		//String final_outputfile = "final_" + outputfile;;
		generator.addHTMLContent(contentFile, outputfile, title, ncit_version, contents);
	}

}

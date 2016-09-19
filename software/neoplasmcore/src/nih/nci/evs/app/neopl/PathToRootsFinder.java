package gov.nih.nci.evs.app.neopl;


import gov.nih.nci.evs.browser.bean.*;
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


public class PathToRootsFinder {
	//static String Constants.ASSOCIATION_NAME = "has_child";
	//static String ROOT_CODE = "@";
    HierarchyHelper hierarchyHelper = null;

    public PathToRootsFinder(Vector v) {
        hierarchyHelper = new HierarchyHelper(v);
	}

	public HierarchyHelper getHierarchyHelper() {
		return hierarchyHelper;
	}

	public Vector findPathToRoots(String code) {
		boolean debug_on = false;
		if (code.compareTo("code") == 0) {
			System.out.println("*********************************DEBUGER_ON******************************");
			debug_on = true;
		}

/*
level 2: C6535
looking for: C6535|R_1
path: C6535|R_1
(*) path: C6535|R_1
path: C6535|C3402|R_3
path: C6535|C3402|C3055|R_3
*/

	    Vector v = new Vector();
	    Stack stack = new Stack();
	    stack.push(code);
	    while (!stack.isEmpty()) {
			String path = (String) stack.pop();

			if (debug_on) {
				System.out.println(path);
			}

			Vector u = StringUtils.parseData(path);
			int size = u.size();
			String next_code = (String) u.elementAt(size-1);

			if (debug_on) {
				System.out.println("next_code: " + next_code);
			}

			Vector superclass_codes = hierarchyHelper.getSuperclassCodes(next_code);
			if (superclass_codes == null || superclass_codes.size() == 0) {
				v.add(path);
			} else {
				for (int i=0; i<superclass_codes.size(); i++) {
					String superclass_code = (String) superclass_codes.elementAt(i);

					if (debug_on) {
						System.out.println("superclass_code: " + superclass_code);
					}

					stack.push(path + "|" + superclass_code);
				}
			}
		}
		return v;
	}


	public void dumpTreeHashMap(HashMap hmap) {
		Iterator it = hmap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			TreeItem ti = (TreeItem) hmap.get(key);
			System.out.println(ti._text + " (" + ti._code + ") " + " expandable: " + ti._expandable);
		}
	}


	public TreeItem convertToTreeItem(String pathToRoot) {
		Vector v = StringUtils.parseData(pathToRoot);
		int size = v.size();
		String root_code = (String) v.elementAt(size-1);
		String root_name = (String) hierarchyHelper.getLabel(root_code);
		if (root_name == null) {
			root_name = "Not specified";
		}
		HashMap hmap = new HashMap();
		TreeItem root = new TreeItem(root_code, root_name);
		hmap.put(root_code, root);

		for (int i=0; i<v.size()-1; i++) {
			int j = v.size()-i-2;
			String code = (String) v.elementAt(j);
			String name = (String) hierarchyHelper.getLabel(code);
			String parent_code = (String) v.elementAt(j+1);
			String parent_name = (String) hierarchyHelper.getLabel(parent_code);

			TreeItem sub_ti = new TreeItem(code, name);
			sub_ti._expandable = false;
			TreeItem sup_ti = (TreeItem) hmap.get(parent_code);
			if (sup_ti == null) {
				sup_ti = new TreeItem(parent_code, parent_name);
				sup_ti._expandable = true;
				hmap.put(parent_code, sup_ti);
			}
			sup_ti.addChild(Constants.ASSOCIATION_NAME, sub_ti);
			sup_ti._expandable = true;
			hmap.put(code, sub_ti);
			hmap.put(parent_code, sup_ti);
		}
		assignExpandable(root);
		return root;
	}

	public TreeItem getPathToRootTree(String code) {
		TreeItem root = new TreeItem(Constants.ROOT_CODE, "Root node");
		Vector v = findPathToRoots(code);
		for (int i=0; i<v.size(); i++) {
			String pathToRoot = (String) v.elementAt(i);
			TreeItem ti = convertToTreeItem(pathToRoot);
			root.addChild(Constants.ASSOCIATION_NAME, ti);
			root._expandable = true;
		}
		return root;
	}

	public void assignExpandable(TreeItem ti) {
		ti._expandable = false;
		if (getChildCount(ti) > 0) {
			ti._expandable = true;
		}
		for (String association : ti._assocToChildMap.keySet()) {
			List<TreeItem> children = ti._assocToChildMap.get(association);

			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				assignExpandable(childItem);
			}
        }
	}



    public int getChildCount(TreeItem ti) {
		int knt = 0;
		for (String association : ti._assocToChildMap.keySet()) {
			List<TreeItem> children = ti._assocToChildMap.get(association);
			knt = knt + children.size();
        }
        return knt;
	}

	public void printTree(PrintWriter pw, TreeItem ti, int depth) {
		String indent = "";
		for (int i=0; i<depth; i++) {
			indent = indent + "\t";
		}
		String line = indent + ti._text + " (" + ti._code + ")";
		if (getChildCount(ti) > 0) {
		//if (ti._expandable ) {
			ti._expandable = true;
			line = line + " [+]";
		}
		pw.println(line);
		for (String association : ti._assocToChildMap.keySet()) {
			List<TreeItem> children = ti._assocToChildMap.get(association);
			for (int i=0; i<children.size(); i++) {
				TreeItem childItem = (TreeItem) children.get(i);
				printTree(pw, childItem, depth + 1);
			}
        }
	}

	public static void main(String[] args) {
		String filename = args[0];
		Vector v = FileUtils.readFile(filename);
        PathToRootsFinder pathToRootsFinder = new PathToRootsFinder(v);

        String code = args[1];//"C2852"; // Adenocarcinoma
        String outputfile = code + ".txt";
        if (args.length > 2) {
        	outputfile = args[2];//"C2852"; // Adenocarcinoma
		}

        TreeItem root = pathToRootsFinder.getPathToRootTree(code);
        HashMap hmap = new HashMap();
        hmap.put("<Root>", root);
        PrintWriter pw = null;
        //String outputfile = code + ".txt";
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
        	pathToRootsFinder.printTree(pw, root, 0);
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

}

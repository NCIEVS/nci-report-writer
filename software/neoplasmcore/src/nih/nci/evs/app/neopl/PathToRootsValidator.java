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


public class PathToRootsValidator {
	Vector parent_child = null;
	PathToRootsFinder pathToRootsFinder = null;
	HashMap _parent2childcodesMap = null;
	HierarchyHelper hierarchyHelper = null;

    public PathToRootsValidator() {
		this.parent_child = null;
	}


    public PathToRootsValidator(Vector v) {
		this.parent_child = v;
		this.pathToRootsFinder = new PathToRootsFinder(v);
		this.hierarchyHelper = pathToRootsFinder.getHierarchyHelper();
		this._parent2childcodesMap = pathToRootsFinder.getHierarchyHelper().get_parent2childcodesMap();
	}

	public void dumpVector(PrintWriter pw, Vector w, Vector exclusion) {
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			if (!exclusion.contains(t)) {
				pw.println(t);
			}
		}
	}

	public void dumpVector(Vector w) {
		for (int i=0; i<w.size(); i++) {
			String t = (String) w.elementAt(i);
			int j=i+1;
			System.out.println("(" + j + ") " + t);
		}
	}


	public String areSiblingsOf(String parent_name, String parent_code, String child_name, String child_code) {
	    Iterator it = _parent2childcodesMap.keySet().iterator();
	    while (it.hasNext()) {
			String key = (String) it.next();
			Vector v = (Vector) _parent2childcodesMap.get(key);
			if (v != null) {
				if (v.contains(parent_code) && v.contains(child_code)) {
					String key_name = pathToRootsFinder.getHierarchyHelper().getLabel(key);
					System.out.println("WARNING: " + "both " + parent_name + " (" + parent_code + ") "
					   + "and " + child_name + " (" + child_code + ") "
					   + "are children of " + key_name + " (" + key + ") ");
					return key;
				}
			}
		}
		return null;
	}


	public boolean isAncestorOf(String code_1, String code_2) {
	    Stack stack = new Stack();
	    stack.push(code_2);
	    while (!stack.isEmpty()) {
			String code = (String) stack.pop();
			Vector superclass_codes = hierarchyHelper.getSuperclassCodes(code);
			if (superclass_codes != null && superclass_codes.size() > 0) {
				if (superclass_codes.contains(code_1)) return true;
				for (int i=0; i<superclass_codes.size(); i++) {
					String superclass_code = (String) superclass_codes.elementAt(i);
					stack.push(superclass_code);
				}
			}
		}
		return false;
	}


	public boolean validateSiblings(Vector sibling_vec) {
		if (sibling_vec.size() == 1) return true;
		for (int i=1; i<sibling_vec.size(); i++) {
			String code_1 = (String) sibling_vec.elementAt(i);
			for (int j=0; j<i; j++) {
				String code_2 = (String) sibling_vec.elementAt(j);
				if (isAncestorOf(code_1, code_2)) return false;
				if (isAncestorOf(code_2, code_1)) return false;
			}
		}
		return true;
	}


    public Vector validate_parent_child_relationships() {
		Vector v = new Vector();
		for (int i=0; i<parent_child.size(); i++) {
			String t = (String) parent_child.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
			Vector u = StringUtils.parseData(t);
			String parent_name = (String) u.elementAt(0);
			String parent_code = (String) u.elementAt(1);
			String child_name = (String) u.elementAt(2);
			String child_code = (String) u.elementAt(3);
			String siblingsOf_code = areSiblingsOf(parent_name, parent_code, child_name, child_code);
			if (siblingsOf_code != null) {
				String siblingsOf_name = pathToRootsFinder.getHierarchyHelper().getLabel(siblingsOf_code);
				System.out.println("WARNING: " + "Both " + parent_name + " (" + parent_code + ") "
				   + "and " + child_name + " (" + child_code + ") "
				   + "are children of " + siblingsOf_name + " (" + siblingsOf_code + ") ");

				// remove the sibling from the hashmap
                Vector w = (Vector) _parent2childcodesMap.get(siblingsOf_code);
                boolean removed = w.remove(child_code);
                if (removed) {
					System.out.println(child_name + " (" + child_code + ") " + " removed from subclasses of "
					+ siblingsOf_name + " (" + siblingsOf_code + ") ");
					String removed_line = siblingsOf_name + "|" + siblingsOf_code + "|" + child_name + "|" + child_code;
					v.add(removed_line);
				}
			}
		}
		return v;
	}

    public Vector validate_sibling_relationships() {
		Vector v = new Vector();
		Iterator it = _parent2childcodesMap.keySet().iterator();
		int lcv = 0;
		while (it.hasNext()) {
			String parent_code = (String) it.next();
			String parent_name = hierarchyHelper.getLabel(parent_code);
			lcv++;
			System.out.println("(" + lcv + ") " + parent_name + " (" + parent_code + ")");
			Vector sibling_vec = (Vector) _parent2childcodesMap.get(parent_code);
			for (int i=1; i<sibling_vec.size(); i++) {
				String code_1 = (String) sibling_vec.elementAt(i);
				for (int j=0; j<i; j++) {
					String code_2 = (String) sibling_vec.elementAt(j);
					if (isAncestorOf(code_1, code_2)) {
						// remove child concept code_2 from parent_code
						String child_name = hierarchyHelper.getLabel(code_2);
						String line = parent_name + "|" + parent_code + "|" + child_name + "|" + code_2;
				        v.add(line);
					} else if (isAncestorOf(code_2, code_1)) {
						// remove child concept code_1 from parent_code
						String child_name = hierarchyHelper.getLabel(code_1);
						String line = parent_name + "|" + parent_code + "|" + child_name + "|" + code_1;
				        v.add(line);
				    }
				}
			}
		}
		return v;
	}

	public static void main(String[] args) {
		String filename = args[0];
		PathToRootsValidator test = null;

        PrintWriter pw = null;

        String outputfile = args[1];//"siblings.txt";
        System.out.println("validate_parent_child_relationships ...");
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			System.out.println("Loading " + filename + "...");
			Vector v = FileUtils.readFile(filename);
			test = new PathToRootsValidator(v);
        	Vector removed = test.validate_parent_child_relationships();

			System.out.println("\tBefore: " + v.size());
			System.out.println("\t" + removed.size() + " removed.");
			test.dumpVector(removed);
			int size = v.size() - removed.size();
			System.out.println("\tAfter: " + size);

			test.dumpVector(pw, v, removed);

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

        System.out.println("validate_sibling_relationships ...");
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			System.out.println("Loading " + filename + "...");
			Vector v = FileUtils.readFile(filename);
			test = new PathToRootsValidator(v);
        	Vector removed = test.validate_sibling_relationships();

			System.out.println("\tBefore: " + v.size());
			System.out.println("\t" + removed.size() + " removed.");
			test.dumpVector(removed);
			int size = v.size() - removed.size();
			System.out.println("\tAfter: " + size);

			test.dumpVector(pw, v, removed);

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

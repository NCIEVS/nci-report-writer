package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import gov.nih.nci.evs.browser.bean.*;
import gov.nih.nci.evs.browser.utils.*;
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


public class NeoplasmCoreApplication {
	/*
	static String RELATIONSHIP_FILE_PREFIX = "Neoplasm_Core_Rels_NCIt_Molecular_";

	static String HIERARCHY_FILE_PREFIX = "Neoplasm_Core_Hierarchy_";
	static String HIERARCHY_FILE_PREFIX_PLUS = "Neoplasm_Core_Hierarchy_Plus_";

	static String HIERARCHY_FILE_PREFIX_2 = "Neoplasm_Core_Hierarchy_By_Neoplastic_Status_";
	static String HIERARCHY_FILE_PREFIX_2_PLUS = "Neoplasm_Core_Hierarchy_By_Neoplastic_Status_Plus_";

    static String MAPPING_FILE_PREFIX = "Neoplasm_Core_Mappings_NCIm_Terms_";
    */

	public static void generateHierarchyFiles(String parent_child_file,
	                                          String value_set_ascii_file,
	                                          boolean retree,
	                                          String ncit_version) {
        String date = VSTreeGenerator.getToday("yyyy-MM-dd");
        String neoplasmCore2NCImCSV = Constants.MAPPING_FILE_PREFIX + date + ".csv";

System.out.println("Step 1");

		FileFormatter formatter = new FileFormatter();
		String from = "images/";
		String to = "";
		formatter.setStringReplacement(from, to);

		boolean retree_using_neoplastic_categories = retree;
		//String parent_child_file = args[0];
		System.out.println("NCIt_parent_child_file: " + parent_child_file);

		//String value_set_ascii_file = args[1];
		System.out.println("value_set_ascii_file: " + value_set_ascii_file);

System.out.println("Step 2");

		//String retree = args[2];


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

		//String ncit_version = args[3];

		//parent_child_file: NCIt all distance-1 relationships
		//value_set_ascii_file: core neoplasm value set
		System.out.println("Initializing value set tree generator ...");
		VSTreeGenerator generator = new VSTreeGenerator(parent_child_file, value_set_ascii_file);

        if (!retree_using_neoplastic_categories) {
			System.out.println("Loading neoplasm_roots ...");
			Vector roots = FileUtils.readFile("neoplasm_roots.out");
			generator.initialize_stack(roots);
	    }

System.out.println("Step 4");

		//Apply flooding algorithm to generate all links of the core neoplasm tree stemming from the root concept, Neoplasm.
		System.out.println("generate_parent_child_relationships ...");
		Vector parent_child_vec = generator.generate_parent_child_relationships();
		if (parent_child_vec == null) {
			System.out.println("VSTreeGenerator generate_parent_child_relationships returns null");
		} else {
			//Get all roots
			if (retree_using_neoplastic_categories) {
				System.out.println("get_root_data...");
				Vector root_data = generator.get_root_data(parent_child_vec);
				generator.dumpVector("Roots: ", root_data);
				generator.extend_parent_child_vec(root_data);
			}
	    }

System.out.println("Step 5");

	    Vector exclusion_vec = generator.savePathData("path.txt");

		System.out.println("get_parent_child_vec ...");
		Vector w = generator.get_parent_child_vec();

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

        String mappingfile = neoplasmCore2NCImCSV;//from MetathesaurusUtils
        generator.run_appender(value_set_ascii_file, mappingfile, htmlfile, treefile);
	}


	public static String getVersion(String parent_child_file) {
		//parent_child_16.07d.txt
		if (!parent_child_file.startsWith("parent_child_")) {
			System.out.println("Invalid file format: " + parent_child_file);
			return null;
		}
		int n = parent_child_file.lastIndexOf("_");
		String version = parent_child_file.substring(n+1, parent_child_file.length());
		n = version.lastIndexOf(".");
		version = version.substring(0, n);
		return version;
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



    public static void run(String value_set_ascii_file, String owlfile, String parent_child_file) {
		long ms = System.currentTimeMillis();
		boolean test_mode = false;
		System.out.println(value_set_ascii_file);
		System.out.println(owlfile);
		System.out.println(parent_child_file);
		String ncit_version = getVersion(parent_child_file);
		System.out.println("ncit_version: " + ncit_version);
		if (test_mode) {
			System.exit(0);
		}

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
		neoplasmCoreRelationships.convertToExcel(csvfile);

		NeoplasmCoreApplication application = new NeoplasmCoreApplication();
		//site tree (false)
		application.generateHierarchyFiles(parent_child_file,
	                                       value_set_ascii_file,
	                                       false,
	                                       ncit_version);

		application.generateHierarchyFiles(parent_child_file,
	                                       value_set_ascii_file,
	                                       true,
	                                       ncit_version);
	}

    public static void main(String [ ] args) {
		String value_set_ascii_file = args[0];//"value set file";
		String owlfile = args[1]; //ncit file
		String parent_child_file = args[2];
		run(value_set_ascii_file, owlfile, parent_child_file);
	}

}


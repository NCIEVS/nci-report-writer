package gov.nih.nci.evs.app.neopl;

import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;
import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.json.JsonConverterFactory;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.service.TreeService;
import org.lexevs.tree.service.TreeServiceFactory;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;


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


public class NeoplasmCoreMappingGenerator {
	//public static String MAPPING_FILE_PREFIX = "Neoplasm_Core_Mappings_NCIm_Terms_";

	public static HashMap createCode2NameHashMap(String vsdxlsfile) {
		boolean skip_heading = true;
		List list = ExcelToCSV.readCSV(vsdxlsfile, skip_heading);
		HashMap hmap = new HashMap();
		for (int i=0; i<list.size(); i++) {
			String[] a = (String[]) list.get(i);
			String code = a[0];
			String name = a[1];
			hmap.put(code, name);
		}
		return hmap;
	}

    public static String getVersion(String owlfile) {
		int n = owlfile.lastIndexOf("_");
		String t = owlfile.substring(n+1, owlfile.length());
		n = t.lastIndexOf(".");
		t = t.substring(0, n);
		t = t.trim();
		return t;
	}

	public static void addValueSetHeadingLine(String valueSetCSVFile) {
		String VALUESET_HEADING_STR = "\"Code\",\"Preferred Term\",\"Synonyms\",\"Definition\",\"Neoplastic Status\"";

		Vector v = gov.nih.nci.evs.app.neopl.FileUtils.readFile(valueSetCSVFile);
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(valueSetCSVFile, "UTF-8");
			pw.println(VALUESET_HEADING_STR);
			for (int i=0; i<v.size(); i++) {
			    String t = (String) v.elementAt(i);
			    pw.println(t);
			}
		} catch (Exception ex) {

		} finally {
			try {
				pw.close();
				//System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


    public static void run(String valueSetXLSFile, String valueSetCSVFile) {
		boolean test_mode = false;
        try {
            ExcelToCSV converter = new ExcelToCSV();
            converter.convertExcelToCSV(valueSetXLSFile, valueSetCSVFile);
            System.out.println("addValueSetHeadingLine ...");
            addValueSetHeadingLine(valueSetCSVFile);
            System.out.println("Done addValueSetHeadingLine ...");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String ncit_version = getVersion(valueSetCSVFile);//args[1]; //"16.07d";
		System.out.println(valueSetCSVFile);
		System.out.println(ncit_version);

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		gov.nih.nci.evs.app.neopl.CodingSchemeUtils util = new gov.nih.nci.evs.app.neopl.CodingSchemeUtils(lbSvc);
		util.generateLocalNameMap("localNameMap.txt");

        String title = "Core Mappings: NCIm Terms";
        String label = "NCIt Neoplasm Core Mappings to NCIm Source Terms";
        String date = gov.nih.nci.evs.app.neopl.StringUtils.getToday("YYYY-MM-dd");
        String neoplasmCore2NCImXLS = gov.nih.nci.evs.app.neopl.Constants.MAPPING_FILE_PREFIX + date + ".xls";
        String neoplasmCore2NCImCSV = gov.nih.nci.evs.app.neopl.Constants.MAPPING_FILE_PREFIX + date + ".csv";
        String neoplasmCore2NCImHTML = gov.nih.nci.evs.app.neopl.Constants.MAPPING_FILE_PREFIX + date + ".html";
        String neoplasmCore2NCImTXT = gov.nih.nci.evs.app.neopl.Constants.MAPPING_FILE_PREFIX + date + ".txt";
        String neoplasmCore2NCImSRC = gov.nih.nci.evs.app.neopl.Constants.MAPPING_FILE_PREFIX + date + "_wrk.txt";
        String outputfile = neoplasmCore2NCImXLS;
        int n = 0;

		HashMap code2NameHashMap = new HashMap();
		int column_index = 0;
		boolean skip_heading = true;
		Vector codes = ExcelToCSV.extractColumnDataFromCSV(valueSetCSVFile, column_index, skip_heading);
		column_index = 1;
		Vector names = ExcelToCSV.extractColumnDataFromCSV(valueSetCSVFile, column_index, skip_heading);
		System.out.println("codes.size(): " + codes.size());
		for (int i=0; i<codes.size(); i++) {
			String code = (String) codes.elementAt(i);
			String name = (String) names.elementAt(i);
			int j = i+1;
			code2NameHashMap.put(code, name);
			//System.out.println("(" + j + ")" + code + " --> " + name);
		}

		if (test_mode) {
			codes = new Vector();
			codes.add("C80307");
			codes.add("C2854");
			codes.add("C7419");
			codes.add("C36055");
			codes.add("C82430");
			codes.add("C5532");
			codes.add("C115212");
	    }

		NeoplasmCore2NCIm test = new NeoplasmCore2NCIm(lbSvc);
		test.setCode2NameHashMap(code2NameHashMap);

		long ms = System.currentTimeMillis();
		Vector v = test.getMappingData(codes);
		System.out.println("Total getMappingData run time (ms): " + (System.currentTimeMillis() - ms));


		PrintWriter pw1 = null;
		String mapping_file = "mapping_" + date + ".txt";
		try {
			pw1 = new PrintWriter(mapping_file, "UTF-8");
			for (int k=0; k<v.size(); k++) {
				String t = (String) v.elementAt(k);
				pw1.println(t);
			}
		} catch (Exception ex) {

		} finally {
			try {
				pw1.close();
				System.out.println("Output file " + mapping_file + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

        int[] sort_options = new int[4];
        sort_options[0] = 1;
        sort_options[1] = 2;
        sort_options[2] = 4;
        sort_options[3] = 7;
		String delim = "|";
		v = CSVSortUtils.quickSort(v, sort_options, delim);

		CSVtoExcel csvtoExcel = new CSVtoExcel();
	    csvtoExcel.setHeadings(CSVtoExcel.HEADINGS);
	    int[] codeColumn = new int[8];
	    codeColumn[0] = 1;
	    codeColumn[1] = 0;
	    codeColumn[2] = 2;
	    codeColumn[3] = 0;
	    codeColumn[4] = 0;
	    codeColumn[5] = 0;
	    codeColumn[6] = 3;
	    codeColumn[7] = 0;

	    csvtoExcel.setCodeColumn(codeColumn);
	    csvtoExcel.setSOURCE_INDEX(4);
	    csvtoExcel.initialize();
	    System.out.println("Generating " + neoplasmCore2NCImXLS);
	    csvtoExcel.runHSSF(v, neoplasmCore2NCImXLS);

		System.out.println("Generating " + neoplasmCore2NCImCSV);
		try {
			ExcelToCSV excelToCSV = new ExcelToCSV();
			excelToCSV.setHeader("NCIt Code,NCIt Preferred Term,NCIm CUI,NCIm Preferred Name,NCIm Source,Term Type,Source Code,Source Term");
			excelToCSV.convertExcelToCSV(neoplasmCore2NCImXLS, neoplasmCore2NCImCSV);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		new NCImMappingToASCIITree().generate_ascii_tree(neoplasmCore2NCImXLS, neoplasmCore2NCImTXT);
        new ASCII2HTMLTreeConverter().run(neoplasmCore2NCImXLS, neoplasmCore2NCImSRC, 0, title, label, ncit_version, null);

        MappingHyperlinkUtils mappingHyperlinkUtils = new MappingHyperlinkUtils();
        mappingHyperlinkUtils.setStringReplacement("src=\"images/", "src=\"");
        mappingHyperlinkUtils.run(neoplasmCore2NCImSRC, neoplasmCore2NCImHTML);
    }

    public static void main(String[] args) {
		boolean test_mode = false;
		String valueSetXLSFile = args[0]; //Neoplasm_Core_16.07d.xls
        String valueSetCSVFile = args[1]; //Neoplasm_Core_16.07d.csv
        run(valueSetXLSFile, valueSetCSVFile);
    }
}


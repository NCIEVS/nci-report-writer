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


public class RVStoNCImMappingRunner {
    //public static String Constants.MAPPING_FILE_PREFIX = "Neoplasm_Core_NCIm_Terms_";

    public String getVersion(String value_set_ascii_file) {
		int n = value_set_ascii_file.lastIndexOf("_");
		String t = value_set_ascii_file.substring(n+1, value_set_ascii_file.length());
		n = t.lastIndexOf(".");
		t = t.substring(0, n);
		t = t.trim();
		return t;
	}

/*
    public void run(String value_set_ascii_file, String neoplasmCore2NCImTxt) {
        String ncit_version = getVersion(value_set_ascii_file);
        run(value_set_ascii_file, neoplasmCore2NCImTxt, ncit_version);
	}

*/
	public void generateNeoplasmCore2NCImDataFile(String value_set_ascii_file, String outputfile ) {
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		NeoplasmCore2NCIm neoplasmCore2NCIm = new NeoplasmCore2NCIm(lbSvc);
		PrintWriter pw = null;
		//String outputfile = "NeoplasmCore2NCIm.txt";
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			Vector valueSetCodes = neoplasmCore2NCIm.loadValueSetData(value_set_ascii_file);
            neoplasmCore2NCIm.run(pw, valueSetCodes);

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

/*
    public void run(String value_set_ascii_file, String neoplasmCore2NCImTxt, String ncit_version) {
		//Step 1
		File f = new File(neoplasmCore2NCImTxt);
		if(!f.exists()) {
			generateNeoplasmCore2NCImDataFile(value_set_ascii_file, neoplasmCore2NCImTxt);
	    }

		//Step 2
        String title = "Core Mappings: NCIm Terms";
        String label = "NCIt Neoplasm Core Mappings to NCIm Source Terms";
        String date = StringUtils.getToday("YYYY-MM-dd");
        String htmlfile = Constants.MAPPING_FILE_PREFIX + date + ".html";
        String treefile = Constants.MAPPING_FILE_PREFIX + date + ".txt";
        String outputfile = Constants.MAPPING_FILE_PREFIX + date + "_tmp.html";
        String neoplasmCore2NCImCSV = Constants.MAPPING_FILE_PREFIX + date + "_tmp.csv";
        String mappingTable = Constants.MAPPING_FILE_PREFIX + "_wrk.csv";
        String csvfile = Constants.MAPPING_FILE_PREFIX + date + ".csv";

        NCImMappingToASCIITree3 ncimMappingToASCIITree = new NCImMappingToASCIITree();
        ncimMappingToASCIITree.generateASCIITree(value_set_ascii_file, neoplasmCore2NCImTxt, treefile, mappingTable);

        new ASCII2HTMLTreeConverter().run(treefile, outputfile, 0, title, label, ncit_version, null);
        MappingHyperlinkUtils mappingHyperlinkUtils = new MappingHyperlinkUtils();
        mappingHyperlinkUtils.setStringReplacement("src=\"images/", "src=\"");
        mappingHyperlinkUtils.run(outputfile, htmlfile);

        //Step 3
        ncimMappingToASCIITree.constructMappingTable(neoplasmCore2NCImTxt, neoplasmCore2NCImCSV);

        //Step 4
        new RVStoNCImMapping(value_set_ascii_file, neoplasmCore2NCImCSV, mappingTable, csvfile).run();

        //Step 5
        int[] sort_options = new int[4];
        sort_options[0] = 1;
        sort_options[1] = 2;
        sort_options[2] = 4;
        sort_options[3] = 7;
		CSVSortUtils.sortCSV(csvfile, sort_options);

        //Step 6
        new CSVtoExcelRunner().run(csvfile);
	}

    public static void main(String[] args) {
		String value_set_ascii_file = args[0];
		String entire_mapping_file = args[1];
		RVStoNCImMappingRunner runner = new RVStoNCImMappingRunner();
        runner.run(value_set_ascii_file, entire_mapping_file);
    }
    */
}




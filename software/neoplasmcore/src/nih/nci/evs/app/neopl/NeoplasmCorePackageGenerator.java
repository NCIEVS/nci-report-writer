package gov.nih.nci.evs.app.neopl;


import com.opencsv.CSVReader;
import gov.nih.nci.evs.app.neopl.*;
import gov.nih.nci.evs.browser.utils.*;
import java.awt.Color;
import java.io.*;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPictureData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;


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


public class NeoplasmCorePackageGenerator {

    public NeoplasmCorePackageGenerator() {

	}


	public static void run(String owlfile, String valueSetXLSFile, boolean run_parsor, boolean run_mapping, boolean run_appl) {
		int n = valueSetXLSFile.lastIndexOf(".");
		String valueSetCSVFile = valueSetXLSFile.substring(0, n) + ".csv";
		n = owlfile.lastIndexOf("_");
		String parent_child_file = "parent_child" + owlfile.substring(n, owlfile.length());
		n = parent_child_file.lastIndexOf(".");
		parent_child_file = parent_child_file.substring(0, n) + ".txt";

		System.out.println("owlfile: " + owlfile);
		System.out.println("valueSetXLSFile: " + valueSetXLSFile);
		System.out.println("valueSetCSVFile: " + valueSetCSVFile);
		System.out.println("parent_child_file: " + parent_child_file);

        if (run_parsor) {
			System.out.println("Runnng OWLParserRunner..");
			new OWLParserRunner(owlfile).run();
	    }

	    if (run_mapping) {
			System.out.println("Runnng NeoplasmCoreMappingGenerator..");
			NeoplasmCoreMappingGenerator.run(valueSetXLSFile, valueSetCSVFile);
	    }

	    if (run_appl) {
			System.out.println("Runnng NeoplasmCoreApplication..");
			NeoplasmCoreApplication.run(valueSetCSVFile, owlfile, parent_child_file);
		}
		PackagingUtils.run();
	}

	public static void run(String owlfile, String valueSetXLSFile) {
		run(owlfile, valueSetXLSFile, true, true, true);
	}

    public static void main(String[] args) throws IOException {
		String owlfile = args[0]; //ThesaurusInferred_16.06d.owl
		String valueSetXLSFile = args[1]; //Neoplasm_Core_16.07d.xls
		//boolean run_parsor, boolean run_mapping, boolean run_appl
		boolean run_parsor = true;
		boolean run_mapping = true;
		boolean run_appl = true;
		if (args.length == 3) {
			String run_parser_str = args[2];
			if (run_parser_str.compareToIgnoreCase("false") == 0) {
				run_parsor = false;
			}
		}
		if (args.length == 4) {
			String run_parser_str = args[2];
			if (run_parser_str.compareToIgnoreCase("false") == 0) {
				run_parsor = false;
			}
			String run_mapping_str = args[3];
			if (run_mapping_str.compareToIgnoreCase("false") == 0) {
				run_mapping = false;
			}
		}
		if (args.length == 5) {
			String run_parser_str = args[2];
			if (run_parser_str.compareToIgnoreCase("false") == 0) {
				run_parsor = false;
			}
			String run_mapping_str = args[3];
			if (run_mapping_str.compareToIgnoreCase("false") == 0) {
				run_mapping = false;

			}
			String run_appl_str = args[4];
			if (run_appl_str.compareToIgnoreCase("false") == 0) {
				run_appl = false;
			}
		}
		System.out.println("Run parser? " + run_parsor);
		System.out.println("Run mapping? " + run_mapping);
		System.out.println("Run appl? " + run_appl);
        run(owlfile, valueSetXLSFile, run_parsor, run_mapping, run_appl);
	}

}

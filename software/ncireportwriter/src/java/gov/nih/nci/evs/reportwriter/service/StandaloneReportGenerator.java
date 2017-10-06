/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.service;

import java.io.*;
import java.util.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.formatter.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;
import org.lexgrid.valuesets.*;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

/**
 * @author EVS Team (Kim Ong)
 * @version 1.0
 */

public class StandaloneReportGenerator {
	public static void dumpList(String label, List list) {
		System.out.println(label);
		for (int i=0; i<list.size(); i++) {
			String filename = (String) list.get(i);
			System.out.println(filename);
		}
	}

	public static void main(String[] args) {
		String arg_0 = args[0];
		String output_dir = args[1];
		String userId = args[2];
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current working directory:" +currentDir);
        String path = currentDir + File.separator + arg_0;
        System.out.println("arg[0]: " + path);
        File file = new File(path);
        if (!file.exists()) {
			System.out.println(path + " does not exist.");
			System.exit(1);
		}

		ReportGenerationRunner runner = new ReportGenerationRunner();
		if (file.isDirectory()) {
			System.out.println("Template folder: " + arg_0);
			List list = runner.getFileCanonicalPathNamesInFolder(arg_0);
			dumpList("Template files in folder " + arg_0, list);
			new ReportGenerationRunner().generateStandardReportInBatch(path, output_dir, userId);
		} else {
			System.out.println("Template file: " + arg_0);
			runner.generateStandardReport(path, output_dir, userId);
		}
	}
}

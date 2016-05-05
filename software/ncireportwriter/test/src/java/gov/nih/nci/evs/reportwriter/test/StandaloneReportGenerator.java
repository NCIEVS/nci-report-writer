/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test;

import gov.nih.nci.evs.reportwriter.service.*;

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
	public static void main(String[] args) {
		String templateFile = args[0]; //"template.dat")
		String outputDir = args[1]; //"output")
		String adminPassword = "rwadmin";
		RWUIUtils util = new RWUIUtils();
		StandardReportTemplate template = util.loadStandardReportTemplate(templateFile);
		util.dumpStandardReportTemplate(template);
		Collection<ReportColumn> cc = template.getColumnCollection();
		int[] ncitColumns = new int[cc.size()];
		for (int i=0; i<cc.size(); i++) {
			ncitColumns[i] = 0; // set to 1 if the column is a NCIt code
		}
		Boolean retval = new ReportGenerationRunner().generateStandardReport(outputDir, adminPassword, templateFile, ncitColumns);
	}
}

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

public class StandardReportTemplateExporter {

	public StandardReportTemplateExporter() {

	}

	public static void exportStandardReportTemplates(String outputfile) {
        PrintWriter pw = null;
        try {
			pw = new PrintWriter(outputfile, "UTF-8");
			StandardReportServiceProvider provider = new StandardReportServiceProvider();
			List list = provider.getStandardReportTemplateLabels();
			for (int i=0; i<list.size(); i++) {
				String templateLabel = (String) list.get(i);
				System.out.println(templateLabel);
				StandardReportTemplate template = provider.getStandardReportTemplate(templateLabel);
				provider.dumpStandardReportTemplate(pw, template);
				pw.println("\n");
			}
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

	public static void main(String[] args) {
		String outputfile = args[0];
		exportStandardReportTemplates(outputfile);
	}
}

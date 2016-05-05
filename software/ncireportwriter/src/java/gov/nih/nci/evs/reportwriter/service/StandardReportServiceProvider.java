/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.service;

import java.io.*;
import java.util.*;

import javax.servlet.http.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.utils.*;

import org.apache.log4j.*;
import javax.faces.model.*;


/**
 * @author EVS Team (Kim Ong)
 * @version 1.0
 */


public class StandardReportServiceProvider
{

// Variable declaration
	private String outputDir;
	private String uid;
	private String emailAddress;
	private List ncitColumnList;
	private int[] ncitColumns = null;

// Default constructor
	public StandardReportServiceProvider() {
	}

	public StandardReportServiceProvider(String uid, String emailAddress) {
		this.uid = uid;
		this.emailAddress = emailAddress;
	}


// Constructor
	public StandardReportServiceProvider(
		String outputDir,
		String uid,
		String emailAddress,
		List ncitColumnList) {

		this.outputDir = outputDir;
		this.uid = uid;
		this.emailAddress = emailAddress;
		this.ncitColumnList = ncitColumnList;

		int n = ncitColumnList.size();
	    this.ncitColumns = new int[n];
	    for (int i=0; i<n; i++) {
			Integer int_obj = (Integer) ncitColumnList.get(i);
			this.ncitColumns[n] = Integer.valueOf(int_obj);
		}

	}

// Set methods
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setNcitColumnList(List ncitColumnList) {
		this.ncitColumnList = ncitColumnList;

		int n = ncitColumnList.size();
	    ncitColumns = new int[n];
	    for (int i=0; i<n; i++) {
			Integer int_obj = (Integer) ncitColumnList.get(i);
			ncitColumns[n] = Integer.valueOf(int_obj);
		}
	}


// Get methods
	public String getOutputDir() {
		return this.outputDir;
	}

	public String getUid() {
		return this.uid;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public List getNcitColumnList() {
		return this.ncitColumnList;
	}


	private int[] getSelectedColumns() {
		return ncitColumns;
	}


    public List getStandardReportTemplateLabels() {
		List label_list = new ArrayList();
        List<SelectItem> list = DataUtils.getStandardReportTemplateList();
        for (int i=0; i<list.size(); i++) {
			SelectItem item = (SelectItem) list.get(i);
			label_list.add(item.getLabel());
		}
		return label_list;
	}


    public StandardReportTemplate getStandardReportTemplate(String templateLabel) {
		SDKClientUtil sdkclientutil = new SDKClientUtil();
		StandardReportTemplate standardReportTemplate = null;
		String FQName =
			"gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
		String methodName = "setLabel";
		String key = templateLabel;
		Object standardReportTemplate_obj =
			sdkclientutil.search(FQName, methodName, key);
		if (standardReportTemplate_obj != null) {
			standardReportTemplate =
				(StandardReportTemplate) standardReportTemplate_obj;
			return standardReportTemplate;
		}
		return null;
	}


    public void dumpStandardReportTemplate(StandardReportTemplate template) {
		if (template == null) return;
		System.out.println("label:" + template.getLabel());
		System.out.println("rootConceptCode:" + template.getRootConceptCode());
		System.out.println("codingSchemeName:" + template.getCodingSchemeName());
		System.out.println("codingSchemeVersion:" + template.getCodingSchemeVersion());
		System.out.println("associationName:" + template.getAssociationName());
		System.out.println("direction:" + template.getDirection());
		System.out.println("level:" + template.getLevel());
		System.out.println("delimiter:" + template.getDelimiter());
		System.out.println("columnCollection:");
	    Collection<ReportColumn> c = template.getColumnCollection();
		Object[] a = c.toArray();
		for (int i=0; i<a.length; i++) {
			int j = i+1;
			ReportColumn col = (ReportColumn) a[i];
			System.out.println("\tcolumnNumber:" + j);
			System.out.println("\tlabel:" + col.getLabel()
			  + "\n\tfieldId:" + col.getFieldId()
			  + "\n\tpropertyType:" + col.getPropertyType()
			  + "\n\tpropertyName:" + col.getPropertyName()
			  + "\n\tisPreferred:" + col.getIsPreferred()
			  + "\n\trepresentationalForm:" + col.getRepresentationalForm()
			  + "\n\tsource:" + col.getSource()
			  + "\n\tqualifierName:" + col.getQualifierName()
			  + "\n\tqualifierValue:" + col.getQualifierValue()
			  + "\n\tdelimiter:" + col.getDelimiter()
			  + "\n\tconditionalColumnId:" + col.getConditionalColumnId()
			  + "\n");
		}
	}


    public void dumpStandardReportTemplate(PrintWriter pw, StandardReportTemplate template) {
		if (template == null) return;
		pw.println("label:" + template.getLabel());
		pw.println("rootConceptCode:" + template.getRootConceptCode());
		pw.println("codingSchemeName:" + template.getCodingSchemeName());
		pw.println("codingSchemeVersion:" + template.getCodingSchemeVersion());
		pw.println("associationName:" + template.getAssociationName());
		pw.println("direction:" + template.getDirection());
		pw.println("level:" + template.getLevel());
		pw.println("delimiter:" + template.getDelimiter());
		pw.println("columnCollection:");
	    Collection<ReportColumn> c = template.getColumnCollection();
		Object[] a = c.toArray();
		for (int i=0; i<a.length; i++) {
			int j = i+1;
			ReportColumn col = (ReportColumn) a[i];
			pw.println("\tcolumnNumber:" + j);
			pw.println("\tlabel:" + col.getLabel()
			  + "\n\tfieldId:" + col.getFieldId()
			  + "\n\tpropertyType:" + col.getPropertyType()
			  + "\n\tpropertyName:" + col.getPropertyName()
			  + "\n\tisPreferred:" + col.getIsPreferred()
			  + "\n\trepresentationalForm:" + col.getRepresentationalForm()
			  + "\n\tsource:" + col.getSource()
			  + "\n\tqualifierName:" + col.getQualifierName()
			  + "\n\tqualifierValue:" + col.getQualifierValue()
			  + "\n\tdelimiter:" + col.getDelimiter()
			  + "\n\tconditionalColumnId:" + col.getConditionalColumnId()
			  + "\n");
		}
	}

    public Boolean generateStandardReport(String outputDir,
        String standardReportLabel, String uid, String emailAddress) {
        int[] ncitColumns = getSelectedColumns();
        Thread reportgeneration_thread =
            new Thread(new ReportGenerationThread(outputDir,
                standardReportLabel, uid, emailAddress, ncitColumns));
        reportgeneration_thread.start();
        return Boolean.TRUE;
    }

}


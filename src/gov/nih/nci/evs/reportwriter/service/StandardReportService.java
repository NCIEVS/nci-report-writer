package gov.nih.nci.evs.reportwriter.service;

import gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate;
import gov.nih.nci.evs.reportwriter.utils.RemoteServerUtil;
import gov.nih.nci.evs.reportwriter.utils.SDKClientUtil;
import gov.nih.nci.system.applicationservice.EVSApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;


import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;


import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Instruction;
import org.LexGrid.concepts.Presentation;

import org.apache.log4j.Logger;

import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;

import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;

import org.LexGrid.concepts.ConceptProperty;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.ConceptProperty;

import org.LexGrid.relations.Relations;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;


import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.bean.*;


/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
 */

public class StandardReportService {

    private EVSApplicationService  appService = null;
    private LexBIGService lbSvc;

    private String serviceUrl = null;

	private String codingScheme = null;
	private String version = null;

	private PrintWriter writer = null;

	/**
	* Default constructor.
	*/
	public StandardReportService() {
		try {
			this.lbSvc = RemoteServerUtil.createLexBIGService();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public StandardReportService(String url) {
		try {
			Boolean retval = connect(url);
			if (retval == Boolean.TRUE)
			{
				this.lbSvc = RemoteServerUtil.createLexBIGService(url);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

    public LexBIGService getLexBIGService()
    {
		return this.lbSvc;
	}

	private Boolean connect(String serviceUrl)
	{
		if (serviceUrl == null || serviceUrl.compareTo("") == 0)
		{
			try {
				this.lbSvc = new LexBIGServiceImpl();
            return Boolean.TRUE;
		    } catch (Exception ex) {
				ex.printStackTrace();
				return Boolean.FALSE;
			}
		}
		try{
			System.out.println("URL: " + serviceUrl);
            this.serviceUrl = serviceUrl;
			this.appService = (EVSApplicationService) ApplicationServiceProvider.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

            this.lbSvc = (LexBIGService) this.appService;

            return Boolean.TRUE;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	public PrintWriter openPrintWriter(String outputfile)
	{
		try {
			PrintWriter pw = new PrintWriter (new BufferedWriter(new FileWriter(outputfile)));
            return pw;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
    }

	public void closePrintWriter(PrintWriter pw)
	{
		if (pw == null)
		{
			System.out.println("WARNING: pw is not open.");
			return;
		}
		pw.close();
		pw = null;
	}


    public void printReportHeading(PrintWriter pw, ReportColumn[] cols) {
		String columnHeadings = "";
		String delimeter_str = "\t";
		if (cols == null)
		{
			return;
		}
		for (int i=0; i<cols.length; i++)
		{
			ReportColumn rc = (ReportColumn) cols[i];
			columnHeadings = columnHeadings + rc.getLabel();
			if (i<cols.length-1) columnHeadings = columnHeadings + delimeter_str;
		}
		pw.println(columnHeadings);
	}


    private void traverse(PrintWriter pw, String scheme, String version, String tag, String code, String hierarchyAssociationName,
                          String associationName, boolean direction, int level, int maxLevel, ReportColumn[] cols)
    {
		//System.out.println("Level: " + level + "\tmaxLevel: " + maxLevel);
		if (maxLevel != -1 && level > maxLevel) return;

		String matchAlgorithm = "exactMatch";
		EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService();

//		LexBIGUtils lexBIGUtils = new LexBIGUtils(lbSvc);

		//Concept root = lexBIGUtils.getConceptByCode(scheme, version, tag, code);

		Concept root = DataUtils.getConceptByCode(scheme, version, tag, code);
		if (root == null) {
			System.out.println("DataUtils.getConceptByCode return null?????????????????");
			return;
		} else {
			System.out.println("DataUtils.getConceptByCode return concept " + root.getId());
		}

		String delim = "$";

		Vector v = new Vector();
		DataUtils util = new DataUtils();

        //level++;
System.out.println("DIRECTION: " + direction);

        if (direction)
        {
System.out.println("getAssociationTargets: " + direction);

			//v = lexBIGUtils.getAssociationTargets(lexBIGUtils.getLexBIGService(), scheme, version, root.getId(), associationName);
			v = util.getAssociationTargets(scheme, version, root.getId(), associationName);
		}
		else
		{
			v = util.getAssociationSources(scheme, version, root.getId(), associationName);//, matchAlgorithm);
		}

// associated concepts (i.e., concepts in subset)
        for (int i=0; i<v.size(); i++)
        {
			Concept c = (Concept) v.elementAt(i);
			pw.println(root.getId() + delim + root.getEntityDescription().getContent() + delim + c.getId() + delim + c.getEntityDescription().getContent());
		}

        Vector<Concept> subconcept_vec = util.getAssociationTargets(scheme, version, root.getId(), hierarchyAssociationName);
        if (subconcept_vec == null | subconcept_vec.size() == 0) return;
        level++;
        for (int k=0; k<subconcept_vec.size(); k++)
        {
 			Concept concept = (Concept) subconcept_vec.elementAt(k);
 			String subconcep_code = concept.getId();
            traverse(pw, scheme, version, tag, subconcep_code, hierarchyAssociationName, associationName, direction, level, maxLevel, cols);
		}
	}



    public Boolean generateStandardReport(String outputDir, String standardReportLabel, String uid)
    {
		System.out.println("Output directory: " + outputDir);
		System.out.println("standardReportLabel: " + standardReportLabel);
		System.out.println("uid: " + uid);

		File dir = new File(outputDir);
		if (!dir.exists())
		{
			System.out.println("Output directory " + outputDir + " does not exist -- try to create the directory.");
			boolean retval = dir.mkdir();
			if (!retval)
			{
				System.out.println("Unable to create output directory " + outputDir + " - please check privilege setting.");
				return Boolean.FALSE;
			}
			else
			{
				System.out.println("Output directory: " + outputDir + " created.");
			}
		}
		else
		{
			System.out.println("Output directory: " + outputDir + " exists.");
		}

		String pathname = outputDir + File.separator + standardReportLabel + ".txt";
		pathname = pathname.replaceAll(" ", "_");
		System.out.println("Full path name: " + pathname);

        StandardReportTemplate standardReportTemplate = null;
        try{
        	SDKClientUtil sdkclientutil = new SDKClientUtil();
        	String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
        	String methodName = "setLabel";
        	Object obj = sdkclientutil.search(FQName, methodName, standardReportLabel);
			standardReportTemplate = (StandardReportTemplate) obj;
			System.out.println("standardReportTemplate ID: " + standardReportTemplate.getId());
			System.out.println("standardReportTemplate label: " + standardReportTemplate.getLabel());

	    } catch(Exception e) {
			System.out.println("Error finding standardReportTemplate label: " + standardReportLabel);
		    return Boolean.FALSE;
	    }

        if (standardReportTemplate == null)
        {
			System.out.println("Unable to identify report label " + standardReportLabel + " -- report not generated." );
			return Boolean.FALSE;
		}
		else
		{
			System.out.println("Report template  " + standardReportLabel + " found.");
		}

		PrintWriter pw = openPrintWriter(pathname);
		if (pw == null)
		{
			System.out.println("Unable to create output file " + pathname + " - please check privilege setting.");
			return Boolean.FALSE;
		}
		else
		{
			System.out.println("opened PrintWriter " + pathname );
		}

		int id = -1;
		String label = null;
		String codingSchemeName = null;
		String codingSchemeVersion = null;
		String rootConceptCode = null;
		String associationName = null;
		int level = -1;
		char delim = '$';

		id = standardReportTemplate.getId();
		label = standardReportTemplate.getLabel();
		codingSchemeName = standardReportTemplate.getCodingSchemeName();
		codingSchemeVersion = standardReportTemplate.getCodingSchemeVersion();
		rootConceptCode = standardReportTemplate.getRootConceptCode();
		associationName = standardReportTemplate.getAssociationName();
		boolean direction = standardReportTemplate.getDirection();
		level = standardReportTemplate.getLevel();
		Character delimiter = standardReportTemplate.getDelimiter();

		pw.println("ID: " + id);
		pw.println("Label: " + label);
		pw.println("CodingSchemeName: " + codingSchemeName);
		pw.println("CodingSchemeVersion: " + codingSchemeVersion);
		pw.println("Root: " + rootConceptCode);
		pw.println("AssociationName: " + associationName);
		pw.println("Direction: " + direction);
		pw.println("Level: " + level);
		pw.println("Delimiter: " + delimiter);

		System.out.println("ID: " + id);
		System.out.println("Label: " + label);
		System.out.println("CodingSchemeName: " + codingSchemeName);
		System.out.println("CodingSchemeVersion: " + codingSchemeVersion);
		System.out.println("Root: " + rootConceptCode);
		System.out.println("AssociationName: " + associationName);
		System.out.println("Direction: " + direction);
		System.out.println("Level: " + level);
		System.out.println("Delimiter: " + delimiter);

		String columnHeadings = "";
		String delimeter_str = "\t";

/*
    public void printReportHeading(PrintWriter pw, ReportColumn[] cols) {
*/


        Object[] objs = null;
	    java.util.Collection cc = standardReportTemplate.getColumnCollection();
	    if (cc == null)
	    {
			System.out.println("standardReportTemplate.getColumnCollection returns NULL?????????????");
		}
		else
		{
			objs = cc.toArray();
			System.out.println("standardReportTemplate.getColumnCollection objs: -------------------------" + objs.length);
		}


System.out.println("Start For Loop: -------------------------" + objs.length);

	    ReportColumn[] cols = null;
	    if (cc != null) {

			System.out.println("standardReportTemplate.getColumnCollection -------------------------" + objs.length);
	        cols = new ReportColumn[objs.length];

	        if (objs.length > 0) {
                for(int i=0; i<objs.length; i++) {
	                gov.nih.nci.evs.reportwriter.bean.ReportColumn col = (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];

					pw.println("\nReport Column:");
					pw.println("ID: " + col.getId());
					pw.println("Label: " + col.getLabel());
					pw.println("Column Number: " + col.getColumnNumber());
					pw.println("PropertyType: " + col.getPropertyType());
					pw.println("PropertyName: " + col.getPropertyName());
					pw.println("IsPreferred: " + col.getIsPreferred());
					pw.println("RepresentationalForm: " + col.getRepresentationalForm());
					pw.println("Source: " + col.getSource());
					pw.println("QualifierName: " + col.getQualifierName());
					pw.println("QualifierValue: " + col.getQualifierValue());
					pw.println("Delimiter: " + col.getDelimiter());
					pw.println("ConditionalColumnIdD: " + col.getConditionalColumnId());

					cols[i] = col;

					//columnHeadings = columnHeadings + col.getLabel();
					//if (i<objs.length-1) columnHeadings = columnHeadings + delimeter_str;

				}
			}
		}

        pw.println("\n\n");

System.out.println("Calling printReportHeading -------------------------");


System.out.println("********** Start generating report..." + pathname);


        String scheme = standardReportTemplate.getCodingSchemeName();
        String version = standardReportTemplate.getCodingSchemeVersion();
        String code = standardReportTemplate.getRootConceptCode();
        associationName = standardReportTemplate.getAssociationName();
        level = standardReportTemplate.getLevel();

        String tag = null;

        int curr_level = 0;
        int max_level = standardReportTemplate.getLevel();
        if (max_level == -1) max_level = 1;//100;
        //boolean direction = standardReportTemplate.getDirection();

        printReportHeading(pw, cols);
        String matchAlgorithm = "exactMatch";

        Vector hierarchicalAssoName_vec = new DataUtils().getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null || hierarchicalAssoName_vec.size() == 0) return Boolean.FALSE;

        String hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);

		System.out.println("********** traverse..." );
        traverse(pw, scheme, version, tag, code, hierarchicalAssoName, associationName, direction, curr_level, max_level, cols);

        closePrintWriter(pw);
        // convert to Excel:

        // createStandardReport -- need user's loginName
        // StandardReport extends Report
        // private StandardReportTemplate template;

        System.out.println("Output file " + pathname + " generated.");

        // convert tab-delimited file to Excel (to be implemented)



		return Boolean.TRUE;

	}


	private Boolean validReport(
          String standardReportTemplate_value,
		  String reportFormat_value,
		  String reportStatus_value,
		  String user_value) {

        try{
        	    SDKClientUtil sdkclientutil = new SDKClientUtil();

				String FQName = null;
				String methodName = null;
				String key = null;

				StandardReportTemplate standardReportTemplate = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
				methodName = "setLabel";
				key = standardReportTemplate_value;
				Object standardReportTemplate_obj = sdkclientutil.search(FQName, methodName, key);
				if (standardReportTemplate_obj == null) {
					System.out.println("Object " + standardReportTemplate_value + " not found.");
					return Boolean.FALSE;
				}

				ReportFormat reportFormat = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
				methodName = "setDescription";
				key = reportFormat_value;
				Object reportFormat_obj = sdkclientutil.search(FQName, methodName, key);
				if (reportFormat_obj == null) {
					System.out.println("Object " + reportFormat_value + " not found.");
					return Boolean.FALSE;
				}

				ReportStatus reportStatus = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
				methodName = "setLabel";
				key = reportStatus_value;
				Object reportStatus_obj = sdkclientutil.search(FQName, methodName, key);
				if (reportStatus_obj == null) {
					System.out.println("Object " + reportStatus_value + " not found.");
					return Boolean.FALSE;
				}

				User user = null;
				FQName = "gov.nih.nci.evs.reportwriter.bean.User";
				methodName = "setLoginName";
				key = user_value;
				Object user_obj = sdkclientutil.search(FQName, methodName, key);
				if (user_obj == null) {
					System.out.println("Object " + user_value + " not found.");
					return Boolean.FALSE;
				}

				return Boolean.TRUE;

          } catch(Exception e) {
        	  e.printStackTrace();
          }

          return null;

	}


	private Boolean createStandardReport(
		String label,
		String pathName,

        String templateLabel,
		String format,
		String status,
		String uid) {

		// Method called upon generation of two reports (tab-delimited and Excel)

		// if ReportFormat does not exist -- createReportFormat

        try{
			  Boolean retval = validReport(templateLabel, format, status, uid);
			  if (retval != Boolean.FALSE)
			  {
				  System.out.println("Report object not created.");
				  return Boolean.FALSE;
			  }


        	  SDKClientUtil sdkclientutil = new SDKClientUtil();
        	  String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReport";
        	  String methodName = "setLabel";
        	  String key = label;
        	  Object[] objs = sdkclientutil.search(FQName);

        	  if (objs != null)
        	  {
				  // report already exists, delete it
				  for (int i=0; i<objs.length; i++)
				  {
                  	  StandardReport report = (StandardReport) objs[i];
                  	  String reportlabel = report.getLabel();
                  	  if (label.compareTo(reportlabel) == 0)
                  	  {
						  sdkclientutil.deleteStandardReport(report);
					  }
				  }
 			  }

              java.util.Date lastModified = new Date(); // system date
        	  StandardReport report = sdkclientutil.createStandardReport(label, lastModified, pathName);

			  StandardReportTemplate standardReportTemplate = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
        	  methodName = "setLabel";
        	  key = label;

              Object template_obj = sdkclientutil.search(FQName, methodName, key);
              if (template_obj != null)
              {
				  report.setTemplate((StandardReportTemplate) template_obj);
			  }

			  ReportFormat reportformat = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
        	  methodName = "setDescription";
        	  key = format;

              Object format_obj = sdkclientutil.search(FQName, methodName, key);
              if (format_obj != null)
              {
				  report.setFormat((ReportFormat) format_obj);
			  }
			  else
			  {
				  System.out.println("Format " + format + " not found -- report not created.");
			  }

			  ReportStatus reportstatus = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
        	  methodName = "setLabel";
        	  key = status;

              Object status_obj = sdkclientutil.search(FQName, methodName, key);
              if (status_obj != null)
              {
				  report.setStatus((ReportStatus) status_obj);
			  }

			  gov.nih.nci.evs.reportwriter.bean.User user = null;
        	  FQName = "gov.nih.nci.evs.reportwriter.bean.User";
        	  methodName = "setLoginName";
        	  key = uid;

              Object user_obj = sdkclientutil.search(FQName, methodName, key);
              if (user_obj != null)
              {
				  report.setCreatedBy((gov.nih.nci.evs.reportwriter.bean.User) user_obj);
			  }

              sdkclientutil.insertStandardReport(report);

          } catch(Exception e) {
        	  e.printStackTrace();
        	  return Boolean.FALSE;
          }

          return Boolean.TRUE;
	}


    public static void main(String[] args) {

	   try {
		   StandardReportService standardReportService = new StandardReportService();
		   String standardReportLabel = "FDA-UNII Subset Report";
		   String outputfile = standardReportLabel;
		   outputfile.replaceAll(" ", "_");
		   String outputDir = "G:\\ReportWriter\\test";
		   Boolean retval = standardReportService.generateStandardReport(outputDir, standardReportLabel, "kimong");

	   } catch (Exception e) {
		   System.out.println("REQUEST FAILED !!!");
	   }
   }
}


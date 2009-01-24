package gov.nih.nci.evs.reportwriter.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.utils.RemoteServerUtil;
import gov.nih.nci.evs.reportwriter.utils.SDKClientUtil;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.EVSApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Concept;
import org.LexGrid.concepts.ConceptProperty;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Instruction;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.relations.Relations;
import org.apache.log4j.Logger;

import gov.nih.nci.evs.reportwriter.properties.ReportWriterProperties;
import gov.nih.nci.evs.reportwriter.utils.SortUtils;



/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008,2009 NGIT. This software was developed in conjunction with the National Cancer Institute,
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
  *
  * Modification history
  *     Initial implementation kim.ong@ngc.com
  *
 */

public class ReportGenerationThread implements Runnable
{
    private EVSApplicationService  appService = null;
    private LexBIGService lbSvc;
    private String serviceUrl = null;

	String outputDir = null;
	String standardReportLabel = null;
	String uid = null;

	int count = 0;

	public ReportGenerationThread(String outputDir, String standardReportLabel, String uid)
	{
		this.outputDir = outputDir;
		this.standardReportLabel = standardReportLabel;
		this.uid = uid;

		count = 0;
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


	public void run()
	{
		try {
			System.out.print("Generating report -- please wait...");
			long ms = System.currentTimeMillis();
			Boolean retval = generateStandardReport(outputDir, standardReportLabel, uid);
			System.out.print("Report " + " generated.\n");
			System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
		}
		catch (Exception e)
		{
			System.out.println("Exception " + e);
		}
	}


    public Boolean generateStandardReport(String outputDir, String standardReportLabel, String uid)
    {
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
			System.out.println("Unable to identify report label " + standardReportLabel + " -- report not generated." );
		    return Boolean.FALSE;
	    }

        if (standardReportTemplate == null)
        {
			System.out.println("Unable to identify report label " + standardReportLabel + " -- report not generated." );
			return Boolean.FALSE;
		}

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

        String version = standardReportTemplate.getCodingSchemeVersion();
        // append verision to the report file name:
		String pathname = outputDir + File.separator + standardReportLabel + "__" + version + ".txt";
		pathname = pathname.replaceAll(" ", "_");
		System.out.println("Full path name: " + pathname);

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

        Object[] objs = null;
	    java.util.Collection cc = standardReportTemplate.getColumnCollection();
	    if (cc == null)
	    {
			System.out.println("standardReportTemplate.getColumnCollection returns NULL?????????????");
		}
		else
		{
			objs = cc.toArray();
		}

	    ReportColumn[] cols = null;
	    if (cc != null) {
	        cols = new ReportColumn[objs.length];
	        if (objs.length > 0) {
                for(int i=0; i<objs.length; i++) {
	                gov.nih.nci.evs.reportwriter.bean.ReportColumn col = (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];

					System.out.println("\nReport Column:");
					System.out.println("ID: " + col.getId());
					System.out.println("Label: " + col.getLabel());
					System.out.println("Column Number: " + col.getColumnNumber());
					System.out.println("PropertyType: " + col.getPropertyType());
					System.out.println("PropertyName: " + col.getPropertyName());
					System.out.println("IsPreferred: " + col.getIsPreferred());
					System.out.println("RepresentationalForm: " + col.getRepresentationalForm());
					System.out.println("Source: " + col.getSource());
					System.out.println("QualifierName: " + col.getQualifierName());
					System.out.println("QualifierValue: " + col.getQualifierValue());
					System.out.println("Delimiter: " + col.getDelimiter());
					System.out.println("ConditionalColumnIdD: " + col.getConditionalColumnId());

					cols[i] = col;
				}
			}
		}

		System.out.println("********** Start generating report..." + pathname);

        String scheme = standardReportTemplate.getCodingSchemeName();
        version = standardReportTemplate.getCodingSchemeVersion();
        String code = standardReportTemplate.getRootConceptCode();

        Concept defining_root_concept = DataUtils.getConceptByCode(codingSchemeName, codingSchemeVersion, null, rootConceptCode);


        associationName = standardReportTemplate.getAssociationName();
        level = standardReportTemplate.getLevel();

        String tag = null;

        int curr_level = 0;
        int max_level = standardReportTemplate.getLevel();
		if (max_level == -1) {
			String max_level_str = null;
			try {
				max_level_str = ReportWriterProperties.getInstance().getProperty(ReportWriterProperties.MAXIMUM_LEVEL);
				max_level = 20;
				if (max_level_str != null)
				{
					max_level = Integer.parseInt(max_level_str);
				}
		    } catch (Exception ex) {

			}
	    }

        printReportHeading(pw, cols);

        Vector hierarchicalAssoName_vec = new DataUtils().getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null || hierarchicalAssoName_vec.size() == 0)
        {
			return Boolean.FALSE;
		}

        String hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);
        traverse(pw, scheme, version, tag, defining_root_concept, code, hierarchicalAssoName, associationName, direction, curr_level, max_level, cols);
        closePrintWriter(pw);

        System.out.println("Total number of concepts processed: " + count);

        // convert to Excel:

        // createStandardReport -- need user's loginName
        // StandardReport extends Report
        // private StandardReportTemplate template;

        System.out.println("Output file " + pathname + " generated.");

        // convert tab-delimited file to Excel

        String reportFormat_value = "Text (tab delimited)";
        String reportStatus_value = "DRAFT";

	    Boolean bool_obj = new StandardReportService().createStandardReport(
			standardReportLabel + ".txt",
			pathname,
            standardReportTemplate.getLabel(),
            reportFormat_value = "Text (tab delimited)",
            reportStatus_value = "DRAFT",
            uid);

        // convert to Excel
        bool_obj = FileUtil.convertToExcel(pathname, delimeter_str);

        // create xls report record
        pathname = outputDir + File.separator + standardReportLabel + "__" + version + ".xls";
		pathname = pathname.replaceAll(" ", "_");
		System.out.println("Full path name: " + pathname);

	    bool_obj = new StandardReportService().createStandardReport(
			standardReportLabel + ".xls",
			pathname,
            standardReportTemplate.getLabel(),
            reportFormat_value = "Microsoft Office Excel",
            reportStatus_value = "DRAFT",
            uid);

		return bool_obj;
	}

    public void printReportHeading(PrintWriter pw, ReportColumn[] cols) {

		if (cols == null)
		{
			System.out.println("** In printReportHeading numbe of ReportColumn: cols == null ??? ");
		}

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

    private void writeColumnData(PrintWriter pw, String scheme, String version, Concept defining_root_concept, Concept associated_concept, Concept c, String delim, ReportColumn[] cols) {
		//pw.println(root.getId() + delim + root.getEntityDescription().getContent() + delim + c.getId() + delim + c.getEntityDescription().getContent());
    	String output_line = "";
		for (int i=0; i<cols.length; i++)
		{
			ReportColumn rc = (ReportColumn) cols[i];
			String s = getReportColumnValue(scheme, version, defining_root_concept, associated_concept, c, rc);
            if (i == 0)
            {
				output_line = s;
			}
			else
			{
				output_line = output_line + "\t" + s;
			}
		}
        pw.println(output_line);

		count++;
		if ((count/100) * 100 == count)
		{
			System.out.println("Number of concepts processed: " + count);
		}
	}

    private void traverse(PrintWriter pw, String scheme, String version, String tag, Concept defining_root_concept, String code, String hierarchyAssociationName,
                          String associationName, boolean direction, int level, int maxLevel, ReportColumn[] cols)
    {
		//System.out.println("Level: " + level + "\tmaxLevel: " + maxLevel);
		if (maxLevel != -1 && level > maxLevel) return;
		EVSApplicationService lbSvc = new RemoteServerUtil().createLexBIGService();

		Concept root = DataUtils.getConceptByCode(scheme, version, tag, code);
		if (root == null) {
			System.out.println("WARNING: Concept with code " + code + " not found.");
			return;
		} else {
			System.out.println("Level: " + level + " Subset: " + root.getId());
		}

		String delim = "\t";

		Vector v = new Vector();
		DataUtils util = new DataUtils();
        if (direction)
        {
			v = util.getAssociationTargets(scheme, version, root.getId(), associationName);
		}
		else
		{
			v = util.getAssociationSources(scheme, version, root.getId(), associationName);
		}

		// associated concepts (i.e., concepts in subset)
        if (v == null) return;
		System.out.println("Subset size: " + v.size());

        for (int i=0; i<v.size(); i++)
        {
			// subset member element
			Concept c = (Concept) v.elementAt(i);
			writeColumnData(pw, scheme, version, defining_root_concept, root, c, delim, cols);
		}

        Vector<Concept> subconcept_vec = util.getAssociationTargets(scheme, version, root.getId(), hierarchyAssociationName);
        if (subconcept_vec == null | subconcept_vec.size() == 0) return;
        level++;
        for (int k=0; k<subconcept_vec.size(); k++)
        {
 			Concept concept = (Concept) subconcept_vec.elementAt(k);
 			String subconcep_code = concept.getId();
            traverse(pw, scheme, version, tag, defining_root_concept, subconcep_code, hierarchyAssociationName, associationName, direction, level, maxLevel, cols);
		}
	}

    public String getReportColumnValue(String scheme, String version, Concept defining_root_concept, Concept associated_concept, Concept node, ReportColumn rc) {
		return getReportColumnValue(scheme, version, defining_root_concept, associated_concept, node, null, rc);
	}


    public Vector getParentCodes(String scheme, String version, String code) {
		DataUtils util = new DataUtils();
        Vector hierarchicalAssoName_vec = util.getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null || hierarchicalAssoName_vec.size() == 0)
        {
			return null;
		}
        String hierarchicalAssoName = (String) hierarchicalAssoName_vec.elementAt(0);
        //KLO, 01/23/2009
        //Vector<Concept> superconcept_vec = util.getAssociationSources(scheme, version, code, hierarchicalAssoName);
        Vector<Concept> superconcept_vec = util.getAssociationSourceCodes(scheme, version, code, hierarchicalAssoName);
        if (superconcept_vec == null) return null;
        SortUtils.quickSort(superconcept_vec, SortUtils.SORT_BY_CODE);
        return superconcept_vec;

	}


    public String getReportColumnValue(String scheme, String version, Concept defining_root_concept, Concept associated_concept, Concept node, Concept parent, ReportColumn rc)
    {


		String field_Id = rc.getFieldId();
		String property_name = rc.getPropertyName();
		String qualifier_name = rc.getQualifierName();
		String source = rc.getSource();
		String qualifier_value = rc.getQualifierValue();
		String representational_form = rc.getRepresentationalForm();

		// check:
		Boolean isPreferred = rc.getIsPreferred();

        String property_type = rc.getPropertyType();
		char delimiter_ch = rc.getDelimiter();
		String delimiter = "" + delimiter_ch;

		if (isNull(field_Id)) field_Id= null;
		if (isNull(property_name)) property_name = null;
		if (isNull(qualifier_name)) qualifier_name = null;
		if (isNull(source)) source = null;
		if (isNull(qualifier_value)) qualifier_value = null;
		if (isNull(representational_form)) representational_form = null;
		if (isNull(property_type)) property_type = null;
		if (isNull(delimiter)) delimiter = null;

		if (field_Id.equals("Code")) return node.getId();
        if (field_Id.equals("Associated Concept Code")) return associated_concept.getId();

        Concept concept = node;
        if (property_name != null && property_name.compareTo("Contributing_Source") == 0) {
			concept = defining_root_concept;
		}
        else if (field_Id.indexOf("Associated") != -1)
        {
			concept = associated_concept;
		}
        else if (field_Id.indexOf("Parent") != -1)
        {
			Vector superconcept_vec = getParentCodes(scheme, version, node.getId());
			if (superconcept_vec != null && superconcept_vec.size() > 0 && field_Id.indexOf("1st Parent") != -1)
			{
				String superconceptCode = (String) superconcept_vec.elementAt(superconcept_vec.size()-1);
				if (field_Id.equals("1st Parent Code")) return superconceptCode;
				concept = DataUtils.getConceptByCode(scheme, version, null, superconceptCode);
				//concept = (Concept) superconcept_vec.elementAt(superconcept_vec.size()-1);
				//if (field_Id.equals("1st Parent Code")) return concept.getId();
		    }
			else if (superconcept_vec != null && superconcept_vec.size() > 1 && field_Id.indexOf("2nd Parent") != -1)
			{
				String superconceptCode = (String) superconcept_vec.elementAt(superconcept_vec.size()-2);
				if (field_Id.equals("2nd Parent Code")) return superconceptCode;
				concept = DataUtils.getConceptByCode(scheme, version, null, superconceptCode);
				//if (field_Id.equals("2nd Parent Code")) return concept.getId();
				//concept = (Concept) superconcept_vec.elementAt(superconcept_vec.size()-2);
		    }
		    else
		    {
				return "NA";
			}
		}

		org.LexGrid.commonTypes.Property[] properties = null;

		if (property_type.compareToIgnoreCase("GENERIC")== 0)
		{
			properties = concept.getConceptProperty();
		}
		else if (property_type.compareToIgnoreCase("PRESENTATION")== 0)
		{
			properties = concept.getPresentation();
		}
		else if (property_type.compareToIgnoreCase("INSTRUCTION")== 0)
		{
			properties = concept.getInstruction();
		}
		else if (property_type.compareToIgnoreCase("COMMENT")== 0)
		{
			properties = concept.getComment();
		}
		else if (property_type.compareToIgnoreCase("DEFINITION")== 0)
		{
			properties = concept.getDefinition();
		}
        String return_str = " "; // to resolve Excel cell problem
        int num_matches = 0;
        if (field_Id.indexOf("Property Qualifier") != -1)
		{
			//getRepresentationalForm
			boolean match = false;
			for (int i=0; i<properties.length; i++)
			{
				qualifier_value = null;
				org.LexGrid.commonTypes.Property p = properties[i];
				if (p.getPropertyName().compareTo(property_name) == 0) // focus on matching property
				{
					match = true;

					//<P90><![CDATA[<term-name>Black</term-name><term-group>PT</term-group><term-source>CDC</term-source><source-code>2056-0</source-code>]]></P90>

					if (source != null || representational_form != null || qualifier_name != null || isPreferred != null)
					{
                    // compare isPreferred

						if (isPreferred != null && p instanceof Presentation)
						{
							Presentation presentation = (Presentation) p;
							Boolean is_pref = presentation.getIsPreferred();
	                        if (is_pref == null)
	                        {
								match = false;
							}
							else if (!is_pref.equals(isPreferred))
							{
								match = false;
							}
						}

						// match representational_form
						if (match)
						{
							if (representational_form != null && p instanceof Presentation)
							{
								Presentation presentation = (Presentation) p;
								if (presentation.getRepresentationalForm().compareTo(representational_form) != 0)
								{
									match = false;
								}
							}
						}

						// match qualifier
						if (match)
						{
							if (qualifier_name != null) // match property qualifier, if needed
							{
								boolean match_found = false;
								PropertyQualifier[] qualifiers = p.getPropertyQualifier();
								if (qualifiers != null)
								{
									for (int j=0; j<qualifiers.length; j++)
									{
										PropertyQualifier q = qualifiers[j];
										String name = q.getPropertyQualifierId();
										String value = q.getContent();
										if (qualifier_name.compareTo(name) == 0)
										{
											match_found = true;
											qualifier_value = value;
											break;
										}
									}
							    }
								if (!match_found)
								{
									match = false;
								}
							}
						}
						// match source
						if (match)
						{
							if (source != null) //match source
							{
								boolean match_found = false;
								Source[] sources = p.getSource();
								for (int j=0; j<sources.length; j++)
								{
									Source src = sources[j];
									if (src.getContent().compareTo(source) == 0)
									{
										match_found = true;
										break;
									}
								}
								if (!match_found)
								{
									match = false;
								}
							}
						}
					}
				}
				if (match && qualifier_value != null) {
					num_matches++;
					if (num_matches == 1)
					{
						return_str = qualifier_value;
					}
					else
					{
						return_str = return_str + delimiter + qualifier_value;
					}
				}
			}
			return return_str;
		}

		else if (field_Id.compareToIgnoreCase("Property") == 0 || field_Id.compareToIgnoreCase("Associated concept property") == 0
		                                                       || field_Id.indexOf("Parent property") != -1)
		{
			for (int i=0; i<properties.length; i++)
			{
				boolean match = false;
				org.LexGrid.commonTypes.Property p = properties[i];
				if (p.getPropertyName().compareTo(property_name) == 0) // focus on matching property
				{
					match = true;

					if (source != null || representational_form != null || qualifier_name != null || isPreferred != null)
					{
                    // compare isPreferred
						if (isPreferred != null && p instanceof Presentation)
						{
							Presentation presentation = (Presentation) p;
							Boolean is_pref = presentation.getIsPreferred();
							if (is_pref == null)
							{
								match = false;
							}
							else if (is_pref != null && !is_pref.equals(isPreferred))
							{
								match = false;
							}
						}

						// match representational_form
						if (match)
						{
							if (representational_form != null && p instanceof Presentation)
							{
								Presentation presentation = (Presentation) p;
								if (presentation.getRepresentationalForm().compareTo(representational_form) != 0)
								{
									match = false;
								}
							}
						}
						// match qualifier
						if (match)
						{
							if (qualifier_name != null) // match property qualifier, if needed
							{
								boolean match_found = false;
								PropertyQualifier[] qualifiers = p.getPropertyQualifier();
								for (int j=0; j<qualifiers.length; j++)
								{
									PropertyQualifier q = qualifiers[j];
									String name = q.getPropertyQualifierId();
									String value = q.getContent();
									if (qualifier_name.compareTo(name) == 0 && qualifier_value.compareTo(value) == 0)
									{
										match_found = true;
										break;
									}
								}
								if(!match_found)
								{
									match = false;
								}
							}
						}
						// match source
						if (match)
						{
							if (source != null) //match source
							{
								boolean match_found = false;
								Source[] sources = p.getSource();
								for (int j=0; j<sources.length; j++)
								{
									Source src = sources[j];
									if (src.getContent().compareTo(source) == 0)
									{
										match_found = true;
										break;
									}
								}
								if(!match_found)
								{
									match = false;
								}
							}
						}
					}
				}
				if (match)
				{
					num_matches++;
					if (num_matches == 1)
					{
						return_str = p.getText().getContent();
					}
					else
					{
						return_str = return_str + delimiter + p.getText().getContent();
					}
				}
			}

		}
		return return_str;
	}

	private boolean isNull(String s) {
		if (s == null) return true;
		s = s.trim();
		if (s.length() == 0) return true;
		if (s.compareTo("") == 0) return true;
		if (s.compareToIgnoreCase("null") == 0) return true;
		return false;
	}


}



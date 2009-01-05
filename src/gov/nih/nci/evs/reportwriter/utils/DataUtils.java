package gov.nih.nci.evs.reportwriter.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;


import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

import java.util.ArrayList;
import java.util.List;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ListDataModel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.DataModel.Core.*;

import org.LexGrid.concepts.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.versions.*;

import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.concepts.Instruction;

import org.LexGrid.LexBIG.DataModel.Collections.*;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.relations.Relations;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;

import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;

import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

import org.LexGrid.LexBIG.Exceptions.LBException;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.Constructors;


import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistoryServiceImpl;

import org.LexGrid.LexBIG.DataModel.NCIHistory.*;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;

import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;

import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;


import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.versions.SystemRelease;

import org.LexGrid.codingSchemes.CodingSchemeVersion;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;

import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;

import org.LexGrid.naming.SupportedPropertyQualifier;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;

import org.LexGrid.codingSchemes.Mappings;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

import org.apache.log4j.*;
import org.apache.log4j.xml.*;

import org.LexGrid.commonTypes.PropertyQualifier;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;

import java.util.Vector;
import java.util.HashMap;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;

import java.util.Set;

import java.text.NumberFormat;


import javax.faces.model.SelectItem;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.bean.*;


/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2007 NGIT. This software was developed in conjunction with the National Cancer Institute,
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

public class DataUtils {

	Connection con;
	Statement stmt;
	ResultSet rs;

	private List supportedStandardReportList = new ArrayList();

	private static List standardReportTemplateList = null;
	private static List adminTaskList = null;
    private static List userTaskList = null;

    private static List propertyTypeList = null;

	private static List _ontologies = null;

	private static org.LexGrid.LexBIG.LexBIGService.LexBIGService lbSvc = null;
	public org.LexGrid.LexBIG.Utility.ConvenienceMethods lbConvMethods = null;
    public CodingSchemeRenderingList csrl = null;
    private Vector supportedCodingSchemes = null;
    private static HashMap codingSchemeMap = null;
    private Vector codingSchemes = null;

    private static HashMap csnv2codingSchemeNameMap = null;
    private static HashMap csnv2VersionMap = null;

    private static List directionList = null;

    public DataUtils()
    {
		directionList = new ArrayList();

		adminTaskList = new ArrayList();

		adminTaskList.add(new SelectItem("Administer Standard Reports"));
		adminTaskList.add(new SelectItem("Maintain Report Status"));
		adminTaskList.add(new SelectItem("Assign Report Status"));

		userTaskList = new ArrayList();
		userTaskList.add(new SelectItem("Retrieve Standard Reports"));

		standardReportTemplateList = new ArrayList();

        csnv2codingSchemeNameMap = new HashMap();
        csnv2VersionMap = new HashMap();
	}

	public static List getPropertyTypeList() {

		if (propertyTypeList == null)
		{
			propertyTypeList = new ArrayList();
			propertyTypeList.add(new SelectItem(""));
			propertyTypeList.add(new SelectItem("Comment"));
			propertyTypeList.add(new SelectItem("Definition"));
			propertyTypeList.add(new SelectItem("Generic"));
			propertyTypeList.add(new SelectItem("Instruction"));
			propertyTypeList.add(new SelectItem("Presentation"));
		}
	    return propertyTypeList;
	}


	public static List getTaskList(Boolean isAdmin) {

		if (isAdmin == null) return null;
		else if (isAdmin.equals(Boolean.TRUE))
		{
			if (adminTaskList == null)
			{
				adminTaskList = new ArrayList();
				adminTaskList.add(new SelectItem("Administer Standard Reports"));
				adminTaskList.add(new SelectItem("Maintain Report Status"));
				adminTaskList.add(new SelectItem("Assign Report Status"));
			}
		    return adminTaskList;
		}
		else
		{
			if (userTaskList == null)
			{
				userTaskList = new ArrayList();
				userTaskList.add(new SelectItem("Retrieve Standard Reports"));
			}
		    return userTaskList;
		}
	}

	//public static List getStandardReportTemplateList(Boolean isAdmin) {
	public static List getStandardReportTemplateList() {
		//if (isAdmin == null || isAdmin == Boolean.FALSE) return null;

        standardReportTemplateList = new ArrayList();

        // To be modified: retrieve the list from database using SDK writeable API

        try{
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            Object[] objs = util.search(FQName);
            for (int i=0; i<objs.length; i++)
            {
				StandardReportTemplate standardReportTemplate = (StandardReportTemplate) objs[i];
				standardReportTemplateList.add(new SelectItem(standardReportTemplate.getLabel()));
			}
        } catch(Exception e) {
        	e.printStackTrace();
        }
/*
        standardReportTemplateList.add(new SelectItem("Structured Product Labeling (SPL) Report"));
        standardReportTemplateList.add(new SelectItem("FDA-UNII Subset Report"));
        standardReportTemplateList.add(new SelectItem("Individual Case Safety (ICS) Subset Report"));
        standardReportTemplateList.add(new SelectItem("Center for Devices and Radiological Health (CDRH) Subset Report"));
        standardReportTemplateList.add(new SelectItem("FDA-SPL Country Code Report"));
*/

	    return standardReportTemplateList;
	}




	public List getSupportedStandardReports() {
		return supportedStandardReportList;
	}

    public static List getOntologyList() {
	    if(_ontologies == null) setCodingSchemeMap();
	    return _ontologies;
    }


    private static void setCodingSchemeMap()
	{
		if (_ontologies != null) return;

		_ontologies = new ArrayList();
		codingSchemeMap = new HashMap();
		csnv2codingSchemeNameMap = new HashMap();
		csnv2VersionMap = new HashMap();


        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
			CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
			for (int i=0; i<csrs.length; i++)
			{
				CodingSchemeRendering csr = csrs[i];
            	Boolean isActive = csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE);
				if (isActive != null && isActive.equals(Boolean.TRUE))
				{
					CodingSchemeSummary css = csr.getCodingSchemeSummary();
					String formalname = css.getFormalName();
					String representsVersion = css.getRepresentsVersion();
					CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
					vt.setVersion(representsVersion);

					String value = formalname + " (version: " + representsVersion + ")";
					_ontologies.add(new SelectItem(value, value));

					csnv2codingSchemeNameMap.put(value, formalname);
					csnv2VersionMap.put(value, representsVersion);

					CodingScheme scheme = null;
					try {
						scheme = lbSvc.resolveCodingScheme(formalname, vt);
						if (scheme != null)
						{
							codingSchemeMap.put((Object) formalname, (Object) scheme);

						}

				    } catch (Exception e) {
						String urn = css.getCodingSchemeURN();
						try {
							scheme = lbSvc.resolveCodingScheme(urn, vt);
							if (scheme != null)
							{
								codingSchemeMap.put((Object) formalname, (Object) scheme);
							}
						} catch (Exception ex) {

							String localname = css.getLocalName();
							try {
								scheme = lbSvc.resolveCodingScheme(localname, vt);
								if (scheme != null)
								{
									codingSchemeMap.put((Object) formalname, (Object) scheme);
								}
							} catch (Exception e2) {
								//e2.printStackTrace();
                            }
					    }
					}
			    }
			}
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static Vector<String> getSupportedAssociationNames(String key)
    {
		String codingSchemeName = (String) csnv2codingSchemeNameMap.get(key);
		if(codingSchemeName == null) return null;
		String version = (String) csnv2VersionMap.get(key);
		if(version == null) return null;
        return getSupportedAssociationNames(codingSchemeName, version);
	}


    public static Vector<String> getSupportedAssociationNames(String codingSchemeName, String version)
	{
		CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
		if (version != null)
		{
			vt.setVersion(version);
		}

		CodingScheme scheme = null;
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
			if (scheme == null) return null;

			Vector<String> v = new Vector<String>();
			SupportedAssociation[] assos = scheme.getMappings().getSupportedAssociation();
			for (int i=0; i<assos.length; i++)
			{
				 SupportedAssociation sa = (SupportedAssociation) assos[i];
				 v.add(sa.getLocalId());
			}
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public static Vector<String> getPropertyNameListData(String key)
    {
		if (csnv2codingSchemeNameMap == null)
		{
			setCodingSchemeMap();
		}

		String codingSchemeName = (String) csnv2codingSchemeNameMap.get(key);
		if(codingSchemeName == null)
		{
			return null;
		}
		String version = (String) csnv2VersionMap.get(key);
		if(version == null)
		{
			return null;
		}
        return getPropertyNameListData(codingSchemeName, version);
	}


	public static Vector<String> getPropertyNameListData(String codingSchemeName, String version) {
		CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
		if (version != null) {
			vt.setVersion(version);
		}
		CodingScheme scheme = null;
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
			if (scheme == null) return null;
			Vector<String> propertyNameListData = new Vector<String>();
            SupportedProperty[] properties = scheme.getMappings().getSupportedProperty();
            for (int i=0; i<properties.length; i++)
            {
				SupportedProperty property = properties[i];
				propertyNameListData.add(property.getLocalId());
			}
			return propertyNameListData;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public static String getCodingSchemeName(String key)
    {
 		return (String) csnv2codingSchemeNameMap.get(key);
	}

    public static String getCodingSchemeVersion(String key)
    {
 		return (String) csnv2VersionMap.get(key);
	}

    public static Vector<String> getRepresentationalFormListData(String key)
    {
		String codingSchemeName = (String) csnv2codingSchemeNameMap.get(key);
		if(codingSchemeName == null) return null;
		String version = (String) csnv2VersionMap.get(key);
		if(version == null) return null;
        return getRepresentationalFormListData(codingSchemeName, version);
	}


	public static Vector<String> getRepresentationalFormListData(String codingSchemeName, String version) {
		CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
		if (version != null) {
			vt.setVersion(version);
		}
		CodingScheme scheme = null;
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
			if (scheme == null) return null;
			Vector<String> propertyNameListData = new Vector<String>();
            SupportedRepresentationalForm[] forms = scheme.getMappings().getSupportedRepresentationalForm();
            if (forms != null)
            {
				for (int i=0; i<forms.length; i++)
				{
					SupportedRepresentationalForm form = forms[i];
					propertyNameListData.add(form.getLocalId());
				}
		    }
			return propertyNameListData;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public static Vector<String> getPropertyQualifierListData(String key)
    {
		String codingSchemeName = (String) csnv2codingSchemeNameMap.get(key);
		if(codingSchemeName == null) return null;
		String version = (String) csnv2VersionMap.get(key);
		if(version == null) return null;
        return getPropertyQualifierListData(codingSchemeName, version);
	}

	public static Vector<String> getPropertyQualifierListData(String codingSchemeName, String version) {
		CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
		if (version != null) {
			vt.setVersion(version);
		}
		CodingScheme scheme = null;
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
			if (scheme == null) return null;
			Vector<String> propertyQualifierListData = new Vector<String>();
            SupportedPropertyQualifier[] qualifiers = scheme.getMappings().getSupportedPropertyQualifier();
            for (int i=0; i<qualifiers.length; i++)
            {
				SupportedPropertyQualifier qualifier = qualifiers[i];
				propertyQualifierListData.add(qualifier.getLocalId());
			}

			return propertyQualifierListData;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


    public static Vector<String> getSourceListData(String key)
    {
		String codingSchemeName = (String) csnv2codingSchemeNameMap.get(key);
		if(codingSchemeName == null) return null;
		String version = (String) csnv2VersionMap.get(key);
		if(version == null) return null;
        return getSourceListData(codingSchemeName, version);
	}

	public static Vector<String> getSourceListData(String codingSchemeName, String version) {
		CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
		if (version != null) {
			vt.setVersion(version);
		}
		CodingScheme scheme = null;
		try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
			if (scheme == null) return null;
			Vector<String> sourceListData = new Vector<String>();

			//Insert your code here
            SupportedSource[] sources = scheme.getMappings().getSupportedSource();
            for (int i=0; i<sources.length; i++)
            {
				SupportedSource source = sources[i];
				sourceListData.add(source.getLocalId());
			}

			return sourceListData;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public static Vector<String> getReportStatusListData() {
		// To be modified:
		Vector<String> reportStatusListData = new Vector<String>();
		reportStatusListData.add("DRAFT");
		reportStatusListData.add("APPROVED");
		return reportStatusListData;
	}



}

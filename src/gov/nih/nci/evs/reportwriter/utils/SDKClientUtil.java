package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.bean.CustomizedQuery;
import gov.nih.nci.evs.reportwriter.bean.CustomizedReport;
import gov.nih.nci.evs.reportwriter.bean.Report;
import gov.nih.nci.evs.reportwriter.bean.ReportColumn;
import gov.nih.nci.evs.reportwriter.bean.ReportFormat;
import gov.nih.nci.evs.reportwriter.bean.ReportStatus;
import gov.nih.nci.evs.reportwriter.bean.StandardReport;
import gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate;
import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.SearchExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;

import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

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

public class SDKClientUtil {

	private static Logger KLO_log = Logger.getLogger("SDKClientUtil KLO");

	public SDKClientUtil() {

	}

	private CustomizedQuery createCustomizedQuery(
		int ID,
		String queryExpression,
		String codingSchemeName,
		String codingSchemeVersion,
		Date lastModified) {
		CustomizedQuery customizedQuery = new CustomizedQuery();
		customizedQuery.setId(ID);
		customizedQuery.setQueryExpression(queryExpression);
		customizedQuery.setCodingSchemeName(codingSchemeName);
		customizedQuery.setCodingSchemeVersion(codingSchemeVersion);
		customizedQuery.setLastModified(lastModified);
		return customizedQuery;
	}

	public void insertCustomizedQuery(
		int ID,
		String queryExpression,
		String codingSchemeName,
		String codingSchemeVersion,
		Date lastModified) throws Exception {

		CustomizedQuery customizedQuery = createCustomizedQuery(
			ID,
			queryExpression,
			codingSchemeName,
			codingSchemeVersion,
			lastModified);
		insertCustomizedQuery(customizedQuery);
	}

	public void updateCustomizedQuery(
		int ID,
		String queryExpression,
		String codingSchemeName,
		String codingSchemeVersion,
		Date lastModified) throws Exception {

		CustomizedQuery customizedQuery = createCustomizedQuery(
			ID,
			queryExpression,
			codingSchemeName,
			codingSchemeVersion,
			lastModified);
		updateCustomizedQuery(customizedQuery);
	}

	public void deleteCustomizedQuery(
		int ID,
		String queryExpression,
		String codingSchemeName,
		String codingSchemeVersion,
		Date lastModified) throws Exception {

		CustomizedQuery customizedQuery = createCustomizedQuery(
			ID,
			queryExpression,
			codingSchemeName,
			codingSchemeVersion,
			lastModified);
		deleteCustomizedQuery(customizedQuery);
	}

	public void insertCustomizedQuery(CustomizedQuery customizedQuery) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(customizedQuery);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//customizedQuery = (CustomizedQuery)queryResult.getObjectResult();
	}


	public void updateCustomizedQuery(CustomizedQuery customizedQuery) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(customizedQuery);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//customizedQuery = (CustomizedQuery)queryResult.getObjectResult();
	}


	public void deleteCustomizedQuery(CustomizedQuery customizedQuery) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(customizedQuery);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private CustomizedReport createCustomizedReport(int reportID) {
		CustomizedReport customizedReport = new CustomizedReport();
		customizedReport.setId(reportID);

		return customizedReport;
	}

	public void insertCustomizedReport(int reportID) throws Exception {
		CustomizedReport customizedReport = createCustomizedReport(reportID);
		insertCustomizedReport(customizedReport);
	}

	public void updateCustomizedReport(int reportID) throws Exception {
		CustomizedReport customizedReport = createCustomizedReport(reportID);
		updateCustomizedReport(customizedReport);
	}

	public void deleteCustomizedReport(int reportID) throws Exception {
		CustomizedReport customizedReport = createCustomizedReport(reportID);
		deleteCustomizedReport(customizedReport);
	}

	public void insertCustomizedReport(CustomizedReport customizedReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(customizedReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//customizedReport = (CustomizedReport)queryResult.getObjectResult();
	}


	public void updateCustomizedReport(CustomizedReport customizedReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(customizedReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//customizedReport = (CustomizedReport)queryResult.getObjectResult();
	}


	public void deleteCustomizedReport(CustomizedReport customizedReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(customizedReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private Report createReport(
		int ID,
		String label,
		Date lastModified,
		String pathName) {
		Report report = new Report();
		report.setId(ID);
		report.setLabel(label);
		report.setLastModified(lastModified);
		report.setPathName(pathName);
		return report;
	}

	public void insertReport(
		int ID,
		String label,
		Date lastModified,
		String pathName) throws Exception {

		Report report = createReport(
			ID,
			label,
			lastModified,
			pathName);
		insertReport(report);
	}

	public void updateReport(
		int ID,
		String label,
		Date lastModified,
		String pathName) throws Exception {

		Report report = createReport(
			ID,
			label,
			lastModified,
			pathName);
		updateReport(report);
	}

	public void deleteReport(
		int ID,
		String label,
		Date lastModified,
		String pathName) throws Exception {

		Report report = createReport(
			ID,
			label,
			lastModified,
			pathName);
		deleteReport(report);
	}

	public void insertReport(Report report) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(report);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//report = (Report)queryResult.getObjectResult();
	}


	public void updateReport(Report report) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(report);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//report = (Report)queryResult.getObjectResult();
	}


	public void deleteReport(Report report) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(report);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	public ReportColumn createReportColumn(
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter,
		int conditionalColumnId) {

		ReportColumn reportColumn = new ReportColumn();
		reportColumn.setLabel(label);
		reportColumn.setFieldId(fieldId);
		reportColumn.setPropertyType(propertyType);
		reportColumn.setPropertyName(propertyName);
		reportColumn.setIsPreferred(isPreferred);
		reportColumn.setRepresentationalForm(representationalForm);
		reportColumn.setSource(source);
		reportColumn.setQualifierName(qualifierName);
		reportColumn.setQualifierValue(qualifierValue);
		reportColumn.setDelimiter(delimiter);
		reportColumn.setConditionalColumnId(conditionalColumnId);

		return reportColumn;
	}

	public void insertReportColumn(
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter,
		int conditionalColumnId) throws Exception {

		ReportColumn reportColumn = createReportColumn(
			label,
			fieldId,
			propertyType,
			propertyName,
			isPreferred,
			representationalForm,
			source,
			qualifierName,
			qualifierValue,
			delimiter,
			conditionalColumnId);

		//System.out.println("created report column instance... ");
		insertReportColumn(reportColumn);
	}

	public void updateReportColumn(
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter,
		int conditionalColumn) throws Exception {

		ReportColumn reportColumn = createReportColumn(
			label,
			fieldId,
			propertyType,
			propertyName,
			isPreferred,
			representationalForm,
			source,
			qualifierName,
			qualifierValue,
			delimiter,
			conditionalColumn);
		updateReportColumn(reportColumn);
	}

	public void deleteReportColumn(
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter,
		int conditionalColumn) throws Exception {

		ReportColumn reportColumn = createReportColumn(
			label,
			fieldId,
			propertyType,
			propertyName,
			isPreferred,
			representationalForm,
			source,
			qualifierName,
			qualifierValue,
			delimiter,
			conditionalColumn);
		deleteReportColumn(reportColumn);
	}

	public void insertReportColumn(ReportColumn reportColumn) throws Exception {

		//System.out.println("creating writeable app service... ");
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		//System.out.println("creating query... ");
		InsertExampleQuery query = new InsertExampleQuery(reportColumn);
		//System.out.println("obtaining query results... ");
		SDKQueryResult queryResult = appService.executeQuery(query);
		//System.out.println("DONE inserting a report column... ");
		//reportColumn = (ReportColumn)queryResult.getObjectResult();
	}


	public void updateReportColumn(ReportColumn reportColumn) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(reportColumn);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportColumn = (ReportColumn)queryResult.getObjectResult();
	}


	public void deleteReportColumn(ReportColumn reportColumn) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(reportColumn);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private ReportFormat createReportFormat(
		int ID,
		String description) {
		ReportFormat reportFormat = new ReportFormat();
		reportFormat.setId(ID);
		reportFormat.setDescription(description);
		return reportFormat;
	}

	public void insertReportFormat(
		int ID,
		String description) throws Exception {

		ReportFormat reportFormat = createReportFormat(
			ID,
			description);
		insertReportFormat(reportFormat);
	}

	public void updateReportFormat(
		int ID,
		String description) throws Exception {

		ReportFormat reportFormat = createReportFormat(
			ID,
			description);
		updateReportFormat(reportFormat);
	}

	public void deleteReportFormat(
		int ID,
		String description) throws Exception {

		ReportFormat reportFormat = createReportFormat(
			ID,
			description);
		deleteReportFormat(reportFormat);
	}

	public void insertReportFormat(ReportFormat reportFormat) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(reportFormat);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportFormat = (ReportFormat)queryResult.getObjectResult();
	}


	public void updateReportFormat(ReportFormat reportFormat) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(reportFormat);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportFormat = (ReportFormat)queryResult.getObjectResult();
	}


	public void deleteReportFormat(ReportFormat reportFormat) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(reportFormat);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private ReportStatus createReportStatus(
		String label,
		String description,
		boolean active) {
		ReportStatus reportStatus = new ReportStatus();
		reportStatus.setLabel(label);
		reportStatus.setDescription(description);
		reportStatus.setActive(active);
		return reportStatus;
	}

	public void insertReportStatus(
		String label,
		String description,
		boolean active) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			label,
			description,
			active);
		insertReportStatus(reportStatus);
	}

	public void updateReportStatus(
		String label,
		String description,
		boolean active) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			label,
			description,
			active);
		updateReportStatus(reportStatus);
	}

	public void deleteReportStatus(
		String label,
		String description,
		boolean active) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			label,
			description,
			active);
		deleteReportStatus(reportStatus);
	}

	public void insertReportStatus(ReportStatus reportStatus) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(reportStatus);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportStatus = (ReportStatus)queryResult.getObjectResult();
	}


	public void updateReportStatus(ReportStatus reportStatus) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(reportStatus);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportStatus = (ReportStatus)queryResult.getObjectResult();
	}


	public void deleteReportStatus(ReportStatus reportStatus) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(reportStatus);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private StandardReport createStandardReport(int reportID) {
		StandardReport standardReport = new StandardReport();
		standardReport.setId(reportID);

		return standardReport;
	}

	public void insertStandardReport(int reportID) throws Exception {
		StandardReport standardReport = createStandardReport(reportID);
		insertStandardReport(standardReport);
	}

	public void updateStandardReport(int reportID) throws Exception {
		StandardReport standardReport = createStandardReport(reportID);
		updateStandardReport(standardReport);
	}

	public void deleteStandardReport(int reportID) throws Exception {
		StandardReport standardReport = createStandardReport(reportID);
		deleteStandardReport(standardReport);
	}

	public void insertStandardReport(StandardReport standardReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(standardReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//standardReport = (StandardReport)queryResult.getObjectResult();
	}


	public void updateStandardReport(StandardReport standardReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(standardReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//standardReport = (StandardReport)queryResult.getObjectResult();
	}


	public void deleteStandardReport(StandardReport standardReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(standardReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private StandardReportTemplate createStandardReportTemplate(
		String label,
		String codingSchemeName,
		String codingSchemeVersion,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) {

		//System.out.println("************** SDCLIENT: In create method ********************");
		StandardReportTemplate standardReportTemplate = new StandardReportTemplate();
		//System.out.println("************** SDCLIENT: StandardReportTemplate ID is ********************"+ standardReportTemplate.getId());
		standardReportTemplate.setLabel(label);
		standardReportTemplate.setCodingSchemeName(codingSchemeName);
		standardReportTemplate.setCodingSchemeVersion(codingSchemeVersion);
		standardReportTemplate.setRootConceptCode(rootConceptCode);
		standardReportTemplate.setAssociationName(associationName);
		standardReportTemplate.setDirection(direction);
		standardReportTemplate.setLevel(level);
		standardReportTemplate.setDelimiter(delimiter);
		return standardReportTemplate;
	}

	public void insertStandardReportTemplate(
		String label,
		String codingSchemeName,
		String codingSchemeVersion,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) throws Exception {

		//System.out.println("************** SDCLIENT: In insert method ********************");
		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			label,
			codingSchemeName,
			codingSchemeVersion,
			rootConceptCode,
			associationName,
			direction,
			level,
			delimiter);
		System.out.println("************** SDCLIENT: Created template instance ********************");
		insertStandardReportTemplate(standardReportTemplate);
	}

	public void updateStandardReportTemplate(
		String label,
		String codingSchemeName,
		String codingSchemeVersion,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			label,
			codingSchemeName,
			codingSchemeVersion,
			rootConceptCode,
			associationName,
			direction,
			level,
			delimiter);
		updateStandardReportTemplate(standardReportTemplate);
	}

	public void deleteStandardReportTemplate(
		String label,
		String codingSchemeName,
		String codingSchemeVersion,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			label,
			codingSchemeName,
			codingSchemeVersion,
			rootConceptCode,
			associationName,
			direction,
			level,
			delimiter);
		deleteStandardReportTemplate(standardReportTemplate);
	}

	public void insertStandardReportTemplate(StandardReportTemplate standardReportTemplate) throws Exception {
		//System.out.println("************** SDCLIENT: creating writeable app service ********************");
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		//System.out.println("************** SDCLIENT: creating query ********************");
		InsertExampleQuery query = new InsertExampleQuery(standardReportTemplate);
		//System.out.println("************** SDCLIENT: obtaining query result ********************");
		SDKQueryResult queryResult = appService.executeQuery(query);
		//System.out.println("************** SDCLIENT: DONE... ********************");
		//standardReportTemplate = (StandardReportTemplate)queryResult.getObjectResult();
	}


	public void updateStandardReportTemplate(StandardReportTemplate standardReportTemplate) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(standardReportTemplate);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//standardReportTemplate = (StandardReportTemplate)queryResult.getObjectResult();
	}


	public void deleteStandardReportTemplate(StandardReportTemplate standardReportTemplate) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(standardReportTemplate);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}

/*
	public void testGetTemplateCollection() throws Exception {

		System.out.println("************** SDCLIENT testGetTemplateCollection: creating writeable app service ********************");
		Collection<StandardReportTemplate> tc = null;
		StandardReportService srs = new StandardReportService();
		srs.setId(1001);
		srs.setServiceURL("test");

		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		System.out.println("************** SDCLIENT testGetTemplateCollection: creating query ********************");
		SearchExampleQuery query = new SearchExampleQuery(srs);
		System.out.println("************** SDCLIENT testGetTemplateCollection: obtaining query result ********************");
		SDKQueryResult queryResult = appService.executeQuery(query);

		Class klass = StandardReportService.class;
		Object o = (Object) srs;

		try {

			Collection results = appService.search(klass, o);
			for(Object obj : results)
			{
				printObject(obj, klass);
				tc = ((StandardReportService) obj).getTemplateCollection();
				for(StandardReportTemplate t : tc) {
					System.out.println("Template ID: " + t.getId());
				}

				break;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("************** SDCLIENT: DONE... ********************");

	}
*/

	private void printObject(Object obj, Class klass) throws Exception {
		System.out.println("Printing "+ klass.getName());
		Method[] methods = klass.getMethods();
		for(Method method:methods)
		{
			if(method.getName().startsWith("get") && !method.getName().equals("getClass"))
			{
				System.out.print("\t"+method.getName().substring(3)+":");
				Object val = method.invoke(obj, (Object[])null);
				if(val instanceof java.util.Set)
					System.out.println("size="+((Collection)val).size());
				else
					System.out.println(val);
			}
		}
	}


    //FQName: gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate
    //String methodName = "setLabel";
    //Key: "FDA Report";

	public Object search(String FQName, String methodName, String key) {
		try {
			Class klass = Class.forName(FQName);
			Object o = klass.newInstance();
			Method[] methods = klass.getMethods();
			Object[] params = new Object[1];
			params[0] = key;
			for(Method method:methods) {
				if(method.getName().equals(methodName))
				{
					method.invoke(o, params);
					break;
				}
			}
			ApplicationService appService = ApplicationServiceProvider.getApplicationService();
			Collection results = appService.search(klass, o);

			if (results == null) return null;
			Object[] a = results.toArray();
			return a[0];
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public Object search(String FQName, String methodName, int key) {
		try {
			Class klass = Class.forName(FQName);
			Object o = klass.newInstance();
			Method[] methods = klass.getMethods();
			Object[] params = new Object[1];
			params[0] = key;
			for(Method method:methods) {
				if(method.getName().equals(methodName))
				{
					method.invoke(o, params);
					break;
				}
			}
			ApplicationService appService = ApplicationServiceProvider.getApplicationService();
			Collection results = appService.search(klass, o);

			if (results == null) return null;
			Object[] a = results.toArray();
			return a[0];
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public Object[] search(String FQName) {
		try {
			Class klass = Class.forName(FQName);
			Object o = klass.newInstance();
			ApplicationService appService = ApplicationServiceProvider.getApplicationService();
			Collection results = appService.search(klass, o);
			if (results == null) return null;
			Object[] a = results.toArray();
			return a;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}

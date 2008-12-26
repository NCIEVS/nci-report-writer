package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.service.*;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.DeleteExampleQuery;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.WritableApplicationService;

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


	private ReportColumn createReportColumn(
		int ID,
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
		reportColumn.setId(ID);
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
		int ID,
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
			ID,
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
		insertReportColumn(reportColumn);
	}

	public void updateReportColumn(
		int ID,
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
			ID,
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
		int ID,
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
			ID,
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
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(reportColumn);
		SDKQueryResult queryResult = appService.executeQuery(query);
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
		int ID,
		String label,
		String description,
		boolean active) {
		ReportStatus reportStatus = new ReportStatus();
		reportStatus.setId(ID);
		reportStatus.setLabel(label);
		reportStatus.setDescription(description);
		reportStatus.setActive(active);
		return reportStatus;
	}

	public void insertReportStatus(
		int ID,
		String label,
		String description,
		boolean active) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			ID,
			label,
			description,
			active);
		insertReportStatus(reportStatus);
	}

	public void updateReportStatus(
		int ID,
		String label,
		String description,
		boolean active) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			ID,
			label,
			description,
			active);
		updateReportStatus(reportStatus);
	}

	public void deleteReportStatus(
		int ID,
		String label,
		String description,
		boolean active) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			ID,
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


	private StandardReportService createStandardReportService(
		int ID,
		String serviceURL) {
		StandardReportService standardReportService = new StandardReportService();
		standardReportService.setId(ID);
		standardReportService.setServiceURL(serviceURL);
		return standardReportService;
	}

	public void insertStandardReportService(
		int ID,
		String serviceURL) throws Exception {

		StandardReportService standardReportService = createStandardReportService(
			ID,
			serviceURL);
		insertStandardReportService(standardReportService);
	}

	public void updateStandardReportService(
		int ID,
		String serviceURL) throws Exception {

		StandardReportService standardReportService = createStandardReportService(
			ID,
			serviceURL);
		updateStandardReportService(standardReportService);
	}

	public void deleteStandardReportService(
		int ID,
		String serviceURL) throws Exception {

		StandardReportService standardReportService = createStandardReportService(
			ID,
			serviceURL);
		deleteStandardReportService(standardReportService);
	}

	public void insertStandardReportService(StandardReportService standardReportService) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(standardReportService);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//standardReportService = (StandardReportService)queryResult.getObjectResult();
	}


	public void updateStandardReportService(StandardReportService standardReportService) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(standardReportService);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//standardReportService = (StandardReportService)queryResult.getObjectResult();
	}


	public void deleteStandardReportService(StandardReportService standardReportService) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(standardReportService);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private StandardReportTemplate createStandardReportTemplate(
		int ID,
		String codingSchemeName,
		String codingSchemeVersion,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) {
		StandardReportTemplate standardReportTemplate = new StandardReportTemplate();
		standardReportTemplate.setCodingSchemeName(codingSchemeName);
		standardReportTemplate.setCodingSchemeVersion(codingSchemeVersion);
		standardReportTemplate.setLabel(label);
		standardReportTemplate.setRootConceptCode(rootConceptCode);
		standardReportTemplate.setAssociationName(associationName);
		standardReportTemplate.setDirection(direction);
		standardReportTemplate.setLevel(level);
		standardReportTemplate.setDelimiter(delimiter);
		return standardReportTemplate;
	}

	public void insertStandardReportTemplate(
		int ID,
		String codingSchemeName,
		String codingSchemeVersion,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			ID,
			codingSchemeName,
			codingSchemeVersion,
			label,
			rootConceptCode,
			associationName,
			direction,
			level,
			delimiter);
		insertStandardReportTemplate(standardReportTemplate);
	}

	public void updateStandardReportTemplate(
		int ID,
		String codingSchemeName,
		String codingSchemeVersion,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			ID,
			codingSchemeName,
			codingSchemeVersion,
			label,
			rootConceptCode,
			associationName,
			direction,
			level,
			delimiter);
		updateStandardReportTemplate(standardReportTemplate);
	}

	public void deleteStandardReportTemplate(
		int ID,
		String codingSchemeName,
		String codingSchemeVersion,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		char delimiter) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			ID,
			codingSchemeName,
			codingSchemeVersion,
			label,
			rootConceptCode,
			associationName,
			direction,
			level,
			delimiter);
		deleteStandardReportTemplate(standardReportTemplate);
	}

	public void insertStandardReportTemplate(StandardReportTemplate standardReportTemplate) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(standardReportTemplate);
		SDKQueryResult queryResult = appService.executeQuery(query);
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



}

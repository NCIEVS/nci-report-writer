package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.bean.*;
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

	private ReportUser createReportUser(
		String userID,
		String suffix,
		String firstName,
		String middleInitial,
		String lastName,
		String title,
		String organization,
		String phone,
		String email,
		int roleID,
		String pwd,
		Date passwordLastModified,
		int statusID,
		Date statusLastModified,
		String statusModifiedBy) {

		ReportUser reportUser = new ReportUser();
		reportUser.setUserID(userID);
		reportUser.setSuffix(suffix);
		reportUser.setFirstName(firstName);
		reportUser.setMiddleInitial(middleInitial);
		reportUser.setLastName(lastName);
		reportUser.setTitle(title);
		reportUser.setOrganization(organization);
		reportUser.setPhone(phone);
		reportUser.setEmail(email);
		reportUser.setRoleID(roleID);
		reportUser.setPwd(pwd);
		reportUser.setPasswordLastModified(passwordLastModified);
		reportUser.setStatusID(statusID);
		reportUser.setStatusLastModified(statusLastModified);
		reportUser.setStatusModifiedBy(statusModifiedBy);
		return reportUser;
	}

	public void insertReportUser(
		String userID,
		String suffix,
		String firstName,
		String middleInitial,
		String lastName,
		String title,
		String organization,
		String phone,
		String email,
		int roleID,
		String pwd,
		Date passwordLastModified,
		int statusID,
		Date statusLastModified,
		String statusModifiedBy) throws Exception {

		ReportUser reportUser = createReportUser(
			userID,
			suffix,
			firstName,
			middleInitial,
			lastName,
			title,
			organization,
			phone,
			email,
			roleID,
			pwd,
			passwordLastModified,
			statusID,
			statusLastModified,
			statusModifiedBy);
		insertReportUser(reportUser);
	}

	public void updateReportUser(
		String userID,
		String suffix,
		String firstName,
		String middleInitial,
		String lastName,
		String title,
		String organization,
		String phone,
		String email,
		int roleID,
		String pwd,
		Date passwordLastModified,
		int statusID,
		Date statusLastModified,
		String statusModifiedBy) throws Exception {

		ReportUser reportUser = createReportUser(
			userID,
			suffix,
			firstName,
			middleInitial,
			lastName,
			title,
			organization,
			phone,
			email,
			roleID,
			pwd,
			passwordLastModified,
			statusID,
			statusLastModified,
			statusModifiedBy);
		updateReportUser(reportUser);
	}

	public void deleteReportUser(
		String userID,
		String suffix,
		String firstName,
		String middleInitial,
		String lastName,
		String title,
		String organization,
		String phone,
		String email,
		int roleID,
		String pwd,
		Date passwordLastModified,
		int statusID,
		Date statusLastModified,
		String statusModifiedBy) throws Exception {

		ReportUser reportUser = createReportUser(
			userID,
			suffix,
			firstName,
			middleInitial,
			lastName,
			title,
			organization,
			phone,
			email,
			roleID,
			pwd,
			passwordLastModified,
			statusID,
			statusLastModified,
			statusModifiedBy);
		deleteReportUser(reportUser);
	}

	public void insertReportUser(ReportUser reportUser) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(reportUser);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportUser = (ReportUser)queryResult.getObjectResult();
	}


	public void updateReportUser(ReportUser reportUser) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(reportUser);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//reportUser = (ReportUser)queryResult.getObjectResult();
	}


	public void deleteReportUser(ReportUser reportUser) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(reportUser);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private UserRole createUserRole(
		int roleID,
		String roleDescription) {

		UserRole userRole = new UserRole();
		userRole.setRoleID(roleID);
		userRole.setRoleDescription(roleDescription);
		return userRole;
	}

	public void insertUserRole(
		int roleID,
		String roleDescription) throws Exception {

		UserRole userRole = createUserRole(
			roleID,
			roleDescription);
		insertUserRole(userRole);
	}

	public void updateUserRole(
		int roleID,
		String roleDescription) throws Exception {

		UserRole userRole = createUserRole(
			roleID,
			roleDescription);
		updateUserRole(userRole);
	}

	public void deleteUserRole(
		int roleID,
		String roleDescription) throws Exception {

		UserRole userRole = createUserRole(
			roleID,
			roleDescription);
		deleteUserRole(userRole);
	}

	public void insertUserRole(UserRole userRole) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(userRole);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//userRole = (UserRole)queryResult.getObjectResult();
	}


	public void updateUserRole(UserRole userRole) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(userRole);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//userRole = (UserRole)queryResult.getObjectResult();
	}


	public void deleteUserRole(UserRole userRole) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(userRole);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private UserAccountStatus createUserAccountStatus(
		int statusID,
		String statusDescription) {

		UserAccountStatus userAccountStatus = new UserAccountStatus();
		userAccountStatus.setStatusID(statusID);
		userAccountStatus.setStatusDescription(statusDescription);
		return userAccountStatus;
	}

	public void insertUserAccountStatus(
		int statusID,
		String statusDescription) throws Exception {

		UserAccountStatus userAccountStatus = createUserAccountStatus(
			statusID,
			statusDescription);
		insertUserAccountStatus(userAccountStatus);
	}

	public void updateUserAccountStatus(
		int statusID,
		String statusDescription) throws Exception {

		UserAccountStatus userAccountStatus = createUserAccountStatus(
			statusID,
			statusDescription);
		updateUserAccountStatus(userAccountStatus);
	}

	public void deleteUserAccountStatus(
		int statusID,
		String statusDescription) throws Exception {

		UserAccountStatus userAccountStatus = createUserAccountStatus(
			statusID,
			statusDescription);
		deleteUserAccountStatus(userAccountStatus);
	}

	public void insertUserAccountStatus(UserAccountStatus userAccountStatus) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(userAccountStatus);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//userAccountStatus = (UserAccountStatus)queryResult.getObjectResult();
	}


	public void updateUserAccountStatus(UserAccountStatus userAccountStatus) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(userAccountStatus);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//userAccountStatus = (UserAccountStatus)queryResult.getObjectResult();
	}


	public void deleteUserAccountStatus(UserAccountStatus userAccountStatus) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(userAccountStatus);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private SupportedStandardReport createSupportedStandardReport(
		int reportID,
		String reportLabel) {

		SupportedStandardReport supportedStandardReport = new SupportedStandardReport();
		supportedStandardReport.setReportID(reportID);
		supportedStandardReport.setReportLabel(reportLabel);
		return supportedStandardReport;
	}

	public void insertSupportedStandardReport(
		int reportID,
		String reportLabel) throws Exception {

		SupportedStandardReport supportedStandardReport = createSupportedStandardReport(
			reportID,
			reportLabel);
		insertSupportedStandardReport(supportedStandardReport);
	}

	public void updateSupportedStandardReport(
		int reportID,
		String reportLabel) throws Exception {

		SupportedStandardReport supportedStandardReport = createSupportedStandardReport(
			reportID,
			reportLabel);
		updateSupportedStandardReport(supportedStandardReport);
	}

	public void deleteSupportedStandardReport(
		int reportID,
		String reportLabel) throws Exception {

		SupportedStandardReport supportedStandardReport = createSupportedStandardReport(
			reportID,
			reportLabel);
		deleteSupportedStandardReport(supportedStandardReport);
	}

	public void insertSupportedStandardReport(SupportedStandardReport supportedStandardReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(supportedStandardReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//supportedStandardReport = (SupportedStandardReport)queryResult.getObjectResult();
	}


	public void updateSupportedStandardReport(SupportedStandardReport supportedStandardReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(supportedStandardReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//supportedStandardReport = (SupportedStandardReport)queryResult.getObjectResult();
	}


	public void deleteSupportedStandardReport(SupportedStandardReport supportedStandardReport) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(supportedStandardReport);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private ReportStatus createReportStatus(
		int ID,
		String label) {

		ReportStatus reportStatus = new ReportStatus();
		reportStatus.setID(ID);
		reportStatus.setLabel(label);
		return reportStatus;
	}

	public void insertReportStatus(
		int ID,
		String label) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			ID,
			label);
		insertReportStatus(reportStatus);
	}

	public void updateReportStatus(
		int ID,
		String label) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			ID,
			label);
		updateReportStatus(reportStatus);
	}

	public void deleteReportStatus(
		int ID,
		String label) throws Exception {

		ReportStatus reportStatus = createReportStatus(
			ID,
			label);
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


	private Report createReport(
		int ID,
		String label,
		int statusID,
		String formatName,
		String URL,
		Date lastModified,
		String modifiedBy) {

		Report report = new Report();
		report.setID(ID);
		report.setLabel(label);
		report.setStatusID(statusID);
		report.setFormatName(formatName);
		report.setURL(URL);
		report.setLastModified(lastModified);
		report.setModifiedBy(modifiedBy);
		return report;
	}

	public void insertReport(
		int ID,
		String label,
		int statusID,
		String formatName,
		String URL,
		Date lastModified,
		String modifiedBy) throws Exception {

		Report report = createReport(
			ID,
			label,
			statusID,
			formatName,
			URL,
			lastModified,
			modifiedBy);
		insertReport(report);
	}

	public void updateReport(
		int ID,
		String label,
		int statusID,
		String formatName,
		String URL,
		Date lastModified,
		String modifiedBy) throws Exception {

		Report report = createReport(
			ID,
			label,
			statusID,
			formatName,
			URL,
			lastModified,
			modifiedBy);
		updateReport(report);
	}

	public void deleteReport(
		int ID,
		String label,
		int statusID,
		String formatName,
		String URL,
		Date lastModified,
		String modifiedBy) throws Exception {

		Report report = createReport(
			ID,
			label,
			statusID,
			formatName,
			URL,
			lastModified,
			modifiedBy);
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
		int id,
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter) {

		ReportColumn reportColumn = new ReportColumn();
		reportColumn.setId(id);
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
		return reportColumn;
	}

	public void insertReportColumn(
		int id,
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter) throws Exception {

		ReportColumn reportColumn = createReportColumn(
			id,
			label,
			fieldId,
			propertyType,
			propertyName,
			isPreferred,
			representationalForm,
			source,
			qualifierName,
			qualifierValue,
			delimiter);
		insertReportColumn(reportColumn);
	}

	public void updateReportColumn(
		int id,
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter) throws Exception {

		ReportColumn reportColumn = createReportColumn(
			id,
			label,
			fieldId,
			propertyType,
			propertyName,
			isPreferred,
			representationalForm,
			source,
			qualifierName,
			qualifierValue,
			delimiter);
		updateReportColumn(reportColumn);
	}

	public void deleteReportColumn(
		int id,
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		char delimiter) throws Exception {

		ReportColumn reportColumn = createReportColumn(
			id,
			label,
			fieldId,
			propertyType,
			propertyName,
			isPreferred,
			representationalForm,
			source,
			qualifierName,
			qualifierValue,
			delimiter);
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


	private StandardReportTemplate createStandardReportTemplate(
		int id,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		java.util.ArrayList<ReportColumn> reportColumnCollection) {

		StandardReportTemplate standardReportTemplate = new StandardReportTemplate();
		standardReportTemplate.setId(id);
		standardReportTemplate.setLabel(label);
		standardReportTemplate.setRootConceptCode(rootConceptCode);
		standardReportTemplate.setAssociationName(associationName);
		standardReportTemplate.setDirection(direction);
		standardReportTemplate.setLevel(level);
		standardReportTemplate.setReportColumnCollection(reportColumnCollection);
		return standardReportTemplate;
	}

	public void insertStandardReportTemplate(
		int id,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		java.util.ArrayList<ReportColumn> reportColumnCollection) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			id,
			label,
			rootConceptCode,
			associationName,
			direction,
			level,
			reportColumnCollection);
		insertStandardReportTemplate(standardReportTemplate);
	}

	public void updateStandardReportTemplate(
		int id,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		java.util.ArrayList<ReportColumn> reportColumnCollection) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			id,
			label,
			rootConceptCode,
			associationName,
			direction,
			level,
			reportColumnCollection);
		updateStandardReportTemplate(standardReportTemplate);
	}

	public void deleteStandardReportTemplate(
		int id,
		String label,
		String rootConceptCode,
		String associationName,
		boolean direction,
		int level,
		java.util.ArrayList<ReportColumn> reportColumnCollection) throws Exception {

		StandardReportTemplate standardReportTemplate = createStandardReportTemplate(
			id,
			label,
			rootConceptCode,
			associationName,
			direction,
			level,
			reportColumnCollection);
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


	private SupportedFormat createSupportedFormat(
		String name,
		String description) {

		SupportedFormat supportedFormat = new SupportedFormat();
		supportedFormat.setName(name);
		supportedFormat.setDescription(description);
		return supportedFormat;
	}

	public void insertSupportedFormat(
		String name,
		String description) throws Exception {

		SupportedFormat supportedFormat = createSupportedFormat(
			name,
			description);
		insertSupportedFormat(supportedFormat);
	}

	public void updateSupportedFormat(
		String name,
		String description) throws Exception {

		SupportedFormat supportedFormat = createSupportedFormat(
			name,
			description);
		updateSupportedFormat(supportedFormat);
	}

	public void deleteSupportedFormat(
		String name,
		String description) throws Exception {

		SupportedFormat supportedFormat = createSupportedFormat(
			name,
			description);
		deleteSupportedFormat(supportedFormat);
	}

	public void insertSupportedFormat(SupportedFormat supportedFormat) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(supportedFormat);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//supportedFormat = (SupportedFormat)queryResult.getObjectResult();
	}


	public void updateSupportedFormat(SupportedFormat supportedFormat) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(supportedFormat);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//supportedFormat = (SupportedFormat)queryResult.getObjectResult();
	}


	public void deleteSupportedFormat(SupportedFormat supportedFormat) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(supportedFormat);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}


	private SupportedCodingScheme createSupportedCodingScheme(
		int ID,
		String name,
		String version) {

		SupportedCodingScheme supportedCodingScheme = new SupportedCodingScheme();
		supportedCodingScheme.setID(ID);
		supportedCodingScheme.setName(name);
		supportedCodingScheme.setVersion(version);
		return supportedCodingScheme;
	}

	public void insertSupportedCodingScheme(
		int ID,
		String name,
		String version) throws Exception {

		SupportedCodingScheme supportedCodingScheme = createSupportedCodingScheme(
			ID,
			name,
			version);
		insertSupportedCodingScheme(supportedCodingScheme);
	}

	public void updateSupportedCodingScheme(
		int ID,
		String name,
		String version) throws Exception {

		SupportedCodingScheme supportedCodingScheme = createSupportedCodingScheme(
			ID,
			name,
			version);
		updateSupportedCodingScheme(supportedCodingScheme);
	}

	public void deleteSupportedCodingScheme(
		int ID,
		String name,
		String version) throws Exception {

		SupportedCodingScheme supportedCodingScheme = createSupportedCodingScheme(
			ID,
			name,
			version);
		deleteSupportedCodingScheme(supportedCodingScheme);
	}

	public void insertSupportedCodingScheme(SupportedCodingScheme supportedCodingScheme) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(supportedCodingScheme);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//supportedCodingScheme = (SupportedCodingScheme)queryResult.getObjectResult();
	}


	public void updateSupportedCodingScheme(SupportedCodingScheme supportedCodingScheme) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(supportedCodingScheme);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//supportedCodingScheme = (SupportedCodingScheme)queryResult.getObjectResult();
	}


	public void deleteSupportedCodingScheme(SupportedCodingScheme supportedCodingScheme) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(supportedCodingScheme);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}

    // To be deleted. No need to have a RootConcept table.
	private RootConcept createRootConcept(
		int reportID,
		int codingSchemeId,
		String conceptCode,
		String conceptName,
		String constributingSource) {

		RootConcept rootConcept = new RootConcept();
		rootConcept.setReportID(reportID);
		rootConcept.setCodingSchemeId(codingSchemeId);
		rootConcept.setConceptCode(conceptCode);
		rootConcept.setConceptName(conceptName);
		rootConcept.setConstributingSource(constributingSource);
		return rootConcept;
	}

	public void insertRootConcept(
		int reportID,
		int codingSchemeId,
		String conceptCode,
		String conceptName,
		String constributingSource) throws Exception {

		RootConcept rootConcept = createRootConcept(
			reportID,
			codingSchemeId,
			conceptCode,
			conceptName,
			constributingSource);
		insertRootConcept(rootConcept);
	}

	public void updateRootConcept(
		int reportID,
		int codingSchemeId,
		String conceptCode,
		String conceptName,
		String constributingSource) throws Exception {

		RootConcept rootConcept = createRootConcept(
			reportID,
			codingSchemeId,
			conceptCode,
			conceptName,
			constributingSource);
		updateRootConcept(rootConcept);
	}

	public void deleteRootConcept(
		int reportID,
		int codingSchemeId,
		String conceptCode,
		String conceptName,
		String constributingSource) throws Exception {

		RootConcept rootConcept = createRootConcept(
			reportID,
			codingSchemeId,
			conceptCode,
			conceptName,
			constributingSource);
		deleteRootConcept(rootConcept);
	}

	public void insertRootConcept(RootConcept rootConcept) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		InsertExampleQuery query = new InsertExampleQuery(rootConcept);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//rootConcept = (RootConcept)queryResult.getObjectResult();
	}


	public void updateRootConcept(RootConcept rootConcept) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		UpdateExampleQuery query = new UpdateExampleQuery(rootConcept);
		SDKQueryResult queryResult = appService.executeQuery(query);
		//rootConcept = (RootConcept)queryResult.getObjectResult();
	}


	public void deleteRootConcept(RootConcept rootConcept) throws Exception {
		WritableApplicationService appService = (WritableApplicationService)ApplicationServiceProvider.getApplicationService();
		DeleteExampleQuery query = new DeleteExampleQuery(rootConcept);
		SDKQueryResult queryResult = appService.executeQuery(query);
	}



}

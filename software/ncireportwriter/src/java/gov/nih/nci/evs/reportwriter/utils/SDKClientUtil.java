/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.system.applicationservice.*;
import gov.nih.nci.system.client.*;
import gov.nih.nci.system.query.example.*;

import java.lang.reflect.*;
import java.util.*;

import org.apache.log4j.*;

/**
 *
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class SDKClientUtil {
    private static Logger _logger = Logger.getLogger(SDKClientUtil.class);

    private CustomizedQuery createCustomizedQuery(int ID,
            String queryExpression, String codingSchemeName,
            String codingSchemeVersion, Date lastModified) {
        CustomizedQuery customizedQuery = new CustomizedQuery();
        customizedQuery.setId(ID);
        customizedQuery.setQueryExpression(queryExpression);
        customizedQuery.setCodingSchemeName(codingSchemeName);
        customizedQuery.setCodingSchemeVersion(codingSchemeVersion);
        customizedQuery.setLastModified(lastModified);
        return customizedQuery;
    }

    public void insertCustomizedQuery(int ID, String queryExpression,
            String codingSchemeName, String codingSchemeVersion,
            Date lastModified) throws Exception {
        CustomizedQuery customizedQuery =
            createCustomizedQuery(ID, queryExpression, codingSchemeName,
                    codingSchemeVersion, lastModified);
        insertCustomizedQuery(customizedQuery);
    }

    public void updateCustomizedQuery(int ID, String queryExpression,
            String codingSchemeName, String codingSchemeVersion,
            Date lastModified) throws Exception {
        CustomizedQuery customizedQuery =
            createCustomizedQuery(ID, queryExpression, codingSchemeName,
                    codingSchemeVersion, lastModified);
        updateCustomizedQuery(customizedQuery);
    }

    public void deleteCustomizedQuery(int ID, String queryExpression,
            String codingSchemeName, String codingSchemeVersion,
            Date lastModified) throws Exception {
        CustomizedQuery customizedQuery =
            createCustomizedQuery(ID, queryExpression, codingSchemeName,
                    codingSchemeVersion, lastModified);
        deleteCustomizedQuery(customizedQuery);
    }

    public void insertCustomizedQuery(CustomizedQuery customizedQuery)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(customizedQuery);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // customizedQuery = (CustomizedQuery)queryResult.getObjectResult();
    }

    public void updateCustomizedQuery(CustomizedQuery customizedQuery)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(customizedQuery);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // customizedQuery = (CustomizedQuery)queryResult.getObjectResult();
    }

    public void deleteCustomizedQuery(CustomizedQuery customizedQuery)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(customizedQuery);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
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

    public void insertCustomizedReport(CustomizedReport customizedReport)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(customizedReport);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // customizedReport = (CustomizedReport)queryResult.getObjectResult();
    }

    public void updateCustomizedReport(CustomizedReport customizedReport)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(customizedReport);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // customizedReport = (CustomizedReport)queryResult.getObjectResult();
    }

    public void deleteCustomizedReport(CustomizedReport customizedReport)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(customizedReport);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

//    private Report createReport(
//        int ID,
//        String label,
//        Date lastModified,
//        String pathName) {
//        Report report = new Report();
//        report.setId(ID);
//        report.setLabel(label);
//        report.setLastModified(lastModified);
//        report.setPathName(pathName);
//        return report;
//    }
//
//    public void insertReport(
//        int ID,
//        String label,
//        Date lastModified,
//        String pathName) throws Exception {
//        Report report = createReport(
//            ID,
//            label,
//            lastModified,
//            pathName);
//        insertReport(report);
//    }
//
//    public void updateReport(
//        int ID,
//        String label,
//        Date lastModified,
//        String pathName) throws Exception {
//        Report report = createReport(
//            ID,
//            label,
//            lastModified,
//            pathName);
//        updateReport(report);
//    }
//
//    public void deleteReport(
//        int ID,
//        String label,
//        Date lastModified,
//        String pathName) throws Exception {
//        Report report = createReport(
//            ID,
//            label,
//            lastModified,
//            pathName);
//        deleteReport(report);
//    }
//
//    public void insertReport(Report report) throws Exception {
//        WritableApplicationService appService = (WritableApplicationService)
//            ApplicationServiceProvider.getApplicationService();
//        InsertExampleQuery query = new InsertExampleQuery(report);
//        SDKQueryResult queryResult = appService.executeQuery(query);
//        //report = (Report)queryResult.getObjectResult();
//    }
//
//    public void updateReport(Report report) throws Exception {
//        WritableApplicationService appService = (WritableApplicationService)
//            ApplicationServiceProvider.getApplicationService();
//        UpdateExampleQuery query = new UpdateExampleQuery(report);
//        SDKQueryResult queryResult = appService.executeQuery(query);
//        //report = (Report)queryResult.getObjectResult();
//    }
//
//    public void deleteReport(Report report) throws Exception {
//        WritableApplicationService appService = (WritableApplicationService)
//            ApplicationServiceProvider.getApplicationService();
//        DeleteExampleQuery query = new DeleteExampleQuery(report);
//        SDKQueryResult queryResult = appService.executeQuery(query);
//    }
//
//    public ReportColumn createReportColumn(
//        String label,
//        String fieldId,
//        String propertyType,
//        String propertyName,
//        Boolean isPreferred,
//        String representationalForm,
//        String source,
//        String qualifierName,
//        String qualifierValue,
//        char delimiter,
//        int conditionalColumnIdId) {
//        ReportColumn reportColumn = new ReportColumn();
//        reportColumn.setLabel(label);
//        reportColumn.setFieldId(fieldId);
//        reportColumn.setPropertyType(propertyType);
//        reportColumn.setPropertyName(propertyName);
//        reportColumn.setIsPreferred(isPreferred);
//        reportColumn.setRepresentationalForm(representationalForm);
//        reportColumn.setSource(source);
//        reportColumn.setQualifierName(qualifierName);
//        reportColumn.setQualifierValue(qualifierValue);
//        reportColumn.setDelimiter(delimiter);
//        reportColumn.setConditionalColumnIdId(conditionalColumnIdId);
//        return reportColumn;
//    }
//
//    public void insertReportColumn(
//        String label,
//        String fieldId,
//        String propertyType,
//        String propertyName,
//        Boolean isPreferred,
//        String representationalForm,
//        String source,
//        String qualifierName,
//        String qualifierValue,
//        char delimiter,
//        int conditionalColumnIdId) throws Exception {
//        ReportColumn reportColumn = createReportColumn(
//            label,
//            fieldId,
//            propertyType,
//            propertyName,
//            isPreferred,
//            representationalForm,
//            source,
//            qualifierName,
//            qualifierValue,
//            delimiter,
//            conditionalColumnIdId);
//        //_logger.debug("Created report column instance... ");
//        insertReportColumn(reportColumn);
//    }
//
//    public void updateReportColumn(
//        String label,
//        String fieldId,
//        String propertyType,
//        String propertyName,
//        Boolean isPreferred,
//        String representationalForm,
//        String source,
//        String qualifierName,
//        String qualifierValue,
//        char delimiter,
//        int conditionalColumnId) throws Exception {
//        ReportColumn reportColumn = createReportColumn(
//            label,
//            fieldId,
//            propertyType,
//            propertyName,
//            isPreferred,
//            representationalForm,
//            source,
//            qualifierName,
//            qualifierValue,
//            delimiter,
//            conditionalColumnId);
//        updateReportColumn(reportColumn);
//    }
//
//    public void deleteReportColumn(
//        String label,
//        String fieldId,
//        String propertyType,
//        String propertyName,
//        Boolean isPreferred,
//        String representationalForm,
//        String source,
//        String qualifierName,
//        String qualifierValue,
//        char delimiter,
//        int conditionalColumnId) throws Exception {
//        ReportColumn reportColumn = createReportColumn(
//            label,
//            fieldId,
//            propertyType,
//            propertyName,
//            isPreferred,
//            representationalForm,
//            source,
//            qualifierName,
//            qualifierValue,
//            delimiter,
//            conditionalColumnId);
//        deleteReportColumn(reportColumn);
//    }
//
//    public void insertReportColumn(ReportColumn reportColumn) throws Exception {
//        //_logger.debug("Creating writeable app service... ");
//        WritableApplicationService appService = (WritableApplicationService)
//            ApplicationServiceProvider.getApplicationService();
//        //_logger.debug("Creating query... ");
//        InsertExampleQuery query = new InsertExampleQuery(reportColumn);
//        //_logger.debug("Obtaining query results... ");
//        SDKQueryResult queryResult = appService.executeQuery(query);
//        //_logger.debug("DONE inserting a report column... ");
//        //reportColumn = (ReportColumn)queryResult.getObjectResult();
//    }
//
//    public void updateReportColumn(ReportColumn reportColumn) throws Exception {
//        WritableApplicationService appService = (WritableApplicationService)
//            ApplicationServiceProvider.getApplicationService();
//        UpdateExampleQuery query = new UpdateExampleQuery(reportColumn);
//        SDKQueryResult queryResult = appService.executeQuery(query);
//        //reportColumn = (ReportColumn)queryResult.getObjectResult();
//    }
//
//    public void deleteReportColumn(ReportColumn reportColumn) throws Exception {
//        WritableApplicationService appService = (WritableApplicationService)
//            ApplicationServiceProvider.getApplicationService();
//        DeleteExampleQuery query = new DeleteExampleQuery(reportColumn);
//        SDKQueryResult queryResult = appService.executeQuery(query);
//    }

    public ReportColumn createReportColumn(String label, int columnNumber,
            String fieldId, String propertyType, String propertyName,
            Boolean isPreferred, String representationalForm, String source,
            String qualifierName, String qualifierValue, char delimiter,
            int conditionalColumnId) {
        ReportColumn reportColumn = new ReportColumn();
        reportColumn.setLabel(label);
        reportColumn.setColumnNumber(columnNumber);
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

    public void insertReportColumn(String label, int columnNumber,
            String fieldId, String propertyType, String propertyName,
            Boolean isPreferred, String representationalForm, String source,
            String qualifierName, String qualifierValue, char delimiter,
            int conditionalColumnId) throws Exception {

        ReportColumn reportColumn =
            createReportColumn(label, columnNumber, fieldId, propertyType,
                    propertyName, isPreferred, representationalForm, source,
                    qualifierName, qualifierValue, delimiter,
                    conditionalColumnId);
        insertReportColumn(reportColumn);
    }

    public void updateReportColumn(String label, int columnNumber,
            String fieldId, String propertyType, String propertyName,
            Boolean isPreferred, String representationalForm, String source,
            String qualifierName, String qualifierValue, char delimiter,
            int conditionalColumnId) throws Exception {

        ReportColumn reportColumn =
            createReportColumn(label, columnNumber, fieldId, propertyType,
                    propertyName, isPreferred, representationalForm, source,
                    qualifierName, qualifierValue, delimiter,
                    conditionalColumnId);
        updateReportColumn(reportColumn);
    }

    public void deleteReportColumn(String label, int columnNumber,
            String fieldId, String propertyType, String propertyName,
            Boolean isPreferred, String representationalForm, String source,
            String qualifierName, String qualifierValue, char delimiter,
            int conditionalColumnId) throws Exception {

        ReportColumn reportColumn =
            createReportColumn(label, columnNumber, fieldId, propertyType,
                    propertyName, isPreferred, representationalForm, source,
                    qualifierName, qualifierValue, delimiter,
                    conditionalColumnId);
        deleteReportColumn(reportColumn);
    }

    public void insertReportColumn(ReportColumn reportColumn) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(reportColumn);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // reportColumn = (ReportColumn)queryResult.getObjectResult();
    }

    public void updateReportColumn(ReportColumn reportColumn) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(reportColumn);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // reportColumn = (ReportColumn)queryResult.getObjectResult();
    }

    public void deleteReportColumn(ReportColumn reportColumn) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(reportColumn);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

    private ReportFormat createReportFormat(String description) {
        ReportFormat reportFormat = new ReportFormat();
        reportFormat.setDescription(description);
        return reportFormat;
    }

    public void insertReportFormat(String description) throws Exception {
        ReportFormat reportFormat = createReportFormat(description);
        insertReportFormat(reportFormat);
    }

    public void updateReportFormat(String description) throws Exception {
        ReportFormat reportFormat = createReportFormat(description);
        updateReportFormat(reportFormat);
    }

    public void deleteReportFormat(String description) throws Exception {
        ReportFormat reportFormat = createReportFormat(description);
        deleteReportFormat(reportFormat);
    }

    public void insertReportFormat(ReportFormat reportFormat) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(reportFormat);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // reportFormat = (ReportFormat)queryResult.getObjectResult();
    }

    public void updateReportFormat(ReportFormat reportFormat) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(reportFormat);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // reportFormat = (ReportFormat)queryResult.getObjectResult();
    }

    public void deleteReportFormat(ReportFormat reportFormat) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(reportFormat);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

    private ReportStatus createReportStatus(String label, String description,
            boolean active) {
        ReportStatus reportStatus = new ReportStatus();
        reportStatus.setLabel(label);
        reportStatus.setDescription(description);
        reportStatus.setActive(active);
        return reportStatus;
    }

    public void insertReportStatus(String label, String description,
            boolean active) throws Exception {
        ReportStatus reportStatus =
            createReportStatus(label, description, active);
        insertReportStatus(reportStatus);
    }

    public void updateReportStatus(String label, String description,
            boolean active) throws Exception {
        ReportStatus reportStatus =
            createReportStatus(label, description, active);
        updateReportStatus(reportStatus);
    }

    public void deleteReportStatus(String label, String description,
            boolean active) throws Exception {
        ReportStatus reportStatus =
            createReportStatus(label, description, active);
        deleteReportStatus(reportStatus);
    }

    public void insertReportStatus(ReportStatus reportStatus) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(reportStatus);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // reportStatus = (ReportStatus)queryResult.getObjectResult();
    }

    public void updateReportStatus(ReportStatus reportStatus) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(reportStatus);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // reportStatus = (ReportStatus)queryResult.getObjectResult();
    }

    public void deleteReportStatus(ReportStatus reportStatus) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(reportStatus);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

    // =========================================================================
    // StandardReport extends Report
    // =========================================================================
    public StandardReport createStandardReport(String label, Date lastModified,
            String pathName) {
        StandardReport standardReport = new StandardReport();
        standardReport.setLabel(label);
        standardReport.setLastModified(lastModified);
        standardReport.setPathName(pathName);
        return standardReport;
    }

    public void insertStandardReport(String label, Date lastModified,
            String pathName) throws Exception {
        StandardReport standardReport =
            createStandardReport(label, lastModified, pathName);
        insertStandardReport(standardReport);
    }

    public void updateStandardReport(String label, Date lastModified,
            String pathName) throws Exception {
        StandardReport standardReport =
            createStandardReport(label, lastModified, pathName);
        updateStandardReport(standardReport);
    }

    public void deleteStandardReport(String label, Date lastModified,
            String pathName) throws Exception {
        StandardReport standardReport =
            createStandardReport(label, lastModified, pathName);
        deleteStandardReport(standardReport);
    }

    public void insertStandardReport(StandardReport standardReport)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(standardReport);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // standardReport = (StandardReport)queryResult.getObjectResult();
    }

    public void updateStandardReport(StandardReport standardReport)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(standardReport);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // standardReport = (StandardReport)queryResult.getObjectResult();
    }

    public void deleteStandardReport(StandardReport standardReport)
            throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(standardReport);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

    // =========================================================================
    private StandardReportTemplate createStandardReportTemplate(String label,
            String codingSchemeName, String codingSchemeVersion,
            String rootConceptCode, String associationName, Boolean direction,
            int level, char delimiter) {

        StandardReportTemplate standardReportTemplate =
            new StandardReportTemplate();
        _logger.debug("Create Method: StandardReportTemplate ID: "
                + standardReportTemplate.getId());
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

    public void insertStandardReportTemplate(String label,
            String codingSchemeName, String codingSchemeVersion,
            String rootConceptCode, String associationName, Boolean direction,
            int level, char delimiter) throws Exception {
        StandardReportTemplate standardReportTemplate =
            createStandardReportTemplate(label, codingSchemeName,
                    codingSchemeVersion, rootConceptCode, associationName,
                    direction, level, delimiter);
        insertStandardReportTemplate(standardReportTemplate);
    }

    public void updateStandardReportTemplate(String label,
            String codingSchemeName, String codingSchemeVersion,
            String rootConceptCode, String associationName, Boolean direction,
            int level, char delimiter) throws Exception {
        StandardReportTemplate standardReportTemplate =
            createStandardReportTemplate(label, codingSchemeName,
                    codingSchemeVersion, rootConceptCode, associationName,
                    direction, level, delimiter);
        updateStandardReportTemplate(standardReportTemplate);
    }

    public void deleteStandardReportTemplate(String label,
            String codingSchemeName, String codingSchemeVersion,
            String rootConceptCode, String associationName, Boolean direction,
            int level, char delimiter) throws Exception {
        StandardReportTemplate standardReportTemplate =
            createStandardReportTemplate(label, codingSchemeName,
                    codingSchemeVersion, rootConceptCode, associationName,
                    direction, level, delimiter);
        deleteStandardReportTemplate(standardReportTemplate);
    }

    public void insertStandardReportTemplate(
            StandardReportTemplate standardReportTemplate) throws Exception {
         _logger.debug("Creating writeable app service");
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        _logger.debug("Creating query");
        InsertExampleQuery query =
            new InsertExampleQuery(standardReportTemplate);
        _logger.debug("Obtaining query result");
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        _logger.debug("DONE");
        // standardReportTemplate =
        // (StandardReportTemplate)queryResult.getObjectResult();
    }

    public void updateStandardReportTemplate(
            StandardReportTemplate standardReportTemplate) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query =
            new UpdateExampleQuery(standardReportTemplate);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // standardReportTemplate =
        // (StandardReportTemplate)queryResult.getObjectResult();
    }

    public void deleteStandardReportTemplate(
            StandardReportTemplate standardReportTemplate) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query =
            new DeleteExampleQuery(standardReportTemplate);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

    public gov.nih.nci.evs.reportwriter.bean.User createUser(String loginName) {
        User user = new User();
        user.setLoginName(loginName);
        return user;
    }

    public void insertUser(String loginName) throws Exception {
        User user = createUser(loginName);
        insertUser(user);
    }

    public void updateUser(String loginName) throws Exception {
        User user = createUser(loginName);
        updateUser(user);
    }

    public void deleteUser(String loginName) throws Exception {
        User user = createUser(loginName);
        deleteUser(user);
    }

    public void insertUser(User user) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        InsertExampleQuery query = new InsertExampleQuery(user);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // user = (User)queryResult.getObjectResult();
    }

    public void updateUser(User user) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        UpdateExampleQuery query = new UpdateExampleQuery(user);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
        // user = (User)queryResult.getObjectResult();
    }

    public void deleteUser(User user) throws Exception {
        WritableApplicationService appService =
            (WritableApplicationService) ApplicationServiceProvider
                    .getApplicationService();
        DeleteExampleQuery query = new DeleteExampleQuery(user);
        /* SDKQueryResult queryResult = */ appService.executeQuery(query);
    }

//    private void printObject(Object obj, Class<?> klass) throws Exception {
//        _logger.debug("Printing " + klass.getName());
//        Method[] methods = klass.getMethods();
//        for (Method method : methods) {
//            if (method.getName().startsWith("get")
//                    && !method.getName().equals("getClass")) {
//                String msg = "\t" + method.getName().substring(3) + ":";
//                Object val = method.invoke(obj, (Object[]) null);
//                if (val instanceof java.util.Set)
//                    _logger.debug(msg + "size=" + ((Collection<?>) val).size());
//                else
//                    _logger.debug(msg + val);
//            }
//        }
//    }

    // FQName: gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate
    // String methodName = "setLabel";
    // Key: "FDA Report";

    public Object search(String FQName, String methodName, String key) {
        try {
            Class<?> klass = Class.forName(FQName);
            Object o = klass.newInstance();
            Method[] methods = klass.getMethods();
            Object[] params = new Object[1];
            params[0] = key;
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    method.invoke(o, params);
                    break;
                }
            }
            ApplicationService appService =
                ApplicationServiceProvider.getApplicationService();
            Collection<Object> results = appService.search(klass, o);

            if (results == null)
                return null;
            Object[] a = results.toArray();
            if (a.length > 0)
                return a[0];
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object search(String FQName, String methodName, int key) {
        try {
            Class<?> klass = Class.forName(FQName);
            Object o = klass.newInstance();
            Method[] methods = klass.getMethods();
            Object[] params = new Object[1];
            params[0] = key;
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    method.invoke(o, params);
                    break;
                }
            }
            ApplicationService appService =
                ApplicationServiceProvider.getApplicationService();
            Collection<Object> results = appService.search(klass, o);

            if (results == null)
                return null;
            Object[] a = results.toArray();
            if (a.length > 0) {
            	return a[0];
			}

            //return a[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object search(String FQName, String[] methodName, String[] key) {
        try {
            Class<?> klass = Class.forName(FQName);
            Object o = klass.newInstance();
            Method[] methods = klass.getMethods();
            int dim = key.length;

            for (int i = 0; i < dim; i++) {
                String method_name = methodName[i];
                String value = key[i];
                Object[] params = new Object[1];
                params[0] = value;

                for (Method method : methods) {
                    if (method.getName().equals(method_name)) {
                        method.invoke(o, params);
                        break;
                    }
                }
            }
            ApplicationService appService =
                ApplicationServiceProvider.getApplicationService();
            Collection<Object> results = appService.search(klass, o);

            if (results == null)
                return null;
            Object[] a = results.toArray();
            return a[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object[] search(String FQName) {
        try {
            Class<?> klass = Class.forName(FQName);
            Object o = klass.newInstance();
            ApplicationService appService = null;
            try {
                appService = ApplicationServiceProvider.getApplicationService();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (appService == null) {
				//System.out.println("(***) appService = null???");
				return null;
			}

            Collection<Object> results = appService.search(klass, o);
            if (results == null)
                return null;
            Object[] a = results.toArray();
            //System.out.println("Number of matches: " + a.length);
            return a;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

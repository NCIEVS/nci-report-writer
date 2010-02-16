package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.reportwriter.utils.*;

import java.util.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction 
 * with the National Cancer Institute, and so to the extent government 
 * employees are co-authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 *   1. Redistributions of source code must retain the above copyright 
 *      notice, this list of conditions and the disclaimer of Article 3, 
 *      below. Redistributions in binary form must reproduce the above 
 *      copyright notice, this list of conditions and the following 
 *      disclaimer in the documentation and/or other materials provided 
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution, 
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National 
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must 
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not 
 *      authorize the recipient to use any trademarks owned by either NCI 
 *      or NGIT 
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED 
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE 
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class ReportColumnUtil {
    // -------------------------------------------------------------------------
    private static Logger _logger = Logger.getLogger(ReportColumnUtil.class);
    private StandardReportTemplate _standardReportTemplate = null;
    private String _selectedStandardReportTemplate = "";
    private String _fieldLabel = "";
    private int _columnNumber = 0;
    private String _fieldType = "";
    private String _propertyType = "";
    private String _propertyName = "";
    private Boolean _isPreferred = null;
    private String _representationalForm = "";
    private String _source = "";
    private String _propertyQualifier = "";
    private String _qualifierValue = "";
    private char _delimiter = ' ';
    private int _conditionalColumnId = -1;

    // -------------------------------------------------------------------------
    public ReportColumnUtil(String selectedStandardReportTemplate,
        StandardReportTemplate standardReportTemplate) {
        setStandardReportTemplate(selectedStandardReportTemplate,
            standardReportTemplate);
    }

    // -------------------------------------------------------------------------
    public StandardReportTemplate getStandardReportTemplate() {
        return _standardReportTemplate;
    }

    public String getFieldLabel() {
        return _fieldLabel;
    }

    public int getColumnNumber() {
        return _columnNumber;
    }

    public String getFieldType() {
        return _fieldType;
    }

    public String getPropertyType() {
        return _propertyType;
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public Boolean isPreferred() {
        return _isPreferred;
    }

    public String getRepresentationalForm() {
        return _representationalForm;
    }

    public String getSource() {
        return _source;
    }

    public String getPropertyQualifier() {
        return _propertyQualifier;
    }

    public String getQualifierValue() {
        return _qualifierValue;
    }

    public char delimiter() {
        return _delimiter;
    }

    public int getConditionalColumnId() {
        return _conditionalColumnId;
    }

    // -------------------------------------------------------------------------
    public void setStandardReportTemplate(
        String selectedStandardReportTemplate,
        StandardReportTemplate standardReportTemplate) {
        _selectedStandardReportTemplate = selectedStandardReportTemplate;
        _standardReportTemplate = standardReportTemplate;
    }

    public boolean isValid(HttpServletRequest request, StringBuffer warningMsg)
            throws Exception {
        if (_standardReportTemplate == null) {
            warningMsg.append("Unable to retrieve report template "
                + _selectedStandardReportTemplate);
            return false;
        }

        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Method: isValid");
        _fieldLabel = HTTPUtils.getParameter(request, "fieldlabel");
        _logger.debug("* fieldlabel: " + _fieldLabel);
        if (_fieldLabel == null || _fieldLabel.length() <= 0)
            warningMsg.append("\n    * Field Label");

        String columnNumber_str =
            HTTPUtils.getParameter(request, "columnNumber");
        _logger.debug("* columnNumber: " + columnNumber_str);
        if (columnNumber_str == null || columnNumber_str.length() <= 0)
            warningMsg.append("\n    * Column Number");

        _fieldType =
            HTTPUtils.getSessionAttributeString(request, "selectedDataCategory");
        _logger.debug("* fieldType: " + _fieldType);

        _propertyType =
            HTTPUtils.getSessionAttributeString(request, "selectedPropertyType");
        _logger.debug("* propertyType: " + _propertyType);

        _propertyName =
            HTTPUtils.getSessionAttributeString(request, "selectedPropertyName");
        _logger.debug("* propertyName: " + _propertyName);

        String preferred = (String) request.getParameter("preferred");
        if (preferred != null && preferred.equalsIgnoreCase("yes"))
            _isPreferred = Boolean.TRUE;
        else if (preferred != null && preferred.equalsIgnoreCase("no"))
            _isPreferred = Boolean.FALSE;
        _logger.debug("* isPreferred: " + _isPreferred);
        request.setAttribute("isPreferred", _isPreferred);

        _representationalForm =
            HTTPUtils.getSessionAttributeString(request,
                "selectedRepresentationalForm");
        _logger.debug("* representationalForm: " + _representationalForm);

        _source = HTTPUtils.getSessionAttributeString(request, "selectedSource");
        _logger.debug("* source: " + _source);

        _propertyQualifier =
            HTTPUtils.getSessionAttributeString(request,
                "selectedPropertyQualifier");
        _logger.debug("* propertyQualifier: " + _propertyQualifier);

        _qualifierValue = HTTPUtils.getParameter(request, "qualifiervalue");
        _logger.debug("* qualifierValue: " + _qualifierValue);

        String delim =
            HTTPUtils.getSessionAttributeString(request, "selectedDelimiter");
        _logger.debug("* delim: " + delim);
        _delimiter =
            delim != null && delim.length() > 0 ? delim.charAt(0) : ' ';

        String conditionalColumnId =
            HTTPUtils.getParameter(request, "dependentfield");
        _logger.debug("* conditionalColumnId: " + conditionalColumnId);

        if (warningMsg.length() > 0) {
            warningMsg.insert(0, "Please enter the following value(s):");
            return false;
        }

        try {
            _columnNumber = Integer.parseInt(columnNumber_str);
            if (_columnNumber <= 0)
                throw new Exception("Value should be greater than 0.");
        } catch (Exception e) {
            warningMsg
                .append("\n    * Column Number (Expecting an integer value greater than 0)");
        }

        try {
            if (conditionalColumnId != null && conditionalColumnId.length() > 0) {
                Integer highest = (Integer) request.getSession().getAttribute("highestColumnNumber");
                String msg = "\n    * Dependent Field (Expecting either an integer or blank value)";
                if (highest > 0)
                    msg += "\n        * Value should be greater than 0 and less than or equal to " + highest + ".";
                try {
                    _conditionalColumnId = Integer.parseInt(conditionalColumnId);
                } catch (Exception e) {
                    throw new Exception(msg);
                }
                if (_conditionalColumnId == _columnNumber)
                    throw new Exception(
                        "\n    * Dependent Field can not be dependent on itself." +
                        "\n        * It can not have the same value as the Column Number.");
                if (_conditionalColumnId <= 0 || _conditionalColumnId > highest)
                    throw new Exception(msg);
            }
        } catch (Exception e) {
            warningMsg.append(e.getMessage());
        }

        if (warningMsg.length() > 0) {
            warningMsg.insert(0, "The following value(s) are invalid:");
            return false;
        }
        return true;
    }

    public boolean alreadyExists(StringBuffer warningMsg) {
        Collection<ReportColumn> cc =
            _standardReportTemplate.getColumnCollection();
        if (cc == null || cc.toArray().length <= 0)
            return false;

        Object[] objs = cc.toArray();
        for (int i = 0; i < objs.length; i++) {
            ReportColumn c = (ReportColumn) objs[i];
            String col_label = c.getLabel();
            if (col_label.compareToIgnoreCase(_fieldLabel) == 0) {
                warningMsg.append("This field label already exists.");
                return true;
            }
            Integer col_num = c.getColumnNumber();
            if (col_num.intValue() == _columnNumber) {
                warningMsg.append("This column number already exists.");
                return true;
            }
        }
        return false;
    }

    public ReportColumn getReportColumn() {
        Collection<ReportColumn> cc =
            _standardReportTemplate.getColumnCollection();
        if (cc == null || cc.toArray().length <= 0)
            return null;

        Object[] objs = cc.toArray();
        for (int i = 0; i < objs.length; i++) {
            ReportColumn c = (ReportColumn) objs[i];
            Integer col_num = c.getColumnNumber();
            if (col_num.intValue() == _columnNumber)
                return c;
        }
        return null;
    }

    // -------------------------------------------------------------------------
    public static void debug(ReportColumn col) {
        if (!_logger.isDebugEnabled())
            return;

        _logger.debug("");
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("ReportColumn: ");
        _logger.debug("  * Column Number: " + col.getColumnNumber());
        _logger.debug("  * Field Number (Id): " + col.getId());
        _logger.debug("  * Field Label (Label): " + col.getLabel());
        _logger.debug("  * Field Type (FieldId): " + col.getFieldId());
        _logger.debug("  * Property Type: " + col.getPropertyType());
        _logger.debug("  * Property Name: " + col.getPropertyName());
        _logger.debug("  * Is Preferred: " + col.getIsPreferred());
        _logger.debug("  * Representational Form: "
            + col.getRepresentationalForm());
        _logger.debug("  * Source: " + col.getSource());
        _logger.debug("  * Qualifier Name: " + col.getQualifierName());
        _logger.debug("  * Qualifier Value: " + col.getQualifierValue());
        _logger.debug("  * Delimiter: " + col.getDelimiter());
        _logger.debug("  * Dependency (ConditionalColumnId): "
            + col.getConditionalColumnId());
    }

    public static ReportColumn getReportColumn(int fieldNum) throws Exception {
        SDKClientUtil sdkclientutil = new SDKClientUtil();
        String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportColumn";
        String methodName = "setId";
        Object obj = sdkclientutil.search(FQName, methodName, fieldNum);
        ReportColumn reportColumn = (ReportColumn) obj;
        return reportColumn;
    }
}

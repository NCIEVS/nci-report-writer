package gov.nih.nci.evs.reportwriter.webapp;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import javax.servlet.http.*;

import org.LexGrid.codingSchemes.*;
import org.LexGrid.concepts.*;
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

public class ReportTemplateRequest {
    // -------------------------------------------------------------------------
    private static Logger _logger = Logger.getLogger(ReportTemplateRequest.class);
    private String _label = "";
    private String _codingSchemeName = "";
    private String _codingSchemeVersion = "";
    private String _rootConceptCode = "";
    private String _associationName = "";
    private String _direction_str = "";
    private Boolean _direction = null;
    private String _level_str = "";
    private Integer _level = null;
    private char _delimiter = '$';

    // -------------------------------------------------------------------------
    public String getLabel() {
        return _label;
    }

    public String getCodingSchemeName() {
        return _codingSchemeName;
    }

    public String getCodingSchemeVersion() {
        return _codingSchemeVersion;
    }

    public String getRootConceptCode() {
        return _rootConceptCode;
    }

    public String getAssociationName() {
        return _associationName;
    }

    public Boolean getDirection() {
        return _direction;
    }

    public Integer getLevel() {
        return _level;
    }

    public char getDelimiter() {
        return _delimiter;
    }

    // -------------------------------------------------------------------------
    public boolean isAddValid(HttpServletRequest request,
        StringBuffer warningMsg) {
        String codingSchemeNameAndVersion =
            HTTPUtils.getSessionAttributeString(request, "selectedOntology");
        _label = HTTPUtils.getParameter(request, "label");
        _codingSchemeName =
            DataUtils.getCodingSchemeName(codingSchemeNameAndVersion);
        _codingSchemeVersion =
            DataUtils.getCodingSchemeVersion(codingSchemeNameAndVersion);
        _rootConceptCode = HTTPUtils.getParameter(request, "rootConceptCode");
        _associationName =
            HTTPUtils.getSessionAttributeString(request, "selectedAssociation");
        _direction_str = HTTPUtils.getParameter(request, "direction");
        _level_str = HTTPUtils.getSessionAttributeString(request, "selectedLevel");

        return isValid(request, warningMsg);
    }

    // -------------------------------------------------------------------------
    public boolean isModifiedValid(HttpServletRequest request,
        StringBuffer warningMsg) {
        OntologyBean ontologyBean = BeanUtils.getOntologyBean();

        _label =
            HTTPUtils.getSessionAttributeString(request,
                "selectedStandardReportTemplate");
        _codingSchemeName = HTTPUtils.getParameter(request, "codingScheme");
        _codingSchemeVersion =
            (String) HTTPUtils.getParameter(request, "version");
        _rootConceptCode = HTTPUtils.getParameter(request, "rootConceptCode");
        _associationName = ontologyBean.getSelectedAssociation();
        _direction_str = HTTPUtils.getParameter(request, "direction");
        _level_str = ontologyBean.getSelectedLevel();

        return isValid(request, warningMsg);
    }

    private boolean isValid(HttpServletRequest request, StringBuffer warningMsg) {
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Method: isValid");

        _logger.debug("* label: " + _label);
        if (_label == null || _label.length() <= 0)
            warningMsg.append("\n    * Label");

        _logger.debug("* codingSchemeName: " + _codingSchemeName);
        if (_codingSchemeName == null || _codingSchemeName.length() <= 0)
            warningMsg.append("\n    * Coding Scheme");

        _logger.debug("* codingSchemeVersion: " + _codingSchemeVersion);
        if (_codingSchemeVersion == null || _codingSchemeVersion.length() <= 0)
            warningMsg.append("\n    * Version");

        _logger.debug("* rootConceptCode: " + _rootConceptCode);
        if (_rootConceptCode == null || _rootConceptCode.length() <= 0)
            warningMsg.append("\n    * Root Concept Code");

        _logger.debug("* associationName: " + _associationName);
        if (_associationName == null || _associationName.length() <= 0)
            warningMsg.append("\n    * Association name");

        _logger.debug("* direction_str: " + _direction_str);
        _direction = new Boolean(_direction_str.compareTo("source") != 0);
        request.setAttribute("direction", _direction);

        _logger.debug("* level_str: " + _level_str);
        if (_level_str == null || _level_str.length() <= 0)
            warningMsg.append("\n    * Level");

        if (warningMsg.length() > 0) {
            warningMsg.insert(0, "Please enter the following value(s):");
            return false;
        }

        if (!isValidCodingScheme(warningMsg, _codingSchemeName,
            _codingSchemeVersion))
            return false;

        Concept rootConcept =
            DataUtils.getConceptByCode(_codingSchemeName, _codingSchemeVersion,
                null, _rootConceptCode);
        if (rootConcept == null && !_rootConceptCode.contains("|"))
            warningMsg
                .append("\n    * Root Concept Code (check case sensitivity)");

        _level = OntologyBean.levelToInt(_level_str);
        if (_level < -1)
            warningMsg.append("\n    * Level");

        if (warningMsg.length() > 0) {
            warningMsg.insert(0, "The following value(s) are invalid:");
            return false;
        }
        return true;
    }

    // -------------------------------------------------------------------------
    public boolean isValidCodingScheme(StringBuffer warningMsg,
        String codingSchemeName, String codingSchemeVersion) {
        if (DataUtils.getCodingScheme(codingSchemeName) == null) {
            warningMsg.append(codingSchemeName + " "
                + "coding scheme is currently not loaded.");
            return false;
        }

        if (!DataUtils.isValidCodingScheme(codingSchemeName,
            codingSchemeVersion)) {
            CodingScheme cs = DataUtils.getCodingScheme(codingSchemeName);
            String version = cs != null ? cs.getRepresentsVersion() : null;
            warningMsg.append("Invalid coding scheme and version combination.");
            if (version != null) {
                warningMsg.append("\nTry version: ");
                warningMsg.append("\n    * " + version);
            }
            return false;
        }
        return true;
    }
}

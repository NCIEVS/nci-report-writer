package gov.nih.nci.evs.reportwriter.bean;

import java.util.*;

import javax.faces.context.*;
import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.properties.*;

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
 * @author EVS Team
 * @version 1.0
 * 
 *          Modification history Initial implementation kim.ong@ngc.com
 * 
 */

public class OntologyBean // extends BaseBean
{
    private static Logger logger = Logger.getLogger(OntologyBean.class);
    public static final String DEFAULT_ASSOCIATION = "Concept_In_Subset";
    public static final String LEVEL_ALL = "All";

    private static List<SelectItem> _ontologies = null;
    private String selectedOntology = null;
    private List<SelectItem> associationList = null;
    private String selectedAssociation = null;
    private String selectedDirection = null;
    private List<SelectItem> directionList = null;

    protected void init() {
        _ontologies = getOntologyList();
    }

    public void setSelectedOntology(String selectedOntology) {
        this.selectedOntology = selectedOntology;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedOntology", selectedOntology);
    }

    public String getSelectedOntology() {
        return this.selectedOntology;
    }

    public List<SelectItem> getOntologyList() {
        _ontologies = DataUtils.getOntologyList();
        if (_ontologies != null && _ontologies.size() > 0) {
            for (int i = 0; i < _ontologies.size(); i++) {
                SelectItem item = (SelectItem) _ontologies.get(i);
                String key = item.getLabel();
                if (key.indexOf("NCI Thesaurus") != -1
                    || key.indexOf("NCI_Thesaurus") != -1) {
                    selectedOntology = key;
                    break;
                }
            }
            associationList = getAssociationList();
        }
        return _ontologies;
    }

    public static void sortArray(String[] strArray) {
        String tmp;
        if (strArray.length <= 1)
            return;
        for (int i = 0; i < strArray.length; i++) {
            for (int j = i + 1; j < strArray.length; j++) {
                if (strArray[i].compareToIgnoreCase(strArray[j]) > 0) {
                    tmp = strArray[i];
                    strArray[i] = strArray[j];
                    strArray[j] = tmp;
                }
            }
        }
    }

    public String[] getSortedKeys(HashMap<String, ?> map) {
        if (map == null)
            return null;
        Set<String> keyset = map.keySet();
        String[] names = new String[keyset.size()];
        Iterator<String> it = keyset.iterator();
        int i = 0;
        while (it.hasNext()) {
            String s = it.next();
            names[i] = s;
            i++;
        }
        sortArray(names);
        return names;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Report Template Data
    // /////////////////////////////////////////////////////////////////////////

    public void ontologySelectionChanged(ValueChangeEvent vce) {
        String newValue = (String) vce.getNewValue();
        setSelectedOntology(newValue);
        associationList = getAssociationList();
    }

    public void setSelectedDirection(String selectedDirection) {
        this.selectedDirection = selectedDirection;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedDirection",
            selectedDirection);
    }

    public String getSelectedDirection() {
        return this.selectedDirection;
    }

    public List<SelectItem> getAssociationList() {

        if (selectedOntology == null) {
            _ontologies = getOntologyList();
        }

        if (associationList == null) {
            associationList = new ArrayList<SelectItem>();
            Vector<String> associationNames =
                DataUtils.getSupportedAssociationNames(selectedOntology);
            if (associationNames != null) {
                associationList.add(new SelectItem(""));
                for (int i = 0; i < associationNames.size(); i++) {
                    String name = (String) associationNames.elementAt(i);
                    associationList.add(new SelectItem(name));
                }

                if ((selectedAssociation == null || selectedAssociation
                    .length() <= 0)
                    && associationList != null && associationList.size() > 0) {
                    for (int j = 0; j < associationList.size(); j++) {
                        SelectItem item = (SelectItem) associationList.get(j);
                        if (item.getLabel().compareTo(DEFAULT_ASSOCIATION) == 0) {
                            setSelectedAssociation(item.getLabel());
                            break;
                        }

                    }
                }
            }
        }
        return associationList;
    }

    private String selectedLevel = null;
    private List<SelectItem> levelList = null;

    public List<SelectItem> getLevelList() {
        int max_level = 20; // default
        String max_level_str = null;
        try {
            max_level_str =
                ReportWriterProperties
                    .getProperty(ReportWriterProperties.MAXIMUM_LEVEL);
            max_level = 20;
            if (max_level_str != null) {
                max_level = Integer.parseInt(max_level_str);
            }
        } catch (Exception ex) {

        }

        levelList = new ArrayList<SelectItem>();
        for (int i = 0; i <= max_level; i++) {
            String t = Integer.toString(i);
            levelList.add(new SelectItem(t));
        }
        levelList.add(new SelectItem(LEVEL_ALL));
        if (selectedLevel == null || selectedLevel.length() <= 0) {
            selectedLevel = LEVEL_ALL;
        }
        return levelList;
    }

    public static int levelToInt(String value) {
        if (value.equalsIgnoreCase(LEVEL_ALL))
            return -1;
        return Integer.valueOf(value);
    }

    public void setSelectedLevel(String selectedLevel) {
        if (selectedLevel == null || selectedLevel.equals("-1"))
            this.selectedLevel = LEVEL_ALL;
        else
            this.selectedLevel = selectedLevel;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedLevel", selectedLevel);
    }

    public String getSelectedLevel() {
        return selectedLevel;
    }

    public void levelSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String level = (String) event.getNewValue(); // change to integer before
        // saving to database
        setSelectedLevel(level);
    }

    public String getSelectedAssociation() {
        return this.selectedAssociation;
    }

    public void setSelectedAssociation(String selectedAssociation) {
        this.selectedAssociation = selectedAssociation;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedAssociation",
            selectedAssociation);
    }

    public void associationSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String associationName = (String) event.getNewValue();
        setSelectedAssociation(associationName);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Report Column Data
    // /////////////////////////////////////////////////////////////////////////

    private String selectedPropertyName = null;
    private List<SelectItem> propertyNameList = null;
    private Vector<String> propertyNameListData = null;

    public List<SelectItem> getPropertyNameList() {

        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String selectedOntology =
            (String) request.getSession().getAttribute("selectedOntology");
        propertyNameListData =
            DataUtils.getPropertyNameListData(selectedOntology);
        propertyNameList = new Vector<SelectItem>();
        propertyNameList.add(new SelectItem(""));

        try {
            if (propertyNameListData != null) {
                for (int i = 0; i < propertyNameListData.size(); i++) {
                    String t = (String) propertyNameListData.elementAt(i);
                    propertyNameList.add(new SelectItem(t));
                }
                if (propertyNameList != null && propertyNameList.size() > 0) {
                    selectedPropertyName =
                        ((SelectItem) propertyNameList.get(0)).getLabel();
                    setSelectedPropertyName(selectedPropertyName);
                }
            }

        } catch (Exception ex) {
            logger
                .error("=========================== getPropertyNameList() Exception  "
                    + selectedOntology);
        }
        return propertyNameList;
    }

    public void setSelectedPropertyName(String selectedPropertyName) {
        this.selectedPropertyName = selectedPropertyName;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedPropertyName",
            selectedPropertyName);

    }

    public String getSelectedPropertyName() {
        return this.selectedPropertyName;
    }

    public void propertyNameSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedPropertyName(newValue);
    }

    private String selectedRepresentationalForm = null;
    private List<SelectItem> representationalFormList = null;
    private Vector<String> representationalFormListData = null;

    public List<SelectItem> getRepresentationalFormList() {
        if (selectedOntology == null) {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();
            selectedOntology =
                (String) request.getSession().getAttribute("selectedOntology");
        }
        representationalFormListData =
            DataUtils.getRepresentationalFormListData(selectedOntology);
        representationalFormList = new ArrayList<SelectItem>();
        if (representationalFormListData != null) {
            representationalFormList.add(new SelectItem(""));
            for (int i = 0; i < representationalFormListData.size(); i++) {
                String t = (String) representationalFormListData.elementAt(i);
                representationalFormList.add(new SelectItem(t));
            }
            if (representationalFormList != null
                && representationalFormList.size() > 0) {
                selectedRepresentationalForm =
                    ((SelectItem) representationalFormList.get(0)).getLabel();
            }
        }
        return representationalFormList;
    }

    public void setSelectedRepresentationalForm(
        String selectedRepresentationalForm) {
        this.selectedRepresentationalForm = selectedRepresentationalForm;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedRepresentationalForm",
            selectedRepresentationalForm);
    }

    public String getSelectedRepresentationalForm() {
        return this.selectedRepresentationalForm;
    }

    public void representationalFormSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedRepresentationalForm(newValue);
    }

    private String selectedDelimiter = null;
    private List<SelectItem> delimiterList = null;

    public List<SelectItem> getDelimiterList() {
        delimiterList = new ArrayList<SelectItem>();
        delimiterList.add(new SelectItem(" "));
        delimiterList.add(new SelectItem("|"));
        delimiterList.add(new SelectItem("$"));
        // delimiterList.add(new SelectItem("tab")); // used for separating
        // fields/columns
        setSelectedDelimiter("|");
        return delimiterList;
    }

    public void setSelectedDelimiter(String selectedDelimiter) {
        this.selectedDelimiter = selectedDelimiter;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedDelimiter",
            selectedDelimiter);
    }

    public String getSelectedDelimiter() {
        return this.selectedDelimiter;
    }

    public void delimiterSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedDelimiter(newValue);
    }

    private String selectedPropertyQualifier = null;
    private List<SelectItem> propertyQualifierList = null;
    private Vector<String> propertyQualifierListData = null;

    public List<SelectItem> getPropertyQualifierList() {
        if (selectedOntology == null) {
            HttpServletRequest request =
                (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();
            selectedOntology =
                (String) request.getSession().getAttribute("selectedOntology");
        }
        propertyQualifierListData =
            DataUtils.getPropertyQualifierListData(selectedOntology);
        propertyQualifierList = new ArrayList<SelectItem>();
        propertyQualifierList.add(new SelectItem(""));
        for (int i = 0; i < propertyQualifierListData.size(); i++) {
            String t = (String) propertyQualifierListData.elementAt(i);
            propertyQualifierList.add(new SelectItem(t));
        }
        if (propertyQualifierList != null && propertyQualifierList.size() > 0) {
            selectedPropertyQualifier =
                ((SelectItem) propertyQualifierList.get(0)).getLabel();
        }
        return propertyQualifierList;
    }

    public List<SelectItem> getDirectionList() {
        directionList = new ArrayList<SelectItem>();
        directionList.add(new SelectItem("source"));
        directionList.add(new SelectItem("target"));

        setSelectedDirection("source");
        return directionList;
    }

    public void setSelectedPropertyQualifier(String selectedPropertyQualifier) {
        this.selectedPropertyQualifier = selectedPropertyQualifier;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedPropertyQualifier",
            selectedPropertyQualifier);
    }

    public String getSelectedPropertyQualifier() {
        return this.selectedPropertyQualifier;
    }

    public void propertyQualifierSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedPropertyQualifier(newValue);
    }

    private String selectedDataCategory = null;
    private List<SelectItem> dataCategoryList = null;

    public List<SelectItem> getDataCategoryList() {
        dataCategoryList = new ArrayList<SelectItem>();
        dataCategoryList.add(new SelectItem("Code"));
        dataCategoryList.add(new SelectItem("Property"));
        dataCategoryList.add(new SelectItem("Property Qualifier"));

        dataCategoryList.add(new SelectItem("Associated Concept Code"));
        dataCategoryList.add(new SelectItem("Associated Concept Property"));
        dataCategoryList.add(new SelectItem(
            "Associated Concept Property Qualifier"));

        dataCategoryList.add(new SelectItem("1st Parent Code"));
        dataCategoryList.add(new SelectItem("1st Parent Property"));
        dataCategoryList.add(new SelectItem("1st Parent Property Qualifier"));

        dataCategoryList.add(new SelectItem("2nd Parent Code"));
        dataCategoryList.add(new SelectItem("2nd Parent Property"));
        dataCategoryList.add(new SelectItem("2nd Parent Property Qualifier"));

        if (dataCategoryList != null && dataCategoryList.size() > 0) {
            selectedDataCategory =
                ((SelectItem) dataCategoryList.get(0)).getLabel();
        }
        return dataCategoryList;
    }

    public void setSelectedDataCategory(String selectedDataCategory) {
        this.selectedDataCategory = selectedDataCategory;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedDataCategory",
            selectedDataCategory);
    }

    public String getSelectedDataCategory() {
        return this.selectedDataCategory;
    }

    public void dataCategorySelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedDataCategory(newValue);
    }

    private String selectedSource = null;
    private List<SelectItem> sourceList = null;
    private Vector<String> sourceListData = null;

    public List<SelectItem> getSourceList() {
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        String selectedOntology =
            (String) request.getSession().getAttribute("selectedOntology");
        if (selectedOntology == null) {
            String templateLabel =
                (String) request.getSession().getAttribute(
                    "selectedStandardReportTemplate");
            try {
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
                    String ontologyNameAndVersion =
                        standardReportTemplate.getCodingSchemeName()
                            + " (version: "
                            + standardReportTemplate.getCodingSchemeVersion()
                            + ")";
                    request.getSession().setAttribute("selectedOntology",
                        ontologyNameAndVersion);
                }
            } catch (Exception ex) {

            }
        }

        sourceListData = DataUtils.getSourceListData(selectedOntology);

        sourceList = new ArrayList<SelectItem>();
        sourceList.add(new SelectItem(" "));
        for (int i = 0; i < sourceListData.size(); i++) {
            String t = (String) sourceListData.elementAt(i);
            sourceList.add(new SelectItem(t));
        }

        if (sourceList != null && sourceList.size() > 0) {
            selectedSource = ((SelectItem) sourceList.get(0)).getLabel();
        }
        return sourceList;
    }

    public void setSelectedSource(String selectedSource) {
        this.selectedSource = selectedSource;
        HttpServletRequest request =
            (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        request.getSession().setAttribute("selectedSource", selectedSource);
    }

    public String getSelectedSource() {
        return this.selectedSource;
    }

    public void sourceSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedSource(newValue);
    }
}

/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.bean;

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class OntologyBean // extends BaseBean
{
    private static final String DEFAULT_ONTOLOGY = "NCI Thesaurus";
    public static final String DEFAULT_ASSOCIATION = "Concept_In_Subset";
    public static final String LEVEL_ALL = "All";
    private static Logger _logger = Logger.getLogger(OntologyBean.class);

    private static List<SelectItem> _ontologies = null;
    private String _selectedOntology = null;
    private List<SelectItem> _associationList = null;
    private String _selectedAssociation = null;
    private String _selectedDirection = null;
    private List<SelectItem> _directionList = null;
    
    public String getDefaultOntologyKey() {
        String version = DataUtils.getCodingSchemeVersionByName(DEFAULT_ONTOLOGY);
        if (version == null)
            return null;
        String key = DataUtils.getCSNVKey(DEFAULT_ONTOLOGY, version);
        return key;
    }
    
    public void setDefaultSelectedOntology() {
        setSelectedOntology(getDefaultOntologyKey());
    }

    public void setSelectedOntology(String selectedOntology) {
        _selectedOntology = selectedOntology;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedOntology", selectedOntology);
    }

    public String getSelectedOntology() {
        return _selectedOntology;
    }

    public List<SelectItem> getOntologyList() {
        if (_ontologies != null)
            return _ontologies;
        
        _ontologies = DataUtils.getOntologyList();
        if (_ontologies != null && _ontologies.size() > 0) {
            for (int i = 0; i < _ontologies.size(); i++) {
                SelectItem item = _ontologies.get(i);
                String key = item.getLabel();
                if (key.indexOf(DEFAULT_ONTOLOGY) != -1) {
                    _selectedOntology = key;
                    break;
                }
            }
            _associationList = getAssociationList();
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
        _associationList = getAssociationList();
        HttpServletRequest request = HTTPUtils.getRequest();
        request.setAttribute("ValueChangeEvent", vce);
    }

    public void setSelectedDirection(String selectedDirection) {
        _selectedDirection = selectedDirection;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedDirection",
            selectedDirection);
    }

    public String getSelectedDirection() {
        return _selectedDirection;
    }
    
    public List<SelectItem> getAssociationList() {
        if (_selectedOntology == null)
            _ontologies = getOntologyList();

        try {
            //DYEE: if (_associationList == null) {
            _associationList = new ArrayList<SelectItem>();
            Vector<String> associationNames =
                DataUtils.getSupportedAssociations(
                    DataUtils.AssociationType.Names, _selectedOntology);
            if (associationNames != null) {
                _associationList.add(new SelectItem(""));
                for (int i = 0; i < associationNames.size(); i++) {
                    String name = (String) associationNames.elementAt(i);
                    _associationList.add(new SelectItem(name));
                }

                if ((_selectedAssociation == null || _selectedAssociation.length() <= 0)
                    && _associationList != null && _associationList.size() > 0) {
                    for (int j = 0; j < _associationList.size(); j++) {
                        SelectItem item = (SelectItem) _associationList.get(j);
                        if (item.getLabel().compareTo(DEFAULT_ASSOCIATION) == 0) {
                            setSelectedAssociation(item.getLabel());
                            break;
                        }
                    }
                }
            }
            //DYEE: }
            return _associationList;
        } catch (Exception e) {
            _associationList = null;
            ExceptionUtils.print(_logger, e, new String[] {
                "  * In method: OntologyBean.getAssociationList",
                "  * _selectedOntology: " + _selectedOntology,
                "  * _associationList: " + _associationList,
            });
            e.printStackTrace();
            return new ArrayList<SelectItem>();
        }
    }

    private String _selectedLevel = null;
    private List<SelectItem> _levelList = null;

    public List<SelectItem> getLevelList() {
        int max_level = AppProperties.getInstance().getIntProperty(
            AppProperties.MAXIMUM_LEVEL, 20);

        _levelList = new ArrayList<SelectItem>();
        // Note: Level = 0 means generating report only contains the root concept.
        for (int i = 0; i <= max_level; i++) {
            String t = Integer.toString(i);
            _levelList.add(new SelectItem(t));
        }
        _levelList.add(new SelectItem(LEVEL_ALL));
        if (_selectedLevel == null || _selectedLevel.length() <= 0) {
            _selectedLevel = LEVEL_ALL;
        }
        return _levelList;
    }

    public static int levelToInt(String value) {
        if (value.equalsIgnoreCase(LEVEL_ALL))
            return -1;
        return Integer.valueOf(value);
    }

    public void setSelectedLevel(String selectedLevel) {
        if (selectedLevel == null || selectedLevel.equals("-1"))
            _selectedLevel = LEVEL_ALL;
        else
            _selectedLevel = selectedLevel;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedLevel", selectedLevel);
    }

    public String getSelectedLevel() {
        return _selectedLevel;
    }

    public void levelSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String level = (String) event.getNewValue(); // change to integer before
        // saving to database
        setSelectedLevel(level);
    }

    public String getSelectedAssociation() {
        return _selectedAssociation;
    }

    public void setSelectedAssociation(String selectedAssociation) {
        _selectedAssociation = selectedAssociation;
        HttpServletRequest request = HTTPUtils.getRequest();
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

    private String _selectedPropertyName = null;
    private List<SelectItem> _propertyNameList = null;
    private Vector<String> _propertyNameListData = null;

    public List<SelectItem> getPropertyNameList() {
        HttpServletRequest request = HTTPUtils.getRequest();
        String selectedOntology =
            (String) request.getSession().getAttribute("selectedOntology");
        _propertyNameListData =
            DataUtils.getPropertyNameListData(selectedOntology);
        _propertyNameList = new Vector<SelectItem>();
        _propertyNameList.add(new SelectItem(""));

        try {
            if (_propertyNameListData != null) {
                for (int i = 0; i < _propertyNameListData.size(); i++) {
                    String t = (String) _propertyNameListData.elementAt(i);
                    _propertyNameList.add(new SelectItem(t));
                }
            }
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e, new String[] { 
                "  * Method: getPropertyNameList",
                "  * selectedOntology: " +  selectedOntology,
            });
        }
        return _propertyNameList;
    }

    public void setSelectedPropertyName(String selectedPropertyName) {
        _selectedPropertyName = selectedPropertyName;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedPropertyName",
            selectedPropertyName);

    }

    public String getSelectedPropertyName() {
        return _selectedPropertyName;
    }

    public void propertyNameSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedPropertyName(newValue);
    }

    private String _selectedRepresentationalForm = null;
    private List<SelectItem> _representationalFormList = null;
    private Vector<String> _representationalFormListData = null;

    public List<SelectItem> getRepresentationalFormList() {
        if (_selectedOntology == null) {
            HttpServletRequest request = HTTPUtils.getRequest();
            _selectedOntology =
                (String) request.getSession().getAttribute("selectedOntology");
        }
        _representationalFormListData =
            DataUtils.getRepresentationalFormListData(_selectedOntology);
        _representationalFormList = new ArrayList<SelectItem>();
        if (_representationalFormListData != null) {
            _representationalFormList.add(new SelectItem(""));
            for (int i = 0; i < _representationalFormListData.size(); i++) {
                String t = (String) _representationalFormListData.elementAt(i);
                _representationalFormList.add(new SelectItem(t));
            }
        }
        return _representationalFormList;
    }

    public void setSelectedRepresentationalForm(
        String selectedRepresentationalForm) {
        _selectedRepresentationalForm = selectedRepresentationalForm;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedRepresentationalForm",
            selectedRepresentationalForm);
    }

    public String getSelectedRepresentationalForm() {
        return _selectedRepresentationalForm;
    }

    public void representationalFormSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedRepresentationalForm(newValue);
    }

    private String _selectedDelimiter = null;
    private List<SelectItem> _delimiterList = null;

    public List<SelectItem> getDelimiterList() {
        _delimiterList = new ArrayList<SelectItem>();

        //Note: " " causes problem when redisplaying the report columns.
        //  However, saving is not a problem.
        //_delimiterList.add(new SelectItem(" "));
        _delimiterList.add(new SelectItem("|"));
        _delimiterList.add(new SelectItem("$"));
        // _delimiterList.add(new SelectItem("tab"));
        return _delimiterList;
    }

    public void setSelectedDelimiter(String selectedDelimiter) {
        _selectedDelimiter = selectedDelimiter;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedDelimiter",
            selectedDelimiter);
    }

    public String getSelectedDelimiter() {
        return _selectedDelimiter;
    }

    public void delimiterSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedDelimiter(newValue);
    }

    private String _selectedPropertyQualifier = null;
    private List<SelectItem> _propertyQualifierList = null;
    private Vector<String> _propertyQualifierListData = null;

    public List<SelectItem> getPropertyQualifierList() {
        if (_selectedOntology == null) {
            HttpServletRequest request = HTTPUtils.getRequest();
            _selectedOntology =
                (String) request.getSession().getAttribute("selectedOntology");
        }
        _propertyQualifierList = new ArrayList<SelectItem>();
        _propertyQualifierList.add(new SelectItem(""));
        _propertyQualifierListData =
            DataUtils.getPropertyQualifierListData(_selectedOntology);
        if (_propertyQualifierListData != null) {
            for (int i = 0; i < _propertyQualifierListData.size(); i++) {
                String t = (String) _propertyQualifierListData.elementAt(i);
                _propertyQualifierList.add(new SelectItem(t));
            }
        }
        return _propertyQualifierList;
    }

    public List<SelectItem> getDirectionList() {
        _directionList = new ArrayList<SelectItem>();
        _directionList.add(new SelectItem("source"));
        _directionList.add(new SelectItem("target"));

        setSelectedDirection("source");
        return _directionList;
    }

    public void setSelectedPropertyQualifier(String selectedPropertyQualifier) {
        _selectedPropertyQualifier = selectedPropertyQualifier;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedPropertyQualifier",
            selectedPropertyQualifier);
    }

    public String getSelectedPropertyQualifier() {
        return _selectedPropertyQualifier;
    }

    public void propertyQualifierSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedPropertyQualifier(newValue);
    }

    private String _selectedDataCategory = null;
    private List<SelectItem> _dataCategoryList = null;

    public List<SelectItem> getDataCategoryList() {
        if (_dataCategoryList != null)
            return _dataCategoryList;
        
        _dataCategoryList = new ArrayList<SelectItem>();
        _dataCategoryList.add(new SelectItem("Code"));
        _dataCategoryList.add(new SelectItem("Property"));
        _dataCategoryList.add(new SelectItem("Property Qualifier"));

        _dataCategoryList.add(new SelectItem("Associated Concept Code"));
        _dataCategoryList.add(new SelectItem("Associated Concept Property"));
        _dataCategoryList.add(new SelectItem(
            "Associated Concept Property Qualifier"));

        _dataCategoryList.add(new SelectItem("1st Parent Code"));
        _dataCategoryList.add(new SelectItem("1st Parent Property"));
        _dataCategoryList.add(new SelectItem("1st Parent Property Qualifier"));

        _dataCategoryList.add(new SelectItem("2nd Parent Code"));
        _dataCategoryList.add(new SelectItem("2nd Parent Property"));
        _dataCategoryList.add(new SelectItem("2nd Parent Property Qualifier"));

        return _dataCategoryList;
    }

    public void setSelectedDataCategory(String selectedDataCategory) {
        _selectedDataCategory = selectedDataCategory;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedDataCategory",
            selectedDataCategory);
    }

    public String getSelectedDataCategory() {
        return _selectedDataCategory;
    }

    public void dataCategorySelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedDataCategory(newValue);
    }

    private String _selectedSource = null;
    private List<SelectItem> _sourceList = null;
    private Vector<String> _sourceListData = null;

    public List<SelectItem> getSourceList() {
        HttpServletRequest request = HTTPUtils.getRequest();
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
            } catch (Exception e) {
                ExceptionUtils.print(_logger, e);
            }
        }

        _sourceList = new ArrayList<SelectItem>();
        _sourceList.add(new SelectItem(" "));
        _sourceListData = DataUtils.getSourceListData(selectedOntology);
        if (_sourceListData != null) {
            for (int i = 0; i < _sourceListData.size(); i++) {
                String t = (String) _sourceListData.elementAt(i);
                _sourceList.add(new SelectItem(t));
            }
        }
        return _sourceList;
    }

    public void setSelectedSource(String selectedSource) {
        _selectedSource = selectedSource;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedSource", selectedSource);
    }

    public String getSelectedSource() {
        return _selectedSource;
    }

    public void sourceSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String newValue = (String) event.getNewValue();
        setSelectedSource(newValue);
    }
}

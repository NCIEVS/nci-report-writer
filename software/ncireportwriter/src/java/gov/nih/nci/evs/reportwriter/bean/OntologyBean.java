package gov.nih.nci.evs.reportwriter.bean;

import java.util.*;

import javax.faces.event.*;
import javax.faces.model.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import gov.nih.nci.evs.reportwriter.service.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.naming.SupportedAssociation;


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
    private String _selectedParentAssociation = null;
    private String _selectedDirection = null;
    private List<SelectItem> _directionList = null;

    private HashMap<String, String> _hasParentAssociationMap = null;


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


    public String getSelectedParentAssociation() {
        return _selectedParentAssociation;
    }

    public void setSelectedParentAssociation(String selectedParentAssociation) {
        _selectedParentAssociation = selectedParentAssociation;
        HttpServletRequest request = HTTPUtils.getRequest();
        request.getSession().setAttribute("selectedParentAssociation",
            selectedParentAssociation);
    }

    public void parentAssociationSelectionChanged(ValueChangeEvent event) {
        if (event.getNewValue() == null)
            return;
        String parentAssociationName = (String) event.getNewValue();
        setSelectedParentAssociation(parentAssociationName);
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
    private String _selectedDataCategoryArgs = null;
    private List<SelectItem> _dataCategoryList = null;




    public Vector<String> getSupportedAssociations(String codingSchemeName, String version)
            throws Exception {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null)
            vt.setVersion(version);

        CodingScheme scheme = null;
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);

        Vector<String> v = new Vector<String>();
        SupportedAssociation[] assos =
            scheme.getMappings().getSupportedAssociation();
        _logger.debug("");
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("List of supported associations:");
        for (int i = 0; i < assos.length; i++) {
            SupportedAssociation sa = (SupportedAssociation) assos[i];
            v.add(sa.getLocalId());
        }
        SortUtils.quickSort(v);
        return v;
    }


    public static String fieldID2AssociationName(String codingSchemeName, String fieldID) {
        if (codingSchemeName == null) {
			 codingSchemeName = DEFAULT_ONTOLOGY;
		}

		HashMap hmap = DataUtils.getHasParentAssociationMap(codingSchemeName, null);
	    if (hmap == null) return null;
	    return (String) hmap.get(fieldID);
	}


    public List<SelectItem> getDataCategoryList() {
        if (_dataCategoryList != null)
            return _dataCategoryList;

         _dataCategoryList = new ArrayList<SelectItem>();

         Vector v0 = new Vector();

         v0.add("Code");
         v0.add("Property");
         v0.add("Property Qualifier");

         v0.add("Associated Concept Code");
         v0.add("Associated Concept Property");
         v0.add("Associated Concept Property Qualifier");

/*
         v0.add("1st Associated Concept Code");
         v0.add("1st Associated Concept Property");
         v0.add("1st Associated Concept Property Qualifier");

         v0.add("2nd Associated Concept Code");
         v0.add("2nd Associated Concept Property");
         v0.add("2nd Associated Concept Property Qualifier");

         v0.add("1st Parent Code");
         v0.add("1st Parent Property");
         v0.add("1st Parent Property Qualifier");

         v0.add("2nd Parent Code");
         v0.add("2nd Parent Property");
         v0.add("2nd Parent Property Qualifier");
*/

         if (_selectedOntology == null) {
			 _selectedOntology = DEFAULT_ONTOLOGY;
		 }

         Vector<String> v = null;
         try {
         	v = getSupportedAssociations(_selectedOntology, null);
         	if (v != null) {
         	    System.out.println("getSupportedAssociations returns: " + v.size());
			}
		 } catch (Exception ex) {
			 ex.printStackTrace();
		 }

	     HashMap hmap = DataUtils.getHasParentAssociationMap(_selectedOntology, null);
	     Vector w = new Vector();
	     if (hmap != null) {
			 Iterator it = hmap.keySet().iterator();
			 while (it.hasNext()) {
				 String key = (String) it.next();
				 w.add(key);
			 }
		 }
		 w = SortUtils.quickSort(w);
		 for (int i=0; i<w.size(); i++) {
			 String s = (String) w.elementAt(i);
			 v0.add(s);
		 }

         for (int i=0; i<v0.size(); i++) {
			 String propName = (String) v0.elementAt(i);
			 System.out.println(propName);
			 _dataCategoryList.add(new SelectItem(propName));
		 }

		 String t = "CDISC Submission Value";
		 _dataCategoryList.add(new SelectItem(t));
         return _dataCategoryList;
    }

    public void setSelectedDataCategory(String selectedDataCategory) {
        HttpServletRequest request = HTTPUtils.getRequest();

        if (selectedDataCategory == null) {
            _selectedDataCategory = null;
            _selectedDataCategoryArgs = null;
        } else {
            _selectedDataCategory = SpecialCases.GetHasParent.
                getValueWithoutArgs(selectedDataCategory);
            if (_selectedDataCategoryArgs == null) {
                _selectedDataCategoryArgs = SpecialCases.GetHasParent.
                    getArgs(selectedDataCategory);
            }
        }

        request.getSession().setAttribute("selectedDataCategory",
            _selectedDataCategory);
        request.getSession().setAttribute("selectedDataCategoryArgs",
            _selectedDataCategoryArgs);
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

/*
     public Vector<String> getPropertyNameListData(
        String codingSchemeName, String version) {
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            // RemoteServerUtil rsu = new RemoteServerUtil();
            // EVSApplicationService lbSvc = rsu.createLexBIGService();
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            if (scheme == null)
                return null;
            Vector<String> propertyNameListData = new Vector<String>();
            SupportedProperty[] properties =
                scheme.getMappings().getSupportedProperty();
            for (int i = 0; i < properties.length; i++) {
                SupportedProperty property = properties[i];
                propertyNameListData.add(property.getLocalId());
            }
            return propertyNameListData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
*/
}

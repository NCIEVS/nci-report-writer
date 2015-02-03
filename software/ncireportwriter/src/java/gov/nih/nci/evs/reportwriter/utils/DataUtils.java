/*
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


import gov.nih.nci.evs.reportwriter.bean.*;

import javax.faces.model.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;
import gov.nih.nci.system.client.*;

import org.LexGrid.LexBIG.Exceptions.*;
import org.apache.log4j.*;

import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.PropertyMatchValue;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

/**
 *
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class DataUtils {
	private static String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };

    public static enum AssociationType {
        Codes, Names
    };

    private static Logger _logger = Logger.getLogger(DataUtils.class);
    private static int _maxReturn = AppProperties.getInstance().getIntProperty(
        AppProperties.MAXIMUM_RETURN, 10000);

    private static List<SelectItem> _standardReportTemplateList =
        new ArrayList<SelectItem>();
    private static List<SelectItem> _adminTaskList = null;
    private static List<SelectItem> _userTaskList = null;
    private static List<SelectItem> _propertyTypeList = null;

    private static List<SelectItem> _ontologies = null;
    private static HashMap<String, CodingScheme> _codingSchemeMap = null;
    private static HashMap<String, CSNVInfo> _csnv2InfoMap = null;


    private static HashMap<String, HashMap> _cs2HasParentAssociationMap = null;

    public static String NCIT_VERSION = null;//AppProperties.getInstance().getProperty(AppProperties.NCIT_VERSION);

    static {
        setCodingSchemeMap();
        NCIT_VERSION = getVocabularyVersionByTag("NCI_Thesaurus", "PRODUCTION");
    }

    public static List<SelectItem> getPropertyTypeList() {
        if (_propertyTypeList == null) {
            _propertyTypeList = new ArrayList<SelectItem>();
            _propertyTypeList.add(new SelectItem(""));
            _propertyTypeList.add(new SelectItem("COMMENT"));
            _propertyTypeList.add(new SelectItem("DEFINITION"));
            _propertyTypeList.add(new SelectItem("GENERIC"));
            _propertyTypeList.add(new SelectItem("INSTRUCTION"));
            _propertyTypeList.add(new SelectItem("PRESENTATION"));
        }
        return _propertyTypeList;
    }

    public static List<SelectItem> getTaskList(Boolean isAdmin) {
        if (isAdmin != null && isAdmin) {
            if (_adminTaskList == null) {
                _adminTaskList = new ArrayList<SelectItem>();
                _adminTaskList
                    .add(new SelectItem("Administer Standard Reports"));
                _adminTaskList.add(new SelectItem("Maintain Report Status"));
                _adminTaskList.add(new SelectItem("Assign Report Status"));
                _adminTaskList.add(new SelectItem("Administer Excel Metadata"));
                _adminTaskList.add(new SelectItem("Generate Hierarchy Report"));
                _adminTaskList.add(new SelectItem("Retrieve Standard Reports"));
                _adminTaskList.add(new SelectItem("Unlock User Account"));
            }
            return _adminTaskList;
        }
        if (_userTaskList == null) {
            _userTaskList = new ArrayList<SelectItem>();
            _userTaskList.add(new SelectItem("Retrieve Standard Reports"));
        }
        return _userTaskList;
    }

    public static List<SelectItem> getStandardReportTemplateList() {
        _standardReportTemplateList = new ArrayList<SelectItem>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";

            Object[] objs = util.search(FQName);
            if (objs == null || objs.length == 0) {
                return _standardReportTemplateList;
			}
            Vector<String> v = new Vector<String>();
            for (int i = 0; i < objs.length; i++) {
                StandardReportTemplate standardReportTemplate =
                    (StandardReportTemplate) objs[i];
                // standardReportTemplateList.add(new
                // SelectItem(standardReportTemplate.getLabel()));
                v.add(standardReportTemplate.getLabel());
            }
            SortUtils.quickSort(v);
            for (int i = 0; i < v.size(); i++) {
                String name = (String) v.elementAt(i);
                _standardReportTemplateList.add(new SelectItem(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _standardReportTemplateList;
    }

    public static List<SelectItem> getOntologyList() {
        return _ontologies;
    }

    public static boolean isValidCodingScheme(String codingSchemeName,
        String version) {
        String csnv = getCSNVKey(codingSchemeName, version);
        return getCodingSchemeVersion(csnv) != null;
    }

    private static void setCodingSchemeMap() {
        _ontologies = new ArrayList<SelectItem>();
        _codingSchemeMap = new HashMap<String, CodingScheme>();
        _csnv2InfoMap = new HashMap<String, CSNVInfo>();
        _cs2HasParentAssociationMap = new HashMap();

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus()
                        .equals(CodingSchemeVersionStatus.ACTIVE);
                if (isActive == null || isActive.equals(Boolean.FALSE))
                    continue;
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                String formalName = css.getFormalName();
                String representsVersion = css.getRepresentsVersion();
                CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
                vt.setVersion(representsVersion);

                CodingScheme scheme = null;
                int j = 0;
                while (true) {
                    try {
                        switch (j) {
                        case 0:
                            try {
                                scheme = lbSvc.resolveCodingScheme(formalName, vt);

                            } catch (Exception e) {
                                _logger.warn(e.getClass().getSimpleName() + ": "
                                    + e.getMessage());
                                _logger.warn("Possible security token needed for: ");
                                _logger.warn("  * " + formalName);
                            }
                            break;
                        case 1:
                            String urn = css.getCodingSchemeURI();
                            scheme = lbSvc.resolveCodingScheme(urn, vt);
                            break;
                        case 2:
                            String localname = css.getLocalName();
                            scheme = lbSvc.resolveCodingScheme(localname, vt);
                            break;
                        }
                        break;
                    } catch (Exception e) {
                        ExceptionUtils.print(_logger, e);
                        // e.printStackTrace();
                    }
                    j++;
                }

                _codingSchemeMap.put(formalName, scheme);
                String value = getCSNVKey(formalName, representsVersion);
                _ontologies.add(new SelectItem(value, value));
                CSNVInfo info = new CSNVInfo();
                info.codingSchemeName = formalName;
                info.version = representsVersion;
                _csnv2InfoMap.put(value, info);
            }
            SortUtils.quickSort(_ontologies);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERROR: setCodingSchemeMap throws exceptions.");
        }
    }

    public static String getCSNVKey(String codingSchemeName, String version) {
        String value = codingSchemeName + " (version: " + version + ")";
        return value;
    }

    public static String getCSNFromKey(String key) {
        if (key == null || key.length() <= 0)
            return "";
        int i = key.indexOf(" (version: ");
        if (i <= 0)
            return "";

        String codingSchemeName = key.substring(0, i);
        return codingSchemeName;
    }

    public static String getCodingSchemeName(String key) {
        CSNVInfo info = _csnv2InfoMap.get(key);
        return info != null ? info.codingSchemeName : null;
    }

    public static String getCodingSchemeVersion(String key) {
        CSNVInfo info = _csnv2InfoMap.get(key);
        return info != null ? info.version : null;
    }

    public static class CSNVInfo {
        public String codingSchemeName = "";
        public String version = "";
    }

    public static CSNVInfo getLatestCSNVInfo(String key) {
        CSNVInfo info = new CSNVInfo();
        info.codingSchemeName = getCSNFromKey(key);
        CodingScheme cs = getCodingScheme(info.codingSchemeName);
        if (cs == null)
            return null;

        info.version = cs.getRepresentsVersion();
        _logger.warn("Method: DataUtils.getTempCSNVInfo");
        _logger.warn(" * CSNV key not valid: " + key);
        _logger.warn(" * Instead, using: "
            + getCSNVKey(info.codingSchemeName, info.version));
        return info;
    }

    public static CodingScheme getCodingScheme(String codingSchemeName) {
        return _codingSchemeMap.get(codingSchemeName);
    }

    public static String getCodingSchemeVersionByName(String codingSchemeName) {
        CodingScheme cs = getCodingScheme(codingSchemeName);
        if (cs == null)
            return null;
        String version = cs.getRepresentsVersion();
        return version;
    }

    public static Vector<String> getSupportedAssociations(
        AssociationType associationType, String key) throws Exception {
        CSNVInfo info = _csnv2InfoMap.get(key);
        if (info == null)
            info = getLatestCSNVInfo(key);
        if (info == null)
            return null;
        return getSupportedAssociations(associationType, info.codingSchemeName,
            info.version);
    }

    public static Vector<String> getSupportedAssociations(
        AssociationType associationType, String codingSchemeName, String version)
            throws Exception {

		/* resolveCodingScheme
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null)
            vt.setVersion(version);

        CodingScheme scheme = null;
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
        */
        CodingScheme scheme = resolveCodingScheme(codingSchemeName, version);

        Vector<String> v = new Vector<String>();
        SupportedAssociation[] assos =
            scheme.getMappings().getSupportedAssociation();
        _logger.debug("");
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("List of supported associations:");
        for (int i = 0; i < assos.length; i++) {
            SupportedAssociation sa = (SupportedAssociation) assos[i];
//            _logger.debug("  " + i + ") id=" + sa.getLocalId() + ", name=" + sa.getContent());
            switch (associationType) {
            case Names:
                v.add(sa.getContent());
                break;
            default:
                v.add(sa.getLocalId());
                break;
            }
        }
        SortUtils.quickSort(v);
        return v;
    }

    public static String getAssociationCode(String codingSchemeName,
        String version, String name) throws Exception {

		/* resolveCodingScheme
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null)
            vt.setVersion(version);

        CodingScheme scheme = null;
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
        */
        CodingScheme scheme = resolveCodingScheme(codingSchemeName, version);

        SupportedAssociation[] assos =
            scheme.getMappings().getSupportedAssociation();
        for (int i = 0; i < assos.length; i++) {
            SupportedAssociation sa = (SupportedAssociation) assos[i];
            String aName = sa.getContent();
            if (aName.equals(name))
                return sa.getLocalId();
        }
        return "";
    }

    public static Vector<String> getPropertyNameListData(String key) {
        CSNVInfo info = _csnv2InfoMap.get(key);
        if (info == null)
            return null;
        return getPropertyNameListData(info.codingSchemeName, info.version);
    }

    public static Vector<String> getPropertyNameListData(
        String codingSchemeName, String version) {
		/* resolveCodingScheme
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {

            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
*/
        CodingScheme scheme = null;
        try {
            scheme = resolveCodingScheme(codingSchemeName, version);

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

    public static Vector<String> getRepresentationalFormListData(String key) {
        CSNVInfo info = _csnv2InfoMap.get(key);
        if (info == null) {
			Vector<String> v = getRepresentationalFormListData(key, null);
			return v;
		}

        return getRepresentationalFormListData(info.codingSchemeName,
            info.version);


    }

    public static Vector<String> getRepresentationalFormListData(
        String codingSchemeName, String version) {
		CodingScheme scheme = null;
		/*
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }

        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
            (*/

        try {
			scheme = resolveCodingScheme(codingSchemeName, version);
            if (scheme == null)
                return null;
            Vector<String> propertyNameListData = new Vector<String>();
            SupportedRepresentationalForm[] forms =
                scheme.getMappings().getSupportedRepresentationalForm();
            boolean debug = true;
            if (debug) {
                _logger.debug(StringUtils.SEPARATOR);
                _logger.debug("Method getRepresentationalFormListData");
                _logger.debug("* codingSchemeName: " + codingSchemeName);
                _logger.debug("* version: " + version);
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < forms.length; ++i) {
                    list.add(forms[i].getLocalId());
				}
                StringUtils.debug(false, _logger, "* forms: ", list);
            }
            if (forms != null) {
                for (int i = 0; i < forms.length; i++) {
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

    public static Vector<String> getPropertyQualifierListData(String key) {
		/*
        CSNVInfo info = _csnv2InfoMap.get(key);
        if (info == null) {
            return null;
		}
		*/


        CSNVInfo info = _csnv2InfoMap.get(key);
        if (info == null) {
            return getPropertyQualifierListData(key, null);
		}

        return getPropertyQualifierListData(info.codingSchemeName, info.version);
    }

    public static Vector<String> getPropertyQualifierListData(
        String codingSchemeName, String version) {
		CodingScheme scheme = null;
		/* resolveCodingScheme
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
        */

         try {
            scheme = resolveCodingScheme(codingSchemeName, version);

            if (scheme == null)
                return null;
            Vector<String> propertyQualifierListData = new Vector<String>();
            SupportedPropertyQualifier[] qualifiers =
                scheme.getMappings().getSupportedPropertyQualifier();
            for (int i = 0; i < qualifiers.length; i++) {
                SupportedPropertyQualifier qualifier = qualifiers[i];
                propertyQualifierListData.add(qualifier.getLocalId());
            }

            return propertyQualifierListData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Vector<String> getSourceListData(String key) {
        CSNVInfo info = _csnv2InfoMap.get(key);
        if (info == null)
            return null;
        return getSourceListData(info.codingSchemeName, info.version);
    }

    public static Vector<String> getSourceListData(String codingSchemeName,
        String version) {
		/*	resolveCodingScheme
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null) {
            vt.setVersion(version);
        }
        CodingScheme scheme = null;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
        */
        CodingScheme scheme = null;
        try {
            scheme = resolveCodingScheme(codingSchemeName, version);
            if (scheme == null)
                return null;
            Vector<String> sourceListData = new Vector<String>();

            // Insert your code here
            SupportedSource[] sources =
                scheme.getMappings().getSupportedSource();
            for (int i = 0; i < sources.length; i++) {
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
        Vector<String> reportStatusListData = new Vector<String>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportStatus";
            Object[] objs = util.search(FQName);

            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    ReportStatus reportStatus = (ReportStatus) objs[i];
                    reportStatusListData.add(reportStatus.getLabel());
                }
            } else {
                // Initial status (DRAFT and APPROVED)
                String label = "DRAFT";
                String description =
                    "Report is a draft, not ready for download.";
                boolean active = true;
                try {
                    util.insertReportStatus(label, description, active);
                } catch (Exception ex) {
                    _logger.error("*** insertReportStatus DRAFT failed.");
                }

                label = "APPROVED";
                description = "Report has been approved for download by users";
                active = true;
                try {
                    util.insertReportStatus(label, description, active);
                } catch (Exception ex) {
                    _logger.error("*** insertReportStatus APPROVED failed.");
                }

                objs = util.search(FQName);
                if (objs != null && objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        ReportStatus reportStatus = (ReportStatus) objs[i];
                        reportStatusListData.add(reportStatus.getLabel());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportStatusListData;
    }

    public static Vector<String> getReportFormatListData() {
        Vector<String> reportFormatListData = new Vector<String>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            Object[] objs = util.search(FQName);

            if (objs != null && objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    ReportFormat reportFormat = (ReportFormat) objs[i];
                    reportFormatListData.add(reportFormat.getDescription());
                }
            } else {
                String description = "Text (tab delimited)";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    _logger.debug("*** insertReportFormat " + description
                        + " failed.");
                }

                description = "Microsoft Office Excel";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    _logger.debug("*** insertReportFormat " + description
                        + " failed.");
                }

                objs = util.search(FQName);
                if (objs != null && objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        ReportFormat reportFormat = (ReportFormat) objs[i];
                        reportFormatListData.add(reportFormat.getDescription());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportFormatListData;
    }

    public static List<ReportFormat> getAvailableReportFormat() {
        List<ReportFormat> list = new ArrayList<ReportFormat>();
        try {
            SDKClientUtil util = new SDKClientUtil();
            String FQName = "gov.nih.nci.evs.reportwriter.bean.ReportFormat";
            Object[] objs = util.search(FQName);

            if (objs == null || objs.length == 0) {
                String description = "Text (tab delimited)";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    _logger.debug("*** insertReportFormat " + description
                        + " failed.");
                }

                description = "Microsoft Office Excel";
                try {
                    util.insertReportFormat(description);
                } catch (Exception ex) {
                    _logger.debug("*** insertReportFormat " + description
                        + " failed.");
                }

                objs = util.search(FQName);
                if (objs != null && objs.length > 0) {
                    for (int i = 0; i < objs.length; i++) {
                        ReportFormat reportFormat = (ReportFormat) objs[i];
                        list.add(reportFormat);
                    }
                }
            }

            else // if (objs != null && objs.length > 0)
            {
                for (int i = 0; i < objs.length; i++) {
                    ReportFormat reportFormat = (ReportFormat) objs[i];
                    list.add(reportFormat);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String int2String(Integer int_obj) {
        if (int_obj == null) {
            return null;
        }

        String retstr = Integer.toString(int_obj);
        return retstr;
    }

    // =========================================================================
    public static Entity getConceptByCode(String codingSchemeName,
        String version, String tag, String code) throws Exception {

        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        if (lbSvc == null)
            throw new Exception("lbSvc == null???");

        CodingSchemeVersionOrTag vtag = new CodingSchemeVersionOrTag();
        vtag.setVersion(version);

        ConceptReferenceList crefs =
            createConceptReferenceList(new String[] { code },
                codingSchemeName);

        CodedNodeSet cns = null;
        try {
            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, vtag);
        } catch (Exception e1) {
            _logger.debug("Method: getConceptByCode");
            _logger.debug("  * codingSchemeName: " + codingSchemeName);
            _logger.debug("  * version: " + version);
            _logger.debug("  * tag: " + tag);
            _logger.debug("  * code: " + code);
            throw new Exception(
                "lbSvc.getCodingSchemeConcepts threw exception??? " + code);
        }

        cns = cns.restrictToCodes(crefs);
        ResolvedConceptReferenceList matches =
            cns.resolveToList(null, null, null, 1);

        if (matches == null) {
            _logger.warn("Concept not found for " + code + ".");
            return null;
        }

        if (matches.getResolvedConceptReferenceCount() > 0) {
            ResolvedConceptReference ref =
                (ResolvedConceptReference) matches
                    .enumerateResolvedConceptReference().nextElement();

            Entity entry = ref.getReferencedEntry();
            return entry;
        }
        return null;
    }

    public static NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null) {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static Vector<Entity> getAssociationTargets(
        LexEVSValueSetDefinitionServices definitionServices,
        String uri, String scheme,
        String version, String code, String assocName) throws Exception {
        boolean targetToSource = false;
        return getAssociations(definitionServices, uri,
            targetToSource, scheme, version, code, assocName);
    }

    public static ResolvedConceptReferenceList getNext(
        ResolvedConceptReferencesIterator iterator) {
        return iterator.getNext();
    }

    /**
     * Dump_matches to output, for debug purposes
     *
     * @param iterator
     *            the iterator
     * @param maxToReturn
     *            the max to return
     */
    public static Vector<Entity> resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn) {
        Vector<Entity> v = new Vector<Entity>();
        if (iterator == null) {
            _logger.debug("No match.");
            return v;
        }
        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra =
                    rcrl.getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
                    Entity ce = rcr.getReferencedEntry();
                    // _logger.debug("Iteration " + iteration + " " +
                    // ce.getId() + " " +
                    // ce.getEntityDescription().getContent());
                    v.add(ce);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public static Vector<Entity> getAssociationSources(
        LexEVSValueSetDefinitionServices definitionServices,
        String uri, String scheme,
        String version, String code, String assocName) throws Exception {
        boolean targetToSource = true;
        return getAssociations(definitionServices, uri,
            targetToSource, scheme, version, code, assocName);
    }

    public static Vector<Entity> getAssociations(
        LexEVSValueSetDefinitionServices definitionServices,
        String uri, boolean targetToSource,
        String scheme, String version, String code, String assocName)
        throws Exception {

        boolean includeRoot = false;
        return resolveValueSet(definitionServices, uri, scheme, version, code,
            targetToSource, assocName, includeRoot);

        // CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        // if (version != null)
        // csvt.setVersion(version);
        // ResolvedConceptReferenceList matches = null;
        // Vector<Entity> v = new Vector<Entity>();
        // try {
        // LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        // CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
        //
        // NameAndValueList nameAndValueList =
        // createNameAndValueList(new String[] { assocCode }, null);
        //
        // NameAndValueList nameAndValueList_qualifier = null;
        // cng =
        // cng.restrictToAssociations(nameAndValueList,
        // nameAndValueList_qualifier);
        //
        // boolean resolveForward = retrieveTargets;
        // boolean resolveBackward = !retrieveTargets;
        // matches =
        // cng.resolveAsList(
        // ConvenienceMethods.createConceptReference(code, scheme),
        // resolveForward, resolveBackward, 1, 1, new LocalNameList(),
        // null, null, _maxReturn);
        //
        // if (matches.getResolvedConceptReferenceCount() > 0) {
        // Enumeration refEnum =
        // matches.enumerateResolvedConceptReference();
        //
        // while (refEnum.hasMoreElements()) {
        // ResolvedConceptReference ref =
        // (ResolvedConceptReference) refEnum.nextElement();
        //
        // AssociationList alist =
        // retrieveTargets ? ref.getSourceOf() : ref.getTargetOf();
        // if (alist == null)
        // continue;
        // Association[] associations = alist.getAssociation();
        //
        // for (int i = 0; i < associations.length; i++) {
        // Association assoc = associations[i];
        // // KLO
        // assoc = processForAnonomousNodes(assoc);
        // AssociatedConcept[] acl =
        // assoc.getAssociatedConcepts()
        // .getAssociatedConcept();
        // for (int j = 0; j < acl.length; j++) {
        // AssociatedConcept ac = acl[j];
        // v.add(ac.getReferencedEntry());
        // }
        // }
        // }
        // SortUtils.quickSort(v);
        // }
        //
        // } catch (Exception ex) {
        // ex.printStackTrace();
        // }
        // return v;
    }

    // public static Vector<Entity> getAssociationsNew(boolean retrieveTargets,
    // String scheme, String version, String code, String assocCode) {
    // _logger.info("Method: getAssociationsNew");
    // CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
    // if (version != null)
    // csvt.setVersion(version);
    // Vector<Entity> v = new Vector<Entity>();
    // try {
    // LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
    // CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
    // NameAndValueList nameAndValueList =
    // createNameAndValueList(new String[] { assocCode }, null);
    // NameAndValueList nameAndValueList_qualifier = null;
    // cng =
    // cng.restrictToAssociations(nameAndValueList,
    // nameAndValueList_qualifier);
    //
    // ConceptReference graphFocus =
    // ConvenienceMethods.createConceptReference(code, scheme);
    // boolean resolveForward = retrieveTargets;
    // boolean resolveBackward = !retrieveTargets;
    // int resolveAssociationDepth = 1;
    // int maxToReturn = -1;
    //
    // ConceptReferenceList crefs =
    // createConceptReferenceList(new String[] { code }, scheme);
    // CodedNodeSet codesToRemove =
    // lbSvc.getCodingSchemeConcepts(scheme, csvt);
    // codesToRemove = codesToRemove.restrictToCodes(crefs);
    //
    // ResolvedConceptReferencesIterator iterator =
    // codedNodeGraph2CodedNodeSetIterator(cng, graphFocus,
    // resolveForward, resolveBackward, resolveAssociationDepth,
    // maxToReturn, codesToRemove);
    //
    // v = resolveIterator(iterator, maxToReturn, null); // v =
    // resolveIteratorNew(iterator);
    // SortUtils.quickSort(v);
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // }
    // return v;
    // }

    public static Vector<String> getSubconceptCodes2(String scheme,
        String version, String code) {
        // eturned bar delimited name|code
        Vector<String> list = new Vector<String>();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        long ms = System.currentTimeMillis();


        //try {
			/*
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, csvt);
            */
        CodingScheme cs = null;
        try {
            cs = resolveCodingScheme(scheme, version);

            if (cs == null)
                return null;
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            if (hierarchies == null || hierarchies.length == 0)
                return null;
            SupportedHierarchy hierarchyDefn = hierarchies[0];
            // String hier_id = hierarchyDefn.getLocalId();
            String[] associationsToNavigate =
                hierarchyDefn.getAssociationNames();
            boolean associationsNavigatedFwd =
                hierarchyDefn.getIsForwardNavigable();
            NameAndValueList nameAndValueList =
                createNameAndValueList(associationsToNavigate, null);
            ResolvedConceptReferenceList matches = null;
            try {
				LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
                CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
                NameAndValueList nameAndValueList_qualifier = null;
                cng =
                    cng.restrictToAssociations(nameAndValueList,
                        nameAndValueList_qualifier);
                ConceptReference graphFocus =
                    ConvenienceMethods.createConceptReference(code, scheme);
                matches =
                    cng.resolveAsList(graphFocus, associationsNavigatedFwd,
                        !associationsNavigatedFwd, 1, 1, new LocalNameList(),
                        null, null, -1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Analyze the result ...
            if (matches != null
                && matches.getResolvedConceptReferenceCount() > 0) {

                ResolvedConceptReference ref =
                    (ResolvedConceptReference) matches
                        .enumerateResolvedConceptReference().nextElement();
                if (ref != null) {
                    AssociationList sourceof = ref.getSourceOf();
                    if (!associationsNavigatedFwd)
                        sourceof = ref.getTargetOf();
                    if (sourceof != null) {
                        Association[] associations = sourceof.getAssociation();
                        if (associations != null) {
                            for (int i = 0; i < associations.length; i++) {
                                Association assoc = associations[i];
                                if (assoc != null) {
                                    if (assoc.getAssociatedConcepts() != null) {
                                        AssociatedConcept[] acl =
                                            assoc.getAssociatedConcepts()
                                                .getAssociatedConcept();
                                        if (acl != null) {
                                            for (int j = 0; j < acl.length; j++) {
                                                AssociatedConcept ac = acl[j];
                                                if (ac != null
                                                    && ac.getConceptCode()
                                                        .indexOf("@") == -1) {
                                                    EntityDescription ed =
                                                        ac.getEntityDescription();
                                                    if (ed != null) {
                                                        list.add(ac
                                                            .getConceptCode());

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        _logger.debug("Run time (milliseconds) getSubconcepts: "
            + (System.currentTimeMillis() - ms) + " to resolve ");
        SortUtils.quickSort(list);
        return list;
    }

    public static ResolvedConceptReferencesIterator codedNodeGraph2CodedNodeSetIterator(
        CodedNodeGraph cng, ConceptReference graphFocus,
        boolean resolveForward, boolean resolveBackward,
        int resolveAssociationDepth, int maxToReturn, CodedNodeSet codesToRemove) {

        try {
            CodedNodeSet cns =
                cng.toNodeList(graphFocus, resolveForward, resolveBackward,
                    resolveAssociationDepth, maxToReturn);

            if (cns == null) {
                _logger.warn("cng.toNodeList returns null???");
                return null;
            }

            if (codesToRemove != null)
                cns = cns.difference(codesToRemove);

            SortOptionList sortCriteria = null;
            // Constructors.createSortOptionList(new String[]{"matchToQuery",
            // "code"});
            LocalNameList propertyNames = null;
            CodedNodeSet.PropertyType[] propertyTypes = null;

            ResolvedConceptReferencesIterator iterator =
                cns.resolve(sortCriteria, propertyNames, propertyTypes);
            if (iterator == null)
                _logger.warn("cns.resolve returns null???");
            return iterator;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Vector<Entity> resolveIterator(
        ResolvedConceptReferencesIterator iterator, int maxToReturn, String code) {
        Vector<Entity> v = new Vector<Entity>();
        if (iterator == null) {
            _logger.info("No match.");
            return v;
        }

        try {
            int iteration = 0;
            while (iterator.hasNext()) {
                iteration++;
                iterator = iterator.scroll(maxToReturn);
                ResolvedConceptReferenceList rcrl = iterator.getNext();
                ResolvedConceptReference[] rcra =
                    rcrl.getResolvedConceptReference();
                for (int i = 0; i < rcra.length; i++) {
                    ResolvedConceptReference rcr = rcra[i];
                    Entity ce = rcr.getReferencedEntry();
                    if (code == null) {
                        v.add(ce);
                    } else if (ce.getEntityCode().compareTo(code) != 0) {
                        v.add(ce);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private static final int RESOLVE_ITERATOR_MAX_RETURN = 100;

    public static Vector<Entity> resolveIteratorNew(
        ResolvedConceptReferencesIterator iterator) throws Exception {
        Vector<Entity> list = new Vector<Entity>();
        if (iterator == null)
            return list;

        int lastResolved = 0;
        while (iterator.hasNext()) {
            ResolvedConceptReference[] refs =
                iterator.next(RESOLVE_ITERATOR_MAX_RETURN)
                    .getResolvedConceptReference();
            for (ResolvedConceptReference ref : refs) {
                Entity ce = ref.getReferencedEntry();
                list.add(ce);
                ++lastResolved;
            }
            _logger.debug("resolveIterator: Advancing iterator: "
                + lastResolved);
        }
        return list;
    }

    public static Vector<String> getParentCodes(String scheme, String version,
        String code) {
        Vector<String> hierarchicalAssoName_vec =
            getHierarchyAssociationId(scheme, version);
        if (hierarchicalAssoName_vec == null
            || hierarchicalAssoName_vec.size() == 0) {
            return null;
        }
        String hierarchicalAssoName =
            (String) hierarchicalAssoName_vec.elementAt(0);
        // KLO, 01/23/2009
        // Vector<Entity> superconcept_vec = util.getAssociationSources(scheme,
        // version, code, hierarchicalAssoName);
        Vector<String> superconcept_vec =
            getAssociationSourceCodes(scheme, version, code,
                hierarchicalAssoName);
        if (superconcept_vec == null)
            return null;
        // SortUtils.quickSort(superconcept_vec, SortUtils.SORT_BY_CODE);
        return superconcept_vec;

    }

    public static Vector<String> getAssociationSourceCodes(String scheme,
        String version, String code, String assocCode) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector<String> v = new Vector<String>();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocCode }, null);

            NameAndValueList nameAndValueList_qualifier = null;
            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);

            matches =
                cng.resolveAsList(
                    ConvenienceMethods.createConceptReference(code, scheme),
                    false, true, 1, 1, new LocalNameList(), null, null,
                    _maxReturn);

            if (matches.getResolvedConceptReferenceCount() > 0) {
                /*
                 * Enumeration<ResolvedConceptReference> refEnum =
                 * matches.enumerateResolvedConceptReference();
                 *
                 * while (refEnum.hasMoreElements()) { ResolvedConceptReference
                 * ref = refEnum.nextElement();
                 */
                Enumeration refEnum =
                    matches.enumerateResolvedConceptReference();

                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref =
                        (ResolvedConceptReference) refEnum.nextElement();

                    AssociationList targetof = ref.getTargetOf();
                    if (targetof == null)
                        continue;
                    Association[] associations = targetof.getAssociation();

                    for (int i = 0; i < associations.length; i++) {
                        Association assoc = associations[i];
                        // KLO
                        assoc = processForAnonomousNodes(assoc);
                        AssociatedConcept[] acl =
                            assoc.getAssociatedConcepts()
                                .getAssociatedConcept();
                        for (int j = 0; j < acl.length; j++) {
                            AssociatedConcept ac = acl[j];
                            v.add(ac.getReferencedEntry().getEntityCode());
                        }
                    }
                }
                SortUtils.quickSort(v);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }

    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public static Vector<String> getSubconceptCodes(String scheme,
        String version, String code) { // throws LBException{
        Vector<String> v = new Vector<String>();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);

            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            try {
                lbscm.createCodeNodeSet(new String[] { code }, scheme, csvt)
                    .resolveToList(null, null, null, 1)
                    .getResolvedConceptReference(0).getEntityDescription()
                    .getContent();

            } catch (Exception e) {
                ExceptionUtils
                    .print(_logger, e, "DataUtils.getSubconceptCodes");
                // e.printStackTrace();
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations = null;
                associations = null;
                try {
                    associations =
                        lbscm.getHierarchyLevelNext(scheme, csvt, hierarchyID,
                            code, false, null);
                } catch (Exception e) {
                    ExceptionUtils.print(_logger, e,
                        "DataUtils.getSubconceptCodes");
                    _logger.error("getSubconceptCodes "
                        + "Exception lbscm.getHierarchyLevelNext");
                    return v;
                }

                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ExceptionUtils.print(_logger, ex, "DataUtils.getSubconceptCodes");
            ex.printStackTrace();
        }
        return v;
    }

    public static Vector<String> getSuperconceptCodes(String scheme,
        String version, String code) { // throws LBException{
        long ms = System.currentTimeMillis();
        Vector<String> v = new Vector<String>();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            LexBIGServiceConvenienceMethods lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(version);
            try {
                lbscm.createCodeNodeSet(new String[] { code }, scheme, csvt)
                    .resolveToList(null, null, null, 1)
                    .getResolvedConceptReference(0).getEntityDescription()
                    .getContent();
            } catch (Exception e) {
            }

            // Iterate through all hierarchies and levels ...
            String[] hierarchyIDs = lbscm.getHierarchyIDs(scheme, csvt);
            for (int k = 0; k < hierarchyIDs.length; k++) {
                String hierarchyID = hierarchyIDs[k];
                AssociationList associations =
                    lbscm.getHierarchyLevelPrev(scheme, csvt, hierarchyID,
                        code, false, null);
                for (int i = 0; i < associations.getAssociationCount(); i++) {
                    Association assoc = associations.getAssociation(i);
                    AssociatedConceptList concepts =
                        assoc.getAssociatedConcepts();
                    for (int j = 0; j < concepts.getAssociatedConceptCount(); j++) {
                        AssociatedConcept concept =
                            concepts.getAssociatedConcept(j);
                        String nextCode = concept.getConceptCode();
                        v.add(nextCode);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            _logger
                .debug("Run time (ms): " + (System.currentTimeMillis() - ms));
        }
        return v;
    }

    public static Vector<String> getHierarchyAssociationId(String scheme,
        String version) {

        Vector<String> association_vec = new Vector<String>();
        //try {
			/* resolveCodingScheme
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            // Will handle secured ontologies later.
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            */

        CodingScheme cs = null;
        try {
            cs = resolveCodingScheme(scheme, version);
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            String[] ids = hierarchies[0].getAssociationNames();

            for (int i = 0; i < ids.length; i++) {
                if (!association_vec.contains(ids[i])) {
                    association_vec.add(ids[i]);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return association_vec;
    }

    public static String getVocabularyVersionByTag(String codingSchemeName,
        String ltag) {
        if (codingSchemeName == null)
            return null;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList lcsrl = lbSvc.getSupportedCodingSchemes();
            CodingSchemeRendering[] csra = lcsrl.getCodingSchemeRendering();
            for (int i = 0; i < csra.length; i++) {
                CodingSchemeRendering csr = csra[i];
                CodingSchemeSummary css = csr.getCodingSchemeSummary();
                if (css.getFormalName().compareTo(codingSchemeName) == 0
                    || css.getLocalName().compareTo(codingSchemeName) == 0) {
                    if (ltag == null)
                        return css.getRepresentsVersion();
                    RenderingDetail rd = csr.getRenderingDetail();
                    CodingSchemeTagList cstl = rd.getVersionTags();
                    java.lang.String[] tags = cstl.getTag();
                    for (int j = 0; j < tags.length; j++) {
                        String version_tag = (String) tags[j];
                        if (version_tag.compareToIgnoreCase(ltag) == 0) {
                            return css.getRepresentsVersion();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        _logger.debug("Version corresponding to tag " + ltag + " is not found "
            + " in " + codingSchemeName);
        return null;
    }

    public static Vector<String> getVersionListData(String codingSchemeName) {

        Vector<String> v = new Vector<String>();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
            if (csrl == null)
                _logger.debug("csrl is NULL");

            CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
            for (int i = 0; i < csrs.length; i++) {
                CodingSchemeRendering csr = csrs[i];
                Boolean isActive =
                    csr.getRenderingDetail().getVersionStatus()
                        .equals(CodingSchemeVersionStatus.ACTIVE);
                if (isActive != null && isActive.equals(Boolean.TRUE)) {
                    CodingSchemeSummary css = csr.getCodingSchemeSummary();
                    String formalname = css.getFormalName();
                    if (formalname.compareTo(codingSchemeName) == 0) {
                        String representsVersion = css.getRepresentsVersion();
                        v.add(representsVersion);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
        return v;
    }

    public static String getFileName(String pathname) {
        File file = new File(pathname);
        String filename = file.getName();
        return filename;
    }

    protected static Association processForAnonomousNodes(Association assoc) {
        // clone Association except associatedConcepts
        Association temp = new Association();
        temp.setAssociatedData(assoc.getAssociatedData());
        temp.setAssociationName(assoc.getAssociationName());
        temp.setAssociationReference(assoc.getAssociationReference());
        temp.setDirectionalName(assoc.getDirectionalName());
        temp.setAssociatedConcepts(new AssociatedConceptList());

        for (int i = 0; i < assoc.getAssociatedConcepts()
            .getAssociatedConceptCount(); i++) {
            // Conditionals to deal with anonymous nodes and UMLS top nodes
            // "V-X"
            // The first three allow UMLS traversal to top node.
            // The last two are specific to owl anonymous nodes which can act
            // like false
            // top nodes.
            if (assoc.getAssociatedConcepts().getAssociatedConcept(i)
                .getReferencedEntry() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != false
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@")
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@@")) {
                // do nothing
            } else {
                temp.getAssociatedConcepts().addAssociatedConcept(
                    assoc.getAssociatedConcepts().getAssociatedConcept(i));
            }
        }
        return temp;
    }

    // ==========================================================================
    public static LocalNameList vector2LocalNameList(Vector<String> v) {
        if (v == null)
            return null;
        LocalNameList list = new LocalNameList();
        for (int i = 0; i < v.size(); i++) {
            String vEntry = (String) v.elementAt(i);
            list.addEntry(vEntry);
        }
        return list;
    }

    protected static NameAndValueList createNameAndValueList(
        Vector<String> names, Vector<String> values) {
        if (names == null)
            return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.size(); i++) {
            String name = names.elementAt(i);
            String value = values.elementAt(i);
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null) {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    public static Vector<org.LexGrid.concepts.Entity> restrictToMatchingProperty(
        String codingSchemeName, String version, Vector<String> property_vec,
        Vector<String> source_vec, Vector<String> qualifier_name_vec,
        Vector<String> qualifier_value_vec, java.lang.String matchText,
        java.lang.String matchAlgorithm, java.lang.String language,
        int maxToReturn) {

        LocalNameList propertyList = vector2LocalNameList(property_vec);
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList sourceList = vector2LocalNameList(source_vec);

        NameAndValueList qualifierList =
            createNameAndValueList(qualifier_name_vec, qualifier_value_vec);

        return restrictToMatchingProperty(codingSchemeName, version,
            propertyList, propertyTypes, sourceList, qualifierList, matchText,
            matchAlgorithm, language, maxToReturn);
    }

    public static Vector<org.LexGrid.concepts.Entity> restrictToProperty(
        String codingSchemeName, String version, Vector<String> property_vec,
        Vector<String> source_vec, Vector<String> qualifier_name_vec,
        Vector<String> qualifier_value_vec, int maxToReturn) {

        LocalNameList propertyList = vector2LocalNameList(property_vec);
        CodedNodeSet.PropertyType[] propertyTypes = null;
        LocalNameList sourceList = vector2LocalNameList(source_vec);
        NameAndValueList qualifierList =
            createNameAndValueList(qualifier_name_vec, qualifier_value_vec);
        return restrictToProperty(codingSchemeName, version, propertyList,
            propertyTypes, sourceList, qualifierList, maxToReturn);
    }

    public static Vector<org.LexGrid.concepts.Entity> restrictToProperty(
        String codingSchemeName, String version,

        LocalNameList propertyList, CodedNodeSet.PropertyType[] propertyTypes,
        LocalNameList sourceList, NameAndValueList qualifierList,
        int maxToReturn) {
        CodedNodeSet cns = null;
        Vector<org.LexGrid.concepts.Entity> v =
            new Vector<org.LexGrid.concepts.Entity>();
        try {
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);

            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
            if (cns == null)
                return v;

            LocalNameList contextList = null;
            cns =
                cns.restrictToProperties(propertyList, propertyTypes,
                    sourceList, contextList, qualifierList);

            LocalNameList restrictToProperties = new LocalNameList();
            SortOptionList sortCriteria =
                Constructors.createSortOptionList(new String[] {
                    "matchToQuery", "code" });

            ResolvedConceptReferenceList list = null;
            try {
                list =
                    cns.resolveToList(sortCriteria, restrictToProperties, null,
                        maxToReturn);
            } catch (Exception ex) {
                throw new LBParameterException(ex.getMessage());
            }

            if (list == null)
                return v;
            ResolvedConceptReference[] rcrArray =
                list.getResolvedConceptReference();
            if (rcrArray == null) {
                _logger
                    .warn("DLBWrapper getResolvedConceptReference returns null");
            }

            for (int i = 0; i < rcrArray.length; i++) {
                ResolvedConceptReference rcr =
                    (ResolvedConceptReference) rcrArray[i];
                v.add(rcr.getReferencedEntry());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return v;
        }
        return v;
    }

    // public static Vector<org.LexGrid.concepts.Entity>
    // restrictToMatchingProperty(
    // String codingSchemeName, String version, LocalNameList propertyList,
    // CodedNodeSet.PropertyType[] propertyTypes, LocalNameList sourceList,
    // NameAndValueList qualifierList, java.lang.String matchText,
    // java.lang.String matchAlgorithm, java.lang.String language,
    // int maxToReturn) {
    // CodedNodeSet cns = null;
    // Vector<org.LexGrid.concepts.Entity> v =
    // new Vector<org.LexGrid.concepts.Entity>();
    // try {
    // CodingSchemeVersionOrTag versionOrTag =
    // new CodingSchemeVersionOrTag();
    // versionOrTag.setVersion(version);
    //
    // LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
    //
    // if (lbSvc == null) {
    // _logger.error("lbSvc == null???");
    // return null;
    // }
    //
    // cns = lbSvc.getCodingSchemeConcepts(codingSchemeName, versionOrTag);
    // if (cns == null)
    // return v;
    //
    // LocalNameList contextList = null;
    // cns =
    // cns.restrictToMatchingProperties(propertyList, propertyTypes,
    // sourceList, contextList, qualifierList, matchText,
    // matchAlgorithm, language);
    //
    // LocalNameList restrictToProperties = new LocalNameList();
    // SortOptionList sortCriteria =
    // Constructors
    // .createSortOptionList(new String[] { "matchToQuery" });
    //
    // ResolvedConceptReferenceList list = null;
    // try {
    // list =
    // cns.resolveToList(sortCriteria, restrictToProperties, null,
    // maxToReturn);
    // } catch (Exception ex) {
    // throw new LBParameterException(ex.getMessage());
    // }
    //
    // if (list == null)
    // return v;
    // ResolvedConceptReference[] rcrArray =
    // list.getResolvedConceptReference();
    // if (rcrArray == null) {
    // _logger.warn("getResolvedConceptReference returns null");
    // }
    //
    // for (int i = 0; i < rcrArray.length; i++) {
    // ResolvedConceptReference rcr =
    // (ResolvedConceptReference) rcrArray[i];
    // v.add(rcr.getReferencedEntry());
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // return v;
    // }
    //
    // return toConcept(SortUtils.quickSort(v));
    // }

    public static Vector<org.LexGrid.concepts.Entity> restrictToMatchingProperty(
        String codingSchemeName, String version, LocalNameList propertyList,
        CodedNodeSet.PropertyType[] propertyTypes, LocalNameList sourceList,
        NameAndValueList qualifierList, java.lang.String matchText,
        java.lang.String matchAlgorithm, java.lang.String language,
        int maxToReturn) {

        codingSchemeName = TempFix.modifyCodingSchemeName(codingSchemeName);
        CodedNodeSet cns = null;

        long ms = System.currentTimeMillis();
        long delay = ms;

        Vector<org.LexGrid.concepts.Entity> v =
            new Vector<org.LexGrid.concepts.Entity>();

        ValueSetDefinition vsd = new ValueSetDefinition();
        String valueSetDefinitionURI =
            codingSchemeName + "_" + matchText + "_" + matchAlgorithm;
        if (version != null) {
            valueSetDefinitionURI =
                codingSchemeName + "_" + version + "_" + matchText + "_"
                    + matchAlgorithm;
        }
        valueSetDefinitionURI =
            TempFix.modifyValueSetDefinitionURI(valueSetDefinitionURI);

        try {
            vsd.setValueSetDefinitionURI(valueSetDefinitionURI);
        } catch (Exception e) {
            e.printStackTrace();
            _logger
                .error("ERROR: setValueSetDefinitionURI throws exceptions. ");
            return null;
        }

        String valueSetDefinitionName = valueSetDefinitionURI;
        vsd.setValueSetDefinitionName(valueSetDefinitionName);
        vsd.setDefaultCodingScheme(codingSchemeName);

        DefinitionEntry definitionEntry = new DefinitionEntry();
        definitionEntry.setRuleOrder(new java.lang.Long(1));
        definitionEntry.setOperator(DefinitionOperator.OR);

        // org.LexGrid.valueSets.PropertyReference _propertyReference
        PropertyReference _propertyReference = new PropertyReference();
        _propertyReference.setCodingScheme(codingSchemeName);
        String propertyName = null;
        if (propertyList != null && propertyList.getEntryCount() > 0) {
            propertyName = propertyList.getEntry(0);
            _propertyReference.setPropertyName(propertyName);
        }
        PropertyMatchValue _propertyMatchValue = new PropertyMatchValue();
        _propertyMatchValue.setMatchAlgorithm(matchAlgorithm);
        _propertyMatchValue.setContent(matchText);
        _propertyReference.setPropertyMatchValue(_propertyMatchValue);
        definitionEntry.setPropertyReference(_propertyReference);
        vsd.addDefinitionEntry(definitionEntry);

        try {
            AbsoluteCodingSchemeVersionReferenceList csvList =
                new AbsoluteCodingSchemeVersionReferenceList();
            csvList.addAbsoluteCodingSchemeVersionReference(Constructors
                .createAbsoluteCodingSchemeVersionReference(codingSchemeName,
                    version));

            ms = System.currentTimeMillis();
            ResolvedValueSetDefinition rvdDef =
                getValueSetDefinitionService().resolveValueSetDefinition(vsd,
                    csvList, null, null);
            delay = System.currentTimeMillis() - ms;
            _logger.debug("resolveValueSetDefinition delay: " + delay
                + " milli-seconds.");

            ms = System.currentTimeMillis();
            ResolvedConceptReferencesIterator iterator =
                rvdDef.getResolvedConceptReferenceIterator();
            delay = System.currentTimeMillis() - ms;
            _logger.debug("getResolvedConceptReferenceIterator delay: " + delay
                + " milli-seconds.");

            if (rvdDef != null) {
                Set<String> codes = new HashSet<String>();
                while (iterator.hasNext()) {
                    ResolvedConceptReference rcr = iterator.next();
                    Entity concept = rcr.getReferencedEntry();
                    if (concept == null) {
                        _logger.warn("rcr.getReferencedEntry() returns NULL");
                    } else {
                        v.add(concept);
                    }
                }
            } else {
                _logger.error("Unable to resolveValueSetDefinition??");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return v;
    }

    private static Vector<Entity> toConcept(Vector<?> v) {
        Iterator<?> iterator = v.iterator();
        Vector<Entity> newV = new Vector<Entity>();
        while (iterator.hasNext()) {
            Entity concept = (Entity) iterator.next();
            newV.add(concept);
        }
        return newV;
    }

    public static class SynonymInfo {
        public String name = "";
        public String type = "";
        public String source = "";
        public String sourceCode = "";

        public String toString() {
            return "name=" + name + ", type=" + type + ", source=" + source
                + ", sourceCode=" + sourceCode;
        }
    }

    public static Vector<SynonymInfo> getSynonyms(Entity concept) {
        Vector<SynonymInfo> v = new Vector<SynonymInfo>();
        if (concept == null)
            return v;

        Presentation[] properties = concept.getPresentation();
        for (int i = 0; i < properties.length; i++) {
            Presentation p = properties[i];
            SynonymInfo info = new SynonymInfo();

            info.name = p.getValue().getContent();
            int n = p.getPropertyQualifierCount();
            for (int j = 0; j < n; j++) {
                PropertyQualifier q = p.getPropertyQualifier(j);
                String qualifier_name = q.getPropertyQualifierName();
                String qualifier_value = q.getValue().getContent();
                if (qualifier_name.compareTo("source-code") == 0) {
                    info.sourceCode = qualifier_value;
                    break;
                }
            }

            info.type = p.getRepresentationalForm();
            Source[] sources = p.getSource();
            if (sources != null && sources.length > 0) {
                Source src = sources[0];
                info.source = src.getContent();
            }
            v.add(info);
        }
        SortUtils.quickSort(v);
        return v;
    }

    // -------------------------------------------------------------------------
    // Value Set Implementation:
    // -------------------------------------------------------------------------
    public static LexEVSValueSetDefinitionServices getValueSetDefinitionService()
            throws Exception {
/*
        String serviceUrl =
            AppProperties.getInstance().getProperty(
                AppProperties.EVS_SERVICE_URL);
        LexEVSDistributed distributed =
            (LexEVSDistributed) ApplicationServiceProvider
                .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");

        LexEVSDistributed distributed = getLexEVSDistributedService();
        LexEVSValueSetDefinitionServices service =
            distributed.getLexEVSValueSetDefinitionServices();

        return service;
*/
        return RemoteServerUtil.getValueSetDefinitionService();

    }

    public static Vector resolveValueSet(
        LexEVSValueSetDefinitionServices definitionServices,
        String uri, String codingScheme, String version,
        String code, boolean target2Source, String referenceAssociation,
        boolean includeRoot) throws Exception {

        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Resolving value set:");
        _logger.debug("  * codingScheme: " + codingScheme);
        _logger.debug("  * version: " + version);
        _logger.debug("  * code: " + code);
        _logger.debug("  * target2Source: " + target2Source);
        _logger.debug("  * referenceAssociation: " + referenceAssociation);
        _logger.debug("  * includeRoot: " + includeRoot);
        codingScheme = TempFix.modifyCodingSchemeName(codingScheme);
        ValueSetDefinition vsd = new ValueSetDefinition();
        String valueSetDefinitionURI =
            codingScheme + "_" + code + "_" + referenceAssociation + "_"
                + target2Source;
        if (version != null) {
            valueSetDefinitionURI =
                codingScheme + "_" + version + "_" + code + "_"
                    + referenceAssociation + "_" + target2Source;
        }
        valueSetDefinitionURI =
            TempFix.modifyValueSetDefinitionURI(valueSetDefinitionURI);
        vsd.setValueSetDefinitionURI(valueSetDefinitionURI);

        String valueSetDefinitionName = valueSetDefinitionURI;
        vsd.setValueSetDefinitionName(valueSetDefinitionName);
        vsd.setDefaultCodingScheme(codingScheme);

        DefinitionEntry definitionEntry = new DefinitionEntry();
        definitionEntry.setRuleOrder(new java.lang.Long(1));
        definitionEntry.setOperator(DefinitionOperator.OR);

        EntityReference entity = new EntityReference();
        entity.setEntityCode(code);
        entity.setReferenceAssociation(referenceAssociation);

        Boolean getLeafOnly = Boolean.FALSE;
        entity.setLeafOnly(getLeafOnly);

        Boolean transitiveClosure = Boolean.FALSE;
        entity.setTransitiveClosure(transitiveClosure);

        Boolean targetToSource = new Boolean(target2Source);
        entity.setTargetToSource(targetToSource);

        definitionEntry.setEntityReference(entity);
        vsd.addDefinitionEntry(definitionEntry);

        if (!includeRoot) {
            DefinitionEntry definitionEntry_root = new DefinitionEntry();
            definitionEntry_root.setRuleOrder(new java.lang.Long(2));
            definitionEntry_root.setOperator(DefinitionOperator.SUBTRACT);
            EntityReference entity_root = new EntityReference();
            entity_root.setEntityCode(code);
            definitionEntry_root.setEntityReference(entity_root);
            vsd.addDefinitionEntry(definitionEntry_root);
        }

        Vector v = new Vector();
        AbsoluteCodingSchemeVersionReferenceList csvList =
            new AbsoluteCodingSchemeVersionReferenceList();
        csvList.addAbsoluteCodingSchemeVersionReference(
			//Constructors.createAbsoluteCodingSchemeVersionReference(codingScheme,
			Constructors.createAbsoluteCodingSchemeVersionReference(uri, version));

_logger.debug("Calling definitionServices.resolveValueSetDefinition ...");
        ResolvedValueSetDefinition rvdDef =
            definitionServices.resolveValueSetDefinition(vsd,
                csvList, null, null);

_logger.debug("getResolvedConceptReferenceIterator...");
        int lcv = 0;

        if (rvdDef != null) {
            Set<String> codes = new HashSet<String>();
            while (rvdDef.getResolvedConceptReferenceIterator().hasNext()) {
                ResolvedConceptReference rcr =
                    rvdDef.getResolvedConceptReferenceIterator().next();
                Entity concept = rcr.getReferencedEntry();
                if (concept == null) {
                    _logger.warn("rcr.getReferencedEntry() returns NULL");
                } else {
                    v.add(concept);
                    lcv++;
                    if (lcv/500 * 500 == lcv) {
						_logger.debug("\t" + lcv + " concepts resolved.");
					}
                }
            }
        } else {
            _logger.error("Unable to resolveValueSetDefinition??");
        }
        _logger.debug("\t" + v.size() + " concepts resolved.");
        return v;
    }

    protected static CodingScheme getCodingScheme(String codingScheme,
        CodingSchemeVersionOrTag versionOrTag) throws LBException {

		/*

		// NCIT_VERSION
		if (codingScheme.compareTo("NCI_Thesaurus") == 0) {
			return getCodingScheme(codingScheme, versionOrTag);
		}

        CodingScheme cs = null;
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            cs = lbSvc.resolveCodingScheme(codingScheme, versionOrTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cs;
        */
        return resolveCodingScheme(codingScheme, versionOrTag.getVersion());
    }

    public static String codingSchemeName2URI(String codingSchemeName, String version)
        throws Exception {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		if (version != null) versionOrTag.setVersion(version);
		CodingScheme cs = getCodingScheme(codingSchemeName, versionOrTag);
		if (cs == null) return null;
		return cs.getCodingSchemeURI();
	}



    public static Boolean getIsForwardNavigable(String scheme, String version) {

        Vector association_vec = new Vector();
        //try {
			/*
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

            // Will handle secured ontologies later.
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);
            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            */
        CodingScheme cs = null;
        try {
            cs = resolveCodingScheme(scheme, version);

            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            Boolean forwardNavigable = hierarchies[0].getIsForwardNavigable();
			return forwardNavigable;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static String[] getHierarchyAssociations(String scheme, String version) {
        String[] asso_array = null;
        Vector association_vec = new Vector();
        //try {
			/*
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodingSchemeVersionOrTag versionOrTag =
                new CodingSchemeVersionOrTag();
            versionOrTag.setVersion(version);

            CodingScheme cs = lbSvc.resolveCodingScheme(scheme, versionOrTag);
            */
        CodingScheme cs = null;
        try {
            cs = resolveCodingScheme(scheme, version);
            Mappings mappings = cs.getMappings();
            SupportedHierarchy[] hierarchies = mappings.getSupportedHierarchy();
            for (int k=0; k<hierarchies.length; k++) {
				java.lang.String[] ids = hierarchies[k].getAssociationNames();
				for (int i = 0; i < ids.length; i++) {
					if (!association_vec.contains(ids[i])) {
						association_vec.add(ids[i]);
					}
				}
			}
			asso_array = new String[association_vec.size()];
			for (int i=0; i<association_vec.size(); i++) {
				String name = (String) association_vec.elementAt(i);
				asso_array[i] = name;
			}

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return asso_array;
    }


    public static Vector getSubconcepts(String scheme,
                                 String version,
                                 String code
                                 ) {
		Boolean isForwardNavigable = getIsForwardNavigable(scheme, version);
		String[] asso_array = getHierarchyAssociations(scheme, version);
        return getSubconcepts(scheme,
                              version,
                              code,
                              isForwardNavigable,
                              asso_array);
	}


    public static Vector getSubconcepts(String scheme,
                                 String version,
                                 String code,
                                 Boolean isForwardNavigable,
                                 String[] asso_array
                                 ) {

        Vector v = new Vector();
        boolean direction = true;
		if (isForwardNavigable == null) return null;
        if (isForwardNavigable.equals(Boolean.FALSE)) {
			direction = !direction;
		}

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) csvt.setVersion(version);

        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

           // resolveAsList
			ResolvedConceptReferenceList matches = cng.resolveAsList(ConvenienceMethods.createConceptReference(code, scheme),
			        direction, !direction, 1, 1,
					new LocalNameList(), null, null, -1);

			if (asso_array != null) {
				NameAndValue nv = new NameAndValue();
				NameAndValueList nvList = new NameAndValueList();
				for (int i=0; i<asso_array.length; i++) {
					nv.setName(asso_array[i]);
					nvList.addNameAndValue(nv);
				}
				cng = cng.restrictToAssociations(nvList, null);
			}

			// Analyze the result ...
			if (matches.getResolvedConceptReferenceCount() > 0) {
				ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
						.nextElement();

				AssociationList sourceof = null;
				if (direction) {
					sourceof = ref.getSourceOf();
				} else {
					sourceof = ref.getTargetOf();
				}

				if (sourceof != null) {
					Association[] associations = sourceof.getAssociation();
					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];
						AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
						for (int j = 0; j < acl.length; j++) {
							AssociatedConcept ac = acl[j];
							v.add(ac.getReferencedEntry());
     					}
					}
			    }
			}
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}



    public static Vector getSuperconcepts(String scheme,
                                 String version,
                                 String code
                                 ) {
		Boolean isForwardNavigable = getIsForwardNavigable(scheme, version);
		String[] asso_array = getHierarchyAssociations(scheme, version);
        return getSuperconcepts(scheme,
                              version,
                              code,
                              isForwardNavigable,
                              asso_array);
	}


    public static Vector getSuperconcepts(String scheme,
                                 String version,
                                 String code,
                                 Boolean isForwardNavigable,
                                 String[] asso_array
                                 ) {

        Vector v = new Vector();
        boolean direction = true;
		if (isForwardNavigable == null) return null;
        if (isForwardNavigable.equals(Boolean.FALSE)) {
			direction = !direction;
		}

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null) csvt.setVersion(version);

        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

           // resolveAsList
			ResolvedConceptReferenceList matches = cng.resolveAsList(ConvenienceMethods.createConceptReference(code, scheme),
			        !direction, direction, 1, 1,
					new LocalNameList(), null, null, -1);

			if (asso_array != null) {
				NameAndValue nv = new NameAndValue();
				NameAndValueList nvList = new NameAndValueList();
				for (int i=0; i<asso_array.length; i++) {
					nv.setName(asso_array[i]);
					nvList.addNameAndValue(nv);
				}
				cng = cng.restrictToAssociations(nvList, null);
			}

			// Analyze the result ...
			if (matches.getResolvedConceptReferenceCount() > 0) {
				ResolvedConceptReference ref = (ResolvedConceptReference) matches.enumerateResolvedConceptReference()
						.nextElement();

				AssociationList sourceof = null;
				if (direction) {
					sourceof = ref.getTargetOf();
				} else {
					sourceof = ref.getSourceOf();
				}

				if (sourceof != null) {
					Association[] associations = sourceof.getAssociation();
					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];
						AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
						for (int j = 0; j < acl.length; j++) {
							AssociatedConcept ac = acl[j];
							v.add(ac.getReferencedEntry());
     					}
					}
			    }
			}
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}




    public static LexBIGServiceConvenienceMethods createLexBIGServiceConvenienceMethods(
        LexBIGService lbSvc) {
        LexBIGServiceConvenienceMethods lbscm = null;
        try {
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lbscm;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // [GF#31126] Bad data in NICHD report
    // [GF#31146] Bad data in CDRH report
    public static Vector<AssociatedConcept> getRelatedConcepts(String scheme, String version, String code, String assocName, boolean direction) {
		Vector<AssociatedConcept> v = new Vector();
        LexBIGService lbSvc = null;
        try {
        	lbSvc = RemoteServerUtil.createLexBIGService();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

        LexBIGServiceConvenienceMethods lbscm =
            createLexBIGServiceConvenienceMethods(lbSvc);

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        ConceptReference cr = ConvenienceMethods.createConceptReference(code, scheme);

        CodedNodeGraph cng = null;
        try {
			cng = lbSvc.getNodeGraph(scheme, csvt, null);

			// Perform the query ...

			boolean resolveForward = true;
			boolean resolveBackward = false;
			if (!direction) {
			    resolveForward = false;
			    resolveBackward = true;
			}

			ResolvedConceptReferenceList matches = cng.resolveAsList(
					cr, resolveForward, resolveBackward, 1, 1, new LocalNameList(), null,
					null, 1024);

			String associationName = null;
			// Analyze the result ...
			if (matches.getResolvedConceptReferenceCount() > 0) {
				Enumeration<? extends ResolvedConceptReference> refEnum = matches.enumerateResolvedConceptReference();

				while (refEnum.hasMoreElements()) {
					ResolvedConceptReference ref = refEnum.nextElement();
					AssociationList sourceof = ref.getSourceOf();
					if (!direction) {
						sourceof = ref.getTargetOf();
					}
					Association[] associations = sourceof.getAssociation();

					for (int i = 0; i < associations.length; i++) {
						Association assoc = associations[i];

						try {
							associationName =
								lbscm.getAssociationNameFromAssociationCode(
										scheme, csvt, assoc
											.getAssociationName());
						} catch (Exception ex) {
							associationName = assoc.getAssociationName();
						}

						if (associationName.compareTo(assocName) == 0) {
							AssociatedConcept[] acl = assoc.getAssociatedConcepts().getAssociatedConcept();
							for (int j = 0; j < acl.length; j++) {
								AssociatedConcept ac = acl[j];
								v.add(ac);
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}


        return v;
    }


    // [#30542] SDTM: Use source-code SDTM-XYZ to pull PT
    public static String getPTBySourceCode(String scheme, String version, String code, String source, String source_code) {
        String pt = null;

        LexBIGService lbSvc = null;
        try {
        	lbSvc = RemoteServerUtil.createLexBIGService();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
        LexBIGServiceConvenienceMethods lbscm =
            createLexBIGServiceConvenienceMethods(lbSvc);

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        ConceptReference cr = ConvenienceMethods.createConceptReference(code, scheme);
        ConceptReferenceList codeList = new ConceptReferenceList();
        codeList.addConceptReference(cr);

        CodedNodeSet cns = null;
        try {
			LocalNameList entityTypes = new LocalNameList();
			entityTypes.addEntry("concept");
			cns = lbSvc.getNodeSet(scheme, csvt, entityTypes);

			cns = cns.restrictToCodes(codeList);

			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = new LocalNameList();
			propertyNames.addEntry("FULL_SYN");
			CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
			propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;

			boolean resolveObjects = true;
			int maxToReturn = 10;
            ResolvedConceptReferenceList rcrl = cns.resolveToList(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects, maxToReturn);

            for (int i=0; i<rcrl.getResolvedConceptReferenceCount(); i++) {
				ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
				Entity entity = rcr.getReferencedEntry();
				Presentation[] properties = entity.getPresentation();

				for (int j = 0; j < properties.length; j++) {
					Presentation prop = properties[j];
					if (prop.getRepresentationalForm()
						.compareTo("PT") == 0) {

                        boolean match_found = false;
						Source[] sources = prop.getSource();
						for (int m = 0; m < sources.length; m++) {
							Source src = sources[m];
							if (src.getContent().compareTo(source) == 0) {
								match_found = true;
								break;
							}
						}

                        if (match_found) {
							pt = prop.getValue().getContent();

							PropertyQualifier[] qualifiers =
								prop.getPropertyQualifier();

							if( qualifiers != null) {
								for (int k= 0; k < qualifiers.length; k++ ) {
									PropertyQualifier q = qualifiers[k];
									String name = q.getPropertyQualifierName();
									String value = q.getValue().getContent();

									if( name.compareTo("source-code") == 0 && value.compareTo(source_code) == 0 ) {
										pt = prop.getValue().getContent();
										return pt;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return pt;

	}


    public static String getHasParentAssociationName(String codingSchemeName, String version, String field_Id) {
		HashMap hmap = getHasParentAssociationMap(codingSchemeName, version);
		if (hmap == null) return null;
		return (String) hmap.get(field_Id);
	}


    public static HashMap getHasParentAssociationMap(String codingSchemeName, String version) {

		 if (_cs2HasParentAssociationMap == null) return null;
		 if (_cs2HasParentAssociationMap.containsKey(codingSchemeName)) {
			 return (HashMap) _cs2HasParentAssociationMap.get(codingSchemeName);
		 }

         HashMap hasParentAssociationMap = new HashMap();
         Vector temp_vec = new Vector();
         temp_vec.add("1st");
         temp_vec.add("2nd");

         try {
			 Vector<String> v = getSupportedAssociations(codingSchemeName, version);
			 if (v != null) {
				 for (int i=0; i<v.size(); i++) {
					 String assoName = (String) v.elementAt(i);
					 String assoNameLowerCase = assoName.toLowerCase();
					 if (assoNameLowerCase.startsWith("has") && assoNameLowerCase.endsWith("parent")) {

						 for (int k=0; k<temp_vec.size(); k++) {
                             String prefix = (String) temp_vec.elementAt(k);

							 String src = assoName.substring(4, assoName.length());
							 String s1 = prefix + " " + src + " Code";
							 s1 = s1.replaceAll("_", " ");

							 hasParentAssociationMap.put(s1, assoName);

							 s1 = prefix + " " + src + " Property";
							 s1 = s1.replaceAll("_", " ");

							 hasParentAssociationMap.put(s1, assoName);

							 s1 = prefix + " " + src + " Property Qualifier";
							 s1 = s1.replaceAll("_", " ");

							 hasParentAssociationMap.put(s1, assoName);
					     }
					 }
				 }
				 _cs2HasParentAssociationMap.put(codingSchemeName, hasParentAssociationMap);
			 }
	     } catch (Exception ex) {
			 ex.printStackTrace();
		 }
	     return hasParentAssociationMap;
	 }


    public static Vector<String> getSupportedAssociations(String codingSchemeName, String version)
            throws Exception {
		/*
        CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
        if (version != null)
            vt.setVersion(version);

        CodingScheme scheme = null;
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
        scheme = lbSvc.resolveCodingScheme(codingSchemeName, vt);
        */
        CodingScheme scheme = null;
        try {
            scheme = resolveCodingScheme(codingSchemeName, version);
			Vector<String> v = new Vector<String>();
			SupportedAssociation[] assos =
				scheme.getMappings().getSupportedAssociation();
			for (int i = 0; i < assos.length; i++) {
				SupportedAssociation sa = (SupportedAssociation) assos[i];
				v.add(sa.getLocalId());
			}
			SortUtils.quickSort(v);
			return v;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
    }


//  [GF#32309] Add column to download for file size.
	public static String getFileSize(long size) {
		if(size < 0) return "";
		/*
		int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
		if (digitGroups > 4) return "";
		try {
			return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		*/



		return "";
	}


	public static String getStringSizeLengthFile(long size) {
		DecimalFormat df = new DecimalFormat("0.00");
		float sizeKb = 1024.0f;
		float sizeMo = sizeKb * sizeKb;
		float sizeGo = sizeMo * sizeKb;
		float sizeTerra = sizeGo * sizeKb;
		if(size < sizeMo)
			return df.format(size / sizeKb)+ " KB";
		else if(size < sizeGo)
			return df.format(size / sizeMo) + " MB";
		else if(size < sizeTerra)
			return df.format(size / sizeGo) + " GB";
		return "";
	}




	public static String getFileSize(String filename) {
		File file = new File(filename);
		if (!file.exists()) return "";
		//return getFileSize(file.length());
		return getStringSizeLengthFile(file.length());
	}

/*
	public static String getFileExtension(String format) {
		if (format.compareToIgnoreCase("Text (tab delimited)") == 0) return ".txt";
		if (format.compareToIgnoreCase("Microsoft Office Excel") == 0) return ".xls";
		if (format.compareToIgnoreCase("HyperText Markup Language") == 0) return ".htm";
		return ".xml";
	}
*/

    public static Vector<String> parseData(String line) {
		if (line == null) return null;
        String tab = "|";
        return parseData(line, tab);
    }

    public static Vector<String> parseData(String line, String tab) {
		if (line == null) return null;
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = " ";
            data_vec.add(value);
        }
        return data_vec;
    }

    public static void main(String[] args) {

        try {
			String scheme = "NCI Thesaurus";
			String version = "11.03d";
            String code = "C66767";

            String assocName = "Concept_In_Subset";
            boolean direction = true;

            Vector v = getRelatedConcepts(scheme, version, code, assocName, direction);
            for (int i=0; i<v.size(); i++) {
				AssociatedConcept ac = (AssociatedConcept) v.elementAt(i);
				System.out.println("\t\t" + ac.getConceptCode() + "\t" + ac.getEntityDescription().getContent());
			}

            direction = false;

            v = getRelatedConcepts(scheme, version, code, assocName, direction);
            for (int i=0; i<v.size(); i++) {
				AssociatedConcept ac = (AssociatedConcept) v.elementAt(i);
				System.out.println("\t\t" + ac.getConceptCode() + "\t" + ac.getEntityDescription().getContent());
			}

            code = "C92807";
            assocName = "Has_NICHD_Parent";
            direction = true;

            v = getRelatedConcepts(scheme, version, code, assocName, direction);
            for (int i=0; i<v.size(); i++) {
				AssociatedConcept ac = (AssociatedConcept) v.elementAt(i);
				System.out.println("\t\t" + ac.getConceptCode() + "\t" + ac.getEntityDescription().getContent());
			}

			code = "C74912";
			String source = "CDISC";
			String source_code = "SDTM-LBTEST";
			String pt = getPTBySourceCode(scheme, version, code, source, source_code);
			System.out.println(code + " source: " + source + " source-code: " + source_code + " pt: " + pt);


			code = "C25158";
			source = "CDISC";
			source_code = "SDTM-LBTEST";
			pt = getPTBySourceCode(scheme, version, code, source, source_code);
			System.out.println(code + " source: " + source + " source-code: " + source_code + " pt: " + pt);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNullOrBlank(String value) {
		if (value == null || value.compareToIgnoreCase("null") == 0 || value.compareTo("") == 0) return true;
		return false;
	}

    public static CodingScheme resolveCodingScheme(String scheme, String version) {
		String representsVersion = version;

		if (!isNullOrBlank(NCIT_VERSION) || version.compareTo("@ncit.version@") == 0) {
			representsVersion = NCIT_VERSION;
		}

        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			CodingSchemeVersionOrTag vt = new CodingSchemeVersionOrTag();
			vt.setVersion(representsVersion);
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, vt);
			return cs;
		} catch (Exception e) {
			_logger.warn(e.getClass().getSimpleName() + ": "
				+ e.getMessage());
			_logger.warn("Possible security token needed for: ");
			_logger.warn("  * " + scheme);
		}
		return null;
	}

    public CodingScheme resolveCodingScheme(String scheme, CodingSchemeVersionOrTag vt) {
		if (!isNullOrBlank(NCIT_VERSION)) {
			vt.setVersion(NCIT_VERSION);
		}

        try {
			LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
			CodingScheme cs = lbSvc.resolveCodingScheme(scheme, vt);
			return cs;
		} catch (Exception e) {
			_logger.warn(e.getClass().getSimpleName() + ": "
				+ e.getMessage());
			_logger.warn("Possible security token needed for: ");
			_logger.warn("  * " + scheme);
		}
		return null;
	}


    public static String getLastModified(File file) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String lastModified = sdf.format(file.lastModified());
		return lastModified;
	}

    public static String getFileExtension(int formatId) {
        if (formatId == 404) return "txt";
        else if (formatId == 405) return "xls";
        if (formatId == 406) return "htm";
		return "";
	}

    public static String getFileExtension(String filename) {
		String extension = "";
		int i = filename.lastIndexOf('.');
		int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		if (i > p) {
			extension = filename.substring(i+1);
		}
		return extension;
	}

    public static String getFileFormat(String filename) {
		String extension = "";
		int i = filename.lastIndexOf('.');
		int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		if (i > p) {
			extension = filename.substring(i+1);
		}


		if (extension.compareTo("txt") == 0) {
			return "Text (tab delimited)";
		} else if (extension.compareTo("xls") == 0 || extension.compareTo("xlsx") == 0) {
			return "Microsoft Office Excel";
		} else if (extension.compareTo("htm") == 0 || extension.compareTo("html") == 0) {
			return "HyperText Markup Language";
		}
        return "";
	}

    public static Integer getFormatId(String filename) {
		String extension = "";
		int i = filename.lastIndexOf('.');
		int p = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
		if (i > p) {
			extension = filename.substring(i+1);
		}
		if (extension.compareTo("txt") == 0) {
			return new Integer(404);
		} else if (extension.compareTo("xls") == 0 || extension.compareTo("xlsx") == 0) {
			return new Integer(405);
		} else if (extension.compareTo("htm") == 0 || extension.compareTo("html") == 0) {
			return new Integer(406);
		}
        return new Integer(404);
	}

}

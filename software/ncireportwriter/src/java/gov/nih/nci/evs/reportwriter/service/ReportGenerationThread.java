package gov.nih.nci.evs.reportwriter.service;

import java.io.*;
import java.util.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;

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
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */


public class ReportGenerationThread implements Runnable {
    private static Logger _logger =
        Logger.getLogger(ReportGenerationThread.class);

    private String _outputDir = null;
    private String _standardReportLabel = null;
    private String _uid = null;

    private int _count = 0;
    private String _hierarchicalAssoName = null;

    public ReportGenerationThread(String outputDir, String standardReportLabel,
            String uid) {
        _outputDir = outputDir;
        _standardReportLabel = standardReportLabel;
        _uid = uid;

        _count = 0;
    }

    public PrintWriter openPrintWriter(String outputfile) {
        try {
            PrintWriter pw =
                new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
            return pw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closePrintWriter(PrintWriter pw) {
        if (pw == null) {
            _logger.warn("PrintWriter is not open.");
            return;
        }
        pw.close();
        pw = null;
    }

    public void run() {
        try {
            _logger.info("Generating report -- please wait...");
            long ms = System.currentTimeMillis();
            generateStandardReport(_outputDir, _standardReportLabel, _uid);
            _logger.info("Report " + " generated.");
            _logger.info("Run time (ms): " + (System.currentTimeMillis() - ms));
        } catch (Exception e) {
            _logger.error("Exception " + e);
        }
    }

    public Boolean generateStandardReport(String outputDir,
            String standardReportLabel, String uid) {
        StandardReportTemplate standardReportTemplate = null;
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            Object obj =
                sdkclientutil.search(FQName, methodName, standardReportLabel);
            standardReportTemplate = (StandardReportTemplate) obj;
            _logger.debug("standardReportTemplate ID: "
                    + standardReportTemplate.getId());
            _logger.debug("standardReportTemplate label: "
                    + standardReportTemplate.getLabel());

        } catch (Exception e) {
            _logger.error("Unable to identify report label "
                    + standardReportLabel + " -- report not generated.");
            return Boolean.FALSE;
        }

        if (standardReportTemplate == null) {
            _logger.error("Unable to identify report label "
                    + standardReportLabel + " -- report not generated.");
            return Boolean.FALSE;
        }

        String defining_set_desc = standardReportTemplate.getRootConceptCode();

        if (defining_set_desc.indexOf("|") != -1) {
            return generateSpecialReport(outputDir, standardReportLabel, uid);
        }

        _logger.debug("Output directory: " + outputDir);
        _logger.debug("standardReportLabel: " + standardReportLabel);
        _logger.debug("uid: " + uid);

        File dir = new File(outputDir);
        if (!dir.exists()) {
            _logger.debug("Output directory " + outputDir
                    + " does not exist -- try to create the directory.");
            boolean retval = dir.mkdir();
            if (!retval) {
                _logger.error("Unable to create output directory " + outputDir
                        + " - please check privilege setting.");
                return Boolean.FALSE;
            } else {
                _logger.debug("Output directory: " + outputDir + " created.");
            }
        } else {
            _logger.debug("Output directory: " + outputDir + " exists.");
        }

        String version = standardReportTemplate.getCodingSchemeVersion();
        // append version to the report file name:
        String pathname =
            outputDir + File.separator + standardReportLabel + "__" + version
                    + ".txt";
        pathname = pathname.replaceAll(" ", "_");
        _logger.debug("Full path name: " + pathname);

        PrintWriter pw = openPrintWriter(pathname);
        if (pw == null) {
            _logger.error("Unable to create output file " + pathname
                    + " - please check privilege setting.");
            return Boolean.FALSE;
        } else {
            _logger.debug("opened PrintWriter " + pathname);
        }

        int id = -1;
        String label = null;
        String codingSchemeName = null;
        String codingSchemeVersion = null;
        String rootConceptCode = null;
        String associationName = null;
        int level = -1;

        id = standardReportTemplate.getId();
        label = standardReportTemplate.getLabel();
        codingSchemeName = standardReportTemplate.getCodingSchemeName();
        codingSchemeVersion = standardReportTemplate.getCodingSchemeVersion();

        rootConceptCode = standardReportTemplate.getRootConceptCode();

        associationName = standardReportTemplate.getAssociationName();
        boolean direction = standardReportTemplate.getDirection();
        level = standardReportTemplate.getLevel();
        Character delimiter = standardReportTemplate.getDelimiter();

        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("ID: " + id);
        _logger.debug("Label: " + label);
        _logger.debug("CodingSchemeName: " + codingSchemeName);
        _logger.debug("CodingSchemeVersion: " + codingSchemeVersion);
        _logger.debug("Root: " + rootConceptCode);
        _logger.debug("AssociationName: " + associationName);
        _logger.debug("Direction: " + direction);
        _logger.debug("Level: " + level);
        _logger.debug("Delimiter: " + delimiter);

        String delimeter_str = "\t";

        Object[] objs = null;
        Collection<ReportColumn> cc =
            standardReportTemplate.getColumnCollection();
        if (cc == null) {
            _logger.warn("standardReportTemplate.getColumnCollection"
                    + " returns NULL?????????????");
        } else {
            objs = cc.toArray();
        }

        ReportColumn[] cols = null;
        if (cc != null) {
            cols = new ReportColumn[objs.length];
            if (objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    gov.nih.nci.evs.reportwriter.bean.ReportColumn col =
                        (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];

                    _logger.debug(StringUtils.SEPARATOR);
                    _logger.debug("Report Column:");
                    _logger.debug("ID: " + col.getId());
                    _logger.debug("Label: " + col.getLabel());
                    _logger.debug("Column Number: " + col.getColumnNumber());
                    _logger.debug("PropertyType: " + col.getPropertyType());
                    _logger.debug("PropertyName: " + col.getPropertyName());
                    _logger.debug("IsPreferred: " + col.getIsPreferred());
                    _logger.debug("RepresentationalForm: "
                            + col.getRepresentationalForm());
                    _logger.debug("Source: " + col.getSource());
                    _logger.debug("QualifierName: " + col.getQualifierName());
                    _logger.debug("QualifierValue: " + col.getQualifierValue());
                    _logger.debug("Delimiter: " + col.getDelimiter());
                    _logger.debug("ConditionalColumnIdD: "
                            + col.getConditionalColumnId());

                    cols[i] = col;
                }
            }
        }

        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("* Start generating report..." + pathname);

        printReportHeading(pw, cols);

        String scheme = standardReportTemplate.getCodingSchemeName();
        version = standardReportTemplate.getCodingSchemeVersion();

        String code = standardReportTemplate.getRootConceptCode();
        Concept defining_root_concept =
            DataUtils.getConceptByCode(codingSchemeName, codingSchemeVersion,
                    null, rootConceptCode);

        associationName = standardReportTemplate.getAssociationName();
        level = standardReportTemplate.getLevel();

        String tag = null;

        int curr_level = 0;
        int max_level = standardReportTemplate.getLevel();
        if (max_level == -1) {
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
        }

        // printReportHeading(pw, cols);
        if (_hierarchicalAssoName == null) {
            Vector<String> hierarchicalAssoName_vec =
                new DataUtils().getHierarchyAssociationId(scheme, version);
            if (hierarchicalAssoName_vec == null
                    || hierarchicalAssoName_vec.size() == 0) {
                return Boolean.FALSE;
            }
            _hierarchicalAssoName =
                (String) hierarchicalAssoName_vec.elementAt(0);
        }
        traverse(pw, scheme, version, tag, defining_root_concept, code,
                _hierarchicalAssoName, associationName, direction, curr_level,
                max_level, cols);
        closePrintWriter(pw);

        _logger.debug("Total number of concepts processed: " + _count);

        // convert to Excel:

        // createStandardReport -- need user's loginName
        // StandardReport extends Report
        // private StandardReportTemplate template;

        _logger.debug("Output file " + pathname + " generated.");

        // convert tab-delimited file to Excel

        Boolean bool_obj =
            new StandardReportService().createStandardReport(
                    standardReportLabel + ".txt", pathname,
                    standardReportTemplate.getLabel(), "Text (tab delimited)",
                    "DRAFT", uid);

        // convert to Excel
        bool_obj = FileUtil.convertToExcel(pathname, delimeter_str);

        // create xls report record
        pathname =
            outputDir + File.separator + standardReportLabel + "__" + version
                    + ".xls";
        pathname = pathname.replaceAll(" ", "_");
        _logger.debug("Full path name: " + pathname);

        bool_obj =
            new StandardReportService().createStandardReport(
                    standardReportLabel + ".xls", pathname,
                    standardReportTemplate.getLabel(),
                    "Microsoft Office Excel", "DRAFT", uid);

        return bool_obj;
    }

    public void printReportHeading(PrintWriter pw, ReportColumn[] cols) {

        if (cols == null) {
            _logger.warn("In printReportHeading number of ReportColumn:"
                    + " cols == null ??? ");
        }

        String columnHeadings = "";
        String delimeter_str = "\t";
        if (cols == null) {
            return;
        }
        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            columnHeadings = columnHeadings + rc.getLabel();
            if (i < cols.length - 1)
                columnHeadings = columnHeadings + delimeter_str;
        }
        pw.println(columnHeadings);
    }

    private void writeColumnData(PrintWriter pw, String scheme, String version,
            Concept defining_root_concept, Concept associated_concept,
            Concept c, String delim, ReportColumn[] cols,
            boolean firstColRequired) {

        if (firstColRequired) {
            String firstValue =
                getReportColumnValue(scheme, version, defining_root_concept,
                        associated_concept, c, cols[0]);
            if (firstValue == null)
                return;
            firstValue = firstValue.trim();
            if (firstValue.length() == 0)
                return;
        }

        String output_line = "";
        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            String s =
                getReportColumnValue(scheme, version, defining_root_concept,
                        associated_concept, c, rc);
            if (i == 0) {
                output_line = s;
            } else {
                // output_line = output_line + "\t" + s;
                output_line = output_line + delim + s;
            }
        }
        pw.println(output_line);

        _count++;
        if ((_count / 100) * 100 == _count) {
            _logger.debug("Number of concepts processed: " + _count);
        }
    }

    private void writeColumnData(PrintWriter pw, String scheme, String version,
            Concept defining_root_concept, Concept associated_concept,
            Concept c, String delim, ReportColumn[] cols) {
        String output_line = "";
        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            String s =
                getReportColumnValue(scheme, version, defining_root_concept,
                        associated_concept, c, rc);
            if (i == 0) {
                output_line = s;
            } else {
                // output_line = output_line + "\t" + s;
                output_line = output_line + delim + s;
            }
        }
        pw.println(output_line);

        _count++;
        if ((_count / 100) * 100 == _count) {
            _logger.debug("Number of concepts processed: " + _count);
        }
    }

    private void traverse(PrintWriter pw, String scheme, String version,
            String tag, Concept defining_root_concept, String code,
            String hierarchyAssociationName, String associationName,
            boolean direction, int level, int maxLevel, ReportColumn[] cols) {
        // _logger.debug("Level: " + level + "\tmaxLevel: " + maxLevel);
        if (maxLevel != -1 && level > maxLevel)
            return;
        RemoteServerUtil.createLexBIGService();

        Concept root = DataUtils.getConceptByCode(scheme, version, tag, code);
        if (root == null) {
            _logger.warn("Concept with code " + code + " not found.");
            return;
        } else {
            _logger.debug("Level: " + level + " Subset: " + root.getEntityCode());
        }

        String delim = "\t";

        Vector<Concept> v = new Vector<Concept>();
        DataUtils util = new DataUtils();
        if (direction) {
            v =
                util.getAssociationTargets(scheme, version, root.getEntityCode(),
                        associationName);
        } else {
            v =
                util.getAssociationSources(scheme, version, root.getEntityCode(),
                        associationName);
        }

        // associated concepts (i.e., concepts in subset)
        if (v == null)
            return;
        _logger.debug("Subset size: " + v.size());
        for (int i = 0; i < v.size(); i++) {
            // subset member element
            Concept c = (Concept) v.elementAt(i);
            writeColumnData(pw, scheme, version, defining_root_concept, root,
                    c, delim, cols);
        }

        Vector<Concept> subconcept_vec =
            util.getAssociationTargets(scheme, version, root.getEntityCode(),
                    hierarchyAssociationName);
        if (subconcept_vec == null | subconcept_vec.size() == 0)
            return;
        level++;
        for (int k = 0; k < subconcept_vec.size(); k++) {
            Concept concept = (Concept) subconcept_vec.elementAt(k);
            String subconcep_code = concept.getEntityCode();
            traverse(pw, scheme, version, tag, defining_root_concept,
                    subconcep_code, hierarchyAssociationName, associationName,
                    direction, level, maxLevel, cols);
        }
    }

    public String getReportColumnValue(String scheme, String version,
            Concept defining_root_concept, Concept associated_concept,
            Concept node, ReportColumn rc) {

        String field_Id = rc.getFieldId();
        String property_name = rc.getPropertyName();
        String qualifier_name = rc.getQualifierName();
        String source = rc.getSource();
        String qualifier_value = rc.getQualifierValue();
        String representational_form = rc.getRepresentationalForm();

        // check:
        Boolean isPreferred = rc.getIsPreferred();

        String property_type = rc.getPropertyType();
        char delimiter_ch = rc.getDelimiter();
        String delimiter = "" + delimiter_ch;

        if (isNull(field_Id))
            field_Id = null;
        if (isNull(property_name))
            property_name = null;
        if (isNull(qualifier_name))
            qualifier_name = null;
        if (isNull(source))
            source = null;
        if (isNull(qualifier_value))
            qualifier_value = null;
        if (isNull(representational_form))
            representational_form = null;
        if (isNull(property_type))
            property_type = null;
        if (isNull(delimiter))
            delimiter = null;

        if (field_Id.equals("Code"))
            return node.getEntityCode();
        if (field_Id.equals("Associated Concept Code"))
            return associated_concept.getEntityCode();

        Concept concept = node;
        if (property_name != null
                && property_name.compareTo("Contributing_Source") == 0) {
            concept = defining_root_concept;
        } else if (field_Id.indexOf("Associated") != -1) {
            concept = associated_concept;
        } else if (field_Id.indexOf("Parent") != -1) {
            if (_hierarchicalAssoName == null) {
                Vector<String> hierarchicalAssoName_vec =
                    new DataUtils().getHierarchyAssociationId(scheme, version);
                if (hierarchicalAssoName_vec == null
                        || hierarchicalAssoName_vec.size() == 0) {
                    return null;
                }
                _hierarchicalAssoName =
                    (String) hierarchicalAssoName_vec.elementAt(0);
            }
            // Vector superconcept_vec = new DataUtils().getParentCodes(scheme,
            // version, node.getId());
            Vector<String> superconcept_vec =
                new DataUtils().getAssociationSourceCodes(scheme, version, node
                        .getEntityCode(), _hierarchicalAssoName);
            if (superconcept_vec != null && superconcept_vec.size() > 0
                    && field_Id.indexOf("1st Parent") != -1) {
                String superconceptCode =
                    (String) superconcept_vec
                            .elementAt(superconcept_vec.size() - 1);
                if (field_Id.equals("1st Parent Code"))
                    return superconceptCode;
                concept =
                    DataUtils.getConceptByCode(scheme, version, null,
                            superconceptCode);
            } else if (superconcept_vec != null && superconcept_vec.size() > 1
                    && field_Id.indexOf("2nd Parent") != -1) {
                String superconceptCode =
                    (String) superconcept_vec
                            .elementAt(superconcept_vec.size() - 2);
                if (field_Id.equals("2nd Parent Code"))
                    return superconceptCode;
                concept =
                    DataUtils.getConceptByCode(scheme, version, null,
                            superconceptCode);

            } else {
                return " ";
            }
        }

        org.LexGrid.commonTypes.Property[] properties = null;

        if (property_type.compareToIgnoreCase("GENERIC") == 0) {
            properties = concept.getProperty();
        } else if (property_type.compareToIgnoreCase("PRESENTATION") == 0) {
            properties = concept.getPresentation();
            /*
        } else if (property_type.compareToIgnoreCase("INSTRUCTION") == 0) {
            properties = concept.getInstruction();
            */
        } else if (property_type.compareToIgnoreCase("COMMENT") == 0) {
            properties = concept.getComment();
        } else if (property_type.compareToIgnoreCase("DEFINITION") == 0) {
            properties = concept.getDefinition();
        }
        String return_str = ""; // RWW change from space to empty string
        int num_matches = 0;
        if (field_Id.indexOf("Property Qualifier") != -1) {
            // getRepresentationalForm
            boolean match = false;
            for (int i = 0; i < properties.length; i++) {
                qualifier_value = null;
                org.LexGrid.commonTypes.Property p = properties[i];
                if (p.getPropertyName().compareTo(property_name) == 0) // focus
                // on
                // matching
                // property
                {
                    match = true;

                    // <P90><![CDATA[<term-name>Black</term-name><term-group>PT</term-group><term-source>CDC</term-source><source-code>2056-0</source-code>]]></P90>

                    if (source != null || representational_form != null
                            || qualifier_name != null || isPreferred != null) {
                        // compare isPreferred

                        if (isPreferred != null && p instanceof Presentation) {
                            Presentation presentation = (Presentation) p;
                            Boolean is_pref = presentation.getIsPreferred();
                            if (is_pref == null) {
                                match = false;
                            } else if (!is_pref.equals(isPreferred)) {
                                match = false;
                            }
                        }

                        // match representational_form
                        if (match) {
                            if (representational_form != null
                                    && p instanceof Presentation) {
                                Presentation presentation = (Presentation) p;
                                if (presentation.getRepresentationalForm()
                                        .compareTo(representational_form) != 0) {
                                    match = false;
                                }
                            }
                        }

                        // match qualifier
                        if (match) {
                            if (qualifier_name != null) // match property
                            // qualifier, if needed
                            {
                                boolean match_found = false;
                                PropertyQualifier[] qualifiers =
                                    p.getPropertyQualifier();
                                if (qualifiers != null) {
                                    for (int j = 0; j < qualifiers.length; j++) {
                                        PropertyQualifier q = qualifiers[j];
                                        String name =
                                            q.getPropertyQualifierName();
                                        String value = q.getValue().getContent();
                                        if (qualifier_name.compareTo(name) == 0) {
                                            match_found = true;
                                            qualifier_value = value;
                                            break;
                                        }
                                    }
                                }
                                if (!match_found) {
                                    match = false;
                                }
                            }
                        }
                        // match source
                        if (match) {
                            if (source != null) // match source
                            {
                                boolean match_found = false;
                                Source[] sources = p.getSource();
                                for (int j = 0; j < sources.length; j++) {
                                    Source src = sources[j];
                                    if (src.getContent().compareTo(source) == 0) {
                                        match_found = true;
                                        break;
                                    }
                                }
                                if (!match_found) {
                                    match = false;
                                }
                            }
                        }
                    }
                }
                if (match && qualifier_value != null) {
                    num_matches++;
                    if (num_matches == 1) {
                        return_str = qualifier_value;
                    } else {
                        return_str = return_str + delimiter + qualifier_value;
                    }
                }
            }
            return return_str;
        }

        else if (field_Id.compareToIgnoreCase("Property") == 0
                || field_Id.compareToIgnoreCase("Associated Concept Property") == 0
                || field_Id.indexOf("Parent Property") != -1)

        {
            for (int i = 0; i < properties.length; i++) {
                boolean match = false;
                org.LexGrid.commonTypes.Property p = properties[i];
                if (p.getPropertyName().compareTo(property_name) == 0) // focus
                // on
                // matching
                // property
                {
                    match = true;

                    if (source != null || representational_form != null
                            || qualifier_name != null || isPreferred != null) {
                        // compare isPreferred
                        if (isPreferred != null && p instanceof Presentation) {
                            Presentation presentation = (Presentation) p;
                            Boolean is_pref = presentation.getIsPreferred();
                            if (is_pref == null) {
                                match = false;
                            } else if (is_pref != null
                                    && !is_pref.equals(isPreferred)) {
                                match = false;
                            }
                        }

                        // match representational_form
                        if (match) {
                            if (representational_form != null
                                    && p instanceof Presentation) {
                                Presentation presentation = (Presentation) p;
                                if (presentation.getRepresentationalForm()
                                        .compareTo(representational_form) != 0) {
                                    match = false;
                                }
                            }
                        }
                        // match qualifier
                        if (match) {
                            if (qualifier_name != null) // match property
                            // qualifier, if needed
                            {
                                boolean match_found = false;
                                PropertyQualifier[] qualifiers =
                                    p.getPropertyQualifier();
                                for (int j = 0; j < qualifiers.length; j++) {
                                    PropertyQualifier q = qualifiers[j];
                                    String name = q.getPropertyQualifierName();
                                    String value = q.getValue().getContent();
                                    if (qualifier_name.compareTo(name) == 0
                                            && qualifier_value.compareTo(value) == 0) {
                                        match_found = true;
                                        break;
                                    }
                                }
                                if (!match_found) {
                                    match = false;
                                }
                            }
                        }
                        // match source
                        if (match) {
                            if (source != null) // match source
                            {
                                boolean match_found = false;
                                Source[] sources = p.getSource();
                                for (int j = 0; j < sources.length; j++) {
                                    Source src = sources[j];
                                    if (src.getContent().compareTo(source) == 0) {
                                        match_found = true;
                                        break;
                                    }
                                }
                                if (!match_found) {
                                    match = false;
                                }
                            }
                        }
                    }
                }
                if (match) {
                    num_matches++;
                    if (num_matches == 1) {
                        return_str = p.getValue().getContent();
                    } else {
                        return_str =
                            return_str + delimiter + p.getValue().getContent();
                    }
                }
            }

        }
        return return_str;
    }

    private boolean isNull(String s) {
        if (s == null)
            return true;
        s = s.trim();
        if (s.length() == 0)
            return true;
        if (s.compareTo("") == 0)
            return true;
        if (s.compareToIgnoreCase("null") == 0)
            return true;
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Code for supporting country code like report generation:
    // /////////////////////////////////////////////////////////////////////////

    private Vector<String> parseData(String line, String delimiter) {
        Vector<String> data_vec = new Vector<String>();
        StringTokenizer st = new StringTokenizer(line, delimiter);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            data_vec.add(value);
        }
        return data_vec;
    }

    public Boolean generateSpecialReport(String outputDir,
            String standardReportLabel, String uid) {
        StandardReportTemplate standardReportTemplate = null;
        try {
            SDKClientUtil sdkclientutil = new SDKClientUtil();
            String FQName =
                "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
            String methodName = "setLabel";
            Object obj =
                sdkclientutil.search(FQName, methodName, standardReportLabel);
            standardReportTemplate = (StandardReportTemplate) obj;
            _logger.debug("standardReportTemplate ID: "
                    + standardReportTemplate.getId());
            _logger.debug("standardReportTemplate label: "
                    + standardReportTemplate.getLabel());

        } catch (Exception e) {
            _logger.error("Unable to identify report label "
                    + standardReportLabel + " -- report not generated.");
            return Boolean.FALSE;
        }

        if (standardReportTemplate == null) {
            _logger.error("Unable to identify report label "
                    + standardReportLabel + " -- report not generated.");
            return Boolean.FALSE;
        }

        String queryString = standardReportTemplate.getRootConceptCode();
        String delimiter = "|";
        Vector<String> v = parseData(queryString, delimiter);

        String property = (String) v.elementAt(0);
        String source = (String) v.elementAt(1);
        String qualifier_name = (String) v.elementAt(2);
        String qualifier_value = (String) v.elementAt(3);
        String matchText = (String) v.elementAt(4);
        String matchAlgorithm = (String) v.elementAt(5);

        _logger.debug("Output directory: " + outputDir);
        _logger.debug("standardReportLabel: " + standardReportLabel);
        _logger.debug("uid: " + uid);

        File dir = new File(outputDir);
        if (!dir.exists()) {
            _logger.debug("Output directory " + outputDir
                    + " does not exist -- try to create the directory.");
            boolean retval = dir.mkdir();
            if (!retval) {
                _logger.error("Unable to create output directory " + outputDir
                        + " - please check privilege setting.");
                return Boolean.FALSE;
            } else {
                _logger.debug("Output directory: " + outputDir + " created.");
            }
        } else {
            _logger.debug("Output directory: " + outputDir + " exists.");
        }

        String version = standardReportTemplate.getCodingSchemeVersion();
        // append verision to the report file name:
        String pathname =
            outputDir + File.separator + standardReportLabel + "__" + version
                    + ".txt";
        pathname = pathname.replaceAll(" ", "_");
        _logger.debug("Full path name: " + pathname);

        PrintWriter pw = openPrintWriter(pathname);
        if (pw == null) {
            _logger.error("Unable to create output file " + pathname
                    + " - please check privilege setting.");
            return Boolean.FALSE;
        } else {
            _logger.debug("opened PrintWriter " + pathname);
        }

        int id = -1;
        String label = null;
        String codingSchemeName = null;
        String codingSchemeVersion = null;
        String rootConceptCode = null;
        String associationName = null;
        int level = -1;

        // char delim = '$';

        id = standardReportTemplate.getId();
        label = standardReportTemplate.getLabel();
        codingSchemeName = standardReportTemplate.getCodingSchemeName();
        codingSchemeVersion = standardReportTemplate.getCodingSchemeVersion();

        rootConceptCode = standardReportTemplate.getRootConceptCode();

        associationName = standardReportTemplate.getAssociationName();
        boolean direction = standardReportTemplate.getDirection();
        level = standardReportTemplate.getLevel();

        // Character delimiter = standardReportTemplate.getDelimiter();

        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("ID: " + id);
        _logger.debug("Label: " + label);
        _logger.debug("CodingSchemeName: " + codingSchemeName);
        _logger.debug("CodingSchemeVersion: " + codingSchemeVersion);
        _logger.debug("Root: " + rootConceptCode);
        _logger.debug("AssociationName: " + associationName);
        _logger.debug("Direction: " + direction);
        _logger.debug("Level: " + level);
        // _logger.debug("Delimiter: " + delimiter);

        String delimeter_str = "\t";

        Object[] objs = null;
        Collection<ReportColumn> cc =
            standardReportTemplate.getColumnCollection();
        if (cc == null) {
            _logger.warn("standardReportTemplate.getColumnCollection"
                    + " returns NULL?????????????");
        } else {
            objs = cc.toArray();
        }

        ReportColumn[] cols = null;
        if (cc != null) {
            cols = new ReportColumn[objs.length];
            if (objs.length > 0) {
                for (int i = 0; i < objs.length; i++) {
                    gov.nih.nci.evs.reportwriter.bean.ReportColumn col =
                        (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];

                    _logger.debug(StringUtils.SEPARATOR);
                    _logger.debug("Report Column:");
                    _logger.debug("ID: " + col.getId());
                    _logger.debug("Label: " + col.getLabel());
                    _logger.debug("Column Number: " + col.getColumnNumber());
                    _logger.debug("PropertyType: " + col.getPropertyType());
                    _logger.debug("PropertyName: " + col.getPropertyName());
                    _logger.debug("IsPreferred: " + col.getIsPreferred());
                    _logger.debug("RepresentationalForm: "
                            + col.getRepresentationalForm());
                    _logger.debug("Source: " + col.getSource());
                    _logger.debug("QualifierName: " + col.getQualifierName());
                    _logger.debug("QualifierValue: " + col.getQualifierValue());
                    _logger.debug("Delimiter: " + col.getDelimiter());
                    _logger.debug("ConditionalColumnIdD: "
                            + col.getConditionalColumnId());

                    cols[i] = col;
                }
            }
        }

        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("* Start generating report..." + pathname);

        printReportHeading(pw, cols);

        // String scheme = standardReportTemplate.getCodingSchemeName();
        version = standardReportTemplate.getCodingSchemeVersion();

        Vector<String> property_vec = null;
        if (property != null && property.compareTo("null") != 0) {
            property_vec = new Vector<String>();
            property_vec.add(property);
        }

        Vector<String> source_vec = null;
        if (source != null && source.compareTo("null") != 0) {
            source_vec = new Vector<String>();
            source_vec.add(source);
        }

        Vector<String> qualifier_name_vec = null;
        if (qualifier_name != null && qualifier_name.compareTo("null") != 0) {
            qualifier_name_vec = new Vector<String>();
            qualifier_name_vec.add(qualifier_name);
        }

        Vector<String> qualifier_value_vec = null;
        if (qualifier_value != null && qualifier_value.compareTo("null") != 0) {
            qualifier_value_vec = new Vector<String>();
            qualifier_value_vec.add(qualifier_value);
        }

        int maxToReturn = 10000;
        String language = null;

        Vector<Concept> concept_vec =
            DataUtils.restrictToMatchingProperty(codingSchemeName, version,
                    property_vec, source_vec, qualifier_name_vec,
                    qualifier_value_vec, matchText, matchAlgorithm, language,
                    maxToReturn);

        String delim = "\t";
        for (int i = 0; i < concept_vec.size(); i++) {
            Concept c = (Concept) concept_vec.elementAt(i);
            writeColumnData(pw, codingSchemeName, version, null, null, c,
                    delim, cols, true);
        }

        closePrintWriter(pw);
        _logger.debug("Output file " + pathname + " generated.");

        Boolean bool_obj =
            new StandardReportService().createStandardReport(
                    standardReportLabel + ".txt", pathname,
                    standardReportTemplate.getLabel(), "Text (tab delimited)",
                    "DRAFT", uid);

        // convert to Excel
        bool_obj = FileUtil.convertToExcel(pathname, delimeter_str);

        // create xls report record
        pathname =
            outputDir + File.separator + standardReportLabel + "__" + version
                    + ".xls";
        pathname = pathname.replaceAll(" ", "_");
        _logger.debug("Full path name: " + pathname);

        bool_obj =
            new StandardReportService().createStandardReport(
                    standardReportLabel + ".xls", pathname,
                    standardReportTemplate.getLabel(),
                    "Microsoft Office Excel", "DRAFT", uid);
        return bool_obj;
    }
}

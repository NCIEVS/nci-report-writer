/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.service;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.utils.DataUtils.*;
import gov.nih.nci.evs.utils.*;

import java.io.*;
import java.util.*;

import org.LexGrid.concepts.*;
import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class SpecialCases {
    private static Logger _logger = Logger.getLogger(SpecialCases.class);

    public static class CDRHInfo {
        String value = null;
        Concept associated_concept = null;

        public CDRHInfo(String value) {
            this.value = value;
        }

        public CDRHInfo(Concept associated_concept) {
            this.associated_concept = associated_concept;
        }
    }

    public static class CDRH {
        public static CDRHInfo getAssociatedConcept(String label,
            String scheme, String version, Concept node) {
            if (label.indexOf("[CDRH] PARENT") < 0)
                return null;

            Vector<Concept> v =
                DataUtils.getAssociationTargets(scheme, version, node
                    .getEntityCode(), "A10");
            if (v == null || v.size() <= 0)
                return new CDRHInfo(""); // Previous: Not Available

            Concept associated_concept = (Concept) v.elementAt(0);
            return new CDRHInfo(associated_concept);
        }

        public static String replaceLabel(String label) {
            label = label.replaceAll("\\[CDRH] ", "");
            return label;
        }
    }

    public static class CDISCExtensibleInfo {
        public boolean isExtensibleValue = false;
        public String codelistCode = "";
        public int codelistCodeColumnIndex = -1;
        public int codelistExtensibleColumnIndex = -1;
        public int codelistNameColumnIndex = -1;
        public String extensibleColumnValue = "";
        public String newValue = "";
        public boolean skipRow = false;

        public CDISCExtensibleInfo(ReportColumn[] cols) {
            for (int i = 0; i < cols.length; i++) {
                ReportColumn rc = (ReportColumn) cols[i];
                String header = rc.getLabel();
                if (header.contains(CDISC.EXTENSIBLE_LABEL))
                    codelistExtensibleColumnIndex = i;
                else if (header.contains(CDISC.CODELIST_CODE_LABEL))
                    codelistCodeColumnIndex = i;
                else if (header.contains(CDISC.CODELIST_NAME_LABEL))
                    codelistNameColumnIndex = i;
            }
        }
    }

    public static class CDISC {
        private static boolean _debugGetCdiscPreferredTerm = false;
        private static final String CODELIST_CODE_LABEL = "Codelist Code";
        private static final String EXTENSIBLE_LABEL = "Extensible";
        private static final String CODELIST_NAME_LABEL = "Codelist Name";
        private static final String SUBMISSION_LABEL = "Submission";

        public static String getSubmissionValue(String label, Concept node,
            Concept associated_concept, String delimiter) {
            try {
                if (!label.contains(SUBMISSION_LABEL))
                    return null;
                String value =
                    getPreferredTerm(node, associated_concept, delimiter);
                return value;
            } catch (Exception e) {
                // e.printStackTrace();
                ExceptionUtils.print(_logger, e,
                    "Method: SpecialCases.getCdiscSubmissionValue");
                return null;
            }
        }

        public static String getPreferredTerm(String codingScheme,
            String version, String code, String associatedCode)
                throws Exception {
            Concept concept =
                DataUtils.getConceptByCode(codingScheme, version, null, code);
            if (concept == null)
                throw new Exception("Can not retrieve concept from code "
                    + code + ".");
            Concept associatedConcept =
                DataUtils.getConceptByCode(codingScheme, version, null,
                    associatedCode);
            if (associatedConcept == null)
                throw new Exception(
                    "Can not retrieve associated concept from code "
                        + associatedConcept + ".");

            String name = concept.getEntityDescription().getContent();
            String associatedName =
                associatedConcept.getEntityDescription().getContent();
            _logger.debug("* concept: " + name + "(" + code + ")");
            _logger.debug("* associatedConcept: " + associatedName + "("
                + associatedCode + ")");
            return getPreferredTerm(concept, associatedConcept, "|");
        }

        public static String getPreferredTerm(Concept concept,
            Concept associated_concept, String delimiter) throws Exception {
            delimiter = "; ";
            String nciABTerm = null;
            Vector<SynonymInfo> v = DataUtils.getSynonyms(associated_concept);
            // ListUtils.debug(_logger, "associated_concept: "
            // + associated_concept.getEntityCode(), v);
            int n = v.size();
            for (int i = 0; i < n; ++i) {
                SynonymInfo info = v.get(i);
                if (info.source.equalsIgnoreCase("NCI")
                    && info.type.equalsIgnoreCase("AB")) {
                    nciABTerm = info.name;
                }
            }
            v = DataUtils.getSynonyms(concept);
            // ListUtils.debug(_logger, "concept: " + concept.getEntityCode(),
            // v);
            n = v.size();
            boolean debug = _debugGetCdiscPreferredTerm;

            StringBuffer buffer = new StringBuffer();
            if (nciABTerm != null) {
                for (int i = 0; i < n; ++i) {
                    SynonymInfo info = v.get(i);
                    if (info.sourceCode.equals(nciABTerm))
                        StringUtils.append(buffer, info.name, delimiter);
                }
                if (buffer.length() > 0) {
                    if (debug)
                        buffer.insert(0, "[nciABTerm=" + nciABTerm + "]: ");
                    return buffer.toString();
                }
            }

            // If any, retrieve "CDISC|SY".
            for (int i = 0; i < n; ++i) {
                SynonymInfo info = v.get(i);
                if (info.source.equalsIgnoreCase("CDISC")
                    && info.type.equalsIgnoreCase("SY"))
                    StringUtils.append(buffer, info.name, delimiter);
            }
            if (buffer.length() > 0) {
                if (debug)
                    buffer.insert(0, "[CDISC|SY]: ");
                return buffer.toString();
            }

            // If any, retrieve "CDISC|PT".
            for (int i = 0; i < n; ++i) {
                SynonymInfo info = v.get(i);
                if (info.source.equalsIgnoreCase("CDISC")
                    && info.type.equalsIgnoreCase("PT"))
                    StringUtils.append(buffer, info.name, delimiter);
            }
            if (buffer.length() > 0) {
                if (debug)
                    buffer.insert(0, "[CDISC|PT]: ");
                return buffer.toString();
            }
            return "";
        }

        public static boolean writeExtensibleColumnData(
            SpecialCases.CDISCExtensibleInfo info, ReportColumn rc,
            Vector<String> values, String value, int i) {
            if (!rc.getLabel().contains(EXTENSIBLE_LABEL))
                return false;

            info.extensibleColumnValue = value;
            info.skipRow = value == null || value.trim().length() <= 0;
            if (info.skipRow)
                return true;

            if (!values.get(info.codelistCodeColumnIndex).equals(
                info.codelistCode)) {
                info.isExtensibleValue = true;
                info.codelistCode = values.get(info.codelistCodeColumnIndex);
            }
            info.newValue = "";
            return true;
        }

        public static void writeSubheader(
            SpecialCases.CDISCExtensibleInfo info,
            ReportGenerationThread reportGenerationThread,
            Vector<String> values, PrintWriter pw, String scheme,
            String version, Concept defining_root_concept,
            Concept associated_concept, Concept c, String delim,
            ReportColumn[] cols) {
            if (!info.isExtensibleValue)
                return;

            Vector<String> subHeader = new Vector<String>();
            for (int i = 0; i < cols.length; i++) {
                ReportColumn rc = (ReportColumn) cols[i];
                String value =
                    reportGenerationThread.getReportColumnValue(scheme,
                        version, defining_root_concept, null,
                        associated_concept, rc);
                subHeader.add(value);
            }
            subHeader.set(info.codelistExtensibleColumnIndex,
                info.extensibleColumnValue);
            subHeader.set(info.codelistNameColumnIndex,
                values.get(info.codelistNameColumnIndex));
            pw.println(StringUtils.toString(subHeader, delim, true));
        }
    }
}

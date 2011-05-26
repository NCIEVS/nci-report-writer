package gov.nih.nci.evs.reportwriter.service;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.utils.DataUtils.*;
import gov.nih.nci.evs.utils.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.LexGrid.concepts.*;
import org.apache.log4j.*;
import org.lexgrid.valuesets.*;

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
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class SpecialCases {
    private static Logger _logger = Logger.getLogger(SpecialCases.class);

    private static class RegExArgs {
        public static String replace(String text, String regex,
                String replacement) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            String newValue = matcher.replaceAll(replacement);
            return newValue;
        }

        public static boolean exists(String text, String key) {
            String phrase = getKeyPhrase(key);
            Pattern pattern = Pattern.compile(phrase);
            Matcher matcher = pattern.matcher(text);
            return matcher.find();
        }

        private static String getKeyPhrase(String key) {
            return "\\s*\\[\\s*" + key + "\\s*\\=\\s*";
        }

        /**
         * Gets the value of the name-value pair that is
         * embedded within square brackets.
         * @param text
         * @param key
         * @return
         */
        public static String getValue(String text, String key) {
            String phrase = getKeyPhrase(key);
            String tmpPhrase = "[" + key + "=";
            String value = RegExArgs.replace(text, phrase, tmpPhrase);

            int i = value.indexOf(tmpPhrase);
            if (i < 0)
                return null;
            value = value.substring(i + tmpPhrase.length());
            int j = value.indexOf("]");
            value = value.substring(0, j).trim();
            return value;
        }

        public static String getValueWithoutArgs(String text) {
            if (text == null)
                return null;
            int i = text.indexOf("[");
            if (i < 0)
                return text;
            String value = text.substring(0, i).trim();
            return value;
        }

        public static String getArgs(String text) {
            if (text == null)
                return null;
            int i = text.indexOf("[");
            if (i < 0)
                return "";
            String value = text.substring(i).trim();
            return value;
        }

        public static String mergeValueAndArgs(String value, String args) {
            if (args == null || args.trim().length() <= 0)
                return value;
            return value + " " + args;
        }
    }

    public static class GetHasParent extends RegExArgs {
        public static String getAssocName(String field_Id) {
            if (! field_Id.contains("Associated Concept"))
                return null;
            String assocName = getValue(field_Id, "assocName");
            return assocName;
        }

        public static Entity getAssociatedConcept(
            LexEVSValueSetDefinitionServices definitionServices,
            String uri, String scheme, String version, Entity node,
            String field_Id) throws Exception {
            String assocName = getValue(field_Id, "assocName");

//NOTE_A8: A8 vs Concept_In_Subset (Begin)
//            if (assocName.contains("Has_CDRH_Parent"))
//                assocName = "A10";
//            else if (assocName.contains("Has_NICHD_Parent"))
//                assocName = "A11";
//NOTE_A8: A8 vs Concept_In_Subset (End)
            Vector<Entity> v =
                DataUtils.getAssociationTargets(definitionServices, uri,
                    scheme, version, node.getEntityCode(), assocName);
            if (v == null || v.size() <= 0)
                return null;
            if (field_Id.contains("2nd"))
                return v.size() >= 2 ? v.elementAt(1) : null;
            return v.elementAt(0);
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
        public static boolean ON = true;
        private static boolean _debugGetCdiscPreferredTerm = false;
        private static final String CODELIST_CODE_LABEL = "Codelist Code";
        private static final String EXTENSIBLE_LABEL = "Extensible";
        private static final String CODELIST_NAME_LABEL = "Codelist Name";
        private static final String SUBMISSION_LABEL = "Submission";

        public static String getSubmissionValue(String label, Entity node,
            Entity associated_concept, String delimiter) {
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
            Entity concept =
                DataUtils.getConceptByCode(codingScheme, version, null, code);
            if (concept == null)
                throw new Exception("Can not retrieve concept from code "
                    + code + ".");
            Entity associatedConcept =
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

        public static String getPreferredTerm(Entity concept,
            Entity associated_concept, String delimiter) throws Exception {
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
            LexEVSValueSetDefinitionServices definitionServices,
            String uri, SpecialCases.CDISCExtensibleInfo info,
            ReportGenerationThread reportGenerationThread,
            Vector<String> values, PrintWriter pw, String scheme,
            String version, Entity defining_root_concept,
            Entity associated_concept, Entity c, String delim,
            ReportColumn[] cols) throws Exception {
            if (!info.isExtensibleValue)
                return;

            Vector<String> subHeader = new Vector<String>();
            for (int i = 0; i < cols.length; i++) {
                ReportColumn rc = (ReportColumn) cols[i];
                String value =
                    reportGenerationThread.get_ReportColumnValue(
                        definitionServices, uri, scheme,
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

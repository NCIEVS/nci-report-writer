package gov.nih.nci.evs.reportwriter.service;

import gov.nih.nci.evs.reportwriter.utils.*;
import gov.nih.nci.evs.reportwriter.utils.DataUtils.*;

import java.util.*;

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
                return new CDRHInfo("Not Available");

            Concept associated_concept = (Concept) v.elementAt(0);
            return new CDRHInfo(associated_concept);
        }
        
        public static String replaceLabel(String label) {
            label = label.replaceAll("\\[CDRH] ", "");
            return label;
        }
    }

    public static class CDISC {
        private static boolean _debugGetCdiscPreferredTerm = true; // DYEE

        public static String getSubmissionValue(String label, Concept node,
            Concept associated_concept, String delimiter) {
            try {
                if (!label.equalsIgnoreCase("CDISC Submission Value"))
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
    }
}

package gov.nih.nci.evs.reportwriter.utils;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.commonTypes.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;

import java.util.*;

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

public class FindRelatedCodesUtil {
    private static Logger _logger =
        Logger.getLogger(FindRelatedCodesUtil.class);

    public static void Util_displayAndLogError(String s, Exception e) {
        ExceptionUtils.print(_logger, e, s);
    }

    public static void Util_displayMessage(String s) {
        _logger.debug(s);
    }

    protected void displayRef(ResolvedConceptReference ref) {
        _logger.debug(ref.getConceptCode() + ":"
            + ref.getEntityDescription().getContent());
    }

    protected void displayRef(Concept ce) {
        _logger.debug(StringUtils.SEPARATOR);
        _logger.debug("Concept: " + ce.getEntityCode() + ": "
            + ce.getEntityDescription().getContent());

        Property[] properties = ce.getPresentation();
        outputPropertyDetails(properties);

        _logger.debug(StringUtils.SEPARATOR);
        String atom_name = getAtomName(ce, "FDA", "PT");
        _logger.debug("atom_name: " + atom_name);

        String atom_source_code = getAtomSourceCode(ce, "FDA", "source-code");
        _logger.debug("atom_source_code: " + atom_source_code);
    }

    public void outputPropertyDetails(Property[] properties) {
        for (int i = 0; i < properties.length; i++) {
            Property property = (Property) properties[i];

            String name = property.getPropertyName();
            String prop_value = property.getValue().getContent();
            _logger.debug(name + ": " + prop_value);

            if (property instanceof org.LexGrid.concepts.Presentation) {
                Presentation presentation = (Presentation) property;
                String form = presentation.getRepresentationalForm();
                if (form != null) {
                    _logger.debug("  * RepresentationalForm: " + form);
                }
            }

            Source[] sources = property.getSource();
            for (int j = 0; j < sources.length; j++) {
                Source source = (Source) sources[j];
                String sab = source.getContent();
                _logger.debug("  * Source: " + sab);
            }

            PropertyQualifier[] qualifiers = property.getPropertyQualifier();
            for (int j = 0; j < qualifiers.length; j++) {
                _logger.debug("  * Qualifier name: "
                    + qualifiers[j].getPropertyQualifierName());
                _logger.debug("  * Qualifier value: "
                    + qualifiers[j].getValue().getContent());
            }
        }
    }

    public void dumpResolvedConceptReferencesIterator(
        ResolvedConceptReferencesIterator itr) {
        if (itr == null)
            return;
        try {
            while (itr.hasNext()) {
                ResolvedConceptReference[] refs =
                    itr.next(100).getResolvedConceptReference();
                for (ResolvedConceptReference ref : refs) {
                    displayRef(ref);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getAssociatedConcepts(String scheme, String version,
        String code, String relation, boolean direction, int maxReturn)
            throws Exception {
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();

        // scheme = css.getCodingSchemeURN();
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        NameAndValue nv = new NameAndValue();
        NameAndValueList nvList = new NameAndValueList();

        if (relation != null) {
            nv.setName(relation);
            nvList.addNameAndValue(nv);
        }

        try {
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);
            cng = cng.restrictToAssociations(nvList, null);
            ConceptReference graphFocus =
                ConvenienceMethods.createConceptReference(code, scheme);
            // CodedNodeSet cns = cng.toNodeList(graphFocus, direction,
            // !direction, 1, -1);
            CodedNodeSet cns =
                cng.toNodeList(graphFocus, direction, !direction, 1, -1);
            CodedNodeSet cns0 = lbSvc.getCodingSchemeConcepts(scheme, csvt);
            ConceptReferenceList codeList = new ConceptReferenceList();
            codeList.addConceptReference(graphFocus);
            cns0 = cns0.restrictToCodes(codeList);
            cns = cns.difference(cns0);
            // ResolvedConceptReferencesIterator iterator = null;
            // SortOptionList sortOptions = null;
            // LocalNameList filterOptions = null;
            // LocalNameList propertyNames = null;
            // CodedNodeSet.PropertyType[] propertyTypes = null;
            // LocalNameList restrictToProperties = null;
            // boolean resolveObjects = false;

            Vector<CodedNodeSet> cns_vec = new Vector<CodedNodeSet>();
            cns_vec.add(cns);

            try {
                ResolvedConceptReference[] list =
                    cns.resolveToList(null, null, null, null, true, maxReturn)
                        .getResolvedConceptReference();
                if (list != null) {
                    for (ResolvedConceptReference ref : list) {
                        displayRef(ref.getReferencedEntry());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected String getAtomName(Concept ce, String sab_name, String term_type) {
        if (ce == null)
            return null;
        Property[] properties = ce.getPresentation();
        for (int i = 0; i < properties.length; i++) {
            Property property = (Property) properties[i];
            // String name = property.getPropertyName();
            String prop_value = property.getValue().getContent();
            if (property instanceof org.LexGrid.concepts.Presentation) {
                Presentation presentation = (Presentation) property;
                String form = presentation.getRepresentationalForm();
                if (form != null) {
                    if (form.compareTo(term_type) == 0) {
                        Source[] sources = property.getSource();
                        for (int j = 0; j < sources.length; j++) {
                            Source source = (Source) sources[j];
                            String sab = source.getContent();
                            if (sab.compareTo(sab_name) == 0) {
                                return prop_value;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    protected String getAtomSourceCode(Concept ce, String sab_name,
        String qualifier_name) {
        if (ce == null)
            return null;
        Property[] properties = ce.getPresentation();
        for (int i = 0; i < properties.length; i++) {
            Property property = (Property) properties[i];
            // String name = property.getPropertyName();
            // String prop_value = property.getValue().getContent();

            Source[] sources = property.getSource();
            for (int j = 0; j < sources.length; j++) {
                Source source = (Source) sources[j];
                String sab = source.getContent();
                if (sab.compareTo(sab_name) == 0) {
                    if (property instanceof org.LexGrid.concepts.Presentation) {
                        PropertyQualifier[] qualifiers =
                            property.getPropertyQualifier();
                        if (qualifiers != null) {
                            for (int k = 0; k < qualifiers.length; k++) {
                                PropertyQualifier q = qualifiers[k];
                                String q_name = q.getPropertyQualifierName();
                                String q_value = q.getValue().getContent();
                                if (qualifier_name.compareTo(q_name) == 0) {
                                    return q_value;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            String scheme = "NCI Thesaurus";
            String version = null;
            String code = "C63923";

            FindRelatedCodesUtil test = new FindRelatedCodesUtil();
            code = "C50372";
            String assoName = "A10";
            boolean direction = true;
            test.getAssociatedConcepts(scheme, version, code, assoName,
                direction, 100);
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
        }
    }
}

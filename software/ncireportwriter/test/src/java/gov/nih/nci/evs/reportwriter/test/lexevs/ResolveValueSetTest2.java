package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.apache.log4j.Logger;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

public class ResolveValueSetTest2 {
    private static Logger _logger = Logger
        .getLogger(ResolveValueSetTest2.class);

    public static Vector resolveValueSet(String codingScheme, String version,
        String code, boolean target2Source, String referenceAssociation,
        boolean includeRoot) throws Exception {

        _logger.debug(FromOtherClasses.SEPARATOR);
        _logger.debug("Method: DataUtils.resolveValueSet");
        _logger.debug("  * codingScheme: " + codingScheme);
        _logger.debug("  * version: " + version);
        _logger.debug("  * code: " + code);
        _logger.debug("  * target2Source: " + target2Source);
        _logger.debug("  * referenceAssociation: " + referenceAssociation);
        _logger.debug("  * includeRoot: " + includeRoot);
        codingScheme = FromOtherClasses.modifyCodingSchemeName(codingScheme);
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
            FromOtherClasses.modifyValueSetDefinitionURI(valueSetDefinitionURI);
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
        csvList.addAbsoluteCodingSchemeVersionReference(Constructors
            .createAbsoluteCodingSchemeVersionReference(codingScheme, version));
        ResolvedValueSetDefinition rvdDef =
            getValueSetDefinitionService().resolveValueSetDefinition(vsd,
                csvList, null, null);

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
                }
            }
        } else {
            _logger.error("Unable to resolveValueSetDefinition??");
        }
        return v;
    }

    public static final String EVS_SERVICE_URL =
        "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";

    public static LexEVSValueSetDefinitionServices getValueSetDefinitionService()
            throws Exception {
        // DYEE String serviceUrl =
        // DYEE AppProperties.getInstance().getProperty(
        // DYEE AppProperties.EVS_SERVICE_URL);
        String serviceUrl = EVS_SERVICE_URL;
        LexEVSDistributed distributed =
            (LexEVSDistributed) ApplicationServiceProvider
                .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
        LexEVSValueSetDefinitionServices service =
            distributed.getLexEVSValueSetDefinitionServices();
        return service;
    }

    public static class FromOtherClasses {
        public static final String SEPARATOR =
            "-------------------------------";

        public static String modifyCodingSchemeName(String codingSchemeName) {
            _logger.debug("Replaced codingSchemeName(orig): "
                + codingSchemeName);
            codingSchemeName = codingSchemeName.replaceAll(" ", "_");
            _logger.debug("Replaced codingSchemeName(curr): "
                + codingSchemeName);
            return codingSchemeName;
        }

        public static String modifyValueSetDefinitionURI(
            String valueSetDefinitionURI) {
            _logger.debug("Replaced valueSetDefinitionURI(orig): "
                + valueSetDefinitionURI);
            valueSetDefinitionURI =
                valueSetDefinitionURI.replaceAll("\\.", "_");
            valueSetDefinitionURI = valueSetDefinitionURI.replaceAll(" ", "_");
            _logger.debug("Replaced valueSetDefinitionURI(curr): "
                + valueSetDefinitionURI);
            return valueSetDefinitionURI;
        }
    }

    public static void run_cdrh_1() throws Exception {
        String codingScheme = "NCI Thesaurus";
        String version = "10.08e";
        String code = "C62596"; // CDRH Report
        boolean target2Source = true;
        String referenceAssociation = "Concept_In_Subset";
        boolean includeRoot = false;

        Vector v =
            resolveValueSet(codingScheme, version, code, target2Source,
                referenceAssociation, includeRoot);
        print(v);
    }

    public static void run_spl_1() throws Exception {
        String codingScheme = "NCI Thesaurus";
        String version = "10.08e";
        String code = "C62596"; // CDRH Report
        boolean target2Source = true;
        String referenceAssociation = "Concept_In_Subset";
        boolean includeRoot = false;

        // SPL Report:
        code = "C54452";

        Vector v =
            resolveValueSet(codingScheme, version, code, target2Source,
                referenceAssociation, includeRoot);
        print(v);
    }

    public static void run_cdrh_2() throws Exception {
        String codingScheme = "NCI Thesaurus";
        String version = "10.08e";
        String code = "C50743";
        boolean target2Source = false;
        String referenceAssociation = "Concept_In_Subset";
        boolean includeRoot = false;

        referenceAssociation = "Concept_In_Subset";
        Vector v =
            resolveValueSet(codingScheme, version, code, target2Source,
                referenceAssociation, includeRoot);
        print(v);
    }

    private static void print(Vector vector) {
        if (vector == null || vector.size() <= 0) {
            System.out.println("Empty.");
            return;
        }

        int i = 0;
        Iterator iterator = vector.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();
            System.out.println(++i + ") "
                + entity.getEntityDescription().getContent() + ":"
                + entity.getEntityCode());
        }
    }

    public static void main(String[] args) throws Exception {
        ResolveValueSetTest2.run_cdrh_2();
    }
}

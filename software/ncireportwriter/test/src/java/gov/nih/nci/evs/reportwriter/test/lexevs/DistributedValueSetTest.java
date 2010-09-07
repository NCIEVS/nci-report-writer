package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

public class DistributedValueSetTest {
    
    private static String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60";

    public static void main(String[] args) throws Exception {
        
        LexEVSDistributed distributed = 
            (LexEVSDistributed)
            ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

        LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();

        ValueSetDefinition def = new ValueSetDefinition();
        def.setDefaultCodingScheme("NCI_Thesaurus");
        def.setValueSetDefinitionName("testName");
        def.setValueSetDefinitionURI("testUri");
        
        EntityReference ref1 = new EntityReference();
        ref1.setEntityCode("C12727");
        ref1.setTransitiveClosure(false);
        ref1.setLeafOnly(false);
        ref1.setTargetToSource(true);
        
        EntityReference ref2 = new EntityReference();
        ref2.setEntityCode("C82547");
        ref2.setTransitiveClosure(true);
        ref2.setReferenceAssociation("subClassOf");
        ref2.setLeafOnly(false);
        ref2.setTargetToSource(false);
        
        DefinitionEntry entry = new DefinitionEntry();
        entry.setOperator(DefinitionOperator.OR);
        entry.setEntityReference(ref1);
        entry.setEntityReference(ref2);
        def.addDefinitionEntry(entry);
        
        AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
        csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "10.07e"));
        
        ResolvedValueSetDefinition rvsd = vds.resolveValueSetDefinition(def, csvList, null, null);
        
        ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();
//        while (itr.hasNext()) {
//            ResolvedConceptReference rcr = itr.next();
//            System.out.println(rcr.getEntityDescription().getContent());
//        }
        
        while(itr.hasNext()){
            PrintUtility.print(itr.next());
        }   
    }
}

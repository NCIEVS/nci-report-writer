/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.lexevs;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.util.PrintUtility;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

// Test Program from email sent by Kevin Peterson on Thu 11/11/2010 7:57 AM
//    Subject: "Neoplasm value set definition"
public class DistributedValueSetTest1 {
	
	private static String URL = "http://bmidev4:19280/lexevsapi60";

	public static void main(String[] args) throws Exception {
		
		LexEVSDistributed distributed = 
			(LexEVSDistributed)
			ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");

		LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();

		ValueSetDefinition def = new ValueSetDefinition();
		def.setDefaultCodingScheme("NCI_Thesaurus");
		def.setValueSetDefinitionName("testName");
		def.setValueSetDefinitionURI("testUri");
		
		def.setMappings(new Mappings());
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setLocalId("NCI_Thesaurus");
		scs.setUri("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		def.getMappings().addSupportedCodingScheme(scs);
		
		DefinitionEntry entry = new DefinitionEntry();
		entry.setRuleOrder(0l);
		entry.setOperator(DefinitionOperator.OR);

		EntityReference ref = new EntityReference();
		ref.setEntityCode("C3262");
		ref.setReferenceAssociation("subClassOf");
		ref.setTransitiveClosure(true);
		ref.setLeafOnly(false);
		ref.setTargetToSource(true);
		entry.setEntityReference(ref);
		def.addDefinitionEntry(entry);

		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "10.07e"));
		
		long time = System.currentTimeMillis();
		ResolvedValueSetDefinition rvsd = vds.resolveValueSetDefinition(def, csvList, null, null);
		
		ResolvedConceptReferencesIterator itr = rvsd.getResolvedConceptReferenceIterator();
		System.out.println("Definition Resolve Time: " + (System.currentTimeMillis() - time));
		
		int count = 0;
		
		time = System.currentTimeMillis();
		while(itr.hasNext()){
			count += itr.next(1000).getResolvedConceptReferenceCount();
		}	
		
		System.out.println("Result Return Time: " + (System.currentTimeMillis() - time));
		System.out.println("Results returned: " + count);
	}
}

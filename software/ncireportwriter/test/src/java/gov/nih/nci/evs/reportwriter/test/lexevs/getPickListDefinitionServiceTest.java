package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.evs.reportwriter.utils.RemoteServerUtil;

import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.valueSets.*;
import org.lexgrid.valuesets.*;
import org.lexgrid.valuesets.dto.ResolvedPickListEntryList;

public class getPickListDefinitionServiceTest {
    private static final String EVS_SERVICE_URL =
        "http://ncias-q532-v.nci.nih.gov:29080/lexevsapi60";

    public static LexEVSPickListDefinitionServices getPickListDefinitionService()
            throws Exception {
        LexEVSDistributed distributed =
            RemoteServerUtil.getLexEVSDistributedService(EVS_SERVICE_URL);
        LexEVSPickListDefinitionServices service =
            distributed.getLexEVSPickListDefinitionServices();
        return service;
    }

    public static void main(String[] args) throws Exception {
        String codingSchemeName = "NCI_Thesaurus";
        String version = "10.08e";

        PickListDefinition pickList = new PickListDefinition();
        pickList.setCompleteSet(false); // default value is 'false'

        PickListEntryNode plen = new PickListEntryNode();
        String pickListEntryId = "plen_id_1";
        plen.setPickListEntryId(pickListEntryId);
        PickListEntryNodeChoice plenc = new PickListEntryNodeChoice();
        PickListEntry ple = new PickListEntry();
        ple.setEntityCode("C3367");
        ple.setEntityCodeNamespace(codingSchemeName);
        ple.setEntryOrder(0L); // default value is 0
        plenc.setInclusionEntry(ple);
        plen.setPickListEntryNodeChoice(plenc);
        pickList.addPickListEntryNode(plen);

        plen = new PickListEntryNode();
        pickListEntryId = "plen_id_2";
        plen.setPickListEntryId(pickListEntryId);
        plenc = new PickListEntryNodeChoice();
        ple = new PickListEntry();
        ple.setEntityCode("C36295");
        ple.setEntityCodeNamespace(codingSchemeName);
        ple.setEntryOrder(1L);
        plenc.setInclusionEntry(ple);
        plen.setPickListEntryNodeChoice(plenc);
        pickList.addPickListEntryNode(plen);

        try {
            LexEVSPickListDefinitionServices service =
                getPickListDefinitionService();
            boolean sortByText = true;
            String versionTag = null;
            AbsoluteCodingSchemeVersionReferenceList csVersionList =
                new AbsoluteCodingSchemeVersionReferenceList();
            csVersionList.addAbsoluteCodingSchemeVersionReference(Constructors
                .createAbsoluteCodingSchemeVersionReference(codingSchemeName,
                    version));
            ResolvedPickListEntryList list =
                service.resolvePickList(pickList, sortByText, csVersionList,
                    versionTag);
            
            int count = list.getResolvedPickListEntryCount();
            System.out.println("list == " + count);
            System.out.println("Status: " + (count == 2 ? "Successful" : "Failed"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

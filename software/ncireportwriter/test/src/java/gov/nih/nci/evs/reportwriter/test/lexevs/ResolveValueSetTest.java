package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;

public class ResolveValueSetTest {
    public static void run() throws Exception {
        String codingScheme = "NCI Thesaurus";
        String version = "10.08e";
        String code = "C62596";
        boolean target2Source = true;
        String referenceAssociation = "Concept_In_Subset";
        boolean includeRoot = false;
        DataUtils.resolveValueSet(codingScheme, version, code, target2Source,
            referenceAssociation, includeRoot);
    }

    public static void main(String[] args) throws Exception {
        args = SetupEnv.getInstance().parse(args);
        ResolveValueSetTest.run();
    }
}

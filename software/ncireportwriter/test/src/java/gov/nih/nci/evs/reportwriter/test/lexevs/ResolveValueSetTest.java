/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.lexevs;

import java.util.Iterator;
import java.util.Vector;

import org.LexGrid.concepts.Entity;
import org.lexgrid.valuesets.*;

import gov.nih.nci.evs.reportwriter.test.utils.*;
import gov.nih.nci.evs.reportwriter.utils.*;

public class ResolveValueSetTest {
    public static void run() throws Exception {
        LexEVSValueSetDefinitionServices definitionServices =
            DataUtils.getValueSetDefinitionService();
        String codingScheme = "NCI Thesaurus";
        String version = "10.08e";
        String uri = DataUtils.codingSchemeName2URI(codingScheme, version);
        String code = "C62596";
        boolean target2Source = true;
        String referenceAssociation = "Concept_In_Subset";
        boolean includeRoot = false;
        
        code = "C50743";
        target2Source = false;
        includeRoot = false;
        System.out.println("--------------------------------------------------");
        Vector v = DataUtils.resolveValueSet(definitionServices, uri, codingScheme,
            version, code, target2Source,
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
        args = SetupEnv.getInstance().parse(args);
        while (true) {
            ResolveValueSetTest.run();
        }
    }
}

/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.test.lexevs;

import gov.nih.nci.evs.reportwriter.test.utils.SetupEnv;
import gov.nih.nci.evs.reportwriter.utils.DataUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.concepts.Entity;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;

public class RestrictToMatchingProperty
{
    private final static String SEPARATOR = 
        "----------------------------------------" +
        "----------------------------------------";
    
    //private LexEVSValueSetDefinitionServices vds_ = null;

    private static LexEVSValueSetDefinitionServices getValueSetDefinitionService()
        throws Exception {

        //DYEE: String URL = RemoteServerUtil.getServiceURL();
        String URL = "http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60"; //DYEE
        System.out.println("Connecting to " + URL);
        LexEVSDistributed distributed =
            (LexEVSDistributed)
            ApplicationServiceProvider.getApplicationServiceFromUrl(URL, "EvsServiceInfo");
        LexEVSValueSetDefinitionServices vds = distributed.getLexEVSValueSetDefinitionServices();

        return vds;
    }


    public static Vector<org.LexGrid.concepts.Entity> restrictToMatchingProperty(
       String codingSchemeName, String version, LocalNameList propertyList,
       CodedNodeSet.PropertyType[] propertyTypes, LocalNameList sourceList,
       NameAndValueList qualifierList, java.lang.String matchText,
       java.lang.String matchAlgorithm, java.lang.String language,
       int maxToReturn) {
       //CodedNodeSet cns = null;
       Vector<org.LexGrid.concepts.Entity> v =
           new Vector<org.LexGrid.concepts.Entity>();

        ValueSetDefinition vsd = new ValueSetDefinition();
        String valueSetDefinitionURI = codingSchemeName + "_" + matchText + "_" + matchAlgorithm;
        if (version != null) {
            valueSetDefinitionURI = codingSchemeName + "_" + version + "_" + matchText + "_" + matchAlgorithm;
        }
        try {
            vsd.setValueSetDefinitionURI(valueSetDefinitionURI);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: setValueSetDefinitionURI throws exceptions. ");
            return null;
        }

        String valueSetDefinitionName = valueSetDefinitionURI;
        vsd.setValueSetDefinitionName(valueSetDefinitionName);
        vsd.setDefaultCodingScheme(codingSchemeName);

        DefinitionEntry definitionEntry = new DefinitionEntry();
        definitionEntry.setRuleOrder(new java.lang.Long(1));
        definitionEntry.setOperator(DefinitionOperator.OR);

        //org.LexGrid.valueSets.PropertyReference _propertyReference
        PropertyReference _propertyReference = new PropertyReference();
        _propertyReference.setCodingScheme(codingSchemeName);
        String propertyName = null;
        if (propertyList != null && propertyList.getEntryCount() > 0) {
            propertyName = propertyList.getEntry(0);
            _propertyReference.setPropertyName(propertyName);
        }
        PropertyMatchValue _propertyMatchValue = new PropertyMatchValue();
        _propertyMatchValue.setMatchAlgorithm(matchAlgorithm);
        _propertyMatchValue.setContent(matchText);
        _propertyReference.setPropertyMatchValue(_propertyMatchValue);
        definitionEntry.setPropertyReference(_propertyReference);
        vsd.addDefinitionEntry(definitionEntry);

        try {
            AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
            csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference(codingSchemeName, version));
            ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(vsd, csvList, null, null);

            if (rvdDef != null) {
                //Set<String> codes = new HashSet<String>();
                while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
                {
                    ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
                    Entity concept = rcr.getReferencedEntry();
                    if (concept == null) {
                        System.out.println("rcr.getReferencedEntry() returns NULL");
                    } else {
                        v.add(concept);
                    }
                }
            } else {
                System.out.println("Unable to resolveValueSetDefinition??");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return v;
    }


    public static void run() {
       Vector<org.LexGrid.concepts.Entity> v = null;

       String codingSchemeName = "NCI_Thesaurus";
       String version = "10.07e";
       LocalNameList propertyList = null;
       CodedNodeSet.PropertyType[] propertyTypes = null;
       LocalNameList sourceList = null;
       NameAndValueList qualifierList = null;
       String matchText = "Finding";
       String matchAlgorithm = "contains";
       String language = null;
       int maxToReturn = -1;
       
       boolean test = true;
       if (test) {
           propertyList = new LocalNameList();
           propertyList.addEntry("Semantic_Type");
           //matchText = "Geographic Area (label: Semantic_Type)";
           matchText = "Geographic Area";
           matchAlgorithm = "exactMatch";
       }

       long ms = System.currentTimeMillis();

       //v = restrictToMatchingProperty(
       v = DataUtils.restrictToMatchingProperty(
               codingSchemeName, version, propertyList,
               propertyTypes, sourceList,
               qualifierList,  matchText,
               matchAlgorithm, language,
               maxToReturn);
       
       System.out.println(SEPARATOR);
       long delay = System.currentTimeMillis() - ms;
       System.out.println("Run time (ms): " + delay + " milli-seconds.");
       int size = v != null ? v.size() : -1;
       System.out.println("Number of matches: " + size);
    }
    

    public static void main(String[] args)
    {
        args = SetupEnv.getInstance().parse(args);
        RestrictToMatchingProperty.run();
    }
}

/*
Connecting to http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60
Run time (ms): 50666 milli-seconds.
Number of matches: 2332
*/
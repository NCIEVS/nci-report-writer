package gov.nih.nci.evs.reportwriter.test.lexevs;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.options.BooleanOption;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;



import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Extensions.Export.LexGrid_Exporter;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.loaders.MessageDirector;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.annotations.LgClientSideSafe;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
//import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.admin.Util;
import org.lexgrid.valuesets.dto.ResolvedPickListEntry;
import org.lexgrid.valuesets.dto.ResolvedPickListEntryList;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.helper.PLEntryNodeSortUtil;
import org.lexgrid.valuesets.helper.VSDServiceHelper;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import gov.nih.nci.evs.reportwriter.test.utils.SetupEnv;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
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

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;


public class RestrictToMatchingProperty
{
    public LexEVSValueSetDefinitionServices vds_ = null;

    public RestrictToMatchingProperty()
    {

    }

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
       CodedNodeSet cns = null;
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
                Set<String> codes = new HashSet<String>();
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

       long ms = System.currentTimeMillis();

       //DYEE: v = restrictToMatchingProperty(
       v = gov.nih.nci.evs.reportwriter.utils.DataUtils.restrictToMatchingProperty(
               codingSchemeName, version, propertyList,
               propertyTypes, sourceList,
               qualifierList,  matchText,
               matchAlgorithm, language,
               maxToReturn);

       long delay = System.currentTimeMillis() - ms;
       System.out.println("Run time (ms): " + delay + " milli-seconds.");
       System.out.println("Number of matches: " + v.size());
    }



    public static void main(String[] args)
    {
        args = SetupEnv.getInstance().parse(args);
        Test test = new Test();
        test.run();
    }
}

/*
Connecting to http://ncias-d488-v.nci.nih.gov:29080/lexevsapi60
Run time (ms): 50666 milli-seconds.
Number of matches: 2332
*/
package gov.nih.nci.evs.reportwriter.bean;

import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;

import java.util.ArrayList;
import java.util.List;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.ListDataModel;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.DataModel.Core.*;

import org.LexGrid.concepts.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.versions.*;

import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.concepts.Instruction;

import org.LexGrid.LexBIG.DataModel.Collections.*;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.relations.Relations;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;

import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;

import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

import org.LexGrid.LexBIG.Exceptions.LBException;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.Constructors;


import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.History.NCIThesaurusHistoryServiceImpl;

import org.LexGrid.LexBIG.DataModel.NCIHistory.*;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;

import org.LexGrid.LexBIG.DataModel.NCIHistory.types.ChangeType;

import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;


import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;

import org.LexGrid.versions.SystemRelease;

import org.LexGrid.codingSchemes.CodingSchemeVersion;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;

import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;

import org.LexGrid.naming.SupportedPropertyQualifier;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;

import org.LexGrid.codingSchemes.Mappings;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

import org.apache.log4j.*;
import org.apache.log4j.xml.*;

import org.LexGrid.commonTypes.PropertyQualifier;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;

import java.util.Vector;
import java.util.HashMap;

import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;

import java.util.Set;

import java.text.NumberFormat;


import javax.faces.model.SelectItem;

import gov.nih.nci.evs.reportwriter.utils.*;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2008 NGIT. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by NGIT and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "NGIT" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or NGIT
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */

/**
  * @author EVS Team
  * @version 1.0
 */


public class OntologyBean //extends BaseBean
{
	private static List _ontologies = null;

	private org.LexGrid.LexBIG.LexBIGService.LexBIGService lbSvc = null;
	public org.LexGrid.LexBIG.Utility.ConvenienceMethods lbConvMethods = null;
    public CodingSchemeRenderingList csrl = null;
    private Vector supportedCodingSchemes = null;
    private HashMap codingSchemeMap = null;
    private Vector codingSchemes = null;

    private String selectedOntology = null;

    private List associationList = null;
    private String selectedAssociation = null;


    //public String selectedOntology;

// Initialization
/*
  protected void init() {
      initializeOntologies();
  }
*/
  public void setSelectedOntology(String selectedOntology)
  {
	   this.selectedOntology = selectedOntology;
  }

  public String getSelectedOntology() {
	  return this.selectedOntology;
  }

  public void setSelectedAssociation(String selectedAssociation)
  {
	   this.selectedAssociation = selectedAssociation;
  }

  public String getSelectedAssociation() {
	  return this.selectedAssociation;
  }


      public List getOntologyList()
      {
		  _ontologies = DataUtils.getOntologyList();
		  if (_ontologies != null && _ontologies.size() > 0)
		  {
			  for (int i=0; i<_ontologies.size(); i++)
			  {
				  SelectItem item = (SelectItem) _ontologies.get(i);
				  String key = item.getLabel();
				  if (key.indexOf("NCI Thesaurus") != -1 || key.indexOf("NCI_Thesaurus") != -1)
				  {
				  	   selectedOntology = key;
				  	   break;
				  }
			  }
			  associationList = getAssociationList();
		  }
		  return _ontologies;
	  }


	public static void sortArray(String[] strArray) {
	    String tmp;
	    if (strArray.length <= 1) return;
	    for (int i = 0; i < strArray.length; i++) {
	       for (int j = i + 1; j < strArray.length; j++) {
	  	      if(strArray[i].compareToIgnoreCase(strArray[j] ) > 0 ) {
			     tmp = strArray[i];
			     strArray[i] = strArray[j];
			     strArray[j] = tmp;
			  }
		   }
		}
	}

    public String[] getSortedKeys(HashMap map)
    {
		if (map == null) return null;
	    Set keyset = map.keySet();
		String[] names = new String[keyset.size()];
		Iterator it = keyset.iterator();
		int i = 0;
		while (it.hasNext())
		{
			String s = (String) it.next();
			names[i] = s;
			i++;
		}
		sortArray(names);
		return names;
	}


	  public void ontologySelectionChanged(ValueChangeEvent vce) {
		  String newValue = (String) vce.getNewValue();
          setSelectedOntology(newValue);
          associationList = getAssociationList();
	  }


      public List getAssociationList() {

		  if (selectedOntology == null)
		  {
			  _ontologies = getOntologyList();
		  }

		  System.out.println("************************************** selectedOntology " + selectedOntology);


		  if (associationList == null)
		  {
			  associationList = new ArrayList();
			  Vector associationNames = DataUtils.getSupportedAssociationNames(selectedOntology);
			  if (associationNames != null)
			  {
				  for (int i=0; i<associationNames.size(); i++)
				  {
					  String name = (String) associationNames.elementAt(i);
					  associationList.add(new SelectItem(name));
				  }
				  if (associationList != null && associationList.size() > 0)
				  {
					  SelectItem item = (SelectItem) associationList.get(0);
					  setSelectedAssociation(item.getLabel());
				  }
		      }
		  }
		  return associationList;
	  }
}

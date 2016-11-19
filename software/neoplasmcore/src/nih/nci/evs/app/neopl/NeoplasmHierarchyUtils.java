package gov.nih.nci.evs.app.neopl;

import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.browser.common.*;

import java.io.*;
import java.util.*;
import java.text.*;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;

import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.naming.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;

import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;

import org.LexGrid.LexBIG.Extensions.Generic.SupplementExtension;
import org.LexGrid.relations.Relations;
import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;

/**
 * @author EVS Team
 * @version 1.0
 *
 *      Modification history Initial implementation kim.ong@ngc.com
 *
 */


/**
 * The Class NeoplasmHierarchyUtils.
 */


public class NeoplasmHierarchyUtils {
	LexBIGService lbSvc = null;

	static String NEOPLASM = "Neoplasm"; //Neoplasm (Code C3262)
	static String NEOPLASM_BY_SITE = "Neoplasm by Site"; //Neoplasm by Site (Code C3263)
	static String NEOPLASM_BY_MORPHOLOGY = "Neoplasm by Morphology"; //Neoplasm by Morphology (Code C4741)

	static String NEOPLASM_CODE = "C3262"; //Neoplasm (Code C3262)
	static String NEOPLASM_BY_SITE_CODE = "C3263"; //Neoplasm by Site (Code C3263)
	static String NEOPLASM_BY_MORPHOLOGY_CODE = "C4741"; //Neoplasm by Morphology (Code C4741)

    static Vector NEOPLASM_ROOTS = null;
    static Vector NEOPLASM_BY_SITE_ROOTS = null;
    static Vector NEOPLASM_BY_MORPHOLOGY_ROOTS = null;

    static HashMap NEOPLASM_ROOT_CODE_MAP = null;

    private static String serviceUrl = null;

    static {
		serviceUrl = ServiceTestCase.serviceUrl;
		NEOPLASM_ROOT_CODE_MAP = new HashMap();
		String codingSchemeName = "NCI_Thesaurus";
        LexBIGService lbSvc = RemoteServerUtil.createLexBIGService(serviceUrl);
        String vers = null;
        String ns = codingSchemeName;
        boolean use_ns = true;
        ConceptDetails cd = new ConceptDetails(lbSvc);
        Entity entity = cd.getConceptByCode(codingSchemeName, vers, NEOPLASM_BY_SITE_CODE, ns, use_ns);
        System.out.println(entity.getEntityDescription().getContent() + " (" + entity.getEntityCode() + ")");

        entity = cd.getConceptByCode(codingSchemeName, vers, NEOPLASM_BY_MORPHOLOGY_CODE, ns, use_ns);
        System.out.println(entity.getEntityDescription().getContent() + " (" + entity.getEntityCode() + ")");


        TreeUtils treeUtils = new TreeUtils(lbSvc);
        NEOPLASM_ROOTS = new Vector();
        NEOPLASM_BY_SITE_ROOTS = new Vector();
        NEOPLASM_BY_MORPHOLOGY_ROOTS = new Vector();

        String code = NEOPLASM_BY_SITE_CODE;
        HashMap hmap = treeUtils.getSubconcepts(codingSchemeName, vers, code, ns);
		if (hmap != null) {
			TreeItem ti = (TreeItem) hmap.get(code);
			if (ti != null) {
				for (String association : ti._assocToChildMap.keySet()) {
					List<TreeItem> children =
						ti._assocToChildMap.get(association);
					for (TreeItem childItem : children) {
						String t = childItem._text + "|" + childItem._code;
						NEOPLASM_BY_SITE_ROOTS.add(t);
						NEOPLASM_ROOTS.add(NEOPLASM_BY_SITE + "|R_1|" + t);
					}
				}
			}
		}
		NEOPLASM_BY_SITE_ROOTS = SortUtils.quickSort(NEOPLASM_BY_SITE_ROOTS);
		for (int i=0; i<NEOPLASM_BY_SITE_ROOTS.size(); i++) {
			int j = i+1;
			String t = (String) NEOPLASM_BY_SITE_ROOTS.elementAt(i);
			//System.out.println("(" + j + ") " + t);
		}

        System.out.println("\n");
        code = NEOPLASM_BY_MORPHOLOGY_CODE;
        hmap = treeUtils.getSubconcepts(codingSchemeName, vers, code, ns);
		if (hmap != null) {
			TreeItem ti = (TreeItem) hmap.get(code);
			if (ti != null) {
				for (String association : ti._assocToChildMap.keySet()) {
					List<TreeItem> children =
						ti._assocToChildMap.get(association);
					for (TreeItem childItem : children) {
						String t = childItem._text + "|" + childItem._code;
						NEOPLASM_BY_MORPHOLOGY_ROOTS.add(t);
						NEOPLASM_ROOTS.add(NEOPLASM_BY_MORPHOLOGY + "|R_2|" + t);
					}
				}
			}
		}
		NEOPLASM_BY_MORPHOLOGY_ROOTS = SortUtils.quickSort(NEOPLASM_BY_MORPHOLOGY_ROOTS);
		for (int i=0; i<NEOPLASM_BY_MORPHOLOGY_ROOTS.size(); i++) {
			int j = i+1;
			String t = (String) NEOPLASM_BY_MORPHOLOGY_ROOTS.elementAt(i);
			//System.out.println("(" + j + ") " + t);
		}

		NEOPLASM_ROOT_CODE_MAP.put("R_1", NEOPLASM_BY_SITE_CODE);
		NEOPLASM_ROOT_CODE_MAP.put("R_2", NEOPLASM_BY_MORPHOLOGY_CODE);

		System.out.println("\n");
		//NEOPLASM_ROOTS = SortUtils.quickSort(NEOPLASM_ROOTS);
		for (int i=0; i<NEOPLASM_ROOTS.size(); i++) {
			int j = i+1;
			String t = (String) NEOPLASM_ROOTS.elementAt(i);
			System.out.println("(" + j + ") " + t);
		}


	}

	public NeoplasmHierarchyUtils(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

	public void setLexBIGService(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

    public static void main(String[] args) {

	}


}

/*
Disease, Disorder or Finding
	Disease or Disorder
		Neoplasm
			Neoplasm by Special Category
				Embryonal Neoplasm
					Intraocular Medulloepithelioma
						Benign Intraocular Medulloepithelioma (Code C66807)


Disease, Disorder or Finding
	Disease or Disorder
		Neoplasm
			Neoplasm by Special Category
				Embryonal Neoplasm
					Ewing Sarcoma/Peripheral Primitive Neuroectodermal Tumor (Code C27291)



Disease, Disorder or Finding
	Disease or Disorder
		Neoplasm
			Neoplasm by Special Category
				Embryonal Neoplasm
					Rhabdoid Tumor (Code C3808)



Neoplasm by Special Category (Code C7062)
        Embryonal Neoplasm (Code C3264)


Concepts in Tree but not in value set:
	Endocrine Neoplasm (C3010)
	Thoracic Neoplasm (C3406)
	Cardiovascular Neoplasm (C4784)
	Retinal Cell Neoplasm (C7061)
	Neoplastic Polyp (C7068)
	Giant Cell Neoplasm (C7069)
	Mesenchymal Cell Neoplasm (C7059)
	Urinary System Neoplasm (C3431)
	Reproductive System Neoplasm (C3674)
	Hematopoietic and Lymphoid System Neoplasm (C35813)
	Melanocytic Neoplasm (C7058)
	Neoplasm of Uncertain Histogenesis (C6974)
	Meningothelial Cell Neoplasm (C6971)
	Connective and Soft Tissue Neoplasm (C3810)
	Mixed Neoplasm (C6930)
	Respiratory Tract Neoplasm (C3355)
	Skin Neoplasm (C3372)
	Neuroepithelial, Perineurial, and Schwann Cell Neoplasm (C35562)
	Neoplasm by Morphology (R_2)
	Neoplasm by Site (R_1)
	Eye Neoplasm (C3030)
	Breast Neoplasm (C2910)
	Digestive System Neoplasm (C3052)
	Nervous System Neoplasm (C3268)
	Mesothelial Neoplasm (C3786)
	Head and Neck Neoplasm (C3077)
	Peritoneal and Retroperitoneal Neoplasms (C7337)
	Hematopoietic and Lymphoid Cell Neoplasm (C27134)
	Epithelial Neoplasm (C3709)


Neoplasm by Morphology (Code C4741)
Neoplasm by Site (Code C3263)


Neoplasm by Site|R_1|Breast Neoplasm|C2910
Neoplasm by Site|R_1|Cardiovascular Neoplasm|C4784
Neoplasm by Site|R_1|Connective and Soft Tissue Neoplasm|C3810
Neoplasm by Site|R_1|Digestive System Neoplasm|C3052
Neoplasm by Site|R_1|Endocrine Neoplasm|C3010
Neoplasm by Site|R_1|Eye Neoplasm|C3030
Neoplasm by Site|R_1|Head and Neck Neoplasm|C3077
Neoplasm by Site|R_1|Hematopoietic and Lymphoid System Neoplasm|C35813
Neoplasm by Site|R_1|Nervous System Neoplasm|C3268
Neoplasm by Site|R_1|Peritoneal and Retroperitoneal Neoplasms|C7337
Neoplasm by Site|R_1|Reproductive System Neoplasm|C3674
Neoplasm by Site|R_1|Respiratory Tract Neoplasm|C3355
Neoplasm by Site|R_1|Skin Neoplasm|C3372
Neoplasm by Site|R_1|Thoracic Neoplasm|C3406
Neoplasm by Site|R_1|Urinary System Neoplasm|C3431
Neoplasm by Morphology|R_2|Epithelial Neoplasm|C3709
Neoplasm by Morphology|R_2|Germ Cell Tumor|C3708
Neoplasm by Morphology|R_2|Giant Cell Neoplasm|C7069
Neoplasm by Morphology|R_2|Hematopoietic and Lymphoid Cell Neoplasm|C27134
Neoplasm by Morphology|R_2|Melanocytic Neoplasm|C7058
Neoplasm by Morphology|R_2|Meningothelial Cell Neoplasm|C6971
Neoplasm by Morphology|R_2|Mesenchymal Cell Neoplasm|C7059
Neoplasm by Morphology|R_2|Mesothelial Neoplasm|C3786
Neoplasm by Morphology|R_2|Mixed Neoplasm|C6930
Neoplasm by Morphology|R_2|Neoplasm of Uncertain Histogenesis|C6974
Neoplasm by Morphology|R_2|Neoplastic Polyp|C7068
Neoplasm by Morphology|R_2|Neuroepithelial, Perineurial, and Schwann Cell Neoplasm|C35562
Neoplasm by Morphology|R_2|Retinal Cell Neoplasm|C7061
Neoplasm by Morphology|R_2|Trophoblastic Tumor|C3422



Neoplasm by Site (Code C3263)
 Breast Neoplasm
 Cardiovascular Neoplasm
 Connective and Soft Tissue Neoplasm
 Digestive System Neoplasm
 Endocrine Neoplasm
 Eye Neoplasm
 Head and Neck Neoplasm
 Hematopoietic and Lymphoid System Neoplasm
 Nervous System Neoplasm
 Peritoneal and Retroperitoneal Neoplasms
 Reproductive System Neoplasm
 Respiratory Tract Neoplasm
 Skin Neoplasm
 Thoracic Neoplasm
 Urinary System Neoplasm

Neoplasm by Morphology (Code C4741)
 Epithelial Neoplasm
 Germ Cell Tumor
 Giant Cell Neoplasm
 Hematopoietic and Lymphoid Cell Neoplasm
 Melanocytic Neoplasm
 Meningothelial Cell Neoplasm
 Mesenchymal Cell Neoplasm
 Mesothelial Neoplasm
 Mixed Neoplasm
 Neoplasm of Uncertain Histogenesis
 Neoplastic Polyp
 Neuroepithelial, Perineurial, and Schwann Cell Neoplasm
 Retinal Cell Neoplasm
 Trophoblastic Tumor

*/


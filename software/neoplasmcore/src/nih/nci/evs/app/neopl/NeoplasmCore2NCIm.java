package gov.nih.nci.evs.app.neopl;


import gov.nih.nci.evs.browser.common.*;
import gov.nih.nci.evs.browser.utils.*;
import gov.nih.nci.evs.security.SecurityToken;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import java.io.*;
import java.text.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lexevs.tree.dao.iterator.ChildTreeNodeIterator;
import org.lexevs.tree.json.JsonConverter;
import org.lexevs.tree.json.JsonConverterFactory;
import org.lexevs.tree.model.LexEvsTree;
import org.lexevs.tree.model.LexEvsTreeNode.ExpandableStatus;
import org.lexevs.tree.model.LexEvsTreeNode;
import org.lexevs.tree.service.TreeService;
import org.lexevs.tree.service.TreeServiceFactory;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.LexBIG.caCore.interfaces.LexEVSDistributed;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Extensions.Generic.LexBIGServiceConvenienceMethods.*;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.*;


/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008-2016 NGIS. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIS and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIS" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIS
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIS, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team
 * @version 1.0
 *
 * Modification history:
 *     Initial implementation kim.ong@ngc.com
 *
 */


public class NeoplasmCore2NCIm { //extends ServiceTestCase {

	private static String[] HEADINGS = new String[] {
		"NCIt Code",
		"NCIt Preferred Term",
		"NCIm CUI",
		"NCIm Preferred Name",
		"NCIm Source",
		"Term Type",
		"Source Code",
        "Source Term"
	};


	LexBIGService lbSvc = null;
	ConceptDetails conceptDetails = null;
	MetathesaurusUtils metathesaurusUtils = null;
	EntityExporter entityExporter = null;
	CodingSchemeDataUtils codingSchemeDataUtils = null;
	Vector valueSetCodes = null;
	HashMap code2NameHashMap = null;

	public NeoplasmCore2NCIm(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
		conceptDetails = new ConceptDetails(lbSvc);
		metathesaurusUtils = new MetathesaurusUtils(lbSvc);
		entityExporter = new EntityExporter(lbSvc);
		codingSchemeDataUtils = new CodingSchemeDataUtils(lbSvc);
	}

	public void setCode2NameHashMap(HashMap hmap) {
		this.code2NameHashMap = hmap;
	}

    public String getCode(String csvLine) {
		int n = csvLine.indexOf("\",");
		String code = csvLine.substring(1, n);
		return code;
	}

    public String getCodeAndName(String csvLine) {
		int n = csvLine.indexOf("\",");
		String code = csvLine.substring(1, n);
		csvLine = csvLine.substring(n+2, csvLine.length());
		n = csvLine.indexOf("\",");
		String name = csvLine.substring(1, n);
		return code + "|" + name;
	}

    public Vector getPresentationProperties(Entity node) {
        if (node == null) return null;
        Vector w = new Vector();

        String term = null;
        String term_type = null;
        String source_code = null;
        String source = null;
        String pf = null;
        String src_code = null;
        boolean has_non_ncit_src = false;

		Presentation[] presentations = node.getPresentation();

		for (int i = 0; i < presentations.length; i++) {
			 term = null;
			 term_type = null;
			 source_code = null;
			 source = null;
			 pf = null;
			 Presentation presentation = presentations[i];
			 //StringBuffer buf = new StringBuffer();
			 term = presentation.getValue().getContent();
			 //buf.append(presentation.getValue().getContent());
			 pf = presentation.getRepresentationalForm();
			 //buf.append("|").append(pf);
			 //String term_and_type = buf.toString();

			 PropertyQualifier[] qualifiers = presentation.getPropertyQualifier();
			 if (qualifiers.length > 0) {
				 boolean has_source_code = false;
				 for (int k=0; k<qualifiers.length; k++) {
					  PropertyQualifier qualifier = qualifiers[k];
					  if (qualifier.getPropertyQualifierName().compareTo("source-code") == 0) {
						  has_source_code = true;
						  source_code = qualifier.getValue().getContent();
						  Source[] sources = presentation.getSource();
						  if (sources.length > 0) {
							  for (int k2=0; k2<sources.length; k2++) {
								  Source src = sources[k2];
								  source = src.getContent();
								  String t = source + "|" + pf + "|" + source_code + "|" + term;
								  if (source.compareTo("NCI") != 0) {
									  has_non_ncit_src = true;
								  }
								  w.add(t);
							  }
						  } else {
							  String t = source + "|" + pf + "|" + source_code + "|" + term;
							  w.add(t);
						  }
					  }
				 }
				 if (!has_source_code) {
					  String t = source + "|" + pf + "|" + source_code + "|" + term;
					  w.add(t);
				 }
			 } else {
				 String t = source + "|" + pf + "|" + source_code + "|" + term;
				 w.add(t);
			 }
		}
		if (!has_non_ncit_src) {
			 w = new Vector();
			 String t = " " + "|" + " " + "|" + " " + "| ";
			 w.add(t);
		}
		return w;
	}


    public void dumpProperty(Property property) {
		System.out.println("\n" + property.getPropertyType());
		System.out.println(property.getPropertyName() + ": " + property.getValue().getContent());

		PropertyQualifier[] qualifiers = property.getPropertyQualifier();
		if (qualifiers != null) {
			System.out.println("Property Qualifiers: " );
			for (int i=0; i<qualifiers.length; i++) {
				PropertyQualifier qualifier = qualifiers[i];
				System.out.println("\t" + qualifier.getPropertyQualifierName() + ": " + qualifier.getValue().getContent());
			}
		}
		Source[] sources = property.getSource();

		if (sources != null) {
			System.out.println("Sources: " );
			for (int i=0; i<sources.length; i++) {
				Source source = sources[i];
				System.out.println("\t" + source.getContent());
			}
	    }
	    if (property instanceof Presentation) {
			Presentation presentation = (Presentation) property;
			if (presentation.getRepresentationalForm() != null) {
				System.out.println("RepresentationalForm: " + presentation.getRepresentationalForm());
		    }
		}
	}

    public Vector getMappingData(Vector codes) {
		Vector w = new Vector();
		String scheme = "NCI_Thesaurus";
		String version = codingSchemeDataUtils.getVocabularyVersionByTag(scheme, "PRODUCTION");
		String ncim_scheme = "NCI Metathesaurus";
		String ncim_version = codingSchemeDataUtils.getVocabularyVersionByTag(ncim_scheme, "PRODUCTION");
		String ltag = null;
		String ncit_name = null;
		long ms = System.currentTimeMillis();

		int lcv = 0;
		for (int i=0; i<codes.size(); i++) {
			String code = (String) codes.elementAt(i);
			if (code2NameHashMap != null) {
				ncit_name = (String) code2NameHashMap.get(code);
			} else {
				Entity ncit_entity = conceptDetails.getConceptByCode(scheme, version, code);
				ncit_name = ncit_entity.getEntityDescription().getContent();
			}
            Vector cuis = metathesaurusUtils.getMatchedMetathesaurusCUIs(scheme, version, ltag, code);
		    if (cuis != null && cuis.size() > 0) {
				for (int k=0; k<cuis.size(); k++) {
					String cui = (String) cuis.elementAt(k);
					Entity ncim_entity = conceptDetails.getConceptByCode(ncim_scheme, ncim_version, cui);
					String ncim_name = ncim_entity.getEntityDescription().getContent();
					Vector w2 = getPresentationProperties(ncim_entity);
					for (int k2=0; k2<w2.size(); k2++) {
						String t = (String) w2.elementAt(k2);
						String s = code + "|" + ncit_name + "|" + cui + "|" + ncim_name + "|" + t;
						//System.out.println("\t" + s);
						w.add(s);
					}
					if (w2.size() == 0) {
						String s = code + "|" + ncit_name + "|" + cui + "|" + ncim_name + "| | | | |";
						//System.out.println("\t" + s);
						w.add(s);
					}
				}
		    }
		    lcv++;
		    if ((lcv / 100) * 100 == lcv || lcv == codes.size()) {
				System.out.println("" + lcv + " out of " + codes.size() + " completed in " + (System.currentTimeMillis() - ms) + " (ms).");
			}
	    }
	    System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
	    return w;
    }


    public void run(PrintWriter pw, Vector codes) {
		String scheme = "NCI_Thesaurus";
		String version = codingSchemeDataUtils.getVocabularyVersionByTag(scheme, "PRODUCTION");
		//String version = "16.04d";
		String ncim_scheme = "NCI Metathesaurus";
		//String ncim_version = "2.6";
		String ncim_version = codingSchemeDataUtils.getVocabularyVersionByTag(ncim_scheme, "PRODUCTION");
		String ltag = null;
		long ms = System.currentTimeMillis();
		//for (int i=0; i<10; i++) {
		for (int i=0; i<codes.size(); i++) {
			String line = (String) codes.elementAt(i);
			Vector u = StringUtils.parseData(line);
			String code = (String) u.elementAt(0);
			String name = (String) u.elementAt(1);
			int j = i+1;
			//System.out.println("(" + j + ") " + name + " (" + code + ")");
			pw.println("(" + j + ") " + name + " (" + code + ")");
            Vector cuis = metathesaurusUtils.getMatchedMetathesaurusCUIs(scheme, version, ltag, code);
            //for (int k=0; k<cuis.size(); k++) {
		    if (cuis != null && cuis.size() > 0) {
				for (int k=0; k<cuis.size(); k++) {
					String cui = (String) cuis.elementAt(k);
					pw.println("CUI: " + cui);
					Entity ncim_entity = conceptDetails.getConceptByCode(ncim_scheme, ncim_version, cui);
					try {
						entityExporter.printProperties(pw, ncim_entity);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
		    }
		    pw.println("\n");
	    }
	    System.out.println("Total run time (ms): " + (System.currentTimeMillis() - ms));
    }



    public Vector getMatchedMetathesaurusCUIs(Entity c) {
        if (c != null) {
            Vector v = conceptDetails.getConceptPropertyValues(c, "NCI_META_CUI");
            if (v == null || v.size() == 0) {
				return conceptDetails.getConceptPropertyValues(c, "UMLS_CUI");
			}
        }
        return null;
    }


/////////////////////////////////////////////////////////////////////////////////////////
    public Vector loadValueSetData(String csvFile) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		Vector v = new Vector();
		valueSetCodes = new Vector();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			int k = 0;
			while ((line = br.readLine()) != null) {
                String code = getCode(line);
				String codeName = getCodeAndName(line);
				valueSetCodes.add(code);
				v.add(codeName);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return v;
	}

	public static void dumpVector(String label, Vector v) {
		System.out.println(label);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}
	}

/*
	public static void main(String[] args) {
		String value_set_ascii_file = args[0];
		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		NeoplasmCore2NCIm NeoplasmCore2NCIm = new NeoplasmCore2NCIm(lbSvc);
		PrintWriter pw = null;
		String outputfile = "NeoplasmCore2NCIm.txt";
		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			Vector valueSetCodes = NeoplasmCore2NCIm.loadValueSetData(value_set_ascii_file);
			dumpVector("valueSetCodes", valueSetCodes);
            NeoplasmCore2NCIm.run(pw, valueSetCodes);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
				System.out.println("Output file " + outputfile + " generated.");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
*/

    public static void main(String[] args) {
        String csvfile = args[0];
        String outputfile = args[1];
		//String csvfile = "Neoplasm_Core__16.07d.csv";
		int column_index = 0;
		Vector codes = ExcelToCSV.extractColumnDataFromCSV(csvfile, column_index);
		System.out.println("codes.size(): " + codes.size());
		for (int i=0; i<codes.size(); i++) {
			String t = (String) codes.elementAt(i);
			int j = i+1;
			System.out.println("(" + j + ") " + t);
		}

		LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
		NeoplasmCore2NCIm test = new NeoplasmCore2NCIm(lbSvc);
		Vector v = test.getMappingData(codes);
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			System.out.println(t);
		}

        int[] sort_options = new int[4];
        sort_options[0] = 1;
        sort_options[1] = 2;
        sort_options[2] = 4;
        sort_options[3] = 7;
		String delim = "|";
		v = CSVSortUtils.quickSort(v, sort_options, delim);
		CSVtoExcel csvtoExcel = new CSVtoExcel();
	    csvtoExcel.setHeadings(CSVtoExcel.HEADINGS);
	    int[] codeColumn = new int[8];
	    codeColumn[0] = 1;
	    codeColumn[1] = 0;
	    codeColumn[2] = 2;
	    codeColumn[3] = 0;
	    codeColumn[4] = 0;
	    codeColumn[5] = 0;
	    codeColumn[6] = 3;
	    codeColumn[7] = 0;

	    csvtoExcel.setCodeColumn(codeColumn);
	    csvtoExcel.setSOURCE_INDEX(4);
	    csvtoExcel.initialize();
		csvtoExcel.runHSSF(v, outputfile);
	}

}


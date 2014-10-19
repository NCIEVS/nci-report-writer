package gov.nih.nci.evs.reportwriter.service;

import gov.nih.nci.evs.reportwriter.utils.*;

import java.io.*;
import java.util.*;

import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;
import org.lexgrid.valuesets.*;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.types.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Exceptions.*;
import org.LexGrid.LexBIG.Extensions.Generic.*;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.*;
import org.LexGrid.LexBIG.Utility.Iterators.*;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.codingSchemes.*;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.LexGrid.naming.*;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PropertyMatchValue;
import org.LexGrid.valueSets.PropertyReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.types.DefinitionOperator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

public class HierarchyReportGenerator {

    private static int _maxReturn = -1;
    private static boolean RESOLVE_FORWARD = true;


    public static ConceptReferenceList createConceptReferenceList(
        String[] codes, String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.length; i++) {
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(codes[i]);
            list.addConceptReference(cr);
        }
        return list;
    }

    public static ConceptReferenceList createConceptReferenceList(Vector codes,
        String codingSchemeName) {
        if (codes == null) {
            return null;
        }
        ConceptReferenceList list = new ConceptReferenceList();
        for (int i = 0; i < codes.size(); i++) {
            String code = (String) codes.elementAt(i);
            ConceptReference cr = new ConceptReference();
            cr.setCodingSchemeName(codingSchemeName);
            cr.setConceptCode(code);
            list.addConceptReference(cr);
        }
        return list;
    }

    public static CodedNodeSet getNodeSet(LexBIGService lbSvc, String scheme, CodingSchemeVersionOrTag versionOrTag)
        throws Exception {
		CodedNodeSet cns = null;
		try {
			cns = lbSvc.getCodingSchemeConcepts(scheme, versionOrTag);
			CodedNodeSet.AnonymousOption restrictToAnonymous = CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY;
			cns = cns.restrictToAnonymous(restrictToAnonymous);
	    } catch (Exception ex) {
			ex.printStackTrace();
		}

		return cns;
	}



    public static Entity getConceptByCode(String codingSchemeName, String vers, String code) {
        try {
			if (code == null) {
				//System.out.println("Input error in DataUtils.getConceptByCode -- code is null.");
				return null;
			}
			if (code.indexOf("@") != -1) return null; // anonymous class

            LexBIGService lbSvc = new RemoteServerUtil().createLexBIGService();
            if (lbSvc == null) {
                //System.out.println("lbSvc == null???");
                return null;
            }
            CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
            if (vers != null) versionOrTag.setVersion(vers);

            ConceptReferenceList crefs = createConceptReferenceList(
                    new String[] { code }, codingSchemeName);

            CodedNodeSet cns = null;
            try {
				try {
					cns = getNodeSet(lbSvc, codingSchemeName, versionOrTag);

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}

                if (cns == null) {
					//System.out.println("getConceptByCode getCodingSchemeConcepts returns null??? " + codingSchemeName);
					return null;
				}

                cns = cns.restrictToCodes(crefs);
 				ResolvedConceptReferenceList matches = null;

                SortOptionList sortOptions = null;
                LocalNameList filterOptions = null;
                LocalNameList propertyNames = null;
                CodedNodeSet.PropertyType[] propertyTypes = null;
                boolean resolveObjects = true;
                int maxToReturn = 1;
				try {
					//matches = cns.resolveToList(null, null, null, 1);
					matches = cns.resolveToList(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects, maxToReturn);

				} catch (Exception e) {
					e.printStackTrace();
				}

                if (matches == null) {
                    //System.out.println("Concept not found.");
                    return null;
                }
                int count = matches.getResolvedConceptReferenceCount();
                // Analyze the result ...
                if (count == 0)
                    return null;
                if (count > 0) {
                    try {
                        ResolvedConceptReference ref = (ResolvedConceptReference) matches
                                .enumerateResolvedConceptReference()
                                .nextElement();
                        Entity entry = ref.getReferencedEntry();
                        return entry;
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        return null;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

/*
    public static String getFocusConceptPropertyValue(
	    Entity concept,
		String property_name,
		String property_type,
		String qualifier_name,
		String source,
		Vector qualifier_value_vec,
		String representational_form,
		String delimiter,
		Boolean isPreferred) {

        if (concept == null) return "";

        HashSet hset = new HashSet();
        int num_matches = 0;
        org.LexGrid.commonTypes.Property[] properties =
            new org.LexGrid.commonTypes.Property[] {};

        if (property_type == null) {

        } else if (property_type.compareToIgnoreCase("GENERIC") == 0) {
            properties = concept.getProperty();
        } else if (property_type.compareToIgnoreCase("PRESENTATION") == 0) {
            properties = concept.getPresentation();
        } else if (property_type.compareToIgnoreCase("COMMENT") == 0) {
            properties = concept.getComment();
        } else if (property_type.compareToIgnoreCase("DEFINITION") == 0) {
            properties = concept.getDefinition();
        }

        String return_str = "";

		for (int i = 0; i < properties.length; i++) {
			boolean match = false;
			org.LexGrid.commonTypes.Property p = properties[i];
			String propertyName = p.getPropertyName();

			if (propertyName.compareTo(property_name) == 0) {
				match = true;

				if (source != null || representational_form != null
					|| qualifier_name != null || isPreferred != null) {
					// compare isPreferred
					if (isPreferred != null && p instanceof Presentation) {
						Presentation presentation = (Presentation) p;
						Boolean is_pref = presentation.getIsPreferred();
						if (is_pref == null) {
							match = false;
						} else if (is_pref != null
							&& !is_pref.equals(isPreferred)) {
							match = false;
						}
					}

					if (match) {
						if (representational_form != null
							&& p instanceof Presentation) {
							Presentation presentation = (Presentation) p;
							String representationalForm = presentation.getRepresentationalForm();
							if (representationalForm.compareTo(representational_form) != 0) {
								match = false;
							} else {

							}

						}
					}

					// match qualifier
					if (match) {
						if (qualifier_value_vec != null && qualifier_value_vec.size() > 0) // match qualifier name vaue pair
						// qualifier, if needed
						{

							boolean match_found = false;
							PropertyQualifier[] qualifiers = p.getPropertyQualifier();
							for (int j = 0; j < qualifiers.length; j++) {
								PropertyQualifier q = qualifiers[j];
								String name = q.getPropertyQualifierName();
								String value = q.getValue().getContent();
                                if (qualifier_name != null &&
									qualifier_name.compareTo(name) == 0 &&
									qualifier_value_vec.contains(value)) {
										match_found = true;
									    break;
								}

							}
							if (!match_found) {
								match = false;
							}
						}
					}
					// match source
					if (match) {
						if (source != null) // match source
						{
							boolean match_found = false;
							Source[] sources = p.getSource();
							for (int j = 0; j < sources.length; j++) {
								Source src = sources[j];

								if (src.getContent().compareTo(source) == 0) {
									match_found = true;
									break;
								}
							}
							if (!match_found) {
								match = false;
							}
						}
					}
				}

				if (match) {
					String prop_value = p.getValue().getContent();
					if (!hset.contains(prop_value)) {
						num_matches++;
						hset.add(prop_value);
						if (num_matches == 1) {
							return_str = prop_value;
						} else {
							return_str = return_str + delimiter + prop_value;
						}
					}
				}
			}
		}
        return return_str;
	}
*/

//////////////////////////////////////////////////////////////////////////////////////////////////
    public static NameAndValueList createNameAndValueList(String[] names,
        String[] values) {
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.length; i++) {
            NameAndValue nv = new NameAndValue();
            nv.setName(names[i]);
            if (values != null) {
                nv.setContent(values[i]);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    protected static NameAndValueList createNameAndValueList(
        Vector<String> names, Vector<String> values) {
        if (names == null)
            return null;
        NameAndValueList nvList = new NameAndValueList();
        for (int i = 0; i < names.size(); i++) {
            String name = names.elementAt(i);
            String value = values.elementAt(i);
            NameAndValue nv = new NameAndValue();
            nv.setName(name);
            if (value != null) {
                nv.setContent(value);
            }
            nvList.addNameAndValue(nv);
        }
        return nvList;
    }

    protected static Association processForAnonomousNodes(Association assoc) {
        // clone Association except associatedConcepts
        Association temp = new Association();
        temp.setAssociatedData(assoc.getAssociatedData());
        temp.setAssociationName(assoc.getAssociationName());
        temp.setAssociationReference(assoc.getAssociationReference());
        temp.setDirectionalName(assoc.getDirectionalName());
        temp.setAssociatedConcepts(new AssociatedConceptList());

        for (int i = 0; i < assoc.getAssociatedConcepts()
            .getAssociatedConceptCount(); i++) {
            if (assoc.getAssociatedConcepts().getAssociatedConcept(i)
                .getReferencedEntry() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != null
                && assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getReferencedEntry().getIsAnonymous() != false
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@")
                && !assoc.getAssociatedConcepts().getAssociatedConcept(i)
                    .getConceptCode().equals("@@")) {
                // do nothing
            } else {
                temp.getAssociatedConcepts().addAssociatedConcept(
                    assoc.getAssociatedConcepts().getAssociatedConcept(i));
            }
        }
        return temp;
    }


    public static Vector<String> getAssociatedConceptCodes(String scheme,
        String version, String code, String assocName) {
        return getAssociatedConceptCodes(scheme, version, code, assocName, RESOLVE_FORWARD);
	}



    public static Vector<String> getAssociatedConceptCodes(String scheme,
        String version, String code, String assocName, boolean direction) {
        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);
        ResolvedConceptReferenceList matches = null;
        Vector<String> v = new Vector<String>();
        try {
            LexBIGService lbSvc = RemoteServerUtil.createLexBIGService();
            CodedNodeGraph cng = lbSvc.getNodeGraph(scheme, csvt, null);

            NameAndValueList nameAndValueList =
                createNameAndValueList(new String[] { assocName }, null);

            NameAndValueList nameAndValueList_qualifier = null;

            cng =
                cng.restrictToAssociations(nameAndValueList,
                    nameAndValueList_qualifier);

            if (direction) {
				matches =
					cng.resolveAsList(
						ConvenienceMethods.createConceptReference(code, scheme),
						true, false, 1, 1, new LocalNameList(), null, null,
						_maxReturn);
			} else {
				matches =
					cng.resolveAsList(
						ConvenienceMethods.createConceptReference(code, scheme),
						false, true, 1, 1, new LocalNameList(), null, null,
						_maxReturn);
			}

            if (matches.getResolvedConceptReferenceCount() > 0) {
                Enumeration refEnum =
                    matches.enumerateResolvedConceptReference();

                while (refEnum.hasMoreElements()) {
                    ResolvedConceptReference ref =
                        (ResolvedConceptReference) refEnum.nextElement();

                    AssociationList list = ref.getTargetOf();
                    if (direction) {
						list = ref.getSourceOf();
					}
                    if (list == null)
                        continue;
                    Association[] associations = list.getAssociation();

                    for (int i = 0; i < associations.length; i++) {
                        Association assoc = associations[i];
                        // KLO
                        assoc = processForAnonomousNodes(assoc);
                        AssociatedConcept[] acl =
                            assoc.getAssociatedConcepts()
                                .getAssociatedConcept();
                        for (int j = 0; j < acl.length; j++) {
                            AssociatedConcept ac = acl[j];
                            v.add(ac.getReferencedEntry().getEntityCode());
                        }
                    }
                }
                SortUtils.quickSort(v);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }


    public static LexBIGServiceConvenienceMethods createLexBIGServiceConvenienceMethods(
        LexBIGService lbSvc) {
        LexBIGServiceConvenienceMethods lbscm = null;
        try {
            lbscm =
                (LexBIGServiceConvenienceMethods) lbSvc
                    .getGenericExtension("LexBIGServiceConvenienceMethods");
            lbscm.setLexBIGService(lbSvc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lbscm;
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////
    public static Vector<String> parseData(String line) {
		if (line == null) return null;
        String tab = "|";
        return parseData(line, tab);
    }

    public static Vector<String> parseData(String line, String tab) {
		if (line == null) return null;
        Vector data_vec = new Vector();
        StringTokenizer st = new StringTokenizer(line, tab);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            if (value.compareTo("null") == 0)
                value = " ";
            data_vec.add(value);
        }
        return data_vec;
    }


    public static String getTermBySourceName(String scheme, String version, String code, String source, String term_type) {
        String term = null;

        LexBIGService lbSvc = null;
        try {
        	lbSvc = RemoteServerUtil.createLexBIGService();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
        LexBIGServiceConvenienceMethods lbscm =
            createLexBIGServiceConvenienceMethods(lbSvc);

        CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
        if (version != null)
            csvt.setVersion(version);

        ConceptReference cr = ConvenienceMethods.createConceptReference(code, scheme);
        ConceptReferenceList codeList = new ConceptReferenceList();
        codeList.addConceptReference(cr);

        CodedNodeSet cns = null;
        try {
			LocalNameList entityTypes = new LocalNameList();
			entityTypes.addEntry("concept");
			cns = lbSvc.getNodeSet(scheme, csvt, entityTypes);

			cns = cns.restrictToCodes(codeList);

			SortOptionList sortOptions = null;
			LocalNameList filterOptions = null;
			LocalNameList propertyNames = new LocalNameList();
			propertyNames.addEntry("FULL_SYN");
			CodedNodeSet.PropertyType[] propertyTypes = new CodedNodeSet.PropertyType[1];
			propertyTypes[0] = CodedNodeSet.PropertyType.PRESENTATION;

			boolean resolveObjects = true;
			int maxToReturn = 10;
            ResolvedConceptReferenceList rcrl = cns.resolveToList(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects, maxToReturn);

            for (int i=0; i<rcrl.getResolvedConceptReferenceCount(); i++) {
				ResolvedConceptReference rcr = rcrl.getResolvedConceptReference(i);
				Entity entity = rcr.getReferencedEntry();
				Presentation[] properties = entity.getPresentation();

                int match_knt = 0;
				for (int j = 0; j < properties.length; j++) {
					Presentation prop = properties[j];

					if (prop.getRepresentationalForm().compareTo(term_type) == 0) {
                        term = prop.getValue().getContent();
                        boolean match_found = false;
						Source[] sources = prop.getSource();
						for (int m = 0; m < sources.length; m++) {
							Source src = sources[m];
							if (src.getContent().compareTo(source) == 0) {
								match_found = true;
								term = prop.getValue().getContent();
								match_knt++;
							}
						}

						if (match_knt == 1) {
							return term;
						} else if (match_knt > 1) {
							PropertyQualifier[] qualifiers = prop.getPropertyQualifier();
							if (qualifiers != null && qualifiers.length > 0) {
								for (int k = 0; k < qualifiers.length; k++) {
									PropertyQualifier q = qualifiers[k];
									String qualifier_name = q.getPropertyQualifierName();
									String qualifier_value = q.getValue().getContent();
									if (qualifier_name.compareTo("subsource-name") == 0 && qualifier_value.compareTo("CDRH") == 0 ) {
                                        return prop.getValue().getContent();
									}
								}
							}
						}

					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (term == null) {
		    Entity entity = getConceptByCode(scheme, version, code);
		    return entity.getEntityDescription().getContent();
		}
		return term;
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static String getIndentation(int level) {
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<level; i++) {
			buf.append("\t");
		}
		return buf.toString();
	}



	public static void printNode(PrintWriter pw, String scheme, String version, String name, String code,
	                             String assocName, String source, String term_type, int level) {
		String indentation = getIndentation(level);
		pw.println(indentation + name + " (" + code + ")");
		System.out.println(indentation + name + " (" + code + ")");
        Vector u = getAssociatedConceptTermAndCode(scheme, version, code, assocName, source, term_type);
        for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
            Vector v = parseData(t);
            String child_name = (String) v.elementAt(0);
            String child_code = (String) v.elementAt(1);
            printNode(pw, scheme, version, child_name, child_code, assocName, source, term_type, level+1);
		}
	}


    public static void generateHierarchyReport(String filename, String scheme, String version, String code, String assocName,
                                               String source, String term_type) {
		PrintWriter pw = null;
		long ms = System.currentTimeMillis();
		try {
			pw = new PrintWriter(filename, "UTF-8");
			String term = getTermBySourceName(scheme, version, code, source, term_type);
			String name = term;
			int level = 0;
			printNode(pw, scheme, version, name, code, assocName, source, term_type, level);

			System.out.println("Output file " + filename + " generated.");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
            System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
		}
	}


    public static Vector getAssociatedConceptTermAndCode(String scheme, String version, String code, String assocName, String source, String term_type) {
		Vector<String> v = getAssociatedConceptCodes(scheme, version, code, assocName);
		Vector u = new Vector();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			String term = getTermBySourceName(scheme, version, t, source, term_type);
			u.add(term + "|" + t);
		}
		u = SortUtils.quickSort(u);
		return u;
	}

    public static Vector searchByName(String scheme, String version, String matchText, String algorithm) throws LBException {
        Vector v = null;
		//String scheme = "NCI_Thesaurus";
		//String version = null;
		int searchOption = SimpleSearchUtils.BY_NAME;
		//String algorithm = "exactMatch";
		ResolvedConceptReferencesIteratorWrapper wrapper = new SimpleSearchUtils().search(scheme, version, matchText, searchOption, algorithm);
		if (wrapper != null) {
			v = new Vector();
			ResolvedConceptReferencesIterator itr = wrapper.getIterator();
			try {
				while (itr.hasNext()) {
					try {
						ResolvedConceptReference[] refs =
							itr.next(100).getResolvedConceptReference();
						for (ResolvedConceptReference ref : refs) {
							v.add(ref.getConceptCode());
						}
					} catch (Exception ex) {
						break;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return v;
	}


    public static HashMap searchForHierarchyRoots(String scheme, String version, String assocName, Vector roots) {
		HashMap hmap = new HashMap();
		for (int i=0; i<roots.size(); i++) {
			String t = (String) roots.elementAt(i);
			try {
				Vector v = searchByName(scheme, version, t, "exactMatch");
				if (v != null) {
					for (int j=0; j<v.size(); j++) {
						String root_code = (String) v.elementAt(j);
						String code = root_code;
						Vector<String> associated_concept_codes = getAssociatedConceptCodes(scheme, version, code, assocName);
						for (int k=0; k<associated_concept_codes.size(); k++) {
							String associated_concept_code = (String) associated_concept_codes.elementAt(k);
							Vector u = new Vector();
							if (hmap.containsKey(associated_concept_code)) {
								u = (Vector) hmap.get(associated_concept_code);
							}
							u.add(t);
							hmap.put(associated_concept_code, u);
						}
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
        return hmap;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Vector getExcelColumnHeaders(String excelfile) {
		Vector v = new Vector();
		try {
			FileInputStream file = new FileInputStream(new File(excelfile));

			//Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			//Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row = sheet.getRow(0);
			java.util.Iterator<Cell> it = row.cellIterator();
			while (it.hasNext()) {
				Cell cell = it.next();
				String cell_value = cell.getStringCellValue();
				v.add(cell_value);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v;
	}

    public static HashMap getParent2ChildrenHashMap(String excelfile, int col_num, int parent_col_num) {

        HashMap parent2ChildrenHashMap = new HashMap();
		try {
			FileInputStream file = new FileInputStream(new File(excelfile));

			//Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			//Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			int cell_number = col_num;
			int parent_cell_number = parent_col_num;

			//Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			//int lcv = 0;
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Cell cell = row.getCell(cell_number);
				Cell parent_cell = row.getCell(parent_cell_number);
				String cell_value = "";
				String parent_cell_value = "*";
				if (cell != null) {
					cell_value = cell.getStringCellValue();
				}
				if (parent_cell != null) {
					parent_cell_value = parent_cell.getStringCellValue();
				}

				Vector v = null;
				if (parent2ChildrenHashMap.containsKey(parent_cell_value)) {
					v = (Vector) parent2ChildrenHashMap.get(parent_cell_value);
				} else {
					v = new Vector();
				}
				if (!v.contains( cell_value )) {
					v.add(cell_value);
				}
				parent2ChildrenHashMap.put(parent_cell_value, v);
			}
			file.close();

			Iterator it = parent2ChildrenHashMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				Vector v = (Vector) parent2ChildrenHashMap.get(key);
				v = SortUtils.quickSort(v);
				parent2ChildrenHashMap.put(key, v);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parent2ChildrenHashMap;
    }


    public static String getDefaultHierarchyReportName(String excelfile) {
		int n = excelfile.lastIndexOf(".");
		String outputfile = excelfile.substring(0, n) + "_Hierarchy.txt";
		return outputfile;
	}


    public static void generateHierarchyReport(String excelfile, String outputfile, String assocName) {



        if (outputfile == null) {
			outputfile = getDefaultHierarchyReportName(excelfile);
	    }

	    if (assocName == null) {
			assocName = "Has_CDRH_Parent";
		}

System.out.println("(*) excelfile: " + excelfile);
System.out.println("(*) outputfile: " + outputfile);
System.out.println("(*) assocName: " + assocName);



		Vector headers = getExcelColumnHeaders(excelfile);

		int col_num = -1;
		int parent_col_num = -1;

		String col = null;
		String parent_col = null;

		for (int i=0; i<headers.size(); i++) {
			String header = (String) headers.elementAt(i);
			String header_lower = header.toLowerCase();
			if (header_lower.endsWith("source pt") && header_lower.startsWith("parent")) {
				parent_col_num = i;
				parent_col = header_lower;
			} else if (header_lower.endsWith("source pt")) {
			    col_num = i;
			    col = header;
			}
		}

		System.out.println("col: " + col);
		System.out.println("col_num: " + col_num);
		System.out.println("parent_col: " + parent_col);
		System.out.println("parent_col_num: " + parent_col_num);

		String scheme = "NCI_Thesaurus";
		String version = null;


        HashMap parent2ChildrenHashMap = getParent2ChildrenHashMap(excelfile, col_num, parent_col_num);
        Vector roots = (Vector) parent2ChildrenHashMap.get("*");
        HashMap topNodes2RootsHashMap = searchForHierarchyRoots(scheme, version, assocName, roots);

        generateHierarchyReport(outputfile, parent2ChildrenHashMap, topNodes2RootsHashMap);
	}


	public static void printNode(PrintWriter pw, HashMap parent2ChildrenHashMap, String name, int level) {
		String indentation = getIndentation(level);
		pw.println(indentation + name);
        Vector u = (Vector) parent2ChildrenHashMap.get(name);
        if (u == null) {
			return;
		}

        for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
            printNode(pw, parent2ChildrenHashMap, t, level+1);
		}
	}


    public static void generateHierarchyReport(String outputfile, HashMap parent2ChildrenHashMap, HashMap topNodes2RootsHashMap) {
		PrintWriter pw = null;
		long ms = System.currentTimeMillis();

		String scheme = "NCI_Thesaurus";
		String version = null;
		String source = "FDA";
		String term_type = "PT";

		HashMap topnodeName2CodeHashMap = new HashMap();

		try {
			pw = new PrintWriter(outputfile, "UTF-8");
			Vector topnodes = new Vector();
			Iterator it = topNodes2RootsHashMap.keySet().iterator();
			while (it.hasNext()) {
				String code = (String) it.next();
				String term = getTermBySourceName(scheme, version, code, source, term_type);
				topnodes.add(term);
				topnodeName2CodeHashMap.put(term, code);
			}
			topnodes = SortUtils.quickSort(topnodes);
			for (int i=0; i<topnodes.size(); i++) {
			    String topnode = (String) topnodes.elementAt(i);
			    String topenode_code = (String) topnodeName2CodeHashMap.get(topnode);
			    System.out.println("TOP NODE: " + topnode + " (" + topenode_code + ")");

			    pw.println(topnode);
			    Vector roots = (Vector) topNodes2RootsHashMap.get(topenode_code);
			    roots = SortUtils.quickSort(roots);
				for (int j=0; j<roots.size(); j++) {
					String name = (String) roots.elementAt(j);
					System.out.println("ROOT: " + name);
					printNode(pw, parent2ChildrenHashMap, name, 1);
				}
		    }
			System.out.println("Output file " + outputfile + " generated.");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				pw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
            System.out.println("Run time (ms): " + (System.currentTimeMillis() - ms));
		}
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public static void main(String [ ] args)
	{
		/*
		String code = "C101805";
		String codingSchemeName = "NCI_Thesaurus";
		String vers = null;
		Entity entity = HierarchyReportGenerator.getConceptByCode(codingSchemeName, vers, code);
		if (entity != null) {
			System.out.println(entity.getEntityDescription().getContent());
		}
		String source_code = HierarchyReportGenerator.getFocusConceptPropertyValue(
		    entity, "FULL_SYN", "PRESENTATION", null, "NCI", null, "AB", "|", null);
		if (source_code != null) {
			System.out.println("source_code: " + source_code);
		} else {
			System.out.println("source_code not available.");
		}

		code = "C102034";
		Vector qualifier_value_vec = new Vector();
		qualifier_value_vec.add(source_code);
		entity = HierarchyReportGenerator.getConceptByCode(codingSchemeName, vers, code);
		String submission_value = HierarchyReportGenerator.getFocusConceptPropertyValue(
		    entity, "FULL_SYN", "PRESENTATION", "source-code", "CDISC", qualifier_value_vec, "PT", "|", null);
		if (source_code != null) {
			System.out.println("submission_value: " + submission_value);
		} else {
			System.out.println("submission_value not available.");
		}
		*/

		String code = "C62596";
		String codingSchemeName = "NCI_Thesaurus";
		String version = null;
		String assocName = "Has_CDRH_Parent";
		String source = "FDA";
		String term_type = "PT";

		/*

		Vector<String> v = getAssociatedConceptCodes(codingSchemeName, version, code, assocName);
		Vector u = new Vector();
		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			String term = getTermBySourceName(codingSchemeName, version, t, source, term_type);
			u.add(term + "|" + t);
		}
		u = SortUtils.quickSort(u);
		for (int i=0; i<u.size(); i++) {
			String t = (String) u.elementAt(i);
			System.out.println(t);
		}
		*/

		String filename = "hierarchy.txt";
        //generateHierarchyReport(filename, codingSchemeName, version, code, assocName, source, term_type);

        code = "C54027";

        /*
        String term = getTermBySourceName(codingSchemeName, version, code, source, term_type);
        System.out.println(term);
        */

/*
		Vector roots = new Vector();
		roots.add("DIAGNOSTIC, THERAPEUTIC, OR RESEARCH EQUIPMENT");
		roots.add("EVALUATION CONCLUSION");
		roots.add("EVALUATION METHOD");
		roots.add("EVALUATION RESULT");
		roots.add("MISSING VALUE REASON");
		roots.add("PATIENT PROBLEM/MEDICAL PROBLEM");


        HashMap hmap = searchForHierarchyRoots(codingSchemeName, version, assocName, roots);
        Iterator it = hmap.keySet().iterator();
        while (it.hasNext()) {
			code = (String) it.next();
			System.out.println("PARENT: " + code);
			String name = getTermBySourceName(codingSchemeName, version, code, "FDA", "PT");
			System.out.println("parent: " + name + " (" + code + ")");

			Vector u = (Vector) hmap.get(code);
			u = SortUtils.quickSort(u);
			for (int i=0; i<u.size(); i++) {
				String t = (String) u.elementAt(i);
				System.out.println(t);
			}
		}

*/

        String excelfile = "FDA-CDRH_NCIt_Subsets.xls";
        int n = excelfile.lastIndexOf(".");
        String outputfile = excelfile.substring(0, n) + "_Hierarchy.txt";
        System.out.println(outputfile);
        generateHierarchyReport(excelfile, outputfile, assocName);
	}
}

/*
[#31496] CDISC reports: Use source-code Prefix-XYZ to pull PT

Given a focus concept code and an associated concept code (i.e., codelist code),
and a source name, first find the name of the FULL_SYN of the associated concept
with a source NCI and presentation form SY (i.e., the source-code qualifier value
to be used in the next step).

Find the FULL_SYNs of the focus concept with source equaling to the specified source name
and source code equaling to the source-code qualifier (i.e., the name of the FULL_SYN of
the associated concept found in the previous step above). Output the name of the FULL_SYN
such found. This is so-called CDISC Submission Value.



*/


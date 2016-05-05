/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.service;

import java.io.*;
import java.util.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.formatter.*;
import gov.nih.nci.evs.reportwriter.utils.*;

import org.LexGrid.commonTypes.*;
import org.LexGrid.concepts.*;
import org.apache.log4j.*;
import org.lexgrid.valuesets.*;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;

/**
 * @author EVS Team (Kim Ong)
 * @version 1.0
 */

public class ReportGenerationRunner { //implements Runnable {
    private static Logger _logger = Logger
        .getLogger(ReportGenerationRunner.class);

    private String _outputDir = null;
    private String _standardReportLabel = null;
    private String _uid = null;
    private String _emailAddress = null;
    private int[] _ncitColumns = new int[] {};

    private int _count = 0;
    private String _hierarchicalAssoName = null;
    private int _abortLimit = 0;

    private HashMap _code2PTHashMap = null;

    SpecialCases.CDISCExtensibleInfo _cdiscInfo = null;

    private String prev_subset_code = null;
    private boolean subheader_line_required = false;
    private int KEY_INDEX_1 = 4; // for CDISC Sort
    private int KEY_INDEX_2 = 0;

    ReportColumn[] temp_cols = null;
    private static String SOURCE_CODE = "source-code";

    private LexEVSValueSetDefinitionServices definitionServices = null;

    public ReportGenerationRunner() {
		try {
        	this.definitionServices = RemoteServerUtil.getValueSetDefinitionService();
		} catch (Exception ex) {

		}
    }

    public PrintWriter openPrintWriter(String outputfile) {
        return RWUIUtils.openPrintWriter(outputfile);
    }

    public void closePrintWriter(PrintWriter pw) {
		RWUIUtils.closePrintWriter(pw);
		pw = null;
    }

    public Boolean generateStandardReport(String outputDir, String uid, StandardReportTemplate standardReportTemplate, int[] ncitColumns) {
        try {
			String standardReportLabel = standardReportTemplate.getLabel();
            String defining_set_desc =
                standardReportTemplate.getRootConceptCode();
            File dir = new File(outputDir);
            if (!dir.exists()) {
                _logger.debug("Output directory " + outputDir
                    + " does not exist -- try to create the directory.");
                boolean retval = dir.mkdir();
                if (!retval) {
                    throw new Exception("Unable to create output directory "
                        + outputDir + " - please check privilege setting.");
                } else {
                    _logger.debug("Output directory: " + outputDir
                        + " created.");
                }
            } else {
                _logger.debug("Output directory: " + outputDir + " exists.");
            }

            String codingSchemeVersion = standardReportTemplate.getCodingSchemeVersion();

			if (!DataUtils.isNullOrBlank(DataUtils.NCIT_VERSION)) {
				codingSchemeVersion = DataUtils.NCIT_VERSION;
			}

            // append version to the report file name:
            String pathname =
                outputDir + File.separator + standardReportLabel + "__"
                    + codingSchemeVersion + ".txt";
            pathname = pathname.replaceAll(" ", "_");
            _logger.debug("Full path name: " + pathname);

            PrintWriter pw = openPrintWriter(pathname);
            if (pw == null)
                throw new Exception("Unable to create output file " + pathname
                    + " -- please check privilege setting.");
            _logger.debug("opened PrintWriter " + pathname);

/*
            int id = standardReportTemplate.getId();
*/

            String label = standardReportTemplate.getLabel();
            String codingSchemeName =
                standardReportTemplate.getCodingSchemeName();

            String rootConceptCode =
                standardReportTemplate.getRootConceptCode();

            String associationName =
                standardReportTemplate.getAssociationName();

            boolean direction = standardReportTemplate.getDirection();
            int level = standardReportTemplate.getLevel();
            Character delimiter = standardReportTemplate.getDelimiter();
            String delimeter_str = "\t";

            _logger.debug(StringUtils.SEPARATOR);
            //_logger.debug("  * ID: " + id);
            _logger.debug("  * Label: " + label);
            _logger.debug("  * CodingSchemeName: " + codingSchemeName);
            _logger.debug("  * CodingSchemeVersion: " + codingSchemeVersion);
            _logger.debug("  * Root: " + rootConceptCode);
            _logger.debug("  * AssociationName: " + associationName);
            _logger.debug("  * Direction: " + direction);
            _logger.debug("  * Level: " + level);
            _logger.debug("  * Delimiter: " + delimiter);

            Object[] objs = null;
            Collection<ReportColumn> cc =
                standardReportTemplate.getColumnCollection();
            if (cc == null) {
                throw new Exception(
                    "standardReportTemplate.getColumnCollection"
                        + " returns null???");
            } else {
                objs = cc.toArray();
            }

            ReportColumn[] cols = null;

            if (cc != null) {
                cols = new ReportColumn[objs.length];
                temp_cols = new ReportColumn[objs.length];
                for (int i = 0; i < objs.length; i++) {
                    gov.nih.nci.evs.reportwriter.bean.ReportColumn col =
                        (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];
                    //Debug.print(col);
                    cols[i] = col;
                }
            }

            temp_cols = copyReportColumns(cols);
            setReportColumns(temp_cols);


            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("* Start generating report..." + pathname);

            printReportHeading(pw, cols);

            String scheme = standardReportTemplate.getCodingSchemeName();
            codingSchemeVersion = standardReportTemplate.getCodingSchemeVersion();

            associationName = standardReportTemplate.getAssociationName();
            // associationName = "A8"; //NOTE_A8: A8 vs Concept_In_Subset
            level = standardReportTemplate.getLevel();

            String tag = null;
            int curr_level = 0;
            int max_level = standardReportTemplate.getLevel();
/*

            if (max_level < 0)
                max_level =
                    AppProperties.getInstance().getIntProperty(
                        AppProperties.MAXIMUM_LEVEL, 20);
*/
            // printReportHeading(pw, cols);
            if (_hierarchicalAssoName == null) {
                Vector<String> hierarchicalAssoName_vec =
                    DataUtils.getHierarchyAssociationId(scheme, codingSchemeVersion);
                if (hierarchicalAssoName_vec == null
                    || hierarchicalAssoName_vec.size() == 0) {
                    return Boolean.FALSE;
                }
                _hierarchicalAssoName =
                    (String) hierarchicalAssoName_vec.elementAt(0);
            }

            String associationCode = "";
            try {
                associationCode =
                    DataUtils.getAssociationCode(codingSchemeName,
                        codingSchemeVersion, associationName);
            } catch (Exception e) {
                throw new Exception(
                    "Unable to create output file "
                        + pathname
                        + " - could not map association name to its corresponding code.");
            }
            _cdiscInfo = new SpecialCases.CDISCExtensibleInfo(cols);

            //LexEVSValueSetDefinitionServices definitionServices =
            //    DataUtils.getValueSetDefinitionService();

            String uri = DataUtils.codingSchemeName2URI(scheme, codingSchemeVersion);

            curr_level = 0;
			String code = standardReportTemplate.getRootConceptCode();
			Entity defining_root_concept = null;


			//[GF#32771] Allow report templates with multiple roots.
			Vector w = DataUtils.parseData(code, ";");
			for (int k=0; k<w.size(); k++) {
				code = (String) w.elementAt(k);

					if (!DataUtils.isNullOrBlank(DataUtils.NCIT_VERSION)) {
						codingSchemeVersion = DataUtils.NCIT_VERSION;
					}

				defining_root_concept =
					DataUtils.getConceptByCode(codingSchemeName,
						codingSchemeVersion, null, code);

				curr_level = 0;
				traverse(definitionServices, uri, pw, scheme, codingSchemeVersion, tag, defining_root_concept, code,
					_hierarchicalAssoName, associationName, direction, curr_level,
					max_level, cols);

			}

            closePrintWriter(pw);

            _logger.debug("Total number of concepts processed: " + _count);
            _logger.debug("Output file " + pathname + " generated.");

            return createStandardReports(outputDir, standardReportLabel, uid,
                standardReportTemplate, pathname, codingSchemeVersion, delimeter_str);


        } catch (Exception e) {
			e.printStackTrace();
        }

        return Boolean.TRUE;
    }

    public void printReportHeading(PrintWriter pw, ReportColumn[] cols) {

        if (cols == null) {
            _logger.warn("In printReportHeading number of ReportColumn:"
                + " cols == null ??? ");
        }

        String columnHeadings = "";
        String delimeter_str = "\t";
        if (cols == null) {
            return;
        }
        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            String label = rc.getLabel();
            columnHeadings = columnHeadings + label;
            if (i < cols.length - 1)
                columnHeadings = columnHeadings + delimeter_str;
        }
        pw.println(columnHeadings);
    }

    private void writeColumnData(LexEVSValueSetDefinitionServices definitionServices,
        String uri, PrintWriter pw, String scheme, String version,
        Entity defining_root_concept, Entity associated_concept, Entity c,
        String delim, ReportColumn[] cols, boolean firstColRequired)
        throws Exception {

        if (firstColRequired) {
            String firstValue =
               get_ReportColumnValue(definitionServices, uri,
                    scheme, version, defining_root_concept,
                    associated_concept, c, cols[0]);
            if (firstValue == null)
                return;
            firstValue = firstValue.trim();
            if (firstValue.length() == 0)
                return;
        }

        String output_line = "";
        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            String s =
                //KLO
                get_ReportColumnValue(definitionServices, uri,
                    scheme, version, defining_root_concept,
                    associated_concept, c, rc);
            if (i == 0) {
                output_line = s;
            } else {
                // output_line = output_line + "\t" + s;
                output_line = output_line + delim + s;
            }
        }
        pw.println(output_line);
        _count++;
        if ((_count / 100) * 100 == _count) {
            _logger.debug("Number of concepts processed: " + _count);
        }
    }

    private void writeColumnData(LexEVSValueSetDefinitionServices definitionServices,
        String uri, PrintWriter pw, String scheme, String version,
        Entity defining_root_concept, Entity associated_concept, Entity c,
        String delim, ReportColumn[] cols) throws Exception {
		Vector<String> values = getColumnData(definitionServices,
         uri,  pw, scheme, version,
         defining_root_concept, associated_concept, c, delim, cols);
        if (values != null) {
			printColumnData(pw, values, delim);
			_count++;
			if ((_count / 100) * 100 == _count) {
				_logger.debug("Number of concepts processed: " + _count);
			}
		}
    }

    private void printColumnData(PrintWriter pw, Vector<String> values, String delim) {
		String t = StringUtils.toString(values, delim, true);
		pw.println(StringUtils.toString(values, delim, true));
	}


    private Vector<String> getColumnData(LexEVSValueSetDefinitionServices definitionServices,
        String uri, PrintWriter pw, String scheme, String version,
        Entity defining_root_concept, Entity associated_concept, Entity c,
        String delim, ReportColumn[] cols) throws Exception {
        Vector<String> values = new Vector<String>();

        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            String value =
                get_ReportColumnValue(definitionServices, uri,
                    scheme, version, defining_root_concept,
                    associated_concept, c, rc);

            if (value == null) value = "";
            values.add(value);

        }
        return values;
    }


    private void traverse(LexEVSValueSetDefinitionServices definitionServices,
        String uri, PrintWriter pw, String scheme, String version,
        String tag, Entity defining_root_concept, String code,
        String hierarchyAssociationName, String associationName,
        boolean direction, int level, int maxLevel, ReportColumn[] cols)
        throws Exception {
        if (_abortLimit > 0 && _count > _abortLimit)
            return;
        if (maxLevel != -1 && level > maxLevel)
            return;

        Entity root = DataUtils.getConceptByCode(scheme, version, tag, code);
        if (root == null) {
            _logger.warn("Concept with code " + code + " not found.");
            return;
        } else {
            _logger.debug("Level: " + level + " Subset: "
                + root.getEntityCode());
        }

        String delim = "\t";


        Vector<Entity> v = new Vector<Entity>();
        if (direction) {
            v =
                DataUtils.getAssociationTargets(definitionServices, uri,
                    scheme, version, root.getEntityCode(), associationName);
        } else {
            v =
                DataUtils.getAssociationSources(definitionServices, uri,
                    scheme, version, root.getEntityCode(), associationName);
        }

		//Boolean isForwardNavigable = DataUtils.getIsForwardNavigable(scheme, version);
		//String[] asso_array = DataUtils.getHierarchyAssociations(scheme, version);
        //Vector<Entity> v = DataUtils.getSubconcepts(scheme, version, code);

        // associated concepts (i.e., concepts in subset)
        if (v == null)
            return;
        _logger.debug("Subset size: " + v.size());

        String extensible_list = null;
		if (subheader_line_required && prev_subset_code != null && root.getEntityCode().compareTo(prev_subset_code) != 0) {
            String src_code = null;
    		extensible_list = getFocusConceptPropertyValue(
				root,
				"Extensible_List",
				"GENERIC",
				null,
				null,
				src_code,
				null,
				null,
				null);

           if (extensible_list != null && extensible_list.compareTo("") != 0) {
				writeColumnData(definitionServices, uri,
					pw, scheme, version, defining_root_concept, root,
					root, delim, temp_cols);
				prev_subset_code = root.getEntityCode();
		    }
		}

        if (subheader_line_required) {
            String src_code = null;
    		extensible_list = getFocusConceptPropertyValue(
				root,
				"Extensible_List",
				"GENERIC",
				null,
				null,
				src_code,
				null,
				null,
				null);

			if (extensible_list != null && extensible_list.compareTo("") != 0) {
				HashMap hmap = new HashMap();
				Vector key_vec = new Vector();
				for (int i = 0; i < v.size(); i++) {
					// subset member element
					Entity c = (Entity) v.elementAt(i);

					Vector values = getColumnData(definitionServices, uri,
						pw, scheme, version, defining_root_concept, root,
						c, delim, cols);

					String key = (String) values.elementAt( KEY_INDEX_1 ) + "|" + (String) values.elementAt( KEY_INDEX_2 );
					key_vec.add(key);

					hmap.put(key, values);

					if (_abortLimit > 0 && _count > _abortLimit)
						break;
				}

				key_vec = SortUtils.quickSort(key_vec);
				for (int i = 0; i < key_vec.size(); i++) {
					String key = (String) key_vec.elementAt(i);
					Vector values = (Vector) hmap.get(key);
					printColumnData(pw, values, delim);
				}

				hmap.clear();
		    }

	    } else {
			for (int i = 0; i < v.size(); i++) {
				// subset member element
				Entity c = (Entity) v.elementAt(i);
				writeColumnData(definitionServices, uri,
					pw, scheme, version, defining_root_concept, root,
					c, delim, cols);

				if (_abortLimit > 0 && _count > _abortLimit)
					break;
			}
        }

        Vector<String> subconcept_vec =
            DataUtils
                .getSubconceptCodes2(scheme, version, root.getEntityCode());

        //Vector<Entity> subconcept_vec = DataUtils.getSubconcepts(scheme, version, code);
        if (subconcept_vec == null | subconcept_vec.size() == 0)
            return;
        level++;
        for (int k = 0; k < subconcept_vec.size(); k++) {
            String subconcep_code = subconcept_vec.elementAt(k);

            traverse(definitionServices, uri, pw, scheme, version, tag, defining_root_concept,
                subconcep_code, hierarchyAssociationName, associationName,
                direction, level, maxLevel, cols);
        }
    }


    private boolean isNull(String s) {
        if (s == null)
            return true;
        s = s.trim();
        if (s.length() == 0)
            return true;
        if (s.compareTo("") == 0)
            return true;
        if (s.compareToIgnoreCase("null") == 0)
            return true;
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Code for supporting country code like report generation:
    // /////////////////////////////////////////////////////////////////////////

    private Vector<String> parseData(String line, String delimiter) {
        Vector<String> data_vec = new Vector<String>();
        StringTokenizer st = new StringTokenizer(line, delimiter);
        while (st.hasMoreTokens()) {
            String value = st.nextToken();
            data_vec.add(value);
        }
        return data_vec;
    }

    private String getPathname(String outputDir, String standardReportLabel,
        String version, String extension) {
        String pathname =
            outputDir + File.separator + standardReportLabel + "__" + version
                + extension;
        pathname = pathname.replaceAll(" ", "_");
        _logger.debug("Full path name: " + pathname);
        return pathname;
    }

    public static enum ReportFormatType implements Comparator<ReportFormatType> {
        Text("Text (tab delimited)", 0), Excel("Microsoft Office Excel", 1), Html(
                "HyperText Markup Language", 2);

        private static HashMap<String, ReportFormatType> _map =
            new HashMap<String, ReportFormatType>();
        private String _name = "";
        private int _sortValue = -1;

        ReportFormatType(String name, int sortValue) {
            _name = name;
            _sortValue = sortValue;
        }

        public String getName() {
            return _name;
        }

        public int getSortValue() {
            return _sortValue;
        }

        public int compare(ReportFormatType obj1, ReportFormatType obj2) {
            int sortValue1 = obj1.getSortValue();
            int sortValue2 = obj2.getSortValue();
            if (sortValue1 == sortValue2)
                return obj1.getName().compareTo(obj2.getName());
            return obj1.getSortValue() - obj2.getSortValue();
        }

        public static ReportFormatType value_of(String name) {
            return _map.get(name);
        }

        static {
            for (ReportFormatType type : ReportFormatType.values()) {
                _map.put(type.getName(), type);
            }
        }
    }

    private Boolean createStandardReports(String textfile, String delimiter)
            throws Exception {
        AppProperties appProperties = AppProperties.getInstance();
        String ncitUrl = appProperties.getProperty(AppProperties.NCIT_URL);
        String displayNCItCodeUrl =
            appProperties.getProperty(AppProperties.DISPLAY_NCIT_CODE_URL);

        AsciiToHtmlFormatter htmlFormatter = new AsciiToHtmlFormatter();
        htmlFormatter.setDisplayNCItCodeUrl(displayNCItCodeUrl);

        FileFormatterBase[] formatters =
            new FileFormatterBase[] { new AsciiToExcelFormatter(),
                htmlFormatter };

        Boolean bool_obj = true;
        for (FileFormatterBase formatter : formatters) {
            formatter.setNcitUrl(ncitUrl);
            formatter.setNcitCodeColumns(_ncitColumns);
            bool_obj &= formatter.convert(textfile, delimiter);
        }
        return bool_obj;
    }

    private Boolean createStandardReports(String outputDir,
        String standardReportLabel, String uid,
        StandardReportTemplate standardReportTemplate, String textfile,
        String version, String delimiter) throws Exception {
        Boolean bool_obj = createStandardReports(textfile, delimiter);

        // Version: Text
        String label = standardReportLabel + ".txt";
        String pathname = textfile;
        String templateLabel = standardReportTemplate.getLabel();
        String format = ReportFormatType.Text.getName();
        String status = "DRAFT";
        bool_obj &=
            StandardReportService.createStandardReport(label, textfile,
                templateLabel, format, status, uid);

        // Version: Excel
        label = standardReportLabel + ".xls";
        pathname = getPathname(outputDir, standardReportLabel, version, ".xls");
        format = ReportFormatType.Excel.getName();
        bool_obj &=
            StandardReportService.createStandardReport(label, pathname,
                templateLabel, format, status, uid);

        // Version: Html
        label = standardReportLabel + ".htm";
        pathname = getPathname(outputDir, standardReportLabel, version, ".htm");
        format = ReportFormatType.Html.getName();
        bool_obj &=
            StandardReportService.createStandardReport(label, pathname,
                templateLabel, format, status, uid);
        return bool_obj;
    }


    public Entity getFocusConcept(String scheme, String version, Entity associated_concept, Entity node, String field_Id) {
		if (field_Id.indexOf("Associated Concept") != -1) {
			return associated_concept;
		}
		else if (field_Id.indexOf("Parent") != -1) {
    		String associationName = DataUtils.getHasParentAssociationName(scheme, version, field_Id);
    		if (associationName == null) {
				// find superconcept

    			Vector superconcepts = DataUtils.getSuperconcepts(scheme, version, node.getEntityCode());
                if (superconcepts == null) return null;
                if (superconcepts.size() == 0) return null;

                if (field_Id.startsWith("2nd")) {
					if (superconcepts.size() > 1) {
						return (Entity) superconcepts.elementAt(1);
					} else {
						return null;
					}
				} else {
					return (Entity) superconcepts.elementAt(0);
				}


			} else {
				// find associated concept based on associationName
                Vector<AssociatedConcept> asso_concepts = DataUtils.getRelatedConcepts(scheme, version, node.getEntityCode(),
                    associationName, true);
                if (asso_concepts == null) {
					return null;
				}
                if (asso_concepts.size() == 0) {
					return null;
				}

 				if (field_Id.startsWith("2nd")) {
					if (asso_concepts.size() > 1) {
						Entity e = (Entity) asso_concepts.elementAt(1).getReferencedEntry();
                        return e;
					} else {
						return null;
					}

				} else {
					Entity e = (Entity) asso_concepts.elementAt(0).getReferencedEntry();
					return e;
				}
			}
		}
		return node;
	}

    public String getFocusConceptCode(Entity concept) {
		if (concept == null) return "";
		return concept.getEntityCode();
	}


    public static String getFocusConceptPropertyValue(
	    Entity concept,
		String property_name,
		String property_type,
		String qualifier_name,
		String source,
		String qualifier_value,
		String representational_form,
		String delimiter,
		Boolean isPreferred) {




		Vector qualifier_value_vec = null;
		if (qualifier_value	!= null) {
		     qualifier_value_vec = new Vector();
		     qualifier_value_vec.add(qualifier_value);
	    }

		return getFocusConceptPropertyValue(
			 concept,
			 property_name,
			 property_type,
			 qualifier_name,
			 source,
			 qualifier_value_vec,
			 representational_form,
			 delimiter,
			 isPreferred);
	}

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

        if (concept == null) {
			return "";
		}

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
				if (source == null && representational_form == null
					&& qualifier_name == null && isPreferred == null) {

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

				} else if (source != null || representational_form != null
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

    public String getFocusConceptPropertyQualifierValue(
            Entity concept,
            String property_name,
            String property_type,
	        String qualifier_name,
	        String source,
	        String qualifier_value,
	        String representational_form,
	        String delimiter,
	        Boolean isPreferred) {

        if (concept == null) return "";

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
		boolean match = false;

		String ret_qualifier_value = null;

		for (int i = 0; i < properties.length; i++) {
			ret_qualifier_value = null;
			org.LexGrid.commonTypes.Property p = properties[i];
			if (p.getPropertyName().compareTo(property_name) == 0) {
				match = true;

				if (source != null || representational_form != null
					|| qualifier_name != null || isPreferred != null) {

					// compare isPreferred
					if (isPreferred != null && p instanceof Presentation) {
						Presentation presentation = (Presentation) p;
						Boolean is_pref = presentation.getIsPreferred();
						if (is_pref == null) {
							match = false;
						} else if (!is_pref.equals(isPreferred)) {
							match = false;
						}

					}

					// match representational_form
					if (match) {
						if (representational_form != null
							&& p instanceof Presentation) {
							Presentation presentation = (Presentation) p;
							if (presentation.getRepresentationalForm()
								.compareTo(representational_form) != 0) {
								match = false;
							}
						}

					}

					// match qualifier
					if (match) {
						if (qualifier_name != null) // match property
						{
							boolean match_found = false;
							PropertyQualifier[] qualifiers = p.getPropertyQualifier();
							if (qualifiers != null && qualifiers.length > 0) {
								if (qualifier_value == null || qualifier_value.compareTo("") == 0) { // find qualifier value by matching qualifier name
									for (int j= 0; j < qualifiers.length; j++ ) {
										PropertyQualifier q = qualifiers[j];
										String name = q.getPropertyQualifierName();
										String value = q.getValue().getContent();

										if( name.compareTo(qualifier_name) == 0) {
											ret_qualifier_value = value;
											match_found = true;
											break;
										}
									}


								} else { // qualifier value (subsource_name) is given
									for (int j= 0; j < qualifiers.length; j++ ) {
										PropertyQualifier q = qualifiers[j];
										String name = q.getPropertyQualifierName();
										String value = q.getValue().getContent();

										if( name.compareTo(qualifier_name) == 0 && value.compareTo(qualifier_value) == 0) {
											for (int k= 0; k < qualifiers.length; k++ ) {
												q = qualifiers[k];
												String q_name = q.getPropertyQualifierName();
												String q_value = q.getValue().getContent();

												if( q_name.compareTo(SOURCE_CODE) == 0) {
													ret_qualifier_value = q_value;
													match_found = true;
													break;
												}
											}
										}
									}
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

				if (match && ret_qualifier_value != null) {
					num_matches++;
					if (num_matches == 1) {
						return_str = ret_qualifier_value;
					} else {
						return_str = return_str + delimiter + ret_qualifier_value;
					}
				}
			}
        }
        return return_str;
	}


    public String get_ReportColumnValue(LexEVSValueSetDefinitionServices definitionServices,
        String uri, String scheme, String version,
        Entity defining_root_concept, Entity associated_concept,
        Entity node, ReportColumn rc) throws Exception {

        String field_Id = rc.getFieldId();

        if (field_Id.compareTo("Blank") == 0) {
			return "";
		}

        if (field_Id.compareTo("Associated Concept Code") == 0
            && associated_concept != null
            && node != null
            && associated_concept.getEntityCode().compareTo( node.getEntityCode() ) == 0) {
			return "";
		}



        if (field_Id.compareTo("CDISC Submission Value") == 0) {
			return getCDISCSubmissionValue(scheme, version, associated_concept, node, rc);
		}


		Entity focused_concept = null;
        String property_name = rc.getPropertyName();
        String qualifier_name = rc.getQualifierName();
        String source = rc.getSource();
        String qualifier_value = rc.getQualifierValue();
        String representational_form = rc.getRepresentationalForm();
        Boolean isPreferred = rc.getIsPreferred();
        String property_type = rc.getPropertyType();
        char delimiter_ch = rc.getDelimiter();
        String delimiter = "" + delimiter_ch;
        //GF28844: delimiter = " " + delimiter + " ";

        if (isNull(field_Id))
            field_Id = null;
        if (isNull(property_name))
            property_name = null;


 if (property_name != null && property_name.compareTo("Extensible_List") == 0) {
	if (associated_concept != null && node != null && associated_concept.getEntityCode().compareTo( node.getEntityCode() ) != 0 ) return "";
 }

        if (isNull(qualifier_name))
            qualifier_name = null;
        if (isNull(source))
            source = null;
        if (isNull(qualifier_value))
            qualifier_value = null;
        if (isNull(representational_form))
            representational_form = null;
        if (isNull(property_type))
            property_type = null;
        if (isNull(delimiter))
            delimiter = null;

        Entity concept = node;
        if (property_name != null
            && property_name.compareTo("Contributing_Source") == 0) {
            concept = defining_root_concept;
		} else {
			concept = getFocusConcept(scheme, version, associated_concept, node, field_Id);
		}

        if (field_Id.endsWith("Code")) {
			String value = getFocusConceptCode(concept);
			return value;

		} else if (field_Id.compareTo("Associated Concept Property") == 0) {
            String value = getFocusConceptPropertyValue(
             associated_concept,
             property_name,
             property_type,
	         qualifier_name,
	         source,
	         qualifier_value,
	         representational_form,
	         delimiter,
	         isPreferred);
	         return value;

		} else if (field_Id.endsWith("Property Qualifier")) {
	         String value = getFocusConceptPropertyQualifierValue(
             concept,
             property_name,
             property_type,
	         qualifier_name,
	         source,
	         qualifier_value,
	         representational_form,
	         delimiter,
	         isPreferred);

	        return value;

		} else if (field_Id.endsWith("Property")) {
            String value = getFocusConceptPropertyValue(
             concept,
             property_name,
             property_type,
	         qualifier_name,
	         source,
	         qualifier_value,
	         representational_form,
	         delimiter,
	         isPreferred);
	         return value;
		}

		return null;
    }

    public String getCodeListPT(String code) {
		if (code == null) return null;
		if (_code2PTHashMap.containsKey(code)) {
			return (String) _code2PTHashMap.get(code);
		}
		return null;
	}

    public String getCDISCSubmissionValue(String scheme, String version,
        Entity associated_concept, Entity node, ReportColumn rc) {
		if (associated_concept == null || node == null) return null;

        String property_name = rc.getPropertyName();
        String qualifier_name = rc.getQualifierName();
        String source = rc.getSource();
        String qualifier_value = rc.getQualifierValue();
        String representational_form = rc.getRepresentationalForm();
        Boolean isPreferred = rc.getIsPreferred();
        String property_type = rc.getPropertyType();
        char delimiter_ch = rc.getDelimiter();
        String delimiter = "" + delimiter_ch;

        if (isNull(property_name))
            property_name = null;
        if (isNull(qualifier_name))
            qualifier_name = null;
        if (isNull(source))
            source = null;
        if (isNull(qualifier_value))
            qualifier_value = null;
        if (isNull(representational_form))
            representational_form = null;
        if (isNull(property_type))
            property_type = null;
        if (isNull(delimiter))
            delimiter = null;

        String codeListPT = getCodeListPT(associated_concept.getEntityCode());
        if (codeListPT == null) {
            String src_code = null;
    		codeListPT = getFocusConceptPropertyValue(
				associated_concept,
				property_name,
				property_type,
				null,
				source,
				src_code,
				"AB",
				delimiter,
				null);

			if (codeListPT != null) {
				_code2PTHashMap.put(associated_concept.getEntityCode(), codeListPT);
			}
		}

		if (codeListPT != null) {
			Vector qualifier_value_vec = new Vector();
			qualifier_value_vec.add(codeListPT);
			qualifier_name = SOURCE_CODE;
			String retval = getFocusConceptPropertyValue(
				node,
				property_name,
				property_type,
				qualifier_name,
				source,
				qualifier_value_vec,
				representational_form,
				delimiter,
				isPreferred);

			if (retval != null && retval.compareTo("") != 0) return retval;
		}

        String src_code = null;
    	return getFocusConceptPropertyValue(
			node,
            property_name,
            property_type,
	        null,
	        source,
	        src_code,
	        representational_form,
	        delimiter,
	        isPreferred);

		//return DataUtils.getPTBySourceCode(scheme, version, node.getEntityCode(), source, source_code);
	}


    public String getCDISCSubmissionValue(String scheme, String version,
        Entity associated_concept, Entity node, ReportColumn rc, String prefix) {
		if (associated_concept == null || node == null) return null;

		if (prefix == null) prefix = "SDTM-";
		// possible prefix: ADaM-, CDASH-, SDTM-, and SEND-

        String property_name = rc.getPropertyName();
        String qualifier_name = rc.getQualifierName();
        String source = rc.getSource();
        String qualifier_value = rc.getQualifierValue();
        String representational_form = rc.getRepresentationalForm();
        Boolean isPreferred = rc.getIsPreferred();
        String property_type = rc.getPropertyType();
        char delimiter_ch = rc.getDelimiter();
        String delimiter = "" + delimiter_ch;

        if (isNull(property_name))
            property_name = null;
        if (isNull(qualifier_name))
            qualifier_name = null;
        if (isNull(source))
            source = null;
        if (isNull(qualifier_value))
            qualifier_value = null;
        if (isNull(representational_form))
            representational_form = null;
        if (isNull(property_type))
            property_type = null;
        if (isNull(delimiter))
            delimiter = null;

        String codeListPT = getCodeListPT(associated_concept.getEntityCode());
        if (codeListPT == null) {
            String src_code = null;
    		codeListPT = getFocusConceptPropertyValue(
				associated_concept,
				property_name,
				property_type,//"PRESENTATION",
				null,
				source,
				src_code,
				"PT",
				delimiter,
				null);
			if (codeListPT != null) {
				_code2PTHashMap.put(associated_concept.getEntityCode(), codeListPT);
			}
		}

		if (codeListPT != null) {

			// May need to check for all possible prefixes:

			String source_code = prefix + codeListPT;
			qualifier_name = "source-code";
			String retval = getFocusConceptPropertyValue(
				node,
				property_name,
				property_type,
				qualifier_name,
				source,//"CDISC",
				source_code,
				representational_form,
				delimiter,
				isPreferred);

			if (retval != null && retval.compareTo("") != 0) return retval;
		}

        String src_code = null;
    	return getFocusConceptPropertyValue(
			node,
            property_name,
            property_type,
	        null,
	        source,
	        src_code,
	        representational_form,
	        delimiter,
	        isPreferred);
	}

    public void setReportColumns(ReportColumn[] cols) {
        for (int i = 0; i < cols.length; i++) {
			ReportColumn col = cols[i];
			String field_id = col.getFieldId();
            if (field_id.compareTo("CDISC Submission Value") == 0) {
				col.setFieldId("Property");
				col.setRepresentationalForm("PT");
				col.setSource("CDISC");
				col.setQualifierName(null);
				col.setQualifierValue(null);
		    }

		    if (field_id.compareTo("Associated Concept Code") == 0) {
				col.setFieldId("Blank");
			}

			if (col.getPropertyName() != null) {
				if (col.getPropertyName().compareTo("Extensible_List") == 0) {
					col.setFieldId("Property");
				}
			}
		}
	}

    public ReportColumn[] copyReportColumns(ReportColumn[] cols) {
        if (cols == null) return null;
        prev_subset_code = null;
        subheader_line_required = false;
        ReportColumn[] temporary_cols = new ReportColumn[cols.length];
		for (int i = 0; i < cols.length; i++) {
			ReportColumn col = cols[i];
			ReportColumn rc = new ReportColumn();
            rc.setColumnNumber(col.getColumnNumber());
            rc.setId(col.getId());
            rc.setLabel(col.getLabel());
            rc.setFieldId(col.getFieldId());

			if (col.getFieldId().compareTo("CDISC Submission Value") == 0) {
				prev_subset_code = "";
				subheader_line_required = true;
                KEY_INDEX_1	= i;
			}

			if (col.getFieldId().compareTo("Code") == 0) {
				KEY_INDEX_2	= i;
			}

            rc.setPropertyType(col.getPropertyType());
            rc.setPropertyName(col.getPropertyName());
            rc.setIsPreferred(col.getIsPreferred());
            rc.setRepresentationalForm(col.getRepresentationalForm());
            rc.setSource(col.getSource());
            rc.setQualifierName(col.getQualifierName());
            rc.setQualifierValue(col.getQualifierValue());
            rc.setDelimiter(col.getDelimiter());
            rc.setConditionalColumnId(col.getConditionalColumnId());
            temporary_cols[i] = rc;
		}
		return temporary_cols;
	}

	public Boolean generateStandardReport(String outputDir, String adminPassword, String templateFile, int[] ncitColumns) {
		RWUIUtils utils = new RWUIUtils();
		StandardReportTemplate template = utils.loadStandardReportTemplate(templateFile);
        if (ncitColumns == null) {
			Collection<ReportColumn> cc = template.getColumnCollection();
			ncitColumns = new int[cc.size()];
			for (int i=0; i<cc.size(); i++) {
				ncitColumns[i] = 0;
			}
		}
		return new ReportGenerationRunner().generateStandardReport(outputDir, adminPassword, template, ncitColumns);
	}
}

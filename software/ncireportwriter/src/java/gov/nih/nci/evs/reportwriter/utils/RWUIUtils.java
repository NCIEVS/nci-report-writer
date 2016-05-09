/*
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;

import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.formatter.*;

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

public class RWUIUtils { //implements Runnable {
    private static Logger _logger = Logger
        .getLogger(RWUIUtils.class);

    private String _outputDir = null;
    private String _standardReportLabel = null;
    private String _uid = null;
    private String _emailAddress = null;
    private int[] _ncitColumns = new int[] {};

    private int _count = 0;
    private String _hierarchicalAssoName = null;
    private int _abortLimit = 0;

    private HashMap _code2PTHashMap = null;

    private String prev_subset_code = null;
    private boolean subheader_line_required = false;
    private int KEY_INDEX_1 = 4; // for CDISC Sort
    private int KEY_INDEX_2 = 0;

    ReportColumn[] temp_cols = null;
    private static String SOURCE_CODE = "source-code";

    public RWUIUtils() {

    }

	public static Vector readFile(String filename)
	{
		Vector v = new Vector();
		try {
            FileReader a = new FileReader(filename);
            BufferedReader br = new BufferedReader(a);
            String line;
            line = br.readLine();
            while(line != null){
                v.add(line);
                line = br.readLine();
            }
            br.close();
		} catch (Exception ex) {
            ex.printStackTrace();
		}
		return v;
	}

    public ReportColumn createReportColumn(
		int columnNumber,
		String label,
		String fieldId,
		String propertyType,
		String propertyName,
		Boolean isPreferred,
		String representationalForm,
		String source,
		String qualifierName,
		String qualifierValue,
		Character delimiter,
		int conditionalColumnId) {

		ReportColumn rc = new ReportColumn();
		rc.setColumnNumber(columnNumber);
		rc.setLabel(label);
		rc.setFieldId(fieldId);
		rc.setPropertyType(propertyType);
		rc.setPropertyName(propertyName);
		rc.setIsPreferred(isPreferred);
		rc.setRepresentationalForm(representationalForm);
		rc.setSource(source);
		rc.setQualifierName(qualifierName);
		rc.setQualifierValue(qualifierValue);
		rc.setDelimiter(delimiter);
		rc.setConditionalColumnId(conditionalColumnId);
		return rc;
	}



    public StandardReportTemplate createStandardReportTemplate(
		String template_label,
		String rootConceptCode,
		String codingSchemename,
		String codingSchemeVersion,
		String associationName,
		Boolean direction,
		int level,
		Character delimiter) {

		StandardReportTemplate template = new StandardReportTemplate();
		template.setLabel(template_label);
		template.setRootConceptCode(rootConceptCode);
		template.setCodingSchemeName(codingSchemename);
		template.setCodingSchemeVersion(codingSchemeVersion);
		template.setAssociationName(associationName);
		template.setDirection(direction);
		template.setLevel(level);
		template.setDelimiter(delimiter);
        return template;
    }

	public StandardReportTemplate loadStandardReportTemplate(String filename) {
		StandardReportTemplate template = new StandardReportTemplate();
		Vector v = readFile(filename);
		boolean header = true;

		String template_label = null;
		String rootConceptCode = null;
		String codingSchemeName = null;
		String codingSchemeVersion = null;
		String associationName = null;
		Boolean direction = null;
		int level = 0;
		Character delimiter = null;

		int columnNumber = 0;
		String label = null;
		String fieldId = null;
		String propertyType = null;
		String propertyName = null;
		Boolean isPreferred = null;
		String representationalForm = null;
		String source = null;
		String qualifierName = null;
		String qualifierValue = null;
		int conditionalColumnId = 0;

		List<ReportColumn> cols = new ArrayList();

		for (int i=0; i<v.size(); i++) {
			String t = (String) v.elementAt(i);
			t = t.trim();
			if (t.length()> 0) {
				if (t.startsWith("columnCollection")) {
					header = false;
					template = createStandardReportTemplate(
						template_label,
						rootConceptCode,
						codingSchemeName,
						codingSchemeVersion,
						associationName,
						direction,
						level,
						delimiter);

					template_label = null;
					rootConceptCode = null;
					codingSchemeName = null;
					codingSchemeVersion = null;
					associationName = null;
					direction = null;
					level = 0;
					delimiter = null;

				} else if (t.startsWith("columnNumber")) {
					//System.out.println("\n");
				}
				if (header) {
					int n = t.indexOf(":");
					String key = t.substring(0, n);
					String value = t.substring(n+1, t.length());
					if (key.compareTo("label") == 0) {
						if (value.length() > 0) {
							template_label = value;
					    }
					} else if (key.compareTo("rootConceptCode") == 0) {
						if (value.length() > 0) {
							rootConceptCode = value;
					    }
					} else if (key.compareTo("codingSchemeName") == 0) {
						if (value.length() > 0) {
							codingSchemeName = value;
					    }
					} else if (key.compareTo("codingSchemeVersion") == 0) {
						//KLO 05092106
						if (!DataUtils.isNullOrBlank(DataUtils.NCIT_VERSION)) {
							codingSchemeVersion = DataUtils.NCIT_VERSION;
						} else {
							if (value.length() > 0) {
								codingSchemeVersion = value;
							}
					    }
					} else if (key.compareTo("associationName") == 0) {
						if (value.length() > 0) {
							associationName = value;
					    }
					} else if (key.compareTo("direction") == 0) {
						if (value.compareTo("source") == 0 || value.compareTo("false") == 0) {
							direction = false;
						} else if (value.compareTo("target") == 0 || value.compareTo("true") == 0) {
							direction = true;
						}
					} else if (key.compareTo("level") == 0) {
						if (value.compareTo("ALL") == 0) {
							level = -1;
						} else {
							if (value.length() > 0) {
								level = Integer.parseInt(value);
						    }
						}
					} else if (key.compareTo("delimiter") == 0) {
						if (value.length() > 0) {
							char c = value.charAt(0);
							delimiter = new Character(c);
					    }
					}

				} else {
					int n = t.indexOf(":");
					String key = t.substring(0, n);
					String value = t.substring(n+1, t.length());
					//System.out.println("\t" + key + "-->" + value);

					if (key.compareTo("label") == 0) {
						label = value;
					} else if (key.compareTo("columnNumber") == 0) {
						if (value.length() > 0) {
							columnNumber = Integer.parseInt(value);
					    }
					} else if (key.compareTo("fieldId") == 0) {
						if (value.length() > 0) {
							fieldId = value;
						}
					} else if (key.compareTo("propertyType") == 0) {
						if (value.length() > 0) {
							propertyType = value;
						}
					} else if (key.compareTo("propertyName") == 0) {
						if (value.length() > 0) {
							propertyName = value;
						}
					} else if (key.compareTo("associationName") == 0) {
						if (value.length() > 0) {
							associationName = value;
						}
					} else if (key.compareTo("isPreferred") == 0) {
						if (value.length() > 0) {
						    isPreferred = null;
							if (value.length() > 0) {
								if (value.compareTo("true") == 0) {
									isPreferred = Boolean.TRUE;
								} else if (value.compareTo("false") == 0) {
									isPreferred = Boolean.FALSE;
								}
							}
					    }
					} else if (key.compareTo("representationalForm") == 0) {
						if (value.length() > 0) {
                        	representationalForm = value;
					    }
					} else if (key.compareTo("source") == 0) {
						if (value.length() > 0) {
                        	source = value;
					    }
					} else if (key.compareTo("qualifierName") == 0) {
						if (value.length() > 0) {
                        	qualifierName = value;
					    }
					} else if (key.compareTo("qualifierValue") == 0) {
						if (value.length() > 0) {
                        	qualifierValue = value;
					    }
					} else if (key.compareTo("delimiter") == 0) {
						if (value.length() > 0) {
							char c = value.charAt(0);
							delimiter = new Character(c);
					    }
					} else if (key.compareTo("conditionalColumnId") == 0) {
						if (value.length() > 0) {
							conditionalColumnId = Integer.parseInt(value);
						}
						ReportColumn rc = createReportColumn(
							columnNumber,
							label,
							fieldId,
							propertyType,
							propertyName,
							isPreferred,
							representationalForm,
							source,
							qualifierName,
							qualifierValue,
							delimiter,
							conditionalColumnId);

                        cols.add(rc);
						columnNumber = 0;
						label = null;
						fieldId = null;
						propertyType = null;
						propertyName = null;
						isPreferred = null;
						representationalForm = null;
						source = null;
						qualifierName = null;
						qualifierValue = null;
						conditionalColumnId = 0;
					}
				}
		    }
		}
	    template.setColumnCollection(cols);
        return template;
	}


	public void dumpReportColumn(ReportColumn rc) {
		System.out.println("\tColumnNumber: " + rc.getColumnNumber());
		System.out.println("\tLable: " + rc.getLabel());
		System.out.println("\tFieldId: " + rc.getFieldId());
		System.out.println("\tPropertyType: " + rc.getPropertyType());
		System.out.println("\tPropertyName: " + rc.getPropertyName());
		System.out.println("\tIsPreferred: " + rc.getIsPreferred());
		System.out.println("\tRepresentationalForm: " + rc.getRepresentationalForm());
		System.out.println("\tSource: " + rc.getSource());
		System.out.println("\tQualifierName: " + rc.getQualifierName());
		System.out.println("\tQualifierValue: " + rc.getQualifierValue());
		System.out.println("\tConditionalColumnId: " + rc.getConditionalColumnId());
		System.out.println("\n");
	}


	public void dumpStandardReportTemplate(StandardReportTemplate template) {
		if (template == null) return;
		System.out.println("Lable: " + template.getLabel());
		System.out.println("RootConceptCode: " + template.getRootConceptCode());
		System.out.println("CodingSchemeName: " + template.getCodingSchemeName());
		System.out.println("CodingSchemeVersion: " + template.getCodingSchemeVersion());
		System.out.println("AssociationName: " + template.getAssociationName());
		System.out.println("Direction: " + template.getDirection());
		System.out.println("Level: " + template.getLevel());
		System.out.println("Delimiter: " + template.getDelimiter());

		Object[] objs = null;
		Collection<ReportColumn> cc =
			template.getColumnCollection();
		objs = cc.toArray();
		ReportColumn[] cols = null;
		if (cc != null) {
			cols = new ReportColumn[objs.length];
			temp_cols = new ReportColumn[objs.length];
			for (int i = 0; i < objs.length; i++) {
				gov.nih.nci.evs.reportwriter.bean.ReportColumn col =
					(gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];
				cols[i] = col;
			}
		}
		ReportColumn[] temp_cols = null;
		temp_cols = copyReportColumns(cols);

		for (int i=0; i<temp_cols.length; i++) {
			ReportColumn rc = (ReportColumn) temp_cols[i];
			dumpReportColumn(rc);
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

    public static PrintWriter openPrintWriter(String outputfile) {
        try {
            PrintWriter pw =
                new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
            return pw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closePrintWriter(PrintWriter pw) {
        if (pw == null) {
            _logger.warn("PrintWriter is not open.");
            return;
        }
        pw.close();
        pw = null;
    }

	public static void main(String[] args) {
		RWUIUtils runner = new RWUIUtils();
		StandardReportTemplate template = runner.loadStandardReportTemplate("template.dat");
		runner.dumpStandardReportTemplate(template);
	}
}

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
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class ReportGenerationThread implements Runnable {
    private static Logger _logger = Logger
        .getLogger(ReportGenerationThread.class);

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

    public ReportGenerationThread(String outputDir, String standardReportLabel,
        String uid, String emailAddress, int[] ncitColumns) {
        _outputDir = outputDir;
        _standardReportLabel = standardReportLabel;
        _uid = uid;
        _emailAddress = emailAddress;
        _ncitColumns = ncitColumns;

        _count = 0;

        _code2PTHashMap = new HashMap();
    }

    public Boolean warningMsg(StringBuffer buffer, String text) {
        buffer.append(text);
        _logger.warn(text);
        return Boolean.FALSE;
    }

    public PrintWriter openPrintWriter(String outputfile) {
        try {
            PrintWriter pw =
                new PrintWriter(new BufferedWriter(new FileWriter(outputfile)));
            return pw;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closePrintWriter(PrintWriter pw) {
        if (pw == null) {
            _logger.warn("PrintWriter is not open.");
            return;
        }
        pw.close();
        pw = null;
    }

    public void run() {
        _logger.info("Generating report -- please wait...");
        StringBuffer warningMsg = new StringBuffer();
        Date startDate = new Date();
        StopWatch stopWatch = new StopWatch();
        Boolean successful = true;
        try {
            successful =
                generateStandardReport(_outputDir, _standardReportLabel, _uid,
                    warningMsg);
            long runTime = stopWatch.getDuration();
            emailNotification(successful, _standardReportLabel,
                warningMsg, startDate, new Date(), runTime);
            _logger.info("Report " + _standardReportLabel + " generated.");
            _logger.info("  * Start time: " + startDate);
            _logger.info("  * Run time: " + stopWatch.getResult(runTime));
        } catch (Exception e) {
            warningMsg.append("\n" + ExceptionUtils.getStackTrace(e));
            long runTime = stopWatch.getDuration();
            emailNotification(false, _standardReportLabel,
                warningMsg, startDate, new Date(), runTime);
            ExceptionUtils.print(_logger, e, "* In ReportGenerationThread.run");
            e.printStackTrace();
        }
    }

    private void emailNotification(boolean successful, String standardReportLabel,
        StringBuffer warningMsg,
        Date startDate, Date endDate, long runTime) {
        try {
            if (_emailAddress == null || _emailAddress.length() <= 0) {
                _logger.warn("Email notification is not sent for report "
                    + _standardReportLabel + ".");
                _logger.warn("  * email address not set.");
                return;
            }

            String mailServer =
                AppProperties.getInstance().getProperty(
                    AppProperties.MAIL_SMTP_SERVER);
            String from = _emailAddress;
            String recipients = _emailAddress;
            String subject = "";
            StringBuffer message = new StringBuffer();

            boolean displaySummary = false;
            if (displaySummary) {
                message.append(_standardReportLabel + ", " +
                    StopWatch.format(StopWatch.timeInMinutes(runTime)) + " minutes, " +
                    _count + " rows\n");
                message.append("\n");
                message.append(StringUtils.SEPARATOR + "\n");
            }

            if (successful) {
                subject = "Generated: " + _standardReportLabel + "...";
                message.append(_standardReportLabel + " is generated.\n");
            } else {
                subject = "Failed generating: " + _standardReportLabel + "...";
                message.append(_standardReportLabel + " failed to generated.\n");
                message.append(warningMsg + "\n");
            }

            message.append("\n");
            message.append(StringUtils.SEPARATOR + "\n");
            message.append("LexEVS: " + AppProperties.getInstance().getProperty(
                AppProperties.EVS_SERVICE_URL) + "\n");
            message.append("Started:   " + startDate + "\n");
            message.append("Completed: " + endDate + "\n");
            message.append("Report generation time: \n");
            message.append("  * In hours: "
                + StopWatch.format(StopWatch.timeInHours(runTime)) + "\n");
            message.append("  * In minutes: "
                + StopWatch.format(StopWatch.timeInMinutes(runTime)) + "\n");
            message.append("Total number of concepts processed: " + _count + "\n");

            message.append("\n");
            message.append(StringUtils.SEPARATOR + "\n");
            message.append("Report Template Parameters:\n");
            try {
                StandardReportTemplate standardReportTemplate = null;
                standardReportTemplate = getStandardReportTemplate(
                    standardReportLabel, warningMsg);
                message.append("  * Label: " +
                    standardReportTemplate.getLabel() + "\n");
                message.append("  * Coding Scheme Name: " +
                    standardReportTemplate.getCodingSchemeName() + "\n");
                message.append("  * Coding Scheme Version: " +
                    standardReportTemplate.getCodingSchemeVersion() + "\n");
                message.append("  * Root Concept Code: " +
                    standardReportTemplate.getRootConceptCode() + "\n");
                message.append("  * Association Name: " +
                    standardReportTemplate.getAssociationName() + "\n");
                Boolean dirValue = standardReportTemplate.getDirection();
                String dirStr = dirValue ? "Target" : "Source";
                message.append("  * Direction: " + dirStr +
                    "(" + dirValue + ")" + "\n");
                message.append("  * Level: " +
                    standardReportTemplate.getLevel() + "\n");
            } catch (Exception e) {
                message.append("  * Exception: " + e.getMessage() + "\n");
            }

            boolean send = true;
            MailUtils.postMail(mailServer, from, recipients, subject,
                message.toString(), send);
        } catch (Exception e) {
            ExceptionUtils.print(_logger, e);
            e.printStackTrace();
        }
    }

    private StandardReportTemplate getStandardReportTemplate(
        String standardReportLabel, StringBuffer warningMsg) throws Exception {
        StandardReportTemplate standardReportTemplate = null;
        SDKClientUtil sdkclientutil = new SDKClientUtil();
        String FQName =
            "gov.nih.nci.evs.reportwriter.bean.StandardReportTemplate";
        String methodName = "setLabel";
        Object obj =
            sdkclientutil.search(FQName, methodName, standardReportLabel);
        standardReportTemplate = (StandardReportTemplate) obj;
        _logger.debug("standardReportTemplate ID: "
            + standardReportTemplate.getId());
        _logger.debug("standardReportTemplate label: "
            + standardReportTemplate.getLabel());
        return standardReportTemplate;
    }

    public Boolean generateStandardReport(String outputDir,
        String standardReportLabel, String uid, StringBuffer warningMsg) {
        StandardReportTemplate standardReportTemplate = null;
        try {
            standardReportTemplate = getStandardReportTemplate(
                standardReportLabel, warningMsg);
        } catch (Exception e) {
            return warningMsg(warningMsg, "Unable to identify report label "
                + standardReportLabel + " -- report not generated.");
        }

        try {
            String defining_set_desc =
                standardReportTemplate.getRootConceptCode();

            if (defining_set_desc.indexOf("|") != -1) {
                return generateSpecialReport(outputDir, standardReportLabel,
                    uid, warningMsg);
            }

            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("Method: generateStandardReport");
            _logger.debug("  * Output directory: " + outputDir);
            _logger.debug("  * standardReportLabel: " + standardReportLabel);
            _logger.debug("  * uid: " + uid);

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

            String version = standardReportTemplate.getCodingSchemeVersion();
            // append version to the report file name:
            String pathname =
                outputDir + File.separator + standardReportLabel + "__"
                    + version + ".txt";
            pathname = pathname.replaceAll(" ", "_");
            _logger.debug("Full path name: " + pathname);

            PrintWriter pw = openPrintWriter(pathname);
            if (pw == null)
                throw new Exception("Unable to create output file " + pathname
                    + " -- please check privilege setting.");
            _logger.debug("opened PrintWriter " + pathname);

            int id = standardReportTemplate.getId();
            String label = standardReportTemplate.getLabel();
            String codingSchemeName =
                standardReportTemplate.getCodingSchemeName();
            String codingSchemeVersion =
                standardReportTemplate.getCodingSchemeVersion();
            String rootConceptCode =
                standardReportTemplate.getRootConceptCode();
            String associationName =
                standardReportTemplate.getAssociationName();
            boolean direction = standardReportTemplate.getDirection();
            int level = standardReportTemplate.getLevel();
            Character delimiter = standardReportTemplate.getDelimiter();
            String delimeter_str = "\t";

            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("  * ID: " + id);
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
                    Debug.print(col);
                    cols[i] = col;
                }
            }

            temp_cols = copyReportColumns(cols);
            setReportColumns(temp_cols);


            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("* Start generating report..." + pathname);

            printReportHeading(pw, cols);

            String scheme = standardReportTemplate.getCodingSchemeName();
            version = standardReportTemplate.getCodingSchemeVersion();

            String code = standardReportTemplate.getRootConceptCode();
            Entity defining_root_concept =
                DataUtils.getConceptByCode(codingSchemeName,
                    codingSchemeVersion, null, rootConceptCode);

            associationName = standardReportTemplate.getAssociationName();
            // associationName = "A8"; //NOTE_A8: A8 vs Concept_In_Subset
            level = standardReportTemplate.getLevel();

            String tag = null;
            int curr_level = 0;
            int max_level = standardReportTemplate.getLevel();
            if (max_level < 0)
                max_level =
                    AppProperties.getInstance().getIntProperty(
                        AppProperties.MAXIMUM_LEVEL, 20);

            // printReportHeading(pw, cols);
            if (_hierarchicalAssoName == null) {
                Vector<String> hierarchicalAssoName_vec =
                    DataUtils.getHierarchyAssociationId(scheme, version);
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
            LexEVSValueSetDefinitionServices definitionServices =
                DataUtils.getValueSetDefinitionService();
            String uri = DataUtils.codingSchemeName2URI(scheme, version);

            traverse(definitionServices, uri, pw, scheme, version, tag, defining_root_concept, code,
                _hierarchicalAssoName, associationName, direction, curr_level,
                max_level, cols);
            closePrintWriter(pw);

            _logger.debug("Total number of concepts processed: " + _count);
            _logger.debug("Output file " + pathname + " generated.");

            return createStandardReports(outputDir, standardReportLabel, uid,
                standardReportTemplate, pathname, version, delimeter_str);
        } catch (Exception e) {
            return warningMsg(warningMsg, ExceptionUtils.getStackTrace(e));
        }
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

/*
    private void writeColumnData(LexEVSValueSetDefinitionServices definitionServices,
        String uri, PrintWriter pw, String scheme, String version,
        Entity defining_root_concept, Entity associated_concept, Entity c,
        String delim, ReportColumn[] cols) throws Exception {
        Vector<String> values = new Vector<String>();

//        _cdiscInfo.isExtensibleValue = false;

        for (int i = 0; i < cols.length; i++) {
            ReportColumn rc = (ReportColumn) cols[i];
            String value =
                get_ReportColumnValue(definitionServices, uri,
                    scheme, version, defining_root_concept,
                    associated_concept, c, rc);


            //if (SpecialCases.CDISC.ON &&
            //    SpecialCases.CDISC.writeExtensibleColumnData(_cdiscInfo, rc, values, value, i)) {
            //    if (_cdiscInfo.skipRow)
            //        return;
            //    value = _cdiscInfo.newValue;
            //}


            if (value == null) value = "";
            values.add(value);

            //_logger.debug("add value: " + value);

        }


        //if (SpecialCases.CDISC.ON) {
        //    SpecialCases.CDISC.writeSubheader(
        //        definitionServices, uri,
        //        _cdiscInfo, this, values, pw, scheme,
        //        version, defining_root_concept, associated_concept, c, delim, cols);
        //}

        pw.println(StringUtils.toString(values, delim, true));

        _count++;
        if ((_count / 100) * 100 == _count) {
            _logger.debug("Number of concepts processed: " + _count);
        }
    }
*/


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
        /*
        _count++;
        if ((_count / 100) * 100 == _count) {
            _logger.debug("Number of concepts processed: " + _count);
        }
        */

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


//_logger.debug("prev_subset_code: " + prev_subset_code);
//_logger.debug("root.getEntityCode(): " + root.getEntityCode());


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


        // Note: Commented on 2/24/10 (Wed). subconcept_vec size was 0.
        // Vector<Entity> subconcept_vec =
        // DataUtils.getAssociationTargets(scheme, version, root
        // .getEntityCode(), hierarchyAssociationName);

        Vector<String> subconcept_vec =
            DataUtils
                .getSubconceptCodes2(scheme, version, root.getEntityCode());

        //Vector<Entity> subconcept_vec = DataUtils.getSubconcepts(scheme, version, code);
        if (subconcept_vec == null | subconcept_vec.size() == 0)
            return;
        level++;
        for (int k = 0; k < subconcept_vec.size(); k++) {
            // Note: Commented on 2/24/10 (Wed). subconcept_vec size was 0.
            // Entity concept = (Entity) subconcept_vec.elementAt(k);
            //String subconcep_code = concept.getEntityCode();
            //Entity e = (Entity) subconcept_vec.elementAt(k);
            String subconcep_code = subconcept_vec.elementAt(k);

            traverse(definitionServices, uri, pw, scheme, version, tag, defining_root_concept,
                subconcep_code, hierarchyAssociationName, associationName,
                direction, level, maxLevel, cols);
        }
    }

/*
    public String getReportColumnValue(LexEVSValueSetDefinitionServices definitionServices,
        String uri, String scheme, String version,
        Entity defining_root_concept, Entity associated_concept,
        Entity node, ReportColumn rc) throws Exception {
        String field_Id = rc.getFieldId();
        String property_name = rc.getPropertyName();
        String qualifier_name = rc.getQualifierName();
        String source = rc.getSource();
        String qualifier_value = rc.getQualifierValue();
        String representational_form = rc.getRepresentationalForm();

        // check:
        Boolean isPreferred = rc.getIsPreferred();

        String property_type = rc.getPropertyType();
        char delimiter_ch = rc.getDelimiter();
        String delimiter = "" + delimiter_ch;
        //GF28844: delimiter = " " + delimiter + " ";

        if (isNull(field_Id))
            field_Id = null;
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

        String label = rc.getLabel().toUpperCase();

        String assocName = SpecialCases.GetHasParent.getAssocName(field_Id);
        if (assocName != null && assocName.length() > 0) {

            Entity concept = SpecialCases.GetHasParent.getAssociatedConcept(
                definitionServices, uri, scheme, version, node, field_Id);
            if (concept != null)
                associated_concept = concept;
            else return "";
        }

        if (SpecialCases.CDISC.ON) {
            String cdiscValue =
                SpecialCases.CDISC.getSubmissionValue(label, node,
                    associated_concept, delimiter);
            if (cdiscValue != null)
                return cdiscValue;
        }

        if (field_Id.equals("Code"))
            return node.getEntityCode();
        if (field_Id.contains("Associated Concept Code")) {
            if (associated_concept == null)
                return "";
            return associated_concept.getEntityCode();
        }



        Entity concept = node;

        if (property_name != null
            && property_name.compareTo("Contributing_Source") == 0) {
            concept = defining_root_concept;
        } else if (field_Id.indexOf("Associated") != -1) {
            if (associated_concept == null)
                return "";
            concept = associated_concept;
        } else if (field_Id.indexOf("Parent") != -1) {
            if (_hierarchicalAssoName == null) {
                Vector<String> hierarchicalAssoName_vec =
                    DataUtils.getHierarchyAssociationId(scheme, version);
                if (hierarchicalAssoName_vec == null
                    || hierarchicalAssoName_vec.size() == 0) {
                    return null;
                }
                _hierarchicalAssoName =
                    (String) hierarchicalAssoName_vec.elementAt(0);
            }
            // Vector superconcept_vec = new DataUtils().getParentCodes(scheme,
            // version, node.getId());
            Vector<String> superconcept_vec =
                DataUtils.getAssociationSourceCodes(scheme, version,
                    node.getEntityCode(), _hierarchicalAssoName);
            if (superconcept_vec != null && superconcept_vec.size() > 0
                && field_Id.indexOf("1st Parent") != -1) {
                String superconceptCode =
                    (String) superconcept_vec
                        .elementAt(superconcept_vec.size() - 1);
                if (field_Id.equals("1st Parent Code"))
                    return superconceptCode;
                concept =
                    DataUtils.getConceptByCode(scheme, version, null,
                        superconceptCode);
            } else if (superconcept_vec != null && superconcept_vec.size() > 1
                && field_Id.indexOf("2nd Parent") != -1) {
                String superconceptCode =
                    (String) superconcept_vec
                        .elementAt(superconcept_vec.size() - 2);
                if (field_Id.equals("2nd Parent Code"))
                    return superconceptCode;
                concept =
                    DataUtils.getConceptByCode(scheme, version, null,
                        superconceptCode);

            } else {
                return " ";
            }
        }

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
        String return_str = ""; // RWW change from space to empty string
        int num_matches = 0;
        if (field_Id.indexOf("Property Qualifier") != -1) {
            // getRepresentationalForm
            boolean match = false;
            boolean has_qualval = false; //GF28940 RWW

            for (int i = 0; i < properties.length && !has_qualval; i++) {  //GF28940 RWW: there can only be one!
                //GF28940 RWW: commented
                //qualifier_value = null;
                org.LexGrid.commonTypes.Property p = properties[i];
                if (p.getPropertyName().compareTo(property_name) == 0) // focus
                // on
                // matching
                // property
                {
                    match = true;

                    // <P90><![CDATA[<term-name>Black</term-name><term-group>PT</term-group><term-source>CDC</term-source><source-code>2056-0</source-code>]]></P90>

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
                            // qualifier, if needed
                            {
                                boolean match_found = false;
                                PropertyQualifier[] qualifiers =
                                    p.getPropertyQualifier();

                                if( qualifiers != null && qualifier_value != null ) {
                                    for (int j= 0; j < qualifiers.length; j++ ) {
                                        PropertyQualifier q = qualifiers[j];
                                        String name = q.getPropertyQualifierName();
                                        String value = q.getValue().getContent();
                                        //GF28940 RWW: there's no place in the model for another qualifier name
                                        //so I have to do this for "Property Qualifier" columns with a qualifier value
                                        //and luckily we don't have any using this!
                                        if( name.compareTo("subsource-name") == 0 && value.compareTo(qualifier_value) == 0 ) {
                                            has_qualval = true;
                                        }
                                    }
                                }

                                if (qualifiers != null && has_qualval ) {
                                    for (int j = 0; j < qualifiers.length; j++) {
                                        PropertyQualifier q = qualifiers[j];
                                        String name =
                                            q.getPropertyQualifierName();
                                        String value =
                                            q.getValue().getContent();
                                        if (qualifier_name.compareTo(name) == 0) {
                                            match_found = true;
                                            qualifier_value = value;
                                            break;
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
                }
                if (match && qualifier_value != null) {
                    num_matches++;
                    if (num_matches == 1) {
                        return_str = qualifier_value;
                    } else {
                        return_str = return_str + delimiter + qualifier_value;
                    }
                }
            }
            return return_str;
        }

        else if (field_Id.compareToIgnoreCase("Property") == 0
            || field_Id.contains("Associated Concept Property")
            || field_Id.indexOf("Parent Property") != -1)

        {
            for (int i = 0; i < properties.length; i++) {
                boolean match = false;
                org.LexGrid.commonTypes.Property p = properties[i];
                String propertyName = p.getPropertyName();
                if (propertyName.compareTo(property_name) == 0) // focus
                // on
                // matching
                // property
                {
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

                        // match representational_form
                        if (match) {
                            if (representational_form != null
                                && p instanceof Presentation) {
                                Presentation presentation = (Presentation) p;
                                String representationalForm = presentation.getRepresentationalForm();
                                if (representationalForm.compareTo(representational_form) != 0) {
                                    match = false;
                                }
                            }
                        }
                        // match qualifier
                        if (match) {
                            if (qualifier_name != null) // match property
                            // qualifier, if needed
                            {
                                boolean match_found = false;
                                PropertyQualifier[] qualifiers =
                                    p.getPropertyQualifier();
                                for (int j = 0; j < qualifiers.length; j++) {
                                    PropertyQualifier q = qualifiers[j];
                                    String name = q.getPropertyQualifierName();
                                    String value = q.getValue().getContent();
                                    if (qualifier_name.compareTo(name) == 0
                                        && qualifier_value.compareTo(value) == 0) {
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
                }
                if (match) {
                    num_matches++;
                    if (num_matches == 1) {
                        return_str = p.getValue().getContent();
                    } else {
                        return_str =
                            return_str + delimiter + p.getValue().getContent();
                    }
                }
            }

        }
        return return_str;
    }
*/
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

    public Boolean generateSpecialReport(String outputDir,
        String standardReportLabel, String uid, StringBuffer warningMsg) {
        StandardReportTemplate standardReportTemplate = null;
        try {
            standardReportTemplate = getStandardReportTemplate(
                standardReportLabel, warningMsg);
        } catch (Exception e) {
            return warningMsg(warningMsg, "Unable to identify report label "
                + standardReportLabel + " -- report not generated.");
        }

        try {
            String queryString = standardReportTemplate.getRootConceptCode();
            String delimiter = "|";
            Vector<String> v = parseData(queryString, delimiter);

            String property = (String) v.elementAt(0);
            String source = (String) v.elementAt(1);
            String qualifier_name = (String) v.elementAt(2);
            String qualifier_value = (String) v.elementAt(3);
            String matchText = (String) v.elementAt(4);
            String matchAlgorithm = (String) v.elementAt(5);

            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("Method: generateSpecialReport");
            _logger.debug("  * Output directory: " + outputDir);
            _logger.debug("  * standardReportLabel: " + standardReportLabel);
            _logger.debug("  * uid: " + uid);

            File dir = new File(outputDir);
            if (!dir.exists()) {
                _logger.debug("Output directory " + outputDir
                    + " does not exist -- try to create the directory.");
                boolean retval = dir.mkdir();
                if (!retval) {
                    throw new Exception("Unable to create output directory "
                        + outputDir + " -- please check privilege setting.");
                } else {
                    _logger.debug("Output directory: " + outputDir
                        + " created.");
                }
            } else {
                _logger.debug("Output directory: " + outputDir + " exists.");
            }

            String version = standardReportTemplate.getCodingSchemeVersion();
            String pathname =
                outputDir + File.separator + standardReportLabel + "__"
                    + version + ".txt";
            pathname = pathname.replaceAll(" ", "_");
            _logger.debug("Full path name: " + pathname);

            PrintWriter pw = openPrintWriter(pathname);
            if (pw == null)
                throw new Exception("Unable to create output file " + pathname
                    + " -- please check privilege setting.");
            _logger.debug("opened PrintWriter " + pathname);

            int id = standardReportTemplate.getId();
            String label = standardReportTemplate.getLabel();
            String codingSchemeName =
                standardReportTemplate.getCodingSchemeName();
            String codingSchemeVersion =
                standardReportTemplate.getCodingSchemeVersion();
            String rootConceptCode =
                standardReportTemplate.getRootConceptCode();
            String associationName =
                standardReportTemplate.getAssociationName();
            boolean direction = standardReportTemplate.getDirection();
            int level = standardReportTemplate.getLevel();
            // char delim = '$';
            // Character delimiter = standardReportTemplate.getDelimiter();
            String delimeter_str = "\t";

            _logger.debug("  * ID: " + id);
            _logger.debug("  * Label: " + label);
            _logger.debug("  * CodingSchemeName: " + codingSchemeName);
            _logger.debug("  * CodingSchemeVersion: " + codingSchemeVersion);
            _logger.debug("  * Root: " + rootConceptCode);
            _logger.debug("  * AssociationName: " + associationName);
            _logger.debug("  * Direction: " + direction);
            _logger.debug("  * Level: " + level);
            // _logger.debug("Delimiter: " + delimiter);

            Object[] objs = null;
            Collection<ReportColumn> cc =
                standardReportTemplate.getColumnCollection();
            if (cc == null) {
                throw new Exception(
                    "standardReportTemplate.getColumnCollection"
                        + " returned null???");
            } else {
                objs = cc.toArray();
            }

            ReportColumn[] cols = null;
            if (cc != null) {
                cols = new ReportColumn[objs.length];
                for (int i = 0; i < objs.length; i++) {
                    gov.nih.nci.evs.reportwriter.bean.ReportColumn col =
                        (gov.nih.nci.evs.reportwriter.bean.ReportColumn) objs[i];
                    Debug.print(col);
                    cols[i] = col;
                }
            }

            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("* Start generating report..." + pathname);

            printReportHeading(pw, cols);

            // String scheme = standardReportTemplate.getCodingSchemeName();
            version = standardReportTemplate.getCodingSchemeVersion();

            Vector<String> property_vec = null;
            if (property != null && property.compareTo("null") != 0) {
                property_vec = new Vector<String>();
                property_vec.add(property);
            }

            Vector<String> source_vec = null;
            if (source != null && source.compareTo("null") != 0) {
                source_vec = new Vector<String>();
                source_vec.add(source);
            }

            Vector<String> qualifier_name_vec = null;
            if (qualifier_name != null && qualifier_name.compareTo("null") != 0) {
                qualifier_name_vec = new Vector<String>();
                qualifier_name_vec.add(qualifier_name);
            }

            Vector<String> qualifier_value_vec = null;
            if (qualifier_value != null
                && qualifier_value.compareTo("null") != 0) {
                qualifier_value_vec = new Vector<String>();
                qualifier_value_vec.add(qualifier_value);
            }

            int maxToReturn = 10000;
            String language = null;

            Vector<Entity> concept_vec =
                DataUtils.restrictToMatchingProperty(codingSchemeName, version,
                    property_vec, source_vec, qualifier_name_vec,
                    qualifier_value_vec, matchText, matchAlgorithm, language,
                    maxToReturn);
            _logger.debug("concept_vec.size(): " + concept_vec.size());

            String delim = "\t";
            LexEVSValueSetDefinitionServices definitionServices =
                DataUtils.getValueSetDefinitionService();
            String uri = DataUtils.codingSchemeName2URI(codingSchemeName, version);
            for (int i = 0; i < concept_vec.size(); i++) {
                Entity c = (Entity) concept_vec.elementAt(i);
                writeColumnData(definitionServices, uri,
                    pw, codingSchemeName, version, null, null, c,
                    delim, cols, true);
            }

            closePrintWriter(pw);
            _logger.debug("Generated output file: " + pathname);
            return createStandardReports(outputDir, standardReportLabel, uid,
                standardReportTemplate, pathname, version, delimeter_str);
        } catch (Exception e) {
            return warningMsg(warningMsg, ExceptionUtils.getStackTrace(e));
        }
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


    public static String getFocusConceptPropertyValue(Entity concept,
            String property_name,
            String property_type,
	        String qualifier_name,
	        String source,
	        String qualifier_value,
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

					// match representational_form
					if (match) {
						if (representational_form != null
							&& p instanceof Presentation) {
							Presentation presentation = (Presentation) p;
							String representationalForm = presentation.getRepresentationalForm();
							if (representationalForm.compareTo(representational_form) != 0) {
								match = false;
							}
						}
					}
					// match qualifier
					if (match) {
						if (qualifier_value != null) // match qualifier name vaue pair
						// qualifier, if needed
						{
							boolean match_found = false;
							PropertyQualifier[] qualifiers =
								p.getPropertyQualifier();
							for (int j = 0; j < qualifiers.length; j++) {
								PropertyQualifier q = qualifiers[j];
								String name = q.getPropertyQualifierName();
								String value = q.getValue().getContent();

                                if (qualifier_name != null &&
									qualifier_name.compareTo(name) == 0 &&
									qualifier_value.compareTo(value) == 0) {
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

					// match representational_form
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
							PropertyQualifier[] qualifiers =
								p.getPropertyQualifier();

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



/*



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

        String return_str = ""; // RWW change from space to empty string
		boolean match = false;
		boolean has_qualval = false; //GF28940 RWW

		for (int i = 0; i < properties.length && !has_qualval; i++) {  //GF28940 RWW: there can only be one!
			//GF28940 RWW: commented
			//qualifier_value = null;
			org.LexGrid.commonTypes.Property p = properties[i];
			if (p.getPropertyName().compareTo(property_name) == 0) {
				match = true;

				// <P90><![CDATA[<term-name>Black</term-name><term-group>PT</term-group><term-source>CDC</term-source><source-code>2056-0</source-code>]]></P90>

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
						// qualifier, if needed
						{
							boolean match_found = false;
							PropertyQualifier[] qualifiers =
								p.getPropertyQualifier();

							if( qualifiers != null && qualifier_value != null ) {
								for (int j= 0; j < qualifiers.length; j++ ) {
									PropertyQualifier q = qualifiers[j];
									String name = q.getPropertyQualifierName();
									String value = q.getValue().getContent();
									//GF28940 RWW: there's no place in the model for another qualifier name
									//so I have to do this for "Property Qualifier" columns with a qualifier value
									//and luckily we don't have any using this!
									if( name.compareTo("subsource-name") == 0 && value.compareTo(qualifier_value) == 0 ) {
										has_qualval = true;
									}
								}
							}

							if (qualifiers != null && has_qualval ) {
								for (int j = 0; j < qualifiers.length; j++) {
									PropertyQualifier q = qualifiers[j];
									String name =
										q.getPropertyQualifierName();
									String value =
										q.getValue().getContent();
									if (qualifier_name.compareTo(name) == 0) {
										match_found = true;
										qualifier_value = value;
										break;
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
			}
			if (match && qualifier_value != null) {
				num_matches++;
				if (num_matches == 1) {
					return_str = qualifier_value;
				} else {
					return_str = return_str + delimiter + qualifier_value;
				}
			}
            return return_str;
        }
        return "";
	}
*/



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

/*
// Business rule::
// If both qualifier name and qualifier value are present, then the source-code value would be returned for
// the property that matched with the given pair of qualifier name and qualifier value.

scheme = "NCI Thesaurus";
version = "11.04d";
code = "C3671";
FULL_SYN: INJURY
        RepresentationalForm: PT
        Source: FDA
        Qualifier name: source-code
        Qualifier value: 2348
        Qualifier name: subsource-name
        Qualifier value: CDRH

//C54027
FULL_SYN: PATIENT PROBLEM/MEDICAL PROBLEM
	RepresentationalForm: PT
	Source: FDA
	Qualifier name: source-code
	Qualifier value: 2688
	Qualifier name: subsource-name
	Qualifier value: CDRH


FULL_SYN: DEVICE ISSUE
	RepresentationalForm: PT
	Source: FDA
	Qualifier name: source-code
	Qualifier value: 2379
	Qualifier name: subsource-name
	Qualifier value: CDRH

*/

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

												if( q_name.compareTo("source-code") == 0) {
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
			return getFocusConceptCode(concept);

		} else if (field_Id.endsWith("Property Qualifier")) {

            return getFocusConceptPropertyQualifierValue(
             concept,
             property_name,
             property_type,
	         qualifier_name,
	         source,
	         qualifier_value,
	         representational_form,
	         delimiter,
	         isPreferred);

		} else if (field_Id.endsWith("Property")) {

            return getFocusConceptPropertyValue(
             concept,
             property_name,
             property_type,
	         qualifier_name,
	         source,
	         qualifier_value,
	         representational_form,
	         delimiter,
	         isPreferred);

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
			Vector qualifier_value_vec = new Vector();
			qualifier_value_vec.add("ADaM-" + codeListPT);
			qualifier_value_vec.add("CDASH-"+ codeListPT);
			qualifier_value_vec.add("SDTM-" + codeListPT);
			qualifier_value_vec.add("SEND-" + codeListPT);
			qualifier_value_vec.add("CDISC-"+ codeListPT);

			//String source_code = "SDTM-" + codeListPT;
			qualifier_name = "source-code";
			String retval = getFocusConceptPropertyValue(
				node,
				property_name,
				property_type,
				qualifier_name,
				source,//"CDISC",
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

		//return DataUtils.getPTBySourceCode(scheme, version, node.getEntityCode(), source, source_code);
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
/*
		    if (i == 1) {
				col.setFieldId("Blank");
			}


		    if (field_id.compareTo("Associated Concept Code") == 0) {
				col.setFieldId("Code");
			}
*/
		    if (field_id.compareTo("Associated Concept Code") == 0) {
				col.setFieldId("Blank");
			}

			if (col.getPropertyName().compareTo("Extensible_List") == 0) {
				col.setFieldId("Property");
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

}

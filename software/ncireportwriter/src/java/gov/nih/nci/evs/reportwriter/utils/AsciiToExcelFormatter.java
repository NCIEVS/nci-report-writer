/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;

import gov.nih.nci.evs.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class AsciiToExcelFormatter extends BaseFileFormatter {
    private static Logger _logger = Logger
        .getLogger(AsciiToExcelFormatter.class);
    private static final int MAX_WIDTH = 30;
    private static final int MAX_CELL_WIDTH = 50;
    private static final int MAX_CODE_WIDTH = 10;
    private static boolean ADJUST_HEIGHT = false;

    public Boolean convert(String textfile, String delimiter) throws Exception {
        return convert2(textfile, "xls", delimiter);
    }

    public Boolean convert(String textfile, String delimiter, String outfile)
            throws Exception {

        Vector<String> headings = getColumnHeadings(textfile, delimiter);
        Vector<Integer> maxChars = getColumnMaxChars(textfile, delimiter);

        // Note: Special Case for CDISC STDM Terminology report.
        int extensible_col = findColumnIndicator(headings, "Extensible");

        int heading_height_multiplier = 1;
        for (int i = 0; i < maxChars.size(); i++) {
            String heading = (String) headings.elementAt(i);
            int maxCellLen = maxChars.elementAt(i);
            int maxTokenLen = getMaxTokenLength(heading);
            if (maxTokenLen > maxCellLen) {
                maxCellLen = maxTokenLen;
                maxChars.setElementAt(new Integer(maxCellLen), i);
            }
            if (maxCellLen < MAX_CODE_WIDTH) {
                Vector<String> tokens = parseData(heading, " ");
                if (tokens.size() > heading_height_multiplier)
                    heading_height_multiplier = tokens.size();
            }
        }

        Boolean[] a = findWrappedColumns(textfile, delimiter, MAX_WIDTH);
        // Note: The max column number allowed in an Excel spreadsheet is 256
        int[] b = new int[255];
        for (int i = 0; i < 255; i++) {
            b[i] = 0;
        }

        File file = new File(textfile);
        String absolutePath = file.getAbsolutePath();
        _logger.debug("Absolute Path: " + absolutePath);
        String filename = file.getName();
        _logger.debug("filename: " + filename);

        int m = filename.indexOf(".");
        String workSheetLabel = filename.substring(0, m);
        int n = workSheetLabel.indexOf("__");
        workSheetLabel = workSheetLabel.substring(0, n);
        _logger.debug("workSheetLabel: " + workSheetLabel);

        if (workSheetLabel.compareTo("") == 0)
            return Boolean.FALSE;

        String pathName = file.getPath();
        _logger.debug("Path: " + pathName);

        BufferedReader br = getBufferReader(textfile);
        FileOutputStream fout = new FileOutputStream(outfile);
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet ws = wb.createSheet(workSheetLabel);
        HSSFCellStyle toprow = wb.createCellStyle();
        HSSFCellStyle highlightedrow = wb.createCellStyle();

        HSSFCellStyle cs = wb.createCellStyle();

        // Note: GF20673 shade top row
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        toprow.setFont(font);
        toprow.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        toprow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        toprow.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        toprow.setWrapText(true);

        highlightedrow.setFont(font);
        highlightedrow.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        highlightedrow.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        highlightedrow.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // highlightedrow.setWrapText(true);

        cs.setWrapText(true);
        // cs.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
        cs.setAlignment(HSSFCellStyle.VERTICAL_TOP);

        HSSFRow wr = null;
        int rownum = 0;
        // int baseline_height = 15;
        int baseline_height = 12;
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            // line = line.trim(); Note: 090512 first value could be empty
            if (line.length() <= 0)
                continue;

            Vector<String> v = parseData(line, delimiter);
            wr = ws.createRow(rownum);
            // wr.setHeightInPoints(60);
            if (rownum == 0) {
                wr.setHeightInPoints(baseline_height
                    * heading_height_multiplier);
            } else {
                wr.setHeightInPoints(baseline_height);
                if (ADJUST_HEIGHT) {
                    int num_lines =
                        getHeightInPoints(v, ADJUST_HEIGHT, MAX_CELL_WIDTH);
                    wr.setHeightInPoints(baseline_height * num_lines);
                }
            }

            // Note: Special Case for CDISC STDM Terminology report.
            boolean highlight_row =
                extensible_col != -1
                    && v.elementAt(extensible_col).trim().length() > 0;

            for (int i = 0; i < v.size(); i++) {
                HSSFCell wc = wr.createCell(i);
                if (rownum == 0) {
                    wc.setCellStyle(toprow);
                } else if (a[i].equals(Boolean.TRUE)) {

                    wc.setCellStyle(cs);
                    wc.setCellType(HSSFCell.CELL_TYPE_STRING);

                    if (highlight_row)
                        wc.setCellStyle(highlightedrow);

                } else {
                    if (highlight_row)
                        wc.setCellStyle(highlightedrow);
                }

                String s = (String) v.elementAt(i);
                s = s.trim();

                if (s.length() > b[i]) {
                    b[i] = s.length();
                }
                if (s.equals("")) {
                    s = null;
                }

                wc.setCellValue(s);
                if (_ncitCodeColumns.contains(i) && rownum > 0 && s != null
                    && s.length() > 0) {
                    try {
                        wc.setCellFormula("HYPERLINK(\"" + getNCItCodeUrl(s)
                            + "\", \"" + s + "\")");
                    } catch (Exception e) {
                        ExceptionUtils.print(_logger, e,
                            "The following string is too large to be a "
                                + "valid NCIt code (" + filename + "): " + s);
                    }
                }
            }
            rownum++;
        }

        br.close();
        for (int i = 0; i < 255; i++) {
            if (b[i] != 0) {
                int multiplier = b[i];
                if (i < headings.size()) {
                    Integer int_obj = (Integer) maxChars.elementAt(i);
                    multiplier = int_obj.intValue();
                }

                // Note(GF20673): 315 is the magic number for this font and size
                int colWidth = multiplier * 315;

                // Fields like definition run long, some sanity required
                if (colWidth > 20000) {
                    colWidth = 20000;
                }
                // _logger.debug("Calculated column width " + i + ": " +
                // colWidth);
                ws.setColumnWidth(i, colWidth);
            }
        }

        // Note(GF20673): Freeze top row
        ws.createFreezePane(0, 1, 0, 1);
        wb.write(fout);
        fout.close();
        return Boolean.TRUE;
    }

    public static void test(String textfile, int[] ncitCodeColumns) {
        try {
            String delimiter = "\t";
            AsciiToExcelFormatter formatter = new AsciiToExcelFormatter();
            formatter.setNcitUrl("http://ncit-dev.nci.nih.gov/ncitbrowser/");
            formatter.setNcitCodeColumns(ncitCodeColumns);
            formatter.convert(textfile, delimiter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String dir = "C:/apps/evs/ncireportwriter-webapp/downloads/";
        test(dir + "CDISC_SDTM_Terminology__10.06e.txt", new int[] { 0, 1 });
        test(dir + "CDISC_Subset_REPORT__10.06e.txt", new int[] { 1, 3 });
        test(dir + "CDRH_Subset_REPORT__10.06e.txt", new int[] { 1, 3, 9 });
        test(dir + "FDA-SPL_Country_Code_REPORT__10.06e.txt", new int[] { 1 });
        test(dir + "FDA-UNII_Subset_REPORT__10.06e.txt", new int[] { 2 });
        test(dir + "Individual_Case_Safety_(ICS)_Subset_REPORT__10.06e.txt",
            new int[] { 1, 3 });
        test(dir + "Structured_Product_Labeling_(SPL)_REPORT__10.06e.txt",
            new int[] { 1, 3 });
        _logger.debug("Done");
    }
}

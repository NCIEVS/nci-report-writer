package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
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
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
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
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class AsciiToHtmlFormatter extends BaseFileFormatter {
    private static Logger _logger = Logger.getLogger(AsciiToHtmlFormatter.class);
    private static final int MAX_WIDTH = 30;
    private static final int MAX_CELL_WIDTH = 50;
    private static final int MAX_CODE_WIDTH = 10;
    private static boolean ADJUST_HEIGHT = false;

    public Boolean[] findWrappedColumns(String textfile,
        String delimiter, int maxLength) throws Exception {
        Boolean[] a = null;
        File file = new File(textfile);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(bis));

        int rownum = 0;
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            if (line.length() <= 0)
                continue;
            Vector<String> v = parseData(line, delimiter);
            if (rownum == 0) {
                a = new Boolean[v.size()];
                for (int i = 0; i < v.size(); i++) {
                    a[i] = Boolean.FALSE;
                }
            } else {
                for (int i = 0; i < v.size(); i++) {
                    String s = (String) v.elementAt(i);
                    if (s.length() > maxLength && a[i].equals(Boolean.FALSE)) {
                        a[i] = Boolean.TRUE;
                    }
                }
            }
            rownum++;
        }
        br.close();
        return a;
    }

    public int getHeightInPoints(Vector<String> v) {
        int num_lines = 1;
        if (!ADJUST_HEIGHT)
            return num_lines;

        for (int i = 0; i < v.size(); i++) {
            String s = (String) v.elementAt(i);
            int len = s.length();
            int lines = len / MAX_CELL_WIDTH;
            if (lines > num_lines)
                num_lines = lines;
        }
        return num_lines;
    }

    public Boolean convert(String textfile, String delimiter)
            throws Exception {
    	return convert2(textfile, "xls", delimiter);
    }

    public int findColumnIndicator(Vector<String> headings, String label) {
        for (int i = 0; i < headings.size(); i++) {
            String heading = headings.elementAt(i);
            if (heading.contains(label))
                return i;
        }
        return -1;
    }

    public Boolean convert(String textfile, String delimiter,
        String excelfile) throws Exception {

        Vector<String> headings = getColumnHeadings(textfile, delimiter);
        Vector<Integer> width_vec = getColumnWidths(textfile, delimiter);

        // Note: Special Case for CDISC STDM Terminology report.
        int extensible_col = findColumnIndicator(headings, "Extensible");

        int heading_height_multiplier = 1;
        for (int i = 0; i < width_vec.size(); i++) {
            String heading = (String) headings.elementAt(i);
            Integer int_obj = (Integer) width_vec.elementAt(i);
            int maxCellLen = int_obj.intValue();
            int maxTokenLen = getMaxTokenLength(heading);
            if (maxTokenLen > maxCellLen) {
                maxCellLen = maxTokenLen;
                width_vec.setElementAt(new Integer(maxCellLen), i);
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

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(bis));

        FileOutputStream fout = new FileOutputStream(excelfile);
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
                    int num_lines = getHeightInPoints(v);
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

            }
            rownum++;
        }

        br.close();
        for (int i = 0; i < 255; i++) {
            if (b[i] != 0) {
                int multiplier = b[i];
                if (i < headings.size()) {
                    Integer int_obj = (Integer) width_vec.elementAt(i);
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

    public int getMaxTokenLength(String heading) {
        if (heading == null || heading.length() == 0)
            return 0;
        int max = 0;
        String delimiter = " ";
        Vector<String> v = parseData(heading, delimiter);
        for (int k = 0; k < v.size(); k++) {
            String s = (String) v.elementAt(k);
            int len = s.length();
            if (len > max)
                max = len;
        }
        return max;
    }

    public static void main(String[] args) {
        try {
        	String dir = "C:/apps/evs/ncireportwriter-webapp/downloads";
        	String infile = dir + "/FDA-SPL_Country_Code_REPORT__10.06e.txt";
            String delimiter = "\t";
            
            new AsciiToHtmlFormatter().convert(infile, delimiter);
            _logger.debug("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package gov.nih.nci.evs.reportwriter.utils;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;

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
    private static Logger _logger = Logger
        .getLogger(AsciiToHtmlFormatter.class);

    public Boolean convert(String textfile, String delimiter) throws Exception {
        return convert2(textfile, "htm", delimiter);
    }

    public Boolean convert(String textfile, String delimiter, String outfile)
            throws Exception {
        BufferedReader br = getBufferReader(textfile);
        MyFileOutputStream out = new MyFileOutputStream(outfile);
        int numHeadings = -1;

        header(out, outfile);
        int row = 0;
        out.indent();
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;
            if (line.length() <= 0)
                continue;

            Vector<String> v = parseData(line, delimiter);
            if (numHeadings < 0)
                numHeadings = v.size();
            // Adds cells when current vector size is less than the header.
            int n = numHeadings - v.size();
            for (int i = 0; i < n; ++i)
                v.add(null);

            out.writeln_normal("<tr>");
            out.indent();
            for (int col = 0; col < v.size(); col++) {
                String value = v.get(col);
                if (value == null || value.trim().length() <= 0)
                    value = "&nbsp;";
                if (row <= 0)
                    out.writeln_normal("<th>" + value + "</th>");
                else if (_ncitCodeColumns.contains(col) && ! value.equals("&nbsp;"))
                    out.writeln_normal("<td>" + getNCItCodeUrl(value, false)
                        + "</td>");
                else
                    out.writeln_normal("<td>" + value + "</td>");
            }
            out.undent();
            out.writeln_normal("</tr>");
            row++;
        }
        br.close();
        footer(out);
        out.close();
        return Boolean.TRUE;
    }

    private void header(MyFileOutputStream out, String filename)
            throws Exception {
        out.writeln_normal("<html>");
        out.writeln_indent("<head>");
        out.writeln_indent("<title>" + out.getFilename() + "</title>");
        out.writeln_undent("</head>");
        out.writeln_normal("<body>");
        out.writeln_indent("<table border=\"1\" width=\"100%\">");
    }

    private void footer(MyFileOutputStream out) throws Exception {
        out.writeln_undent("</table>");
        out.writeln_undent("</body>");
        out.writeln_undent("</html>");
    }

    private String getNCItCodeUrl(String code, boolean separateWindow) {
        String ncitCodeUrl = super.getNCItCodeUrl(code);
        StringBuffer buffer = new StringBuffer();
        buffer.append("<a href=\"" + ncitCodeUrl + "\"");
        if (separateWindow)
            buffer.append(" target=\"_blank\"");
        buffer.append(">");
        buffer.append(code);
        buffer.append("</a>");
        return buffer.toString();
    }

    public static void main(String[] args) {
        try {
            String dir = "C:/apps/evs/ncireportwriter-webapp/downloads";
            AsciiToHtmlFormatter formatter = new AsciiToHtmlFormatter();
            formatter.setNcitUrl("http://ncit-dev.nci.nih.gov/ncitbrowser/");
            String delimiter = "\t";
            String infile;
            
            infile = dir + "/CDISC_SDTM_Terminology__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {0, 1});
            formatter.convert(infile, delimiter);
            
            infile = dir + "/CDISC_Subset_REPORT__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {1, 3});
            formatter.convert(infile, delimiter);
            
            infile = dir + "/CDRH_Subset_REPORT__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {1, 3, 9});
            formatter.convert(infile, delimiter);
            
            infile = dir + "/FDA-SPL_Country_Code_REPORT__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {1});
            formatter.convert(infile, delimiter);
            
            infile = dir + "/FDA-UNII_Subset_REPORT__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {2});
            formatter.convert(infile, delimiter);

            infile = dir + "/Individual_Case_Safety_(ICS)_Subset_REPORT__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {1, 3});
            formatter.convert(infile, delimiter);
            
            infile = dir + "/Structured_Product_Labeling_(SPL)_REPORT__10.06e.txt";
            formatter.setNcitCodeColumns(new int[] {1, 3});
            formatter.convert(infile, delimiter);

            _logger.debug("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

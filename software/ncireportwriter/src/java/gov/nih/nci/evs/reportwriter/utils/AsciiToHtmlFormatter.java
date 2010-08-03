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
    private static Logger _logger =
        Logger.getLogger(AsciiToHtmlFormatter.class);
    private String _ncitUrl = "http://ncit.nci.nih.gov/ncitbrowser/";
    private Vector<Integer> _codeColumns = new Vector<Integer>();

    public void addNcitCodeColumn(int column) {
        if (!_codeColumns.contains(column))
            _codeColumns.add(column);
    }

    public void setNcitUrl(String url) {
        if (url.charAt(url.length() - 1) != '/')
            url += "/";
        _ncitUrl = url;
    }

    public Boolean convert(String textfile, String delimiter) throws Exception {
        return convert2(textfile, "htm", delimiter);
    }

    public Boolean convert(String textfile, String delimiter, String outfile)
            throws Exception {
        BufferedReader br = getBufferReader(textfile);
        MyFileOutputStream out = new MyFileOutputStream(outfile);

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
            out.writeln_normal("<tr>");
            out.indent();
            for (int col = 0; col < v.size(); col++) {
                if (row <= 0)
                    out.writeln_normal("<th>" + v.get(col) + "</th>");
                else if (_codeColumns.contains(col))
                    out.writeln_normal("<td>"
                        + this.getNCItCodeUrl(v.get(col), false) + "</td>");
                else
                    out.writeln_normal("<td>" + v.get(col) + "</td>");
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
        String ncitCodeUrl =
            _ncitUrl + "ConceptReport.jsp?dictionary=NCI%20Thesaurus"
                + "&code=" + code;

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
            String infile = dir + "/FDA-SPL_Country_Code_REPORT__10.06e.txt";
            String delimiter = "\t";

            AsciiToHtmlFormatter formatter = new AsciiToHtmlFormatter();
            formatter.setNcitUrl("http://ncit-dev.nci.nih.gov/ncitbrowser/");
            formatter.addNcitCodeColumn(1);
            formatter.convert(infile, delimiter);
            _logger.debug("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

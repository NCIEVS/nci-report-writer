package gov.nih.nci.evs.reportwriter.formatter;

import java.io.*;
import java.net.*;

import org.apache.log4j.*;

public class UrlAsciiToTextFormatter extends AsciiToTextFormatter {
    private static Logger _logger = Logger
        .getLogger(UrlAsciiToTextFormatter.class);
    private static final String FTP_URL = "http://evs.nci.nih.gov/ftp1";
    private static final String CDISC_SDTM_REPORT_URL = FTP_URL
        + "/CDISC/SDTM/SDTM%20Terminology.txt";
    private static final String CDRH_REPORT_URL = FTP_URL
        + "/FDA/CDRH/FDA-CDRH_NCIt_Subsets.txt";
    private static final String FDA_SPL_REPORT_URL = FTP_URL
        + "/FDA/SPL/FDA-SPL_Country_Codes.txt";
    private static final String FDA_UNII_REPORT_URL = FTP_URL
        + "/FDA/UNII/FDA-UNII_NCIt_Subsets.txt";

    protected Boolean convert2(String textfile, String toExt, String delimiter)
            throws Exception {
        int i = textfile.lastIndexOf("/");
        String outfile = textfile.substring(i + 1);
        i = outfile.lastIndexOf(".");
        outfile = outfile.substring(0, i) + "." + toExt;
        outfile = outfile.replaceAll("%20", " ");
        return convert(textfile, delimiter, outfile);
    }

    protected BufferedReader getBufferReader(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        _logger.debug("fileUrl: " + fileUrl);
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        return br;
    }

    // -- Test Program- --------------------------------------------------------
    public static void test(String textfile, int[] ncitCodeColumns) {
        try {
            String delimiter = "\t";
            UrlAsciiToTextFormatter formatter = new UrlAsciiToTextFormatter();
            formatter.convert(textfile, delimiter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        test(CDISC_SDTM_REPORT_URL, new int[] { 0, 1 });
        // test(dir + "CDISC_Subset_REPORT__10.06e.txt", new int[] { 1, 3 });
        test(CDRH_REPORT_URL, new int[] { 1, 3, 9 });
        test(FDA_SPL_REPORT_URL, new int[] { 1 });
        test(FDA_UNII_REPORT_URL, new int[] { 2 });
        // test(dir + "Individual_Case_Safety_(ICS)_Subset_REPORT__10.06e.txt",
        // new int[] { 1, 3 });
        // test(dir + "Structured_Product_Labeling_(SPL)_REPORT__10.06e.txt",
        // new int[] { 1, 3 });
        _logger.debug("Done");
    }
}

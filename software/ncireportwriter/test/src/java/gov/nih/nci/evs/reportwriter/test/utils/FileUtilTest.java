package gov.nih.nci.evs.reportwriter.test.utils;

import gov.nih.nci.evs.reportwriter.utils.*;

public class FileUtilTest {
    public static void main(String[] args) {
        try {
            String dir = "C:/apps/evs/ncireportwriter-webapp/downloads";
            String txtFile = dir + "/CDRH_Subset_REPORT__09.12d.txt";
            String xlsFile = dir + "/CDRH_Subset_REPORT__09.12d_test.xls";
            String delimiter = "\t";
            FileUtil.convertToExcel(txtFile, delimiter,xlsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

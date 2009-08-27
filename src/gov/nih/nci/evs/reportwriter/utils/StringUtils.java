package gov.nih.nci.evs.reportwriter.utils;

public class StringUtils {
    public static String toHtml(String text) {
        text = text.replaceAll("\n", "<br/>");
        text = text.replaceAll("  ", "&nbsp;&nbsp;");
        return text;
    }
}

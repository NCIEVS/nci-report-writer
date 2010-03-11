package gov.nih.nci.evs.reportwriter.utils;

public class WordUtils {
    public static String plural(int count, String pluralText) {
        // Note: pluralText could be "s", "es", "ies".
        return count == 1 ? "" : pluralText;
    }

    public static String was_were(int count, String pluralText) {
        // Note: pluralText could be "s", "es", "ies".
        if (count == 1)
            return " was";
        return pluralText + " were";
    }

    public static String addEndingSpaceIfNeeded(String value) {
        if (value == null) 
            return "";
        value = value.trim();
        if (value.length() > 0)
            value += " ";
        return value;
    }
}

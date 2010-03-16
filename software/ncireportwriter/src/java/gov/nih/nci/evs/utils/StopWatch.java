package gov.nih.nci.evs.utils;

import java.text.*;
import java.util.*;

public class StopWatch {
    private static DecimalFormat _doubleFormatter = new DecimalFormat("0.00");
    private long _startMS = 0;
    private long _incrementMS = 0;

    public StopWatch() {
        start();
    }

    public static DecimalFormat getFormatter() {
        return _doubleFormatter;
    }

    public void reset() {
        _startMS = 0;
        _incrementMS = 0;
    }

    public void start() {
        _startMS = System.currentTimeMillis();
    }

    public void storeIncrement() {
        _incrementMS += getIncrement();
    }

    public long getIncrement() {
        return System.currentTimeMillis() - _startMS;
    }

    public long getDuration() {
        if (_incrementMS > 0)
            return _incrementMS;
        return getIncrement();
    }

    public String getResult(long timeMS) {
        return getTimingResults(timeMS);
    }

    public String getResult() {
        long timeMS = getDuration();
        return getResult(timeMS);
    }

    public String formatInSec(long timeMS) {
        return format(timeInSeconds(timeMS));
    }

    public String formatInSec() {
        long timeMS = getDuration();
        return formatInSec(timeMS);
    }

    public static String getTimingResults(long timeMS) {
        double timeSec = timeMS / 1000.0;
        double timeMin = timeSec / 60.0;

        return "" + timeMS + " ms, " + _doubleFormatter.format(timeSec)
            + " sec, " + _doubleFormatter.format(timeMin) + " min";
    }

    public static double timeInSeconds(long timeMS) {
        double value = timeMS / 1000.0;
        return value;
    }

    public static double timeInMinutes(long timeMS) {
        double value = timeMS / 60000.0; // / 1000.0 / 60.0;
        return value;
    }

    public static double timeInHours(long timeMS) {
        double value = timeMS / 3600000.0; // / 1000.0 / 60.0 / 60.0;
        return value;
    }
    
    public static String format(double value) {
        return _doubleFormatter.format(value);
    }

    public static String[] toStrings(String value, String delimiter,
        boolean includeDelimiter, boolean trim) {
        StringTokenizer tokenizer =
            new StringTokenizer(value, delimiter, includeDelimiter);
        ArrayList<String> list = new ArrayList<String>();
        while (tokenizer.hasMoreElements()) {
            String s = tokenizer.nextToken();
            if (trim)
                s = s.trim();
            if (s.length() > 0)
                list.add(s);
        }
        return list.toArray(new String[list.size()]);
    }
}

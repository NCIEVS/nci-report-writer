package gov.nih.nci.evs.reportwriter.test.utils;

import java.util.*;

public class TestEnv {
    public static final String RW_PROPERTY_FILE = 
        "gov.nih.nci.cacore.ncireportwriterProperties";
    public static String _propertyFile =
        "C:/apps/evs/ncireportwriter-webapp/conf/ncireportwriter.properties";
    
    public static void setup() {
        System.setProperty(RW_PROPERTY_FILE, _propertyFile);
    }
    
    public static void setup(String propertyFile) {
        System.setProperty(RW_PROPERTY_FILE, propertyFile);
    }

    public static String[] parse(String[] args) {
        String prevArg = "";
        ArrayList<String> newArgs = new ArrayList<String>();
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];
            if (arg.equals("-propertyFile")) {
                prevArg = arg;
            } else if (prevArg.equals("-propertyFile")) {
                setup(arg);
                prevArg = "";
            } else {
                newArgs.add(arg);
            }
        }
        return newArgs.toArray(new String[newArgs.size()]);
    }
}

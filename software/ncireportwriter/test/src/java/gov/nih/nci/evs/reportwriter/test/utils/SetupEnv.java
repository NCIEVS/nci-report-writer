package gov.nih.nci.evs.reportwriter.test.utils;

import java.util.*;

public class SetupEnv {
    private static final String RW_PROPERTY_FILE = 
        "gov.nih.nci.cacore.ncireportwriterProperties";
    private static String DEFAULT_PROPERTY_FILE =
        "C:/apps/evs/ncireportwriter-webapp/conf/ncireportwriter.properties";
    private static SetupEnv _instance = null;
    
    private SetupEnv() {
        System.setProperty(RW_PROPERTY_FILE, DEFAULT_PROPERTY_FILE);
    }
    
    public static SetupEnv getInstance() {
        if (_instance == null)
            _instance = new SetupEnv();
        return _instance;
    }
    
    public void setup(String propertyFile) {
        System.setProperty(RW_PROPERTY_FILE, propertyFile);
    }

    public String[] parse(String[] args) {
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

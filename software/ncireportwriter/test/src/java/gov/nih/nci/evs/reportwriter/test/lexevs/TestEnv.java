package gov.nih.nci.evs.reportwriter.test.lexevs;

public class TestEnv {
    public static final String RW_PROPERTY_FILE = 
        "gov.nih.nci.cacore.ncireportwriterProperties";
    public static String _propertyFile =
        "C:/apps/evs/ncireportwriter-webapp/conf/ncireportwriter.properties";
    
    public static void setup(String propertyFile) {
        System.setProperty(RW_PROPERTY_FILE, propertyFile);
    }

    public static void setup() {
        setup(_propertyFile);
    }
}

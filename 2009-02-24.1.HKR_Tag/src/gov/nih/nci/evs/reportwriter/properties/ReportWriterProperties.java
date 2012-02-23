package gov.nih.nci.evs.reportwriter.properties;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * Singleton for accessing Report Writer Properties.
 *
 * @author <a href="mailto:rajasimhah@mail.nih.gov">Harsha Rajasimha</a>
 *
 */
public class ReportWriterProperties {

       //KLO
		public static final String EVS_SERVICE_URL = "EVS_SERVICE_URL";
		public static final String REPORT_DOWNLOAD_DIRECTORY = "REPORT_DOWNLOAD_DIRECTORY";
		public static final String MAXIMUM_LEVEL = "MAXIMUM_LEVEL";
		public static final String MAXIMUM_RETURN = "MAXIMUM_RETURN";

	    private static Logger log = Logger.getLogger(ReportWriterProperties.class);

		private static ReportWriterProperties reportwriterProperties;

	    private static Properties properties = new Properties();


	    /**
	     * Private constructor for singleton pattern.
	     */
		private ReportWriterProperties() {}

		/**
		 * Gets the single instance of ReportWriterProperties.
		 *
		 * @return single instance of ReportWriterProperties
		 *
		 * @throws Exception the exception
		 */
		public static ReportWriterProperties getInstance() throws Exception{
			if(reportwriterProperties == null) {
				synchronized(ReportWriterProperties.class) {
					if(reportwriterProperties == null) {
						reportwriterProperties = new ReportWriterProperties();
						loadProperties();
					}
				}
			}
			return reportwriterProperties ;
		}


	    //public String getProperty(String key) throws Exception{
		public static String getProperty(String key) throws Exception{
	    	return properties.getProperty(key);
	    }


	    private static void loadProperties() throws Exception{
			String propertyFile = System.getProperty("gov.nih.nci.cacore.ncireportwriterProperties");

			log.info("reportwriterProperties FileLocation= "+ propertyFile);

			if(propertyFile != null && propertyFile.length() > 0){
				FileInputStream fis = new FileInputStream(new File(propertyFile));
				properties.load(fis);
			}
			else System.out.println("propertyFile is null");

			for(Iterator i = properties.keySet().iterator(); i.hasNext();){
				String key = (String)i.next();
				String value  = properties.getProperty(key);
	            log.debug("KEY: "+ key +"\t - "+value);
			}
		}
	}

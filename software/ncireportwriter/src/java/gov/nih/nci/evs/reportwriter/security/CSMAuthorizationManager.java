package gov.nih.nci.evs.reportwriter.security;

import java.util.*;
import java.net.*;
import org.apache.log4j.*;
import gov.nih.nci.security.*;
import gov.nih.nci.security.util.*;
import gov.nih.nci.security.authentication.*;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSConfigurationException;
import gov.nih.nci.security.provisioning.AuthorizationManagerImpl;
import gov.nih.nci.security.system.ApplicationSecurityConfigurationParser;
import gov.nih.nci.security.authorization.AuthorizationManagerFactory;


public class CSMAuthorizationManager {

	private static boolean THROW_ON_LOAD_FAILURE = true;

	private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;


    private static final String APP_NAME = "ncireportwriter";
	private static final Logger log = Logger.getLogger(CSMAuthorizationManager.class);

	public static AuthorizationManager getAuthorizationManager(String applicationContextName, URL url) throws CSException, CSConfigurationException{
		AuthorizationManager authorizationManager = null;
		String applicationManagerClassName = ApplicationSecurityConfigurationParser.getAuthorizationManagerClass(applicationContextName, url);
		if (null == applicationManagerClassName || applicationManagerClassName.equals(""))
		{
			if (log.isDebugEnabled())
				log.debug("Authorization|"+applicationContextName+"||getAuthorizationManager|Success|Initializing Common Authorization Manager|");

			authorizationManager = (AuthorizationManager) SecurityServiceProvider.getUserProvisioningManager(applicationContextName);
			authorizationManager.initialize(applicationContextName);
		}
		else
		{
			try
			{
				authorizationManager = (AuthorizationManager)(Class.forName(applicationManagerClassName)).newInstance();
				try
				{
					//FileLoader fileLoader = FileLoader.getInstance();
					System.out.println("(1) " + applicationContextName + gov.nih.nci.security.constants.Constants.FILE_NAME_SUFFIX);
					url = getFileAsURL(applicationContextName + gov.nih.nci.security.constants.Constants.FILE_NAME_SUFFIX);
				}
				catch (Exception e)
				{
					url = null;
				}

				if (url != null)
				{
					if (log.isDebugEnabled())
						log.debug("Authorization|"+applicationContextName+"||getAuthorizationManager|Success|Initializing Custom Authorization Manager "+applicationManagerClassName+"|" );
					authorizationManager.initialize(applicationContextName,url);
				}
				else
				{
					if (log.isDebugEnabled())
						log.debug("Authorization|"+applicationContextName+"||getAuthorizationManager|Success|Initializing Custom Authorization Manager with Hibernate File"+applicationManagerClassName+"|" );
					authorizationManager.initialize(applicationContextName);
				}
				return authorizationManager;
			}
			catch (Exception exception)
			{
				if (log.isDebugEnabled())
					log.debug("Authorization|"+applicationContextName+"||getAuthorizationManager|Failure| Error initializing Custom Authorization Manager "+applicationManagerClassName+"|" + exception.getMessage() );
				exception.printStackTrace();
				throw new CSConfigurationException("Error in loading the configured AuthorizationManager for the Application", exception);
			}
		}
		return authorizationManager;
	}

	private static UserProvisioningManager getUserProvisioningManagerDirectly(String applicationContextName) throws CSConfigurationException
	{
		//FileLoader fileLoader = FileLoader.getInstance();
		URL url = null;
		try
		{
			System.out.println("getUserProvisioningManagerDirectly: " + applicationContextName + gov.nih.nci.security.constants.Constants.FILE_NAME_SUFFIX);
			url = getFileAsURL(applicationContextName + gov.nih.nci.security.constants.Constants.FILE_NAME_SUFFIX);
		}
		catch (Exception e)
		{
			url = null;
			e.printStackTrace();
		}
		if (url != null)
			return new AuthorizationManagerImpl(applicationContextName, url);
		return null;
	}

	private static String getConfigFileSystemProperty() {
		String configFile = System.getProperty("gov.nih.nci.security.configFile");
        //configFile = "C:\\Tomcat 7.0.54/conf/ApplicationSecurityConfig.xml";
		System.out.println("(***) " + configFile);
		return configFile;
	}

/*
	public static AuthorizationManager getAuthorizationManagerDirectly(String applicationContextName) throws CSConfigurationException, CSException
	{
		String configFile = gov.nih.nci.security.constants.Constants.APPLICATION_SECURITY_CONFIG_FILE;//getConfigFileSystemProperty();
		return getAuthorizationManagerDirectly(configFile, applicationContextName);
	}
*/

	public static AuthorizationManager getAuthorizationManagerDirectly(String applicationContextName) throws CSConfigurationException, CSException
	{
		// BEGIN - Customization for caGrid Requirements
		//FileLoader fileLoader = FileLoader.getInstance();
		URL url = null;
		AuthorizationManager authorizationManager = null;
		try
		{
			//String logfile = System.getProperty("gov.nih.nci.cacore.ncireportwriterlog4jProperties");
			url = getFileAsURL(gov.nih.nci.security.constants.Constants.APPLICATION_SECURITY_CONFIG_FILE);
			//String configFile = System.getProperty("gov.nih.nci.security.configFile");
			//url = getFileAsURL(configFile);
		}
		catch (Exception e)
		{
			url = null;
		}
		if (url != null)
		{
			try {
				authorizationManager = AuthorizationManagerFactory.getAuthorizationManager(applicationContextName, url);
			} catch (Exception ex) {
				System.out.println("Exception: getAuthorizationManager");
			}
		}
		if (authorizationManager != null)
			return authorizationManager;
		else
		// END - Customization for caGrid Requirements
		return (AuthorizationManager) getUserProvisioningManagerDirectly(applicationContextName);
	}

	public static URL getFileAsURL(final String name)
	{
		return getFileAsURL(name, Thread.currentThread().getContextClassLoader());
	}

	public static URL getFileAsURL(String name, ClassLoader loader) throws IllegalArgumentException
	{
		if (name == null)
			throw new IllegalArgumentException("null input: name");

		if (name.startsWith("/"))
			name = name.substring(1);

		URL url = null;
		try
		{
			if (loader == null)
				loader = ClassLoader.getSystemClassLoader();

			if (LOAD_AS_RESOURCE_BUNDLE)
			{
				name = name.replace('/', '.');
				// throws MissingResourceException on lookup failures:
				final ResourceBundle rb = ResourceBundle.getBundle(name, Locale.getDefault(), loader);
			}
			else
			{
				//name = name.replace('.', '/');

				// returns null on lookup failures:
				url = loader.getResource(name);
				if (url != null)
				{
					// result = new Properties();
					// result.load(in); // can throw IOException
					THROW_ON_LOAD_FAILURE = false;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		}

		if (THROW_ON_LOAD_FAILURE)
		{
			throw new IllegalArgumentException("could not load [" + name + "]" + " as " + (LOAD_AS_RESOURCE_BUNDLE ? "a resource bundle" : "a classloader resource"));
		}

		return url;
	}

}



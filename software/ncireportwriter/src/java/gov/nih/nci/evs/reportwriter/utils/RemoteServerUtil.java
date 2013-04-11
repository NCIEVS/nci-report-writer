/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.evs.utils.*;
import gov.nih.nci.system.client.*;

import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.apache.log4j.*;

import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class RemoteServerUtil {
    private static Logger _logger = Logger
        .getLogger(RemoteServerUtil.class);
    private static boolean _firstTime = true;
    private static boolean _firstTimeSimpleTest = true;

    public static LexBIGService createLexBIGService() throws Exception {
        String serviceUrl = AppProperties.getInstance()
            .getProperty(AppProperties.EVS_SERVICE_URL);
        return createLexBIGService(serviceUrl);
    }

    public static LexBIGService createLexBIGService(String serviceUrl)
            throws Exception {
        if (_firstTime) {
            _logger.debug(StringUtils.SEPARATOR);
            _logger.debug("serviceUrl: " + serviceUrl);
            _firstTime = false;
        }
        if (serviceUrl == null || serviceUrl.compareTo("") == 0) {
            LexBIGService lbSvc = new LexBIGServiceImpl();
            return lbSvc;
        }
        LexEVSApplicationService lexevsService =
            (LexEVSApplicationService) ApplicationServiceProvider
                .getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
        return (LexBIGService) lexevsService;
    }


    public static LexEVSDistributed getLexEVSDistributedService() throws Exception {
        String serviceUrl = AppProperties.getInstance()
            .getProperty(AppProperties.EVS_SERVICE_URL);
        return getLexEVSDistributedService(serviceUrl);
    }

	public static LexEVSDistributed getLexEVSDistributedService(String serviceUrl) {
		LexEVSDistributed distributed = null;
		try {
			distributed =
				(LexEVSDistributed) ApplicationServiceProvider
					.getApplicationServiceFromUrl(serviceUrl, "EvsServiceInfo");
		} catch (Exception e) {
			System.out.println("Unable to instantiate LexEVSDistributedService " + serviceUrl);
			e.printStackTrace();
		}
		return distributed;
    }

    public static LexEVSValueSetDefinitionServices getValueSetDefinitionService()
            throws Exception {
        LexEVSDistributed distributed = getLexEVSDistributedService();
        LexEVSValueSetDefinitionServices service =
            distributed.getLexEVSValueSetDefinitionServices();
        return service;
    }


    public static boolean isRunning(StringBuffer warningMsg) {
        try {
            LexBIGService lbsvr = createLexBIGService();
            simpleTest(lbsvr);
        } catch (Exception e) {
            warningMsg.append("LexEVS is currently down." +
                "  Please report the following problem:");
            warningMsg.append("\n    * " + e.getMessage());
            return false;
        }
        return true;
    }

    private static void simpleTest(LexBIGService lbSvc) throws Exception {
        CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();

        if (! _firstTimeSimpleTest)
            return;
        _firstTimeSimpleTest = false;

        _logger.debug(StringUtils.SEPARATOR);
        CodingSchemeRendering[] csrs = csrl.getCodingSchemeRendering();
        _logger.debug("List of coding schemes:");
        for (int i = 0; i < csrs.length; i++) {
            CodingSchemeRendering csr = csrs[i];
            CodingSchemeSummary css = csr.getCodingSchemeSummary();
            String name = css.getFormalName();
            String version = css.getRepresentsVersion();
            _logger.debug("  " + i + ") " + name + "(version: " + version + ")");
        }
    }
}

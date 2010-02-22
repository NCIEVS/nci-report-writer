package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.reportwriter.properties.*;
import gov.nih.nci.system.client.*;

import org.LexGrid.LexBIG.caCore.interfaces.*;
import org.LexGrid.LexBIG.DataModel.Collections.*;
import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.*;
import org.LexGrid.LexBIG.Impl.*;
import org.LexGrid.LexBIG.LexBIGService.*;
import org.apache.log4j.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * Copyright 2008,2009 NGIT. This software was developed in conjunction
 * with the National Cancer Institute, and so to the extent government
 * employees are co-authors, any rights in such works shall be subject
 * to Title 17 of the United States Code, section 105.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the disclaimer of Article 3,
 *      below. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   2. The end-user documentation included with the redistribution,
 *      if any, must include the following acknowledgment:
 *      "This product includes software developed by NGIT and the National
 *      Cancer Institute."   If no such end-user documentation is to be
 *      included, this acknowledgment shall appear in the software itself,
 *      wherever such third-party acknowledgments normally appear.
 *   3. The names "The National Cancer Institute", "NCI" and "NGIT" must
 *      not be used to endorse or promote products derived from this software.
 *   4. This license does not authorize the incorporation of this software
 *      into any third party proprietary programs. This license does not
 *      authorize the recipient to use any trademarks owned by either NCI
 *      or NGIT
 *   5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED
 *      WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *      OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE
 *      DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
 *      NGIT, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT,
 *      INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *      BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *      LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *      CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *      LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *      ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *      POSSIBILITY OF SUCH DAMAGE.
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class RemoteServerUtil {
    private static Logger _logger = Logger
        .getLogger(RemoteServerUtil.class);
    private static boolean _firstTime = true;
    
    public static LexBIGService createLexBIGService() throws Exception {
        String serviceUrl = ReportWriterProperties.getInstance()
            .getProperty(ReportWriterProperties.EVS_SERVICE_URL);
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

        boolean debug = false;
        if (! debug)
            return;

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

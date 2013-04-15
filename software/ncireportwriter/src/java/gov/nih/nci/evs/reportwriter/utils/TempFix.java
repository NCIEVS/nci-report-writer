/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.utils.*;

import java.util.*;

import org.LexGrid.concepts.*;
import org.apache.log4j.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class TempFix {
    private static Logger _logger = Logger.getLogger(TempFix.class);

    public static String modifyCodingSchemeName(String codingSchemeName) {
        _logger.debug("Replaced codingSchemeName(orig): "
            + codingSchemeName);
        codingSchemeName = codingSchemeName.replaceAll(" ", "_");
        _logger.debug("Replaced codingSchemeName(curr): "
            + codingSchemeName);
        return codingSchemeName;
    }

    public static String modifyValueSetDefinitionURI(
        String valueSetDefinitionURI) {
        _logger.debug("Replaced valueSetDefinitionURI(orig): "
            + valueSetDefinitionURI);
        valueSetDefinitionURI = valueSetDefinitionURI.replaceAll("\\.", "_");
        valueSetDefinitionURI = valueSetDefinitionURI.replaceAll(" ", "_");
        _logger.debug("Replaced valueSetDefinitionURI(curr): "
            + valueSetDefinitionURI);
        return valueSetDefinitionURI;
    }
    
    public static void debugList(Vector<Entity> list) {
        _logger.debug(StringUtils.SEPARATOR);
        if (list == null) {
            _logger.debug("List: null");
            return;
        }

        Iterator<Entity> iterator = list.iterator();
        int i=0;
        _logger.debug("List: ");
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            String code = entity.getEntityCode();
            String content = entity.getEntityDescription().getContent();
            _logger.debug("  " + (i++) + ") " + code + ": " + content);
        }
    }
}

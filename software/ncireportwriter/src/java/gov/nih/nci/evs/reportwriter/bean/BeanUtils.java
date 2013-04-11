/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.bean;

import gov.nih.nci.evs.utils.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class BeanUtils {
    public static OntologyBean getOntologyBean() {
        return (OntologyBean) HTTPUtils.getBean("ontologyBean",
            "gov.nih.nci.evs.reportwriter.bean.OntologyBean");
    }

    public static UserSessionBean getUserSessionBean() {
        return (UserSessionBean) HTTPUtils.getBean("userSessionBean",
            "gov.nih.nci.evs.reportwriter.bean.UserSessionBean");
    }
}

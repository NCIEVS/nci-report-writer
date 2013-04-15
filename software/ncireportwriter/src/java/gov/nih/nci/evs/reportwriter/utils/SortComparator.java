/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import java.util.*;

import javax.faces.model.*;

import org.LexGrid.LexBIG.DataModel.Core.*;
import org.LexGrid.concepts.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class SortComparator implements Comparator<Object> {
    public enum SortBy { Name, Code };
    private SortBy _sortBy = SortBy.Name;

    public SortComparator(SortBy sortBy) {
        _sortBy = sortBy;
    }

    public int compare(Object object1, Object object2) {
        String key1 = getKey(object1, _sortBy);
        String key2 = getKey(object2, _sortBy);
        return key1.compareTo(key2);
    }

    private String getKey(Object object, SortBy sortBy) {
        if (object == null)
            return "NULL";
        if (object instanceof Entity)
            return getConceptValue(object, sortBy);
        if (object instanceof AssociatedConcept)
            return getAssociatedConceptValue(object, sortBy);
        if (object instanceof SelectItem)
            return getSelectItemValue(object, sortBy);
        if (object instanceof String)
            return getStringValue(object, sortBy);
        return object.toString();
    }

    private String getConceptValue(Object object, SortBy sortBy) {
        Entity value = (Entity) object;
        if (sortBy == SortBy.Code)
            return value.getEntityCode();
        return value.getEntityDescription().getContent();
    }

    private String getAssociatedConceptValue(Object object, SortBy sortBy) {
        AssociatedConcept value = (AssociatedConcept) object;
        if (sortBy == SortBy.Code)
            return value.getConceptCode();
        return value.getEntityDescription().getContent();
    }

    private String getSelectItemValue(Object object, SortBy sortBy) {
        SelectItem value = (SelectItem) object;
        return value.getValue().toString();
    }

    private String getStringValue(Object object, SortBy sortBy) {
        String value = (String) object;
        return value;
    }
}

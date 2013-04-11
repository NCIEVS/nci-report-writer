/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report--writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import gov.nih.nci.evs.utils.*;

import java.util.*;

/**
 * 
 */

/**
 * @author EVS Team (Kim Ong, David Yee)
 * @version 1.0
 */

public class SortUtils {
    public static List<?> quickSort(List<?> list) {
        return quickSort(list, SortComparator.SortBy.Name);
    }

    public static List<?> quickSort(List<?> list, 
        SortComparator.SortBy sortBy) {
        if (list == null || list.size() <= 1)
            return list;
        Collections.sort(list, new SortComparator(sortBy));
        return list;
    }

    public static Vector<?> quickSort(Vector<?> list,
        SortComparator.SortBy sortBy) {
        if (list == null || list.size() <= 1)
            return list;
        Collections.sort(list, new SortComparator(sortBy));
        return list;
    }

    public static Vector<?> quickSort(Vector<?> vector) {
        return quickSort(vector, SortComparator.SortBy.Name);
    }

    public static Enumeration<?> sort(Enumeration<?> enumeration) {
        return ListUtils.sort(enumeration);
    }
}

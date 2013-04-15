/*L
 * Copyright Northrop Grumman Information Technology.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/nci-report-writer/LICENSE.txt for details.
 */

package gov.nih.nci.evs.reportwriter.utils;

import java.util.*;
import gov.nih.nci.evs.reportwriter.bean.*;
import gov.nih.nci.evs.reportwriter.service.*;

/**
 * 
 */

/**
 * @author EVS Team (David Yee)
 * @version 1.0
 */

public class ListConverter {
    public static class StandardReportSortComparator implements
            Comparator<StandardReport> {
        public int compare(StandardReport obj1, StandardReport obj2) {
            String label1 = obj1.getTemplate().getLabel();
            String label2 = obj2.getTemplate().getLabel();
            if (! label1.equals(label2))
                return label1.compareTo(label2);
            
            String format1 = obj1.getFormat().getDescription();
            String format2 = obj2.getFormat().getDescription();
            ReportGenerationThread.ReportFormatType formatType1 =
                ReportGenerationThread.ReportFormatType.value_of(format1);
            ReportGenerationThread.ReportFormatType formatType2 =
                ReportGenerationThread.ReportFormatType.value_of(format2);
            return formatType1.compareTo(formatType2);
        }
    }

    public static Vector<StandardReport> toStandardReport(Object[] objs,
        boolean sort) {
        @SuppressWarnings("rawtypes")
        List list = Arrays.asList(objs);
        @SuppressWarnings("unchecked")
        Vector<StandardReport> vector = new Vector<StandardReport>(list);
        if (sort)
            Collections.sort(vector, new StandardReportSortComparator());
        return vector;
    }
}

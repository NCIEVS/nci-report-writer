package gov.nih.nci.evs.reportwriter.utils;

import javax.servlet.http.HttpServletRequest;

public class FormUtils {
    public static String getBasePath(HttpServletRequest request) {
        return request.getContextPath();
    }
    
    public static String getPagesPath(HttpServletRequest request) {
        String basePath = getBasePath(request);
        return basePath + "/pages";
    }

    public static String getImagesPath(HttpServletRequest request) {
        String basePath = getBasePath(request);
        return basePath + "/images";
    }

    public static String getJSPath(HttpServletRequest request) {
        String basePath = getBasePath(request);
        return basePath + "/js";
    }

    public static String getCSSPath(HttpServletRequest request) {
        String basePath = getBasePath(request);
        return basePath + "/css";
    }
}

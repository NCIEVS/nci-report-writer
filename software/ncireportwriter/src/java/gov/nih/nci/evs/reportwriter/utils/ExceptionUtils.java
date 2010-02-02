package gov.nih.nci.evs.reportwriter.utils;

import org.apache.log4j.*;

public class ExceptionUtils {
    private static String getMessage(Throwable exception) {
        return exception.getClass().getSimpleName() + ": "
            + exception.getMessage();
    }

    public static void print(Logger logger, Throwable exception) {
        logger.error(getMessage(exception));
    }

    public static void print(Logger logger, Throwable exception,
        String text) {
        logger.error(getMessage(exception));
        logger.error(text);
    }

    public static void print(Logger logger, Throwable exception,
        String[] texts) {
        logger.error(getMessage(exception));
        for (int i=0; i<texts.length; ++i)
            logger.error(texts[i]);
    }
}

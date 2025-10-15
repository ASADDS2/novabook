package com.codeup.novabook.infra;

import java.util.logging.Logger;

/**
 * Logs pseudo HTTP calls to console and app.log using java.util.logging.
 */
public final class HttpLogger {
    private static final Logger logger = Logger.getLogger(HttpLogger.class.getName());

    private HttpLogger() {}

    public static void log(String message) {
        logger.info(message);
    }
}

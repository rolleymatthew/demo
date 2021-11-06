package com.wy;

/**
 * @author yunwang
 * @Date 2021-11-06
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLF4JTest {

    private static final String TAG = "TAG";

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TAG);

        logger.trace("test_trace");
        logger.debug("test_debug");
        logger.info("test_info");
        logger.warn("test_warn");
        logger.error("test_error");
    }
}
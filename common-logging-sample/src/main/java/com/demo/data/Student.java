package com.demo.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Student {

    private static Log log = LogFactory.getLog(Student.class);

    public void log() {
        log.trace("Trace Log");
        log.debug("Debug Log");
        log.info("Info Log");
        log.warn("Warn Log");
        log.error("Error Log");
    }
}

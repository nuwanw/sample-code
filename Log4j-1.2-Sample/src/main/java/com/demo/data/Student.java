package com.demo.data;

import org.apache.log4j.Logger;

public class Student {

    private static Logger log = Logger.getLogger(Student.class);

    public void log() {
        log.trace("Trace Log");
        log.debug("Debug Log");
        log.info("Info Log");
        log.warn("Warn Log");
        log.error("Error Log");
    }
}

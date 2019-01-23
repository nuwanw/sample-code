package com.demo;

import com.demo.data.Student;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MainApp {
    private static Log log = LogFactory.getLog(MainApp.class);

    public static void main(String[] args) {
        Student student = new Student();
        log.info("Start App Logging");
        student.log();
        log.info("End App Logging");
    }
}

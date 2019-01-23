package com.demo;

import com.demo.data.Student;
import org.apache.log4j.Logger;

public class MainApp {
    private static Logger log = Logger.getLogger(MainApp.class);

    public static void main(String[] args) {
        Student student = new Student();
        log.info("Start App Logging");
        student.log();
        log.info("End App Logging");
    }
}

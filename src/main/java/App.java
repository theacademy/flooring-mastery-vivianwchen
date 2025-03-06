package com.m3.vwc;

import com.m3.vwc.controller.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.scan("com.m3.vwc");
        appContext.refresh();

        FlooringController controller = appContext.getBean("controller", FlooringController.class);
        controller.run();
    }
}

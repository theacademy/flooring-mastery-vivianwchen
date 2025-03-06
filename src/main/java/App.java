package com.m3.vwc;

import com.m3.vwc.controller.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
    public static void main(String[] args) {

        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringController controller =
                ctx.getBean("controller", FlooringController.class);
        controller.run();
    }
}

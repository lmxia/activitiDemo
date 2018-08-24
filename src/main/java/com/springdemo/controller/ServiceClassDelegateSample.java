package com.springdemo.controller;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.logging.Logger;
@Service
public class ServiceClassDelegateSample implements JavaDelegate,Serializable {

    private static AlaudaService alaudaService;

    private final Logger log = Logger.getLogger(ServiceClassDelegateSample.class.getName());

    public void execute(DelegateExecution execution) throws Exception {
        log.info(alaudaService.sayHello("xailingming"));
        Thread.sleep(10000);
        execution.setVariable("task1", "I am task 1");
        log.info("I am task 1.");

    }

    public void setAlaudaService(AlaudaServiceImpl alaudaService) {
        this.alaudaService = alaudaService;
    }
}

package com.springdemo.controller;

import com.springdemo.controller.AlaudaService;
import org.springframework.stereotype.Component;

/*
 * @author lmxia@alauda.io
 */
@Component
public class AlaudaServiceImpl implements AlaudaService {
    public String sayHello(String name) {
        return "welcome to alauda " + name;
    }
}

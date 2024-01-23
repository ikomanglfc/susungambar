package com.triyakom.susun.controller;

import com.triyakom.susun.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GeneralController {

    @Autowired
    PropertyService service;

    @RequestMapping("reload")
    public Object reload(){
        service.loadProps();
        return service.getProperty();
    }
}

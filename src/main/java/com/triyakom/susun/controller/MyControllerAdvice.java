package com.triyakom.susun.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String exception(Exception ex){
        ex.printStackTrace();
        return "redirect:/error?r=reg";
    }
}

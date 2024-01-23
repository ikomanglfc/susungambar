package com.triyakom.susun.controller;

import com.triyakom.susun.model.SusunGambarUser;
import com.triyakom.susun.repo.UserRepo;
import com.triyakom.susun.service.Hashids;
import com.triyakom.susun.service.UserService;
import com.triyakom.susun.shorter.ParseShortUrlData;
import com.triyakom.susun.shorter.PassData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ShortUrlController {
    public static final String WEB_PARAM = "wdpr";

    public static final String COOKIE_NAME = "ACC";

    private static int MAX_COOKIE_AGE = 365 * 24 * 60 * 60;

    @Autowired
    ParseShortUrlData parser;


    @Autowired
    Hashids hashids;

    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;


    /**
     * @param wdpr
     * @param r        redirect page. r=home -> home, r=snk -> snk
     * @param response
     * @return
     */
    @RequestMapping("go")
    public String go(@RequestParam(WEB_PARAM) String wdpr,
                     @RequestParam(required = false, defaultValue = "") String r,
                     HttpServletResponse response, ModelMap map) {

        try {
            PassData pass = parser.parsing(wdpr, false);
            SusunGambarUser user = userRepo.getOne(pass.getMsisdn());

            userService.saveCookie(user,response);

            switch (r) {
                case "snk":
                    return "redirect:/snk";
                default:
                    return "redirect:/home";
            }
        } catch (Exception ex2) {
            return "redirect:/error?r=reg";
        }
    }


    public static String ThrowableToString(Throwable e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String stackTrace = writer.toString();
        return stackTrace;
    }

    @RequestMapping("header")
    @ResponseBody
    public Object header(HttpServletRequest request) {
        Map map = new HashMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String s = headers.nextElement();
            map.put(s, request.getHeader(s));
        }

        return map;
    }


}

package com.triyakom.susun.controller;

import com.triyakom.susun.model.LuckySpin;
import com.triyakom.susun.model.SusunGambarUser;
import com.triyakom.susun.repo.QuestionRepo;
import com.triyakom.susun.repo.UserRepo;
import com.triyakom.susun.service.PropertyService;
import com.triyakom.susun.service.UserException;
import com.triyakom.susun.service.UserService;
import com.triyakom.susun.service.quest.QuestionService;
import com.triyakom.susun.shorter.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    QuestionService questionService;

    @Autowired
    PropertyService propertyService;

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    Encrypt encrypt;

    @RequestMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("home")
    public String home(@CookieValue(name = UserService.AUTH_COOKIE, required = false) String cookie,
                       ModelMap map) throws UserException {

        SusunGambarUser user = userService.getUser(cookie);
        if (StringUtils.isEmpty(user.getName())) {
            map.put("popName", true);
        }

        //block cheater
        boolean block = user.getFlagBlock() == 0 && user.getDateRelease() != null && user.getDateRelease().getTime() > System.currentTimeMillis();

        //eligible luckyspin
        LuckySpin luckySpin = questionRepo.checkEligible(user.getUserId());

        System.out.println("From Table = " + luckySpin);

        if(luckySpin == null) {

            boolean notEligible = luckySpin == null;

            map.put("tutor", user.getFlagTutor());
            map.put("block", block);
            map.put("notEligible", notEligible);
            map.put("hasPlay", questionService.hasPlay(user));
            map.put("homeBanner", propertyService.getHomeBanner());
            map.put("popupBanner", propertyService.getPopupBanner());
            return "index";

        } else {

            boolean eligible = luckySpin.getMsisdn() != null && luckySpin.getSessionId() != null;
            luckySpin.setDate(System.currentTimeMillis());
            luckySpin.setUid(String.valueOf(user.getUserId()));
            luckySpin.setStep("init");
            luckySpin.setProgramId(11);

            System.out.println("For Token = " + luckySpin);
            String token = encrypt.encrypt(luckySpin);
            System.out.println("Token print = " + token);

            map.put("tutor", user.getFlagTutor());
            map.put("block", block);
            map.put("eligible", eligible);
            map.put("hasPlay", questionService.hasPlay(user));
            map.put("homeBanner", propertyService.getHomeBanner());
            map.put("popupBanner", propertyService.getPopupBanner());
            map.put("urlLuckySpin", "https://3yk.co/spin/go?token=" + token);

            return "index";
        }
    }

    @RequestMapping("update_name")
    @ResponseBody
    public String updateName(@CookieValue(name = UserService.AUTH_COOKIE) String cookie,
                             @RequestParam String name) throws UserException {
        SusunGambarUser user = userService.getUser(cookie);
        userService.updateName(user.getUserId(), name);
        return "ok";
    }

    @RequestMapping("leaderboard")
    public String leaderboard(@RequestParam(name = "p", required = false) String pop, ModelMap map, @CookieValue(name = UserService.AUTH_COOKIE) String cookie) throws UserException {

        SusunGambarUser user = userService.getUser(cookie);

        int userId;

        userId = user.getUserId();

        map.put("leaderboard", userRepo.getLeaderboard(userId));

        map.put("user", user);

        map.put("pop", pop);

        return "leaderboard";
    }

    @RequestMapping("info")
    public String info(ModelMap map) {

        map.put("snkBanner", propertyService.getSnkBanner());

        return "snk";
    }

    @RequestMapping("error")
    public String error(@RequestParam(required = false) String r, ModelMap map) {
        switch (r) {
            case "block":
                map.put("title", "Mohon Maaf");
                map.put("text", "Aktivitas mencurigakan terdeteksi dari akun anda. Demi keamanan, akun anda akan kami batasi sampai penyelidikan selesai. Terima Kasih.");
                map.put("btn", "Home");
                map.put("url1", "https://three.susungambar.com/home");
                map.put("url2", "/");
                break;
            default:
                map.put("title", "Daftar dulu yuk!");
                map.put("text", "Cuma Rp. 1110/hari kamu dapat kesempatan main setiap hari.");
                map.put("btn", "Daftar");
                map.put("url1", "sms:99386?body=REG%2011SUSUN%20WEB");
                map.put("url2", "sms:99386?body=REG%2011SUSUN%20WEB");
        }
        return "register";
    }

    @RequestMapping("dummy")
    public String dummyLogin(HttpServletResponse response) throws UserException {
        SusunGambarUser user = userService.getUser(1);
        userService.saveCookie(user, response);
        return "redirect:/home";
    }

    @RequestMapping("dummy/{msisdn}")
    public String dummyLogin(HttpServletResponse response, @PathVariable String msisdn) throws UserException {
        SusunGambarUser user = userService.getUserByMsisdn(msisdn);
        userService.saveCookie(user, response);
        return "redirect:/home";
    }

}

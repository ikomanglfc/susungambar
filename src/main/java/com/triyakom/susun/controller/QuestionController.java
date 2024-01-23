package com.triyakom.susun.controller;

import com.triyakom.susun.model.SusunGambarUser;
import com.triyakom.susun.repo.UserRepo;
import com.triyakom.susun.service.KuotaService;
import com.triyakom.susun.service.UserException;
import com.triyakom.susun.service.UserService;
import com.triyakom.susun.service.quest.QuestSession;
import com.triyakom.susun.service.quest.Question;
import com.triyakom.susun.service.quest.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QuestionController {

    private static final int MAX_NUMBER = 7;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    KuotaService kuotaService;

    @Autowired
    UserRepo userRepo;

    @RequestMapping("ready")
    public String getReady() throws UserException {
        return "get_ready";
    }

    @RequestMapping("tutor")
    public String tutor(ModelMap map) throws UserException {
        Question quest = questionService.getTutor();
        map.put("quest", quest);
        return "tutorial";
    }

    @RequestMapping("tutordone")
    public String tutorDone(@CookieValue(name = UserService.AUTH_COOKIE) String cookie) throws UserException {
        SusunGambarUser user = userService.getUser(cookie);
        userService.updateFlagTutor(user.getUserId());
        return "redirect:/quest";
    }

    @RequestMapping("quest")
    public String quest(@CookieValue(name = UserService.AUTH_COOKIE) String cookie,
                        @SessionAttribute(name = "QS", required = false) QuestSession questSession,
                        ModelMap map, HttpSession session, HttpServletRequest request) throws UserException {

        SusunGambarUser user = userService.getUser(cookie);
        if(user.getFlagTutor() == 0){
            return tutor(map);
        }
        if(questSession == null) {
            questSession = questionService.start(user, session);
        }else{
            //check refresh
            if(questSession.getNumberReq() == questSession.getNumber()){
                questSession.setNumber(questSession.getNumber() - 1);
                questSession.setReason("refresh");
                questionService.sendKuota(questSession, request);
                return "redirect:/result";
            }
            //req == current number
            questSession.setNumberReq(questSession.getNumber());
        }

        if(questSession == null){
            return "redirect:/leaderboard?p=playturn";
        }

        Question question = questionService.getQuestion(user, questSession);
        map.put("quest", question);
        map.put("kuota", kuotaService.getKuotaString(questSession.getNumber()));

        return "quest";
    }

    @RequestMapping("answer")
    public String answer(@SessionAttribute(name = "QS", required = false) QuestSession questSession,
                         @RequestParam String[] data, @RequestParam int n,
                         ModelMap map, HttpSession session, HttpServletRequest request){
        boolean next = questionService.processAnswer(questSession, data, request);
        if(next){
           return "redirect:/quest";
        }else {
            return "redirect:/result";
        }
    }

    @RequestMapping("result")
    public String result(@SessionAttribute(name = "QS", required = false) QuestSession questSession,
                         HttpSession session, ModelMap map, HttpServletRequest request) throws UserException {
        if(questSession == null){
            return "redirect:/home";
        }

        if("block".equals(questSession.getReason())){
            return "redirect:/error?r=block";
        }

        String title;
        String desc;
        String titleClass = "";

        SusunGambarUser user = userService.getUser(questSession.getUserId());

        if(questSession.getNumber() == kuotaService.getMaxNumber()){
            title = "SELAMAT!";
            desc = "Game telah selesai!<br/> Kamu dapat kuota";
        }else if(questSession.getNumber() == 0){
            titleClass = "black";
            title = "Hai, " + (StringUtils.isEmpty(user.getName()) ? user.getMsisdn() : user.getName()) + "!";
            desc = "Maaf kamu belum berhasil<br/>mendapatkan kuota";
        }else{
            title = "GAME OVER!";
            desc = "Kamu berhasil<br/>mendapatkan kuota";
        }

        int kuota = kuotaService.getKuota(questSession.getNumber());
        String kuotaString = kuota >= 1000 ? (kuota / 1000) + "<sub>GB</sub>" : kuota + "<sub>MB</sub>";

        map.put("title", title);
        map.put("titleClass", titleClass);
        map.put("desc", desc);
        map.put("kuota", kuotaString);
        map.put("winners", userRepo.getWinner());

        session.invalidate();
        map.put("hasPlay", questionService.hasPlay(user));
        if(questSession.getNumber() == 0){
            map.put("audio", "game_over_lose.mp3");
        }else if(questSession.getNumber() >= kuotaService.getMaxNumber()){
            map.put("audio", "winner.mp3");
        }else{
            map.put("audio", "lose_get_kuota.mp3");
        }

        return "result";
    }

    @RequestMapping("hint")
    @ResponseBody
    public Object hint(@CookieValue(name = UserService.AUTH_COOKIE) String cookie, @SessionAttribute(name = "QS", required = false) QuestSession questSession) throws UserException {
        SusunGambarUser user = userService.getUser(cookie);
        Map map = new HashMap<>();
        if(user.getHint() <= 0){
            map.put("ok", false);
        }else {
            userService.updateHint(user.getUserId(), user.getHint() - 1);
            userService.logHint(user.getUserId(),questSession.getSessionId(), questSession.getNumber());
            map.put("ok", true);
        }
        return map;
    }
}

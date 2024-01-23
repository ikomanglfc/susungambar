package com.triyakom.susun.service.quest;

import com.triyakom.susun.controller.QuestionController;
import com.triyakom.susun.model.QuestDB;
import com.triyakom.susun.model.SusunGambarUser;
import com.triyakom.susun.repo.QuestionRepo;
import com.triyakom.susun.repo.UserRepo;
import com.triyakom.susun.service.KuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//direct to its implementation
@Service
public class QuestionService {
    public static final String SESSION_QS = "QS";
    private static final long DAY = 24 * 60 * 60 * 1000;
    private static final long TIMEOUT = 30_000;
    private static final Random random = new Random();

    //end
    public static final String RESULT_LOOSE = "loose";
    public static final String RESULT_TIMEOUT = "timeout";
    public static final String RESULT_PRIZE = "prize";

    //win
    public static final String RESULT_WIN = "win";

    @Autowired
    QuestionRepo questionRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    KuotaService kuotaService;

    public boolean hasPlay(SusunGambarUser user){
        return user.getPull() > 0 || (user.isHasPlay() && user.getDatePush() != null
                && user.getDatePush().getTime() < System.currentTimeMillis() + DAY);
    }

    public QuestSession start(SusunGambarUser user, HttpSession session){
        boolean allowed = false;
        String source = null;
        if(user.isHasPlay() && user.getDatePush() != null
                && user.getDatePush().getTime() < System.currentTimeMillis() + DAY){
            user.setHasPlay(false);
            allowed = true;
            source = "push";
            userRepo.updateHasPlay(user.getUserId());
        }else if(user.getPull() > 0){
            user.setPull(user.getPull() - 1);
            allowed = true;
            source = "pull";
            userRepo.updatePull(user.getUserId(), user.getPull());
        }
        if(allowed){
            QuestSession quest = new QuestSession();
            quest.setUserId(user.getUserId());
            quest.setNumber(1);
            quest.setSource(source);
            quest.setNumberReq(1);
            quest.setSessionId(questionRepo.initPlay(user.getUserId(), source));

            session.setAttribute(SESSION_QS, quest);

            return quest;
        }
        return null;
    }

    public Question getQuestion(SusunGambarUser user, QuestSession questSession) {
        if (questSession.getNumber() > kuotaService.getMaxNumber()) return null; //quiz dah abis

        QuestDB question = questionRepo.loadQuestion(questSession.getSessionId(), questSession.getNumber());

        questSession.setQId(question.getQuestionId());
        questSession.setTimeRequest(System.currentTimeMillis());
        questSession.setTimer(question.getTime() * 1000);

        Question q  = parse(question, questSession.getNumber());
        q.setKuota(kuotaService.getKuotaString(1));
        q.setHint(user.getHint());

        Collections.shuffle(q.getImages());

        int[] idx = new int[q.getImages().size()];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = q.getImages().get(i).getIndex();
        }
        questSession.setIdx(idx);

        return q;
    }

    // return: goto next quest ?
    public boolean processAnswer(QuestSession questSession, String[] datas, HttpServletRequest request){
        //prevent null exception
        if(datas == null) datas = new String[0];

        StringBuilder logPlay = new StringBuilder();
        int[] idx = questSession.getIdx();
        for (int i = 0; i < idx.length; i++) {
            logPlay.append(idx[i]);
            if(i < idx.length - 1) logPlay.append(",");
        }
        logPlay.append("=");
        for (int i = 0; i < datas.length; i++) {
            logPlay.append(datas[i]);
            if(i < datas.length - 1) logPlay.append(",");
        }

        long duration = System.currentTimeMillis() - questSession.getTimeRequest();
        questionRepo.logQuest(questSession.getSessionId(),
                questSession.getQId(), logPlay.toString(), duration, questSession.getNumber());

        //check duration
        if(duration > questSession.getTimer() + TIMEOUT){
            //timeout
            questSession.setReason(RESULT_TIMEOUT);
            sendKuota(questSession, request);
            return false;
        }

        //check answer
        for (String data : datas){
            int dash = data.indexOf("-");
            if(dash > 0){
                int i = Integer.parseInt(data.substring(0, dash));
                int j = Integer.parseInt(data.substring(dash + 1));
                int p = idx[i];
                idx[i] = idx[j];
                idx[j] = p;
            }
        }
        boolean right = true;
        //check klo berurutan indexnya berarti benar
        for (int i = 0; i < idx.length; i++) {
            if(i != idx[i] - 1){
                right = false;
                break;
            }
        }

        if(right){
            if(questSession.getNumber() == kuotaService.getMaxNumber()){
                //quiz berakhir dengan kemenangan
                questSession.setReason(RESULT_WIN);
                sendKuota(questSession, request);
                return false;
            }else{
                //next question
                questSession.setNumber(questSession.getNumber() + 1);
                return true;
            }
        }else{
            questSession.setNumber(questSession.getNumber() - 1);
            sendKuota(questSession, request);
            return false;
        }

    }

    public void sendKuota(QuestSession questSession, HttpServletRequest request) {

        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || "".equals(ip)) {
            ip = request.getRemoteAddr();
        }

        String ua = request.getHeader("User-Agent");
        if (ua != null && ua.length() > 200) ua = ua.substring(0, 190) + "...";

        int kuota = kuotaService.getKuota(questSession.getNumber());
        questionRepo.updateSession(questSession.getSessionId(), questSession.getReason(), kuota);

        int result = questionRepo.checkCheater(questSession.getSessionId(), kuota, ip, ua);

        if (result == 1) {
            questionRepo.sendKuota(questSession.getSessionId(), kuota);
        }

        else {
            questSession.setReason("block");
        }

    }

    private List<SliceImage> getSliceImage(int col, int row, List<String> images) {
        SliceImage sliceImage[] = new SliceImage[col * row];
        for (int i = 0; i < sliceImage.length; i++) {
            int n = i + 1;
            sliceImage[i] = new SliceImage();
            sliceImage[i].index = n;
            sliceImage[i].code = images.get(i);
        }
        List<SliceImage> l = Arrays.asList(sliceImage);
        return l;
    }


    public Question getTutor() {
        Question question = new Question();
        question.setTimer(10);
        question.setHint(1);
        question.setNo(0);
        question.setKuota("");
        question.setQuestion("Yuk cari tau dulu caranya sebelum mulai bermain");
        question.setCol(3);
        question.setRow(3);
        question.setWidth(480);
        question.setHeight(480);
        question.setOriginalImage("dummy/3x2/YeezwDPwJ_resized.jpg");
        String images = "tutor/1.jpeg,tutor/2.jpeg,tutor/3.jpeg,tutor/4.jpeg,tutor/5.jpeg,tutor/6.jpeg,tutor/7.jpeg,tutor/8.jpeg,tutor/9.jpeg";
        List<SliceImage> list = getSliceImage(3, 3, Arrays.asList(images.split(",")));

        //swap 0, 1
        SliceImage img = list.get(0);
        list.set(0, list.get(1));
        list.set(1, img);

        //swap 4, 5
        img = list.get(4);
        list.set(4, list.get(5));
        list.set(5, img);
        question.setImages(list);
        for (int i = 0; i < list.size(); i++) {
            if(i == 4 || i == 5) continue;
            list.get(i).setFixed(true);
        }

        return question;
    }

    private Question parse(QuestDB questDB, int number){
        Question question = new Question();
        question.setTimer(questDB.getTime());
        question.setNo(number);
        question.setQuestion(questDB.getDescription());
        question.setOriginalImage(questDB.getFullImage());
        question.setRow(questDB.getRow());
        question.setCol(questDB.getCol());
        question.setWidth(questDB.getWidth());
        question.setHeight(questDB.getHeight());
        question.setFlagPreview(questDB.getFlagPreview());
        question.setFlagGreyscale(questDB.getFlagGreyscale());

        List<String> images = Arrays.asList(questDB.getImage().split(","));
        question.setImages(getSliceImage(question.getCol(), question.getRow(), images));

        return question;
    }
}

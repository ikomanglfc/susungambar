package com.triyakom.susun.repo;

import com.triyakom.susun.model.QuestDB;
import com.triyakom.susun.service.quest.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;

//@Repository
public class DummyQuestionRepo /*implements QuestionRepo*/{

    private int[] timer = {10, 30, 25, 35, 55, 60, 70};

    @Autowired
    QuestionService service;

    //Dummy Data
    //@Override
    public QuestDB loadQuestion(long sessionId, int number) {
        QuestDB question = new QuestDB();
        question.setTime(timer[number - 1]);
        //question.setNo(number);
        switch (number){
            case 1:
                question.setImage("dummy/3x2/E88PApxvbiE.jpg,dummy/3x2/nnnvkVaQpsL.jpg,dummy/3x2/YeezwDP25t8.jpg,"
                                + "dummy/3x2/J99NzGgALhM.jpg,dummy/3x2/d11Py0X4biX.jpg,dummy/3x2/9bbQAp51YiM.jpg");
                question.setCol(3);
                question.setRow(2);
                question.setWidth(480);
                question.setHeight(320);
                question.setFullImage("dummy/3x2/YeezwDPwJ_resized.jpg");
                return question;
            case 2:
                question.setImage("dummy/3x3/R55agD7gwtQ.jpg,dummy/3x3/QkkDAx2AOfL.jpg,dummy/3x3/zKKeO8XO2ub.jpg," +
                        "dummy/3x3/rllGL8kLrtZ.jpg,dummy/3x3/ellkZPQZntn.jpg,dummy/3x3/0KKvRXqR1uE.jpg," +
                        "dummy/3x3/O55vaN1aetl.jpg,dummy/3x3/wZZQK8pKquJ.jpg,dummy/3x3/qXXaK8PKzuJ.jpg"
                );
                question.setCol(3);
                question.setRow(3);
                question.setWidth(480);
                question.setHeight(480);
                question.setFullImage("dummy/3x3/XeeQ3RN1z_resized.jpg");
                return question;
            case 3:
                question.setImage(
                        "dummy/3x3/yJJrEl2QDt7.jpg,dummy/3x3/4WWGv7kz1F1.jpg,dummy/3x3/N99LDqvEWSe.jpg," +
                        "dummy/3x3/pLLDO7zvPu0.jpg,dummy/3x3/VaaozyRDWTz.jpg,dummy/3x3/lKKlZqgz1SO.jpg," +
                        "dummy/3x3/L88VvOk1WFG.jpg,dummy/3x3/Z88rwVegWFq.jpg,dummy/3x3/XeeQ3Nz5Wfv.jpg"
                );
                question.setCol(3);
                question.setRow(3);
                question.setWidth(480);
                question.setHeight(480);
                question.setFullImage("dummy/3x3/nnnvk3RNe_resized.jpg");
                return question;
            case 4:
                question.setImage(
                        "dummy/4x3/O55van9ZyHp.jpg,dummy/4x3/wZZQK4Xa2hb.jpg,dummy/4x3/qXXaK29L0up.jpg,dummy/4x3/1ZZxQP5rwhZ.jpg," +
                        "dummy/4x3/3ZZarbOl1hN.jpg,dummy/4x3/2ZZDzk0rqhn.jpg,dummy/4x3/722oap3OPhM.jpg,dummy/4x3/PXXxGnVM4ur.jpg," +
                        "dummy/4x3/kLLVk09rou0.jpg,dummy/4x3/bqq2wG9d5hA.jpg,dummy/4x3/yJJrE5L0PUk.jpg,dummy/4x3/4WWGvywV5fe.jpg"
                );
                question.setCol(4);
                question.setRow(3);
                question.setWidth(480);
                question.setHeight(360);
                question.setFullImage("dummy/4x3/555Ax4K7v_resized.jpg");
                return question;
            case 5:
                question.setImage(
                        "dummy/4x4/K88prOnN1sM.jpg,dummy/4x4/WGGRrNn9lUL.jpg,dummy/4x4/555AxN4GJiE.jpg,dummy/4x4/vkkR1Yyl2HZ.jpg," +
                        "dummy/4x4/x99znYNZPfG.jpg,dummy/4x4/D887G5n2zs1.jpg,dummy/4x4/aOO3wYaR8ue.jpg,dummy/4x4/G88boznk5sN.jpg," +
                        "dummy/4x4/Aww15G8qoFN.jpg,dummy/4x4/gwwy48QOGFJ.jpg,dummy/4x4/oKKxNY1keHR.jpg,dummy/4x4/R55agPnEyib.jpg," +
                        "dummy/4x4/QkkDA7nXbHG.jpg,dummy/4x4/zKKeOYVEbHW.jpg,dummy/4x4/rllGLY74wcK.jpg,dummy/4x4/ellkZYoeDcz.jpg"
                );
                question.setCol(4);
                question.setRow(4);
                question.setWidth(480);
                question.setHeight(480);
                question.setFullImage("dummy/4x4/aOO3wYavD_resized.jpg");
                return question;
            case 6:
                question.setImage(
                        "dummy/4x4/J99NzoDqECg.jpg,dummy/4x4/d11PyEoxWuV.jpg,dummy/4x4/9bbQAN9MouK.jpg,dummy/4x4/8WWRG4987c5.jpg," +
                        "dummy/4x4/M99lxzA2ZCd.jpg,dummy/4x4/K88prRa1LuJ.jpg,dummy/4x4/WGGRr7wlPiD.jpg,dummy/4x4/555Axk9J8FA.jpg," +
                        "dummy/4x4/vkkR1wG2zH3.jpg,dummy/4x4/x99zn1gPGCY.jpg,dummy/4x4/D887Gx9z3uO.jpg,dummy/4x4/aOO3wLG8xIg.jpg," +
                        "dummy/4x4/G88boG95ZuK.jpg,dummy/4x4/Aww15n9obTe.jpg,dummy/4x4/gwwy4vzGdTV.jpg,dummy/4x4/oKKxNR3elF4.jpg"
                );
                question.setCol(4);
                question.setRow(4);
                question.setWidth(480);
                question.setHeight(480);
                question.setFullImage("dummy/4x4/gwwy4vzWp_resized.jpg");
                return question;
            case 7:
                question.setImage(
                        "dummy/5x5/QkkDAPnWyFO.jpg,dummy/5x5/zKKeO4VaofY.jpg,dummy/5x5/rllGL17bVHD.jpg,dummy/5x5/ellkZGozwHl.jpg,dummy/5x5/0KKvR470Ef0.jpg," +
                        "dummy/5x5/O55va2nWksX.jpg,dummy/5x5/wZZQKk4vGsX.jpg,dummy/5x5/qXXaKq2kpT9.jpg,dummy/5x5/1ZZxQNPOEsD.jpg,dummy/5x5/3ZZarVb5Esz.jpg," +
                        "dummy/5x5/2ZZDzpkwKsb.jpg,dummy/5x5/722oaXp7Eck.jpg,dummy/5x5/PXXxG7nWbTE.jpg,dummy/5x5/kLLVkz0qPsL.jpg,dummy/5x5/bqq2wxGoeID.jpg," +
                        "dummy/5x5/yJJrEy5Dgiz.jpg,dummy/5x5/4WWGvly1Eta.jpg,dummy/5x5/N99LD3nWPiN.jpg,dummy/5x5/pLLDOw3PNsw.jpg,dummy/5x5/VaaozNnW5F5.jpg," +
                        "dummy/5x5/lKKlZrO1kfJ.jpg,dummy/5x5/L88VvqnW2fR.jpg,dummy/5x5/Z88rwLnWGfA.jpg,dummy/5x5/XeeQ3VnWDHE.jpg,dummy/5x5/E88PA5nWef8.jpg"
                );
                question.setCol(5);
                question.setRow(5);
                question.setWidth(480);
                question.setHeight(480);
                question.setFullImage("dummy/5x5/zKKeO4VqP_resized.jpg");
                return question;
        }
        return null;
    }
}

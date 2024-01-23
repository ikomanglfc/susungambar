package com.triyakom.susun.service.quest;

import lombok.Data;

@Data
//digenerate setiap 1x playturn
public class QuestSession {
    long sessionId;
    int userId;
    String source; // push, pull
    String reason; //end reason

    //berubah setiap pertanyaan
    int number;
    int numberReq;
    long timeRequest;
    int qId;
    long timer;
    long randomCode; // for encrypt purpose
    int[] idx;

}

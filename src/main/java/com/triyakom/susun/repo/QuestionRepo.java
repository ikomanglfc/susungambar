package com.triyakom.susun.repo;

import com.triyakom.susun.model.LuckySpin;
import com.triyakom.susun.model.QuestDB;

public interface QuestionRepo {
    QuestDB loadQuestion(long sessionId, int number);
    long initPlay(int userId, String source);
    void updateSession(long sessionId, String reason, int kuota);
    void logQuest(long sessionId, int questionId, String answer, long duration, int number);
    void sendKuota(long sessionId, int kuota);
    int checkCheater(long sessionId, int kuota, String ipAddress, String userAgent);
    LuckySpin checkEligible (int userId);
}

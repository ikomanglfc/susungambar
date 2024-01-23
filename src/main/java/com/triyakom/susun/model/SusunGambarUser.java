package com.triyakom.susun.model;

import lombok.Data;

import java.util.Date;

@Data
public class SusunGambarUser {

    int userId;

    String msisdn;

    String name;

    Date dateInsert = new Date();

    String operator;

    int pull = 0;

    boolean hasPlay = false;

    Date datePush;

    Date dateAccess;

    Date dateRegister;

    int flagDummy = 0;

    int flagTutor;

    int hint;

    int flagBlock;

    Date dateRelease;

}

package com.triyakom.susun;

import java.io.IOException;

public class InsertDBQuestion {
    public static void main(String[] args) throws Exception {
        InsertToDB insert = new InsertToDB(
                "2022-01-02", //yyyy-MM-dd
                "D:\\trash\\susun\\batch 1\\Database_soal_susun_gambar_Batch_1_200122.xlsx",
                "D:\\trash\\susun\\batch 1",
                "D:\\trash\\susun\\batch 1\\result");

        insert.go();

    }
}

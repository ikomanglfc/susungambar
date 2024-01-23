package com.triyakom.susun;

public class InsertQuestToDatabase {

    public static void main(String[] args) throws Exception {

        InsertToDB insert = new InsertToDB(
                "2022-06-09", //yyyy-MM-dd
                "D:\\Trash\\Susun Gambar\\batch 4\\Database soal susun gambar Batch 4 080622.xlsx",
                "D:\\Trash\\Susun Gambar\\batch 4\\",
                "D:\\Trash\\Susun Gambar\\batch 4\\result");
        insert.go();
    }

}

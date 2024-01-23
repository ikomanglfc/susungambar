package com.triyakom.susun;

import com.triyakom.susun.service.Hashids;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InsertToDB {


    /*flow upload quiz:
    1. data sesuai batch
    2. exec script (inget hapus .lock dan result jika failed)
    3. upload image result ke ftp 5.10 (/media/hdd/nginx_data/static/img/susun/slice)
    4. update flag_active = 1 where flag_active = 2
     */
    public static void main(String[] args) throws Exception {

//        InsertToDB insert = new InsertToDB(
//                "2022-01-02", //yyyy-MM-dd
//                "D:\\susun\\batch_1\\file_batch_1.xlsx",
//                "D:\\susun\\batch_1\\image",
//                "D:\\susun\\batch_1\\result");
//
//        insert.go();
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection("jdbc:sqlserver://192.168.5.9;database=DB_APPS;applicationName=Testing Aja", "sa", "pisaubermata2");
        return con;
    }

    Hashids hashids = new Hashids("saltsusungambarnih", 6);

    String query = "insert into trivia_susun_question(date_insert, image," +
            " flag_active, type_id, description, full_image) values(?, ?, 2, ?, ?, ?)";

    String pathServer = "slice/";
    File source;
    File imgFolder;

    File dest;
    Workbook xlsx;
    String date;

    boolean db = true;

    List<QuestData> questDataList = new ArrayList<>();

    InsertToDB(String date, String xlsx, String source, String dest) throws IOException {
        this.date = date;
        this.xlsx = new XSSFWorkbook(new FileInputStream(xlsx));
        this.source = new File(source);
        this.imgFolder = new File(source, "image");

        this.dest = new File(dest);
    }

    void go() throws Exception {
        if(!dest.exists()) dest.mkdirs();

        //prevent 2x insert untuk batch yg sama
        File lock = new File(source, ".lock");
        if(lock.exists()) throw new IllegalArgumentException("Folder: " + source.getName() + " sudah diproses. Untuk prosess ulang hapus file .lock di folder tsb.");

        lock.createNewFile();

        readXls(false);

        if(db){

            readXls(true);
            System.out.println("\n=======DATABASE========\n");
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

            for(QuestData quest : questDataList){
                ps.setString(1, date);
                ps.setString(2, quest.slices);
                ps.setInt(3, quest.questType);
                ps.setString(4, quest.question);
                                ps.setString(5, quest.originalImage);

                ps.executeUpdate();
                System.out.printf("%s -%s - %s - %s - %s\n", date, quest.slices, quest.questType, quest.question, quest.originalImage);
            }
        }

    }

    void readXls(boolean isSlice) throws Exception {
        Sheet questSheet = xlsx.getSheetAt(0); //read sheet xlsx
        System.out.println("Read Sheet: " + questSheet.getSheetName());

        Iterator<Row> iterator = questSheet.rowIterator();
        iterator.next(); //ignore first row

        while(iterator.hasNext()){
            Row row = iterator.next();

            //check first row klo kosong berarti udah berakhir
            if(checkEmptyCell(row.getCell(0))) break;

            String kotak = row.getCell(2).getStringCellValue();
            String question = row.getCell(3).getStringCellValue();
            String image = row.getCell(4).getStringCellValue();
            String difficulty = row.getCell(6).getStringCellValue();

            System.out.printf("%s[%s] - %s : %s\n", kotak, difficulty, question, image);

            QuestData slice = parseSlice(kotak);
            slice.question = question;

            parseQuestType(slice, difficulty);

            //check image
            File imgFile = new File(imgFolder, image);
            System.out.println(imgFile.getAbsolutePath());
            if(!imgFile.exists()) throw new IllegalArgumentException("image not exist: " + image);

            //sliceImage
            if(isSlice) {
                slicingImage(slice, imgFile);
                questDataList.add(slice);
            }


        }
    }

    boolean checkEmptyCell(Cell cell){

        try {
            CellType type = cell.getCellTypeEnum();
            if(type == CellType.NUMERIC){
                double p = cell.getNumericCellValue();
                return false;
            }
            String s = cell.getStringCellValue();
            if (s.trim().length() == 0) return true;

            return false;
        }catch (Exception ex){
            return true;
        }
    }

    void slicingImage(QuestData slice, File imgFile) throws IOException {
        SliceImageFile sliceImageFile = new SliceImageFile(hashids, pathServer, imgFile, dest,
                slice.w, slice.h, slice.col, slice.row);
        sliceImageFile.go();

        slice.originalImage = pathServer + sliceImageFile.getOrigImage();
        slice.slices = sliceImageFile.getSlices();
    }

    QuestData parseSlice(String kotak){
        int i = kotak.indexOf("x");
        if(i == -1) throw new IllegalArgumentException("Cell kotak tidak sesuai format: x");
        try {
            QuestData slice = new QuestData();
            slice.w = 480;
            slice.col = Integer.parseInt(kotak.substring(0, i).trim());
            slice.row = Integer.parseInt(kotak.substring(i + 1).trim());
            slice.h = slice.w * slice.row / slice.col;

            return slice;

        }catch (Exception ex) {
            throw new IllegalArgumentException("Cell kotak tidak sesuai format: col and row");
        }
    }

    //mapping ke table trivia_susun_quest_type
    void parseQuestType(QuestData questData, String difficulty){
        difficulty = difficulty.trim();
        if(questData.col == 3 && questData.row == 2 && "easy".equals(difficulty)) questData.questType = 1;
        else if(questData.col == 3 && questData.row == 3 && "easy".equals(difficulty)) questData.questType = 2;
        else if(questData.col == 3 && questData.row == 3 && "hard".equals(difficulty)) questData.questType = 3;
        else if(questData.col == 4 && questData.row == 3 && "easy".equals(difficulty)) questData.questType = 4;
        else if(questData.col == 4 && questData.row == 4 && "easy".equals(difficulty)) questData.questType = 5;
        else if(questData.col == 4 && questData.row == 4 && "hard".equals(difficulty)) questData.questType = 6;
        else if(questData.col == 5 && questData.row == 5 && "easy".equals(difficulty)) questData.questType = 7;

        else throw new IllegalArgumentException("Quest Type tidak dikenali");
    }


    class QuestData {
        int col;
        int row;
        int w;
        int h;

        String question;
        int questType;
        String originalImage;
        String slices;

    }

}

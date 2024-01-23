package com.triyakom.susun;

import com.triyakom.susun.repo.DummyQuestionRepo;
import com.triyakom.susun.service.quest.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DummyInsertToDB {
    public static void main(String[] args) {
        //insert();
    }

    static void insert(){
        String query = "insert into trivia_susun_question(date_insert, image," +
                " flag_active, type_id, description) values(getdate(), ?, 0, 1, ?)";

        try(Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            DummyQuestionRepo repo = new DummyQuestionRepo();

            /*
            insertToDB(ps, repo.loadQuestion(1, 1));
            insertToDB(ps, repo.loadQuestion(1, 2));
            insertToDB(ps, repo.loadQuestion(1, 3));
            insertToDB(ps, repo.loadQuestion(1, 4));
            insertToDB(ps, repo.loadQuestion(1, 5));
            insertToDB(ps, repo.loadQuestion(1, 6));
            insertToDB(ps, repo.loadQuestion(1, 7));
            */

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection con = DriverManager.getConnection("jdbc:sqlserver://192.168.5.9;database=DB_APPS;applicationName=Testing Aja", "sa", "pisaubermata2");
        return con;
    }

    /*static void insertToDB(PreparedStatement ps, Question question) throws SQLException {
        String image = "";
        for(String img : question.getImages()){
            image += img + ",";
        }
        image = image.substring(0, image.length() - 1);
        String desc = "Dummy " + question.getNo();
        ps.setString(1, image);
        ps.setString(2, desc);

        ps.executeUpdate();
    }*/
}

package controllers;

import com.google.gson.Gson;
import database.DBConnector;
import model.Book;
import model.Curriculum;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by mortenlaursen on 17/10/2016.
 */
public class CurriculumController {


    public ArrayList<Curriculum> getCurriculums() throws IllegalAccessException, SQLException {
        DBConnector db = new DBConnector();
        ArrayList<Curriculum> curriculums = db.getCurriculums();
        db.close();
        return curriculums;
    }

    public Curriculum getCurriculum(int id) throws SQLException {
        DBConnector db = new DBConnector();
        Curriculum curriculum = db.getCurriculum(id);
        db.close();
        return curriculum;
    }

    public boolean editCurriculum(int id, String data) throws SQLException {
        DBConnector db = new DBConnector();
        boolean b = db.editCurriculum(id, data);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteCurriculum(int id) throws SQLException {
        DBConnector db = new DBConnector();
        boolean b = db.deleteCurriculum(id);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addCurriculum(String data) throws SQLException {
        DBConnector db = new DBConnector();
        Curriculum c = new Gson().fromJson(data, Curriculum.class);
        boolean b = db.addCurriculum(c);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Book> getCurriculumBooks(int curriculumID) throws SQLException {
        DBConnector db = new DBConnector();
        ArrayList<Book> curriculumBooks = db.getCurriculumBooks(curriculumID);
        db.close();
        return curriculumBooks;

    }

    public boolean addCurriculumBook(String data) throws SQLException {
        DBConnector db = new DBConnector();
        //  boolean addCurriculumBook = db.addCurriculumBook(data);
        db.close();
        return true;
    }
}

package controllers;

import com.google.gson.Gson;
import database.DBConnector;
import model.Book;

import java.sql.SQLException;
import java.util.ArrayList;

public class BookController {
    DBConnector db = new DBConnector();

    public ArrayList<Book> getBooks() throws Exception {
        ArrayList<Book> books = db.getBooks();
       // db.close();
        return books;
    }

    public Book getBook(int id) throws Exception {
        DBConnector db = new DBConnector();
        Book book = db.getBook(id);
        db.close();
        return book;
    }

    public boolean editBook(int id, String data) throws Exception {
        DBConnector db = new DBConnector();
        boolean b = db.editBook(id, data);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteBook(int id) throws Exception {
        DBConnector db = new DBConnector();
        boolean b = db.deleteBook(id);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addBook(String data) throws Exception {
        DBConnector db = new DBConnector();
        Book book = new Gson().fromJson(data, Book.class);
        boolean b = db.addCurriculumBook(book);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addBook(Book book) throws SQLException {
        DBConnector db = new DBConnector();
        boolean b = db.addCurriculumBook(book);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

}

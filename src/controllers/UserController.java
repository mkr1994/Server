package controllers;

import Encrypters.*;
import com.google.gson.Gson;
import database.DBConnector;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Usercontroller used to control all userfunctions.
 * Created by mortenlaursen on 17/10/2016.
 */
public class UserController {
    Gson gson;
    DBConnector db;


    public UserController() {
        this.gson = new Gson();
        this.db = new DBConnector();
    }


    public ArrayList<User> getUsers() throws SQLException {
        DBConnector db = new DBConnector();
        ArrayList<User> users = db.getUsers();
        db.close();
        return users;

    }

    public User getUser(int id) throws SQLException {
        DBConnector db = new DBConnector();
        User user = db.getUser(id);
        db.close();
        return user;
    }

    public boolean editUser(User user) throws SQLException {
        DBConnector db = new DBConnector();
        boolean b = db.editUser(user);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteUser(int id) throws SQLException {
        DBConnector db = new DBConnector();
        boolean b = db.deleteUser(id);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addUser(String data) throws Exception {
        User u = gson.fromJson(data, User.class);
        String hashedPassword = Digester.hashWithSalt(u.getPassword());
        u.setPassword(hashedPassword);
        boolean b = db.addUser(u);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addUser(User user) throws Exception {
        String hashedPassword = Digester.hashWithSalt(user.getPassword());
        user.setPassword(hashedPassword);
        boolean b = db.addUser(user);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }


    }

}

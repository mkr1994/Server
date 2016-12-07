package controllers;

import Encrypters.*;
import database.DBConnector;
import model.User;

import java.sql.SQLException;

/**
 * Token controller, used for token related processing
 * Created by Tastum on 19/10/2016.
 */
public class TokenController {

    DBConnector db = new DBConnector();

    public String authenticate(String username, String password) throws SQLException {
        // Authenticate the user using the credentials provided

        String token;

        User foundUser = db.authenticate(username, password);
        if (foundUser != null) {

            token = Crypter.buildToken("abcdefghijklmnopqrstuvxyz1234567890@&%!?", 25);

            db.addToken(token, foundUser.getUserID());

        } else {
            token = null;
        }
        //Retunerer en access token til klienten.
        return token;


    }

    public User getUserFromTokens(String token) throws SQLException {
        DBConnector db = new DBConnector();
        User user = db.getUserFromToken(token);
        db.close();
        return user;

    }

    public boolean deleteToken(String token) throws SQLException{
        DBConnector db = new DBConnector();
        boolean b = db.deleteToken(token);
        db.close();
        if (b) {
            return true;
        } else {
            return false;
        }

    }
}

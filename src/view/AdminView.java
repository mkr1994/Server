package view;

import Encrypters.Digester;
import database.DBConnector;
import model.Book;
import model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by magnusrasmussen on 24/11/2016.
 */
public class AdminView {

    DBConnector dbConnector = new DBConnector();
    Scanner input = new Scanner(System.in);
    public void menu() throws SQLException {


        while(true) {
            System.out.println("Press 1 to view all users\nPress 2 to create new user\nPress 3 to delete an user\nPress 4 to create new book");

            switch (input.nextInt()) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    createNewUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    createNewBook();
                    break;
            }
            input.nextLine();
        }
    }

    public void createNewBook(){
        input.nextLine();
        String publisher = null, title = null, author = null;
        double priceAB = 0, priceSAXO = 0, priceCDON = 0, ISBN = 0;
        int version = 0;
        boolean inputOk = false;

        System.out.println("Enter the book information: ");

        do {
            try {
                System.out.println("Enter title: ");
                title = input.nextLine();
                System.out.println("Enter author: ");
                author = input.nextLine();
                System.out.println("Enter publisher: ");
                publisher = input.nextLine();
                System.out.println("Enter version: ");
                version = input.nextInt();
                System.out.println("Enter book ISBN:");
                ISBN = input.nextDouble();
                System.out.println("Enter price at Amazon: ");
                priceAB = input.nextDouble();
                System.out.println("Enter price at SAXO:");
                priceSAXO = input.nextDouble();
                System.out.println("Enter price at CDON:");
                priceCDON = input.nextDouble();
                inputOk = true;
            } catch (InputMismatchException e) {
                System.out.println("Seems like you entered a bad value! Please try again!");
                inputOk = false;
            }
            input.nextLine();
        } while (!inputOk);

        Book book = new Book(publisher, title, author, version, ISBN, priceAB, priceSAXO, priceCDON);

        try {
            dbConnector.addCurriculumBook(book);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser() throws SQLException {
        input.nextLine();
        int userId = 0;

           ArrayList<User> users = dbConnector.getUsers();
        System.out.printf("%-15s %-30s %-30s %-25s %-25s %-15s\n", "User ID:", "Username:", "Firstname:", "Lastname:", "Email:", "Admin status:");
        for (User user : users) {
            System.out.printf("%-15d %-30s %-30s %-25s %-25s %-15b\n", user.getUserID(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserType());
        }

        System.out.println("Please enter userid on user you wish to delete:");
        userId = input.nextInt();
        System.out.println("Are you sure you want to delete the account? Write \"yes\" to confirm");
        if (!input.nextLine().equals("yes")) {
            try {
                dbConnector.deleteUser(userId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("You didn't write yes. Returning to menu...");
        }

    }
    public void createNewUser(){
        input.nextLine();
        String firstName, lastName, username, email, password;
        boolean adminStatus;
        System.out.println("Enter your firstname: ");
        firstName = input.nextLine();
        System.out.println("Enter your lastname: ");
        lastName = input.nextLine();
        System.out.println("Enter your username: ");
        username = input.nextLine();
        System.out.println("Enter your email: ");
        email = input.next();
        System.out.println("Enter your password: ");
        password = input.next();
        System.out.println("Is the new user an admin?");
        adminStatus = input.nextBoolean();
        User user = new User(firstName, lastName, username, email, password, adminStatus);
        user.setPassword(Digester.hashWithSalt(password));

        System.out.println("Are you sure that you want to create a new user with the following details:");
        System.out.printf("%-30s %-30s %-25s %-25s %-15s\n", "Username:", "Firstname:", "Lastname:", "Email:", "Admin status:");
        System.out.printf("%-30s %-30s %-25s %-25s %-15b\n", user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserType());
        System.out.println("Enter \"yes\" to confirm:");
        if (input.next().equals("yes")) {
            try {
                dbConnector.addUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("You didn't enter yes. Returning to main menu");
        }

    }

    public void viewAllUsers() throws SQLException{
        ArrayList<User> users = dbConnector.getUsers();
        // Header for showing users
        System.out.printf("%-15s %-30s %-30s %-25s %-25s %-15s\n", "User ID:", "Username:", "Firstname:", "Lastname:", "Email:", "Admin status:");
        for (User user : users) {
            System.out.printf("%-15d %-30s %-30s %-25s %-25s %-15b\n", user.getUserID(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getUserType());
        }
    }

}



package database;

import com.google.gson.Gson;
import config.Config;
import model.Book;
import model.Curriculum;
import model.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by mortenlaursen on 17/10/2016.
 */
public class DBConnector {
    /**
     * Constructor for establishing connection.
     *
     * @throws Exception
     */
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://" + Config.getDbUrl() + ":" + Config.getDbPort() + "/" + Config.getDbName();

    //  Database credentials
    static final String USER = Config.getDbUserName();
    static final String PASS = Config.getDbPassword();

    Connection conn = null;
    Statement stmt = null;

    public DBConnector() {

        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER).newInstance();

            //STEP 3: Open a connection
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            this.stmt = conn.createStatement();

            System.out.println("Connected");

            //STEP 6: Clean-up environment
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all users that isnt deleted
     *
     * @return
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    public ArrayList getUsers() throws IllegalArgumentException, SQLException {
        ArrayList results = new ArrayList();
        ResultSet resultSet = null;

        try {
            PreparedStatement getUsers = conn.prepareStatement("SELECT * FROM Users WHERE Deleted = 0 ");
            resultSet = getUsers.executeQuery();

            while (resultSet.next()) {
                try {

                    User users = new User(
                            resultSet.getInt("UserID"),
                            resultSet.getString("First_Name"),
                            resultSet.getString("Last_Name"),
                            resultSet.getString("Username"),
                            resultSet.getString("Email"),
                            resultSet.getString("Password"),
                            resultSet.getBoolean("Usertype")
                    );

                    results.add(users);

                } catch (Exception e) {

                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            close();
        }
        return results;

    }

    /**
     * Returns a single user
     *
     * @param id
     * @return
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    public User getUser(int id) throws IllegalArgumentException, SQLException {
        User user = null;
        ResultSet resultSet = null;

        try {
            PreparedStatement getUser = conn.prepareStatement("SELECT * FROM Users WHERE UserID=? and Deleted = 0");
            getUser.setInt(1, id);
            resultSet = getUser.executeQuery();


            while (resultSet.next()) {
                try {

                    user = new User(
                            resultSet.getInt("UserID"),
                            resultSet.getString("First_Name"),
                            resultSet.getString("Last_Name"),
                            resultSet.getString("Username"),
                            resultSet.getString("Email"),
                            resultSet.getString("Password"),
                            resultSet.getBoolean("Usertype")
                    );

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }


        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            close();
        }
        return user;
    }

    /**
     * Method for editing a users information
     *
     * @param u
     * @return
     * @throws SQLException
     */
    public boolean editUser(User u) throws SQLException {
        PreparedStatement editUserStatement = conn
                .prepareStatement("UPDATE Users SET First_Name = ?, Last_Name = ?, Username = ?, Email = ?, Usertype = ?, Password = ? WHERE userID =?");

        try {
            editUserStatement.setString(1, u.getFirstName());
            editUserStatement.setString(2, u.getLastName());
            editUserStatement.setString(3, u.getUserName());
            editUserStatement.setString(4, u.getEmail());
            editUserStatement.setBoolean(5, u.getUserType());
            editUserStatement.setString(6, u.getPassword());
            editUserStatement.setInt(7, u.getUserID());


            editUserStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

        return true;
    }

    /**
     * Method for adding an user
     *
     * @param u
     * @return
     * @throws Exception
     */
    public boolean addUser(User u) throws Exception {

        PreparedStatement addUserStatement =
                conn.prepareStatement("INSERT INTO Users (First_Name, Last_Name, Username, Email, Password, Usertype) VALUES (?, ?, ?, ?, ?, ?)");

        try {
            addUserStatement.setString(1, u.getFirstName());
            addUserStatement.setString(2, u.getLastName());
            addUserStatement.setString(3, u.getUserName());
            addUserStatement.setString(4, u.getEmail());
            addUserStatement.setString(5, u.getPassword());
            addUserStatement.setBoolean(6, u.getUserType());

            addUserStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    /**
     * Method for deleting an user, by setting "Deleted" =1
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteUser(int id) throws SQLException {

        PreparedStatement deleteUserStatement = conn.prepareStatement("UPDATE Users SET Deleted = 1 WHERE UserID=?");

        try {
            deleteUserStatement.setInt(1, id);
            deleteUserStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
            close();

        return true;
    }

    /*Curriculum methods*/

    /**
     * Returns all curriculums
     *
     * @return
     * @throws IllegalArgumentException
     */
    public ArrayList getCurriculums() throws IllegalArgumentException, SQLException {
        ArrayList<Curriculum> results = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            PreparedStatement getCurriculums = conn.prepareStatement("SELECT * FROM Curriculum ");
            resultSet = getCurriculums.executeQuery();

            while (resultSet.next()) {
                try {

                    Curriculum cul = new Curriculum(
                            resultSet.getInt("CurriculumID"),
                            resultSet.getString("School"),
                            resultSet.getString("Education"),
                            resultSet.getInt("Semester")
                    );

                    results.add(cul);

                    String test = resultSet.getString("School");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            close();
        }
        return results;

    }

    /**
     * Methos for returning a single curriculum
     *
     * @param curriculumID
     * @return
     * @throws IllegalArgumentException
     */
    public Curriculum getCurriculum(int curriculumID) throws IllegalArgumentException, SQLException {
        ResultSet resultSet = null;
        Curriculum curriculum = null;

        try {
            PreparedStatement getCurriculum = conn.prepareStatement("SELECT * FROM Curriculum WHERE CurriculumID=?");
            getCurriculum.setInt(1, curriculumID);
            resultSet = getCurriculum.executeQuery();

            while (resultSet.next()) {
                try {

                    curriculum = new Curriculum(
                            resultSet.getInt("CurriculumID"),
                            resultSet.getString("School"),
                            resultSet.getString("Education"),
                            resultSet.getInt("Semester")
                    );

                } catch (Exception e) {

                }
            }
        } catch (SQLException sqlException) {

            System.out.println(sqlException.getMessage());
        } finally {
            close();
        }
        return curriculum;

    }

    /**
     * Methos for editing a curriculum
     *
     * @param id
     * @param data
     * @return
     * @throws SQLException
     */
    public boolean editCurriculum(int id, String data) throws SQLException {
        PreparedStatement editCurriculumStatement = conn.prepareStatement("UPDATE Curriculum SET School = ?, Education = ?, Semester = ? WHERE curriculumID = ?");

        Curriculum c = new Gson().fromJson(data, Curriculum.class);

        try {
            editCurriculumStatement.setString(1, c.getSchool());
            editCurriculumStatement.setString(2, c.getEducation());
            editCurriculumStatement.setInt(3, c.getSemester());
            editCurriculumStatement.setInt(4, id);

            editCurriculumStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    /**
     * Method for adding a curriculum
     *
     * @param c
     * @return
     * @throws SQLException
     */
    public boolean addCurriculum(Curriculum c) throws SQLException {
        PreparedStatement addCurriculumStatement = conn.prepareStatement("INSERT INTO Curriculum (School, Education, Semester) VALUES (?, ?, ?)");

        try {

            addCurriculumStatement.setString(1, c.getSchool());
            addCurriculumStatement.setString(2, c.getEducation());
            addCurriculumStatement.setInt(3, c.getSemester());

            addCurriculumStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    /**
     * Method for deleting a curriculum by setting "Deleted" = 1
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteCurriculum(int id) throws SQLException {
        PreparedStatement deleteCurriculumStatement = conn.prepareStatement("UPDATE Curriculum SET Deleted = 1 WHERE CurriculumID=?");

        try {
            deleteCurriculumStatement.setInt(1, id);
            deleteCurriculumStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    /**
     * Returns the Books in a specific curriculum
     * @param curriculumID
     * @return
     */
    public ArrayList<Book> getCurriculumBooks(int curriculumID) throws SQLException {
        ArrayList results = new ArrayList();
        ResultSet resultSet = null;


        try {
            PreparedStatement getCurriculumBooks = conn.prepareStatement("SELECT * FROM Books INNER JOIN BooksCurriculum ON Books.BookID=BooksCurriculum.BookID WHERE CurriculumID = ? and  Books.Deleted=0");
            getCurriculumBooks.setInt(1, curriculumID);
            resultSet = getCurriculumBooks.executeQuery();

            while (resultSet.next()) {
                try {

                    Book books = new Book(
                            resultSet.getInt("BookID"),
                            resultSet.getString("Publisher"),
                            resultSet.getString("Title"),
                            resultSet.getString("Author"),
                            resultSet.getInt("Version"),
                            resultSet.getDouble("ISBN"),
                            resultSet.getDouble("PriceAB"),
                            resultSet.getDouble("PriceSAXO"),
                            resultSet.getDouble("PriceCDON")
                    );

                    results.add(books);

                } catch (Exception e) {

                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            close();
        }
        return results;
    }

    /*books methods*/

    /**
     * Returns all books that isn't deleted
     * @return
     * @throws IllegalArgumentException
     */
    public ArrayList getBooks() throws IllegalArgumentException, SQLException {

        ArrayList<Book> results = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            PreparedStatement getBooks = conn.prepareStatement("SELECT * FROM Books WHERE Deleted = 0 ");
            resultSet = getBooks.executeQuery();

            while (resultSet.next()) {
                try {

                    Book books = new Book(
                            resultSet.getInt("BookID"),
                            resultSet.getString("Publisher"),
                            resultSet.getString("Title"),
                            resultSet.getString("Author"),
                            resultSet.getInt("Version"),
                            resultSet.getDouble("ISBN"),
                            resultSet.getDouble("PriceAB"),
                            resultSet.getDouble("PriceSAXO"),
                            resultSet.getDouble("PriceCDON")
                    );

                    results.add(books);

                } catch (Exception e) {

                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        } finally {
            close();
        }
        return results;

    }

    /**
     * Returns a single book
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    public Book getBook(int id) throws IllegalArgumentException {
        Book book = null;
        ResultSet resultSet = null;

        try {
            PreparedStatement getBook = conn.prepareStatement("SELECT * FROM Books WHERE BookID=? ");
            getBook.setInt(1, id);
            resultSet = getBook.executeQuery();
            resultSet.next();
            book = new Book(
                    resultSet.getInt("BookID"),
                    resultSet.getString("Publisher"),
                    resultSet.getString("Title"),
                    resultSet.getString("Author"),
                    resultSet.getInt("Version"),
                    resultSet.getDouble("ISBN"),
                    resultSet.getDouble("PriceAB"),
                    resultSet.getDouble("PriceSAXO"),
                    resultSet.getDouble("PriceCDON")
            );
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return book;

    }

    /**
     * Method used to edit book information
     * @param id
     * @param data
     * @return
     * @throws SQLException
     */
    public boolean editBook(int id, String data) throws SQLException {
        PreparedStatement editBookStatement = conn.prepareStatement("UPDATE Books SET Title = ?, Version = ?, ISBN = ?, PriceAB = ?, PriceSAXO = ?, PriceCDON = ?, Publisher = ?, Author = ? WHERE bookID =?");
        Book b = new Gson().fromJson(data, Book.class);
        try {
            editBookStatement.setString(1, b.getTitle());
            editBookStatement.setInt(2, b.getVersion());
            editBookStatement.setDouble(3, b.getISBN());
            editBookStatement.setDouble(4, b.getPriceAB());
            editBookStatement.setDouble(5, b.getPriceSAXO());
            editBookStatement.setDouble(6, b.getPriceCDON());
            editBookStatement.setString(7, b.getPublisher());
            editBookStatement.setString(8, b.getAuthor());
            editBookStatement.setInt(9, id);

            editBookStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    /**
     * Lets you create a new book and adds the book to the BooksCurriculum table.
     * @param b
     * @return
     * @throws SQLException
     */
    public boolean addCurriculumBook(Book b) throws SQLException {
        int id;
        PreparedStatement addBookStatement = conn.prepareStatement("INSERT INTO Books (Title, Version, ISBN, PriceAB, PriceSAXO, PriceCDON, Publisher, Author) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

        try {
            addBookStatement.setString(1, b.getTitle());
            addBookStatement.setInt(2, b.getVersion());
            addBookStatement.setDouble(3, b.getISBN());
            addBookStatement.setDouble(4, b.getPriceAB());
            addBookStatement.setDouble(5, b.getPriceSAXO());
            addBookStatement.setDouble(6, b.getPriceCDON());
            addBookStatement.setString(7, b.getPublisher());
            addBookStatement.setString(8, b.getAuthor());

            addBookStatement.executeUpdate();
            ResultSet rs = addBookStatement.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);

                PreparedStatement addToBooksCurriculum = conn.prepareStatement("INSERT INTO BooksCurriculum (BookID, CurriculumID) VALUES (?,?)");
                addToBooksCurriculum.setInt(1, id);
                addToBooksCurriculum.setInt(2, b.getCurriculumID());
                addToBooksCurriculum.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }

        return true;

    }


    /**
     * Method for deleting a book.
     * @param id
     * @return
     * @throws SQLException
     */
    public boolean deleteBook(int id) throws SQLException {
        PreparedStatement deleteUserStatement = conn.prepareStatement("UPDATE Books SET Deleted = 1 WHERE BookID = ?");
        PreparedStatement deleteBooksCurriculumRows = conn.prepareStatement("DELETE FROM BooksCurriculum WHERE BookID = ?");

        try {
            deleteUserStatement.setInt(1, id);
            deleteUserStatement.executeUpdate();

            deleteBooksCurriculumRows.setInt(1, id);
            deleteBooksCurriculumRows.executeQuery();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    /**
     * Authentiates an user and returns the user's info.
     * @param username
     * @param password
     * @return
     */
    public User authenticate(String username, String password) throws SQLException {

        ResultSet resultSet = null;
        User userFound = null;

        try {
            PreparedStatement authenticate = conn.prepareStatement("select * from Users where username = ? AND Password = ? AND Deleted = 0");
            authenticate.setString(1, username);
            authenticate.setString(2, password);


            resultSet = authenticate.executeQuery();

            while (resultSet.next()) {
                try {
                    userFound = new User();
                    userFound.setUserID(resultSet.getInt("UserID"));

                } catch (SQLException e) {

                }


            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return userFound;

    }

    /**
     * Used to check if the users token is ecists and is valid.
     * @param token
     * @return
     * @throws SQLException
     */
    public User getUserFromToken(String token) throws SQLException {
        ResultSet resultSet = null;
        User userFromToken = null;


        try {

            PreparedStatement getUserFromToken = conn
                    .prepareStatement("select Tokens.user_id, Users.Usertype, Users.UserID, Users.First_Name, Users.Last_Name, Users.Username, Users.Email, Users.Password from Tokens inner join Users on Tokens.user_id = Users.UserID where Tokens.token = ? and Tokens.UpdateTs >= DATE_SUB(NOW(), interval 1 minute)");
            getUserFromToken.setString(1, token);
            resultSet = getUserFromToken.executeQuery();

            while (resultSet.next()) {

                userFromToken = new User(
                        resultSet.getInt("UserID"),
                        resultSet.getString("First_Name"),
                        resultSet.getString("Last_Name"),
                        resultSet.getString("Username"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getBoolean("Usertype"));

            }
            if(userFromToken != null){
                PreparedStatement updateTimeStamp = conn.prepareStatement("UPDATE Tokens set UpdateTs = CURRENT_TIMESTAMP where token = ? ");
                updateTimeStamp.setString(1, token);
                updateTimeStamp.executeUpdate();
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return userFromToken;

    }

    public void addToken(String token, int userId) {

        PreparedStatement addTokenStatement;
        try {
            addTokenStatement = conn.prepareStatement("INSERT INTO Tokens (token, user_id) VALUES (?,?)");
            addTokenStatement.setString(1, token);
            addTokenStatement.setInt(2, userId);
            addTokenStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public boolean deleteToken(String token) throws SQLException {

        PreparedStatement deleteTokenStatement = conn.prepareStatement("DELETE FROM Tokens WHERE token=?");

        try {
            deleteTokenStatement.setString(1, token);
            deleteTokenStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close();
        }
        return true;
    }

    public void close() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
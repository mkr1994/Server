package endpoints; /**
 * Created by mortenlaursen on 09/10/2016.
 */

import com.google.gson.Gson;
import controllers.TokenController;
import controllers.UserController;
import model.User;
import Encrypters.Crypter;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

/**
 *  The Java class will be hosted at the URI path "/users" and contains alle user related endpoints
 */
@Path("/user")
public class UsersEndpoint {
    UserController controller;
    TokenController tokenController;

    public UsersEndpoint() {
        this.controller = new UserController();
        this.tokenController = new TokenController();
    }

    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain" and returns all users
    @Produces("application/json")

    public Response get(@HeaderParam("authorization") String authToken) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated
            if (controller.getUsers() != null) { //Returns 400 if no users exists
                return Response
                        .status(200)
                        .entity(Crypter.encryptDecryptXOR(new Gson().toJson(controller.getUsers())))
                        .build();
            } else {
                return Response
                        .status(400)
                        .entity("{\"message\":\"failed\"}")
                        .build();
            }
        } else return Response.status(401).entity("{\"message\":\"unauthorized\"}").build();

    }


    // Methpd returns a single users from token id.
    @GET
    @Produces("application/json")
    @Path("/fromToken")
    public Response getFromToken(@HeaderParam("authorization") String authToken) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated
            return Response
                    .status(200)
                    .entity(Crypter.encryptDecryptXOR(new Gson().toJson(user)))
                    .build();
        } else {
            return Response
                    //error response
                    .status(401)
                    .entity("{\"message\":\"unauthorized\"}")
                    .build();

        }

    }

    // Method returns 1 specific user
    @Path("/{id}")
    @Produces("application/json")
    @GET
    public Response get(@HeaderParam("authorization") String authToken, @PathParam("id") int userId) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated
            if (controller.getUser(userId) != null) { //Return 400 if statement fails
                return Response
                        .status(200)
                        .entity(Crypter.encryptDecryptXOR(new Gson().toJson(controller.getUser(userId))))
                        .build();
            }
            return Response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();

        } else return Response
                .status(401)
                .entity("{\"message\":\"unauthorized\"}")
                .build();
    }

    // Edit specific user
    @PUT
    @Path("/{Id}")
    @Produces("application/json")
    public Response edit(@HeaderParam("authorization") String authToken, @PathParam("Id") int id, String data) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated
            String decrypt = Crypter.encryptDecryptXOR(data);
            User u = new Gson().fromJson(decrypt, User.class);
            if (controller.getUser(u.getUserID()) != null) { // return 400 if user doesn't exist
                if (controller.editUser(u)) { //return 400 if statement fails
                    return Response
                            .status(200)
                            .entity("{\"message\":\"Success! User edited\"}")
                            .build();
                } else {
                    return Response
                            .status(400)
                            .entity("{\"message\":\"failed\"}")
                            .build();
                }
            } else {
                return Response
                        .status(400)
                        .entity("{\"message\":\"failed. No such user\"}")
                        .build();
            }

        } else return Response.status(401).entity("{\"message\":\"unauthorized\"}").build();


    }

    // Create new user
    @POST
    @Produces("application/json")
    public Response create(String data) throws Exception {
        String decrypt = Crypter.encryptDecryptXOR(data);

        if (controller.addUser(decrypt)) { //Return 400 if statement fails
            return Response
                    .status(200)
                    .entity("{\"message\":\"Success! User added\"}")
                    .build();
        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
    }

    // delete user
    @Path("/{id}")
    @DELETE
    public Response delete(@HeaderParam("authorization") String authToken, @PathParam("id") int userId) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated
            if (controller.deleteUser(userId)) { //return 400 if statement fails
                return Response.status(200).entity("{\"message\":\"Success! User deleted\"}").build();
            } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
        } else return Response.status(401).entity("{\"message\":\"unauthorized\"}").build();


    }

    // Login method that returns a token on successful login.
    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(String data) throws SQLException {
        String decrypt = Crypter.encryptDecryptXOR(data);

        User userLogin = new Gson().fromJson(decrypt, User.class);

        String token = tokenController.authenticate(userLogin.getUserName(), userLogin.getPassword());

        if (token != null) { //Return 401 if user isn't authenticated
            return Response
                    .status(200).entity(token).build();
        } else return Response
                .status(401)
                .build();
    }

    // Logout that deletes  token from database
    @POST
    @Path("/logout")
    public Response logout(String data) throws SQLException {
        if (tokenController.deleteToken(data)) {
            return Response
                    .status(200)
                    .entity("Success!")
                    .build();

        } else return Response
                .status(400)
                .entity("Failure")
                .build();
    }

}


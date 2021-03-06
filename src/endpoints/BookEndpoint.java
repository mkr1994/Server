package endpoints; /**
 * Created by mortenlaursen on 09/10/2016.
 */

import com.google.gson.Gson;
import controllers.BookController;
import controllers.TokenController;
import model.Book;
import model.User;
import Encrypters.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 *
 The Java class will be hosted at the URI path "/Book" and contains all endpoints related to books
 */
@Path("/book")
public class BookEndpoint {

    BookController controller;
    TokenController tokenController;
    Gson gson;

    public BookEndpoint() {
        this.controller = new BookController();
        this.tokenController = new TokenController();
        this.gson = new Gson();
    }

    // The Java method will process HTTP GET requests and returns all books
    @GET
    @Produces("application/json")
    public Response get() throws Exception {

        ArrayList<Book> bookArrayList = controller.getBooks();
        if (bookArrayList != null) {
            return Response
                    .status(200)
                    .entity(Crypter.encryptDecryptXOR(gson.toJson(bookArrayList)))
                    .build();
        } else {
            return Response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();
        }
    }


    // Returns one specific book
    @Path("/{id}")
    @Produces("application/json")
    @GET
    public Response get(@PathParam("id") int bookId) throws Exception {
        if (controller.getBook(bookId) != null) {
            return Response
                    .status(200)
                    .entity(Crypter.encryptDecryptXOR(new Gson().toJson(controller.getBook(bookId))))
                    .build();
        } else {
            return Response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();
        }
    }

    // Edit one specific book
    @PUT
    @Path("/{bookId}")
    @Produces("application/json")
    public Response edit(@HeaderParam("authorization") String authToken, @PathParam("bookId") int id, String data) throws Exception {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated

            if (controller.getBook(id) != null) { // Return 400 if book dosn't exist
                String s = new Gson().fromJson(data, String.class);
                String decrypt = Crypter.encryptDecryptXOR(s);
                if (controller.editBook(id, decrypt)) { //Return 400 if edit fails.
                    return Response
                            .status(200)
                            .entity("{\"message\":\"Success! Book edited\"}")
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
                        .entity("{\"message\":\"failed. Book not found\"}")
                        .build();
            }

        } else return Response.status(401).entity("{\"message\":\"unauthorized\"}").build();


    }

    // Create new book
    @POST
    @Produces("application/json")
    public Response create(@HeaderParam("authorization") String authToken, String data) throws Exception {
        String decrypt = Crypter.encryptDecryptXOR(data);

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated
            if (controller.addBook(decrypt)) { //Return 400 if creation doesn't succeed.
                return Response
                        .status(200)
                        .entity("{\"message\":\"Success! Book created\"}")
                        .build();
            } else {
                return Response
                        .status(400)
                        .entity("{\"message\":\"failed\"}")
                        .build();
            }
        } else return Response.status(401).entity("{\"message\":\"unauthorized\"}").build();
    }


    // Delete specific book
    @Path("/{id}")
    @DELETE
    public Response delete(@HeaderParam("authorization") String authToken, @PathParam("id") int bookId) throws Exception {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) { //Return 401 if user isn't authenticated

            if (controller.deleteBook(bookId)) { //Return 400 if deletion doesn't succeed.
                return Response.status(200).entity("{\"message\":\"Success! Book deleted\"}").build();
            } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
        } else return Response.status(401).entity("{\"message\":\"unauthorized\"}").build();


    }
}


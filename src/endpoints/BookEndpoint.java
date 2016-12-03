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

// The Java class will be hosted at the URI path "/Book"

@Path("/book")
public class BookEndpoint {

    BookController controller = new BookController();
    TokenController tokenController = new TokenController();

    public BookEndpoint() {
    }

    // The Java method will process HTTP GET requests

    @GET
    @Produces("application/json")
    public Response get() throws Exception {

        ArrayList<Book> bookArrayList = controller.getBooks();
        if (bookArrayList != null) {
            return Response
                    .status(200)
                    .entity(Crypter.encryptDecryptXOR(new Gson().toJson(bookArrayList)))
                    .build();
        } else {
            return Response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();
        }
    }


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

    @PUT
    @Path("/{bookId}")
    @Produces("application/json")

    public Response edit(@HeaderParam("authorization") String authToken, @PathParam("bookId") int id, String data) throws Exception {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) {

            if (controller.getBook(id) != null) {
                String s = new Gson().fromJson(data, String.class);
                String decrypt = Crypter.encryptDecryptXOR(s);
                if (controller.editBook(id, decrypt)) {
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

        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();


    }

    @POST
    @Produces("application/json")
    public Response create(@HeaderParam("authorization") String authToken, String data) throws Exception {
        String decrypt = Crypter.encryptDecryptXOR(data);

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) {
            if (controller.addBook(decrypt)) {
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
        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
    }


    @Path("/{id}")
    @DELETE
    public Response delete(@HeaderParam("authorization") String authToken, @PathParam("id") int bookId) throws Exception {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) {

            if (controller.deleteBook(bookId)) {
                return Response.status(200).entity("{\"message\":\"Success! Book deleted\"}").build();
            } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();


    }
}


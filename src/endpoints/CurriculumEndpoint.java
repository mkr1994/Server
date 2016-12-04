package endpoints;

import com.google.gson.Gson;
import controllers.CurriculumController;
import controllers.TokenController;
import Encrypters.*;
import model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.sql.SQLException;


/**
 * Created by magnusrasmussen on 17/10/2016.
 */

@Path("/curriculum")
public  class CurriculumEndpoint  {
    CurriculumController curriculumController;

    TokenController tokenController;


    public CurriculumEndpoint() {
        this.curriculumController = new CurriculumController();
        this.tokenController = new TokenController();
    }


    /**
     * Metode til at hente alle bøgerne på et semester
     *
     * @param curriculumID
     * @return
     * @throws IllegalAccessException
     */
    @GET
    @Path("/{curriculumID}/books")
    @Produces("application/json")
    public Response getCurriculumBooks(@PathParam("curriculumID") int curriculumID) throws Exception {

        if (curriculumController.getCurriculum(curriculumID) != null) {
            return Response
                    .status(200)
                    .entity(Crypter.encryptDecryptXOR(new Gson().toJson(curriculumController.getCurriculumBooks(curriculumID))))
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } else {
            return Response
                    //error response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();
        }
    }

    /**
     * Metode til at hente alle semestre
     *
     * @return
     * @throws IllegalAccessException
     */
    @GET
    @Produces("application/json")
    public Response get() throws Exception {

        if (curriculumController.getCurriculums() != null) {
            return Response
                    .status(200)
                    .entity(Crypter.encryptDecryptXOR(new Gson().toJson(curriculumController.getCurriculums())))
                    .header("Access-Control-Allow-Origin", "*")
                    .build(); //kør
        } else {
            return Response
                    //error response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();
        }
    }

    /**
     * Metode til at hente et bestemt semester
     *
     * @param id
     * @return
     * @throws IllegalAccessException
     */
    @GET
    @Path("/{curriculumID}")
    @Produces("application/json")
    public Response get(@PathParam("curriculumID") int id) throws Exception {


        if (curriculumController.getCurriculums() != null) {
            return Response
                    .status(200)
                    .entity(new Gson().toJson(Crypter.encryptDecryptXOR(new Gson().toJson(curriculumController.getCurriculum(id)))))
                    .header("Access-Control-Allow-Origin", "*")
                    .build(); //kør
        } else {
            return Response
                    //error response
                    .status(400)
                    .entity("{\"message\":\"failed\"}")
                    .build();
        }
    }

    /**
     * Metode til at oprette nyt semester
     *
     * @param data
     * @return
     * @throws Exception
     */

    @POST
    @Produces("application/json")
    public Response create(@HeaderParam("authorization") String authToken, String data) throws Exception {
        User user = tokenController.getUserFromTokens(authToken);


        if (user != null) {
            String decrypt = Crypter.encryptDecryptXOR(data);
            if (curriculumController.addCurriculum(decrypt)) {
                return Response
                        .status(200)
                        .entity("Success!")
                        .build();
            } else {
                return Response
                        .status(400)
                        .build();
            }

        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
    }



    @POST
    @Path("/{curriculumID}/book")
    @Produces("application/json")

    public Response addToCurriculum(@HeaderParam("authorization") String authToken, String data) throws Exception {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null){
            String s = new Gson().fromJson(data,String.class);
            String decrypt = Crypter.encryptDecryptXOR(s);
            if (curriculumController.addCurriculumBook(decrypt)) {
                return Response
                        .status(200)
                        .entity("Success!")
                        .build();
            }
            else {
                return Response
                        .status(400)
                        .build();
            }

        }else return Response.status(400).entity("{\"message\":\"failed\"}").build();
    }

    /**
     * Metode til at ændre et semester
     *
     * @return
     */

    @PUT
    @Path("/{curriculumID}")
    @Produces("application/json")

    public Response edit(@HeaderParam("authorization") String authToken, @PathParam("curriculumID") int id, String data) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) {
            if (curriculumController.getCurriculum(id) != null) {
                String s = new Gson().fromJson(data, String.class);
                String decrypt = Crypter.encryptDecryptXOR(s);
                if (curriculumController.editCurriculum(id, decrypt)) {
                    return Response
                            .status(200)
                            .entity("{\"message\":\"Success! Curriculum was changed.\"}")
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
                        .entity("{\"message\":\"failed. Curriculum doesn't exist.\"}")
                        .build();
            }
        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();

    }


    /**
     * Metode til at slette et semester
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @DELETE
    @Path("/{curriculumId}")
    @Produces("application/json")

    public Response delete(@HeaderParam("authorization") String authToken, @PathParam("curriculumId") int id) throws SQLException {

        User user = tokenController.getUserFromTokens(authToken);

        if (user != null) {
            if (curriculumController.deleteCurriculum(id)) {
                return Response.status(200).entity("{\"message\":\"Curriculum was deleted\"}").build();
            } else return Response.status(400).entity("{\"message\":\"Failed. Curriculum was not deleted\"}").build();
        } else return Response.status(400).entity("{\"message\":\"failed\"}").build();
    }
}

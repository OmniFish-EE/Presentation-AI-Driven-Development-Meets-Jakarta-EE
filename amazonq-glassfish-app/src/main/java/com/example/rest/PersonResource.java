package com.example.rest;

import com.example.entity.Person;
import com.example.service.PersonService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * REST resource for Person entity operations
 * Follows single responsibility principle by delegating business logic to service layer
 */
@Path("/people")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private static final Logger LOGGER = Logger.getLogger(PersonResource.class.getName());

    @Inject
    private PersonService personService;

    @GET
    public List<Person> getAllPeople() {
        LOGGER.info("Retrieving all people");
        return personService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getPerson(@PathParam("id") @NotNull Long id) {
        LOGGER.info(() -> "Retrieving person with ID: " + id);
        return personService.findById(id)
                .map(person -> Response.ok(person).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createPerson(@Valid Person person) {
        LOGGER.info(() -> "Creating new person: " + person.getFirstName() + " " + person.getLastName());
        try {
            Person created = personService.create(person);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid person data: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") @NotNull Long id, @Valid Person person) {
        LOGGER.info(() -> "Updating person with ID: " + id);
        try {
            return personService.update(id, person)
                    .map(updated -> Response.ok(updated).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid person data: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") @NotNull Long id) {
        LOGGER.info(() -> "Deleting person with ID: " + id);
        return personService.delete(id) 
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Simple error response DTO for consistent error handling
     */
    public static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() { return message; }
    }
}

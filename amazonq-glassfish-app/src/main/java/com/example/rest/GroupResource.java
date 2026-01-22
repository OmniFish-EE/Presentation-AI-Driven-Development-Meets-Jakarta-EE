package com.example.rest;

import com.example.entity.Group;
import com.example.service.GroupService;
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
 * REST resource for Group entity operations
 * Follows single responsibility principle by delegating business logic to service layer
 */
@Path("/groups")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {

    private static final Logger LOGGER = Logger.getLogger(GroupResource.class.getName());

    @Inject
    private GroupService groupService;

    @GET
    public List<Group> getAllGroups() {
        LOGGER.info("Retrieving all groups");
        return groupService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getGroup(@PathParam("id") @NotNull Long id) {
        LOGGER.info(() -> "Retrieving group with ID: " + id);
        return groupService.findById(id)
                .map(group -> Response.ok(group).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createGroup(@Valid Group group) {
        LOGGER.info(() -> "Creating new group: " + group.getName());
        try {
            Group created = groupService.create(group);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid group data: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PersonResource.ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateGroup(@PathParam("id") @NotNull Long id, @Valid Group group) {
        LOGGER.info(() -> "Updating group with ID: " + id);
        try {
            return groupService.update(id, group)
                    .map(updated -> Response.ok(updated).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid group data: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PersonResource.ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteGroup(@PathParam("id") @NotNull Long id) {
        LOGGER.info(() -> "Deleting group with ID: " + id);
        return groupService.delete(id) 
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}

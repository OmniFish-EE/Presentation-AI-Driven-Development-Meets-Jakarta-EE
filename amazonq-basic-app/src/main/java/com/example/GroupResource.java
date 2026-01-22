package com.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {
    private final GroupService groupService = new GroupService();
    
    @GET
    public List<Group> getGroups() {
        return groupService.findAll();
    }
    
    @POST
    public Response createGroup(Group group) {
        Group created = groupService.create(group);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteGroup(@PathParam("id") Long id) {
        groupService.delete(id);
        return Response.noContent().build();
    }
}

package com.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final PersonService personService = new PersonService();
    
    @GET
    public List<Person> getPeople() {
        return personService.findAll();
    }
    
    @GET
    @Path("/teachers")
    public List<Person> getTeachers() {
        return personService.findTeachers();
    }
    
    @GET
    @Path("/students")
    public List<Person> getStudents() {
        return personService.findStudents();
    }
    
    @POST
    public Response createPerson(Person person) {
        Person created = personService.create(person);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
    
    @PUT
    @Path("/{id}")
    public Person updatePerson(@PathParam("id") Long id, Person person) {
        return personService.update(id, person);
    }
    
    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.delete(id);
        return Response.noContent().build();
    }
}

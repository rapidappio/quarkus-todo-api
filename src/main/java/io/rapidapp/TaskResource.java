package io.rapidapp;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskRepository taskRepository;

    // Retrieve all tasks
    @GET
    public List<Task> list() {
        return taskRepository.listAll();
    }

    // Retrieve a single task by ID
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(task).build();
    }

    // Create a new task
    @POST
    @Transactional
    public Response create(Task task) {
        taskRepository.persist(task);
        return Response.status(Response.Status.CREATED).entity(task).build();
    }

    // Update an existing task
    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id);
        if (existingTask == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setCompleted(updatedTask.isCompleted());
        return Response.ok(existingTask).build();
    }

    // Delete a task by ID
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = taskRepository.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}

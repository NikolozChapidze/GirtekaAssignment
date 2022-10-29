package eu.girteka.assignment.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entity, long id) {
        super(String.format("%s not found with id %d", entity, id));
    }
}

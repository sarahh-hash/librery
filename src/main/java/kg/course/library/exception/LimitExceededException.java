package kg.course.library.exception;

public class LimitExceededException extends LibraryException {
    public LimitExceededException(String message) {
        super(message);
    }
}
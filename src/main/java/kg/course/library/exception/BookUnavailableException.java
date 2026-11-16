package kg.course.library.exception;

public class BookUnavailableException extends LibraryException {
    public BookUnavailableException(String message) {
        super(message);
    }
}
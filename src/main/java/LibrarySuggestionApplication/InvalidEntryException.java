package LibrarySuggestionApplication;

public class InvalidEntryException extends RuntimeException{
    String message;

    public InvalidEntryException (String message) {
        this.message = message;
    }

    @Override
    public String toString () {
        return this.message;
    }
}

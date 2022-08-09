package hello.jdbc.repository.ex;

public class MyDuplicateException extends MyDbException{
    public MyDuplicateException() {
        super();
    }

    public MyDuplicateException(String message) {
        super(message);
    }

    public MyDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateException(Throwable cause) {
        super(cause);
    }
}

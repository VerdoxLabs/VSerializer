package de.verdox.vserializer.json;

public class JsonUpdateMethodNotSupportedException extends RuntimeException {
    public JsonUpdateMethodNotSupportedException() {
    }

    public JsonUpdateMethodNotSupportedException(String message) {
        super(message);
    }

    public JsonUpdateMethodNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonUpdateMethodNotSupportedException(Throwable cause) {
        super(cause);
    }

    public JsonUpdateMethodNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

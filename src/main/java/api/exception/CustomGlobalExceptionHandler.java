package api.exception;

import static api.constant.ExceptionsConstantsHolder.MESSAGE;
import static api.constant.ExceptionsConstantsHolder.STATUS;
import static api.constant.ExceptionsConstantsHolder.TIME_STAMP;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            FileUploadException.class
    })
    protected ResponseEntity<Object> handleException(Exception exception) {
        Map<String, Object> body = getBody(exception, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getBody(Exception ex, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIME_STAMP, LocalDateTime.now());
        body.put(STATUS, status);
        body.put(MESSAGE, ex.getMessage());
        return body;
    }
}

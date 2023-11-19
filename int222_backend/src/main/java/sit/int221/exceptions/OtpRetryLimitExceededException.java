package sit.int221.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class OtpRetryLimitExceededException extends RuntimeException {
    public OtpRetryLimitExceededException(String message) {
        super(message);
    }
}

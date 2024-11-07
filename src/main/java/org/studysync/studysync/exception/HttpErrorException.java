package org.studysync.studysync.exception;

import lombok.Getter;
import org.studysync.studysync.config.HttpErrorCode;

@Getter
public class HttpErrorException extends RuntimeException{
    private final HttpErrorCode httpErrorCode;

    public HttpErrorException(HttpErrorCode httpErrorCode){
        super(httpErrorCode.getMessage());
        this.httpErrorCode = httpErrorCode;
    }
}


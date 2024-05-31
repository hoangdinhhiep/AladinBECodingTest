package com.tatoeba.exception;

import com.tatoeba.models.dto.BaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class TatoebaExceptionHandler {

    /**
     * HttpMediaTypeException
     *
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<BaseResponseDto> handleHttpMediaTypeException() {

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("HttpMediaTypeException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * HttpMediaTypeNotAcceptableException
     *
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<BaseResponseDto> handleHttpMediaTypeNotAcceptableException() {

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("HttpMediaTypeNotAcceptableException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * HttpMediaTypeNotSupportedException
     *
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<BaseResponseDto> handleHttpMediaTypeNotSupportedException() {

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("HttpMediaTypeNotSupportedException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * HttpRequestMethodNotSupportedException
     *
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponseDto> handleHttpRequestMethodNotSupportedException() {

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("HttpRequestMethodNotSupportedException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * MethodArgumentNotValidException
     *
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        // Get field errors and extract errors info
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("MethodArgumentNotValidException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * Handle ConstraintViolationException error and return detailed error cause
     *
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto> handleConstraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("ConstraintViolationException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * Handle MethodArgumentTypeMismatchException error and return detailed error cause
     *
     * @param exception MethodArgumentTypeMismatchException was catch
     * @return ResponseBodyDto with http code 400 and error info
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<BaseResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(exception.getName(), exception.getMessage());

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("MethodArgumentTypeMismatchException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .data(errorMap)
                        .build());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public ResponseEntity<BaseResponseDto> handle(MultipartException exception) {
        if (exception instanceof MaxUploadSizeExceededException) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(BaseResponseDto.builder()
                            .isSuccess(false)
                            .message("MaxUploadSizeExceededException")
                            .errorCode(ErrorCode.ERR_TOO_LARGE_DATA)
                            .build());
        }

        log.error("MultipartException", exception);

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("MultipartException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseBody
    public ResponseEntity<BaseResponseDto> handle(FileUploadException exception) {
        if (exception instanceof SizeException) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body(BaseResponseDto.builder()
                            .isSuccess(false)
                            .message("SizeException")
                            .errorCode(ErrorCode.ERR_TOO_LARGE_DATA)
                            .build());
        }

        log.error("FileUploadException", exception);

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("FileUploadException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<BaseResponseDto> handle(ServletRequestBindingException exception) {
        log.error("ServletRequestBindingException", exception);

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("ServletRequestBindingException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<BaseResponseDto> handle(MissingServletRequestPartException exception) {
        log.error("MissingServletRequestPartException", exception);

        return ResponseEntity.badRequest()
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("MissingServletRequestPartException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * Handle BusinessException error and return detailed error cause
     *
     * @param exception BusinessException was catch
     * @return BaseResponseDto with http code 200 and error info
     */
    @ExceptionHandler(TatoebaException.class)
    @ResponseBody
    public ResponseEntity<BaseResponseDto> handleBusinessException(TatoebaException exception) {
        log.error("handleBusinessException", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("TatoebaException")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }

    /**
     * Exception
     *
     * @param exception Exception
     * @return ResponseEntity
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto> handleAllException(Exception exception) {
        log.error("handleAllException", exception);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
        } else if (exception instanceof MethodNotAllowedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
        }

        // Return response with HTTP status 500
        return ResponseEntity.status(status)
                .body(BaseResponseDto.builder()
                        .isSuccess(false)
                        .message("Exception")
                        .errorCode(ErrorCode.ERR_GENERAL)
                        .build());
    }
}

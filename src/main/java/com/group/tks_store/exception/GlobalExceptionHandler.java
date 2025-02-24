//package com.group.tks_store.exception;//package exception;//package exception;
//
//import com.group.tks_store.common.model.ErrorResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ServiceException.class)
//    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
//       ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_ACCEPTABLE);
//    }
//
//}
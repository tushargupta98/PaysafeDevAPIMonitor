package com.paysafe.apimonitor.ws.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
//Reference https://www.toptal.com/java/spring-boot-rest-api-error-handling
public class ApiErrorVO {

   private HttpStatus status;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
   private LocalDateTime timestamp;
   private String message;
   private String debugMessage;

   private ApiErrorVO() {
       timestamp = LocalDateTime.now();
   }

   public ApiErrorVO(HttpStatus status) {
       this();
       this.status = status;
   }

   public ApiErrorVO(HttpStatus status, Throwable ex) {
       this();
       this.status = status;
       this.message = "Unexpected error";
       this.debugMessage = ex.getLocalizedMessage();
   }

   public ApiErrorVO(HttpStatus status, String message, Throwable ex) {
       this();
       this.status = status;
       this.message = message;
       this.debugMessage = ex.getLocalizedMessage();
   }
   public HttpStatus getStatus() {
	   return status;	   
   }
   
   public String getMessage() {
	   return message;	   
   }
   
   public String getDebugMessage() {
	   return debugMessage;	   
   }
}
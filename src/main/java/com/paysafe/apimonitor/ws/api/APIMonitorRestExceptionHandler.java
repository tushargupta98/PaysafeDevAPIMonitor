package com.paysafe.apimonitor.ws.api;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.paysafe.apimonitor.ws.model.ApiErrorVO;
/**
 * Reference: https://www.toptal.com/java/spring-boot-rest-api-error-handling
 * This Class helps in handling the Spring's rest api error caused during invocation.
 * For instance it lets the client know that the type of the interval passed is not long in case it isn't
 * I is helpful in providing more meaninful messages to the user as response in an event of an exception
 * @author tushar
 * @version 1.0
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class APIMonitorRestExceptionHandler  extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
			HttpMessageNotReadableException ex, HttpHeaders headers, 
			HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiErrorVO(HttpStatus.BAD_REQUEST, error, ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiErrorVO apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}

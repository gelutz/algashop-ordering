package com.lutz.algashop.ordering.presentation;

public class BadGatewayException extends RuntimeException {
	public BadGatewayException(String message, Throwable throwable) {
		super(message, throwable);
	}
}

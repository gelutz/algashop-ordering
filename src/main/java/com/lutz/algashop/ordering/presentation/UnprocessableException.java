package com.lutz.algashop.ordering.presentation;

public class UnprocessableException extends RuntimeException {
	public UnprocessableException(String message, Throwable cause) {
		super(message, cause);
	}
}

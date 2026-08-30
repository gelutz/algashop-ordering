package com.lutz.algashop.ordering.presentation;

public class GatewayTimeoutException extends RuntimeException {
	public GatewayTimeoutException(String message, Throwable e) {
		super(message, e);
	}
}

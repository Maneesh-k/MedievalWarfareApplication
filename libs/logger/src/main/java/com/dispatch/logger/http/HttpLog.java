package com.dispatch.logger.http;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HttpLog {

	private Long responseTime;

	private ResponseInfo response;

	private RequestInfo request;

	private Map<String, Object> context;

	private Map<String, String> error;
}

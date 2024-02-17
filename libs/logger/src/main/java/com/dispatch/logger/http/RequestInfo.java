package com.dispatch.logger.http;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {
	private String id;
	private String method;
	private String path;
	private String url;
	private Map<String, String> headers;
	private JsonNode body;
	private String ip;
	private Date timestamp;
}

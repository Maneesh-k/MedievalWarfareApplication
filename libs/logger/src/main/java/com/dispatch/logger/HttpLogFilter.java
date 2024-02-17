package com.dispatch.logger;

import com.dispatch.logger.http.HttpLog;
import com.dispatch.logger.http.RequestInfo;
import com.dispatch.logger.http.ResponseInfo;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class HttpLogFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		ContentCachingRequestWrapper wrappedRequest =
				new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);

		ContentCachingResponseWrapper wrappedResponse =
				new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

		setInitialLog(wrappedRequest);

		chain.doFilter(wrappedRequest, wrappedResponse);

		emitLog(wrappedRequest, wrappedResponse);

		wrappedResponse.copyBodyToResponse();
	}

	protected void setInitialLog(ContentCachingRequestWrapper request) {

		String url = request.getRequestURI();

		if (request.getQueryString() != null) url = url + "?" + request.getQueryString();

		Map<String, String> headers = this.getRequestHeaders(request);

		String requestId = headers.get("x-request-id");

		RequestInfo requestInfo = new RequestInfo(
				requestId == null ? UUID.randomUUID().toString() : requestId,
				request.getMethod(),
				request.getRequestURI(),
				url,
				headers,
				null,
				this.extractIpAddress(headers),
				new Date(Instant.now().toEpochMilli()));

		HttpLog log = new HttpLog();

		log.setRequest(requestInfo);

		HttpLogger.set(log);
	}

	private Map<String, String> getRequestHeaders(ContentCachingRequestWrapper request) {
		Enumeration<String> headers = request.getHeaderNames();

		Map<String, String> headersMap = new HashMap<String, String>();

		while (headers.hasMoreElements()) {
			String header = headers.nextElement();

			headersMap.put(header, request.getHeader(header));
		}

		return headersMap;
	}

	private Map<String, String> getResponseHeaders(ContentCachingResponseWrapper response) {

		Collection<String> headers = response.getHeaderNames();

		if (headers.size() == 0) return null;

		Map<String, String> headersMap = new HashMap<String, String>();

		for (String header : headers) {
			headersMap.put(header, response.getHeader(header));
		}

		return headersMap;
	}

	protected void emitLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {

		HttpLog log = HttpLogger.take();

		RequestInfo requestInfo = log.getRequest();

		try {
			JsonNode jsonBody = new ObjectMapper().readTree(new String(request.getContentAsByteArray()));

			requestInfo.setBody(jsonBody);
		} catch (Exception e) {
		}

		log.setRequest(requestInfo);

		Long responseTime =
				Instant.now().toEpochMilli() - requestInfo.getTimestamp().getTime();

		log.setResponseTime(responseTime);

		log.setContext(ContextLogger.take());

		log.setError(ErrorLogger.take());

		Map<String, String> responseHeader = this.getResponseHeaders(response);

		ResponseInfo logResponse = new ResponseInfo(response.getStatus(), responseHeader);

		log.setResponse(logResponse);

		try {
			String stringifiedJson = new ObjectMapper().writeValueAsString(log);

			logger.info(stringifiedJson);
		} catch (Exception e) {
		}
	}

	private String extractIpAddress(Map<String, String> headers) {
		String xForwardedFor = headers.get("x-forwarded-for");

		if (xForwardedFor == null || xForwardedFor.isEmpty()) return null;

		String[] ipAddressList = xForwardedFor.split(",");
		return ipAddressList[0].trim();
	}
}

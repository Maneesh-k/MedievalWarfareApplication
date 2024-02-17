package com.dispatch.logger;

import com.dispatch.logger.http.HttpLog;
import com.dispatch.logger.http.RequestInfo;
import com.dispatch.logger.http.ResponseInfo;
import com.dispatch.logger.utils.Utils;

import java.nio.charset.Charset;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;

@Plugin(name = "CustomJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE)
public class CustomJsonLayout extends AbstractStringLayout {
	private final ObjectMapper objectMapper = new ObjectMapper();

	public CustomJsonLayout(
			Configuration config, Charset aCharset, Serializer headerSerializer, Serializer footerSerializer) {
		super(config, aCharset, headerSerializer, footerSerializer);
	}

	@PluginFactory
	public static CustomJsonLayout createLayout(
			@PluginConfiguration final Configuration config,
			@PluginAttribute(value = "charset", defaultString = "US-ASCII") final Charset charset) {
		return new CustomJsonLayout(config, charset, null, null);
	}

	@SneakyThrows
	@Override
	public String toSerializable(LogEvent event) {
		String loggerName = event.getLoggerName();

		if (loggerName.equals("com.dispatch.logger.utils.Utils"))
			return event.getMessage().getFormattedMessage().concat("\r\n");

		if (!loggerName.equals("com.dispatch.logger.HttpLogFilter")) {
			String log = String.format("%s -- %s : ", Utils.getLogLocalTime(), event.getLevel());

			if (event.getThrown() != null) {
				return log + Utils.getErrorStackFromException(event.getThrown()).concat("\r\n");
			}

			return log + event.getMessage().getFormattedMessage().concat("\r\n");
		}

		ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();

		jsonObject.put("level", event.getLevel().name());

		jsonObject.put("thread", event.getThreadName());

		jsonObject.put("app", true);

		HttpLog logObject = objectMapper.readValue(event.getMessage().getFormattedMessage(), HttpLog.class);

		jsonObject.put("responseTime", logObject.getResponseTime());

		RequestInfo request = logObject.getRequest();

		ResponseInfo response = logObject.getResponse();

		Map<String, String> headers = request.getHeaders();

		String message = String.format(
				"method=%s url=%s status=%s request_id=%s fwd=%s",
				request.getMethod(),
				request.getUrl(),
				response.getStatus(),
				request.getId(),
				headers.get("x-forwarded-for"));

		jsonObject.put("message", message);

		jsonObject.set("req", objectMapper.valueToTree(request));

		jsonObject.set("res", objectMapper.valueToTree(response));

		Map<String, Object> contextMap = logObject.getContext();

		if (contextMap.keySet().size() != 0)
			jsonObject.set("contextLog", objectMapper.valueToTree(logObject.getContext()));

		Map<String, String> errorMap = logObject.getError();

		if (errorMap.keySet().size() != 0) jsonObject.set("error", objectMapper.valueToTree(logObject.getError()));

		return objectMapper.writeValueAsString(jsonObject).concat("\r\n");
	}
}

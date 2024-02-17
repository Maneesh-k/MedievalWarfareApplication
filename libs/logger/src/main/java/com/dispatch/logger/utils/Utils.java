package com.dispatch.logger.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.logging.log4j.LogManager;

public class Utils {

	@SuppressWarnings("unchecked")
	public static Map<String, Object> findMethodMap(
			String className, String methodName, Map<String, Object> contextStore) {

		contextStore.putIfAbsent(className, new HashMap<String, Object>());

		Map<String, Object> classPath = (Map<String, Object>) contextStore.get(className);

		classPath.putIfAbsent(methodName, new HashMap<String, Object>());

		Map<String, Object> methodPath = (Map<String, Object>) classPath.get(methodName);

		return methodPath;
	}

	public static String convertStackTrace(StackTraceElement[] stackTraces) {
		StringJoiner joiner = new StringJoiner("\n\tat ");

		Arrays.stream(stackTraces).map(StackTraceElement::toString).forEach(joiner::add);

		return "\n\tat " + joiner.toString();
	}

	public static String getFirstStackTrace(StackTraceElement[] stackTraces) {
		return stackTraces.length >= 1 ? stackTraces[0].toString() : "";
	}

	public static void emit(String message) {
		LogManager.getLogger().info(message);
	}

	public static String getLogLocalTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}

	public static String getErrorStackFromException(Throwable exception) {
		StringWriter stringWriter = new StringWriter();

		PrintWriter printWriter = new PrintWriter(stringWriter);

		printWriter.println();

		exception.printStackTrace(printWriter);

		printWriter.close();

		return stringWriter.toString();
	}
}

package com.dispatch.logger;

import com.dispatch.logger.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ErrorLogger {

	private static ThreadLocal<Map<String, String>> threadLocal = ThreadLocal.withInitial(() -> new HashMap<>());

	private ErrorLogger() {}

	public static void add(String key, Exception e) {

		writeThreadLocal(key, e.getMessage(), Utils.getFirstStackTrace(e.getStackTrace()));
	}

	public static void add(String key, String message) {

		writeThreadLocal(key, message);
	}

	static Map<String, String> take() {
		Map<String, String> errorMap = threadLocal.get();

		threadLocal.set(new HashMap<>());

		return errorMap;
	}

	private static void writeThreadLocal(String type, String message, String stackTrace) {
		writeThreadLocal(type, message);
		Map<String, String> threadLocalMap = threadLocal.get();
		threadLocalMap.put("stackTrace", stackTrace);
	}

	private static void writeThreadLocal(String type, String message) {
		Map<String, String> threadLocalMap = threadLocal.get();
		threadLocalMap.put("type", type);
		threadLocalMap.put("message", message);
	}
}

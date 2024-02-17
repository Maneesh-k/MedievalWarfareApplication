package com.dispatch.logger;

import com.dispatch.logger.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class ContextLogger {

	private static ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(() -> new HashMap<>());

	private ContextLogger() {}

	public static void add(String key, Object value) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		String[] path = stackTrace[2].getClassName().split("[.]");

		String invokedClassName = path[path.length - 1];

		String invokedMethodName = stackTrace[2].getMethodName();

		Map<String, Object> methodMap = Utils.findMethodMap(invokedClassName, invokedMethodName, threadLocal.get());

		methodMap.put(key, value);
	}

	static Map<String, Object> take() {

		Map<String, Object> contextMap = threadLocal.get();

		threadLocal.set(new HashMap<>());

		return contextMap;
	}
}

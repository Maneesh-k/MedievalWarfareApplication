package com.dispatch.logger;

import com.dispatch.logger.http.HttpLog;

class HttpLogger {

	private static final ThreadLocal<HttpLog> threadLocal = ThreadLocal.withInitial(() -> new HttpLog());

	private HttpLogger() {}

	static void set(HttpLog data) {
		threadLocal.set(data);
	}

	static HttpLog take() {
		HttpLog log = threadLocal.get();

		threadLocal.set(new HttpLog());

		return log;
	}
}

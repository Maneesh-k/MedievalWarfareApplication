package com.dispatch.logger;

import com.dispatch.logger.job.JobLog;
import com.dispatch.logger.job.JobStatus;
import com.dispatch.logger.utils.Utils;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.quartz.JobExecutionException;

public class JobLogger {

	private static ThreadLocal<JobLog> threadLocal = ThreadLocal.withInitial(() -> new JobLog());

	private JobLogger() {}

	public static void setInitLog(JobLog log) {

		threadLocal.set(log);
	}

	@SneakyThrows
	public static void emitLog(JobExecutionException executionException) {
		JobLog log = threadLocal.get();

		threadLocal.set(new JobLog());

		Date jobExitTime = new Date();

		log.setEndTime(jobExitTime);

		log.setTimeTaken(log.getEndTime().getTime() - log.getStartTime().getTime());

		log.setStatus(JobStatus.SUCCESS.toString().toLowerCase());

		log.setContextLog(ContextLogger.take());

		log.setErrorLog(ErrorLogger.take());

		if (log.getContextLog().keySet().size() == 0) log.setContextLog(null);

		if (log.getErrorLog().keySet().size() == 0) log.setErrorLog(null);

		if (executionException != null) {

			log.setStatus(JobStatus.FAILURE.toString().toLowerCase());

			log.setErrorLog(JobLogger.errorMessageMap(executionException));
		}

		String message =
				String.format("name=%s group=%s status=%s", log.getJobName(), log.getJobGroup(), log.getStatus());

		log.setMessage(message);

		String stringifiedJson = new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.writeValueAsString(log);

		Utils.emit(stringifiedJson);
	}

	private static Map<String, String> errorMessageMap(JobExecutionException jobExecutionException) {
		Throwable underlyingException = jobExecutionException.getUnderlyingException();

		String message = underlyingException.getMessage();

		Throwable exceptionCause = underlyingException.getCause();

		if (exceptionCause == null) return Map.of("message", message);

		return Map.of("message", message + " " + exceptionCause.getMessage());
	}
}

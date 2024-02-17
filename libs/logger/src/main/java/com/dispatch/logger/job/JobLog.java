package com.dispatch.logger.job;

import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobLog {
	private String message;
	private String jobName;
	private String jobGroup;
	private String triggerName;
	private String triggerGroup;
	private Date scheduledFireTime;
	private Date fireTime;
	private Date startTime;
	private Date endTime;
	private Long timeTaken;
	private Map<String, Object> jobData;
	private String status;
	private Map<String, Object> contextLog;
	private Map<String, String> errorLog;
	private boolean isRecovering;
}

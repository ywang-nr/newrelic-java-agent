package com.nr.agent.instrumentation.logbackclassic12;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.NewRelic;

import java.util.HashMap;
import java.util.Map;

import static com.nr.agent.instrumentation.logbackclassic12.ElementName.CLASS_NAME;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.ERROR_CLASS;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.ERROR_MESSAGE;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.ERROR_STACK;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.LOGGER_NAME;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.LOG_LEVEL;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.MESSAGE;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.THROWABLE;
import static com.nr.agent.instrumentation.logbackclassic12.ElementName.TIMESTAMP;

public class AgentUtil {

    public static void recordNewRelicLogEvent(String message, long timeStamp, Level level, Logger logger, String fqcn, Throwable throwable) {
        Map<String, String> agentLinkingMetadata = NewRelic.getAgent().getLinkingMetadata();
        HashMap<String, Object> logEventMap = new HashMap<>(agentLinkingMetadata);

        logEventMap.put(MESSAGE, message);
        logEventMap.put(TIMESTAMP,timeStamp);
        logEventMap.put(LOG_LEVEL, level);
        logEventMap.put(LOGGER_NAME, logger.getName());
        logEventMap.put(CLASS_NAME, fqcn);

        if (throwable != null) {
            logEventMap.put(THROWABLE, throwable.toString());
            logEventMap.put(ERROR_CLASS, throwable.getClass().getName());
            logEventMap.put(ERROR_MESSAGE, throwable.getMessage());
            logEventMap.put(ERROR_STACK, ExceptionUtil.getErrorStack(throwable));
        }

        AgentBridge.getAgent().getLogSender().recordLogEvent(logEventMap);
    }

    public static Map<String, String> getLinkingMetadataAsMap() {
        return NewRelic.getAgent().getLinkingMetadata();
    }

    public static String getLinkingMetadataAsString() {
        return NewRelic.getAgent().getLinkingMetadata().toString();
    }

}
package io.artifactz.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitTestFeedback implements Feedback {
    private static final Logger logger = LoggerFactory.getLogger(UnitTestFeedback.class);

    @Override
    public void send(FeedbackLevel level, String message) {
        switch (level) {
            case DEBUG: logger.debug(message);
            break;
            case INFO: logger.info(message);
            break;
            case WARNING: logger.warn(message);
            break;
            default: logger.error(message);
        }
    }
}

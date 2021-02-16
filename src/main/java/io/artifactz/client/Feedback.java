package io.artifactz.client;

/**
 * The Feedback interface provide is used as a callback interface by the Client
 * to send messages while processing request to log them or to present them in the other form for the consumption
 */
public interface Feedback {
    /**
     * Sends a message for consumption for example to the log
     * @param level message level, used to decide which messages will be presented by the implementation
     * @param message a mesage to report
     *
     * @see FeedbackLevel
     */
    void send(FeedbackLevel level, String message);
}

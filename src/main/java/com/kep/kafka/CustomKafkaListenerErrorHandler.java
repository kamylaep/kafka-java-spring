package com.kep.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class CustomKafkaListenerErrorHandler implements KafkaListenerErrorHandler {

  public static final String BEAN_NAME = "customKafkaListenerErrorHandler";

  private Logger logger = LoggerFactory.getLogger(CustomKafkaListenerErrorHandler.class);

  @Override
  public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
    logger.error("Error reading message={}", message, exception);
    return null;
  }
}
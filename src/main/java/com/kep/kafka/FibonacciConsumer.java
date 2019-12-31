package com.kep.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

@Component
public class FibonacciConsumer {

  private Deque<String> fibonacciSequence = new ArrayDeque<>();
  private Logger logger = LoggerFactory.getLogger(FibonacciConsumer.class);

  /*
  @KafkaListener(topics = FibonacciProducer.TOPIC, errorHandler = CustomKafkaListenerErrorHandler.BEAN_NAME)
  public void listener(@Payload Long payload) {
    fibonacciSequence.offer(payload);
    System.out.println("***************************************************** " + payload);
  }
  */

  @KafkaListener(topics = FibonacciProducer.TOPIC, errorHandler = CustomKafkaListenerErrorHandler.BEAN_NAME)
  public void listener(ConsumerRecord<String, String> record) {
    logger.debug("********** Consuming: {} **********", record);
    Optional<String> value = Optional.ofNullable(record.value());
    fibonacciSequence.offer(value.orElse(""));
  }

  public Deque<String> getFibonacciSequence() {
    return fibonacciSequence;
  }
}

package com.kep.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class FibonacciProducer {

  public static final String TOPIC = "fibonacci";

  @Value("${com.kep.producingDelay}")
  private long producingDelay;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  private Logger logger = LoggerFactory.getLogger(FibonacciProducer.class);

  public void produce() {
    BigInteger first = BigInteger.valueOf(0);
    BigInteger second = BigInteger.valueOf(1);

    while (true) {
      try {
        logger.debug("********** Producing: {} **********", first);
        kafkaTemplate.send(TOPIC, first + "", first + "");

        BigInteger sum = first.add(second);
        first = second;
        second = sum;

        sleep();
      } catch (Exception e) {
        logger.error("Error generating next fibonacci sequence", e);
      }
    }
  }

  private void sleep() {
    try {
      Thread.sleep(producingDelay);
    } catch (InterruptedException e) {
      logger.error("Thread interrupted");
    }
  }
}

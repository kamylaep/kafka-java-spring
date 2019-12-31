package com.kep.kafka;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EmbeddedKafka(topics = {FibonacciProducer.TOPIC}, partitions = 1)
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {KafkaSpringBootApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FibonacciConsumerTest {

  private static final int N = 10;
  private static final List<String> EXPECTED_VALUES = Arrays.asList("0", "1", "1", "2", "3", "5", "8", "13", "21", "34");

  @Autowired
  private FibonacciProducer fibonacciProducer;

  @SpyBean
  private FibonacciConsumer fibonacciConsumer;

  @Test
  void consumeFibonacciSequenceWithSuccess() throws InterruptedException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<?> task = executorService.submit(fibonacciProducer::produce);
    Thread.sleep(1000);
    task.cancel(true);

    //Validate if the listener was invoked N time
    Mockito.verify(fibonacciConsumer, Mockito.atLeast(N)).listener(Mockito.any());
    Assertions.assertTrue(fibonacciConsumer.getFibonacciSequence().containsAll(EXPECTED_VALUES));
  }

}

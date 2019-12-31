package com.kep.kafka;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
public class FibonacciProducerTest {

  private static final int N = 10;
  private static final List<String> EXPECTED_VALUES = Arrays.asList("0", "1", "1", "2", "3", "5", "8", "13", "21", "34");

  @Autowired
  protected EmbeddedKafkaBroker embeddedKafka;

  @Autowired
  private FibonacciProducer fibonacciProducer;

  @Test
  public void produceFibonacciSequenceWithSuccess() throws InterruptedException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Future<?> task = executorService.submit(fibonacciProducer::produce);
    Thread.sleep(800);
    task.cancel(true);

    List<ConsumerRecord<String, String>> records = consumeKafkaEvent();

    List<String> actualValues = records.stream().map(ConsumerRecord::value).collect(Collectors.toList());
    List<String> actualKeys = records.stream().map(ConsumerRecord::key).collect(Collectors.toList());

    Assertions.assertTrue(actualValues.containsAll(EXPECTED_VALUES));
    Assertions.assertTrue(actualKeys.containsAll(EXPECTED_VALUES));
  }

  protected List<ConsumerRecord<String, String>> consumeKafkaEvent() {
    Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", this.embeddedKafka);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
    Consumer<String, String> consumer = cf.createConsumer();
    this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, FibonacciProducer.TOPIC);

    ConsumerRecords<String, String> consumerRecords = KafkaTestUtils.getRecords(consumer);
    List<ConsumerRecord<String, String>> records = new ArrayList<>();
    consumerRecords.forEach(records::add);
    return records;
  }
}

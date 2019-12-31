package com.kep.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
public class FibonacciController {

  @Autowired
  private FibonacciProducer producer;
  @Autowired
  private FibonacciConsumer consumer;

  @PostConstruct
  public void init() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(producer::produce);
  }

  @GetMapping("/fibonacci")
  public String fibonacci(Model model) {
    model.addAttribute("fibonacciSequence", consumer.getFibonacciSequence());
    return "fibonacci";
  }
}

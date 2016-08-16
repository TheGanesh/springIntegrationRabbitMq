package com.gwiazda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.http.Http;

import java.util.Scanner;

/**
 * Created by michal.gwiazda on 15.08.16.
 */

@SpringBootApplication
@IntegrationComponentScan
public class StartClient {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(StartClient.class, args);
        Gateway gate = context.getBean(Gateway.class);
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            System.out.println(gate.exchange(line));
        }
        scanner.close();
    }

    @MessagingGateway(defaultRequestChannel = "httpOut.input")
    public interface Gateway {
        public String exchange(String out);
    }

    /**
     *
     * use the one below to route message via http /receiveGateway
     * @return
     */

    @Bean
    IntegrationFlow httpOut(){
      return f -> f.handle(Http.outboundGateway("http://localhost:8080/receiveGateway")
      .expectedResponseType(String.class));
    }

    /**
     *
     * use this one to connect using rabbit on standard 127.0.0.1 port 5672
     */

//    @Bean
//    IntegrationFlow httpOut(RabbitTemplate rabbitTemplate){
//        return f -> f.handle(Amqp.outboundGateway(rabbitTemplate).routingKey("mojKlucz").exchangeName("mojaNowa"));
//    }

//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory());
//        template.setRoutingKey(this.helloWorldQueueName);
//        return template;
//    }
}

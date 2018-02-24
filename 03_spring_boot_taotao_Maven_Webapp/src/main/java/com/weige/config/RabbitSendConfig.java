package com.weige.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitSendConfig {
	
	 @Bean(name="frontQueue")
     public Queue getQueueA() {
         return new Queue("frontQueue", true, false, true);
     }
	 
	 @Bean(name="backQueue")
     public Queue getQueueB() {
		 return new Queue("backQueue", true, false, true);
     }



	 @Bean
     public TopicExchange exchange() {
         return new TopicExchange("taotao_exchange");
     }

     @Bean
     Binding bindingExchangeMessageA(@Qualifier("frontQueue") Queue frontQueue,TopicExchange exchange) {
    	 return BindingBuilder.bind(frontQueue).to(exchange).with("item.update");
     }
     
     @Bean
     Binding bindingExchangeMessageC(@Qualifier("frontQueue") Queue frontQueue,TopicExchange exchange) {
    	 return BindingBuilder.bind(frontQueue).to(exchange).with("item.delete");
     }
     
     @Bean
     Binding bindingExchangeMessageB(@Qualifier("backQueue") Queue backQueue,TopicExchange exchange) {
         return BindingBuilder.bind(backQueue).to(exchange).with("item.*");
     }
	
}

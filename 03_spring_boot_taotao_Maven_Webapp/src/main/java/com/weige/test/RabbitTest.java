package com.weige.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weige.App;
import com.weige.pojo.message;


@RunWith(SpringRunner.class)
@SpringBootTest(classes={App.class})

public class RabbitTest {
	@Autowired 
	private RabbitTemplate rabbitTemplate;
	
	@Test
	public void testRabbit(){
		message message = new message();
		message.setName("lxy");
		rabbitTemplate.convertAndSend("exchange","topic.a",message);
	}
}

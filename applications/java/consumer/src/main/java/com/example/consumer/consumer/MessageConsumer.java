package com.example.consumer.consumer;

import java.util.logging.Logger;

import com.example.consumer.constant.RabbitMQConstant;
import com.example.consumer.model.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    
    private static final Logger logger = Logger.getLogger(MessageConsumer.class.getName());

    @RabbitListener(queues = RabbitMQConstant.QUEUE_MESSAGE)
    private void consumer(Message message) {
        logger.info(message.getMessage());
    }

}

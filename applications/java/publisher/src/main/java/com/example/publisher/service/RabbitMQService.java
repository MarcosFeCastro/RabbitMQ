package com.example.publisher.service;

import java.util.logging.Logger;

import com.example.publisher.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    
    private static final Logger logger = Logger.getLogger(RabbitMQService.class.getName());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(String queue, Message message) {
        try {
            String msgJson = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(queue, msgJson);
            logger.info(message.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}

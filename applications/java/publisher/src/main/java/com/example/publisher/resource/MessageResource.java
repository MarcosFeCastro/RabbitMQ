package com.example.publisher.resource;

import com.example.publisher.model.Message;
import com.example.publisher.service.RabbitMQService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageResource {

    @Autowired
    private RabbitMQService rabbitMQService;

    @GetMapping(value = "/message/{msg}")
    public ResponseEntity<Void> sendMessage(@PathVariable String msg) {
        Message message = new Message(msg);
        rabbitMQService.sendMessage("MESSAGE", message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

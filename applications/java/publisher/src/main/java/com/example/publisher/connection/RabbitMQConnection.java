package com.example.publisher.connection;

import javax.annotation.PostConstruct;

import com.example.publisher.constant.RabbitMQConstant;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConnection {
 
    private AmqpAdmin amqpAdmin;

    public RabbitMQConnection(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    private Queue queue(String queue) {
        return new Queue(queue, true, false,false);
    }

    private DirectExchange directExchange() {
        return new DirectExchange(RabbitMQConstant.EXCHANGE_NAME);
    }

    private Binding binding(Queue queue, DirectExchange exchange) {
        return new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchange.getName(), queue.getName(), null);
    }

    @PostConstruct
    private void run() {
        Queue orderQueue = this.queue(RabbitMQConstant.QUEUE_MESSAGE);
        DirectExchange directExchange = this.directExchange();
        Binding orderBinding = this.binding(orderQueue, directExchange);

        amqpAdmin.declareQueue(orderQueue);
        amqpAdmin.declareExchange(directExchange);
        amqpAdmin.declareBinding(orderBinding);
    }
    
}
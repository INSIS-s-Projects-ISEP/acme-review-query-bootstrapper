package com.isep.bootstrapper.messaging;

import java.io.IOException;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.isep.bootstrapper.dto.mapper.ProductMapper;
import com.isep.bootstrapper.dto.message.ProductMessage;
import com.isep.bootstrapper.model.Product;
import com.isep.bootstrapper.repository.ProductRepository;
import com.rabbitmq.client.Channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ProductConsumer {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    
    @RabbitListener(queues = "#{rpcProductQueue.name}", ackMode = "MANUAL")
    public String rpcProducts(String instanceId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException{

        log.info("RPC Product Request received: " + instanceId);
        try {
            List<Product> products = productRepository.findAll();
            List<ProductMessage> messages = productMapper.toMessageList(products);
            String response = productMapper.toJson(messages);

            log.info("RPC Product Request sent to: " + instanceId);
            channel.basicAck(tag, false);
            return response;
        }
        catch (Exception e) {
            log.error("Error to send RPC Product Request to: " + instanceId);
            channel.basicReject(tag, true);
            return "";
        }
    }

}

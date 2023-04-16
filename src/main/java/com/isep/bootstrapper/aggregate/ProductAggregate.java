package com.isep.bootstrapper.aggregate;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.isep.bootstrapper.event.ProductCreatedEvent;
import com.isep.bootstrapper.event.ProductDeletedEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAggregate {

    @AggregateIdentifier
    private UUID productId;
    private String sku;

    @CommandHandler
    public ProductAggregate(ProductCreatedEvent productCreatedEvent){
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        this.sku = productCreatedEvent.getSku();
        this.productId = productCreatedEvent.getProductId();
    }

    @CommandHandler
    public void handle(ProductDeletedEvent productDeletedEvent){
        AggregateLifecycle.apply(productDeletedEvent);
    }

    @EventSourcingHandler
    public void on(ProductDeletedEvent productDeletedEvent){
        AggregateLifecycle.markDeleted();
    }

    // @EventSourcingHandler
    // public void on(ProductDeletedEvent productDeletedEvent){
    // }

}

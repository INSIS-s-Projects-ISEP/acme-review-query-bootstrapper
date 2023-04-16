package com.isep.bootstrapper.projection;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.isep.bootstrapper.event.ReviewCreatedEvent;
import com.isep.bootstrapper.event.ReviewDeletedEvent;
import com.isep.bootstrapper.event.ReviewUpdatedEvent;
import com.isep.bootstrapper.model.Product;
import com.isep.bootstrapper.model.Review;
import com.isep.bootstrapper.repository.ProductRepository;
import com.isep.bootstrapper.repository.ReviewRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReviewProjection {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    
    @EventHandler
    public void on(ReviewCreatedEvent event){
        Product product = productRepository.findBySku(event.getSku()).orElseThrow();
        reviewRepository.save(new Review(
            event.getReviewId(),
            event.getApprovalStatus(),
            event.getReviewText(),
            event.getReport(),
            event.getPublishingDate(),
            event.getFunFact(),
            product,
            event.getUser(),
            event.getRate()
        ));
    }
    
    @EventHandler
    public void on(ReviewUpdatedEvent event){

        Product product = productRepository.findBySku(event.getSku()).orElseThrow();
        Review review = reviewRepository.findById(event.getReviewId()).orElseThrow();

        review.setReviewId(event.getReviewId());
        review.setApprovalStatus(event.getApprovalStatus());
        review.setReviewText(event.getReviewText());
        review.setPublishingDate(event.getPublishingDate());
        review.setFunFact(event.getFunFact());
        review.setProduct(product);
        review.setUserr(event.getUser());
        review.setRate(event.getRate());
        
        reviewRepository.save(review);

    }
    
    @EventHandler
    public void on(ReviewDeletedEvent event){
        reviewRepository.deleteById(event.getReviewId());
    }
    
}

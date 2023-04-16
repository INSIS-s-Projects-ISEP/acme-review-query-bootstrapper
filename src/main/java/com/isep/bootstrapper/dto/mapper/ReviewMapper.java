package com.isep.bootstrapper.dto.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.bootstrapper.dto.message.ReviewMessage;
import com.isep.bootstrapper.model.Product;
import com.isep.bootstrapper.model.Review;
import com.isep.bootstrapper.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReviewMapper {
    
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public Review toEntity(ReviewMessage reviewMessage){

        Product product = productRepository.findBySku(reviewMessage.getSku()).orElseThrow();
        return new Review(
            reviewMessage.getReviewId(),
            reviewMessage.getApprovalStatus(),
            reviewMessage.getReviewText(),
            reviewMessage.getReport(),
            reviewMessage.getPublishingDate(),
            reviewMessage.getFunFact(),
            product,
            reviewMessage.getUser(),
            reviewMessage.getRate()
        );
    }

    public ReviewMessage toMessage(Review review){
        return new ReviewMessage(
            review.getReviewId(),
            review.getApprovalStatus(),
            review.getReviewText(),
            review.getReport(),
            review.getPublishingDate(),
            review.getFunFact(),
            review.getProduct().getSku(),
            review.getUserr(),
            review.getRate()
        );
    }

    public List<ReviewMessage> toMessageList(List<Review> reviews){
        return (reviews.stream()
            .map(this::toMessage)
            .collect(Collectors.toList())
        );
    }

    public String toJson(List<ReviewMessage> messages) throws JsonProcessingException{
        Map<String, List<ReviewMessage>> dataMap = new HashMap<>();
        dataMap.put("response", messages);
        return objectMapper.writeValueAsString(dataMap);
    }
}

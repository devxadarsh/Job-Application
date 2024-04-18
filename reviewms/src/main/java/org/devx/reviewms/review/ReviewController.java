package org.devx.reviewms.review;

import org.devx.reviewms.review.dto.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;

    //using for connect to rabbitMQ
    private final ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    @GetMapping
    public ResponseEntity<List<Review>> findAllByCompanyId(@RequestParam Long companyId) {
        return new ResponseEntity<>(reviewService.getAllReviews(companyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addReview(@RequestParam Long companyId,@RequestBody Review review) {
        boolean isReviewSaved = reviewService.addReview(companyId, review);
        if(isReviewSaved){
            reviewMessageProducer.sendMessage(review); //to share massage to update the average review for company
            return new ResponseEntity<>("Review added Successfully", HttpStatus.CREATED);
        }
        else return new ResponseEntity<>("Review not added Successfully", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        return new ResponseEntity<>(reviewService.getReview(reviewId), HttpStatus.OK);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
        boolean isReviewSaved = reviewService.updateReview(reviewId, review);
        if(isReviewSaved) return new ResponseEntity<>("Review updated Successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Review not updated Successfully", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        boolean isDeleted = reviewService.deleteReview(reviewId);
        if(isDeleted) return new ResponseEntity<>("Review deleted Successfully", HttpStatus.OK);
        else return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/averageReview")
    public Double getAverageReview(@RequestParam Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        return reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }
}

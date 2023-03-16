package com.github.prgrms.reviews;

import static com.github.prgrms.utils.ApiUtils.success;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.security.JwtAuthentication;
import com.github.prgrms.utils.ApiUtils.ApiResult;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class ReviewRestController {
    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{id}/review")
    public ApiResult<ReviewDto> review(@AuthenticationPrincipal JwtAuthentication authentication, @PathVariable Long id,
                                       @RequestBody ReviewRequest request) {
        Long seq = reviewService.newReview(authentication.id, id, request.getContent());
        Optional<Review> result = reviewService.findById(seq);

        return success(result.map(ReviewDto::new)
                .orElseThrow(() -> new NotFoundException("Could not found review for " + seq)));
    }
}

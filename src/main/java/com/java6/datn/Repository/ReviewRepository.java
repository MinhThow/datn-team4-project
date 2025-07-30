package com.java6.datn.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.java6.datn.Entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	List<Review> findByProductProductID(Integer productID);

	List<Review> findByUserUserID(Integer userID);

	List<Review> findByProductProductIDOrderByReviewDateDesc(Integer productId);

	Long countByProductProductID(Integer productId);

	@Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.productID = :productId AND r.rating > 0")
	Double findAverageRatingByProductId(@Param("productId") Integer productId);

	@Query("SELECT r FROM Review r WHERE r.product.productID = :productId ORDER BY r.reviewDate DESC")
	List<Review> findTopReviewsByProductId(@Param("productId") Integer productId);
	Optional<Review> findByUserUserIDAndProductProductID(Integer userId, Integer productId);

}

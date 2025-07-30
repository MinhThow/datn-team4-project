package com.java6.datn.Controller;

import com.java6.datn.DTO.OrderDisplayDTO;
import com.java6.datn.DTO.CartItemDTO;

import com.java6.datn.DTO.OrderDisplayDTO;
import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.DTO.ReviewDTO;
import com.java6.datn.DTO.UserDTO;

import com.java6.datn.Entity.Order;
import com.java6.datn.Entity.OrderItem;
import com.java6.datn.Entity.ProductImage;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.UserMapper;

import com.java6.datn.Repository.OrderRepository;
import com.java6.datn.Repository.ProductImageRepository;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.ReviewService;
import com.java6.datn.Service.UserService;
import com.java6.datn.Service.VerificationTokenService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {

	private final UserService userService;
	private final UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductImageRepository productImageRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private VerificationTokenService tokenService;
	@Autowired
	private ReviewService reviewService;

	public AccountController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;

	}

	// Hiển thị form tài khoản
	@GetMapping
	public String showAccountPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// ✅ Kiểm tra null hoặc anonymous user
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/login";
		}

		String email = authentication.getName();
		User userEntity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		List<ReviewDTO> userReviews = new ArrayList<>();
		model.addAttribute("userVerified", userEntity.isEmailVerified());
		UserDTO userDTO = UserMapper.toDTO(userEntity);
		model.addAttribute("user", userDTO);

		long passwordAge = 0;
		if (userEntity.getPasswordChangedAt() != null) {
			passwordAge = ChronoUnit.DAYS.between(userEntity.getPasswordChangedAt().toLocalDate(), LocalDate.now());
		}
		model.addAttribute("passwordAge", passwordAge);

		List<Order> orders = orderRepository.findByUserUserID(userEntity.getUserID());

		List<OrderDisplayDTO> allOrders = new ArrayList<>();
		List<OrderDisplayDTO> deliveringOrders = new ArrayList<>();
		List<OrderDisplayDTO> completedOrders = new ArrayList<>();
		List<OrderDisplayDTO> canceledOrders = new ArrayList<>();

		for (Order order : orders) {
			for (OrderItem item : order.getOrderItems()) {
				ProductImage img = productImageRepository.findFirstByProductAndIsMain(item.getProduct(), true);
				// ✅ Map màu dựa theo order.getStatus()
				String color = switch (order.getStatus()) {
				case "Đã giao" -> "bg-success";
				case "Đang giao" -> "bg-primary";
				case "Đã hủy" -> "bg-secondary";
				default -> "bg-secondary";
				};

				OrderDisplayDTO dto = OrderDisplayDTO.builder().orderId(order.getOrderId())
						.productName(item.getProductName()).imageUrl(img != null ? img.getImageUrl() : "")
						.size(item.getSize()).quantity(item.getQuantity()).price(item.getPrice())
						.status(order.getStatus()).shippingAddress(order.getShippingAddress()).note(order.getNote())
						.paymentMethodName(order.getPaymentMethodName())
						.formattedDate(order.getOrderDate().toLocalDate().toString()).badgeColor(color) // 👈 gắn thêm
																										// dòng này
						.productId(item.getProduct().getProductID()) // 🟢 Thêm dòng này
						.rating(reviewService.getUserReviewRating(userEntity.getUserID(), item.getProduct().getProductID()).orElse(null))
						.comment(reviewService.getUserReviewComment(userEntity.getUserID(), item.getProduct().getProductID()).orElse(null))
						.reviewed(reviewService.hasUserReviewedProduct(userEntity.getUserID(), item.getProduct().getProductID()))


						.totalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).build();

				allOrders.add(dto);

				switch (order.getStatus()) {
				case "Đang giao" -> deliveringOrders.add(dto);
				case "Đã giao" -> completedOrders.add(dto);
				case "Đã hủy" -> canceledOrders.add(dto);
				}
				// ✅ Gán đánh giá nếu có
				List<ReviewDTO> productReviews = reviewService.getReviewsByProductId(item.getProduct().getProductID());
				productReviews.stream().filter(r -> r.getUserId().equals(userEntity.getUserID())).findFirst()
						.ifPresent(userReviews::add);
			}
		}
		model.addAttribute("allOrders", allOrders);
		model.addAttribute("deliveringOrders", deliveringOrders);
		model.addAttribute("completedOrders", completedOrders);
		model.addAttribute("canceledOrders", canceledOrders);
		model.addAttribute("userReviews", userReviews);
		return "account"; // account.html

	}

	// Xử lý cập nhật thông tin
	@PostMapping
	public String updateAccount(@ModelAttribute("user") UserDTO updatedUser, RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// ✅ Kiểm tra phòng thủ
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/login";
		}

		String email = authentication.getName();
		User userEntity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		updatedUser.setUserId(userEntity.getUserID()); // giữ nguyên ID gốc
		userService.updateUser(updatedUser.getUserId(), updatedUser);

		redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		return "redirect:/account";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
			RedirectAttributes redirectAttributes) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/login";
		}

		String email = authentication.getName();
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		if (!userService.checkPassword(user, currentPassword)) {
			redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng!");
			return "redirect:/account";
		}

		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp!");
			return "redirect:/account";
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		user.setPasswordChangedAt(LocalDateTime.now());
		userRepository.save(user);
		redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
		return "redirect:/account";
	}

	
	@PostMapping("/review") // 👈 KHÔNG dùng "/account/review" vì đã có @RequestMapping("/account")
	public String submitReview(@ModelAttribute ReviewDTO reviewDTO,
	                           RedirectAttributes redirectAttributes) {
		System.out.println("==> POST /reviews/add đã được gọi");


	    try {
	        reviewService.createReview(reviewDTO);
	        redirectAttributes.addFlashAttribute("successMessage", "Đánh giá đã được gửi thành công!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi gửi đánh giá: " + e.getMessage());
	    }

	    return "redirect:/account";
	}



}

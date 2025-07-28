package com.java6.datn.Controller;

import com.java6.datn.DTO.OrderDisplayDTO;
import com.java6.datn.DTO.CartItemDTO;
import com.java6.datn.DTO.LoginHistoryDTO;
import com.java6.datn.DTO.OrderDisplayDTO;
import com.java6.datn.DTO.ProductDTO;
import com.java6.datn.DTO.UserDTO;
import com.java6.datn.Entity.BankAccount;
import com.java6.datn.Entity.LoginHistory;
import com.java6.datn.Entity.Order;
import com.java6.datn.Entity.OrderItem;
import com.java6.datn.Entity.ProductImage;
import com.java6.datn.Entity.User;
import com.java6.datn.Mapper.UserMapper;
import com.java6.datn.Repository.LoginHistoryRepository;
import com.java6.datn.Repository.OrderRepository;
import com.java6.datn.Repository.ProductImageRepository;
import com.java6.datn.Repository.UserRepository;
import com.java6.datn.Service.BankAccountService;
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
	private final BankAccountService bankAccountService;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductImageRepository productImageRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private final LoginHistoryRepository loginHistoryRepository;

	public AccountController(UserService userService, UserRepository userRepository,
			BankAccountService bankAccountService, LoginHistoryRepository loginHistoryRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.bankAccountService = bankAccountService;
		this.loginHistoryRepository = loginHistoryRepository;
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
		model.addAttribute("userVerified", userEntity.isEmailVerified());
		UserDTO userDTO = UserMapper.toDTO(userEntity);
		model.addAttribute("user", userDTO);
		model.addAttribute("bankAccounts", bankAccountService.findByUser(userEntity));
		model.addAttribute("newBankAccount", new BankAccount());
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
						.totalPrice(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).build();

				allOrders.add(dto);

				switch (order.getStatus()) {
				case "Đang giao" -> deliveringOrders.add(dto);
				case "Đã giao" -> completedOrders.add(dto);
				case "Đã hủy" -> canceledOrders.add(dto);
				}
			}
		}
		model.addAttribute("allOrders", allOrders);
		model.addAttribute("deliveringOrders", deliveringOrders);
		model.addAttribute("completedOrders", completedOrders);
		model.addAttribute("canceledOrders", canceledOrders);
		
		 // Lấy login history từ DB ✅
	    List<LoginHistory> loginHistoryList = loginHistoryRepository
	            .findTop10ByUserOrderByLoginTimeDesc(userEntity);

	    // Format loginTime thành chuỗi
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    List<LoginHistoryDTO> loginHistoryDTOs = loginHistoryList.stream()
	            .map(history -> new LoginHistoryDTO(
	                    formatter.format(history.getLoginTime()),
	                    history.getIpAddress(),
	                    history.getUserAgent()))
	            .toList();

	    model.addAttribute("loginHistory", loginHistoryDTOs);

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

		updatedUser.setUserID(userEntity.getUserID()); // giữ nguyên ID gốc
		userService.updateUser(updatedUser.getUserID(), updatedUser);

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

	@Autowired
	private VerificationTokenService tokenService;

	@PostMapping("/delete")
	public ResponseEntity<?> deleteAccount(Principal principal) {
		if (principal == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String email = principal.getName();
		Optional<User> userOpt = userRepository.findByEmail(email);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			// ✅ Xoá token trước
			tokenService.deleteByUser(user);

			userRepository.delete(user);
			return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/logout").build();
		}

		return ResponseEntity.notFound().build();
	}

}

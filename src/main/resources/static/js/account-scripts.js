
	document.querySelectorAll('.toggle-password').forEach(button => {
		button.addEventListener('click', () => {
			const targetId = button.getAttribute('data-target');
			const input = document.getElementById(targetId);
			const icon = button.querySelector('i');

			if (input.type === 'password') {
				input.type = 'text';
				icon.classList.remove('bi-eye');
				icon.classList.add('bi-eye-slash');
			} else {
				input.type = 'password';
				icon.classList.remove('bi-eye-slash');
				icon.classList.add('bi-eye');
			}
		});
	});
	const passwordForm = document.getElementById('passwordForm');
	passwordForm.addEventListener('submit', function (event) {
		const newPassword = document.getElementById('newPassword').value;
		const confirmPassword = document.getElementById('confirmPassword').value;
		const errorDiv = document.getElementById('passwordError');

		if (newPassword !== confirmPassword) {
			event.preventDefault(); // Ngăn form submit
			errorDiv.style.display = 'block';
		} else {
			errorDiv.style.display = 'none'; // Ẩn lỗi nếu đúng
		}
	});

	document.getElementById("bankForm").addEventListener("submit", function (e) {
		e.preventDefault();

		const bankName = document.getElementById("bankName").value;
		const accountNumber = document.getElementById("accountNumber").value;
		const accountHolder = document.getElementById("accountHolder").value;
		const branch = document.getElementById("branch").value;
		const cardType = document.getElementById("cardType").value;
		const cvv = document.getElementById("cvv").value;
		const expiryDate = document.getElementById("expiryDate").value;

		const badgeColor = cardType === 'Credit' ? 'warning' : 'info';

		const cardHTML = `
        <div class="card shadow-sm w-75 mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h6 class="card-title mb-2">${bankName}</h6>
                        <p class="mb-1"><strong>Account Number:</strong> ${accountNumber}</p>
                        <p class="mb-1"><strong>Account Holder:</strong> ${accountHolder}</p>
                        <p class="mb-1"><strong>Branch:</strong> ${branch}</p>
                        <p class="mb-1"><strong>Card Type:</strong> 
                            <span class="badge bg-${badgeColor} text-dark">${cardType}</span>
                        </p>
                    </div>
                    <div class="d-flex flex-column justify-content-center align-items-end">
                        <button class="btn btn-sm btn-outline-secondary mb-2">Edit</button>
                        <button class="btn btn-sm btn-outline-danger">Delete</button>
                    </div>
                </div>
            </div>
        </div>
    `;

		// ✅ Đảm bảo ID đúng với phần chứa card
		const container = document.getElementById("bankCardContainer");
		container.insertAdjacentHTML("beforeend", cardHTML);

		this.reset();
		const modal = bootstrap.Modal.getInstance(document.getElementById("addBankModal"));
		modal.hide();
	});


	// Hiển thị điều khoản
	document.getElementById("deleteBtn").addEventListener("click", function () {
		document.getElementById("deleteAccountSection").classList.add("d-none");
		document.getElementById("confirmSection").classList.remove("d-none");
	});

	// Mở modal xác nhận cuối cùng
	document.getElementById("proceedBtn").addEventListener("click", function () {
		const modal = new bootstrap.Modal(document.getElementById("confirmDeleteModal"));
		modal.show();
	});

	// Xử lý khi bấm "Yes, Delete"
	document.getElementById("finalDeleteBtn").addEventListener("click", function () {
		// TODO: Gửi request xóa tài khoản ở đây
		alert("Account has been deleted!"); // Tạm thời để thông báo giả lập
		const modal = bootstrap.Modal.getInstance(document.getElementById("confirmDeleteModal"));
		modal.hide();
	});

	document.querySelectorAll(".main-toggle").forEach(toggle => {
		toggle.addEventListener("change", function () {
			const targetId = this.getAttribute("data-target");
			const target = document.querySelector(targetId);
			const bsCollapse = bootstrap.Collapse.getOrCreateInstance(target);

			if (this.checked) {
				bsCollapse.show();
			} else {
				bsCollapse.hide();
			}
		});
	});

	

	document.getElementById('confirmVerifyEmailBtn').addEventListener('click', function () {
	    fetch('/verify-email', { method: 'POST' })
	        .then(res => {
	            if (res.ok) {
	                alert("Email xác minh đã được gửi!");
	                const modal = bootstrap.Modal.getInstance(document.getElementById("verifyEmailModal"));
	                modal.hide();
	            } else {
	                alert("Gửi thất bại");
	            }
	        });
	});


	const loginHistoryData = [
		{time: "25/07/2025 14:02", details: "IP: 192.168.1.10 · Chrome · Windows"},
		{time: "24/07/2025 21:47", details: "IP: 192.168.1.15 · Firefox · Ubuntu"},
		{time: "23/07/2025 10:12", details: "IP: 192.168.1.20 · Safari · macOS"},
		{time: "22/07/2025 08:55", details: "IP: 192.168.1.25 · Edge · Windows"},
		{time: "21/07/2025 19:30", details: "IP: 192.168.1.30 · Opera · Linux"},
		{time: "20/07/2025 17:10", details: "IP: 192.168.1.35 · Chrome · Windows"},
		{time: "19/07/2025 12:05", details: "IP: 192.168.1.40 · Firefox · macOS"}
	];

	let currentPage = 1;
	const itemsPerPage = 5;
	let viewAllMode = false;

	function renderLoginHistory() {
		const list = document.getElementById("login-history-list");
		const pagination = document.getElementById("login-pagination");
		const toggleBtn = document.getElementById("toggle-login-history");

		list.innerHTML = "";

		if (!viewAllMode) {
			loginHistoryData.slice(0, 3).forEach(item => {
				list.innerHTML += `<li class="list-group-item"><div class="fw-bold">${item.time}</div><div class="text-muted">${item.details}</div></li>`;
			});
			toggleBtn.textContent = "View All";
			pagination.classList.add("d-none");
		} else {
			const start = (currentPage - 1) * itemsPerPage;
			const paginatedItems = loginHistoryData.slice(start, start + itemsPerPage);
			paginatedItems.forEach(item => {
				list.innerHTML += `<li class="list-group-item"><div class="fw-bold">${item.time}</div><div class="text-muted">${item.details}</div></li>`;
			});

			toggleBtn.textContent = "View Less";

			// Render pagination if needed
			const totalPages = Math.ceil(loginHistoryData.length / itemsPerPage);
			pagination.innerHTML = "";
			if (totalPages > 1) {
				for (let i = 1; i <= totalPages; i++) {
					pagination.innerHTML += `
          <li class="page-item ${i === currentPage ? 'active' : ''}">
            <a class="page-link" href="#" onclick="goToPage(${i})">${i}</a>
          </li>
        `;
				}
				pagination.classList.remove("d-none");
			} else {
				pagination.classList.add("d-none");
			}
		}
	}

	function toggleLoginHistory() {
		viewAllMode = !viewAllMode;
		currentPage = 1;
		renderLoginHistory();
	}

	function goToPage(page) {
		currentPage = page;
		renderLoginHistory();
	}

	document.addEventListener("DOMContentLoaded", () => {
		renderLoginHistory();
	});

	function showDeleteModal(id) {
		const form = document.getElementById("deleteForm");
		form.action = "/api/bank-accounts/delete/" + id;

		const modal = new bootstrap.Modal(document.getElementById('confirmDeleteModal'));
		modal.show();
	}

	document.getElementById("finalDeleteBtn").addEventListener("click", function () {
		fetch("/account/delete", {
			method: "POST"
		})
			.then(response => {
				if (response.redirected) {
					window.location.href = response.url; // chuyển hướng nếu xoá thành công
				} else {
					alert("Không thể xoá tài khoản!");
				}
			})
			.catch(error => {
				console.error("Lỗi khi xoá:", error);
				alert("Đã có lỗi xảy ra!");
			});
	});

  function submitReview() {
    alert("Đánh giá đã được gửi!");
    const modal = bootstrap.Modal.getInstance(document.getElementById('reviewModal'));
    modal.hide();
  }


/**
 * Product Detail Page JavaScript
 * 
 * Handles all interactive features on the product detail page including:
 * - Add to cart functionality with AJAX
 * - Quantity selector controls
 * - Size selection validation
 * - Cart counter updates
 * - User feedback and notifications
 */

$(document).ready(function() {
    'use strict';

    // Initialize product detail functionality
    initializeProductDetail();
    
    /**
     * Initialize all product detail page functionality
     */
    function initializeProductDetail() {
        setupQuantityControls();
        setupSizeSelection();
        setupAddToCartForm();
        setupNotifications();
        setupProductGallery();
        setupColorSelection();
        setupWishlist();
        setupTooltips();
        setupSmoothScroll();
    }
    
    /**
     * Setup quantity increment/decrement controls
     */
    function setupQuantityControls() {
        // Quantity increment button
        $('.quantity-increment').on('click', function() {
            const quantityInput = $(this).siblings('input[name="quantity"]');
            const currentValue = parseInt(quantityInput.val()) || 1;
            const maxStock = parseInt(quantityInput.attr('max')) || 999;
            
            if (currentValue < maxStock) {
                quantityInput.val(currentValue + 1);
                updatePriceDisplay();
            } else {
                showNotification('Maximum stock reached: ' + maxStock, 'warning');
            }
        });
        
        // Quantity decrement button
        $('.quantity-decrement').on('click', function() {
            const quantityInput = $(this).siblings('input[name="quantity"]');
            const currentValue = parseInt(quantityInput.val()) || 1;
            
            if (currentValue > 1) {
                quantityInput.val(currentValue - 1);
                updatePriceDisplay();
            }
        });
        
        // Quantity input validation
        $('input[name="quantity"]').on('change', function() {
            const value = parseInt($(this).val()) || 1;
            const maxStock = parseInt($(this).attr('max')) || 999;
            
            if (value < 1) {
                $(this).val(1);
            } else if (value > maxStock) {
                $(this).val(maxStock);
                showNotification('Maximum stock: ' + maxStock, 'warning');
            }
            
            updatePriceDisplay();
        });
    }
    
    /**
     * Setup size selection functionality
     */
    function setupSizeSelection() {
        $('input[name="productSize"]').on('change', function() {
            // Remove error styling from size selection
            $('.size-selection-error').removeClass('size-selection-error');
            
            // Remove active class from all size labels
            $('.product__details__option__size label').removeClass('active');
            
            // Add active class to selected size label
            $(this).parent().addClass('active');
        });
        
        // Handle click on size labels (for better UX)
        $('.product__details__option__size label').on('click', function() {
            // Remove active class from all size labels
            $('.product__details__option__size label').removeClass('active');
            
            // Add active class to clicked label
            $(this).addClass('active');
            
            // Check the radio button
            $(this).find('input[type="radio"]').prop('checked', true);
            
            // Remove error styling
            $('.size-selection-error').removeClass('size-selection-error');
        });
        
        // Initialize first size as active if none is selected
        if (!$('.product__details__option__size label.active').length) {
            $('.product__details__option__size label:first').addClass('active');
            $('.product__details__option__size label:first input[type="radio"]').prop('checked', true);
        }
    }
    
    /**
     * Setup add to cart form submission
     */
    function setupAddToCartForm() {
        $('.add-to-cart-form').on('submit', function(e) {
            e.preventDefault();
            
            // Validate form before submission
            if (!validateAddToCartForm()) {
                return false;
            }
            
            // Get form data
            const formData = {
                productId: $('input[name="productId"]').val(),
                quantity: $('input[name="quantity"]').val(),
                size: $('input[name="productSize"]:checked').val()
            };
            
            // Show loading state
            showLoadingState(true);
            
            // Submit AJAX request
            $.ajax({
                url: '/cart/add',
                method: 'POST',
                data: formData,
                success: function(response) {
                    handleAddToCartSuccess(response);
                },
                error: function(xhr, status, error) {
                    handleAddToCartError(xhr);
                },
                complete: function() {
                    showLoadingState(false);
                }
            });
        });
    }
    
    /**
     * Validate add to cart form
     */
    function validateAddToCartForm() {
        let isValid = true;
        
        // Check if size is selected
        const selectedSize = $('input[name="productSize"]:checked').val();
        if (!selectedSize) {
            $('.product__details__option__size').addClass('size-selection-error');
            showNotification('Please select a size', 'error');
            isValid = false;
        }
        
        // Check quantity
        const quantity = parseInt($('input[name="quantity"]').val()) || 0;
        if (quantity < 1) {
            $('input[name="quantity"]').addClass('error');
            showNotification('Please enter a valid quantity', 'error');
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Handle successful add to cart response
     */
    function handleAddToCartSuccess(response) {
        if (response.success) {
            // Update cart counter in header
            updateCartCounter(response.cartItemCount);
            
            // Show success message
            showNotification(
                `${response.productName} (${response.selectedSize}) added to cart successfully!`,
                'success'
            );
            
            // Optional: Show cart total
            if (response.cartTotal) {
                updateCartTotal(response.cartTotal);
            }
            
            // Add visual feedback
            animateAddToCartSuccess();
            
        } else {
            showNotification(response.message || 'Failed to add product to cart', 'error');
        }
    }
    
    /**
     * Handle add to cart error response
     */
    function handleAddToCartError(xhr) {
        let errorMessage = 'Failed to add product to cart';
        
        if (xhr.responseJSON && xhr.responseJSON.message) {
            errorMessage = xhr.responseJSON.message;
        } else if (xhr.status === 400) {
            errorMessage = 'Invalid request. Please check your selection.';
        } else if (xhr.status === 500) {
            errorMessage = 'Server error. Please try again later.';
        }
        
        showNotification(errorMessage, 'error');
    }
    
    /**
     * Show/hide loading state for add to cart button
     */
    function showLoadingState(isLoading) {
        const addToCartBtn = $('.add-to-cart-btn');
        
        if (isLoading) {
            addToCartBtn.prop('disabled', true);
            addToCartBtn.html('<i class="fa fa-spinner fa-spin"></i> Adding to Cart...');
        } else {
            addToCartBtn.prop('disabled', false);
            addToCartBtn.html('<i class="fa fa-shopping-cart"></i> ADD TO CART');
        }
    }
    
    /**
     * Update cart counter in header
     */
    function updateCartCounter(count) {
        $('.cart-counter').text(count);
        
        // Add animation effect
        $('.cart-counter').addClass('updated');
        setTimeout(() => {
            $('.cart-counter').removeClass('updated');
        }, 1000);
    }
    
    /**
     * Update cart total display
     */
    function updateCartTotal(total) {
        $('.cart-total').text('$' + total.toFixed(2));
    }
    
    /**
     * Update price display based on quantity
     */
    function updatePriceDisplay() {
        const quantity = parseInt($('input[name="quantity"]').val()) || 1;
        const unitPrice = parseFloat($('.unit-price').data('price')) || 0;
        const totalPrice = quantity * unitPrice;
        
        $('.total-price').text('$' + totalPrice.toFixed(2));
    }
    
    /**
     * Animate add to cart success
     */
    function animateAddToCartSuccess() {
        const addToCartBtn = $('.add-to-cart-btn');
        
        // Add success class for visual feedback
        addToCartBtn.addClass('success');
        
        // Remove success class after animation
        setTimeout(() => {
            addToCartBtn.removeClass('success');
        }, 2000);
    }
    
    /**
     * Setup notification system
     */
    function setupNotifications() {
        // Create notification container if it doesn't exist
        if ($('.notification-container').length === 0) {
            $('body').append('<div class="notification-container"></div>');
        }
    }
    
    /**
     * Show notification message
     */
    function showNotification(message, type = 'info') {
        const notificationHtml = `
            <div class="notification notification-${type}">
                <span class="notification-message">${message}</span>
                <button class="notification-close">&times;</button>
            </div>
        `;
        
        const notification = $(notificationHtml);
        $('.notification-container').append(notification);
        
        // Show notification with animation
        setTimeout(() => {
            notification.addClass('show');
        }, 100);
        
        // Auto hide after 5 seconds
        setTimeout(() => {
            hideNotification(notification);
        }, 5000);
        
        // Close button handler
        notification.find('.notification-close').on('click', function() {
            hideNotification(notification);
        });
    }
    
    /**
     * Hide notification
     */
    function hideNotification(notification) {
        notification.removeClass('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }
    
    /**
     * Setup product gallery
     */
    function setupProductGallery() {
        $('.product__details__pic__slider').owlCarousel({
            loop: true,
            margin: 20,
            items: 4,
            dots: true,
            smartSpeed: 1200,
            autoHeight: false,
            autoplay: true,
            responsive: {
                0: {
                    items: 3,
                },
                480: {
                    items: 4,
                }
            }
        });
        
        // Change main image on thumbnail click
        $('.product__details__pic__slider img').on('click', function() {
            var imgUrl = $(this).data('imgbigurl');
            $('.product__details__pic__item img').attr('src', imgUrl);
        });
    }
    
    /**
     * Setup color selection
     */
    function setupColorSelection() {
        $('input[name="productColor"]').on('change', function() {
            // Remove active class from all color labels
            $('.product__details__option__color label').removeClass('active');
            
            // Add active class to selected color label
            $(this).parent().addClass('active');
        });
        
        // Handle click on color labels (for better UX)
        $('.product__details__option__color label').on('click', function() {
            // Remove active class from all color labels
            $('.product__details__option__color label').removeClass('active');
            
            // Add active class to clicked label
            $(this).addClass('active');
            
            // Check the radio button
            $(this).find('input[type="radio"]').prop('checked', true);
        });
        
        // Initialize first color as active if none is selected
        if (!$('.product__details__option__color label.active').length) {
            $('.product__details__option__color label:first').addClass('active');
            $('.product__details__option__color label:first input[type="radio"]').prop('checked', true);
        }
    }
    
    /**
     * Setup wishlist
     */
    function setupWishlist() {
        $('.heart-icon').on('click', function(e) {
            e.preventDefault();
            $(this).toggleClass('active');
            showNotification('Product ' + ($(this).hasClass('active') ? 'added to' : 'removed from') + ' wishlist', 'success');
        });
    }
    
    /**
     * Setup tooltips
     */
    function setupTooltips() {
        $('[data-toggle="tooltip"]').tooltip();
    }
    
    /**
     * Setup smooth scroll
     */
    function setupSmoothScroll() {
        $('.rating').on('click', function(e) {
            e.preventDefault();
            $('html, body').animate({
                scrollTop: $('#reviews').offset().top - 100
            }, 1000);
        });
    }
});

/**
 * Additional utility functions
 */

/**
 * Format currency display
 */
function formatCurrency(amount) {
    return '$' + parseFloat(amount).toFixed(2);
}

/**
 * Validate email format
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
} 
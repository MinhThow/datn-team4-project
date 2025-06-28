/*  ---------------------------------------------------
    Template Name: Male Fashion
    Description: Male Fashion - ecommerce teplate
    Author: Colorib
    Author URI: https://www.colorib.com/
    Version: 1.0
    Created: Colorib
---------------------------------------------------------  */

'use strict';

(function ($) {

    /*------------------
        Preloader
    --------------------*/
    $(window).on('load', function () {
        $(".loader").fadeOut();
        $("#preloder").delay(200).fadeOut("slow");

        /*------------------
            Gallery filter
        --------------------*/
        $('.filter__controls li').on('click', function () {
            console.log('Filter clicked:', $(this).data('filter'));
            $('.filter__controls li').removeClass('active');
            $(this).addClass('active');
        });
        if ($('.product__filter').length > 0) {
            console.log('Initializing MixItUp...');
            var containerEl = document.querySelector('.product__filter');
            var mixer = mixitup(containerEl);
            console.log('MixItUp initialized:', mixer);
        } else {
            console.log('No .product__filter found');
        }
    });

    /*------------------
        Background Set
    --------------------*/
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });

    // Search functionality for direct input (overlay removed)

    // Search functionality for header and mobile search
    let searchTimeout;
    const headerSearchInput = $('#header-search-input');
    const mobileSearchInput = $('#mobile-search-input');
    const headerSearchResults = $('#header-search-results');
    const mobileSearchResults = $('#mobile-search-results');

    // Setup search for both header and mobile inputs
    function setupSearchInput(inputElement, resultsElement) {
        inputElement.on('input', function() {
            const query = $(this).val().trim();
            
            // Clear previous timeout
            clearTimeout(searchTimeout);
            
            if (query.length === 0) {
                resultsElement.hide();
                return;
            }
            
            // Debounce search requests
            searchTimeout = setTimeout(function() {
                performSearch(query, resultsElement);
            }, 300);
        });

        inputElement.on('keypress', function(e) {
            if (e.which === 13) { // Enter key
                e.preventDefault();
                const query = $(this).val().trim();
                if (query.length > 0) {
                    performSearch(query, resultsElement);
                }
            }
        });
    }

    // Initialize search for both inputs
    setupSearchInput(headerSearchInput, headerSearchResults);
    setupSearchInput(mobileSearchInput, mobileSearchResults);

    function performSearch(query, resultsElement) {
        console.log('Searching for:', query);
        
        // Show loading state
        resultsElement.html('<div class="search-result-item">Đang tìm kiếm...</div>').show();
        
        $.ajax({
            url: '/api/products/search',
            method: 'GET',
            data: { query: query },
            success: function(products) {
                console.log('Search results:', products);
                displaySearchResults(products, resultsElement);
            },
            error: function(xhr, status, error) {
                console.error('Search error:', error);
                resultsElement.html('<div class="search-result-item">Có lỗi xảy ra khi tìm kiếm</div>').show();
            }
        });
    }

    function displaySearchResults(products, resultsElement) {
        if (products.length === 0) {
            resultsElement.html('<div class="search-result-item">Không tìm thấy sản phẩm nào</div>').show();
            return;
        }
        
        let resultsHtml = '';
        products.forEach(function(product) {
            const imageUrl = product.image || 'img/product/product-1.jpg';
            resultsHtml += `
                <div class="search-result-item" onclick="selectProduct(${product.productID})">
                    <img src="${imageUrl}" alt="${product.name}">
                    <div class="search-result-info">
                        <h6>${product.name}</h6>
                        <div class="search-price">$${product.price}</div>
                    </div>
                </div>
            `;
        });
        
        resultsElement.html(resultsHtml).show();
    }

    function selectProduct(productId) {
        console.log('Selected product:', productId);
        // TODO: Navigate to product detail page
        // For now, just clear search inputs and hide results
        headerSearchInput.val('');
        mobileSearchInput.val('');
        headerSearchResults.hide();
        mobileSearchResults.hide();
    }

    // Close search results when clicking outside
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.header__search').length) {
            headerSearchResults.hide();
        }
        if (!$(e.target).closest('.mobile-search').length) {
            mobileSearchResults.hide();
        }
    });

    /*------------------
		Navigation
	--------------------*/
    $(".mobile-menu").slicknav({
        prependTo: '#mobile-menu-wrap',
        allowParentLinks: true
    });

    /*------------------
        Accordin Active
    --------------------*/
    $('.collapse').on('shown.bs.collapse', function () {
        $(this).prev().addClass('active');
    });

    $('.collapse').on('hidden.bs.collapse', function () {
        $(this).prev().removeClass('active');
    });

    //Canvas Menu
    $(".canvas__open").on('click', function () {
        $(".offcanvas-menu-wrapper").addClass("active");
        $(".offcanvas-menu-overlay").addClass("active");
    });

    $(".offcanvas-menu-overlay").on('click', function () {
        $(".offcanvas-menu-wrapper").removeClass("active");
        $(".offcanvas-menu-overlay").removeClass("active");
    });

    /*-----------------------
        Hero Slider
    ------------------------*/
    $(".hero__slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 1,
        dots: false,
        nav: true,
        navText: ["<span class='arrow_left'><span/>", "<span class='arrow_right'><span/>"],
        animateOut: 'fadeOut',
        animateIn: 'fadeIn',
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: false
    });

    /*--------------------------
        Select
    ----------------------------*/
    $("select").niceSelect();

    /*-------------------
		Radio Btn
	--------------------- */
    $(".product__color__select label, .shop__sidebar__size label, .product__details__option__size label").on('click', function () {
        $(".product__color__select label, .shop__sidebar__size label, .product__details__option__size label").removeClass('active');
        $(this).addClass('active');
    });

    /*-------------------
		Scroll
	--------------------- */
    $(".nice-scroll").niceScroll({
        cursorcolor: "#0d0d0d",
        cursorwidth: "5px",
        background: "#e5e5e5",
        cursorborder: "",
        autohidemode: true,
        horizrailenabled: false
    });

    /*------------------
        CountDown
    --------------------*/
    // For demo preview start
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    if(mm == 12) {
        mm = '01';
        yyyy = yyyy + 1;
    } else {
        mm = parseInt(mm) + 1;
        mm = String(mm).padStart(2, '0');
    }
    var timerdate = mm + '/' + dd + '/' + yyyy;
    // For demo preview end


    // Uncomment below and use your date //

    /* var timerdate = "2020/12/30" */

    $("#countdown").countdown(timerdate, function (event) {
        $(this).html(event.strftime("<div class='cd-item'><span>%D</span> <p>Days</p> </div>" + "<div class='cd-item'><span>%H</span> <p>Hours</p> </div>" + "<div class='cd-item'><span>%M</span> <p>Minutes</p> </div>" + "<div class='cd-item'><span>%S</span> <p>Seconds</p> </div>"));
    });

    /*------------------
		Magnific
	--------------------*/
    $('.video-popup').magnificPopup({
        type: 'iframe'
    });

    /*-------------------
		Quantity change
	--------------------- */
    var proQty = $('.pro-qty');
    proQty.prepend('<span class="fa fa-angle-up dec qtybtn"></span>');
    proQty.append('<span class="fa fa-angle-down inc qtybtn"></span>');
    proQty.on('click', '.qtybtn', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });

    var proQty = $('.pro-qty-2');
    proQty.prepend('<span class="fa fa-angle-left dec qtybtn"></span>');
    proQty.append('<span class="fa fa-angle-right inc qtybtn"></span>');
    proQty.on('click', '.qtybtn', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });

    /*------------------
        Achieve Counter
    --------------------*/
    $('.cn_num').each(function () {
        $(this).prop('Counter', 0).animate({
            Counter: $(this).text()
        }, {
            duration: 4000,
            easing: 'swing',
            step: function (now) {
                $(this).text(Math.ceil(now));
            }
        });
    });

})(jQuery);
package com.java6.datn.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Integer productID;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal salePrice;
    private boolean hasDiscount;
    private Integer discountPercent;
    private Integer stock;
    private String image;
    private Integer categoryID;
    private String size;
    private Double rating;
    private Integer totalReviews;
    private boolean isNew;
    private boolean isSale;

    // Constructors
    public ProductDTO() {}

    public ProductDTO(Integer productID, String name, String description, BigDecimal price, 
                     BigDecimal salePrice, Integer stock, String image, Integer categoryID, String size) {
        this.productID = productID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.stock = stock;
        this.image = image;
        this.categoryID = categoryID;
        this.size = size;
        
        // Calculate derived fields
        this.hasDiscount = (salePrice != null && salePrice.compareTo(price) < 0);
        this.isSale = this.hasDiscount;
        
        if (this.hasDiscount) {
            this.discountPercent = price.subtract(salePrice)
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(price, 0, java.math.RoundingMode.HALF_UP)
                                    .intValue();
        } else {
            this.discountPercent = 0;
        }
    }

    // Getters and setters
    public Integer getProductID() { return productID; }
    public void setProductID(Integer productID) { this.productID = productID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { 
        this.salePrice = salePrice;
        // Recalculate derived fields when salePrice changes
        this.hasDiscount = (salePrice != null && price != null && salePrice.compareTo(price) < 0);
        this.isSale = this.hasDiscount;
        
        if (this.hasDiscount && price != null) {
            this.discountPercent = price.subtract(salePrice)
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(price, 0, java.math.RoundingMode.HALF_UP)
                                    .intValue();
        } else {
            this.discountPercent = 0;
        }
    }
    
    public boolean isHasDiscount() { return hasDiscount; }
    public void setHasDiscount(boolean hasDiscount) { this.hasDiscount = hasDiscount; }
    
    public Integer getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }
    
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    public Integer getCategoryID() { return categoryID; }
    public void setCategoryID(Integer categoryID) { this.categoryID = categoryID; }
    
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
    
    public boolean isSale() { return isSale; }
    public void setSale(boolean isSale) { this.isSale = isSale; }
} 
package com.micropos.products.service;

import com.micropos.model.Product;

import java.util.List;

public interface ProductService {


    List<Product> products();

    Product getProduct(String id);

    Product randomProduct();
}

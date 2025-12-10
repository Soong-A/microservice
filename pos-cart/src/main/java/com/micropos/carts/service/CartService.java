package com.micropos.carts.service;

import com.micropos.model.Cart;
import com.micropos.model.Item;

public interface CartService {
    Cart getCart();

    Cart addToCart(Item item);

    Item getItem(String id);

    Cart removeItem(String id);

    Cart modifyItem(Item item);

    Item randomItem();
}

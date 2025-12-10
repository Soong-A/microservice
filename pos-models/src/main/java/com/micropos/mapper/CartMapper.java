package com.micropos.mapper;

import com.micropos.dto.ItemDto;
import com.micropos.model.Item;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(implementationPackage = "com.micropos.carts.mapper")
public interface CartMapper {
    Collection<ItemDto> toCartDto(Collection<Item> items);

    Collection<Item> toCart(Collection<ItemDto> cartItems);

    ItemDto toItemDto(Item cartItem);

    Item toItem(ItemDto cartItem);
}

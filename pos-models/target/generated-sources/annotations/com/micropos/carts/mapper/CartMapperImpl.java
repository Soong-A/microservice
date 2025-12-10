package com.micropos.carts.mapper;

import com.micropos.dto.ItemDto;
import com.micropos.mapper.CartMapper;
import com.micropos.model.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public Collection<ItemDto> toCartDto(Collection<Item> items) {
        if ( items == null ) {
            return null;
        }

        Collection<ItemDto> collection = new ArrayList<ItemDto>( items.size() );
        for ( Item item : items ) {
            collection.add( toItemDto( item ) );
        }

        return collection;
    }

    @Override
    public Collection<Item> toCart(Collection<ItemDto> cartItems) {
        if ( cartItems == null ) {
            return null;
        }

        Collection<Item> collection = new ArrayList<Item>( cartItems.size() );
        for ( ItemDto itemDto : cartItems ) {
            collection.add( toItem( itemDto ) );
        }

        return collection;
    }

    @Override
    public ItemDto toItemDto(Item cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        ItemDto itemDto = new ItemDto();

        itemDto.setProductId( cartItem.getProductId() );
        itemDto.setQuantity( BigDecimal.valueOf( cartItem.getQuantity() ) );

        return itemDto;
    }

    @Override
    public Item toItem(ItemDto cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        int quantity = 0;

        if ( cartItem.getQuantity() != null ) {
            quantity = cartItem.getQuantity().intValue();
        }

        String product = null;

        Item item = new Item( product, quantity );

        return item;
    }
}

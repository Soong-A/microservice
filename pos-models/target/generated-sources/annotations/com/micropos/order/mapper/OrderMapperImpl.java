package com.micropos.order.mapper;

import com.micropos.dto.ItemDto;
import com.micropos.dto.OrderDto;
import com.micropos.mapper.OrderMapper;
import com.micropos.model.Item;
import com.micropos.model.Order;
import com.micropos.model.OrderStatus;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Collection<Order> toOrders(Collection<OrderDto> orderDtos) {
        if ( orderDtos == null ) {
            return null;
        }

        Collection<Order> collection = new ArrayList<Order>( orderDtos.size() );
        for ( OrderDto orderDto : orderDtos ) {
            collection.add( toOrder( orderDto ) );
        }

        return collection;
    }

    @Override
    public Collection<OrderDto> toOrderDtos(Collection<Order> orders) {
        if ( orders == null ) {
            return null;
        }

        Collection<OrderDto> collection = new ArrayList<OrderDto>( orders.size() );
        for ( Order order : orders ) {
            collection.add( toOrderDto( order ) );
        }

        return collection;
    }

    @Override
    public Order toOrder(OrderDto orderDto) {
        if ( orderDto == null ) {
            return null;
        }

        String id = null;
        List<Item> items = null;
        OrderStatus status = null;

        id = orderDto.getId();
        items = itemDtoListToItemList( orderDto.getItems() );
        if ( orderDto.getStatus() != null ) {
            status = Enum.valueOf( OrderStatus.class, orderDto.getStatus() );
        }

        Order order = new Order( id, items, status );

        return order;
    }

    @Override
    public OrderDto toOrderDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setId( order.getId() );
        orderDto.setItems( itemListToItemDtoList( order.getItems() ) );
        if ( order.getStatus() != null ) {
            orderDto.setStatus( order.getStatus().name() );
        }

        return orderDto;
    }

    protected Item itemDtoToItem(ItemDto itemDto) {
        if ( itemDto == null ) {
            return null;
        }

        int quantity = 0;

        if ( itemDto.getQuantity() != null ) {
            quantity = itemDto.getQuantity().intValue();
        }

        String product = null;

        Item item = new Item( product, quantity );

        return item;
    }

    protected List<Item> itemDtoListToItemList(List<ItemDto> list) {
        if ( list == null ) {
            return null;
        }

        List<Item> list1 = new ArrayList<Item>( list.size() );
        for ( ItemDto itemDto : list ) {
            list1.add( itemDtoToItem( itemDto ) );
        }

        return list1;
    }

    protected ItemDto itemToItemDto(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemDto itemDto = new ItemDto();

        itemDto.setProductId( item.getProductId() );
        itemDto.setQuantity( BigDecimal.valueOf( item.getQuantity() ) );

        return itemDto;
    }

    protected List<ItemDto> itemListToItemDtoList(List<Item> list) {
        if ( list == null ) {
            return null;
        }

        List<ItemDto> list1 = new ArrayList<ItemDto>( list.size() );
        for ( Item item : list ) {
            list1.add( itemToItemDto( item ) );
        }

        return list1;
    }
}

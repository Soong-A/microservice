package com.micropos.mapper;

import com.micropos.dto.OrderDto;
import com.micropos.model.Order;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(implementationPackage = "com.micropos.order.mapper")
public interface OrderMapper {
    Collection<Order> toOrders(Collection<OrderDto> orderDtos);

    Collection<OrderDto> toOrderDtos(Collection<Order> orders);

    Order toOrder(OrderDto orderDto);

    OrderDto toOrderDto(Order order);
}

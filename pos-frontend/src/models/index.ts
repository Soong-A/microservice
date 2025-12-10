// import type { Item, ItemDto } from './item';
// import type { Product } from './product';
//
// export { Item, Product, ItemDto };



// 商品类型（对应 Product 服务）
export interface Product {
    id: string;
    name: string;
    price: number;
    image: string; // 商品图片地址
    description?: string; // 可选描述
}

// 购物车项类型（对应 Cart 服务）
export interface ItemDto {
    id: string; // 购物车项 ID
    productId: string; // 关联商品 ID
    productName: string; // 商品名称（冗余存储）
    price: number; // 商品单价
    quantity: number; // 数量
    image?: string; // 商品图片（可选）
}

// 订单类型（对应 Order 服务）
export interface Order {
    id: string; // 订单 ID
    items: ItemDto[]; // 订单包含的商品
    totalPrice: number; // 订单总价
    address: string; // 收货地址
    createTime: string; // 创建时间（ISO 格式，如 "2025-12-10T10:00:00"）
    status: 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED'; // 订单状态
}

// 配送状态类型（对应 Delivery 服务）
export type DeliveryStatus = {
    orderId: string;
    status: 'PREPARING' | 'SHIPPED' | 'IN_TRANSIT' | 'DELIVERED' | 'FAILED'; // 配送状态
    logisticsCompany?: string; // 物流公司（可选）
    trackingNumber?: string; // 物流单号（可选）
    updateTime: string; // 最后更新时间
};
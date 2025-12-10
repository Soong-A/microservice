// import axios from 'axios';
// import { ItemDto, Product } from '../models';
//
// export function fetchProducts() {
//     return axios.get('/api/products').then(res => res.data as Product[]);
// }
//
// export function fetchProduct(id: string) {
//     return axios.get(`/api/products/${id}`).then(res => res.data as Product);
// }
//
// export function fetchCart() {
//     return axios.get('/api/cart').then(res => res.data as ItemDto[]);
// }
//
// export function addToCart(productId: string) {
//     return axios
//         .post(`/api/cart/${productId}`)
//         .then(res => res.data as ItemDto[]);
// }
//
// export function removeFromCart(productId: string) {
//     return axios
//         .delete(`/api/cart/${productId}`)
//         .then(res => res.data as ItemDto[]);
// }
//
// export function modifyCart(productId: string, quantity: number) {
//     return axios
//         .put(
//             `/api/cart/${productId}`,
//             {},
//             {
//                 params: { quantity: quantity }
//             }
//         )
//         .then(res => res.data as ItemDto[]);
// }
//
// export function fetchCartItem(productId: string) {
//     return axios.get(`/api/cart/${productId}`).then(res => res.data as ItemDto);
// }
//
// // 订单服务 API
// export function fetchOrders() {
//     // @ts-ignore
//     return axios.get('/api/orders').then(res => res.data as Order[]);
// }
//
// export function createOrder(orderRequest: { items: ItemDto[] }) {
//     // @ts-ignore
//     return axios.post('/api/orders', orderRequest).then(res => res.data as Order);
// }
//
// // 计数器服务 API
// export function incrementProductView(productId: string) {
//     // 通常作为副作用调用，不关心返回值
//     return axios.post(`/api/counter/${productId}/inc`);
// }
//
// export function fetchProductCount(productId: string) {
//     return axios.get(`/api/counter/${productId}`).then(res => res.data as { count: number });
// }
//
// // 配送服务 API
// export function fetchDeliveryStatus(orderId: string) {
//     // @ts-ignore
//     return axios.get(`/api/delivery/${orderId}`).then(res => res.data as DeliveryStatus);
// }



// @ts-ignore
import axios from '../utils/request';
import { Product, ItemDto, Order, DeliveryStatus } from '../models';

// 1. 商品服务 API
export function fetchProducts() {
    return axios.get('/api/products').then((res: { data: Product[]; }) => res.data as Product[]);
}

export function fetchProduct(id: string) {
    return axios.get(`/api/products/${id}`).then((res: { data: Product; }) => res.data as Product);
}

// 2. 购物车服务 API
export function fetchCart() {
    return axios.get('/api/cart').then((res: { data: ItemDto[]; }) => res.data as ItemDto[]);
}

export function addToCart(productId: string) {
    return axios.post(`/api/cart/${productId}`).then((res: { data: ItemDto[]; }) => res.data as ItemDto[]);
}

export function removeFromCart(productId: string) {
    return axios.delete(`/api/cart/${productId}`).then((res: { data: ItemDto[]; }) => res.data as ItemDto[]);
}

export function modifyCart(productId: string, quantity: number) {
    return axios
        .put(`/api/cart/${productId}`, {}, { params: { quantity } })
        .then((res: { data: ItemDto[]; }) => res.data as ItemDto[]);
}

export function fetchCartItem(productId: string) {
    return axios.get(`/api/cart/${productId}`).then((res: { data: ItemDto; }) => res.data as ItemDto);
}

// 3. 订单服务 API
export function fetchOrders() {
    return axios.get('/api/orders').then((res: { data: Order[]; }) => res.data as Order[]);
}

export function createOrder(orderRequest: { items: ItemDto[]; address: string }) {
    return axios.post('/api/orders', orderRequest).then((res: { data: Order; }) => res.data as Order);
}

// 4. 计数器服务 API
export function incrementProductView(productId: string) {
    return axios.post(`/api/counter/${productId}/inc`);
}

export function fetchProductCount(productId: string) {
    return axios.get(`/api/counter/${productId}`).then((res: { data: { count: number; }; }) => res.data as { count: number });
}

// 5. 配送服务 API
export function fetchDeliveryStatus(orderId: string) {
    return axios.get(`/api/delivery/${orderId}`).then((res: { data: DeliveryStatus; }) => res.data as DeliveryStatus);
}
package com.fpt.exam.controller;

import com.fpt.exam.dto.ApiResponse;
import com.fpt.exam.dto.PaginationResponse;
import com.fpt.exam.entity.Order;
import com.fpt.exam.service.impl.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<Order>>> getAllOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Page<Order> orderPage = orderService.getAllOrders(keyword, status, userId, page, size, sortBy, sortDir);
            PaginationResponse<Order> paginationResponse = PaginationResponse.of(
                    orderPage.getContent(),
                    orderPage.getNumber(),
                    orderPage.getSize(),
                    orderPage.getTotalElements()
            );
            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", paginationResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve orders: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to retrieve order: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(ApiResponse.success("Order created successfully", createdOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create order: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> updateOrder(@PathVariable Long id, @Valid @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(id, order);
            return ResponseEntity.ok(ApiResponse.success("Order updated successfully", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update order: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete order: " + e.getMessage()));
        }
    }
}


package com.example.controller;

import com.example.dto.UserDTO;
import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OrderService orderService;

    // GET all orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    // GET order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order == null)
            return ResponseEntity.status(404).body("Order not found");
        return ResponseEntity.ok(order);
    }

    // CREATE new order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    // UPDATE order
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order order = orderService.findById(id);
        if (order == null)
            return ResponseEntity.status(404).body("Order not found");

        order.setUserId(updatedOrder.getUserId());
        order.setProduct(updatedOrder.getProduct());
        order.setAmount(updatedOrder.getAmount());

        return ResponseEntity.ok(orderService.saveOrder(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);

        if (order == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Order not found with ID: " + id));
        }

        orderService.deleteOrder(id);

        // ✔ Trả JSON hợp lệ
        return ResponseEntity.ok(
                Map.of("message", "Order with ID " + id + " deleted successfully")
        );
    }


    // CALL USER SERVICE
    @GetMapping("/details/{userId}")
    public UserDTO getOrderDetails(@PathVariable Long userId) {
        String userServiceUrl = "lb://user-service/api/users/" + userId;
        return restTemplate.getForObject(userServiceUrl, UserDTO.class);
    }
}

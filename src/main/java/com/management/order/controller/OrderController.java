package com.management.order.controller;

import com.management.order.model.Order;
import com.management.order.security.service.UserDetailsImpl;
import com.management.order.service.IOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/order")
@Slf4j
@AllArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping("/number")
    public ResponseEntity<?> getOrderNumber(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Order getOrderNumber called");
        return orderService.getOrderNumber(userDetails);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Order getAllByUser called");
        return orderService.getAllByUser(userDetails);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderWithOrderDetails(@PathVariable("id") long id) {
        log.info("Order getOrderWithOrderDetails called");
        return orderService.getOrderWithOrderDetails(id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> parameters
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Order Create called");
        return orderService.createOrder(parameters, userDetails);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Order> updateOrder(@RequestBody Map<String, Object> parameters
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Order Update called");
        return orderService.updateOrder(parameters, userDetails);
    }

    @PutMapping("/status/{id}")
    @Transactional
    public ResponseEntity<Order> updateStatusOfOrder(@PathVariable("id") long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Order updateStatusOfOrder called");
        return orderService.updateStatusOfOrder(id, userDetails);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<HttpStatus> deleteAllByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Order deleteAllByUser called");
        return orderService.deleteAllByUser(userDetails);
    }
}

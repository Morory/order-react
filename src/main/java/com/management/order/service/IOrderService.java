package com.management.order.service;

import com.management.order.model.Order;
import com.management.order.security.service.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IOrderService {

    public ResponseEntity<?> getOrderNumber(UserDetailsImpl userDetails);

    public ResponseEntity<List<Order>> getAllByUser(UserDetailsImpl userDetails);

    public ResponseEntity<Map<String, Object>> getOrderWithOrderDetails(long id);

    public ResponseEntity<Order> createOrder(Map<String, Object> parameters, UserDetailsImpl userDetails);

    public ResponseEntity<Order> updateOrder(Map<String, Object> parameters, UserDetailsImpl userDetails);

    public ResponseEntity<Order> updateStatusOfOrder(long id, UserDetailsImpl userDetails);

    public ResponseEntity<HttpStatus> deleteAllByUser(UserDetailsImpl userDetails);
}

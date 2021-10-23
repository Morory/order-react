package com.management.order.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.order.model.Client;
import com.management.order.model.Order;
import com.management.order.model.OrderDetail;
import com.management.order.repository.ClientRepository;
import com.management.order.repository.OrderDetailRepository;
import com.management.order.repository.OrderRepository;
import com.management.order.security.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;

    private final ClientRepository clientRepository;

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public ResponseEntity<?> getOrderNumber(UserDetailsImpl userDetails) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(Calendar.getInstance().getTime());
        int cnt = orderRepository.findCountByUserIdAndToday(userDetails.getId(), today);
        String orderNumber = today.replace("-", "") + "-" + String.format("%03d", ++cnt);
        return new ResponseEntity<>(orderNumber, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Order>> getAllByUser(UserDetailsImpl userDetails) {
        try {
            List<Order> orders = new ArrayList<>();

            orderRepository.findAllByUserId(userDetails.getId()).forEach(orders::add);

            if(orders.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getOrderWithOrderDetails(long id) {
        try {
            Optional<Order> _order = orderRepository.findById(id);

            if(!_order.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(id);

            Map<String, Object> map = new HashMap<>();
            map.put("order", _order.get());
            map.put("orderDetails", orderDetails);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Order> createOrder(Map<String, Object> parameters, UserDetailsImpl userDetails) {
        try {
            Optional<Client> client = clientRepository.findById(Long.parseLong(parameters.get("clientId").toString()));

            if(!client.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Order _order = Order.builder()
                    .user(userDetails.getUser())
                    .client(client.get())
                    .orderDate(dateParseOf(parameters.get("orderDate")))
                    .deliveryDate(dateParseOf(parameters.get("deliveryDate")))
                    .orderNumber(parameters.get("orderNumber").toString())
                    .title(parameters.get("title").toString())
                    .completed(Boolean.parseBoolean(parameters.get("completed").toString()))
                    .build();
            if(parameters.containsKey("list")) {
                List<OrderDetail> orderDetails = new ObjectMapper().readValue(
                        parameters.get("list").toString(), new TypeReference<ArrayList<OrderDetail>>() {});
                for(OrderDetail orderDetail : orderDetails) {
                    orderDetail.setOrder(_order);
                    orderDetailRepository.save(orderDetail);
                }
            }
            orderRepository.save(_order);
            return new ResponseEntity<>(_order, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Order> updateOrder(Map<String, Object> parameters, UserDetailsImpl userDetails) {
        try {
            Optional<Client> client = clientRepository.findById(Long.parseLong(parameters.get("clientId").toString()));

            if(!client.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Optional<Order> _order = orderRepository.findById(Long.parseLong(parameters.get("id").toString()));

            if(!_order.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Order order = _order.get();
            order.setClient(client.get());
            order.setUser(userDetails.getUser());
            order.setOrderDate(dateParseOf(parameters.get("orderDate")));
            order.setDeliveryDate(dateParseOf(parameters.get("deliveryDate")));
            order.setOrderNumber(parameters.get("orderNumber").toString());
            order.setTitle(parameters.get("title").toString());
            order.setCompleted(Boolean.parseBoolean(parameters.get("completed").toString()));
            if(parameters.containsKey("list")) {
                List<OrderDetail> orderDetails = new ObjectMapper().readValue(
                        parameters.get("list").toString(), new TypeReference<ArrayList<OrderDetail>>() {});
                for(OrderDetail orderDetail : orderDetails) {
                    orderDetail.setOrder(order);
                }
                order.setOrderDetails(new HashSet<>(orderDetails));
                orderDetailRepository.deleteAllByOrderId(order.getId());
            }
            orderRepository.save(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Order> updateStatusOfOrder(long id, UserDetailsImpl userDetails) {
        try {
            Optional<Order> _order = orderRepository.findById(id);

            if(!_order.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Order order = _order.get();
            if(order.getUser().getId() != userDetails.getId() ) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            order.setDeleted(!order.isDeleted());

            return new ResponseEntity<>(orderRepository.save(order), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteAllByUser(UserDetailsImpl userDetails) {
        try {
            List<Order> orders = orderRepository.findAllByUserIdAndDeletedTrue(userDetails.getId());

            for(Order order : orders) {
                orderDetailRepository.deleteAllByOrderId(order.getId());
                orderRepository.deleteById(order.getId());
            }

            orders.forEach(order -> {
                orderDetailRepository.deleteAllByOrderId(order.getId());
                orderRepository.deleteById(order.getId());
            });


            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Date dateParseOf(Object object) throws ParseException {
        return "".equals(object.toString()) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(object.toString());
    }
}

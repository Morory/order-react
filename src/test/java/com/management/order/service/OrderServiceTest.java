package com.management.order.service;

import com.management.order.model.Client;
import com.management.order.model.Order;
import com.management.order.payload.request.SignupRequest;
import com.management.order.repository.ClientRepository;
import com.management.order.repository.OrderRepository;
import com.management.order.security.service.UserDetailsImpl;
import com.management.order.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private IAuthService authService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setup() {
        String username = "testName";
        String email = "testEmail@email.com";
        String password = "testPassword";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);

        authService.registerUser(signupRequest);

        userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
    }

    @Test
    void getOrderNumber() {
        String orderNumber = (String) orderService.getOrderNumber(userDetails).getBody();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();
        String strToday = sdf.format(c1.getTime());

        assertEquals(strToday + "-001", orderNumber);
    }

    @Test
    void getAllByUser() {
        ResponseEntity<List<Order>> responseEntity = orderService.getAllByUser(userDetails);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void getOrderWithOrderDetails() {
        Order order = Order.builder()
                .user(userDetails.getUser())
                .client(clientRepository.save(
                        Client.builder()
                                .name("TestName")
                                .build()
                ))
                .build();
        Order savedOrder = orderRepository.save(order);
        ResponseEntity<Map<String, Object>> responseEntity = orderService.getOrderWithOrderDetails(savedOrder.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void createOrder() {
        Client savedClient = clientRepository.save(Client.builder().build());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("clientId", savedClient.getId());
        parameters.put("orderDate", "2021-10-21");
        parameters.put("deliveryDate", "2021-10-21");
        parameters.put("orderNumber", "20211021-001");
        parameters.put("title", "testTitle");
        parameters.put("completed", "false");
        ResponseEntity<Order> responseEntity = orderService.createOrder(parameters, userDetails);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void updateOrder() {
        Client savedClient = clientRepository.save(Client.builder().build());

        Order order = Order.builder()
                .user(userDetails.getUser())
                .title("originalTitle")
                .client(savedClient)
                .build();
        orderRepository.save(order);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", order.getId());
        parameters.put("clientId", savedClient.getId());
        parameters.put("orderDate", "2021-10-21");
        parameters.put("deliveryDate", "2021-10-21");
        parameters.put("orderNumber", "20211021-001");
        parameters.put("title", "UpdateTitle");
        parameters.put("completed", "false");
        ResponseEntity<Order> responseEntity = orderService.updateOrder(parameters, userDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateStatusOfOrder() {
        Order order = Order.builder()
                .user(userDetails.getUser())
                .client(clientRepository.save(
                        Client.builder()
                                .name("TestName")
                                .build()
                ))
                .build();
        Order savedOrder = orderRepository.save(order);
        ResponseEntity<Order> responseEntity = orderService.updateStatusOfOrder(savedOrder.getId(), userDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteAllByUser() {
        ResponseEntity<HttpStatus> responseEntity = orderService.deleteAllByUser(userDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}

package com.adwait.aitrading.controller;

import com.adwait.aitrading.model.Coin;
import com.adwait.aitrading.model.Order;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.request.CreateOrderRequest;
import com.adwait.aitrading.service.CoinService;
import com.adwait.aitrading.service.OrderService;
import com.adwait.aitrading.service.UserService;
// import com.adwait.aitrading.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    //@Autowired
    //private WalletTransactionService wallet_transaction_service;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
                                                 @RequestBody CreateOrderRequest req) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(req.getCoinId());

        Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwtToken,
                                              @PathVariable Long orderId) throws Exception{
        // this was added
        if (jwtToken == null) {
            throw new Exception("Token missing.");
        }

        User user = userService.findUserProfileByJwt(jwtToken);
        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return ResponseEntity.ok(order);
        }
        else{
            //throw new Exception("You don't have access");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrderForUser(@RequestHeader("Authorization") String jwt,
                                                          @RequestParam(required = false) String orderType,
                                                          @RequestParam(required = false) String assetSymbol) throws Exception{

        if (jwt == null) {
            throw new Exception("Token missing.");
        }

        Long userId = userService.findUserProfileByJwt(jwt).getId();

        List<Order> userOrders = orderService.getAllOrdersOfUser(userId, orderType, assetSymbol);

        return ResponseEntity.ok(userOrders);
    }


}

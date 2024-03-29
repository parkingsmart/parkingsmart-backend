package com.oocl.parkingsmart.controller;

import com.oocl.parkingsmart.endpoint.OrderEndpoint;
import com.oocl.parkingsmart.endpoint.UserEndpoint;
import com.oocl.parkingsmart.entity.Employee;
import com.oocl.parkingsmart.entity.Order;
import com.oocl.parkingsmart.entity.ParkingLot;
import com.oocl.parkingsmart.exception.NotEnoughCapacityException;
import com.oocl.parkingsmart.exception.ResourceConflictException;
import com.oocl.parkingsmart.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderEndpoint orderEndpoint;
    @GetMapping
    public ResponseEntity getAllOrders() {
        HashMap ordersMap = new HashMap();
        List<Order> orders = orderService.getAllOrders();
        ordersMap.put("AllOrdersNum", orders.size());
        ordersMap.put("orders", orders);
        return ResponseEntity.ok().body(ordersMap);
    }

    @GetMapping(params = {"page"})
    public ResponseEntity getPageOrders(@RequestParam(name = "page" ,defaultValue = "1") int page) {
        HashMap ordersMap = new HashMap();
        Long allOrdersNum = orderService.getAllOrdersNum();
        ordersMap.put("AllOrdersNum", allOrdersNum);
        List<Order> orders = orderService.getPageOrders(page);
        ordersMap.put("orders", orders);
        return ResponseEntity.ok().body(ordersMap);
    }

    @GetMapping(params = {"status"})
    public ResponseEntity getOrdersByStatus(@RequestParam(name = "status", defaultValue = "0") int status){
        HashMap ordersMap = new HashMap();
        List<Order> newOrders=orderService.getOrdersByStatus(status);
        ordersMap.put("orders", newOrders);
        return ResponseEntity.ok().body(ordersMap);
    }

    @GetMapping(params = {"status", "page"})
    public ResponseEntity getPageOrdersByStatus(
            @RequestParam(name = "status", defaultValue = "0") int status,
            @RequestParam(name = "page", defaultValue = "1") int page){
        HashMap ordersMap = new HashMap();
        List<Order> newOrders=orderService.getPageOrdersByStatus(status, page);
        ordersMap.put("orders", newOrders);
        return ResponseEntity.ok().body(ordersMap);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrdersByStatus(@PathVariable Long id){
        Order order=orderService.getOrdersById(id);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping
    public ResponseEntity receiveAnOrder(@RequestBody Order order) throws ResourceConflictException {
        orderService.addOrder(order);
        orderEndpoint.sendAllMessage("1");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity grabOrderById(@PathVariable Long id,@RequestParam(name = "employeeId") Long employeeId) {
        orderService.grabOrderById(id,employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/parkinglot")
    public ResponseEntity updateOrderParkingLot(@PathVariable Long id,@RequestBody ParkingLot parkingLot) throws NotEnoughCapacityException {
        orderService.updateOrderParkingLot(id,parkingLot);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity updateOrderStatus(@PathVariable Long id,@RequestParam(name = "status") int status){
        orderService.updateOrderStatus(id,status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}",params = {"endTime"})
    public ResponseEntity updateOrderStatus(@PathVariable Long id,
    @RequestParam(name = "endTime") Long endTime){
        orderService.payAnOrder(id,endTime);
        return ResponseEntity.ok().build();
    }

}

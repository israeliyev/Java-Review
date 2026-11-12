package org.example.patterns;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class CreateOrderCommand {
    private String name;
    private Integer quantity;
    private BigDecimal price;
}

@Service
class OrderService {

    @Transactional
    void createOrder(CreateOrderCommand command) {
        // order create operations
    }
}

@RequiredArgsConstructor
@Service
class OrderBatchService {
    private final OrderService orderService;

    void createOrder(List<CreateOrderCommand> commands){
        commands.forEach(orderService::createOrder);
    }

}

class OrderRequest {
  List<CreateOrderCommand> commands;
}

@RestController
@RequiredArgsConstructor
class OrderController {

    private final OrderBatchService orderBatchService;

    @PostMapping
    void createOrder(@RequestBody OrderRequest request){
 this.orderBatchService.createOrder(request.commands);
    }
}

public class ProxyPatternSpring {
}

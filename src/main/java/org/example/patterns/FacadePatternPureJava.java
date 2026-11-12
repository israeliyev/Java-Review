//package org.example.patterns;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.List;
//
//class InventoryReservation {
//
//}
//
//class PaymentResult {
//
//}
//
//class Shipment {
//
//}
//
//class InventoryService {
//    // inventory operations
//
//    InventoryReservation reserve(List<Long> productIds) {
//        return null;
//    }
//}
//
//class PaymentService {
//    // payment operations
//
//    PaymentResult charge(Long customerId, Integer amount) {
//        return null;
//    }
//}
//
//class ShippingService {
//    // shipping operations
//
//    Shipment createShipment(Long orderId) {
//        return null;
//    }
//}
//
//class NotificationService {
//    // notifaction operations
//    void sendOrderConfirmation(Long customerId, Long orderId) {
//    }
//}
//
//class CheckoutFacade {
//
//    private final InventoryService inventoryService;
//    private final PaymentService paymentService;
//    private final ShippingService shippingService;
//    private final NotificationService notificationService;
//
//    CheckoutFacade(InventoryService inventoryService, PaymentService paymentService, ShippingService shippingService, NotificationService notificationService) {
//        this.inventoryService = inventoryService;
//        this.paymentService = paymentService;
//        this.shippingService = shippingService;
//        this.notificationService = notificationService;
//    }
//
//
//    public CheckoutResult checkout(CheckoutCommand command) {
//        InventoryReservation reservation = inventoryService.reserve(command.getProductIds());
//
//        PaymentResult paymentResult = paymentService.charge(
//                command.getCustomerId(),
//                command.getAmount()
//        );
//
//        Shipment shipment = shippingService.createShipment(command.getOrderId());
//
//        notificationService.sendOrderConfirmation(
//                command.getCustomerId(),
//                command.getOrderId());
//
//        // some operations and return data
//
//        return null;
//    }
//
//}
//
//class CheckoutResult {
//
//}
//
//@Getter
//@Setter
//class CheckoutCommand {
//    private Long customerId;
//    private Long orderId;
//    private Integer amount;
//    private List<Long> productIds;
//}
//
//public class FacadePatternPureJava {
//
//    InventoryService inventoryService = new InventoryService();
//    PaymentService paymentService = new PaymentService();
//    ShippingService shippingService = new ShippingService();
//    NotificationService notificationService = new NotificationService();
//
//    CheckoutFacade checkoutFacade = new CheckoutFacade(inventoryService, paymentService, shippingService, notificationService);
//
//    CheckoutCommand checkoutCommand = new CheckoutCommand();
//    // assigning command vlaues
//
//    CheckoutResult checkoutResult = checkoutFacade.checkout(checkoutCommand);
//
//}

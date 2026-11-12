//package org.example.patterns;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//
//interface PaymentGateWay{
//    void pay(PaymentRequest paymentRequest);
//}
//
//@Component
//class StripeAdapter implements PaymentGateWay{
//
//    @Override
//    public void pay(PaymentRequest paymentRequest) {
//
//    }
//}
//
//class UnsupportedPaymentTypeException extends RuntimeException{
//    public UnsupportedPaymentTypeException(String message){
//        super(message);
//    }
//}
//
//enum PaymentType {
//    CARD, PAYPAL, BANK
//}
//
//record PaymentResult(String message) {
//}
//
//record PaymentRequest(PaymentType paymentType, BigDecimal price) {
//}
//
//interface PaymentStrategy {
//
//    PaymentType supportedType();
//
//    PaymentResult pay(
//            PaymentRequest request
//    );
//}
//
//@Component
//@RequiredArgsConstructor
//class CardPaymentStrategy implements PaymentStrategy{
//
//    private final PaymentGateWay paymentGateWay;
//
//    @Override
//    public PaymentType supportedType() {
//        return PaymentType.CARD;
//    }
//
//    @Override
//    public PaymentResult pay(PaymentRequest request) {
//        return paymentGateWay.pay(request);
//    }
//}
//
//@Component
//class PaypalPaymentStrategy implements PaymentStrategy{
//
//    @Override
//    public PaymentType supportedType() {
//        return PaymentType.PAYPAL;
//    }
//
//    @Override
//    public PaymentResult pay(PaymentRequest request) {
//        return null;
//    }
//}
//
//@Component
//class BankTransferPaymentStrategy implements PaymentStrategy{
//
//    @Override
//    public PaymentType supportedType() {
//        return PaymentType.BANK;
//    }
//
//    @Override
//    public PaymentResult pay(PaymentRequest request) {
//        return null;
//    }
//}
//
//@Component
//class PaymentStrategyResolver {
//
//    private final Map<PaymentType, PaymentStrategy> strategyMap;
//
//        PaymentStrategyResolver(List<PaymentStrategy> strategies){
//            strategyMap = strategies.stream().collect(Collectors.toUnmodifiableMap(PaymentStrategy::supportedType, Function.identity()));
//        }
//
//    public PaymentStrategy get(PaymentRequest paymentRequest){
//            return strategyMap.get(paymentRequest.paymentType());
//    }
//
//}
//
//@Service
//@RequiredArgsConstructor
//class PaymentService {
//    private final PaymentStrategyResolver paymentStrategyResolver;
//
//    public PaymentResult pay(PaymentRequest paymentRequest){
//        PaymentStrategy strategy = paymentStrategyResolver.get(paymentRequest);
//
//        if (Objects.isNull(strategy)){
//            throw new UnsupportedPaymentTypeException("Unsupported payment type!");
//        }
//
//        return strategy.pay(paymentRequest);
//    }
//
//}
//
//public class StrategyPatternSpring {
//}

//package org.example.patterns;
//
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.Objects;
//
//// ==========================================
//// 1. DOMAIN SPECIFIC EXCEPTIONS
//// ==========================================
//
//class AccountNotFoundException extends RuntimeException {
//    public AccountNotFoundException(String accountId) {
//        super("Account not found with ID: " + accountId);
//    }
//}
//
//class InsufficientBalanceException extends RuntimeException {
//    public InsufficientBalanceException(String accountId) {
//        super("Insufficient balance in account: " + accountId);
//    }
//}
//
//class FraudDetectedException extends RuntimeException {
//    public FraudDetectedException(String message) {
//        super("Transfer blocked due to fraud risk: " + message);
//    }
//}
//
//class TransferLimitExceededException extends RuntimeException {
//    public TransferLimitExceededException(BigDecimal amount, BigDecimal limit) {
//        super("Transfer amount " + amount + " exceeds current limit of " + limit);
//    }
//}
//
//// ==========================================
//// 2. DOMAIN MODELS & COMMANDS / DTOs
//// ==========================================
//
//record TransferRequest(
//        String fromAccountId,
//        String toAccountId,
//        BigDecimal amount,
//        String currency
//) {}
//
//record TransferCommand(
//        String fromAccountId,
//        String toAccountId,
//        BigDecimal amount,
//        String currency
//) {
//    public TransferCommand {
//        Objects.requireNonNull(fromAccountId, "fromAccountId is required");
//        Objects.requireNonNull(toAccountId, "toAccountId is required");
//        Objects.requireNonNull(amount, "amount is required");
//        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Transfer amount must be positive");
//        }
//    }
//}
//
//record TransferResult(
//        String transactionId,
//        String status,
//        Instant timestamp
//) {}
//
//// ==========================================
//// 3. SPECIALIZED SERVICES (Business Logic)
//// ==========================================
//
//@Service
//class AccountService {
//    public void validateAccount(String accountId) {
//        // Həqiqi tətbiqdə bazadan hesabın varlığı yoxlanılır
//        if ("invalid-acc".equals(accountId)) {
//            throw new AccountNotFoundException(accountId);
//        }
//    }
//}
//
//@Service
//class FraudService {
//    public void checkFraud(TransferCommand command) {
//        // Şübhəli transferlərin yoxlanılması
//        if (command.amount().compareTo(new BigDecimal("100000")) > 0) {
//            throw new FraudDetectedException("Amount exceeds high-risk threshold");
//        }
//    }
//}
//
//@Service
//class TransferLimitService {
//    private static final BigDecimal DAILY_LIMIT = new BigDecimal("50000");
//
//    public void checkLimits(TransferCommand command) {
//        if (command.amount().compareTo(DAILY_LIMIT) > 0) {
//            throw new TransferLimitExceededException(command.amount(), DAILY_LIMIT);
//        }
//    }
//}
//
//@Service
//class BalanceService {
//    /*
//     * MICROSERVICE REFACTORING NOTE (if BalanceService becomes a remote microservice):
//     * 1. Direct DB/Local method call is replaced with an HTTP/gRPC client (e.g., Spring WebClient or OpenFeign).
//     * 2. Local DB @Transactional boundary CANNOT cover remote REST calls (2PC or Saga Pattern needed).
//     * 3. Must implement resilience patterns: Circuit Breaker (Resilience4j), Timeouts, Retries.
//     * 4. Must handle network failures (e.g., RemoteServiceUnavailableException).
//     */
//    public void withdraw(String accountId, BigDecimal amount) {
//        // Balans yoxlanışı və çıxarılması
//        if ("no-money-acc".equals(accountId)) {
//            throw new InsufficientBalanceException(accountId);
//        }
//    }
//
//    public void deposit(String accountId, BigDecimal amount) {
//        // Balansın artırılması
//    }
//}
//
//@Service
//class AuditService {
//    public void logTransfer(TransferCommand command, String transactionId) {
//        // Audit loglarının yazılması (məlumat bazasına və ya fayla)
//    }
//}
//
//@Service
//class NotificationService {
//    /*
//     * MICROSERVICE REFACTORING NOTE (if NotificationService becomes a remote microservice/event listener):
//     * 1. Should execute asynchronously (e.g., using @Async or publishing an integration event to Kafka/RabbitMQ).
//     * 2. Notification failure MUST NOT roll back the main financial transaction (Eventual Consistency).
//     * 3. Execute OUTSIDE the main @Transactional boundary or publish event via Transactional Outbox Pattern.
//     */
//    public void sendNotification(String accountId, String message) {
//        // SMS / Email bildirişinin göndərilməsi
//    }
//}
//
//// ==========================================
//// 4. FACADE LAYER (Workflow Coordinator)
//// ==========================================
//
//@Service
//@RequiredArgsConstructor
//public class TransferFacade {
//
//    private final AccountService accountService;
//    private final FraudService fraudService;
//    private final TransferLimitService transferLimitService;
//    private final BalanceService balanceService;
//    private final AuditService auditService;
//    private final NotificationService notificationService;
//
//    /**
//     * Local transaction boundary covers the core balance transfers and audit logging.
//     * Non-critical side-effects (like notifications) could be async or event-driven.
//     */
//    @Transactional
//    public TransferResult transfer(TransferCommand command) {
//        // 1. Account existence validation
//        accountService.validateAccount(command.fromAccountId());
//        accountService.validateAccount(command.toAccountId());
//
//        // 2. Fraud & Business Limit checks
//        fraudService.checkFraud(command);
//        transferLimitService.checkLimits(command);
//
//        // 3. Execution of state changes (Money Transfer)
//        balanceService.withdraw(command.fromAccountId(), command.amount());
//        balanceService.deposit(command.toAccountId(), command.amount());
//
//        // 4. Audit Logging
//        String txId = "TX-" + System.currentTimeMillis();
//        auditService.logTransfer(command, txId);
//
//        // 5. Side-effect: Notification (Ideally async/event-driven)
//        notificationService.sendNotification(command.fromAccountId(), "Transfer completed successfully. TxID: " + txId);
//
//        return new TransferResult(txId, "SUCCESS", Instant.now());
//    }
//}
//
//// ==========================================
//// 5. CONTROLLER LAYER (HTTP Adapter)
//// ==========================================
//
//@RestController
//@RequestMapping("/api/transfers")
//@RequiredArgsConstructor
//class TransferController {
//
//    // Controller talks ONLY to TransferFacade
//    private final TransferFacade transferFacade;
//
//    @PostMapping
//    public ResponseEntity<TransferResult> transfer(@RequestBody TransferRequest request) {
//        // Map DTO to Immutable Command Object
//        TransferCommand command = new TransferCommand(
//                request.fromAccountId(),
//                request.toAccountId(),
//                request.amount(),
//                request.currency()
//        );
//
//        TransferResult result = transferFacade.transfer(command);
//        return ResponseEntity.status(HttpStatus.CREATED).body(result);
//    }
//}
//
//// Main class container for single-file demonstration
//public class FacadePatternWithSpring {
//}
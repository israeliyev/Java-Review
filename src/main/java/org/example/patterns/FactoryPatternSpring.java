package org.example.patterns;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

enum NotificationType {
    EMAIL,
    SMS,
    PUSH
}

interface NotificationSender {
    void send(String receiver, String message);
    NotificationType supportsType();
}

@Component
@RequiredArgsConstructor
class EmailNotificationSender implements NotificationSender {

    @Override
    public void send(String receiver, String message) {

    }

    @Override
    public NotificationType supportsType() {
        return NotificationType.EMAIL;
    }
}

@Component
@RequiredArgsConstructor
class SmsNotificationSender implements NotificationSender {

    @Override
    public void send(String receiver, String message) {

    }

    @Override
    public NotificationType supportsType() {
        return NotificationType.SMS;
    }
}

@Component
@RequiredArgsConstructor
class PushNotificationSender implements NotificationSender {

    @Override
    public void send(String receiver, String message) {

    }

    @Override
    public NotificationType supportsType() {
        return NotificationType.PUSH;
    }
}

@Component
@RequiredArgsConstructor
class NotificationSenderFactory{

    private static Map<NotificationType, NotificationSender> senders;

    NotificationSenderFactory(List<NotificationSender> notificationSenders) {
      senders = notificationSenders.stream().collect(Collectors.toUnmodifiableMap(NotificationSender::supportsType, Function.identity()));
    }

    NotificationSender get(NotificationType notificationType){
        NotificationSender sender = senders.get(notificationType);

        if(Objects.isNull(sender)){
            throw new RuntimeException("Sender is not found");
        }
        return sender;
    }
}

@Service
@RequiredArgsConstructor
class NotificationServiceForFactoryPattern {
    private final NotificationSenderFactory notificationSenderFactory;

    public void send(NotificationType notificationType, String receiver, String message){
        NotificationSender notificationSender = notificationSenderFactory.get(notificationType);
        notificationSender.send(receiver, message);
    }
}


public class FactoryPatternSpring {
}

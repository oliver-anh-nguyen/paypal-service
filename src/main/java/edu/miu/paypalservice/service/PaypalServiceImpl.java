package edu.miu.paypalservice.service;

import edu.miu.paypalservice.entity.Notification;
import edu.miu.paypalservice.entity.Paypal;
import edu.miu.paypalservice.repository.PaypalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaypalServiceImpl implements PaypalService {

    private final PaypalRepository paypalRepository;

    private final KafkaTemplate<String, Notification> kafkaTemplate;

    @Value("${kafka.topic.notification}")
    private String topicNotification;

    @Override
    public void publish(String topic, Notification payment) {
        ListenableFuture<SendResult<String, Notification>> future = kafkaTemplate.send(topic, payment);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(final SendResult<String, Notification> message) {
                log.info("PAYPAL sent message= " + message + " with offset= " + message.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(final Throwable throwable) {
                log.error("PAYPAL unable to send message= " + payment, throwable);
            }
        });
    }

    @Override
    @KafkaListener(id = "paymentId", topics = "${kafka.topic.paypal}")
    public void listenPayment(Paypal paypal) {
        log.info("PAYPAL received info from paypal topic: " + paypal);
        if (paypal != null) {
            save(paypal);
            String body = "Successfully! Thank you for your payment! Property ID: " + paypal.getPropertyId();
            Notification notificationRequest = new Notification(paypal.getEmail(), "PAYPAL Notification",  body);
            publish(topicNotification, notificationRequest);
        }
    }
    @Transactional
    public void save(Paypal paypal) {
        paypal.setId(UUID.randomUUID());
        paypalRepository.save(paypal);
    }

    public Paypal getById(UUID id) {
        return paypalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find transaction paypal with id: " + id));
    }
}

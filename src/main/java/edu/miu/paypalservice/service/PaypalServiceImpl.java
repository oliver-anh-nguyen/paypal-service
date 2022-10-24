package edu.miu.paypalservice.service;

import edu.miu.paypalservice.entity.Paypal;
import edu.miu.paypalservice.repository.PaypalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaypalServiceImpl implements PaypalService {

    private final PaypalRepository paypalRepository;

    private final KafkaTemplate<String, Paypal> kafkaTemplate;

    @Override
    public void publish(String topic, Paypal message) {
        kafkaTemplate.send(topic, message);
    }

    @Override
    @KafkaListener(id = "paymentId", topics = "${kafka.topicPaypal}")
    public void listenPayment(Paypal paypal) {
        System.out.println("Received info from paypal topic: " + paypal);
        if (paypal != null) {
            save(paypal);
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

package edu.miu.paypalservice.service;

import edu.miu.paypalservice.entity.Paypal;

import java.util.UUID;

public interface PaypalService {
    void publish(String topic, Paypal message);

    void listenPayment(Paypal message);
}

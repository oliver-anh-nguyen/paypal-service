package edu.miu.paypalservice.service;

import edu.miu.paypalservice.entity.Notification;
import edu.miu.paypalservice.entity.Paypal;

import java.util.UUID;

public interface PaypalService {
    void publish(String topic, Notification message);

    void listenPayment(Paypal message);
}

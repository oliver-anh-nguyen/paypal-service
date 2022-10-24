package edu.miu.paypalservice.service;

import edu.miu.paypalservice.entity.Paypal;
import edu.miu.paypalservice.repository.PaypalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaypalServiceImplTest {
    @InjectMocks
    private PaypalServiceImpl paypalService;

    @Mock
    private PaypalRepository paypalRepository;

    @Test
    void should_save_paypal_transaction() {
        UUID paypalId = UUID.randomUUID();
        UUID propertyId = UUID.randomUUID();
        Paypal paypal = new Paypal(paypalId, "PAYPAL", "anh@gmail.com", 500.0, 2, propertyId);
        paypalService.save(paypal);
        assertThat(paypal.getTotal()).isEqualTo(500);
        assertThat(paypal.getId()).isNotNull();
    }

    @Test
    void should_return_paypal_transaction() {
        UUID transactionId = UUID.randomUUID();
        UUID propertyId = UUID.randomUUID();
        Paypal expectedReservation = new Paypal(transactionId, "PAYPAL", "anh@gmail.com", 500.0, 2, propertyId);
        when(paypalRepository.findById(transactionId)).thenReturn(Optional.of(expectedReservation));
        assertThat(paypalService.getById(transactionId)).isEqualTo(expectedReservation);

    }

    @Test
    void should_throw_paypal_exception() {
        UUID transactionId = UUID.randomUUID();
        when(paypalRepository.findById(transactionId)).thenReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> paypalService.getById(transactionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Cannot find transaction paypal with id: " + transactionId);

    }
}

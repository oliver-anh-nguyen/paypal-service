package edu.miu.paypalservice.repository;

import edu.miu.paypalservice.entity.Paypal;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaypalRepository extends CassandraRepository<Paypal, UUID> {

}

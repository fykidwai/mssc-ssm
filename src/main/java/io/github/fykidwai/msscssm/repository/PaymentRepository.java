package io.github.fykidwai.msscssm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.fykidwai.msscssm.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}

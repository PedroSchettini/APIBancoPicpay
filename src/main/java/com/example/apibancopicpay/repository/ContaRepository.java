package com.example.apibancopicpay.repository;

import com.example.apibancopicpay.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ContaRepository extends JpaRepository<Conta, String> {
    Conta findByNumeroConta(String numeroConta);
}

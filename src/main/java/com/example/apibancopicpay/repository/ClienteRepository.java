package com.example.apibancopicpay.repository;

import com.example.apibancopicpay.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
}

package com.example.apibancopicpay.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Conta {
    @Id
    @NotNull(message = "O número da conta não pode ser nulo!")
    @Size(max = 5, message = "O número da conta deve ter no máximo 5 digitos!")
    @Column(name = "numero_conta")
    private String numeroConta;
    @NotNull(message = "O saldo não pode ser nulo!")
//    @Digits(integer = 10, fraction = 2, message = "O saldo deve ter 10 digitos e 2 casas decimais!")
    private double saldo;
    @NotNull(message = "O limite especial não pode ser nulo!")
//    @Digits(integer = 10, fraction = 2, message = "O limite especial deve ter 10 digitos e 2 casas decimais!")
    @Column(name = "limite_especial")
    private double limiteEspecial;
    @NotNull(message = "O cpf não pode ser nulo!")
    @CPF(message = "O cpf informado é inválido!")
    @Column(name = "cliente_cpf")
    private String clienteCpf;

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getLimiteEspecial() {
        return limiteEspecial;
    }

    public void setLimiteEspecial(double limiteEspecial) {
        this.limiteEspecial = limiteEspecial;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "numeroConta='" + numeroConta + '\'' +
                ", saldo=" + saldo +
                ", limiteEspecial=" + limiteEspecial +
                ", clienteCpf='" + clienteCpf + '\'' +
                '}';
    }
}

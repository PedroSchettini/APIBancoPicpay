package com.example.apibancopicpay.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Cliente {
    @NotNull(message = "O cpf não pode ser nulo!")
    @CPF(message = "O cpf informado é inválido!")
    @Id
    private String cpf;
    @NotNull(message = "O nome não pode ser nulo!")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 letras!")
    private String nome;
    @NotNull(message = "O email não pode ser nulo!")
    @Email(message = "O email informado é inválido!")
    private String email;
    @NotNull(message = "O telefone não pode ser nulo!")
    @Size(max = 15, message = "O telefone deve ter pelo menos 6 caracteres e máximo de 15 caracteres!")
    private String telefone;

    public Cliente() {
    }

    public Cliente(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                ", cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}

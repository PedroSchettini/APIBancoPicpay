package com.example.apibancopicpay.services;

import com.example.apibancopicpay.models.Cliente;
import com.example.apibancopicpay.models.Conta;
import com.example.apibancopicpay.repository.ContaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final Validator validador;

    public ContaService(ContaRepository contaRepository, Validator validador) {
        this.contaRepository = contaRepository;
        this.validador = validador;
    }

    public Conta buscarContaPorNumero(String numeroConta) {
        return contaRepository.findByNumeroConta(numeroConta);
    }
    public List<Conta> buscarTodasContas() { return contaRepository.findAll(); }

    @Transactional
    public Conta salvarConta(Conta conta) { return contaRepository.save(conta); }

    @Transactional
    public Conta deletarConta(String numeroConta){
        Conta contaDeletada = buscarContaPorNumero(numeroConta);
        contaRepository.deleteById(numeroConta);
        return contaDeletada;
    }

    @Transactional
    public ResponseEntity<String> depositar(String numeroConta, double valor) {
        Conta conta = buscarContaPorNumero(numeroConta);
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);
        return ResponseEntity.ok("Depósito realizado com sucesso!");
    }

    @Transactional
    public ResponseEntity<String> sacar(String numeroConta, double valor){
        if (verificarFinalDeSemana() && valor > 1000) {
            return ResponseEntity.ok("Não pode sacar mais de R$ 1000,00 aos finais de semana!");
        }
        Conta conta = buscarContaPorNumero(numeroConta);
        if(valor > conta.getSaldo() + conta.getLimiteEspecial()){
            return ResponseEntity.ok("Saldo insuficiente!");
        }
        conta.setSaldo(conta.getSaldo() - valor);
        contaRepository.save(conta);
        return ResponseEntity.ok("Saque realizado com sucesso!");
    }

    @Transactional
    public ResponseEntity<String> transferir(String numContaManda, String numContaRecebe, double valor, int tipoTransferencia) {
        LocalTime inicioIntervalo = LocalTime.of(8, 0);
        LocalTime fimIntervalo = LocalTime.of(17, 0);
        LocalTime horaAtual = LocalTime.now();
        if(tipoTransferencia == 2 && !verificarFinalDeSemana() &&
                (horaAtual.isAfter(inicioIntervalo) && horaAtual.isBefore(fimIntervalo))){
            Conta contaManda = buscarContaPorNumero(numContaManda);
            Conta contaRecebe = buscarContaPorNumero(numContaRecebe);
            contaManda.setSaldo(contaManda.getSaldo() - valor);
            contaRecebe.setSaldo(contaRecebe.getSaldo() + valor);
            contaRepository.save(contaManda);
            contaRepository.save(contaRecebe);
            return ResponseEntity.ok("Transferência realizada com sucesso!");
        }
        else if(tipoTransferencia == 1){
            Conta contaManda = buscarContaPorNumero(numContaManda);
            Conta contaRecebe = buscarContaPorNumero(numContaRecebe);
            contaManda.setSaldo(contaManda.getSaldo() - valor);
            contaRecebe.setSaldo(contaRecebe.getSaldo() + valor);
            contaRepository.save(contaManda);
            contaRepository.save(contaRecebe);
            return ResponseEntity.ok("Transferência realizada com sucesso!");
        }
        else{
            return ResponseEntity.ok("Tipo de transferência inválido!");
        }
    }

    public static boolean verificarFinalDeSemana(){
        Calendar data = Calendar.getInstance();
        if(data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || data.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return true;
        }
        return false;
    }
}

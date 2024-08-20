package com.example.apibancopicpay.controllers;

import com.example.apibancopicpay.models.Cliente;
import com.example.apibancopicpay.models.Conta;
import com.example.apibancopicpay.services.ContaService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/conta")
public class ContaController {
    private final ContaService contaService;
    private final Validator validador;

    public ContaController(ContaService contaService, Validator validador) {
        this.contaService = contaService;
        this.validador = validador;
    }

    @GetMapping("/listarContas")
    public List<Conta> listarTodasContas() {
        return contaService.buscarTodasContas();
    }

    @GetMapping("/buscarContaPorNumero/{numeroConta}")
    public Conta buscarContaPorNumero(@PathVariable String numeroConta) {
        return contaService.buscarContaPorNumero(numeroConta);
    }

    @PostMapping("/salvarConta")
    public ResponseEntity salvarCliente(@Valid @RequestBody Conta conta, BindingResult result) {
        if(result.hasErrors()){
            StringBuilder sb = new StringBuilder("Erro ao salvar cliente");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try{
            contaService.salvarConta(conta);
            return ResponseEntity.ok("Cliente salvo com sucesso");
        }catch (DataIntegrityViolationException dive){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível adicionar o cliente");
        }
    }

    @DeleteMapping("/deletarContaPorNumero/{numeroConta}")
    public ResponseEntity<String> deletarContaPorId(@PathVariable String numeroConta){
        Conta contaExcluida = contaService.deletarConta(numeroConta);
        if(contaExcluida != null){
            return ResponseEntity.ok("Conta deletada com sucesso!");
        }
        return ResponseEntity.status(404).body("Conta não encontrado!");
    }

    @PatchMapping("/depositar/{numeroConta}")
    public ResponseEntity<String> depositar(@PathVariable String numeroConta, @RequestBody double valor){
        return contaService.depositar(numeroConta, valor);
    }

    @PatchMapping("/sacar/{numeroConta}")
    public ResponseEntity<String> sacar(@PathVariable String numeroConta, @RequestBody double valor){
        return contaService.sacar(numeroConta, valor);
    }

    @PatchMapping("/transferir")
    public ResponseEntity<String> sacar(@RequestBody Map<String, Object> dados){
        String numContaManda = dados.get("numContaManda").toString();
        String numContaRecebe = dados.get("numContaRecebe").toString();
        double valor = Double.parseDouble(dados.get("valor").toString());
        int tipoTransferencia = Integer.parseInt(dados.get("tipoTransferencia").toString());
        return contaService.transferir(numContaManda, numContaRecebe, valor, tipoTransferencia);
    }
}

package com.example.apibancopicpay.controllers;

import com.example.apibancopicpay.models.Cliente;
import com.example.apibancopicpay.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    private final ClienteService clienteService;
    private final Validator validador;

    public ClienteController(ClienteService clienteService, Validator validador) {
        this.clienteService = clienteService;
        this.validador = validador;
    }

    @GetMapping("/listarClientes")
    public List<Cliente> listarTodosClientes() {
        return clienteService.buscarTodosClientes();
    }

    @GetMapping("/buscarPorCpf/{cpf}")
    public Cliente buscarClientePorCpf(@PathVariable String cpf) {
        return clienteService.buscarClientePorCpf(cpf);
    }

    @PostMapping("/salvarCliente")
    public ResponseEntity salvarCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
        if(result.hasErrors()){
            StringBuilder sb = new StringBuilder("Erro ao salvar cliente");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try{
            clienteService.salvarCliente(cliente);
            return ResponseEntity.ok("Cliente salvo com sucesso");
        }catch (DataIntegrityViolationException dive){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível adicionar o cliente");
        }
    }

    @DeleteMapping("/deletarPorCpf/{cpf}")
    public ResponseEntity<String> deletarPorCpf(@PathVariable String cpf) {
        Cliente clienteExcluido = clienteService.deletarClientePorCpf(cpf);
        if(clienteExcluido != null){
            return ResponseEntity.ok("Cliente deletado com sucesso");
        }
        else{
            return ResponseEntity.status(404).body("Cliente não encontrado");
        }
    }

    @PutMapping("/atualizarCliente/{cpf}")
    public ResponseEntity<String> atualizarCliente(@PathVariable String cpf,
                                                   @Valid @RequestBody Cliente clienteAtualizado,
                                                   BindingResult result ) {
        if (result.hasErrors()){
            StringBuilder sb = new StringBuilder("Erro ao atualizar o cliente");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try {
            clienteService.atualizarClientePorCpf(cpf, clienteAtualizado);
            return ResponseEntity.ok("Cliente atualizado com sucesso");
        }catch (DataIntegrityViolationException dive){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível atualizar o cliente");
        }
    }

    @PatchMapping("/atualizarClienteParcialmente/{cpf}")
    public ResponseEntity<String> atualizarClienteParcialmente(@PathVariable String cpf,
                                                               @RequestBody Map<String, Object> dados){
        ResponseEntity<String> response = clienteService.atualizarClienteParcialmente(cpf, dados);
        return response;
    }
}

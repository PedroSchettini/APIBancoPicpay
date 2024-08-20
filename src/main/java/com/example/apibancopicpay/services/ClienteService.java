package com.example.apibancopicpay.services;

import com.example.apibancopicpay.models.Cliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.apibancopicpay.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final Validator validador;

    public ClienteService(ClienteRepository clienteRepository, Validator validador) {
        this.clienteRepository = clienteRepository;
        this.validador = validador;
    }

    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClientePorCpf(String cpf){
        return clienteRepository.findById(cpf).orElseThrow(() ->
                new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente){
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente deletarClientePorCpf(String cpf){
        Cliente cliente = buscarClientePorCpf(cpf);
        clienteRepository.deleteById(cpf);
        return cliente;
    }

    public Cliente atualizarClientePorCpf(String cpf, Cliente clienteAtualizado){
        Cliente cliente = buscarClientePorCpf(cpf);
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        return clienteRepository.save(cliente);
    }

    public ResponseEntity<String> atualizarClienteParcialmente(String cpf, Map<String, Object> dados){
        Cliente cliente = buscarClientePorCpf(cpf);
        if(dados.containsKey("cpf")){
            cliente.setCpf((String) dados.get("cpf"));
        }
        if (dados.containsKey("nome")){
            cliente.setNome((String) dados.get("nome"));
        }
        if(dados.containsKey("email")){
            cliente.setEmail((String) dados.get("email"));
        }
        if(dados.containsKey("telefone")){
            cliente.setTelefone((String) dados.get("telefone"));
        }
        DataBinder binder = new DataBinder(cliente);
        binder.setValidator(validador);
        binder.validate();
        BindingResult result = binder.getBindingResult();
        if(result.hasErrors()){
            StringBuilder sb = new StringBuilder("Erro ao atualizar o cliente");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        clienteRepository.save(cliente);
        return ResponseEntity.ok("Cliente atualizado com sucesso");
    }
}

package com.ProjetoPI.BaberShop.Service.impl;

import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Repository.ClienteRepository;
import com.ProjetoPI.BaberShop.Service.ClienteService;
import com.ProjetoPI.BaberShop.exception.ErroAutenticacao;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente autenticar(String email, String senha,String statusPerfil) {
        Optional<Cliente>cliente =clienteRepository.findByEmail(email);
        String statusValido = "ATIVO";
        if (!cliente.isPresent()){
            throw new ErroAutenticacao("Usuario não encontrado para o email encontrado");
        }
        if (!cliente.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha Incorreta");
        }
        if (!cliente.get().getStatusPerfil().equals(statusValido)){
            throw new ErroAutenticacao("USUARIO INATIVO");
        }

        return cliente.get();
    }

    @Override
    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
     validarEmail(cliente.getEmail());
     validarCPF(cliente.getCpf());
     return clienteRepository.save(cliente);

    }

    @Override
    @Transactional
    public Cliente atualizar(Cliente cliente) {
        Objects.requireNonNull(cliente.getId());
        return clienteRepository.save(cliente);    }

    @Override
    public void validarEmail(String email) {
        boolean existe = clienteRepository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Ja existe um usuario cadastrado com esse email");
        }
    }
    @Override
    public void validarCPF(String cpf) {
        boolean existe = clienteRepository.existsByCpf(cpf);
        if (existe){
            throw new RegraNegocioException("Já existe um usuário cadastrado com esse CPF");
        }
    }

    @Override
    @Transactional
    public List<Cliente> buscar(Cliente clienteFiltro) {
        Example example = Example.of(clienteFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return clienteRepository.findAll(example);    }

    @Override
    public Optional<Cliente> obterPorId(Long id) {
        return clienteRepository.findById(id);
    }

}

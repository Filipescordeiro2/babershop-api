package com.ProjetoPI.BaberShop.Service.impl;

import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Repository.ClienteRepository;
import com.ProjetoPI.BaberShop.Service.ClienteService;
import com.ProjetoPI.BaberShop.exception.ErroAutenticacao;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente autenticar(String email, String senha) {
        Optional<Cliente>cliente =clienteRepository.findByEmail(email);

        if (!cliente.isPresent()){
            throw new ErroAutenticacao("Usuario não encontrado para o email encontrado");
        }
        if (!cliente.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha Incorreta");
        }

        return cliente.get();
    }

    @Override
    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
     validarEmail(cliente.getEmail());
     return clienteRepository.save(cliente);

    }

    @Override
    public void validarEmail(String email) {
        boolean existe = clienteRepository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Ja existe um usuario cadastrado com esse email");
        }
    }

    @Override
    public Optional<Cliente> obterPorId(Long id) {
        return clienteRepository.findById(id);
    }

}

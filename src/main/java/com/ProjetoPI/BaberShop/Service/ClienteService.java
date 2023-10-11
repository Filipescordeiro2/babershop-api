package com.ProjetoPI.BaberShop.Service;

import com.ProjetoPI.BaberShop.Model.Cliente;

import java.util.Optional;

public interface ClienteService {

    Cliente autenticar (String email, String senha);
    Cliente salvarCliente (Cliente cliente);

    void validarEmail (String email);

    Optional<Cliente> obterPorId(Long id);
}

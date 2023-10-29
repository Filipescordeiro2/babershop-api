package com.ProjetoPI.BaberShop.Service;

import com.ProjetoPI.BaberShop.Model.Agendamento;
import com.ProjetoPI.BaberShop.Model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente autenticar (String email, String senha,String statusPerfil);
    Cliente salvarCliente (Cliente cliente);
    Cliente atualizar(Cliente cliente);

    void validarEmail (String email);
    List<Cliente> buscar (Cliente clienteFiltro);

    Optional<Cliente> obterPorId(Long id);
}

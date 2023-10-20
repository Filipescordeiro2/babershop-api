package com.ProjetoPI.BaberShop.Service;

import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Model.Profissional;

import java.util.List;
import java.util.Optional;

public interface ProfissionalService {

    Profissional autenticar (String email, String senha);
    Profissional salvarProfissional (Profissional profissional);

    void validarEmail (String email);
    List<Profissional> buscar (Profissional profissionalFiltro);

    Optional<Profissional> obterPorId(Long id);
}

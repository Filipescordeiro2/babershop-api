package com.ProjetoPI.BaberShop.Service;

import com.ProjetoPI.BaberShop.Model.Profissional;

import java.util.Optional;

public interface ProfissionalService {

    Profissional autenticar (String email, String senha);
    Profissional salvarProfissional (Profissional profissional);

    void validarEmail (String email);

    Optional<Profissional> obterPorId(Long id);
}

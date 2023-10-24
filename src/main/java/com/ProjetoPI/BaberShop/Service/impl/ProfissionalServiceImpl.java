package com.ProjetoPI.BaberShop.Service.impl;

import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Model.Profissional;
import com.ProjetoPI.BaberShop.Repository.ProfissionalRepository;
import com.ProjetoPI.BaberShop.Service.ProfissionalService;
import com.ProjetoPI.BaberShop.exception.ErroAutenticacao;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalServiceImpl implements ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    public Profissional autenticar(String email, String senha) {
        Optional<Profissional>profissional = profissionalRepository.findByEmail(email);

        if (!profissional.isPresent()){
            throw new ErroAutenticacao("Usuario não encontrado para o email encontrado");
        }
        if (!profissional.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha Incorreta");
        }

        return profissional.get();
    }

    @Override
    @Transactional
    public Profissional salvarProfissional(Profissional profissional) {
     validarEmail(profissional.getEmail());
     return profissionalRepository.save(profissional);

    }

    @Override
    public void validarEmail(String email) {
        boolean existe = profissionalRepository.existsByEmail(email);
        if (existe){
            throw new RegraNegocioException("Ja existe um usuario cadastrado com esse email");
        }
    }

    @Override
    @Transactional
    public List<Profissional> buscar(Profissional profissionalFiltro) {
        Example example = Example.of(profissionalFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return profissionalRepository.findAll(example);    }

    @Override
    public Optional<Profissional> obterPorId(Long id) {
        return profissionalRepository.findById(id);

    }
    @Override
    public List<Profissional>findByNome(String nome) {
        return profissionalRepository.findByNome(nome);
    }


}

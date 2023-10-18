package com.ProjetoPI.BaberShop.Service.impl;


import com.ProjetoPI.BaberShop.Model.Horario;
import com.ProjetoPI.BaberShop.Model.JornadaDeTrabalho;
import com.ProjetoPI.BaberShop.Repository.HorarioRepository;
import com.ProjetoPI.BaberShop.Repository.JornadaDeTrabalhoRepository;
import com.ProjetoPI.BaberShop.Service.JornadaDeTrabalhoService;
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
public class JornadaDeTrabalhoServiceImp implements JornadaDeTrabalhoService {


    @Autowired
    private JornadaDeTrabalhoRepository jornadaDeTrabalhoRepository;
    @Autowired
    private HorarioRepository horarioRepository;


    @Override
    @Transactional
    public JornadaDeTrabalho Salvar(JornadaDeTrabalho jornadaDeTrabalho) {
       validar(jornadaDeTrabalho);
        return jornadaDeTrabalhoRepository.save(jornadaDeTrabalho);
    }

    @Override
    public void validar(JornadaDeTrabalho jornadaDeTrabalho) {

        if (jornadaDeTrabalho.getPeriodo()==null){
            throw new RegraNegocioException("Jornada de trabalho não foi preenchida");
        }
        if (jornadaDeTrabalho.getData()==null  || jornadaDeTrabalho.getData().trim().equals("")){
            throw new RegraNegocioException("Necessario escolher o dia do atendimento");
        }
        if (jornadaDeTrabalho.getIntervalo()==null  || jornadaDeTrabalho.getIntervalo().trim().equals("")){
            throw new RegraNegocioException("Necessario escolher um Espaço entre os horarios");
        }
        if(jornadaDeTrabalho.getProfissional()==null || jornadaDeTrabalho.getProfissional().getId()==null){
            throw new RegraNegocioException("Profissioanal não identificado");
        }
    }

    @Override
    @Transactional
    public void deletarJornada(JornadaDeTrabalho jornadaDeTrabalho) {
        Objects.requireNonNull(jornadaDeTrabalho.getId());
        jornadaDeTrabalhoRepository.delete(jornadaDeTrabalho);
    }

    @Override
    @Transactional
    public void deletarHorario(Horario horario) {
        Objects.requireNonNull(horario.getId());
        horarioRepository.delete(horario);

    }

    @Override
    public Optional<JornadaDeTrabalho> obterPorId(Long id) {

        return jornadaDeTrabalhoRepository.findById(id);
    }

    @Override
    public Optional<Horario> obterHorarioPorId(Long id) {
        return horarioRepository.findById(id);
    }

    @Override
    @Transactional
    public List<JornadaDeTrabalho> buscarJornada(JornadaDeTrabalho jornadaFiltro) {
        Example example = Example.of(jornadaFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return jornadaDeTrabalhoRepository.findAll(example);
    }

    @Override
    @Transactional
    public List<Horario> buscarHorario(Horario horarioFiltro) {
        Example example = Example.of(horarioFiltro,ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return horarioRepository.findAll(example);
    }

    @Override
    @Transactional
    public JornadaDeTrabalho atualizar(JornadaDeTrabalho jornadaDeTrabalho) {
        Objects.requireNonNull(jornadaDeTrabalho.getId());
        validar(jornadaDeTrabalho);
        return jornadaDeTrabalhoRepository.save(jornadaDeTrabalho);
    }
}

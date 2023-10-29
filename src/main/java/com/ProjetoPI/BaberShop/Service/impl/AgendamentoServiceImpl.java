package com.ProjetoPI.BaberShop.Service.impl;

import com.ProjetoPI.BaberShop.Enums.StatusAgendamento;
import com.ProjetoPI.BaberShop.Enums.TiposDeAgendamentos;
import com.ProjetoPI.BaberShop.Model.Agendamento;
import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Model.Horario;
import com.ProjetoPI.BaberShop.Repository.AgendamentoRepository;
import com.ProjetoPI.BaberShop.Service.AgendamentoService;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;


    @Override
    @Transactional
    public Agendamento salvar(Agendamento agendamento) {
        validar(agendamento);
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);
        return repository.save(agendamento);
    }

    @Override
    @Transactional
    public Agendamento atualizar(Agendamento agendamento) {
        Objects.requireNonNull(agendamento.getId());
        validar(agendamento);
        return repository.save(agendamento);
    }

    @Override
    @Transactional
    public void deletar(Agendamento agendamento) {
    Objects.requireNonNull(agendamento.getId());
    repository.delete(agendamento);

    }

    @Override
    @Transactional
    public List<Agendamento> buscar(Agendamento agendamentoFiltro) {
        Example example = Example.of(agendamentoFiltro,
                ExampleMatcher.matching()
                        .withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example);
    }

    @Override
    public void atualizarStatus(Agendamento agendamento, StatusAgendamento status) {
      agendamento.setStatusAgendamento(status);
      atualizar(agendamento);
    }

    @Override
    public void validar(Agendamento agendamento) {
        TiposDeAgendamentos tipo1 = TiposDeAgendamentos.TIPO_1_CORTE_BARBA_SOBRANCELHA;
        TiposDeAgendamentos tipo2 = TiposDeAgendamentos.TIPO_2_CORTE_SOBRANCELHA;
        TiposDeAgendamentos tipo3 = TiposDeAgendamentos.TIPO_3_CORTE_BARBA;
        TiposDeAgendamentos tipo4 = TiposDeAgendamentos.TIPO_4_CORTE_PENTEADO;
        TiposDeAgendamentos tipo5 = TiposDeAgendamentos.TIPO_5_BARBA;
        TiposDeAgendamentos tipo6 = TiposDeAgendamentos.TIPO_6_CORTE;


        if(agendamento.getTiposDeAgendamentos()==null){
            throw new RegraNegocioException("Informe um tipo de agendamento");
        }
        if (agendamento.getTiposDeAgendamentos()==tipo1){
            agendamento.setDescricaoAgendamento("Corte,barba e sobrancelha");
        }
        if (agendamento.getTiposDeAgendamentos()==tipo2){
            agendamento.setDescricaoAgendamento("Corte + Sobrancelha");
        }
        if (agendamento.getTiposDeAgendamentos()==tipo3){
            agendamento.setDescricaoAgendamento("Corte + Barba");
        }
        if (agendamento.getTiposDeAgendamentos()==tipo4){
            agendamento.setDescricaoAgendamento("Corte + Penteado");
        }
        if (agendamento.getTiposDeAgendamentos()==tipo5){
            agendamento.setDescricaoAgendamento("Barba");
        }
        if (agendamento.getTiposDeAgendamentos()==tipo6){
            agendamento.setDescricaoAgendamento("Corte");
        }

        if(agendamento.getProfissional()==null || agendamento.getProfissional().getId()==null){
            throw new RegraNegocioException("Informe um Profissional");
        }
        if(agendamento.getCliente()==null || agendamento.getCliente().getId()==null){
            throw new RegraNegocioException("Cliente não identificado");
        }
        if (agendamento.getHorario()==null || agendamento.getHorario().getId()  ==null ){
            throw new RegraNegocioException("Horario não encontrado");
        }

    }

    @Override
    public Optional<Agendamento> obterPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Agendamento> obterAgendamentosDoClienteParaData(Cliente cliente, String dataAgendamento) {

        return repository.findByClienteAndData(cliente, dataAgendamento);
    }

}

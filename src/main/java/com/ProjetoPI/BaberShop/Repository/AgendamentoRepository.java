package com.ProjetoPI.BaberShop.Repository;

import com.ProjetoPI.BaberShop.Model.Agendamento;
import com.ProjetoPI.BaberShop.Model.Cliente;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository <Agendamento,Long> {


    List<Agendamento> findByClienteAndData(Cliente cliente, String dataAgendamento);

}

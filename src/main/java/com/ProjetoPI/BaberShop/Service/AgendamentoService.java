package com.ProjetoPI.BaberShop.Service;

import com.ProjetoPI.BaberShop.Enums.StatusAgendamento;
import com.ProjetoPI.BaberShop.Model.Agendamento;
import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Model.Horario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AgendamentoService {


     Agendamento salvar(Agendamento agendamento);
     Agendamento atualizar(Agendamento agendamento);
     void deletar(Agendamento agendamento);
     List<Agendamento> buscar (Agendamento agendamentoFiltro);
     void atualizarStatus (Agendamento agendamento, StatusAgendamento status);
     void validar (Agendamento agendamento);
     Optional<Agendamento> obterPorId (Long id);
     List<Agendamento>obterAgendamentosDoClienteParaData(Cliente cliente,String dataAgendamento);

}

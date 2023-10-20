package com.ProjetoPI.BaberShop.Service;

import com.ProjetoPI.BaberShop.Model.Agendamento;
import com.ProjetoPI.BaberShop.Model.Horario;
import com.ProjetoPI.BaberShop.Model.JornadaDeTrabalho;

import java.util.List;
import java.util.Optional;

public interface JornadaDeTrabalhoService {

    JornadaDeTrabalho Salvar (JornadaDeTrabalho jornadaDeTrabalho);
    void validar(JornadaDeTrabalho jornadaDeTrabalho);
    void deletarJornada(JornadaDeTrabalho jornadaDeTrabalho);
    void deletarHorario(Horario horario);
    Optional<JornadaDeTrabalho> obterPorId (Long id);
    Optional<Horario>obterHorarioPorId(Long id);
    List<JornadaDeTrabalho> buscarJornada (JornadaDeTrabalho jornadaFiltro);
    List<Horario> buscarHorario (Horario horarioFiltro);

    JornadaDeTrabalho atualizar(JornadaDeTrabalho jornadaDeTrabalho);


}

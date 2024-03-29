package com.ProjetoPI.BaberShop.Controller;

import com.ProjetoPI.BaberShop.DTO.AgendamentoDTO;
import com.ProjetoPI.BaberShop.DTO.AtualizaStatusDTO;
import com.ProjetoPI.BaberShop.Enums.StatusAgendamento;
import com.ProjetoPI.BaberShop.Enums.TiposDeAgendamentos;
import com.ProjetoPI.BaberShop.Model.*;
import com.ProjetoPI.BaberShop.Service.AgendamentoService;
import com.ProjetoPI.BaberShop.Service.ClienteService;
import com.ProjetoPI.BaberShop.Service.JornadaDeTrabalhoService;
import com.ProjetoPI.BaberShop.Service.ProfissionalService;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProfissionalService profissionalService;
    @Autowired
    private JornadaDeTrabalhoService jornadaDeTrabalhoService;

    @PostMapping
    public ResponseEntity salvar(@RequestBody AgendamentoDTO dto) {
        try {
            // Converter DTO para entidade Agendamento
            Agendamento entidade = converter(dto);

            // Verifique se o cliente já possui um agendamento para a data escolhida
               String dataAgendamento = dto.getData();
               Cliente cliente = entidade.getCliente();

               if (clienteJaPossuiAgendamento(cliente, dataAgendamento)) {
                // Cliente já possui um agendamento para a data escolhida
                return ResponseEntity.badRequest().body("O cliente já possui um agendamento para a data escolhida.");
            }
    

            // Se não houver conflito, salvar o agendamento
            entidade = service.salvar(entidade);
    
            return new ResponseEntity(entidade, HttpStatus.CREATED);

        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public boolean clienteJaPossuiAgendamento(Cliente cliente, String dataAgendamento) {
        // Implemente a lógica para verificar se o cliente já possui um agendamento para a data
        // Certifique-se de incluir a verificação do status diferente de "CANCELADO".
        // Pode ser feita uma consulta ao banco de dados para verificar os agendamentos existentes
        // para o cliente na data especificada com um status diferente de "CANCELADO".
        
        List<Agendamento> agendamentos = service.obterAgendamentosDoClienteParaData(cliente, dataAgendamento);
        
        // Verifique se há agendamentos com status diferente de "CANCELADO"
        for (Agendamento agendamento : agendamentos) {
            if (!agendamento.getStatusAgendamento().equals(StatusAgendamento.CANCELADO)) {
                return true; // Cliente já possui um agendamento com status diferente de "CANCELADO"
            }
        }
        
        return false; // Cliente não possui agendamentos com status diferente de "CANCELADO"
    }
    

  
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id")Long id,@RequestBody AgendamentoDTO dto){
        return service.obterPorId(id).map(entity ->{

            try {
                Agendamento agendamento = converter(dto);
                agendamento.setId(entity.getId());
                service.atualizar(agendamento);
                return ResponseEntity.ok(agendamento);
            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(()-> new ResponseEntity("Lancamento não encontrado no banco de dados", HttpStatus.BAD_REQUEST));
    }


    @DeleteMapping("{id}")
    public ResponseEntity deletar (@PathVariable("id") Long id){
        return service.obterPorId(id).map(entidade ->{
            service.deletar(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(()-> new ResponseEntity("Lancamento não encontrado no banco de dados",HttpStatus.BAD_REQUEST));
    }

    @PutMapping("{id}/atualiza-status")
    public ResponseEntity atualizaStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto){
        return service.obterPorId(id).map(entity->{
            StatusAgendamento statusSelecionado = StatusAgendamento.valueOf(dto.getStatus());
            if (statusSelecionado==null){
                return ResponseEntity.badRequest().body("Não foi possivel atualizar o status do agendamento, envie um status valido");
            }try {
                entity.setStatusAgendamento(statusSelecionado);
                service.atualizar(entity);
                return ResponseEntity.ok(entity);
            } catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(()-> new ResponseEntity("Agendamento não encontrado na base de dados",HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "status",required = false)String status,
                                 @RequestParam(value = "data",required = false) String dataAgendamento,
                                 @RequestParam(value = "hora",required = false)String horaAgendamento,
                                 @RequestParam(value = "idCliente")Long idCliente){

        Agendamento agendamentoFiltro = new Agendamento();
        agendamentoFiltro.setStatusAgendamento(StatusAgendamento.valueOf(status));
        agendamentoFiltro.setData(dataAgendamento);


        Optional<Cliente>cliente=clienteService.obterPorId(idCliente);

        if (!cliente.isPresent()){
            return ResponseEntity.badRequest().body("não foi possivel realizar a consulta");
        }else {
            agendamentoFiltro.setCliente(cliente.get());
        }

        List<Agendamento>agendamentos=service.buscar(agendamentoFiltro);
        return ResponseEntity.ok(agendamentos);
    }

    private Agendamento converter(AgendamentoDTO dto){
        Agendamento agendamento = new Agendamento();
        agendamento.setId(dto.getId());
        agendamento.setDescricaoAgendamento(dto.getDescricaoAgendamento());
        agendamento.setData(dto.getData());

        Cliente cliente = clienteService
                .obterPorId(dto.getId_cliente())
                .orElseThrow(()-> new RegraNegocioException("Cliente não encontrado"));
        Profissional profissional = profissionalService
                .obterPorId(dto.getId_profissional())
                .orElseThrow(()-> new RegraNegocioException("Profissional não encontrado"));
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);

        Horario horario = jornadaDeTrabalhoService.obterHorarioPorId(dto.getId_horario())
                .orElseThrow(()->new RegraNegocioException("horario não encontrado"));
        agendamento.setHorario(horario);

        if (dto.getTiposDeAgendamentos()!=null){
            agendamento.setTiposDeAgendamentos(TiposDeAgendamentos.valueOf(dto.getTiposDeAgendamentos()));
        }
        if (dto.getStatusAgendamento()!=null){
            agendamento.setStatusAgendamento(StatusAgendamento.valueOf(dto.getStatusAgendamento()));
        }
        return agendamento;
    }
}

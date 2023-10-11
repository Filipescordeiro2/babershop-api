package com.ProjetoPI.BaberShop.Controller;

import com.ProjetoPI.BaberShop.DTO.AgendamentoDTO;
import com.ProjetoPI.BaberShop.DTO.AtualizaStatusDTO;
import com.ProjetoPI.BaberShop.Enums.StatusAgendamento;
import com.ProjetoPI.BaberShop.Enums.TiposDeAgendamentos;
import com.ProjetoPI.BaberShop.Model.Agendamento;
import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Model.Profissional;
import com.ProjetoPI.BaberShop.Service.AgendamentoService;
import com.ProjetoPI.BaberShop.Service.ClienteService;
import com.ProjetoPI.BaberShop.Service.ProfissionalService;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping
    public ResponseEntity salvar(@RequestBody AgendamentoDTO dto){
        try {
            Agendamento entidade = converter(dto);
            entidade=service.salvar(entidade);
            return new ResponseEntity(entidade, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
    public ResponseEntity buscar(@RequestParam(value = "descricaoAgendamento",required = false)String descricaoAgendamento,
                                 @RequestParam(value = "dataAgendamento",required = false)String dataAgendamento,
                                 @RequestParam(value = "horaAgendamento",required = false)String horaAgendamento,
                                 @RequestParam(value = "cliente")Long idCliente                                 ){


        Agendamento agendamentoFiltro = new Agendamento();
        agendamentoFiltro.setDescricaoAgendamento(descricaoAgendamento);
        agendamentoFiltro.setDataAgendamento(dataAgendamento);
        agendamentoFiltro.setHoraAgendamento(horaAgendamento);

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
        agendamento.setDataAgendamento(dto.getDataAgendamento());
        agendamento.setHoraAgendamento(dto.getHoraAgendamento());

        Cliente cliente = clienteService
                .obterPorId(dto.getCliente())
                .orElseThrow(()-> new RegraNegocioException("Cliente não encontrado"));
        Profissional profissional = profissionalService
                .obterPorId(dto.getProfissional())
                .orElseThrow(()-> new RegraNegocioException("Profissional não encontrado"));
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);

        if (dto.getTiposDeAgendamentos()!=null){
            agendamento.setTiposDeAgendamentos(TiposDeAgendamentos.valueOf(dto.getTiposDeAgendamentos()));
        }
        if (dto.getStatusAgendamento()!=null){
            agendamento.setStatusAgendamento(StatusAgendamento.valueOf(dto.getStatusAgendamento()));
        }
        return agendamento;
    }
}

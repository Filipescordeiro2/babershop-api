package com.ProjetoPI.BaberShop.Controller;


import com.ProjetoPI.BaberShop.DTO.AtualizaStatusDTO;
import com.ProjetoPI.BaberShop.DTO.HorarioDTO;
import com.ProjetoPI.BaberShop.DTO.JornadaDeTrabalhoDTO;
import com.ProjetoPI.BaberShop.Enums.StatusAgendamento;
import com.ProjetoPI.BaberShop.Enums.TipoDeJornada;
import com.ProjetoPI.BaberShop.Model.Horario;
import com.ProjetoPI.BaberShop.Model.JornadaDeTrabalho;
import com.ProjetoPI.BaberShop.Model.Profissional;
import com.ProjetoPI.BaberShop.Repository.HorarioRepository;
import com.ProjetoPI.BaberShop.Repository.JornadaDeTrabalhoRepository;
import com.ProjetoPI.BaberShop.Repository.ProfissionalRepository;
import com.ProjetoPI.BaberShop.Service.JornadaDeTrabalhoService;
import com.ProjetoPI.BaberShop.Service.ProfissionalService;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/jornadaDeTrabalho")
public class JornadaDeTrabalhoController {

    @Autowired
    private JornadaDeTrabalhoService service;
    @Autowired
    private ProfissionalService profissionalService;
    @Autowired
    private HorarioRepository horarioRepository;
     @Autowired
     private ProfissionalRepository profissionalRepository;
     @Autowired
     private JornadaDeTrabalhoRepository jornadaDeTrabalhoRepository;
     

    @PostMapping("/cadastrar/jornada")
    public ResponseEntity<String> cadastrarJornada(@RequestBody JornadaDeTrabalhoDTO jornadaDTO) {
        try {
            // Mapeia os atributos da DTO para a entidade JornadaDeTrabalho
            JornadaDeTrabalho entidade = new JornadaDeTrabalho();
            entidade.setId(jornadaDTO.getId());
            entidade.setIntervalo(jornadaDTO.getIntervalo());
            entidade.setData(jornadaDTO.getData());

            // Obtenha o profissional com base no ID informado na DTO
            Optional<Profissional> profissionalOpt = profissionalService.obterPorId(jornadaDTO.getProfissional());

            if (!profissionalOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Profissional não encontrado.");
            }
            entidade.setProfissional(profissionalOpt.get());

            // Mapeie o tipo de jornada a partir da DTO
            entidade.setPeriodo(TipoDeJornada.valueOf(jornadaDTO.getPeriodo()));

            // Verifique se já existe uma jornada para o mesmo profissional, dia e tipo de jornada
            if (verificarJornadaExistente(entidade)) {
                return ResponseEntity.badRequest().body("Jornada já existe para este profissional, dia e tipo de jornada.");
            }

            // Resto do código para salvar a jornada no serviço
            entidade = service.Salvar(entidade);

            // Crie horários com base nos parâmetros
            String periodo = entidade.getPeriodo().toString();
            String intervalo = entidade.getIntervalo();
            String diaDoAtendimento = entidade.getData();

            List<String> horarios = generateHorarios(diaDoAtendimento, periodo, intervalo);

            // Salve cada horário no banco de dados com status "LIVRE" e o ID do profissional
            for (String horario : horarios) {
                Horario novoHorario = new Horario();
                novoHorario.setData(diaDoAtendimento); // Extraia a data da string de horário
                novoHorario.setHora(horario.split(" ")[1]); // Extraia a hora da string de horário
                novoHorario.setPeriodo(periodo);
                novoHorario.setIntervalo(intervalo);
                novoHorario.setStatus("LIVRE");
                novoHorario.setJornadaDeTrabalho(entidade);
                // Configure o relacionamento com o profissional
                Profissional profissional = profissionalRepository.findById(jornadaDTO.getProfissional()).orElse(null);
                if (profissional != null) {
                    novoHorario.setProfissional(profissional);
                }
                horarioRepository.save(novoHorario);
            }

            return ResponseEntity.ok("Jornada cadastrada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar jornada. " + e.getMessage());
        }
    }



    @DeleteMapping("{id}")
    public ResponseEntity deletarJornada (@PathVariable("id") Long id){
        return service.obterPorId(id).map(entidade ->{
            service.deletarJornada(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(()-> new ResponseEntity("Jordana não encontrado no banco de dados",HttpStatus.BAD_REQUEST));
    }

   

    @GetMapping
    public ResponseEntity buscar(@RequestParam(value = "intervalor",required = false)String intervalo,
                                 @RequestParam(value = "data",required = false)String data,
                                 @RequestParam(value = "profissional")Long idProfissional                                 ){


        JornadaDeTrabalho jornadaFiltro = new JornadaDeTrabalho();
        jornadaFiltro.setData(data);
        jornadaFiltro.setIntervalo(intervalo);

        Optional<Profissional> profissional=profissionalService.obterPorId(idProfissional);

        if (!profissional.isPresent()){
            return ResponseEntity.badRequest().body("não foi possivel realizar a consulta");
        }else {
            jornadaFiltro.setProfissional(profissional.get());
        }
        List<JornadaDeTrabalho> jornadaDeTrabalhos=service.buscarJornada(jornadaFiltro);
        return ResponseEntity.ok(jornadaDeTrabalhos);
    }



    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id")Long id,@RequestBody JornadaDeTrabalhoDTO dto){
        return service.obterPorId(id).map(entity ->{

            try {
                JornadaDeTrabalho jornadaDeTrabalho = converter(dto);
                jornadaDeTrabalho.setId(entity.getId());
                if (verificarJornadaExistente(jornadaDeTrabalho)) {
                    return ResponseEntity.badRequest().body("Jornada já existe para este profissional, dia e tipo de jornada.");
                }
                service.atualizar(jornadaDeTrabalho);
                return ResponseEntity.ok(jornadaDeTrabalho);
            }catch (RegraNegocioException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(()-> new ResponseEntity("Lancamento não encontrado no banco de dados", HttpStatus.BAD_REQUEST));
    }
    

    private JornadaDeTrabalho converter(JornadaDeTrabalhoDTO dto){
       JornadaDeTrabalho jornadaDeTrabalho = new JornadaDeTrabalho();
       jornadaDeTrabalho.setId(dto.getId());
       jornadaDeTrabalho.setIntervalo(dto.getIntervalo());
       jornadaDeTrabalho.setData(dto.getData());

        Profissional profissional = profissionalService
                .obterPorId(dto.getProfissional())
                .orElseThrow(()-> new RegraNegocioException("Profissional não encontrado"));
        jornadaDeTrabalho.setProfissional(profissional);

        if (dto.getPeriodo() != null) {
            jornadaDeTrabalho.setPeriodo(TipoDeJornada.valueOf(dto.getPeriodo()));
        }
        return jornadaDeTrabalho;
    }

    private boolean verificarJornadaExistente(JornadaDeTrabalho jornada) {
        // Verifique se já existe uma jornada para o mesmo profissional, dia e tipo de jornada
        List<JornadaDeTrabalho> jornadasExistentes = service.buscarJornada(jornada);
        jornadasExistentes.removeIf(j -> j.getId().equals(jornada.getId())); // Remova a jornada atual (se estiver sendo atualizada)
        return !jornadasExistentes.isEmpty();
    }
    private List<String> generateHorarios(String diaDoAtendimento, String periodo, String intervalo) {
        List<String> horarios = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();

        try {
            Date data = dateFormat.parse(diaDoAtendimento); // Converta a string de data para Date
            calendar.setTime(data);
        } catch (ParseException e) {
            // Trate exceção de análise, se necessário
        }

        int horaInicio = 0;
        int horaFim = 0;

        // Define o período com base na entrada
        if ("MANHA".equals(periodo)) {
            horaInicio = 9; // Manhã começa às 09:00
            horaFim = 12;   // Manhã termina às 12:00
        } else if ("TARDE".equals(periodo)) {
            horaInicio = 12; // Tarde começa às 12:00
            horaFim = 18;   // Tarde termina às 18:00
        } else if ("NOITE".equals(periodo)) {
            horaInicio = 19; // Noite começa às 19:00
            horaFim = 22;   // Noite termina às 22:00
        }

        calendar.set(Calendar.HOUR_OF_DAY, horaInicio);
        calendar.set(Calendar.MINUTE, 0);

        int intervaloMinutos = "30min".equals(intervalo) ? 30 : 60;

        while (calendar.get(Calendar.HOUR_OF_DAY) < horaFim) {
            String dataStr = dateFormat.format(calendar.getTime()); // Formate a data e hora
            horarios.add(dataStr);

            calendar.add(Calendar.MINUTE, intervaloMinutos);
        }

        return horarios;
    }




}

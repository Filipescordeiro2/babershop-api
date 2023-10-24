package com.ProjetoPI.BaberShop.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ProjetoPI.BaberShop.Model.Horario;
import com.ProjetoPI.BaberShop.Model.Profissional;

import com.ProjetoPI.BaberShop.Service.JornadaDeTrabalhoService;
import com.ProjetoPI.BaberShop.Service.ProfissionalService;
@RestController
@RequestMapping("/api/Horario")
public class HorariosController {

    
    @Autowired
    private JornadaDeTrabalhoService service;
    @Autowired
    private ProfissionalService profissionalService;
     
    @GetMapping
    public ResponseEntity<java.util.List<Horario>> buscarHorariosDoProfissional(@RequestParam(value = "Profissional")Long Profissional,
                                                                      @RequestParam(value = "data",required = false) String data,
                                                                      @RequestParam(value = "hora",required = false)String hora,
                                                                      @RequestParam(value = "status",required=false)String status)
                                                                                                     {


        Optional<Profissional> profissionalOpt = profissionalService.obterPorId(Profissional);

        if (!profissionalOpt.isPresent()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        Profissional profissional = profissionalOpt.get();
        Horario horarioFiltro = new Horario();
        horarioFiltro.setData(data);
        horarioFiltro.setHora(hora);
        horarioFiltro.setProfissional(profissionalOpt.get());
        horarioFiltro.setStatus(status);
        java.util.List<Horario> horarios = service.buscarHorario(horarioFiltro);

        return ResponseEntity.ok(horarios);
    }

     @DeleteMapping("{id}")
    public ResponseEntity deletarHorario(@PathVariable("id") Long id){
        return service.obterHorarioPorId(id).map(entidade ->{
            service.deletarHorario(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(()-> new ResponseEntity("Horario não encontrado no banco de dados",HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/buscarHorario")
    public ResponseEntity<java.util.List<Horario>> buscarHorarios(@RequestParam(value = "nomeProfissional")String nomeProfissional,
                                                                                @RequestParam(value = "data",required = false) String data,
                                                                                @RequestParam(value = "hora",required = false)String hora
                                                                              )
    {


        List<Profissional> profissionais = profissionalService.findByNome(nomeProfissional);

        if (profissionais.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        // Suponhamos que você deseja usar o primeiro profissional encontrado.
        Profissional profissional = profissionais.get(0);

        Horario horarioFiltro = new Horario();
        horarioFiltro.setData(data);
        horarioFiltro.setHora(hora);
        horarioFiltro.setProfissional(profissional);
        horarioFiltro.setStatus("LIVRE");
        java.util.List<Horario> horarios = service.buscarHorario(horarioFiltro);

        return ResponseEntity.ok(horarios);
    }
}

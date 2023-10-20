package com.ProjetoPI.BaberShop.Controller;

import com.ProjetoPI.BaberShop.DTO.ClienteDTO;
import com.ProjetoPI.BaberShop.DTO.ProfissionalDTO;
import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Model.Profissional;
import com.ProjetoPI.BaberShop.Service.ProfissionalService;
import com.ProjetoPI.BaberShop.exception.ErroAutenticacao;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profissional")
public class ProfissionalController {

    @Autowired
    private ProfissionalService service;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody ProfissionalDTO dto){
        try {
            Profissional profissionalAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(profissionalAutenticado);
        }catch (ErroAutenticacao e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar (@RequestBody ClienteDTO dto){
        Profissional profissional = Profissional.builder()
                .nome(dto.getNome())
                .sobrenome(dto.getSobrenome())
                .telefone(dto.getTelefone())
                .cpf(dto.getCpf())
                .dataDeNascimento(dto.getDataDeNascimento())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        try {
            Profissional profissionalSalvo = service.salvarProfissional(profissional);
            return new ResponseEntity<>(profissionalSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
      @GetMapping
    public ResponseEntity buscar (@RequestParam(value = "idProfissional",required = false)Long idProfissional){

      Profissional profissionalFiltro = new Profissional();

      Optional<Profissional>profissional=service.obterPorId(idProfissional);

        if (!profissional.isPresent()){
            return ResponseEntity.badRequest().body("não foi possivel realizar a consulta");
        }

      List<Profissional> profissionais = service.buscar(profissionalFiltro);
      return ResponseEntity.ok(profissionais);
  }

}

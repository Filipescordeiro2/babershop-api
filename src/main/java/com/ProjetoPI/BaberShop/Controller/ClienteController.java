package com.ProjetoPI.BaberShop.Controller;


import com.ProjetoPI.BaberShop.DTO.ClienteDTO;
import com.ProjetoPI.BaberShop.Model.Cliente;
import com.ProjetoPI.BaberShop.Service.AgendamentoService;
import com.ProjetoPI.BaberShop.Service.ClienteService;
import com.ProjetoPI.BaberShop.exception.ErroAutenticacao;
import com.ProjetoPI.BaberShop.exception.RegraNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;
    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody ClienteDTO dto){
        try {
            Cliente clienteAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(clienteAutenticado);
        }catch (ErroAutenticacao e){
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity salvar (@RequestBody ClienteDTO dto){
        Cliente cliente = Cliente.builder()
                .nome(dto.getNome())
                .telefone(dto.getTelefone())
                .cpf(dto.getCpf())
                .dataDeNascimento(dto.getDataDeNascimento())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();
        try {
            Cliente clienteSalvo = service.salvarCliente(cliente);
            return new ResponseEntity<>(clienteSalvo, HttpStatus.CREATED);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

  @GetMapping("/buscarPorNome")
  public ResponseEntity buscar (@RequestParam(value = "nome",required = false)String nome){

      Cliente clienteFiltro = new Cliente();
           clienteFiltro.setNome(nome);

      List<Cliente> clientes = service.buscar(clienteFiltro);
      return ResponseEntity.ok(clientes);
  }

  @GetMapping
    public ResponseEntity buscar (@RequestParam(value = "idCliente",required = false)Long idCliente){

      Cliente ClienteFiltro = new Cliente();

      Optional<Cliente>cliente=service.obterPorId(idCliente);

        if (!cliente.isPresent()){
            return ResponseEntity.badRequest().body("não foi possivel realizar a consulta");
        }

      List<Cliente> clientes = service.buscar(ClienteFiltro);
      return ResponseEntity.ok(clientes);
  }



}

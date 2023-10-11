package com.ProjetoPI.BaberShop.DTO;



import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nome;
    private String sobrenome;
    private String telefone;
    private String cpf;
    private String dataDeNascimento;
    private String senha;
    private String email;
}

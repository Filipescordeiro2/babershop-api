package com.ProjetoPI.BaberShop.Model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "cliente")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "nome")
    private String nome;
    @Column  (name = "telefone")
    private String telefone;
    @Column (name = "cpf")
    private String cpf;
    @Column (name = "dataDeNascimento")
    private String dataDeNascimento;
    @Column (name = "senha")
    private String senha;
    @Column (name = "email")
    private String email;
    private String statusPerfil;
}

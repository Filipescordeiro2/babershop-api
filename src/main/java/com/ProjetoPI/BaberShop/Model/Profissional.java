package com.ProjetoPI.BaberShop.Model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profissional")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Profissional {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "nome")
    private String nome;
    @Column (name = "sobrenome")
    private String sobrenome;
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
}

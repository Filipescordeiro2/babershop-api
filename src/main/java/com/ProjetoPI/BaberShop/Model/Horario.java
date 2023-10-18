package com.ProjetoPI.BaberShop.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String data;
    private String periodo;
    private String intervalo;
    private String status;
    private String Hora;
    @ManyToOne
    private Profissional profissional;
    @ManyToOne
    private JornadaDeTrabalho jornadaDeTrabalho;
}

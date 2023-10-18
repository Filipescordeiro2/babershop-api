package com.ProjetoPI.BaberShop.Model;

import com.ProjetoPI.BaberShop.Enums.TipoDeJornada;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jornadaDeTrabalho")
public class JornadaDeTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "id_profissional")
    @ManyToOne
    private Profissional profissional;

    @Column(name = "periodo")
    @Enumerated(value = EnumType.STRING)
    private TipoDeJornada periodo;

    @Column(name = "intervalo")
    private String intervalo;

    @Column(name = "data")
    private String data;

}

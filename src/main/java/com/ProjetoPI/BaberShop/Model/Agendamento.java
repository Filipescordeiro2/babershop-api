package com.ProjetoPI.BaberShop.Model;

import com.ProjetoPI.BaberShop.Enums.StatusAgendamento;
import com.ProjetoPI.BaberShop.Enums.TiposDeAgendamentos;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agendamento")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Agendamento {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;
    @Column (name = "dataAgendamento")
    private String dataAgendamento;
    @Column (name = "horaAgendamento")
    private String horaAgendamento;
    @Column(name = "descricaoAgendamento")
    private String descricaoAgendamento;
    @JoinColumn(name = "profissional")
    @ManyToOne
    private Profissional profissional;
    @JoinColumn(name = "cliente")
    @ManyToOne
    private Cliente cliente;
    @Column(name = "tipoDeAgendamento")
    @Enumerated(value = EnumType.STRING)
    private TiposDeAgendamentos tiposDeAgendamentos;
    @Column(name = "statusAgendamento")
    @Enumerated(value = EnumType.STRING)
    private StatusAgendamento statusAgendamento;

}

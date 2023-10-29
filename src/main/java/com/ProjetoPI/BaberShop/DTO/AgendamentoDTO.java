package com.ProjetoPI.BaberShop.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    AgendamentoDTO {

    private Long id;
    private Long id_profissional;
    private Long id_cliente;
    private Long id_horario;
    private String tiposDeAgendamentos;
    private String statusAgendamento;
    private String descricaoAgendamento;
    private String data;

}

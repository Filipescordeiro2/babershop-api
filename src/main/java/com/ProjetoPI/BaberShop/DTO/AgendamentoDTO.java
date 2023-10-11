package com.ProjetoPI.BaberShop.DTO;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendamentoDTO {

    private Long id;
    private String dataAgendamento;
    private String horaAgendamento;
    private String descricaoAgendamento;
    private Long profissional;
    private Long cliente;
    private String tiposDeAgendamentos;
    private String statusAgendamento;
}

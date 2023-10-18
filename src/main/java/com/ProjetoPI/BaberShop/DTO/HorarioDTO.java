package com.ProjetoPI.BaberShop.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {

    private String data;
    private String Hora;
    private String periodo;
    private String intervalo;
    private String status;
    private Long profissional;
    private Long jornadaDeTrabalho;
}

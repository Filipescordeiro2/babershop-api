package com.ProjetoPI.BaberShop.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JornadaDeTrabalhoDTO {

    private Long id;
    private Long profissional;
    private String periodo;
    private String intervalo;
    private String data;
}

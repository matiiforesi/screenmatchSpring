package com.aluracursos.screenmatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {
    @GetMapping("/series")
    public String mostrarMensaje() {
        return "Este es el primer mensaje de la aplicacion web.";
    }
}

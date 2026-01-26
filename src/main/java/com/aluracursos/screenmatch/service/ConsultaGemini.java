package com.aluracursos.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ConsultaGemini {
    public static String obtenerTraduccion(String texto) {
        String modelo = "gemini-3-flash-preview";
        String prompt = "Traduce el siguiente texto al español, dame solo la traduccion: " + texto;

        Client cliente = new Client.Builder().apiKey("AIzaSyBAlEwcGMGbKLP42G5gBzEWZjChCF21gEY").build();

        try {
            GenerateContentResponse respuesta = cliente.models.generateContent(
                    modelo,
                    prompt,
                    null // parametro para configuraciones adicionales
            );

            if (!respuesta.text().isEmpty()) {
                return respuesta.text();
            }
        } catch (Exception e) {
            System.err.println("Error al llamar a la API de Gemini para traducción: " + e.getMessage());
        }

        return null;
    }
}
package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();

    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "${OMDB_APIKEY}"; // mas adelante se vuelve variable de ambiente

    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();

    private SerieRepository repository;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series previamente buscadas
                    0 - Salir
                    """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicacion...");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escriba el nombre de la serie que deseas buscar: ");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        DatosSerie datosSerie = getDatosSerie();
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i <= datosSerie.totalDeTemporadas(); i++) {
            var json = consumoApi.obtenerDatos(
                    URL_BASE + datosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY
            );
            DatosTemporadas datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }

        temporadas.forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
//        datosSeries.add(datos);
        Serie serie = new Serie(datos);
        repository.save(serie);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
//        List<Serie> series = new ArrayList<>();
//        series = datosSeries.stream()
//                .map(d -> new Serie(d))
//                .collect(Collectors.toList());

        List<Serie> series = repository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}

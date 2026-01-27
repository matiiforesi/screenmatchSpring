package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria genero);
//    List<Serie> findByTotalTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalTemporadas, Double evaluacion);
//    @Query(value = "SELECT * FROM SERIES WHERE SERIES.TOTAL_DE_TEMPORADAS <= 6 AND SERIES.EVALUACION >= 7.8", nativeQuery = true)
    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas >= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadasYEvaluacion(int totalTemporadas, Double evaluacion);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5EpisodiosPorSerie(Serie serie);
}

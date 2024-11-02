package com.ingenieriaSoftware.videojuegos.business.repository;

import com.ingenieriaSoftware.videojuegos.business.domain.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repositorio de videojuego
 * @author Tomas Rando
 * @version 1.0.0
 */
@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, String> {

    @Query("SELECT p FROM Videojuego p WHERE p.activo = TRUE")
    public Collection<Videojuego> listarVideojuegoActivo();

    @Query("SELECT p FROM Videojuego p WHERE p.titulo = :titulo AND p.activo = TRUE")
    public Videojuego buscarPorTitulo(@Param("titulo") String nombre);

    @Query("SELECT p FROM Videojuego p WHERE p.estudio.id = :id AND p.activo = TRUE")
    public Collection<Videojuego> listarVideojuegoPorEstudio(@Param("id") String estudio);

    @Query("SELECT p FROM Videojuego p WHERE p.categoria.id = :id AND p.activo = TRUE")
    public Collection<Videojuego> listarVideojuegoPorCategoria(@Param("id") String estudio);

    public Videojuego findFirstByEstudioIdAndActivoTrue(String estudioId);

}

package com.ingenieriaSoftware.videojuegos.business.repository;

import com.ingenieriaSoftware.videojuegos.business.domain.Estudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repositorio de estudio
 * @version 1.0.0
 * @author Tomas Rando
 */
@Repository
public interface EstudioRepository extends JpaRepository<Estudio, String> {
    @Query("SELECT p FROM Estudio p WHERE p.activo = TRUE")
    public Collection<Estudio> listarEstudioActivo();

    @Query("SELECT p FROM Estudio p WHERE p.activo = TRUE AND p.nombre = :nombre")
    public Estudio buscarEstudioPorNombre(@Param("nombre") String nombre);
}

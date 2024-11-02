package com.ingenieriaSoftware.videojuegos.business.repository;

import com.ingenieriaSoftware.videojuegos.business.domain.Categoria;
import com.ingenieriaSoftware.videojuegos.business.domain.Estudio;
import com.ingenieriaSoftware.videojuegos.business.domain.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repositorio de categoría
 * @version 1.0.0
 * @author Tomas Rando
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, String> {
    @Query("SELECT p FROM Categoria p WHERE p.activo = TRUE")
    public Collection<Categoria> listarCategoriaActivo();

    @Query("SELECT p FROM Categoria p WHERE p.activo = TRUE AND p.nombre = :nombre")
    public Categoria buscarCategoriaPorNombre(@Param("nombre") String nombre);
}

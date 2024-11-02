package com.ingenieriaSoftware.videojuegos.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de categoría
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
@NoArgsConstructor
@Data
public class Categoria {
    private String nombre;
    @Id
    private String id;
    private String categoriaImg;
    private boolean activo;
}
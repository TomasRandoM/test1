package com.ingenieriaSoftware.videojuegos.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Entidad de videojuego
 * @version 1.0.0
 * @author Tomas Rando
 */
@Entity
@Data
@NoArgsConstructor
public class Videojuego {
    private String titulo;
    private String rutaImg;
    private float precio;
    private short cantidad;
    private String descripcion;
    private boolean oferta;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lanzamiento;
    private boolean activo;
    @Id
    private String id;
    @ManyToOne
    private Estudio estudio;
    @ManyToOne
    private Categoria categoria;
}

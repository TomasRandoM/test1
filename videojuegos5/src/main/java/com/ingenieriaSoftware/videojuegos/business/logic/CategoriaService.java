package com.ingenieriaSoftware.videojuegos.business.logic;

import com.ingenieriaSoftware.videojuegos.business.domain.Categoria;
import com.ingenieriaSoftware.videojuegos.business.domain.Videojuego;
import com.ingenieriaSoftware.videojuegos.business.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de categoría
 * @author Tomas Rando
 * @version 1.0.0
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Crea la categoría con el nombre especificado
     * @param nombre String
     * @throws ErrorServiceException
     */
    public void crearCategoria(String nombre, String categoriaImg) throws ErrorServiceException {
        try {

            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el nombre");
            }

            if (categoriaImg == null || categoriaImg.isEmpty()) {
                throw new ErrorServiceException("Debe indicar la url de la imagen");
            }

            if (categoriaRepository.buscarCategoriaPorNombre(nombre) != null) {
                throw new ErrorServiceException("La categoria ya existe");
            }

            Categoria categoria = new Categoria();
            categoria.setId(UUID.randomUUID().toString());
            categoria.setNombre(nombre);
            categoria.setActivo(true);
            categoria.setCategoriaImg(categoriaImg);

            categoriaRepository.save(categoria);
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     * @param idCategoria String
     * @return Categoria
     * @throws ErrorServiceException
     */
    public Categoria buscarCategoria(String idCategoria) throws ErrorServiceException {
        Categoria categoria;
        try {
            if (idCategoria == null || idCategoria.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Optional optional = categoriaRepository.findById(idCategoria);

            if (optional.isPresent()) {
                 categoria = (Categoria) optional.get();
            } else {
                throw new ErrorServiceException("La categoria no existe");
            }

            if (!categoria.isActivo()) {
                throw new ErrorServiceException("La categoria no existe");
            }

            return categoria;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     * @param idCategoria String
     * @throws ErrorServiceException
     */
    public void eliminarCategoria(String idCategoria) throws ErrorServiceException {
        try {
            if (idCategoria == null || idCategoria.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Categoria categoria = buscarCategoria(idCategoria);
            categoria.setActivo(false);
            categoriaRepository.save(categoria);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Lista las categorías
     * @return Collection
     * @throws ErrorServiceException
     */
    public Collection<Categoria> listarCategorias() throws ErrorServiceException {
        try {
            return categoriaRepository.listarCategoriaActivo();
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema");
        }
    }

    /**
     * Modifica el nombre de la categoría con el id indicado
     * @param id String
     * @param nombre String
     * @throws ErrorServiceException
     */
    public void modificarCategoria(String id, String nombre, String categoriaImg) throws ErrorServiceException {
        try {
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            if (nombre == null || nombre.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un nombre");
            }
            if (categoriaImg == null || categoriaImg.isEmpty()) {
                throw new ErrorServiceException("Debe indicar una imagen");
            }

            Categoria categoria = buscarCategoria(id);

            Categoria categoriaAux = categoriaRepository.buscarCategoriaPorNombre(nombre);
            if (categoriaAux != null) {
                if (!categoriaAux.getId().equals(id)) {
                    throw new ErrorServiceException("Ya existe una categoria con ese nombre");
                }
            }
            categoria.setNombre(nombre);
            categoria.setCategoriaImg(categoriaImg);
            categoriaRepository.save(categoria);

        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    //No se agrega método de verificación por ser pocos atributos
}
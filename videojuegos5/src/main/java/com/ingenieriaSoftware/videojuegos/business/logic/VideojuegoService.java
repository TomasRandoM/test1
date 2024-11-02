package com.ingenieriaSoftware.videojuegos.business.logic;

import com.ingenieriaSoftware.videojuegos.VideojuegosApplication;
import com.ingenieriaSoftware.videojuegos.business.domain.Videojuego;
import com.ingenieriaSoftware.videojuegos.business.domain.Categoria;
import com.ingenieriaSoftware.videojuegos.business.domain.Estudio;

import com.ingenieriaSoftware.videojuegos.business.repository.VideojuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de videojuego
 * @author Tomas Rando
 * @version 1.0.0
 */
@Service
public class VideojuegoService {

    private final VideojuegoRepository videojuegoRepository;
    private EstudioService estudioService;
    private final CategoriaService categoriaService;

    @Autowired
    public VideojuegoService(VideojuegoRepository videojuegoRepository, CategoriaService categoriaService) {
        this.videojuegoRepository = videojuegoRepository;
        this.categoriaService = categoriaService;
    }

    @Autowired
    public void setEstudioService(EstudioService estudioService) {
        this.estudioService = estudioService;
    }

    /**
     * Crea el videojuego y lo persiste
     *
     * @param titulo      String
     * @param rutaImg     String
     * @param precio      float
     * @param cantidad    short
     * @param descripcion String
     * @param oferta      boolean
     * @param lanzamiento Date
     * @param estudioId   String
     * @param categoriaId String
     * @throws ErrorServiceException
     */
    public void crearVideojuego(String titulo, String rutaImg, float precio, short cantidad, String descripcion, boolean oferta, Date lanzamiento, String estudioId, String categoriaId) throws ErrorServiceException {
        try {

            validarDatos("alta", null, titulo, rutaImg, precio, cantidad, descripcion, oferta, lanzamiento, estudioId, categoriaId);

            if (videojuegoRepository.buscarPorTitulo(titulo) != null) {
                throw new ErrorServiceException("El titulo ya existe");
            }

            Videojuego videojuego = new Videojuego();
            videojuego.setId(UUID.randomUUID().toString());
            videojuego = setearDatos(videojuego, titulo, rutaImg, precio, cantidad, descripcion, oferta, lanzamiento, estudioId, categoriaId);
            videojuegoRepository.save(videojuego);
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Busca al objeto en la base de datos y lo devuelve
     *
     * @param idVideojuego String
     * @return Videojuego
     * @throws ErrorServiceException
     */
    public Videojuego buscarVideojuego(String idVideojuego) throws ErrorServiceException {
        Videojuego videojuego;
        try {
            if (idVideojuego == null || idVideojuego.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Optional optional = videojuegoRepository.findById(idVideojuego);

            if (optional.isPresent()) {
                videojuego = (Videojuego) optional.get();
            } else {
                throw new ErrorServiceException("El videojuego no existe");
            }

            if (!videojuego.isActivo()) {
                throw new ErrorServiceException("El videojuego no existe");
            }

            return videojuego;
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Elimina el objeto de la base de datos
     *
     * @param idVideojuego String
     * @throws ErrorServiceException
     */
    public void eliminarVideojuego(String idVideojuego) throws ErrorServiceException {
        try {
            if (idVideojuego == null || idVideojuego.isEmpty()) {
                throw new ErrorServiceException("Debe indicar el id");
            }
            Videojuego videojuego = buscarVideojuego(idVideojuego);
            videojuego.setActivo(false);
            videojuegoRepository.save(videojuego);
        } catch (ErrorServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    /**
     * Lista los videojuegos activos
     *
     * @return Collection
     * @throws ErrorServiceException
     */
    public Collection<Videojuego> listarVideojuegos() throws ErrorServiceException {
        try {
            return videojuegoRepository.listarVideojuegoActivo();
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistema");
        }
    }

    /**
     * Modifica el videojuego
     *
     * @param id          String
     * @param titulo      String
     * @param rutaImg     String
     * @param precio      float
     * @param cantidad    short
     * @param descripcion String
     * @param oferta      boolean
     * @param lanzamiento Date
     * @param estudioId   String
     * @param categoriaId String
     */
    public void modificarVideojuego(String id, String titulo, String rutaImg, float precio, short cantidad, String descripcion, boolean oferta, Date lanzamiento, String estudioId, String categoriaId) {
        try {
            validarDatos("modificar", id, titulo, rutaImg, precio, cantidad, descripcion, oferta, lanzamiento, estudioId, categoriaId);

            Videojuego videojuego = buscarVideojuego(id);

            Videojuego videojuegoAux;
            videojuegoAux = videojuegoRepository.buscarPorTitulo(titulo);
            if (videojuegoAux != null) {
                if (!videojuegoAux.getId().equals(id)) {
                    throw new ErrorServiceException("Un videojuego con ese título ya existe");
                }
            }

            videojuego = setearDatos(videojuego, titulo, rutaImg, precio, cantidad, descripcion, oferta, lanzamiento, estudioId, categoriaId);

            videojuegoRepository.save(videojuego);

        } catch (ErrorServiceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Valida los datos de videojuego
     *
     * @param tipo        String
     * @param id          String
     * @param titulo      String
     * @param rutaImg     String
     * @param precio      float
     * @param cantidad    short
     * @param descripcion String
     * @param oferta      boolean
     * @param lanzamiento Date
     * @param estudioId   String
     * @param categoriaId String
     * @throws ErrorServiceException
     */
    private void validarDatos(String tipo, String id, String titulo, String rutaImg, float precio, short cantidad, String descripcion, boolean oferta, Date lanzamiento, String estudioId, String categoriaId) throws ErrorServiceException {

        if (!tipo.equals("alta")) {
            if (id == null || id.isEmpty()) {
                throw new ErrorServiceException("Debe indicar un videojuego");
            }
        }

        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServiceException("Debe indicar un titulo");
        }

        if (rutaImg == null || rutaImg.isEmpty()) {
            throw new ErrorServiceException("Debe indicar un ruta de imagen");
        }

        if (precio < 0) {
            throw new ErrorServiceException("Debe indicar el precio");
        }

        if (cantidad < 0) {
            throw new ErrorServiceException("Debe indicar la cantidad");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new ErrorServiceException("Debe indicar la descripcion");
        }

        if (lanzamiento == null) {
            throw new ErrorServiceException("Debe indicar la lanzamiento");
        }

        if (estudioId == null || estudioId.isEmpty()) {
            throw new ErrorServiceException("Debe indicar el estudio");
        }

        if (categoriaId == null || categoriaId.isEmpty()) {
            throw new ErrorServiceException("Debe indicar la categoria");
        }
    }


    /**
     * Setea los datos en el objeto videojuego pasado como parámetro
     *
     * @param videojuego  Videojuego
     * @param titulo      String
     * @param rutaImg     String
     * @param precio      float
     * @param cantidad    short
     * @param descripcion String
     * @param oferta      boolean
     * @param lanzamiento Date
     * @param estudioId   String
     * @param categoriaId String
     * @return Videojuego
     * @throws ErrorServiceException
     */
    private Videojuego setearDatos(Videojuego videojuego, String titulo, String rutaImg, float precio, short cantidad, String descripcion, boolean oferta, Date lanzamiento, String estudioId, String categoriaId) throws ErrorServiceException {
        Estudio estudio = estudioService.buscarEstudio(estudioId);
        Categoria categoria = categoriaService.buscarCategoria(categoriaId);
        videojuego.setTitulo(titulo);
        videojuego.setRutaImg(rutaImg);
        videojuego.setPrecio(precio);
        videojuego.setCantidad(cantidad);
        videojuego.setDescripcion(descripcion);
        videojuego.setOferta(oferta);
        videojuego.setActivo(true);
        videojuego.setLanzamiento(lanzamiento);
        videojuego.setEstudio(estudio);
        videojuego.setCategoria(categoria);
        return videojuego;
    }

    public Collection<Videojuego> listarVideojuegoPorCategoria(String idCategoria) throws ErrorServiceException {
        try {
            return videojuegoRepository.listarVideojuegoPorCategoria(idCategoria);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public Collection<Videojuego> listarVideojuegoPorEstudio(String idEstudio) throws ErrorServiceException {
        try {
            return videojuegoRepository.listarVideojuegoPorEstudio(idEstudio);
        } catch (Exception ex) {
            throw new ErrorServiceException("Error de sistemas");
        }
    }

    public Videojuego buscarPrimerVideojuegoPorEstudio(String idEstudio) throws ErrorServiceException {
        try {
            return videojuegoRepository.findFirstByEstudioIdAndActivoTrue(idEstudio);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistemas");
        }
    }
}

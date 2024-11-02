package com.ingenieriaSoftware.videojuegos.controller;

import com.ingenieriaSoftware.videojuegos.business.domain.Categoria;
import com.ingenieriaSoftware.videojuegos.business.domain.Estudio;
import com.ingenieriaSoftware.videojuegos.business.domain.Videojuego;
import com.ingenieriaSoftware.videojuegos.business.logic.CategoriaService;
import com.ingenieriaSoftware.videojuegos.business.logic.ErrorServiceException;
import com.ingenieriaSoftware.videojuegos.business.logic.EstudioService;
import com.ingenieriaSoftware.videojuegos.business.logic.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
public class VideojuegoController {

    @Autowired
    private VideojuegoService videojuegoService;
    @Autowired
    private EstudioService estudioService;
    @Autowired
    private CategoriaService categoriaService;

    /**
     * Maneja el alta de videojuegos
     * @param videojuego Videojuego
     * @param model Model
     * @return String
     */
    @GetMapping("/altaVideojuego")
    public String alta(Videojuego videojuego, Model model) {
        try {
            cargarListas(model, false);
            return "view/videojuego/editVideojuego";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error con el formulario");
            return "/view/videojuego/editVideojuego";
        }
    }

    /**
     * Encargada de manejar el caso de modificación del videojuego
     * @param id Id del videojuego a modificar
     * @param model Model
     * @return String
     */
    @GetMapping("/modificarVideojuego")
    public String modificar(@RequestParam(value="id") String id, Model model) {
        try {
            cargarListas(model, false);
            Videojuego videojuego = videojuegoService.buscarVideojuego(id);
            model.addAttribute("videojuego", videojuego);
            return "view/videojuego/editVideojuego";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error con el formulario");
            return "/view/videojuego/editVideojuego";
        }
    }

    /**
     * Se encarga de dar de baja el videojuego. Llama al servicio para que borre el videojuego con el id que viene como parametro
     * @param id String
     * @param model Model
     * @return String
     */
    @GetMapping("/bajaVideojuego")
    public String baja(@RequestParam(value="id") String id, Model model) {
        try {
            videojuegoService.eliminarVideojuego(id);
            return "redirect:/inicio";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "inicio";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error con el formulario");
            return "inicio";
        }
    }

    @GetMapping("/consultarVideojuego")
    public String consulta(@RequestParam(value="id") String id, Model model) {
        try {
            Videojuego videojuego = videojuegoService.buscarVideojuego(id);
            model.addAttribute("videojuego", videojuego);
            cargarListas(model, true);
            return "view/videojuego/editVideojuego";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "view/videojuego/editVideojuego";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error con el formulario");
            return "view/videojuego/editVideojuego";
        }
    }

    /**
     * Carga las listas de categorias y estudios
     * @param model Model
     * @throws ErrorServiceException
     */
    public void cargarListas(Model model, boolean isDisabled) throws ErrorServiceException {
        try {
            Collection<Estudio> estudioList = estudioService.listarEstudios();
            Collection<Categoria> categoriaList = categoriaService.listarCategorias();
            model.addAttribute("estudios", estudioList);
            model.addAttribute("categorias", categoriaList);
            model.addAttribute("isDisabled", isDisabled);

        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Maneja el proceso que se realiza al presionar el botón aceptar
     * @param videojuego Videojuego
     * @param result BindingResult
     * @param attributes RedirectAttributes
     * @param model Model
     * @return String
     */
    @PostMapping("/videojuego/aceptarEditVideojuego")
    public String aceptarEdit(Videojuego videojuego, BindingResult result, RedirectAttributes attributes, Model model) {
        try {

            if (result.hasErrors()) {
                model.addAttribute("mensajeError", "Error con el formulario");
                return "/view/videojuego/editVideojuego";
            }

            if (videojuego.getId() == null || videojuego.getId().isEmpty()) {
                videojuegoService.crearVideojuego(videojuego.getTitulo(), videojuego.getRutaImg(), videojuego.getPrecio(), videojuego.getCantidad(), videojuego.getDescripcion(), videojuego.isOferta(), videojuego.getLanzamiento(), videojuego.getEstudio().getId(), videojuego.getCategoria().getId());
            } else {
                videojuegoService.modificarVideojuego(videojuego.getId(), videojuego.getTitulo(), videojuego.getRutaImg(), videojuego.getPrecio(), videojuego.getCantidad(), videojuego.getDescripcion(), videojuego.isOferta(), videojuego.getLanzamiento(), videojuego.getEstudio().getId(), videojuego.getCategoria().getId());
            }
            return "redirect:/inicio";
        } catch (ErrorServiceException e) {
            model.addAttribute("mensajeError", e.getMessage());
            return "/view/videojuego/editVideojuego";
        } catch (Exception ex) {
            ex.printStackTrace();
            model.addAttribute("mensajeError", "Error de sistemas");
            return "/view/videojuego/editVideojuego";

        }
    }

    @GetMapping("/inicio")
    public String inicio(Model model, @RequestParam(value = "categoria", required = false) String idCat) {
        Collection<Videojuego> colVideo;
        try {
            if (idCat == null || idCat.isEmpty()) {
                colVideo = videojuegoService.listarVideojuegos();
            } else {
                colVideo = videojuegoService.listarVideojuegoPorCategoria(idCat);
            }
            model.addAttribute("videojuegos", colVideo);
            return "view/inicio";
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "error";
        }
    }
}

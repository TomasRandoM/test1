package com.ingenieriaSoftware.videojuegos.controller;

import com.ingenieriaSoftware.videojuegos.business.domain.Categoria;
import com.ingenieriaSoftware.videojuegos.business.logic.ErrorServiceException;
import com.ingenieriaSoftware.videojuegos.business.logic.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;

@Controller
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/categorias")
    public String listarCategorias(Model model) throws ErrorServiceException {
        try {
            Collection<Categoria> categorias = categoriaService.listarCategorias();
            model.addAttribute("categorias", categorias);
            return "/view/categoria/categoriaList";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/categoria/categoriaList";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el sistema");
            return "/view/categoria/categoriaList";
        }
    }

    /**
     * Da de baja un categoría
     * @param id String
     * @param model Model
     * @return String
     */
    @GetMapping("/bajaCategoria")
    public String baja(@RequestParam(value="id") String id, Model model) {
        try {
            categoriaService.eliminarCategoria(id);
            return "redirect:/categorias";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/categoria/categoriaList";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error con el formulario");
            return "/view/categoria/categoriaList";
        }
    }

    /**
     * Redirige a la vista con el formulario para realizar el alta
     * @param model Model
     * @param categoria Categoria
     * @return String
     */
    @GetMapping("/altaCategoria")
    public String alta(Model model, Categoria categoria) {
        model.addAttribute("isDisabled", false);
        return "/view/categoria/categoriaEdit";
    }

    /**
     * Busca la categoría y lo guarda en el modelo para mostrarlo en la vista de modificación
     * @param model Model
     * @param id String
     * @return String
     */
    @GetMapping("/modificarCategoria")
    public String modificar(Model model, @RequestParam("id") String id) {
        try {
            Categoria categoria = categoriaService.buscarCategoria(id);
            model.addAttribute("categoria", categoria);
            model.addAttribute("isDisabled", false);
            return "/view/categoria/categoriaEdit";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/categoria/categoriaEdit";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el sistema");
            return "/view/categoria/categoriaEdit";
        }
    }

    /**
     * Busca la categoría y la agrega al modelo para ser visualizada
     * @param model Model
     * @param id String
     * @return String
     */
    @GetMapping("/consultarCategoria")
    public String consultar(Model model, @RequestParam("id") String id) {
        try {
            Categoria categoria = categoriaService.buscarCategoria(id);
            model.addAttribute("categoria", categoria);
            model.addAttribute("isDisabled", true);
            return "/view/categoria/categoriaEdit";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/categoria/categoriaEdit";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el sistema");
            return "/view/categoria/categoriaEdit";
        }
    }

    /**
     * Realiza el proceso necesario para agregar o modificar la categoría luego de enviar el formulario
     * @param model Model
     * @param categoria Categoria
     * @param result BindingResult
     * @param attributes RedirectAttributes
     * @return String
     * @throws ErrorServiceException
     */
    @PostMapping("categoria/aceptarEditCategoria")
    public String aceptarEdit(Model model, Categoria categoria, BindingResult result, RedirectAttributes attributes) throws ErrorServiceException {
        try {
            if (result.hasErrors()) {
                model.addAttribute("mensajeError", "Error en el formulario");
                return "/view/categoria/categoriaEdit";
            }

            if (categoria.getId() == null || categoria.getId().isEmpty()) {
                categoriaService.crearCategoria(categoria.getNombre(), categoria.getCategoriaImg());
            } else {
                categoriaService.modificarCategoria(categoria.getId(), categoria.getNombre(), categoria.getCategoriaImg());
            }

            return "redirect:/categorias";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/categoria/categoriaEdit";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el formulario");
            return "/view/categoria/categoriaEdit";
        }
    }
}

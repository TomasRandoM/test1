package com.ingenieriaSoftware.videojuegos.controller;

import com.ingenieriaSoftware.videojuegos.business.domain.Estudio;
import com.ingenieriaSoftware.videojuegos.business.logic.ErrorServiceException;
import com.ingenieriaSoftware.videojuegos.business.logic.EstudioService;
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
public class EstudioController {
    @Autowired
    private EstudioService estudioService;

    @GetMapping("/estudios")
    public String listarEstudios(Model model) throws ErrorServiceException {
        try {
            Collection<Estudio> estudios = estudioService.listarEstudios();
            model.addAttribute("estudios", estudios);
            return "/view/estudio/estudioList";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/estudio/estudioList";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el sistema");
            return "/view/estudio/estudioList";
        }
    }

    /**
     * Da de baja un estudio
     * @param id String
     * @param model Model
     * @return String
     */
    @GetMapping("/bajaEstudio")
    public String baja(@RequestParam(value="id") String id, Model model) {
        try {
            estudioService.eliminarEstudio(id);
            return "redirect:/estudios";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/estudio/estudioList";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error con el formulario");
            return "/view/estudio/estudioList";
        }
    }

    /**
     * Redirige a la vista con el formulario para realizar el alta
     * @param model Model
     * @param estudio Estudio
     * @return String
     */
    @GetMapping("/altaEstudio")
    public String alta(Model model, Estudio estudio) {
        model.addAttribute("isDisabled", false);
        return "/view/estudio/estudioEdit";
    }

    /**
     * Busca el estudio y lo guarda en el modelo para mostrarlo en la vista de modificación
     * @param model Model
     * @param id String
     * @return String
     */
    @GetMapping("/modificarEstudio")
    public String modificar(Model model, @RequestParam("id") String id) {
        try {
            Estudio estudio = estudioService.buscarEstudio(id);
            model.addAttribute("estudio", estudio);
            model.addAttribute("isDisabled", false);
            return "/view/estudio/estudioEdit";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/estudio/estudioEdit";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el sistema");
            return "/view/estudio/estudioEdit";
        }
    }

    /**
     * Busca el estudio y lo agrega al modelo para ser visualizado
     * @param model Model
     * @param id String
     * @return String
     */
    @GetMapping("/consultarEstudio")
    public String consultar(Model model, @RequestParam("id") String id) {
        try {
            Estudio estudio = estudioService.buscarEstudio(id);
            model.addAttribute("estudio", estudio);
            model.addAttribute("isDisabled", true);
            return "/view/estudio/estudioEdit";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/estudio/estudioEdit";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el sistema");
            return "/view/estudio/estudioEdit";
        }
    }

    /**
     * Realiza el proceso necesario para agregar o modificar el estudio luego de enviar el formulario
     * @param model Model
     * @param estudio Estudio
     * @param result BindingResult
     * @param attributes RedirectAttributes
     * @return String
     * @throws ErrorServiceException
     */
    @PostMapping("estudio/aceptarEditEstudio")
    public String aceptarEdit(Model model, Estudio estudio, BindingResult result, RedirectAttributes attributes) throws ErrorServiceException {
        try {
            if (result.hasErrors()) {
                model.addAttribute("mensajeError", "Error en el formulario");
                return "/view/estudio/estudioEdit";
            }

            if (estudio.getId() == null || estudio.getId().isEmpty()) {
                estudioService.crearEstudio(estudio.getNombre(), estudio.getEstudioImg(), estudio.getPaginaWeb());
            } else {
                estudioService.modificarEstudio(estudio.getId(), estudio.getNombre(), estudio.getEstudioImg(), estudio.getPaginaWeb());
            }

            return "redirect:/estudios";
        } catch (ErrorServiceException ex) {
            model.addAttribute("mensajeError", ex.getMessage());
            return "/view/estudio/estudioEdit";
        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error en el formulario");
            return "/view/estudio/estudioEdit";
        }
    }
}

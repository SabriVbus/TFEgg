/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.proyecto.comparaTecno.controlador;

import egg.proyecto.comparaTecno.entidades.Proveedor;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.servicio.ProveedorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author OMEN LEZE
 */
@Controller
@RequestMapping("/proveedor")
public class ProveedorControlador {

    @Autowired
    private ProveedorServicio proveedorServicio;
    
    private String errorEliminar;
    private String idProveedor;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modProv/{idProveedor}")
    public String modProv(@PathVariable String idProveedor, ModelMap modelo) throws ExceptionCreados{
        
        this.idProveedor= idProveedor;
        
        Proveedor proveedor = proveedorServicio.buscarPorId(this.idProveedor);
        modelo.put("nombre", proveedor.getNombre());
        modelo.put("archivo", proveedor.getFotoProveedor());
        return "modificarProveedor.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registrarProveedor")
    public String registroVeedor(ModelMap modelo) {
        modelo.addAttribute("proveedor", proveedorServicio.listarProveedores());
        if (this.errorEliminar !=null) {
           modelo.put("ErrorEliminar", this.errorEliminar);
           this.errorEliminar= null;
        }

        return "registroProveedor.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cargarProveedor")
    public String nuevoProveedor(ModelMap modelo, @RequestParam String nombre, MultipartFile archivo) {
        try {
            proveedorServicio.nuevoProveedor(nombre, archivo);

            modelo.put("exito", "registro de proveedor exitoso");
            return "redirect:/proveedor/registrarProveedor";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "redirect:/proveedor/registrarProveedor";
        }

    }

    @PostMapping("/buscar")
    public String buscarProveedor(ModelMap modelo, @RequestParam(value = "nombre") String nombre) {

        List<Proveedor> proveedor;
        System.out.println("******* PALABRA PARA BUSCAR= " + nombre);
        try {
            proveedor = proveedorServicio.buscarProveedor(nombre);
            modelo.addAttribute("proveedores", proveedor);
            modelo.put("resultados", "Resultados de la busqueda: ");
            return "buscador.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//			e.printStackTrace();
            System.out.println(e.getMessage());
            return "buscador.html";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modificarProveedor")
    public String modificarProveedor(ModelMap modelo, @RequestParam String nombre, @RequestParam MultipartFile archivo) throws Exception {

        try {
            proveedorServicio.modificarProveedor(this.idProveedor, nombre, archivo);
            return "redirect:/proveedor/registrarProveedor";
        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());            
            e.printStackTrace();
        }

        return "modificarProveedor.html";
    }

    @GetMapping("/bajaProveedor/{id}")
    public String bajaProveedor(@PathVariable(value = "id") String id, ModelMap model) {

        try {
            proveedorServicio.eliminarProveedor(id);           
            return "redirect:/proveedor/registrarProveedor";
            
        } catch (Exception e) {
            
           this.errorEliminar="No se puede eliminar porque está relacionado con un link";
            e.printStackTrace();
            return "redirect:/proveedor/registrarProveedor";
        }

    }

}

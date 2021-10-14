package egg.proyecto.comparaTecno.controlador;

import egg.proyecto.comparaTecno.entidades.Producto;
import egg.proyecto.comparaTecno.entidades.Usuario;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.servicio.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "registroUsuario.html";
    }

    @PostMapping("/registro")
    public String crear(ModelMap modelo, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String mail, @RequestParam String password, @RequestParam String password2) {

        try {
            usuarioServicio.crearUsuario(nombre, apellido, mail, password,password2, "CLIENTE");

            modelo.put("exito", "registro exitoso");
            return "redirect:/login";
        } catch (Exception e) {
//            e.printStackTrace();
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("password", password);
            modelo.put("password2", password2);
            return "registroUsuario.html";
        }
    }

    @GetMapping("/perfilUsuario")
    public String perfil(HttpSession session, ModelMap modelo) {

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            List<Producto> lista = usuarioServicio.listaFavorito(usuario.getIdUsuario());
            
            modelo.put("usuario", usuario);
            modelo.addAttribute("favoritos2", lista);

            return "perfilUsuario.html";

        } catch (Exception e) {

            e.printStackTrace();
            return "index.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/baja")
    public String baja(HttpSession session) {

        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            usuarioServicio.baja(usuario.getIdUsuario());
            System.out.println("");
            return "redirect:/logout";

        } catch (Exception e) {
            e.printStackTrace();
            return "perfilUsuario.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/guardaFavorito/{idProducto}")
    public String guardaFavorito(HttpSession session, @PathVariable(value = "idProducto") String id) {

        try {

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            
            usuarioServicio.guardarFavorito(usuario.getIdUsuario(), id);
            System.out.println("se agregó favorito");
            
            return "redirect:/producto/buscar/vista/" + id;


        } catch (Exception e) {
            
            e.printStackTrace();

            return "perfilUsuario.html";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_CLIENTE')")
    @GetMapping("/quitarFavorito/{idProducto}")
    public String quitarFavorito(HttpSession session, @PathVariable(value = "idProducto") String id) {

        try {

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            
            usuarioServicio.eliminarFavorito(usuario.getIdUsuario(), id);
            System.out.println("se elimino favorito");
            
            return "redirect:/usuario/perfilUsuario";


        } catch (Exception e) {
            
            e.printStackTrace();

            return "index.html";
        }
    }

    @GetMapping("/modificarUsuario")
    public String modificarUsuario(@RequestParam String id, ModelMap modelo) {
    	
    	try {
			Usuario usuario = usuarioServicio.buscarPorId(id);
			modelo.addAttribute("perfil", usuario);
		} catch (ExceptionCreados e) {
			modelo.addAttribute("error", e.getMessage());
		}
    	
    	return "modificarUsuario.html";
    }
    
    
    @PostMapping("/guardarModificacionUsuario")
    public String guardarModificarUsuario(ModelMap modelo, HttpSession session, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String mail, @RequestParam String password, @RequestParam String password2, @RequestParam String rol) {
    	
    	Usuario usuario =null;
    	
    	try {
			usuario = usuarioServicio.buscarPorId(id);
			usuarioServicio.modificarUsuario(id, nombre, apellido, mail, password, password2, rol);
			session.setAttribute("usuario", usuario);
			
			return "redirect:/usuario/perfilUsuario";
			
		} catch (ExceptionCreados e) {
			
			modelo.put("error", e.getMessage());
			modelo.put("perfil", usuario);
			modelo.put("password", password);
			modelo.put("password", password2);
			
			return "modificarUsuario.html";
		}
    		
    }
    
    
    
}

package egg.proyecto.comparaTecno.controlador;

import egg.proyecto.comparaTecno.entidades.Link;
import egg.proyecto.comparaTecno.entidades.Producto;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.servicio.LinkServicio;
import egg.proyecto.comparaTecno.servicio.ProductoServicio;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/producto")
public class ProductoControlador {

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private LinkServicio linkServicio;

    private String nombreProducto;
    private String errorEliminar;

    @GetMapping("/productos")
    public String productos() {

        return "registrarProducto";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registrarProducto")
    public String registrarProducto() {
        return "registroProducto.html";
    }

    @GetMapping("/modificarProducto")
    public String modificarProducto(ModelMap modelo, @RequestParam String id) throws ExceptionCreados {

        Producto producto = productoServicio.buscarPorId(id);
        modelo.addAttribute("producto", producto);

        return "modificarProducto.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cargarProducto")
    public String crear(ModelMap modelo, @RequestParam String nombre, @RequestParam String marca,
            @RequestParam String tipo, @RequestParam String descripcion, MultipartFile file) {

        try {

            productoServicio.crearProducto(nombre, marca, tipo, descripcion, file);

            modelo.put("exito", "registro de producto exitoso");
            return "index.html";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "registroProducto.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE')")
    @PostMapping({"/buscar"})
    public String buscarProducto(ModelMap modelo, @RequestParam(value = "nombre") String nombre) {

        List<Producto> producto;
        System.out.println("******* PALABRA PARA BUSCAR= " + nombre);
        try {
            producto = productoServicio.buscarProducto(nombre);
            HashSet<String> marca = productoServicio.filtroMarcas(producto);
            this.nombreProducto = nombre;

            modelo.addAttribute("productos", producto);
            modelo.addAttribute("marcas", marca);
            modelo.put("resultados", "Resultados de la busqueda: ");
            return "buscador.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//			e.printStackTrace();
            System.out.println(e.getMessage());
            return "buscador.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE')")
    @GetMapping("/buscar/vista/{idProducto}")
    public String buscarProductoVista(ModelMap modelo, @PathVariable(value = "idProducto") String idProducto) {

        Producto producto;
        System.out.println("******* ID del producto= " + idProducto);
        try {
            producto = productoServicio.buscarPorId(idProducto);

            modelo.addAttribute("productos", producto);

            List<Link> linkProducto = linkServicio.listarLinks(idProducto);
            modelo.addAttribute("links", linkProducto);

            return "producto.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//			e.printStackTrace();
            System.out.println(e.getMessage());
            return "producto.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE')")
    @GetMapping("/ordenar/precio/menor/{idProducto}")
    public String ordernarPorPrecioMenor(ModelMap modelo, @PathVariable(value = "idProducto") String idProducto) {

        Producto producto;
        System.out.println("******* ID del producto= " + idProducto);
        try {
            producto = productoServicio.buscarPorId(idProducto);

            modelo.addAttribute("productos", producto);

            List<Link> linkProducto = linkServicio.ordenarPorPrecioMenor(idProducto);
            modelo.addAttribute("links", linkProducto);

            return "producto.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//			e.printStackTrace();
            System.out.println(e.getMessage());
            return "producto.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE')")
    @GetMapping("/ordenar/precio/mayor/{idProducto}")
    public String ordernarPorPrecioMayor(ModelMap modelo, @PathVariable(value = "idProducto") String idProducto) {

        Producto producto;
        System.out.println("******* ID del producto= " + idProducto);
        try {
            producto = productoServicio.buscarPorId(idProducto);

            modelo.addAttribute("productos", producto);

            List<Link> linkProducto = linkServicio.ordenarPorPrecioMayor(idProducto);
            modelo.addAttribute("links", linkProducto);

            return "producto.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//			e.printStackTrace();
            System.out.println(e.getMessage());
            return "producto.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE')")
    @GetMapping({"/buscar/{marca}"})
    public String buscarPorMarca(@PathVariable(value = "marca", required = false) String marca, ModelMap modelo) {

        List<Producto> producto;
        System.out.println("******* MARCA PARA BUSCAR= " + marca + " " + this.nombreProducto);

        try {
            producto = productoServicio.buscarPorMarca(marca, this.nombreProducto);
            modelo.addAttribute("productos", producto);
            modelo.put("resultados", "Resultados de la busqueda: ");
            return "buscador.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//   			e.printStackTrace();
            System.out.println(e.getMessage());
            return "buscador.html";
        }
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENTE')")
//   	@GetMapping({"/buscar/{precio1}/{precio2}/{nombre}"})
//   	public String buscarPorPrecio(@PathVariable(value="precio1", required = false) double precio1,@PathVariable(value="precio2", required = false) double precio2,@PathVariable(value="nombre", required = false) String nombre,ModelMap modelo) {
//    	
//   		List<Producto> producto;
//   		System.out.println("******* MARCA PARA BUSCAR= " + nombre +" "+ this.nombreProducto);
//   		
//   		try {
//   			producto = linkServicio.buscarPorPrecio(precio1,precio2,nombre);
//   			modelo.addAttribute("productos", producto);
//   			modelo.put("resultados", "Resultados de la busqueda: ");
//   			return "buscador.html";
//   			
//   		} catch (ExceptionCreados e) {
//   			modelo.put("error", e.getMessage());
////   			e.printStackTrace();
//   			System.out.println(e.getMessage());
//   			return "buscador.html";
//   		}
//   	}
    @PostMapping({"/buscar/{tipo}"})
    public String buscarPorTipo(@PathVariable(value = "tipo", required = false) String tipo, ModelMap modelo) {

        List<Producto> producto;
        System.out.println("******* TIPO PARA BUSCAR= " + tipo);
        try {
            producto = productoServicio.buscarPorTipo(tipo);
            modelo.addAttribute("productos", producto);
            modelo.put("resultados", "Resultados de la busqueda: ");
            return "buscador.html";

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
//   			e.printStackTrace();
            System.out.println(e.getMessage());
            return "buscador.html";
        }
    }

    @GetMapping("/eliminarProducto")
    public String eliminarProducto(ModelMap modelo, @RequestParam String id) {

        try {
            productoServicio.darBaja(id);
            return "index.html";
        } catch (ExceptionCreados e) {

            this.errorEliminar = "No se puede eliminar porque está relacionado con un link y/o proveedor";
            e.printStackTrace();
            return "redirect:/producto/buscar/vista/" + id;
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/guardarModificarProducto")
    public String modificarProducto(ModelMap modelo, @RequestParam String id, @RequestParam String nombre,
            @RequestParam String marca, @RequestParam String tipo, @RequestParam String descripcion, MultipartFile file) throws Exception {

        try {
            productoServicio.modificarProducto(id, nombre, marca, tipo, descripcion, file);

            return "redirect:/producto/buscar/vista/" + id;

        } catch (ExceptionCreados e) {
            modelo.put("error", e.getMessage());
            Producto producto = productoServicio.buscarPorId(id);
            modelo.addAttribute("producto", producto);
            e.printStackTrace();
        }
        return "modificarProducto.html";

    }

}

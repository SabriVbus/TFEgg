package egg.proyecto.comparaTecno.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egg.proyecto.comparaTecno.entidades.Link;
import egg.proyecto.comparaTecno.entidades.Proveedor;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.servicio.LinkServicio;
import egg.proyecto.comparaTecno.servicio.ProveedorServicio;

@Controller
@RequestMapping("/link")
public class LinkControlador {

	@Autowired
	private LinkServicio linkServicio;

	@Autowired
	private ProveedorServicio proveedorServicio;

	private String idProduc;
	private String idLink;

	@GetMapping("/{idProducto}/{idLink}")
	public String nuevoLink(ModelMap modelo, @PathVariable(value = "idProducto") String idProducto,
			@PathVariable(value = "idLink", required = false) String idLink) {

		this.idProduc = idProducto;
		this.idLink = idLink;
		System.out.println("***********" + idProducto);
		System.out.println("***********" + idLink);
		List<Proveedor> proveedores = proveedorServicio.listarProveedores();

		if (idLink.equals("null")) {

			modelo.addAttribute("titulo", "Nuevo Link");
			modelo.addAttribute("boton", "Registrar Link");
			modelo.addAttribute("action", "N");
		} else {

			Link link = linkServicio.buscarLink(this.idLink);

			modelo.put("url", link.getUrl());
			modelo.put("precio", link.getPrecioProducto());
			modelo.put("idProveedor", link.getProveedor().getIdProveedor());
			modelo.addAttribute("titulo", "Modificar Link");
			modelo.addAttribute("boton", "Guardar");
			modelo.addAttribute("action", "U");
		}

		modelo.addAttribute("proveedores", proveedores);

		return "registroLink.html";
	}

	@PostMapping("/cargarLink")
	public String cargarLink(ModelMap modelo, @RequestParam String url, @RequestParam Double precio,
			@RequestParam String idProveedor) {

		try {

			linkServicio.nuevoLink(url, precio, idProveedor, this.idProduc);

			return "redirect:/producto/buscar/vista/" + this.idProduc;

		} catch (ExceptionCreados e) {
			e.printStackTrace();

			modelo.put("error", e.getMessage());
			modelo.put("url", url);
			modelo.put("precio", precio);

			return "registroLink.html";

		}

	}

	@PostMapping("/modificarLink")
	public String modificarLink(ModelMap modelo, @RequestParam String url, @RequestParam Double precio,
			@RequestParam String idProveedor) {

		try {

			linkServicio.modificarLink(this.idLink, url, precio, idProveedor, this.idProduc);
			
			return "redirect:/producto/buscar/vista/" + this.idProduc;

		} catch (ExceptionCreados e) {
			e.printStackTrace();

			modelo.put("error", e.getMessage());
			modelo.put("url", url);
			modelo.put("precio", precio);

			return "registroLink.html";
		}

		
	}

	@GetMapping("/eliminarLink/{idProducto}/{idLink}")
	public String eliminarLink( @PathVariable(value = "idProducto") String idProducto,@PathVariable(value = "idLink") String idLink) {
		this.idProduc=idProducto;
		linkServicio.eliminarLink(idLink);
		return "redirect:/producto/buscar/vista/" + this.idProduc;
	}

}

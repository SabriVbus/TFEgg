package egg.proyecto.comparaTecno.servicio;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egg.proyecto.comparaTecno.entidades.Link;
import egg.proyecto.comparaTecno.entidades.Producto;
import egg.proyecto.comparaTecno.entidades.Proveedor;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.repositorio.LinkRepositorio;
import egg.proyecto.comparaTecno.repositorio.ProductoRepositorio;
import egg.proyecto.comparaTecno.repositorio.ProveedorRepositorio;

@Service
public class LinkServicio {

	@Autowired
	private LinkRepositorio linkRepositorio;

	@Autowired
	private ProveedorRepositorio proveedorRepositorio;

	@Autowired
	private ProductoRepositorio productoRepositorio;

	public void nuevoLink(String url, Double precio, String proveedor, String producto) throws ExceptionCreados {

		validar(url, precio, proveedor, producto);

		Optional<Proveedor> respuesta = proveedorRepositorio.findById(proveedor);
		Optional<Producto> respuesta2 = productoRepositorio.findById(producto);

		if (respuesta2.isPresent()) {

			if (respuesta.isPresent()) {

				Producto productoBuscado = respuesta2.get();
				Proveedor proveedorBuscado = respuesta.get();

				Link link = new Link();
				link.setUrl(url);
				link.setPrecioProducto(precio);
				link.setAlta(new Date());
				link.setProveedor(proveedorBuscado);
				link.setProducto(productoBuscado);

				linkRepositorio.save(link);

			} else {
				throw new ExceptionCreados("No se encontro proveedor");
			}

		} else {
			throw new ExceptionCreados("No se encontro producto");
		}

	}

	public void modificarLink(String id, String url, Double precio, String proveedor, String producto)
			throws ExceptionCreados {

		validar(url, precio, proveedor, producto);

		Optional<Link> respuesta = linkRepositorio.findById(id);

		if (respuesta.isPresent()) {
			Optional<Proveedor> respuestaProveedor = proveedorRepositorio.findById(proveedor);
			Optional<Producto> respuesta2 = productoRepositorio.findById(producto);

			if (respuesta2.isPresent()) {
				if (respuestaProveedor.isPresent()) {
					Producto productoBuscado = respuesta2.get();
					Proveedor proveedorBuscado = respuestaProveedor.get();

					Link link = respuesta.get();
					link.setUrl(url);
					link.setPrecioProducto(precio);
					link.setProveedor(proveedorBuscado);
					link.setProducto(productoBuscado);

					linkRepositorio.save(link);
					
				} else {
					throw new ExceptionCreados("No se encontro proveedor");
				}
			} else {
				throw new ExceptionCreados("No se encontro producto");
			}
		} else {
			throw new ExceptionCreados("No se encontro la url");
		}

	}

	public void bajaLink(String id) throws ExceptionCreados {

		Optional<Link> respuesta = linkRepositorio.findById(id);

		if (respuesta.isPresent()) {

			Link link = respuesta.get();
			link.setBaja(new Date());
			linkRepositorio.save(link);

		} else {
			throw new ExceptionCreados("No se encontro la url");
		}

	}

	private void validar(String url, Double precio, String proveedor, String producto) throws ExceptionCreados {

		if (url.isEmpty() || url.equals(null)) {
			throw new ExceptionCreados("La url esta vacia");
		}

		if (precio.equals(null)) {
			throw new ExceptionCreados("La precio esta vacio");
		}

		if (proveedor.isEmpty() || proveedor.equals(null)) {
			throw new ExceptionCreados("Seleccione el proveedor");
		}

		if (producto.isEmpty() || producto.equals(null)) {
			throw new ExceptionCreados("Producto invalido");
		}

	}
	
	public List<Link> listarLinks(String idProducto) {
		return linkRepositorio.listarLink(idProducto);		
	}
	
	
	public List<Link> ordenarPorPrecioMenor(String idProducto) {
		return linkRepositorio.ordenarPorPrecioMenor(idProducto);		
	}
	
	public List<Link> ordenarPorPrecioMayor(String idProducto) {
		return linkRepositorio.ordenarPorPrecioMayor(idProducto);		
	}
	
	public Link buscarLink(String idLink) {
		return linkRepositorio.buscarLink(idLink);
	}
	
	public void eliminarLink(String idlink) {
		linkRepositorio.deleteById(idlink);
	}
	
//	public List<Producto> buscarPorPrecio(double precio1, double precio2, String nombre) throws ExceptionCreados {
//		
//		List<Producto> respuesta = linkRepositorio.buscarPorPrecio(precio1, precio2, nombre);
//		if (respuesta.isEmpty()) {
//
//			throw new ExceptionCreados("No se encontraron productos");
//
//		}	
//		
//		return respuesta;
//
//	}
	
	
	

}

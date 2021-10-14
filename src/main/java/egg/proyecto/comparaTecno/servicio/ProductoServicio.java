package egg.proyecto.comparaTecno.servicio;

import egg.proyecto.comparaTecno.entidades.Foto;
import egg.proyecto.comparaTecno.entidades.Producto;

import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.repositorio.ProductoRepositorio;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
@Service
public class ProductoServicio {

	@Autowired
	private ProductoRepositorio productoRepositorio;

	@Autowired
	private FotoServicio fotoServicio;

	public void crearProducto(String nombre, String marca, String tipo, String descripcion, MultipartFile archivo)
			throws ExceptionCreados {
		validar(nombre, marca, tipo, descripcion);
		Producto producto = new Producto();
		producto.setNombre(nombre);
		producto.setMarca(marca);
		producto.setTipo(tipo);
		producto.setDescripcion(descripcion);
		producto.setAlta(new Date());

		Foto foto = fotoServicio.guardar(archivo);
		producto.setFoto(foto);

		productoRepositorio.save(producto);

	}

	public void darBaja(String id) throws ExceptionCreados {
		Optional<Producto> respuesta = productoRepositorio.findById(id);

		if (respuesta.isPresent()) {
			Producto producto = respuesta.get();
			producto.setBaja(new Date());
			productoRepositorio.save(producto);

		} else {
			throw new ExceptionCreados("No se encontro el producto solicitado");
		}
	}

        @Transactional
	public void modificarProducto(String id, String nombre, String marca, String tipo, String descripcion,
			MultipartFile archivo) throws ExceptionCreados {

		validar(nombre, marca, tipo, descripcion);
		Optional<Producto> respuesta = productoRepositorio.findById(id);

		if (respuesta.isPresent()) {
			Producto producto = respuesta.get();
			producto.setNombre(nombre);
			producto.setMarca(marca);
			producto.setTipo(tipo);
			producto.setDescripcion(descripcion);

			String idFoto = null;
			if (producto.getFoto() != null) {
				idFoto = producto.getFoto().getId();
			}
			Foto foto = fotoServicio.actualizar(idFoto, archivo);
			producto.setFoto(foto);

			productoRepositorio.save(producto);

		} else {
			throw new ExceptionCreados("No se encontro el producto solicitado");
		}

	}

	public void validar(String nombre, String marca, String tipo, String descripcion) throws ExceptionCreados {
		if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
			throw new ExceptionCreados("Debe tener un nombre valido");
		}

		if (marca == null || marca.isEmpty() || marca.contains("  ")) {
			throw new ExceptionCreados("Debe tener una marca valida");
		}

		if (tipo == null || tipo.isEmpty() || tipo.contains("  ")) {
			throw new ExceptionCreados("Debe tener un tipo de producto valido");
		}

		if (descripcion == null || descripcion.isEmpty() || descripcion.contains("  ")) {
			throw new ExceptionCreados("Debe tener una descripcion valida");
		}

	}

	public List<Producto> buscarProducto(String nombre) throws ExceptionCreados {

		List<Producto> respuesta = productoRepositorio.buscarPorNombre(nombre);
		if (respuesta.isEmpty()) {

			throw new ExceptionCreados("No se encontraron productos");

		}
		return respuesta;

	}

	public List<Producto> buscarPorMarca(String marca, String nombre) throws ExceptionCreados {
		
		List<Producto> respuesta = productoRepositorio.buscarPorMarca(marca, nombre);
		if (respuesta.isEmpty()) {

			throw new ExceptionCreados("No se encontraron productos");

		}	
		
		return respuesta;

	}
	
	public HashSet<String> filtroMarcas(List<Producto> productos) throws ExceptionCreados {
		
		HashSet<String> marcas = new HashSet<>();
		
		for (Producto producto : productos) {
			
			marcas.add(producto.getMarca());
			
		}
				
		return marcas;
		
			
	}
	

	public List<Producto> buscarPorTipo(String tipo) throws ExceptionCreados {

		List<Producto> respuesta = productoRepositorio.buscarPorTipo(tipo);
		if (respuesta.isEmpty()) {

			throw new ExceptionCreados("No se encontraron productos");

		}
		return respuesta;

	}
	
	public Producto buscarPorId(String id)throws ExceptionCreados {

		Optional<Producto> respuesta = productoRepositorio.findById(id);

		if (respuesta.isPresent()) {
			Producto producto = respuesta.get();
			return producto;
			
		} else {
			throw new ExceptionCreados("No se encontro el producto solicitado");
			}
		 
	}

}

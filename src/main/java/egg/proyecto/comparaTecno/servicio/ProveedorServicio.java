package egg.proyecto.comparaTecno.servicio;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.joran.conditional.IfAction;
import egg.proyecto.comparaTecno.entidades.Foto;
import egg.proyecto.comparaTecno.entidades.Proveedor;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.repositorio.ProveedorRepositorio;
import java.util.List;

@Service
public class ProveedorServicio {
	
	@Autowired
	private ProveedorRepositorio proveedorRepositorio;
	
	@Autowired
	private FotoServicio fotoServicio;
	
	@Transactional
	public void nuevoProveedor(String nombre, MultipartFile archivo) throws ExceptionCreados {
		
		validar(nombre);
		
		Proveedor proveedor = new Proveedor();
		proveedor.setNombre(nombre);
		proveedor.setAlta(new Date());
		
		Foto foto = fotoServicio.guardar(archivo);
		proveedor.setFotoProveedor(foto);
		
		proveedorRepositorio.save(proveedor);
				
	}
	
	@Transactional
	public void modificarProveedor(String id, String nombre, MultipartFile archivo) throws ExceptionCreados {
		
		validar(nombre);
		
		Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);
		
		if (respuesta.isPresent()) {
			Proveedor proveedor = respuesta.get();
			proveedor.setNombre(nombre);
			
			String idFoto = null;
			if(archivo!=null) {
				idFoto = proveedor.getFotoProveedor().getId();

                        Foto foto = fotoServicio.actualizar(idFoto, archivo);
			proveedor.setFotoProveedor(foto);                			
			}
			
			proveedorRepositorio.save(proveedor);
			
		}else {
			throw new ExceptionCreados("No se encontro el proveedor solicitado");
		}
				
	}
	
        public void eliminarProveedor(String id) {
		proveedorRepositorio.deleteById(id);
	}
	
	private void validar(String nombre) throws ExceptionCreados {
		
		if (nombre.isEmpty() || nombre.equals(null)) {
			throw new ExceptionCreados("El nombre del proveedor esta vacio");			
		}
		
	}
        public List<Proveedor> buscarProveedor(String nombre) throws ExceptionCreados{
		
    	
    	List<Proveedor> respuesta = proveedorRepositorio.buscarPorNombre(nombre);
    	if(respuesta.isEmpty()) {
    		
    		throw new ExceptionCreados("No se encontraron proveedores");
    		
    	}
    	return respuesta;
    	
    	    	
    }
        
        public List<Proveedor> listarProveedores(){
        	return proveedorRepositorio.findAll();
        }
        
        public Proveedor buscarPorId(String id)throws ExceptionCreados {

		Optional<Proveedor> respuesta = proveedorRepositorio.findById(id);

		if (respuesta.isPresent()) {
			Proveedor proveedor = respuesta.get();
			return proveedor;
			
		} else {
			throw new ExceptionCreados("No se encontro el proveedor solicitado");
			}
		 
	}


}

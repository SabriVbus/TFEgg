package egg.proyecto.comparaTecno.servicio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egg.proyecto.comparaTecno.entidades.Foto;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.repositorio.FotoRepositorio;

@Service
public class FotoServicio {

	@Autowired
	private FotoRepositorio fotoRepositorio;

	public Foto guardar(MultipartFile archivo) throws ExceptionCreados {

		if (archivo != null) {
			try {
				Foto foto = new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());

				return fotoRepositorio.save(foto);
			} catch (Exception e) {

				System.err.println(e.getMessage());
			}

		}

		return null;
	}
	
	
	public Foto actualizar(String idFoto, MultipartFile archivo) throws ExceptionCreados{
		if (archivo != null) {
			try {
				Foto foto = new Foto();
				
				if(idFoto!=null) {
					
					Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
					if(respuesta.isPresent()) {
						foto = respuesta.get();
					}
				}
				
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());

				return fotoRepositorio.save(foto);
			} catch (Exception e) {

				System.err.println(e.getMessage());
			}

		}

		return null;
	}
	
	

}

package egg.proyecto.comparaTecno.servicio;

import egg.proyecto.comparaTecno.entidades.Producto;
import egg.proyecto.comparaTecno.entidades.Usuario;
import egg.proyecto.comparaTecno.enums.Rol;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.repositorio.ProductoRepositorio;
import egg.proyecto.comparaTecno.repositorio.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 */
@Service
public class UsuarioServicio implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private ProductoRepositorio productoRepositorio;

	@Transactional
	public void crearUsuario(String nombre, String apellido, String mail, String password, String password2, String rol)
			throws ExceptionCreados {

		validar(nombre, apellido, mail, password, rol);
		validarPass(password, password2);
		validarCorreo(mail);

		Usuario usuario = new Usuario();

		usuario.setNombre(nombre);
		usuario.setApellido(apellido);
		usuario.setMail(mail);
		usuario.setPassword(new BCryptPasswordEncoder().encode(password));
		usuario.setRol(Rol.valueOf(rol));
		usuario.setActivo(true);
		usuario.setAlta(new Date());

		usuarioRepositorio.save(usuario);

	}

	@Transactional
	public void modificarUsuario(String id, String nombre, String apellido, String mail, String password,
			String password2, String rol) throws ExceptionCreados {

		validar(nombre, apellido, mail, password, rol);
		validarPass(password, password2);

		Optional<Usuario> respuesta = usuarioRepositorio.findById(id);

		if (respuesta.isPresent()) {

			Usuario usuario = respuesta.get();

			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setMail(mail);
			usuario.setPassword(new BCryptPasswordEncoder().encode(password));
			usuario.setActivo(true);

			usuarioRepositorio.save(usuario);
		} else {
			throw new ExceptionCreados("No se encontro el Usuario");
		}

	}

	public Usuario buscarPorId(String id)throws ExceptionCreados {

		Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
		if (respuesta.isPresent()) {

			Usuario usuario = respuesta.get();
			return usuario;
		} else {
			throw new ExceptionCreados("No se encontro el Usuario");
		}

	}

	private void validarPass(String password, String password2) throws ExceptionCreados {

		if (!password.equals(password2)) {
			throw new ExceptionCreados("Las claves son diferentes");
		}
	}

	private void validarCorreo(String mail) throws ExceptionCreados {
		List<Usuario> respuesta = usuarioRepositorio.validarEmail(mail);
		// System.out.println(usuario.getMail());

		if (!respuesta.isEmpty()) {
			throw new ExceptionCreados("El Email ya esta en uso");
		}

	}

	public void baja(String id) throws ExceptionCreados {

		Usuario usuario = usuarioRepositorio.getById(id);

		usuario.setBaja(new Date());

		usuario.setActivo(false);

		usuarioRepositorio.save(usuario);
	}

	public void guardarFavorito(String idU, String idP) throws ExceptionCreados {

		Usuario usuario = usuarioRepositorio.findById(idU).get();
		Producto producto = productoRepositorio.findById(idP).get();

		List<Producto> favoritoP = usuario.getProductoFavorito();
		favoritoP.add(producto);

		usuario.setProductoFavorito(favoritoP);
		usuarioRepositorio.save(usuario);

	}

	public List<Producto> listaFavorito(String idU) throws ExceptionCreados {

		Usuario usuario = usuarioRepositorio.findById(idU).get();
		List<Producto> productos = usuario.getProductoFavorito();

		return productos;

	}

	public void eliminarFavorito(String idU, String idP) {
		Usuario usuario = usuarioRepositorio.findById(idU).get();
		Producto producto = productoRepositorio.findById(idP).get();

		List<Producto> favoritoP = usuario.getProductoFavorito();
//        favoritoP.remove()
//        usuarioRepositorio.
		Iterator<Producto> it = favoritoP.iterator();

		while (it.hasNext()) {
			Producto producto1 = it.next();
			if (producto1.getIdProducto().equals(idP)) {
				it.remove();

			}
		}
		usuario.setProductoFavorito(favoritoP);
		usuarioRepositorio.save(usuario);
	}

	public void validar(String nombre, String apellido, String mail, String password, String rol)
			throws ExceptionCreados {

		if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
			throw new ExceptionCreados("Debe tener un nombre valido");
		}

		if (apellido == null || apellido.isEmpty() || apellido.contains("  ")) {
			throw new ExceptionCreados("Debe tener un apellido valido");
		}

		if (mail == null || mail.isEmpty() || mail.contains("  ")) {
			throw new ExceptionCreados("Debe tener un email valido");
		}

		if (password == null || password.isEmpty() || password.contains("  ") || password.length() < 6) {
			throw new ExceptionCreados("Debe tener una clave valida, mínimo 6 caractéres");
		}

		if (!Rol.ADMIN.toString().equals(rol) && !Rol.CLIENTE.toString().equals(rol)) {
			throw new ExceptionCreados("Debe tener rol valido");
		}
	}

	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepositorio.buscarPorEmail(mail);

		if (usuario != null) {
			List<GrantedAuthority> permiso = new ArrayList<>();
			GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
			permiso.add(p);
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuario", usuario);

			return new org.springframework.security.core.userdetails.User(usuario.getMail(), usuario.getPassword(),
					permiso);
		}

		return null;

	}
}

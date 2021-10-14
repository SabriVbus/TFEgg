package egg.proyecto.comparaTecno.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import egg.proyecto.comparaTecno.entidades.Link;
import egg.proyecto.comparaTecno.entidades.Producto;

@Repository
public interface LinkRepositorio extends JpaRepository<Link, String>{
		
	@Query("SELECT a from Link a WHERE a.producto.idProducto LIKE :idProducto")
	public List<Link> listarLink(@Param("idProducto") String id);
	
	@Query("SELECT a from Link a WHERE a.producto.idProducto LIKE :idProducto ORDER BY a.precioProducto ASC")
	public List<Link> ordenarPorPrecioMenor(@Param("idProducto") String idProducto);

	
	@Query("SELECT a from Link a WHERE a.producto.idProducto LIKE :idProducto ORDER BY a.precioProducto DESC")
	public List<Link> ordenarPorPrecioMayor(@Param("idProducto") String idProducto);
	
	@Query("SELECT a from Link a WHERE a.idLink LIKE :idLink")
	public Link buscarLink(@Param("idLink") String id);
}

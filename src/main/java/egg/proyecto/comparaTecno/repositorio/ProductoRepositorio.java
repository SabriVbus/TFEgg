package egg.proyecto.comparaTecno.repositorio;

import egg.proyecto.comparaTecno.entidades.Producto;
import egg.proyecto.comparaTecno.entidades.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author OMEN LEZE
 */
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, String> {

	 @Query("SELECT a from Producto a WHERE a.nombre LIKE :nombre% AND a.baja=null")
	 public List<Producto> buscarPorNombre(@Param("nombre") String nombre);
                  	 
	 @Query("SELECT DISTINCT a from Producto a WHERE a.marca LIKE :marca AND a.nombre LIKE :nombre% AND a.baja=null")
	 public List<Producto> buscarPorMarca(@Param("marca") String marca, @Param("nombre") String nombre);
	 
	 @Query("SELECT a from Producto a WHERE a.tipo LIKE :tipo")
	 public List<Producto> buscarPorTipo(@Param("tipo") String tipo);
	 
}

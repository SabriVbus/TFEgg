package egg.proyecto.comparaTecno.repositorio;

import egg.proyecto.comparaTecno.entidades.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import egg.proyecto.comparaTecno.entidades.Proveedor;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProveedorRepositorio extends JpaRepository<Proveedor, String>{
	
	 @Query("SELECT a from Proveedor a WHERE a.nombre LIKE :nombre")
	 public List<Proveedor> buscarPorNombre(@Param("nombre") String nombre);
}

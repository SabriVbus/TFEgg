/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.proyecto.comparaTecno.repositorio;

import egg.proyecto.comparaTecno.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepositorio extends JpaRepository <Usuario, String> {

    @Query("SELECT a from Usuario a WHERE a.mail LIKE :mail AND a.activo = true")
    public Usuario buscarPorEmail(@Param("mail") String mail);
    
    @Query("SELECT a from Usuario a WHERE a.mail LIKE :mail AND a.activo = true")
    public List<Usuario> validarEmail(@Param("mail") String mail);
    
    
    
}

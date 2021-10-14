/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package egg.proyecto.comparaTecno.controlador;

import egg.proyecto.comparaTecno.entidades.Producto;
import egg.proyecto.comparaTecno.entidades.Proveedor;
import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.servicio.ProductoServicio;
import egg.proyecto.comparaTecno.servicio.ProveedorServicio;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * @author OMEN LEZE
 */
@Controller
@RequestMapping("/foto")
public class FotoControlador {
    @Autowired
	private ProductoServicio productoServicio;
     @Autowired
	private ProveedorServicio proveedorServicio;
    
    @GetMapping("/producto/{id}")
    public ResponseEntity<byte[]> fotoProducto(@PathVariable String id){
    	
    	try {
			Producto producto = productoServicio.buscarPorId(id);
			
			if (producto.getFoto() == null) {
				throw new ExceptionCreados("El producto no tiene foto");
			}
			
			byte[] foto = producto.getFoto().getContenido();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
						
			return new ResponseEntity<>(foto,headers,HttpStatus.OK);
			
		} catch (ExceptionCreados e) {
			Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	
    	
    }
    
    @GetMapping("/proveedor/{id}")
    public ResponseEntity<byte[]> fotoProveedor(@PathVariable String id){
    	
    	try {
			Proveedor proveedor = proveedorServicio.buscarPorId(id);
			
			if (proveedor.getFotoProveedor() == null) {
				throw new ExceptionCreados("El producto no tiene foto");
			}
			
			byte[] foto = proveedor.getFotoProveedor().getContenido();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
						
			return new ResponseEntity<>(foto,headers,HttpStatus.OK);
			
		} catch (ExceptionCreados e) {
			Logger.getLogger(FotoControlador.class.getName()).log(Level.SEVERE, null, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    	
    	
    }
    
   
}

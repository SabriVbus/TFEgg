package egg.proyecto.comparaTecno.entidades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Proveedor {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String idProveedor;

	private String nombre;

	@OneToOne
	private Foto fotoProveedor;

	@Temporal(TemporalType.DATE)
	private Date alta;

	@Temporal(TemporalType.DATE)
	private Date baja;

	public String getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(String idProveedor) {
		this.idProveedor = idProveedor;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Foto getFotoProveedor() {
		return fotoProveedor;
	}

	public void setFotoProveedor(Foto fotoProveedor) {
		this.fotoProveedor = fotoProveedor;
	}

	public Date getAlta() {
		return alta;
	}

	public void setAlta(Date alta) {
		this.alta = alta;
	}

	public Date getBaja() {
		return baja;
	}

	public void setBaja(Date baja) {
		this.baja = baja;
	}

}

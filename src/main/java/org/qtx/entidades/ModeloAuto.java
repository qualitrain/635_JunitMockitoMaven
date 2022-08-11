package org.qtx.entidades;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="modelo")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class ModeloAuto {
	@Id
	private String claveModelo;
	private String nombre;
	private String version;
	private boolean importado;
	
	@XmlTransient
	@ManyToOne
	@JoinColumn(name="claveArmadora")
	private Armadora armadora;
	
	public ModeloAuto(String claveModelo, String nombre, Armadora armadora, 
			String version, boolean importado) {
		super();
		this.claveModelo = claveModelo;
		this.nombre = nombre;
		this.armadora = armadora;
		this.version = version;
		this.importado = importado;
	}

	public ModeloAuto() {
		super();
	}

	public String getClaveModelo() {
		return claveModelo;
	}

	public void setClaveModelo(String claveModelo) {
		this.claveModelo = claveModelo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Armadora getArmadora() {
		return armadora;
	}

	public void setArmadora(Armadora armadora) {
		this.armadora = armadora;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isImportado() {
		return importado;
	}

	public void setImportado(boolean importado) {
		this.importado = importado;
	}

	@Override
	public String toString() {
		return "ModeloAuto [claveModelo=" + claveModelo + ", nombre=" + nombre + ", armadora=" + armadora.getClave() + ", version="
				+ version + ", importado=" + importado + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(armadora.getClave(), claveModelo, importado, nombre, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModeloAuto other = (ModeloAuto) obj;
		return Objects.equals(armadora.getClave(), other.armadora.getClave()) && Objects.equals(claveModelo, other.claveModelo)
				&& importado == other.importado && Objects.equals(nombre, other.nombre)
				&& Objects.equals(version, other.version);
	}
	
}

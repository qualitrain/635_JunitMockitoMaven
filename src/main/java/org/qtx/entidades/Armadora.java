package org.qtx.entidades;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Armadora {
	private static Logger bitacora = LoggerFactory.getLogger(Armadora.class);

	@Id
	private String clave;
	private String nombre;
	private String paisOrigen;
	private int nPlantas;
	@XmlTransient
	@OneToMany(mappedBy="armadora",cascade = CascadeType.PERSIST)
	private Set<ModeloAuto> modelos;
	
	public Armadora(String clave, String nombre, String paisOrigen, int nPlantas) {
		super();
		this.clave = clave;
		this.nombre = nombre;
		this.paisOrigen = paisOrigen;
		this.nPlantas = nPlantas;
	}
	public Armadora() {
		super();
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPaisOrigen() {
		return paisOrigen;
	}
	public void setPaisOrigen(String paisOrigen) {
		this.paisOrigen = paisOrigen;
	}
	public int getnPlantas() {
		return nPlantas;
	}
	public void setnPlantas(int nPlantas) {
		this.nPlantas = nPlantas;
	}
	public Set<ModeloAuto> getModelos() {
		return modelos;
	}
	public void setModelos(Set<ModeloAuto> modelos) {
		this.modelos = modelos;
	}
	@Override
	public String toString() {
		return "Armadora [clave=" + clave + ", nombre=" + nombre + ", paisOrigen=" + paisOrigen + ", nPlantas="
				+ nPlantas + "]";
	}
	public String toHtml() {
		String html = "<span style='font-weight: bold;'>Clave:</span>" + this.clave + "<br>"
		            + "<span style='font-weight: bold;'>Nombre:</span>" + this.nombre + "<br>"
		            + "<span style='font-weight: bold;'>PaisOrigen:</span>" + this.paisOrigen + "<br>"
		            + "<span style='font-weight: bold;'>N&iacute;umero de plantas:</span>" + this.nPlantas + "<br>";
		return html;
	}
	public boolean equivaleA(Armadora otraArmadora) {
		
		// Monitoreo modo DEBUG
		
		bitacora.debug(this.toString() + " equivaleA(" + otraArmadora + ")");
		
		if(otraArmadora == null)
			return false;
		
		if(this.modelos != null)
		   bitacora.debug(".equivaleA(..): n modelos de this:" + this.modelos.size() );
		else
		   bitacora.debug(".equivaleA(..): this NO tiene modelos asociados:");
			
		if(otraArmadora.tieneModelos())
		   bitacora.debug(".equivaleA(..): n modelos de otraArmadora:" + otraArmadora.modelos.size() );
		else
		   bitacora.debug(".equivaleA(..): otraArmadora NO tiene modelos asociados:");

		// Compara campo por campo
		
		if(this.clave.equals(otraArmadora.clave) == false)
			return false;
		if(this.nombre.equals(otraArmadora.nombre) == false)
			return false;
		if(this.paisOrigen.equals(otraArmadora.paisOrigen) == false)
			return false;
		if(this.nPlantas != otraArmadora.nPlantas)
			return false;
		
		bitacora.debug(".equivaleA(..): armadoras equivalen campo a campo");
		
		// Compara los modelos anidados en la armadora
		
		if(this.tieneModelos() == false) {
			if(otraArmadora.tieneModelos() == false)
				return true;
			else
				return false;
		}
		bitacora.debug(".equivaleA(..): this SI tiene modelos" );
		
		
		// Caso la primera armadora tiene modelos, pero la segunda no
		if(otraArmadora.tieneModelos() == false)
			return false;
		
		//Ambas armadoras tienen modelos asociados		
		if(this.modelos.size() != otraArmadora.modelos.size())
			return false;
		bitacora.debug(".equivaleA(..): Coinciden en cantidad de modelos asociados" );
		
				
		
		if(this.modelos.containsAll(otraArmadora.modelos)) 
			  return true;
		 				
		bitacora.debug(".equivaleA(..): No coinciden en los modelos asociados" );
		bitacora.debug(".equivaleA(..): modelos this:" + this.modelos);
		bitacora.debug(".equivaleA(..): modelos otraArmadora:" + otraArmadora.modelos );
		
		return false;
	
	}
	
	public boolean tieneModelos() {
		if(this.modelos == null)
			return false;
		if(this.modelos.size() == 0)
			return false;
		return true;
	}
}

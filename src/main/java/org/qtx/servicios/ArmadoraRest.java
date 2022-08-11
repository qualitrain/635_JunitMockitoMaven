package org.qtx.servicios;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.qtx.entidades.Armadora;
import org.qtx.entidades.ModeloAuto;
import org.qtx.servicios.hateoas.LinkHateoas;
import org.qtx.servicios.hateoas.ObjetoHateoas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Path("armadoras")
public class ArmadoraRest {
	@Autowired
	private IGestorDatos gestorDatos;
	
	private static Logger bitacora = LoggerFactory.getLogger(ArmadoraRest.class);

	public ArmadoraRest(IGestorDatos gestorDatos) {
		this.gestorDatos = gestorDatos;
	}

	public ArmadoraRest() {
		bitacora.info("ArmadoraRest()");
	}

	public IGestorDatos getGestorDatos() {
		return gestorDatos;
	}

	public void setGestorDatos(IGestorDatos gestorDatos) {
		this.gestorDatos = gestorDatos;
	}
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getArmadoras(){
		List<Armadora> listaArmadoras = 
				new ArrayList<Armadora>(this.gestorDatos.getArmadorasTodas());
		return listaArmadoras.toString();
	}
	@Path("id")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getArmadoraXID_texto(
			@QueryParam("cve")
			@DefaultValue("000000")
			String cveArmadora) {
		Armadora armadora = this.gestorDatos.getArmadoraXID(cveArmadora);
		if(armadora == null) {
			return "Armadora con cve " + cveArmadora + " NO EXISTE";
		}
		return armadora.toString();
	}
	@Path("id")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getArmadoraXID_html(
			@QueryParam("cve")
			@DefaultValue("000000")
			String cveArmadora) {
		Armadora armadora = this.gestorDatos.getArmadoraXID(cveArmadora);
		if(armadora == null) {
			return "<span style='color:red;'>Armadora con cve " + cveArmadora + " NO EXISTE</span>";
		}
		return armadora.toHtml();
	}
	
	@Path("{cve}")
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Armadora getArmadoraXID_JSonXML(
			@PathParam("cve")
			@DefaultValue("000000")
			String cveArmadora) {
		Armadora armadora = this.gestorDatos.getArmadoraXID(cveArmadora);
		return armadora;
	}
	
	@GET
	@Path("{cve}/modelos")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<ModeloAuto> getModelosXarmadora(
			@PathParam("cve")
			String cveArmadora){
		Armadora armadora = this.gestorDatos.getArmadoraConModelosXID(cveArmadora);
		if(armadora == null)
			return null;
		Set<ModeloAuto> modelos = armadora.getModelos();
//		System.out.println(modelos);
		
		return new ArrayList<ModeloAuto>(modelos);
	}
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Armadora> getArmadorasXml(){
		List<Armadora> listaArmadoras = 
				new ArrayList<Armadora>(this.gestorDatos.getArmadorasTodas());
		
		return listaArmadoras;
	}
	@GET
	@Produces("application/hal+json")	
	public String getArmadorasHateoas() {
		ObjetoHateoas objHateos = new ObjetoHateoas();
		objHateos.agregarLink("self", new LinkHateoas("webapi/armadoras",false));
		objHateos.agregarLink("filtroXpais", new LinkHateoas("webapi/armadoras{?pais}",true));
		
		List<Armadora> armadoras = this.getArmadorasXml();
		for(Armadora armadoraI: armadoras) {
			Map<String,Object> mapArmadoraI = getMapaArmadora(armadoraI);
			objHateos.agregarDataItem(mapArmadoraI);
		}
		return objHateos.toJson().toString();
	}
	
	private Map<String, Object> getMapaArmadora(Armadora armadoraI) {
		Map<String,Object> mapaArmadora = new LinkedHashMap<>();
		Map<String,LinkHateoas> mapaLinks = new LinkedHashMap<>();

		mapaLinks.put("self", new LinkHateoas("webapi/armadoras/" + armadoraI.getClave(), false));
		mapaLinks.put("modelos", new LinkHateoas("webapi/armadoras/" + armadoraI.getClave() + "/modelos", false));
		mapaArmadora.put("_links",mapaLinks);
		
		mapaArmadora.put("clave", armadoraI.getClave());
		mapaArmadora.put("nombre", armadoraI.getNombre());
		mapaArmadora.put("paisOrigen", armadoraI.getPaisOrigen());
		mapaArmadora.put("nPlantas", armadoraI.getnPlantas());
		return mapaArmadora;
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN + "; charset=ISO-8859-1")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String insertarArmadora(
			@FormParam("clave")
			String clave, 
			@FormParam("nombre")
			String nombre,
			@FormParam("pais")
			String pais, 
			@FormParam("nplantas")
			@DefaultValue("0")
			int nPlantas) {
		String error = this.validarDatosArmadora(clave,nombre,pais,nPlantas);
		if(error != null)
			return error;
		Armadora nvaArmadora = new Armadora(clave,nombre,pais,nPlantas);
		nvaArmadora = this.gestorDatos.insertarArmadora(nvaArmadora);
		return nvaArmadora.toString();
	}
	@POST
	@Produces(MediaType.TEXT_PLAIN + "; charset=ISO-8859-1")
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertarArmadora_Json(Armadora nvaArmadora){
		String error = this.validarDatosArmadora(nvaArmadora);
		if(error != null)
			return error;
		nvaArmadora = this.gestorDatos.insertarArmadora(nvaArmadora);
		return nvaArmadora.toString();
	}
	@PUT
	@Produces(MediaType.TEXT_PLAIN + "; charset=ISO-8859-1")
	@Consumes(MediaType.APPLICATION_JSON)
	public String actualizarArmadora_Json(Armadora armadora){
		String error = this.validarDatosArmadoraUpdate(armadora);
		if(error != null)
			return error;
		armadora = this.gestorDatos.actualizarArmadora(armadora);
		return armadora.toString();
	}

	private String validarDatosArmadoraUpdate(Armadora armadora) {
		return this.validarDatosArmadoraUpdate(armadora.getClave(),armadora.getNombre(), 
                armadora.getPaisOrigen(), armadora.getnPlantas());
	}

	private String validarDatosArmadora(Armadora armadora) {
		return this.validarDatosArmadora(armadora.getClave(),armadora.getNombre(), 
				                         armadora.getPaisOrigen(), armadora.getnPlantas());
	}

	private String validarDatosArmadora(String clave, String nombre, String pais, int nPlantas) {
		String camposVacios = "";
		if (campoVacio(clave))
			camposVacios += "clave, ";
		if (campoVacio(nombre))
			camposVacios += "nombre, ";
		if (campoVacio(pais))
			camposVacios += "pais, ";
		if(camposVacios.isEmpty() == false)
			return "Error, faltan los campos: " + camposVacios;
		Armadora armadora = this.gestorDatos.getArmadoraXID(clave);
		if(armadora != null)
			return "Error, ya existe una armadora con esa clave:" + armadora;
		return null;
	}
	private String validarDatosArmadoraUpdate(String clave, String nombre, String pais, int nPlantas) {
		String camposVacios = "";
		if (campoVacio(clave))
			camposVacios += "clave, ";
		if (campoVacio(nombre))
			camposVacios += "nombre, ";
		if (campoVacio(pais))
			camposVacios += "pais, ";
		if(camposVacios.isEmpty() == false)
			return "Error, faltan los campos: " + camposVacios;
		Armadora armadora = this.gestorDatos.getArmadoraXID(clave);
		if(armadora == null)
			return "Error, No existe una armadora con esa clave:" + clave;
		return null;
	}

	private boolean campoVacio(String campo) {
		if(campo == null)
			return true;
		if(campo.trim().isEmpty())
			return true;
		return false;
	}
}

package org.qtx.servicios.hateoas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ObjetoHateoas {
	private Map<String,LinkHateoas>_links ;
	private List<Map<String,Object>> data;

	public ObjetoHateoas() {
		super();
		this._links = new LinkedHashMap<>();
		this.data = new ArrayList<>();
	}
	public void agregarLink(String relacion, LinkHateoas link) {
		this._links.put(relacion,link);
	}
	public void agregarDataItem(Map<String,Object> mapaObjeto) {
		this.data.add(mapaObjeto);
	}
	public JsonObject toJson() {
		JsonObjectBuilder constructorObj = Json.createObjectBuilder();
		
		JsonObjectBuilder constructorLinks = configurarLinksGlobales();
		constructorObj.add("_links", constructorLinks);
		
		JsonArrayBuilder constructorData = configurarData();
		constructorObj.add("data", constructorData);
		
		return constructorObj.build();
	}
	private JsonObjectBuilder configurarLinksGlobales() {
		JsonObjectBuilder constructorLinks = Json.createObjectBuilder();
		for(String campoI : this._links.keySet()) {
			constructorLinks.add(campoI, this._links.get(campoI).toJson());
		}
		return constructorLinks;
	}
	private JsonArrayBuilder configurarData() {
		JsonArrayBuilder constructorData = Json.createArrayBuilder();
		for(Map<String,Object> mapaObjeto : this.data) {
			JsonObjectBuilder constructorObjI = Json.createObjectBuilder();
			agregarCampoLinks(mapaObjeto, constructorObjI);
			agregarCamposObjetoBase(mapaObjeto, constructorObjI);
			constructorData.add(constructorObjI);
		}
		return constructorData;
	}
	private void agregarCampoLinks(Map<String, Object> mapaObjeto, JsonObjectBuilder constructorObjI) {
		JsonObjectBuilder constructorObjLinksI = Json.createObjectBuilder();
		@SuppressWarnings("unchecked")
		Map<String,LinkHateoas> mapaLinks = (Map<String, LinkHateoas>) mapaObjeto.get("_links");
		for(Entry<String, LinkHateoas> par : mapaLinks.entrySet()) {
			constructorObjLinksI.add(par.getKey(), par.getValue().toJson());
		}
		constructorObjI.add("_links", constructorObjLinksI);
	}
	private void agregarCamposObjetoBase(Map<String, Object> mapaObjeto, JsonObjectBuilder constructorObjI) {
		for(Entry<String, Object> par : mapaObjeto.entrySet()) {
			if(par.getKey().equalsIgnoreCase("_links"))
				continue;
			if(par.getValue() instanceof String)
				constructorObjI.add(par.getKey(),(String)par.getValue() );
			else
			if(par.getValue() instanceof Integer) {
				constructorObjI.add(par.getKey(),(Integer)par.getValue() );
			}
		}
	}
	
}

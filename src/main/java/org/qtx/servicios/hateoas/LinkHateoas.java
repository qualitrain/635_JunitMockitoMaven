package org.qtx.servicios.hateoas;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class LinkHateoas {
	private String href;
	private boolean templated;
	
	public LinkHateoas() {
		super();
	}
	public LinkHateoas(String href, boolean templated) {
		super();
		this.href = href;
		this.templated = templated;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public boolean isTemplated() {
		return templated;
	}
	public void setTemplated(boolean templated) {
		this.templated = templated;
	}
	public JsonObject toJson() {
		 JsonObjectBuilder constructor = Json.createObjectBuilder();
		 constructor.add("href", this.href)
		            .add("templated", this.templated);
		 return constructor.build();
	}

}

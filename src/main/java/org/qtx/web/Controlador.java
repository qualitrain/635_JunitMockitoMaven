package org.qtx.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controlador {

	private static Logger bitacora = LoggerFactory.getLogger(Controlador.class);
	
	public Controlador() {
		bitacora.info("Controlador()");
	}
	@GetMapping("/")
	public String getWelcomeFile(Model modelo) {
		return "principal";
	}
}

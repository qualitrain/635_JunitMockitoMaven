package org.qtx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EjmApiRestApplication {

	private static Logger bitacora = LoggerFactory.getLogger(EjmApiRestApplication.class);
	
	public static void main(String[] args) {
		bitacora.info("Iniciando Aplicacion Version:" 
	                  + EjmApiRestApplication.class
	                                         .getPackage()
	                                         .getImplementationVersion() );
		// Al ejecutar mvn package se guardará en el jar la versión descrita en el pom.xml
		// Al versión será visible al ejecutar el uber jar generado por spring-boot-maven-plugin, con java -jar
		SpringApplication.run(EjmApiRestApplication.class, args);
	}

}

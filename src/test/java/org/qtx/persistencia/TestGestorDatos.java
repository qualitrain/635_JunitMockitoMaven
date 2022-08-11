package org.qtx.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.qtx.entidades.Armadora;
import org.qtx.entidades.ModeloAuto;
import org.qtx.servicios.IGestorDatos;
import org.qtx.servicios.PersistenciaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class TestGestorDatos {
	@Autowired
	private IGestorDatos gestorDatos;
	
	private static Logger bitacora = LoggerFactory.getLogger(TestGestorDatos.class);
		
	public TestGestorDatos() {
		bitacora.info("TestgestorDatos()");
	}
	

	@Test
	public void testGetArmadoraXID() {
		bitacora.debug("TestgestorDatos().testGetArmadoraXID()");

		// Preparacion
		gestorDatos.eliminarArmadora("TestArm");		
		
		// Dados
		Armadora armadora = new Armadora("TestArm","ArmTest AG","Alemania",2);
		gestorDatos.insertarArmadora(armadora);
		
		// Cuando
		Armadora armadoraLeida = gestorDatos.getArmadoraXID(armadora.getClave());
		
		// Entonces
		assertEquals( armadora.getClave(),      armadoraLeida.getClave() );
		assertEquals( armadora.getNombre(),     armadoraLeida.getNombre() );
		assertEquals( armadora.getnPlantas(),   armadoraLeida.getnPlantas() );
		assertEquals( armadora.getPaisOrigen(), armadoraLeida.getPaisOrigen() );		
	}
	
	@Test
	public void testGetArmadoraXID_Inexistente_returnNull() {
		bitacora.debug("TestgestorDatos().testGetArmadoraXID_Inexistente_returnNull()");
		
		// Preparacion
		gestorDatos.eliminarArmadora("XXXXX");
		
		// Dados
		String cveArmadoraInexistente = "XXXXX";
		
		// Cuando
		Armadora armadoraLeida = gestorDatos.getArmadoraXID(cveArmadoraInexistente);
		
		// Entonces
		assertEquals(armadoraLeida, null);		
	}
	
	@Test
	public void testInsertarArmadora_Plana() {
		bitacora.debug("TestgestorDatos().testInsertarArmadora_Plana()");
		
		// Preparacion
		gestorDatos.eliminarArmadora("NvaArma");
		
		// Dados
		Armadora armadoraNva = new Armadora("NvaArma","Armadora Mexicana SA de CV","Brasil",0);
		
		// Cuando
		Armadora armadoraInsertada = gestorDatos.insertarArmadora(armadoraNva);
		
		// Entonces	
		Armadora armadoraTest = gestorDatos.getArmadoraConModelosXID(armadoraNva.getClave());
		assertTrue(armadoraNva.equivaleA(armadoraTest));
		assertTrue(armadoraNva.equivaleA(armadoraInsertada));		
	}
	
	@Test
	public void testInsertarArmadora_ConModelos() {
		bitacora.debug("TestgestorDatos().testInsertarArmadora_ConModelos()");
		
		// Preparacion
		gestorDatos.eliminarModeloAuto("F-45");
		gestorDatos.eliminarModeloAuto("F-49");
		gestorDatos.eliminarModeloAuto("F-55");
		gestorDatos.eliminarArmadora("Armadora");
		
		// Dados
		Armadora armadoraNva = new Armadora("Armadora","Armadora Agrupada SA de CV","Brasil",0);
		Set<ModeloAuto> modelos = new HashSet<>();
		modelos.add(new ModeloAuto("F-45","FireRabbit",  armadoraNva, "Custom", true));
		modelos.add(new ModeloAuto("F-49","FireRaccoon", armadoraNva, "Austero",true));
		modelos.add(new ModeloAuto("F-55","FireRaccoon", armadoraNva, "Típico", true));		
		armadoraNva.setModelos(modelos);
		
		// Cuando
		Armadora armadoraInsertada = gestorDatos.insertarArmadora(armadoraNva);
		
		// Entonces	
		Armadora armadoraTest = gestorDatos.getArmadoraConModelosXID(armadoraNva.getClave());
		assertTrue(armadoraNva.equivaleA(armadoraTest));
		assertTrue(armadoraNva.equivaleA(armadoraInsertada));		
	}
	
	@Test
	public void testInsertarArmadora_Duplicada() {
		bitacora.debug("TestgestorDatos().testInsertarArmadora_Duplicada()");
		
		// Preparacion
		gestorDatos.eliminarArmadora("Armadora02");
		
		// Dados
		Armadora armadoraNva = new Armadora("Armadora02", "Armadora X SA de CV", "México", 1);
		gestorDatos.insertarArmadora(armadoraNva);
		
		// Cuando
		// Entonces	
		
		assertThrows( PersistenciaException.class, () -> gestorDatos.insertarArmadora(armadoraNva) );
	}	

}

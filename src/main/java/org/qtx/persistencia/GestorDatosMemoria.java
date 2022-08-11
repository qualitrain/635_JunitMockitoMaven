package org.qtx.persistencia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.qtx.entidades.Armadora;
import org.qtx.entidades.ModeloAuto;
import org.qtx.servicios.IGestorDatos;
import org.qtx.servicios.ManejadorErrPersistencia;
import org.qtx.servicios.PersistenciaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

//@Primary
@Repository
public class GestorDatosMemoria implements IGestorDatos{
	private Map<String,Armadora> armadoras;
	private Map<String,ModeloAuto> autos;

	private static Logger bitacora = LoggerFactory.getLogger(GestorDatosMemoria.class);

	
	public GestorDatosMemoria() {
		bitacora.info("GestorDatosMemoria()");
		this.armadoras = new HashMap<String,Armadora>();
		this.autos = new HashMap<String,ModeloAuto>();
		this.cargarArmadoras();
	}
	
	@Override
	public Armadora getArmadoraXID(String cveArmadora) {
		bitacora.debug("getArmadoraXID(" +  cveArmadora +")");
		
		return this.armadoras.get(cveArmadora);
	}
	
	@Override
	public ModeloAuto getModeloAutoXID(String cveModelo) {
		bitacora.debug("getModeloAutoXID(" +  cveModelo +")");
		
		return this.autos.get(cveModelo);
	}

	@Override
	public Set<Armadora> getArmadorasTodas() {
		return new HashSet<Armadora>(this.armadoras.values());
	}

	@Override
	public Map<String, Armadora> getMapaArmadoras() {
		return this.armadoras;
	}

	@Override
	public Armadora insertarArmadora(Armadora armadora) {
		bitacora.debug("insertarArmadora(" +  armadora +")");
		if(this.armadoras.containsKey(armadora.getClave()) ) {
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Llave duplicada [" + this.armadoras.get(armadora.getClave())
					+ "] al intentar insertar armadora ");
			detEx.put("tabla", "armadora en memoria");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".insertarArmadora("
					+ armadora
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, null);
			throw pex;			
		}
		this.armadoras.put(armadora.getClave(), armadora);
		armadora.setModelos(new HashSet<>());
		return armadora;
	}
	
	public void cargarArmadoras() {
		Armadora nvaArmadora = new Armadora("VW", " Volkswagen Nutzfahrzeuge", "Alemania", 1);
		this.insertarArmadora(nvaArmadora);
		ModeloAuto modelo = new ModeloAuto("Jetta", "Jetta A4 Trendline", nvaArmadora, "Automatic", false);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("GolfGTI", "Golf GTI DSG", nvaArmadora, "Turbo GTI Stronic", true);
		this.insertarModeloAuto(modelo);
		
		nvaArmadora = new Armadora("Nissan", "Nissan de México S.A. de C.V.", "Japón", 1);
		this.insertarArmadora(nvaArmadora);
		modelo = new ModeloAuto("X-trail", "Nissan X-TRAIL", nvaArmadora, "4WD", true);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("Altima", "Nissan Altima", nvaArmadora, "S3 2.5L", false);
		this.insertarModeloAuto(modelo);
		
		nvaArmadora = new Armadora("Mazda", "Mazda International", "Japón", 1);
		this.insertarArmadora(nvaArmadora);
		modelo = new ModeloAuto("MX-5", "MX-5 MIURA STD", nvaArmadora, "Cabrio", false);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("3", "Mazda-3", nvaArmadora, "Sedan", false);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("5", "Mazda-5", nvaArmadora, "Sedan", false);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("CX5", "Mazda-CX5", nvaArmadora, "Grand Touring 2WD", false);
		this.insertarModeloAuto(modelo);
		
		nvaArmadora = new Armadora("Ford", "Ford Motor Company", "EUA", 1);
		this.insertarArmadora(nvaArmadora);
		modelo = new ModeloAuto("Mustang", "Mustang GT 500", nvaArmadora, "500 Caballos", true);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("LoboHD", "Lobo Harley Davidson", nvaArmadora, "Harley Davidson", true);
		this.insertarModeloAuto(modelo);
		
		nvaArmadora = new Armadora("GM", "General Motors Company", "EUA", 2);
		this.insertarArmadora(nvaArmadora);
		modelo = new ModeloAuto("Spark", "Spark Std Aus", nvaArmadora, "Estándar austero", false);
		this.insertarModeloAuto(modelo);
		
		nvaArmadora = new Armadora("Fiat", "Fabbrica Italiana Automobili Torino", "Italia", 1);
		this.insertarArmadora(nvaArmadora);
		modelo = new ModeloAuto("500", "Fiat 500 Diabolo", nvaArmadora, "200 Caballos Turbo", true);
		this.insertarModeloAuto(modelo);
		modelo = new ModeloAuto("Panda", "Fiat Panda", nvaArmadora, "Estándar", false);
		this.insertarModeloAuto(modelo);

		nvaArmadora = new Armadora("BMW", "BMW European cars Corporation", "Alemania", 0);
		this.insertarArmadora(nvaArmadora);
		modelo = new ModeloAuto("X3", "Serie 3 SUV", nvaArmadora, "Luxe", true);
		this.insertarModeloAuto(modelo);
		
	}

	@Override
	public ModeloAuto insertarModeloAuto(ModeloAuto modelo) {
		if(this.autos.containsKey(modelo.getClaveModelo()))
			return null;
		modelo.getArmadora().getModelos().add(modelo);
		return this.autos.put(modelo.getClaveModelo(), modelo);
	}

	@Override
	public Armadora getArmadoraConModelosXID(String cveArmadora) {
		bitacora.debug("getArmadoraXID(" +  cveArmadora +")");
		return this.armadoras.get(cveArmadora);
	}

	@Override
	public Armadora actualizarArmadora(Armadora armadora) {
		if(this.armadoras.containsKey(armadora.getClave()) == false) 
			return null;
		return this.armadoras.put(armadora.getClave(), armadora);
	}

	@Override
	public Armadora eliminarArmadora(String cveArmadora) {
		if(this.armadoras.containsKey(cveArmadora) == false) 
			return null;
		return this.armadoras.remove(cveArmadora);
	}

	@Override
	public ModeloAuto eliminarModeloAuto(String cveModelo) {
		if(this.autos.containsKey(cveModelo) == false) 
			return null;
		return this.autos.remove(cveModelo);
	}

}

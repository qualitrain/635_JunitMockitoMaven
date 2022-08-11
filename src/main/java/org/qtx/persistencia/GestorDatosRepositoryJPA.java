package org.qtx.persistencia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.qtx.entidades.Armadora;
import org.qtx.entidades.ModeloAuto;
import org.qtx.servicios.IGestorDatos;
import org.qtx.servicios.ManejadorErrPersistencia;
import org.qtx.servicios.PersistenciaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class GestorDatosRepositoryJPA implements IGestorDatos {
	
	@Autowired
	private IArmadoraRepository repoArmadora;
	@Autowired
	private IModeloAutoRepository repoAuto;

	private static Logger bitacora = LoggerFactory.getLogger(GestorDatosRepositoryJPA.class);
	
	public GestorDatosRepositoryJPA() {
		super();
		bitacora.info("GestorDatosRepositoryJPA()");
	}

	@Override
	public Armadora getArmadoraXID(String cveArmadora) {
		bitacora.debug("getArmadoraXID(" + cveArmadora + ")");
		Optional<Armadora> armadora = repoArmadora.findById(cveArmadora);
		if(armadora.isPresent())
			return armadora.get();
		return null;
	}

	@Override
	public Set<Armadora> getArmadorasTodas() {
		bitacora.debug("getArmadorasTodas()");
		return new HashSet<>(repoArmadora.findAll());
	}

	@Override
	public Map<String, Armadora> getMapaArmadoras() {
		Map<String,Armadora> mapArmadoras = new HashMap<>();
		List<Armadora> lstArmadoras = this.repoArmadora.findAll();
		lstArmadoras.stream()
		            .forEach( armI -> mapArmadoras.put(armI.getClave(), armI) );
		return mapArmadoras;
	}

	@Override
	public Armadora insertarArmadora(Armadora armadora) {
		
		Armadora armadoraPreexistente = this.getArmadoraXID(armadora.getClave());
		if (armadoraPreexistente != null) {
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Llave duplicada [" + armadoraPreexistente
					+ "] al intentar insertar armadora ");
			detEx.put("tabla", "armadora en memoria");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".insertarArmadora("
					+ armadora
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, null);
			throw pex;					
		}
		Set<ModeloAuto> modelosAuto;
		if(armadora.tieneModelos()) {
			modelosAuto = armadora.getModelos();
			armadora.setModelos(null);
			this.repoArmadora.save(armadora);
			this.repoAuto.saveAll(modelosAuto);
			armadora.setModelos(modelosAuto); // Deja el objeto recibido como estaba originalmente
			return this.getArmadoraConModelosXID(armadora.getClave());
		}

		Armadora nvaArmadora = this.repoArmadora.save(armadora);
		return nvaArmadora;
	}

	@Override
	public ModeloAuto insertarModeloAuto(ModeloAuto modelo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Armadora getArmadoraConModelosXID(String cveArmadora) {
		Optional<Armadora> optArmadora = this.repoArmadora.findById(cveArmadora);
		if(optArmadora.isEmpty())
		   return null;
		Armadora armadora = optArmadora.get();
		armadora.setModelos( repoAuto.findByArmadoraClave(armadora.getClave()) );
		return armadora;
	}

	@Override
	public Armadora actualizarArmadora(Armadora armadora) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Armadora eliminarArmadora(String cveArmadora) {
		Optional<Armadora> armadora = repoArmadora.findById(cveArmadora);
		if(armadora.isEmpty())
			return null;
		repoArmadora.deleteById(cveArmadora);
		return armadora.get();
	}

	@Override
	public ModeloAuto eliminarModeloAuto(String cveModelo) {
		Optional<ModeloAuto> auto = repoAuto.findById(cveModelo);
		if(auto.isEmpty())
			return null;
		repoAuto.deleteById(cveModelo);
		return auto.get();
	}

	@Override
	public ModeloAuto getModeloAutoXID(String cveModelo) {
		bitacora.debug("getModeloAutoXID(" + cveModelo + ")");
		Optional<ModeloAuto> auto = repoAuto.findById(cveModelo);
		if(auto.isPresent())
			return auto.get();
		return null;
	}

}

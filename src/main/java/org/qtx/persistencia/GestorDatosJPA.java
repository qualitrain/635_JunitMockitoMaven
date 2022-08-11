package org.qtx.persistencia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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

//@Primary
@Repository
public class GestorDatosJPA implements IGestorDatos {
	@Autowired
	private EntityManagerFactory fabricaEntityManager;
	
	private static Logger bitacora = LoggerFactory.getLogger(GestorDatosJPA.class);

	public GestorDatosJPA() {
		bitacora.info("GestorDatosJPA()");
	}

	@Override
	public Armadora getArmadoraXID(String cveArmadora) {
		bitacora.debug("getArmadoraXID(" +  cveArmadora +")");
		
		EntityManager em = fabricaEntityManager.createEntityManager();
	    Armadora unaArmadora = em.find(Armadora.class, cveArmadora);
	    em.close();
		return unaArmadora;
	}
	@Override
	public ModeloAuto getModeloAutoXID(String cveModelo) {
		bitacora.debug("getModeloAutoXID(" +  cveModelo +")");
		
		EntityManager em = fabricaEntityManager.createEntityManager();
	    ModeloAuto unaAuto = em.find(ModeloAuto.class, cveModelo);
	    em.close();
		return unaAuto;
	}
	@Override
	public Armadora getArmadoraConModelosXID(String cveArmadora) {
		EntityManager em = fabricaEntityManager.createEntityManager();
	    Armadora unaArmadora = em.find(Armadora.class, cveArmadora);
	    if(unaArmadora == null)
	    	return null;
	    unaArmadora.getModelos().size();
	    em.close();
		return unaArmadora;
	}

	@Override
	public Set<Armadora> getArmadorasTodas() {
		String consulta = "SELECT a FROM Armadora a";
		EntityManager em = fabricaEntityManager.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Armadora> listArmadoras = (List<Armadora>) em.createQuery(consulta)
													.getResultList();
	    em.close();
	    return new HashSet<Armadora>(listArmadoras);		
	}

	@Override
	public Map<String, Armadora> getMapaArmadoras() {
		Map<String,Armadora> mapaArmadoras = new HashMap<>();
		Set<Armadora> setArmadoras = this.getArmadorasTodas();
		for(Armadora armadoraI : setArmadoras) {
			mapaArmadoras.put(armadoraI.getClave(), armadoraI);
		}
		return mapaArmadoras;
	}

	@Override
	public Armadora insertarArmadora(Armadora armadora) {
		bitacora.debug("insertarArmadora(" +  armadora +")");
		if(armadora.tieneModelos()) {
			bitacora.debug("insertarArmadora(..): Tiene modelos anidados (" + armadora.getModelos());			
		}
		
		EntityManager em = null;
		try {
			em = this.fabricaEntityManager.createEntityManager();
			EntityTransaction transaccion = em.getTransaction();
			transaccion.begin();
			em.persist(armadora);
			transaccion.commit();
			return armadora;
		}
		catch(Exception ex)
		{
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Falla al intentar insertar armadora");
			detEx.put("tabla", "armadora");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".insertarArmadora("
					+ armadora
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, ex);
			throw pex;
		}
		finally {
			if(em != null)
				em.close();
		}
	}
	@Override
	public Armadora actualizarArmadora(Armadora armadora) {
		EntityManager em = null;
		try {
			em = this.fabricaEntityManager.createEntityManager();
			EntityTransaction transaccion = em.getTransaction();
			transaccion.begin();
			Armadora armadoraAct = em.find(Armadora.class, armadora.getClave());
			if(armadoraAct == null)
				throw new Exception("La armadora no existe. Clave:" + armadora.getClave());
			em.merge(armadora);
			transaccion.commit();
			return armadora;
		}
		catch(Exception ex)
		{
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Falla al intentar actualizar armadora");
			detEx.put("tabla", "armadora");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".actualizarArmadora("
					+ armadora
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, ex);
			throw pex;
		}
		finally {
			if(em != null)
				em.close();
		}
	}
	public ModeloAuto insertarModeloAuto(ModeloAuto modelo) {
		EntityManager em = null;
		try {
			em = this.fabricaEntityManager.createEntityManager();
			EntityTransaction transaccion = em.getTransaction();
			transaccion.begin();
			em.persist(modelo);
			transaccion.commit();
			return modelo;
		}
		catch(Exception ex)
		{
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Falla al intentar insertar ModeloAuto");
			detEx.put("tabla", "ModeloAuto");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".insertarModeloAuto("
					+ modelo
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, ex);
			throw pex;
		}
		finally {
			if(em != null)
				em.close();
		}
	}

	@Override
	public Armadora eliminarArmadora(String cveArmadora) {
		EntityManager em = null;
		try {
			em = this.fabricaEntityManager.createEntityManager();
			EntityTransaction transaccion = em.getTransaction();
			transaccion.begin();
			Armadora armadoraAct = em.find(Armadora.class, cveArmadora);
			if(armadoraAct == null)
				return null;
			em.remove(armadoraAct);
			transaccion.commit();
			return armadoraAct;
		}
		catch(Exception ex)
		{
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Falla al intentar eliminar armadora");
			detEx.put("tabla", "armadora");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".eliminarArmadora("
					+ cveArmadora
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, ex);
			throw pex;
		}
		finally {
			if(em != null)
				em.close();
		}
	}

	@Override
	public ModeloAuto eliminarModeloAuto(String cveModelo) {
		EntityManager em = null;
		try {
			em = this.fabricaEntityManager.createEntityManager();
			EntityTransaction transaccion = em.getTransaction();
			transaccion.begin();
			ModeloAuto autoAct = em.find(ModeloAuto.class, cveModelo);
			if(autoAct == null)
				return null;
			em.remove(autoAct);
			transaccion.commit();
			return autoAct;
		}
		catch(Exception ex)
		{
			Map<String,String> detEx = new HashMap<String, String>();
			detEx.put("msg", "Falla al intentar eliminar ModeloAuto");
			detEx.put("tabla", "ModeloAuto");
			detEx.put("ubicacion", this.getClass().getSimpleName() 
					+ ".eliminarModeloAuto("
					+ cveModelo
					+ ")");
			PersistenciaException pex = ManejadorErrPersistencia.crearEx(detEx, ex);
			throw pex;
		}
		finally {
			if(em != null)
				em.close();
		}
	}
	
	@PreDestroy
	private void cerrarEntityManagerFactory() {
		if (this.fabricaEntityManager != null) {
			this.fabricaEntityManager.close();
		}
	}

}

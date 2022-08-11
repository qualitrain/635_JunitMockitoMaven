package org.qtx.servicios;

import java.util.Map;
import java.util.Set;

import org.qtx.entidades.Armadora;
import org.qtx.entidades.ModeloAuto;

public interface IGestorDatos {
	Armadora getArmadoraXID(String cveArmadora);
	Set<Armadora> getArmadorasTodas();
	Map<String,Armadora> getMapaArmadoras();
	Armadora insertarArmadora(Armadora armadora);
	ModeloAuto insertarModeloAuto(ModeloAuto modelo);
	Armadora getArmadoraConModelosXID(String cveArmadora);
	Armadora actualizarArmadora(Armadora armadora);
	Armadora eliminarArmadora(String cveArmadora);
	ModeloAuto eliminarModeloAuto(String cveModelo);
	ModeloAuto getModeloAutoXID(String cveModelo);
}

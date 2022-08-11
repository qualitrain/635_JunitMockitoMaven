package org.qtx.persistencia;

import java.util.Set;

import org.qtx.entidades.ModeloAuto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IModeloAutoRepository extends JpaRepository<ModeloAuto, String> {
	Set<ModeloAuto> findByArmadoraClave(String cveArmadora);

}

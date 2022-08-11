package org.qtx.persistencia;

import java.util.List;

import org.qtx.entidades.Armadora;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IArmadoraRepository extends JpaRepository<Armadora, String>{
	List<Armadora> findByPaisOrigen(String paisOrigen);
}

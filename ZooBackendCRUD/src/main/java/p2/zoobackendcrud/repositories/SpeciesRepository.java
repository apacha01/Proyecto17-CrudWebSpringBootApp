/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package p2.zoobackendcrud.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import p2.zoobackendcrud.entities.Species;

/**
 *
 * @author Agustín Pacheco
 */
@Repository
public interface SpeciesRepository extends JpaRepository<Species, Integer>{
    
    @Query("SELECT s FROM Species s LEFT JOIN FETCH s.habitats WHERE s.name = :name")
    public List<Species> findByNameContaining(@Param("name") String name);
    
    @Override
    @Query("SELECT DISTINCT s FROM Species s LEFT JOIN FETCH s.habitats")
    public List<Species> findAll();
    
    @Override
    @Query("SELECT s FROM Species s LEFT JOIN FETCH s.habitats WHERE s.id = :id")
    public Optional<Species> findById(@Param("id") Integer id);
    
    @Query("SELECT s FROM Species s WHERE s.zone.id = :zoneId")
    public List<Species> findByZoneId(@Param("zoneId") Integer id);
    
    @Modifying
    @Transactional
    @Query("UPDATE Species s SET s.zone = :newZoneId WHERE s.zone.id = :zoneId")
    public void updateSpeciesZoneByZoneId(@Param("zoneId") Integer zoneId, @Param("newZoneId") Integer newZoneId);
}

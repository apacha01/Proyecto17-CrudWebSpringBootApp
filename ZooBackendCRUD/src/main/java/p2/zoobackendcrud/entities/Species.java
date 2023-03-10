/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoobackendcrud.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Agustín Pacheco
 */
@Entity
@Data
@Table(name = "species")
public class Species implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_species",nullable = false)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    
    @Column(name = "scientific_name", nullable = false, length = 60)
    private String scientificName;
    
    @Column(name = "description", nullable = false, length = 100)
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_zone", nullable = true)
    private Zone zone;
    
    @OneToMany(mappedBy = "species", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    Set<SpeciesKeeper> speciesKeepers;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "species_habitats",
        joinColumns = @JoinColumn(name = "id_species"),
        inverseJoinColumns = @JoinColumn(name = "id_habitat"))
    Set<Habitat> habitats;

    public Species(){
        speciesKeepers = new HashSet<>();
        habitats = new HashSet<>();
    }
    
    public Species(String name, String scientificName, String description, Zone zone) {
        this.name = name;
        this.scientificName = scientificName;
        this.description = description;
        this.zone = zone;
        speciesKeepers = new HashSet<>();
        habitats = new HashSet<>();
    }
    
    public void setZone(Zone z){
        if (zone != null && zone.getSpecies().contains(this))
            zone.removeSpecies(this);
        zone = z;
        if (z != null)
            z.addSpecies(this);
    }
    
    public void addHabitat(Habitat h){
        if(h == null) return;
        habitats.add(h);
        if(!h.getSpecies().contains(this)) h.addSpecies(this);
    }
    
    public void removeHabitat(Habitat h){
        habitats.remove(h);
        if(h.getSpecies().contains(this)) h.removeSpecies(this);
    }
    
    public void removeAllHabitats(){
        habitats.clear();
    }
    
    @Override
    public String toString(){
        String s = "Id: " + id + ". Name: " + name + ". Scientifi Name: " + scientificName +
        ". Zone: " + zone.getName() + "(" + zone.getId() + ")";
        return s;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(this.id, this.scientificName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        final Species other = (Species) obj;
        if (!Objects.equals(this.name, other.name) || !Objects.equals(this.scientificName, other.scientificName)
                || !Objects.equals(this.description, other.description) || !Objects.equals(this.id, other.id)
                || !Objects.equals(this.zone, other.zone)) {
            return false;
        }
        
        return true;
    }
}

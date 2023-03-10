/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoofrontendcrud.entities;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Agustín Pacheco
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Species {
    private Integer id;
    private String name;
    private String scientific_name;
    private String description;
    private Zone zone;
    Set<SpeciesKeeper> species_keepers;
    Set<Habitat> habitats;

    public Species(String name, String scientific_name, String description, Zone zone) {
        this.name = name;
        this.scientific_name = scientific_name;
        this.description = description;
        this.zone = zone;
    }
}

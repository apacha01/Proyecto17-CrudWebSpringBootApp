/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoobackendcrud.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Agustín Pacheco
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "zones")
public class Zone implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_zone", nullable = false)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 20)
    private String name;
    
    @Column(name = "extension", nullable = false)
    private double extension;
    
    //All but id (autogenerated by db)
    public Zone(String name, Double extension) {
        this.name = name;
        this.extension = extension;
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoobackendcrud.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import p2.zoobackendcrud.auxiliar.TYPE_ENUM;

/**
 *
 * @author Agustín Pacheco
 */
@Entity
@Data
@Table(name = "employees")
public class Employee implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_employee", nullable = false)
    private Integer id;
    
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    @Enumerated(EnumType.STRING)
    private TYPE_ENUM type;
    
    @Column(name = "user_name", nullable = false, length = 16, unique = true)
    private String userName;
    
    @Column(name = "password", nullable = false, length = 16)
    private String password;
    
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    
    @Column(name = "address", nullable = false, length = 30)
    private String address;
    
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "first_day", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate firstDay;
    
    @OneToMany(mappedBy = "guide", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    Set<GuideItinerary> guidesItineraries;
    
    @OneToMany(mappedBy = "keeper", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    Set<SpeciesKeeper> speciesKeepers;

    //null safe constructor
    public Employee(){
        guidesItineraries = new HashSet<>();
        speciesKeepers = new HashSet<>();
    }
    
    //All atributes but id (autogenerated)
    public Employee(TYPE_ENUM type, String userName, String password, String name, String address, String phone, LocalDate firstDay) {
        this.type = type;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.firstDay = firstDay;
        guidesItineraries = new HashSet<>();
        speciesKeepers = new HashSet<>();
    }
    
    public String formatedFirstDayAsString(){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(this.firstDay);
    }
    
    public boolean isGuide(){
        return type == TYPE_ENUM.GUIDE;
    }
    
    public boolean isKeeper(){
        return type == TYPE_ENUM.KEEPER;
    }
    
    public boolean isAdmin(){
        return type == TYPE_ENUM.ADMIN;
    }
    
    public boolean hasSpecies(Species s){
        for (SpeciesKeeper speciesKeeper : speciesKeepers) {
            if (Objects.equals(speciesKeeper.getSpecies().getId(), s.getId()))
                return true;
        }
        return false;
    }
    
}
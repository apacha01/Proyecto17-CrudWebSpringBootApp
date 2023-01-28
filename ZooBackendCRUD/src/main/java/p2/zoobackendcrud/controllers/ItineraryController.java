/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoobackendcrud.controllers;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import p2.zoobackendcrud.entities.Employee;
import p2.zoobackendcrud.repositories.ItineraryRepository;
import p2.zoobackendcrud.entities.Itinerary;

/**
 *
 * @author Agustín Pacheco
 */
@RestController
@RequestMapping("itinerario")
public class ItineraryController {
    
    @Autowired
    private ItineraryRepository itRepo;
    
    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Itinerary create(@RequestBody Itinerary i) {
        if (i == null)
            return null;
        return itRepo.save(i);
    }
    
    @GetMapping("/obtenertodos")
    public List<Itinerary> getAllItineraries() {
        return itRepo.findAll();
    }
    
    @GetMapping("/obtenerporid/{id}")
    public ResponseEntity<Itinerary> getItineraryById(@PathVariable("id") Integer itineraryId){
        return itRepo.findById(itineraryId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/obtenerporcodigo/{codigo}")
    public List<Itinerary> getItineraryByCode(@PathVariable("codigo") String code){
        try{
            return itRepo.findByCode(URLDecoder.decode(code, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return new ArrayList<>();
        }
    }
}

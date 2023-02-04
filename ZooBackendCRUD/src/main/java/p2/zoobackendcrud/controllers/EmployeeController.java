/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoobackendcrud.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import p2.zoobackendcrud.auxiliar.TYPE_ENUM;
import p2.zoobackendcrud.repositories.EmployeeRepository;
import p2.zoobackendcrud.entities.Employee;
import p2.zoobackendcrud.entities.GuideItinerary;
import p2.zoobackendcrud.entities.Itinerary;
import p2.zoobackendcrud.repositories.GuideItineraryRepository;
import p2.zoobackendcrud.repositories.ItineraryRepository;

/**
 *
 * @author Agustín Pacheco
 */
@RestController
@RequestMapping("/empleado")
public class EmployeeController {
    
    @Autowired
    private EmployeeRepository empRepo;
    
    @Autowired
    private ItineraryRepository itiRepo;
    
    @Autowired
    private GuideItineraryRepository giRepo;
    
    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee e) {
        if (e == null)
            return null;
        return empRepo.save(e);
    }
    
    @GetMapping("/obtenertodos")
    public List<Employee> getAllEmployees() {
        return empRepo.findAll();
    }
    
    @GetMapping("/obtenerporid/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Integer employeeId){
        return empRepo.findById(employeeId)
                .map(ResponseEntity::ok)                                //Found -> status ok
                .orElseGet(() -> ResponseEntity.notFound().build());    //Not found
    }
    
    @GetMapping("/obtenerpornombre/{nombre}")
    public List<Employee> getEmployeeByName(@PathVariable("nombre") String employeeName){
        try{
            return empRepo.findByNameContaining(URLDecoder.decode(employeeName, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return new ArrayList<>();
        }
    }
    
    @PutMapping("/modificarporid/{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable("id") Integer employeeId, @RequestBody Employee e){
        return empRepo.findById(employeeId)
                .map(savedEmployee -> {
                    if (e.getType() != null) savedEmployee.setType(e.getType());
                    if (e.getUserName() != null) savedEmployee.setUserName(e.getUserName());
                    if (e.getPassword() != null) savedEmployee.setPassword(e.getPassword());
                    if (e.getName() != null) savedEmployee.setName(e.getName());
                    if (e.getAddress() != null) savedEmployee.setAddress(e.getAddress());
                    if (e.getPhone() != null) savedEmployee.setPhone(e.getPhone());
                    if (e.getFirstDay() != null) savedEmployee.setFirstDay(e.getFirstDay());
                    Employee updatedEmployee = empRepo.save(savedEmployee);
                    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{empId}/asignaritinerario/{itinId}")
    public ResponseEntity<GuideItinerary> assignItineraryToGuide(@PathVariable("empId") Integer empId, 
            @PathVariable("itinId") Integer itinId){
        Employee e = empRepo.findById(empId).orElse(null);
        Itinerary i = itiRepo.findById(itinId).orElse(null);
        
        if (e == null || i == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        if(e.getType() != TYPE_ENUM.GUIDE)
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        
        if (i.getAssigned())
            return new ResponseEntity<>(null, HttpStatus.IM_USED);
        
        i.setAssigned(true);
        GuideItinerary gi = giRepo.save(new GuideItinerary(e,i,new Date()));
        
        return new ResponseEntity(gi, HttpStatus.OK);
    }
    
    @PutMapping("/{empId}/quitaritinerario/{itinId}")
    public ResponseEntity<GuideItinerary> removeItineraryFromGuide(@PathVariable("empId") Integer empId, 
            @PathVariable("itinId") Integer itinId){
        Employee e = empRepo.findById(empId).orElse(null);
        Itinerary i = itiRepo.findById(itinId).orElse(null);
        GuideItinerary gi = null;
        
        if (e == null || i == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        if(e.getType() != TYPE_ENUM.GUIDE)
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        
        gi = giRepo.findByIds(itinId, empId);
        
        if(gi != null){
            i.setAssigned(false);
            giRepo.delete(gi);
        }
        
        return new ResponseEntity(gi, HttpStatus.OK);
    }
    
    @PutMapping("/{empId}/asignaritinerarios")
    public ResponseEntity<Employee> assignItinerariesToGuide(@PathVariable("empId") Integer empId, 
           @RequestBody List<Integer> itinsId){
        Employee e = empRepo.findById(empId).orElse(null);
        Boolean assigned = false;
        
        if (e == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        if(e.getType() != TYPE_ENUM.GUIDE)
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        
        for (Integer itinId : itinsId) {
            Itinerary i = itiRepo.findById(itinId).orElse(null);
            if (i != null) {
                if (i.getAssigned())
                    assigned = true;
                
                if(!i.getAssigned()){
                    i.setAssigned(true);
                    giRepo.save(new GuideItinerary(e,i,new Date()));
                }
            }
        }
        
        //If one was assigned give warning so user knows not all itineraries were assigned
        return (assigned ? new ResponseEntity(e, HttpStatus.PARTIAL_CONTENT) : new ResponseEntity(e, HttpStatus.OK));
    }
    
    @PutMapping("/{empId}/removeritinerarios")
    public ResponseEntity<Employee> removeItinerariesFromGuide(@PathVariable("empId") Integer empId, 
           @RequestBody List<Integer> itinsId){
        Employee e = empRepo.findById(empId).orElse(null);
        
        if (e == null)
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        
        if(e.getType() != TYPE_ENUM.GUIDE)
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        
        for (Integer itinId : itinsId) {
            Itinerary i = itiRepo.findById(itinId).orElse(null);
            if (i != null) {
                i.setAssigned(false);
                GuideItinerary gi = giRepo.findByIds(itinId, empId);
                giRepo.delete(gi);
            }
        }
        
        return new ResponseEntity(e, HttpStatus.OK);
    }
    
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Employee> deleteEmployeeById(@PathVariable("id") Integer employeeId){
        Optional<Employee> optEmp = empRepo.findById(employeeId);
        if (optEmp.isEmpty())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        else {
            empRepo.deleteById(employeeId);
            return new ResponseEntity<>(optEmp.get(), HttpStatus.OK);
        }
    }
}

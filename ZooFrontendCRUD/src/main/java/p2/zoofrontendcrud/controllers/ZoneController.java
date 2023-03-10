/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2.zoofrontendcrud.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import p2.zoofrontendcrud.auxiliar.Constants;
import p2.zoofrontendcrud.entities.Employee;
import p2.zoofrontendcrud.entities.Species;
import p2.zoofrontendcrud.entities.Zone;

/**
 *
 * @author Agustín Pacheco
 */
@Controller
@RequestMapping("/zona")
public class ZoneController {

    @GetMapping("/zonas")
    public String zonePage(Model m) {

        RestTemplate rt = new RestTemplate();
        List<Zone> zns = null;

        try {
            zns = rt.exchange(Constants.PREFIX_REQUEST_URL
                    + Constants.ZONE_REQUEST_URL
                    + Constants.GET_ALL_REQUEST_URL,
                    HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<Zone>>() {
            }).getBody();

            for (Zone z : zns) {
                List<Species> sps;
                sps = rt.getForObject(Constants.PREFIX_REQUEST_URL
                        + Constants.ZONE_REQUEST_URL
                        + z.getId() + "/"
                        + Constants.GET_SPECIES_REQUEST_URL,
                        List.class);

                z.setSpecies(new HashSet(sps));
            }
        } catch (RestClientException ex) {
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        
        m.addAttribute("zones", zns);

        return Constants.ZONE_VIEWS + "zones";
    }
    
    @GetMapping("/crear_zona")
    public String createZonePage(){
        return Constants.ZONE_VIEWS + "create_zone";
    }
    
    @PostMapping("/crear_zona")
    public String createZone(Model m, @RequestParam String name, @RequestParam String extension){
        Double newExtension;
        
        try{
            newExtension = Double.valueOf(extension.replaceAll("\\s", ""));
        }catch(NumberFormatException ex){
            m.addAttribute("errorMsg", "ERROR: El numero ingresado no es valido. Recuerde usar el '.' para numeros con coma y no ingrese signos de puntuacion para separar decenas, centenas etc...");
            return Constants.ZONE_VIEWS + "create_zone";
        }catch(NullPointerException ex){
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        
        if (newExtension <= 0) {
            m.addAttribute("errorMsg", "ERROR: El numero ingresado no es valido. Debe ser mayor a 0.");
            return Constants.ZONE_VIEWS + "create_zone";
        }
        
        HttpEntity<Zone> request = new HttpEntity<>(new Zone(name, newExtension));
        RestTemplate rt = new RestTemplate();
        ResponseEntity<Zone> z = null;
        
        try {
            z = rt.postForEntity(Constants.PREFIX_REQUEST_URL
                    + Constants.ZONE_REQUEST_URL
                    + Constants.CREATE_REQUEST_URL,
                    request,
                    Zone.class);
        } catch (RestClientException ex) {
            m.addAttribute("request", request);
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        
        if (z == null) {
            return "error";
        }
        if (z.getStatusCode() == HttpStatus.CREATED) {
            m.addAttribute("msgs", List.of(z.getBody().toString()));
            return "operation_done";
        }
        
        return "error";
    }
    
    @GetMapping("/editar_zona/{id}")
    public String updateZonePage(Model m, @PathVariable Integer id){
        RestTemplate rt = new RestTemplate();
        ResponseEntity<Zone> z = null;

        try {
            z = rt.getForEntity(Constants.PREFIX_REQUEST_URL
                    + Constants.ZONE_REQUEST_URL
                    + Constants.GET_BY_ID_REQUEST_URL
                    + id,
                    Zone.class);
        } catch (RestClientException ex) {
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        
        m.addAttribute("z", z.getBody());
        
        return Constants.ZONE_VIEWS + "update_zone";
    }
    
    @PostMapping("/editar_zona/{id}")
    public String updateZone(Model m, 
            @PathVariable Integer id, 
            @RequestParam String name, 
            @RequestParam String extension){
        
        RestTemplate rt = new RestTemplate();
        ResponseEntity<Zone> z = null;
        Double newExtension;
        
        try{
            newExtension = Double.parseDouble(extension.replaceAll("\\s", ""));
        }catch(NumberFormatException ex){
            m.addAttribute("errorMsg", "ERROR: El numero ingresado no es valido. Recuerde usar el '.' para numeros con coma y no ingrese signos de puntuacion para separar decenas, centenas etc...");
            return Constants.ZONE_VIEWS + "update_zone";
        }catch(NullPointerException ex){
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        
        if (newExtension <= 0) {
            m.addAttribute("errorMsg", "ERROR: El numero ingresado no es valido. Debe ser mayor a 0.");
            return Constants.ZONE_VIEWS + "update_zone";
        }
        
        HttpEntity<Zone> request = new HttpEntity<>(new Zone(name, newExtension));
        
        try {
            z = rt.exchange(Constants.PREFIX_REQUEST_URL
                    + Constants.ZONE_REQUEST_URL
                    + Constants.UPDATE_BY_ID_REQUEST_URL
                    + id,
                    HttpMethod.PUT,
                    request,
                    Zone.class);
        } catch (RestClientException ex) {
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        
        if (z == null) {
            return "error";
        }
        if (z.getStatusCode() == HttpStatus.NOT_FOUND) {
            m.addAttribute("errorMsg", "Zona con el id '" + id + "' no existe.");
            return "error";
        }
        
        m.addAttribute("msgs", List.of(z.getBody().toString()));
        
        return "operation_done";
    }
    
    @GetMapping("//{id}/asignarespecies")
    public String assignSpeciesPage(Model m, @PathVariable Integer id){
        List<Species> species = null;
        List<Species> s = null;
        ResponseEntity<Zone> z = null;

        RestTemplate rt = new RestTemplate();
        try {
            z = rt.getForEntity(Constants.PREFIX_REQUEST_URL
                        + Constants.ZONE_REQUEST_URL
                        + Constants.GET_BY_ID_REQUEST_URL
                        + id,
                    Zone.class);
            species = rt.getForObject(Constants.PREFIX_REQUEST_URL
                    + Constants.SPECIES_REQUEST_URL
                    + Constants.GET_ALL_REQUEST_URL,
                    List.class);
            s = rt.getForObject(Constants.PREFIX_REQUEST_URL
                    + Constants.ZONE_REQUEST_URL
                    + id + "/"
                    + Constants.GET_SPECIES_REQUEST_URL,
                    List.class);
        } catch (RestClientException ex) {
            m.addAttribute("exception", ex.toString());
            return "error";
        }

        if (z.getStatusCode() == HttpStatus.NOT_FOUND) {
            m.addAttribute("errorMsgs", List.of("No existe la zona con el id: " + id));
            return "error";
        }

        //remove duplicates
        species.removeAll(s);
        
        m.addAttribute("name", z.getBody().getName());
        m.addAttribute("assignedSpecies", s);
        m.addAttribute("species", species);
        return Constants.ASSIGN_VIEWS + "assignSpecies";
    }
    
    @PostMapping("/{id}/asignarespecies")
    public String assignSpecies(Model m,
            @PathVariable("id") Integer id,
            @RequestParam(name = "toBeRemoved", required = false) List<Integer> toBeRemovedIds,
            @RequestParam(name = "alreadyAssigned", required = false) List<Integer> alreadyAssignedIds,
            @RequestParam(name = "toBeAssigned", required = false) List<Integer> toBeAssignedIds) {

        List<String> msgs = new ArrayList<>();
        Boolean needRemove = toBeRemovedIds != null;
        Boolean needAssign = toBeAssignedIds != null;
        
        //Requests
        HttpEntity<List<Integer>> requestRemove = null;
        HttpEntity<List<Integer>> requestAssign = null;
        //Responses
        ResponseEntity<Employee> responseRemove = null;
        ResponseEntity<Employee> responseAssign = null;
        
        if(needRemove)
            requestRemove = new HttpEntity<>(toBeRemovedIds);
        if(needAssign)
            requestAssign = new HttpEntity<>(toBeAssignedIds);

        RestTemplate rt = new RestTemplate();
        try {
            
            if(requestAssign != null){
                responseAssign = rt.exchange(Constants.PREFIX_REQUEST_URL
                        + Constants.ZONE_REQUEST_URL
                        + id + "/"
                        + Constants.ADD_SPECIES_REQUEST_URL,
                        HttpMethod.PUT,
                        requestAssign,
                        Employee.class);
            }
            
            if(requestRemove != null){
                responseRemove = rt.exchange(Constants.PREFIX_REQUEST_URL
                        + Constants.ZONE_REQUEST_URL
                        + id + "/"
                        + Constants.REMOVE_SPECIES_REQUEST_URL,
                        HttpMethod.PUT,
                        requestRemove,
                        Employee.class);
            }
        }
        catch (RestClientException ex) {
            m.addAttribute("exception", ex.toString());
            return "error";
        }

        if (responseAssign != null) {
            if (responseAssign.getStatusCode() == HttpStatus.NOT_FOUND)
                msgs.add("La zona de id " + id + " no se encontro.");
        }
        else msgs.add("No se asigno ninguna especie.");
        
        if (responseRemove == null)
            msgs.add("No se removio ninguna especie.");
        
        m.addAttribute("msgs", msgs);
        return "operation_done";
    }
    
    @PostMapping("/eliminar_zona")
    public String deleteZone(Model m, @RequestParam Integer id){
        RestTemplate rt = new RestTemplate();
        try {
            rt.delete(Constants.PREFIX_REQUEST_URL
                    + Constants.ZONE_REQUEST_URL
                    + Constants.DELETE_BY_ID_REQUEST_URL
                    + id);
        } catch (RestClientException ex) {
            m.addAttribute("exception", ex.toString());
            return "error";
        }
        return "operation_done";
    }
}

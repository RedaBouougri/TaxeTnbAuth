package com.example.taxetnbauth.security.springjwt.controller;

import com.example.taxetnbauth.security.springjwt.models.Terain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/terain")
public class GeneralController {


    @Autowired
    RestTemplate restTemplate;

    String uri = "http://localhost:8050/api/terain";

    @GetMapping("/allterain")
    public Terain[] findAllTerain(){

        ResponseEntity<Terain[]> trainList = restTemplate.getForEntity(this.uri+"/all", Terain[].class);

        return  trainList.getBody();
    }

    @PostMapping("/addterain")
    public ResponseEntity<Terain> addTerain(@RequestBody Terain terain){

        ResponseEntity<Terain> ter = restTemplate.postForEntity(this.uri + "/save", terain, Terain.class);
        return ter;

    }

    @DeleteMapping("/deleteterain/{id}")
    public ResponseEntity<Void> deleteTerain(@PathVariable int id){

        restTemplate.delete(this.uri + "/delete/"+id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/updateterain/{id}")
    public ResponseEntity<Terain> updateTerain(@PathVariable int id, @RequestBody Terain updatedTerain) {
        String updateUri = this.uri + "/update/" + id;

        restTemplate.put(updateUri, updatedTerain);

        return ResponseEntity.ok(updatedTerain);
    }






}

package com.example.taxetnbauth.security.springjwt.controller;

import com.example.taxetnbauth.security.springjwt.models.Taxe;
import com.example.taxetnbauth.service.KafkaDemandeTaxe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/taxe")
public class KafkaDemandeController {

    @Autowired
    private KafkaDemandeTaxe kafkaDemandeTaxe;

    @PostMapping("/sendToKafka")
    public String sendTaxeToKafka(@RequestBody Taxe taxe) {
        try {
            kafkaDemandeTaxe.sendMessageToTopic(taxe);
            return "Taxe sent to Kafka successfully!";
        } catch (Exception e) {
            return "Failed to send Taxe to Kafka: " + e.getMessage();
 }
}
}
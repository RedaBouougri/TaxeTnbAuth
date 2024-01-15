package com.example.taxetnbauth.service;

import com.example.taxetnbauth.security.springjwt.models.Taxe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;


@Service
public class KafkaDemandeTaxe {

    @Autowired
    private KafkaTemplate<String, Taxe> template;

    public void sendMessageToTopic(Taxe taxe) {
        ListenableFuture<SendResult<String, Taxe>> future = template
                .send("topic2", String.valueOf(taxe.getId()), taxe);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Taxe>>() {
            @Override
            public void onSuccess(SendResult<String, Taxe> result) {
                System.out.println("Sent message=[" + taxe + "] with offset=[" +
                        result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + taxe + "] due to " + ex.getMessage());
            }
        });
    }

}

package com.api.parkingcontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController // Será um bean do tipo controller e irá gerar as dependências quando necessário; 
public class ParkingControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingControlApplication.class, args);
    }
    
    @GetMapping ("/")
    public String index() {
        return "Olá mundo";
    }

}

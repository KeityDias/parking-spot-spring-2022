/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import dtos.ParkingSpotDto;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author keity
 */
@RestController //
@CrossOrigin(origins = "*", maxAge = 3600 ) // Permite que seja acessado de qualquer fonte;
@RequestMapping("/parking-spot")// URL? - Feito a nível de classe, mas poderia ser por método;
public class ParkingSpotController {
    
    final ParkingSpotService parkingSpotService;  

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }
                            /*CRIAÇÃO DOS DADOS DE ENTRADA*/  
    /**
    *ResponseEntity: monta a resposta com o Status e seu corpo;
    *Object porque teremos vários tipos de retorno de acordo com as verificações iniciais;
    *Recebe a classe ParkingSpotDto, que possui todos os campos que o cliente vai enviar para que esta vaga de estacionamento seja salva;
    *Recebe como parâmetro do método o ParkingSpotDTo, que virá com o gson por meio do body;
    *@Valid é o que valida os dados de entrada e aciona esse método, fazendo as validações;
    * @param parkingSpotDto
    * @return 
    */
    @PostMapping //Envia para "/parking-spot", definido no método postHttp, ativando o Save para atender a solicitação;
    public ResponseEntity <Object> saveParkingSpot (@RequestBody @Valid ParkingSpotDto parkingSpotDto){
         if(parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
        }
        
        var parkingSpotModel = new ParkingSpotModel (); //Pode ser usado dentro de um escopo fechado e é o mesmo que: ParkingSpotModel parkingSpotModel = new ParkingSpotModel ()
        //*Se todos os dados estiverem corretos, eles são convertidos de Dto para Model antes de salvar no BD, senão retorna uma BadRequest;
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        //Recebe um parkingSpotModel, salva e depois retorna ele salvo com o Id
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
        
    }
}

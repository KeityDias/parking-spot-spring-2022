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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author keity
 */
@RestController //
@CrossOrigin(origins = "*", maxAge = 3600) // Permite que seja acessado de qualquer fonte;
@RequestMapping("/parking-spot")// URL? - Feito a nível de classe, mas poderia ser por método;
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    /*CRIAÇÃO DOS DADOS DE ENTRADA*/
    /**
     * ResponseEntity: monta a resposta com o Status e seu corpo; Object porque
     * teremos vários tipos de retorno de acordo com as verificações iniciais;
     * Recebe a classe ParkingSpotDto, que possui todos os campos que o cliente
     * vai enviar para que esta vaga de estacionamento seja salva; Recebe como
     * parâmetro do método o ParkingSpotDTo, que virá com o gson por meio do
     * body;
     *
     * @Valid é o que valida os dados de entrada e aciona esse método, fazendo
     * as validações;
     * @param parkingSpotDto
     * @return
     */
    @PostMapping //Envia para "/parking-spot", definido no método postHttp, ativando o Save para atender a solicitação;
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
        if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
        }

        var parkingSpotModel = new ParkingSpotModel(); //Pode ser usado dentro de um escopo fechado e é o mesmo que: ParkingSpotModel parkingSpotModel = new ParkingSpotModel ()
        //*Se todos os dados estiverem corretos, eles são convertidos de Dto para Model antes de salvar no BD, senão retorna uma BadRequest;
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        //Recebe um parkingSpotModel, salva e depois retorna ele salvo com o Id
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    /**
     * getAll- Solicita toda a listagem de vagas de estacionamento; GetMapping-
     * Envia o caminho "parking-spot" para obter essa listagem;
     * Sort é o critério de ordenação
     * @param pageable
     * @return 
     */
    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }
    /**
     * getOne - Busca a vaga pelo Id específico que for fornecido;
     * @param id está entre {} porque é um Path Variable, números aleatórios de acordo com os Id's que forem sendo gerados;
     * É um objeto, porque se não existir a vaga, precisa enviar uma resposta para o cliente;
     * O Optional nos ajuda a evitar os erros NullPointerException, tirar a necessidade da verificação (if x != null);
     * @return 
     */
    @GetMapping ("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
       //O ifPresent faz um teste para saber se uma situação está ou não presente.
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }
    /**
     * Deleta o valor referênciado da base de dados;
     * @param id
     * @return : Mensagem informando se o item foi deletado ou não encontrado na base de dados de acordo com o Id informado; 
     */
    @DeleteMapping ("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if (!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
        }
    /**
     * Atualiza dados existentes no Banco de dados;
     * 
     * @param id é o PathVariable
     * @param parkingSpotDto é ondem contém os campos pra serem atualizados;
     * @return 
     */
     @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if (!parkingSpotModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        /*Há duas formas de fazer essa solicitação e converte a DTO para Model:
        - Instancia-se a parkingSpotModel, mas não inicia ela, reaproveitando o Optional do código acima..
        - Depois é necessário setar todos os campos que podem ter sido atualizados, com exceção do ID e DatadeRegistro;
        
        PRIMEIRA FORMA:
        var parkingSpotModel = parkingSpotModelOptional.get(); 
        parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber);
        parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar);
        parkingSpotModel.setModelCar(parkingSpotDto.getModelCar);
        parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar);
        parkingSpotModel.setColorCar(parkingSpotDto.getColorCar);
        parkingSpotModel.setResponsibleName(parkingSpotDto.getResponsibleName);
        parkingSpotModel.setApartment(parkingSpotDto.getApartment);
        parkingSpotModel.setBlock(parkingSpotDto.getBlock);
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
        */
        var parkingSpotModel = new ParkingSpotModel(); //Cria uma nova instância do parkingModel;
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);// Faz a conver~sao de Dto para Model;
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId()); //Set o Id para que ele não seja alterado;
        parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }
}

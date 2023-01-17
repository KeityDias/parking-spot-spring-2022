/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.api.parkingcontrol.repository;

import com.api.parkingcontrol.models.ParkingSpotModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Passamos o model do repository e o identificador;
 * Importamos o JpaRepository porque ele já possui vários métodos prontos para transações com o BD (salvar, deletar, atualizar); 
 * Essa interface é um bean do Spring, mas quando usamos o extens JpaRepository, ela automaticamente já se torna uma @Repository, não precisando ter a anotação; 
 * @author keity
 */
@Repository
public interface ParkingSpotRepository extends JpaRepository <ParkingSpotModel, UUID>{

    public boolean existsByLicensePlateCar(String licensePlateCar);

    public boolean existsByParkingSpotNumber(String parkingSpotNumber);

    public boolean existsByApartmentAndBlock(String apartment, String block);
   
}

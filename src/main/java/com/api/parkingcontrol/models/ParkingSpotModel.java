/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.api.parkingcontrol.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @since First application realease
 * @author keity
 */
@Entity //Declara a classe como uma entidade;
@Table (name = "TB_PARKING_SPOT")// Gera uma tabela na base de dados chamada TB_PARKING_SPOT;
public class ParkingSpotModel implements Serializable { //converte elementos java em bites para serem armazenados no BD;
    private static final long serialVersionUID = 1L; // Controle das conversões pela JVM;
    
    //Atributos:
    @Id // Identificador id, do tipo UUID (são únicos, podem ser gerados em qualquer lugar, universais)
    @GeneratedValue(strategy = GenerationType.AUTO) //Auto: gera o ID automaticamente;
    private UUID id; 
    /*@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;*/
    @Column(nullable = false, unique = true, length = 10)
    private String parkingSpotNumber; //Número da vaga, não pode ser nulo e único, ou seja, não permite dois cadastros dentro da mesma vaga; 
    @Column(nullable = false, unique = true, length = 7)
    private String licensePlateCar; //Placa do carro, não pode ser nulo e único;
    @Column(nullable = false, unique = true, length = 70)
    private String brandCar; //Marca do carro, não pode ser nulo e único
    @Column(nullable = false, unique = true, length = 70)
    private String modelCar;//Modelo do carro, não pode ser nulo e único
    @Column(nullable = false, unique = true, length = 70)
    private String colorCar; //Cor do carro, não pode ser nulo e único
    @Column(nullable = false)
    private LocalDateTime registrationDate; //Data de registro, não pode ser nulo;
    @Column(nullable = false, length = 130) 
    private String responsibleName;//Nome do responsável, não pode ser nulo;
    @Column(nullable = false, length = 30)
    private String apartment; //Identificação do apartamento - Ex: Apto. 21, não pode ser nulo;
    @Column(nullable =false, length = 30)
    private String block;//Bloco do apartamento, não pode ser nulo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParkingSpotNumber() {
        return parkingSpotNumber;
    }

    public void setParkingSpotNumber(String parkingSpotNumber) {
        this.parkingSpotNumber = parkingSpotNumber;
    }

    public String getLicensePlateCar() {
        return licensePlateCar;
    }

    public void setLicensePlateCar(String licensePlateCar) {
        this.licensePlateCar = licensePlateCar;
    }

    public String getBrandCar() {
        return brandCar;
    }

    public void setBrandCar(String brandCar) {
        this.brandCar = brandCar;
    }

    public String getModelCar() {
        return modelCar;
    }

    public void setModelCar(String modelCar) {
        this.modelCar = modelCar;
    }

    public String getColorCar() {
        return colorCar;
    }

    public void setColorCar(String colorCar) {
        this.colorCar = colorCar;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
    
    /*O ideal seria criar uma nova classe carro e fazer um relacionamento um (1,1);
      Relacionando uma nova entidade com a vaga de estacionamento 
    
     ** Alt+Insert -> Atalho para gerar Getters and Setters;
    
    */

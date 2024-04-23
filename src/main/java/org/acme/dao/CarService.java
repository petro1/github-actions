package org.acme.dao;

import org.acme.dto.CarDTO;

import java.util.List;

public interface CarService {

    List<CarDTO> findAllCars();

    List<CarDTO> findByBrand(String brand);

    List<CarDTO> findByModel(String model);

    void updateCar(long id, String model, String brand);

    void createCar(CarDTO carDTO);

    void deleteCar(long id);
}

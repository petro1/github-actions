package org.acme.dao;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dao.entity.Car;
import org.acme.dto.CarDTO;
import org.acme.mapper.CarMapperImpl;

import java.util.List;

@ApplicationScoped
public class CarRepository implements PanacheRepository<Car>, CarService {

    @Inject
    CarMapperImpl carMapperImpl;

    @Override
    public List<CarDTO> findAllCars() {
        return findAll().stream().map(car -> carMapperImpl.toCarDTO(car)) .toList();
    }

    @Override
    public List<CarDTO> findByBrand(String brand) {
        return find("brand", brand).stream().map(car -> carMapperImpl.toCarDTO(car)).toList();
    }

    @Override
    public List<CarDTO> findByModel(String model) {
        return find("model", model).stream().map(car -> carMapperImpl.toCarDTO(car)).toList();
    }

    @Transactional
    @Override
    public void updateCar(long id, String model, String brand) {
        Car car = findById(id);
        car.setBrand(brand);
        car.setModel(model);
    }

    @Transactional
    @Override
    public void createCar(CarDTO carDTO) {
        Car car = carMapperImpl.toCar(carDTO);
        persist(car);
    }

    @Transactional
    @Override
    public void deleteCar(long id) {
        deleteById(id);
    }
}

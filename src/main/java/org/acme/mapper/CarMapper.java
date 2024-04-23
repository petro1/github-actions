package org.acme.mapper;

import org.acme.dao.entity.Car;
import org.acme.dto.CarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "jakarta")
public interface CarMapper {

    CarMapper MAPPER = Mappers.getMapper(CarMapper.class);

    CarDTO toCarDTO(Car car);

    Car toCar(CarDTO carDTO);
}

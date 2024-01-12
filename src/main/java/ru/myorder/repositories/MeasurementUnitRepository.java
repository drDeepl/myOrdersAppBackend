package ru.myorder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.myorder.models.MeasurementUnit;

@Repository
public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Long> {

    Boolean existsByName(String name);
}

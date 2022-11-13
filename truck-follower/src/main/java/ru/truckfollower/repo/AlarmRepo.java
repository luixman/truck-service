package ru.truckfollower.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.truckfollower.entity.Alarm;

import java.util.List;

public interface AlarmRepo extends JpaRepository<Alarm,Long> {

    //List<Alarm> findAllByTruckIdAndZoneLeave(Long truckId, Boolean zoneLeave);

    List<Alarm> findAllByZoneLeave(Boolean zoneLeave);



}

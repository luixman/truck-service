package ru.truckfollower.service;

import lombok.extern.slf4j.Slf4j;
import org.postgis.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.truckfollower.entity.Alarm;
import ru.truckfollower.entity.Company;
import ru.truckfollower.entity.ForbiddenZone;
import ru.truckfollower.model.ForbiddenZoneModel;
import ru.truckfollower.model.TruckRabbitMessageModel;
import ru.truckfollower.service.polygon.Polygon;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class CheckingTruckCoordinatesService {

    private final ForbiddenZoneService forbiddenZoneService;
    private final CompanyService companyService;
    private final AlarmService alarmService;


    //мапа ид компании, валуе лист запретных зон
    private Map<Long, List<ForbiddenZoneModel>> companyZones;

    @Autowired
    public CheckingTruckCoordinatesService(ForbiddenZoneService forbiddenZoneService, CompanyService companyService, AlarmService alarmService) {
        this.forbiddenZoneService = forbiddenZoneService;
        this.companyService = companyService;
        this.alarmService = alarmService;

    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${scheduler-time.service.checking-truck-coordinates-service}", timeUnit = TimeUnit.SECONDS)
    private void initialize() {
        Map<Long, List<ForbiddenZoneModel>> map = new HashMap<>();
        companyService.getAll().forEach(x -> map.put(x.getId(), new ArrayList<>()));

        List<ForbiddenZone> forbiddenZoneList = forbiddenZoneService.getAll();
        List<ForbiddenZoneModel> forbiddenZoneModelList = new ArrayList<>();

        for (ForbiddenZone forbiddenZone : forbiddenZoneList) {
            //если запретная зона деактивирована
            if(forbiddenZone.getDeactivated())
                continue;

            ForbiddenZoneModel model = forbiddenZoneService.toModel(forbiddenZone);

            if (!map.containsKey(model.getCompanyId())) {
                log.error(forbiddenZone + " has no connection with the company");
            } else
                map.get(model.getCompanyId()).add(model);
        }

        companyZones = map;
        log.info("CheckingTruckCoordinatesService has ben initialized or updated. " + companyZones.getClass().getName() + "size: " + companyZones.size());
    }

    public void check(TruckRabbitMessageModel truckRabbitMessageModel, Company company) {

        List<ForbiddenZoneModel> companyList = companyZones.get(company.getId());

        if (alarmService.getTrucksInTheForbiddenZone().containsKey(truckRabbitMessageModel.getUniqId())) {
            alarmService.handleAlarmTruck(truckRabbitMessageModel);
            return;
        }

        if (companyList == null) {
            log.warn(company + " not registered in db");
            return;
        }

        for (ForbiddenZoneModel forbiddenZoneModel : companyList) {
            Polygon polygon = Polygon.geometryToPolygon(forbiddenZoneModel.getGeometry());
            boolean result = polygon.contains(new Point(truckRabbitMessageModel.getX(), truckRabbitMessageModel.getY()));
            if (result) {

                Alarm alarm = alarmService.alarmCreate(truckRabbitMessageModel, forbiddenZoneModel);
                break;
            }
        }
    }
}

package ru.telegrambot.entity;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.*;
import ru.telegrambot.model.ForbiddenZoneModel;
import ru.telegrambot.model.TruckModel;
import ru.telegrambot.service.DefaultInstantDeserializer;

import javax.persistence.*;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint default 0")
    private Long id;


    @JsonDeserialize(using = DefaultInstantDeserializer.class)
    @Column(name = "message_time")
    private Instant messageTime;


    @JsonDeserialize(using = DefaultInstantDeserializer.class)
    @Column(name = "leave_time")
    private Instant leaveTime;


    @Column(name = "zone_leave", columnDefinition = "boolean default false")
    private Boolean zoneLeave;

    @Column(name = "tg_alert", columnDefinition = "boolean default false")
    private Boolean TelegramAlert;


    @Column(name ="message_time_wrong", columnDefinition = "boolean default false")
    private Boolean messageTimeWrong;


    @Transient
    private ForbiddenZoneModel forbiddenZone;


    @Transient
    private TruckModel truck;


    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", messageTime=" + messageTime +
                ", leaveTime=" + leaveTime +
                ", zoneLeave=" + zoneLeave +
                ", isTelegramAlert=" + TelegramAlert +
                ", messageTimeWrong=" + messageTimeWrong +
                '}';
    }
}

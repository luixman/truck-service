package ru.truckfollower.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "telegram_conn")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TelegramConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "chat_id")
    Long chatId;

    @Column(name = "auth_key")
    String authKey;

    @Column(name = "authorized")
    Boolean isAuthorized;

    @Column(name = "first_auth_time")
    Instant firstAuthTime;

}

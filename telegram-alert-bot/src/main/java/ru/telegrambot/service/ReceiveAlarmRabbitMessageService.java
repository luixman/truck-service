package ru.telegrambot.service;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.telegrambot.entity.Alarm;
import ru.telegrambot.service.telegram.TelegramAlarmService;

import java.util.concurrent.ExecutorService;

@Service
public class ReceiveAlarmRabbitMessageService {

    private final TelegramAlarmService telegramAlarmService;

    @Autowired
    public ReceiveAlarmRabbitMessageService(TelegramAlarmService telegramAlarmService) {
        this.telegramAlarmService = telegramAlarmService;
    }


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(Alarm a){
        telegramAlarmService.send(a);


    }

}

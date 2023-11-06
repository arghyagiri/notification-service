package com.tcs.training.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.training.model.notification.NotificationContext;
import com.tcs.training.model.reservation.Reservation;
import com.tcs.training.notification.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceBus {

	private final NotificationService notificationService;

	@Bean
	public Consumer<KStream<UUID, NotificationContext>> notificationProcessor() {
		ObjectMapper objectMapper = new ObjectMapper();
		return input -> input.peek((k, v) -> {
			notificationService
				.add(Notification.builder().message(v.getBody()).createDate(LocalDate.now()).referenceId(k).build());
			notificationService.sendSimpleEmail(v.getContext().get("to"), v.getContext().get("sub"), v.getBody());
		}).peek((uuid, order) -> log.info("Notification processed : {}", order)).map(KeyValue::new);
	}

}

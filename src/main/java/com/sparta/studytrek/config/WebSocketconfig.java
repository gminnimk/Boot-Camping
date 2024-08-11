package com.sparta.studytrek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.sparta.studytrek.websocket.handler.NotificationHandler;
import com.sparta.studytrek.websocket.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketconfig implements WebSocketConfigurer {

	private final NotificationService notificationService;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(notificationHandler(), "/ws/notifications")
			.setAllowedOrigins("*");
	}

	@Bean
	public NotificationHandler notificationHandler() {
		return new NotificationHandler(notificationService);
	}
}

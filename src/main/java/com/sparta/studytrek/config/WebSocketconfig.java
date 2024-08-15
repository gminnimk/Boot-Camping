package com.sparta.studytrek.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.studytrek.domain.auth.repository.UserRepository;
import com.sparta.studytrek.domain.chat.service.ChatService;
import com.sparta.studytrek.websocket.handler.ChatHandler;
import com.sparta.studytrek.websocket.handler.NotificationHandler;
import com.sparta.studytrek.websocket.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketconfig implements WebSocketConfigurer {

	private final NotificationService notificationService;
	private final ChatService chatService;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(notificationHandler(), "/ws/notifications")
			.setAllowedOrigins("*");

		registry.addHandler(chatHandler(), "/ws/chat")
			.setAllowedOrigins("*");
	}

	@Bean
	public NotificationHandler notificationHandler() {
		return new NotificationHandler(notificationService);
	}


	@Bean
	public ChatHandler chatHandler() {
		return new ChatHandler(chatService, objectMapper, userRepository);
	}
}

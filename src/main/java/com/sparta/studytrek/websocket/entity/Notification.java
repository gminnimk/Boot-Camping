package com.sparta.studytrek.websocket.entity;

import com.sparta.studytrek.common.Timestamped;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String message;

	private boolean isRead = false;

	public Notification(String username, String message) {
		this.username = username;
		this.message = message;
	}

	public void updateMessage(String newMessage) {
		this.message = newMessage;
	}

	public void markAsRead() {
		this.isRead = true;
	}
}
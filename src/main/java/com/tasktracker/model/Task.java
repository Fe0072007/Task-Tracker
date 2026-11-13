package com.tasktracker.model;

import java.time.OffsetDateTime;

public class Task {

    private long id;
    private String description;
	private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

    public Task() {}

    public Task(long id, String description) {
        this.id = id;
        this.description = description;
        status = Status.TODO;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }


}

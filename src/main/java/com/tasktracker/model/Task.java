package com.tasktracker.model;

import java.time.LocalDate;

public class Task {

    private long id;
    private String description;
	private LocalDate createdAt;
    private LocalDate updatedAt;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
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
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }


}

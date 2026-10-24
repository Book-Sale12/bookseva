package com.bookseva.admin.dto;
import com.bookseva.message.entity.Message;
import com.bookseva.report.entity.Report;


import com.bookseva.report.entity.Report.ReportStatus;
import jakarta.validation.constraints.NotNull;


public class ReportResolutionRequest {
    @NotNull(message = "New status is required")
    private ReportStatus newStatus;

    private String message;

	public ReportStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(ReportStatus newStatus) {
		this.newStatus = newStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}

package com.datn.request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequest {

	private String title;
	private String description;
	private String status;
	private Long projectId;
	private String priority;
	private LocalDate dueDate;
	private Long userId;
	private List<String> tags=new ArrayList<>();
}

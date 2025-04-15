package com.datn.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
	
	private String title;
	private String imageUrl;
	private String description;
	private Long id;

}

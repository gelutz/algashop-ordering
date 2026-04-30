package com.lutz.algashop.ordering.application.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageFilter {
	private int size = 15;
	private int page = 0;
}

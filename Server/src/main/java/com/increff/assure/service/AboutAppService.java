package com.increff.assure.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AboutAppService {

	@Value("${app.name}")
	private String name;
	@Value("${app.version}")
	private String version;


}

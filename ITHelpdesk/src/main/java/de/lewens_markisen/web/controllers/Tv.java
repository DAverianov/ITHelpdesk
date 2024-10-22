package de.lewens_markisen.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/tv")
@Controller
public class Tv {
	@GetMapping()
	public String tv() {
		return "tv/tv";
	}
}

package com.metrics.project;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}

interface DataRepository extends MongoRepository<DataPoint, String> {

}

@Document(collection = "data")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class DataPoint {
	@Id
	private String id;
	private String time;
	private String size;


	@Override
	public String toString() {
		return id +" "+ time + " - " + size;
	}
}

@Controller
class DataController {

	@Autowired
	private DataRepository dataRepository;

	static int newID=0;

	@GetMapping("/")
	public String index(Model model) {
		getAllData(model);
		return "index";
	}

	@PostMapping("/home")
	public String home(Model model) {
		return "redirect:/";
	}

	@PostMapping("/rawdata")
	public String rawdata(Model model) {
		getAllData(model);
		return "rawdata";
	}

	@GetMapping("/testdata")
	public String next(Model model) {
		dataRepository.save(new DataPoint("1","1:00","10"));
		dataRepository.save(new DataPoint("2","2:00","20"));
		dataRepository.save(new DataPoint("3","3:00","30"));
		return "redirect:/";
	}

	private void getAllData(Model model) {
		List<DataPoint> data = dataRepository.findAll();
		Collections.reverse(data);
		model.addAttribute("data", data);
	}

}
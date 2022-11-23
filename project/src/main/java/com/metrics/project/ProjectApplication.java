package com.metrics.project;

import java.util.Collections;
import java.util.List;

import java.io.IOException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import org.icepear.echarts.Line;
import org.icepear.echarts.charts.line.LineAreaStyle;
import org.icepear.echarts.charts.line.LineSeries;
import org.icepear.echarts.components.coord.cartesian.CategoryAxis;
import org.icepear.echarts.render.Engine;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
class DataController {

	@Autowired
	private DataRepository dataRepository;

	static int newID=0;

	private String handlebarRetrieve(String fileName, String insertData)
	{
		Handlebars handlebars = new Handlebars();
		String html = "";
		try {
			Template template = handlebars.compile("templates/"+fileName);
			html = template.apply(""+insertData);
		} catch (IOException e) {
			System.out.println("template file not found");
		}
		return html;
	}

	@GetMapping("/")
	public String indexGet() {
		return handlebarRetrieve("index",null);
	}

	@PostMapping("/")
	public String indexPost() {
		return handlebarRetrieve("index",null);
		
	@PostMapping("/linesAdded")
	@GetMapping("/linesAdded")
	public String linesAdded(Model model){
		Map<String, Integer> graphData = new TreeMap<>();
		graphData.put("2016", -147);
		graphData.put("2017", 1256);
		graphData.put("2018", -3856);
		graphData.put("2019", 19807);
		model.addAttribute("chartData", graphData);
		return "linesAdded";
	}

	@GetMapping("/graph")
	public String graphGet() {
		return handlebarRetrieve("graph","ECharts Java");
	}

	@PostMapping("/graph")
	public String graphPost() {
		return handlebarRetrieve("graph","ECharts Java");
	}

	@GetMapping("/linechart")
	public ResponseEntity<String> getChart() {
		Line line = new Line()
				.addXAxis(new CategoryAxis()
						.setData(new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" })
						.setBoundaryGap(false))
				.addYAxis()
				.addSeries(new LineSeries()
						.setData(new Number[] { 820, 932, 901, 934, 1290, 1330, 1320 })
						.setAreaStyle(new LineAreaStyle()));
		Engine engine = new Engine();
		// return the full html of the echarts, used in iframes in your own template
		String json = engine.renderHtml(line);
		return ResponseEntity.ok(json);
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
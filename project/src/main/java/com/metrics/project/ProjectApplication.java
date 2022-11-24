package com.metrics.project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.IOException;
import java.util.Scanner;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import lombok.*;
import org.icepear.echarts.Line;
import org.icepear.echarts.Bar;
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

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

}

interface DataRepository extends MongoRepository<DataPoint, String> {

}

class linesData{
	public List<String> names = new ArrayList<>();
	public List<Number> additions = new ArrayList<>();
	public List<Number> deletions = new ArrayList<>();
	public List<Number> total = new ArrayList<>();
	public String[] getNamesArray(){
		return names.toArray(new String[0]);
	}
	public Number[] getAdditionsArray(){
		return additions.toArray(new Number[0]);
	}
	public Number[] getDeletionsArray(){
		return deletions.toArray(new Number[0]);
	}
	public Number[] getTotalArray(){
		return total.toArray(new Number[0]);
	}
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
	}

	@GetMapping("/graph")
	public String graphGet() {
		return handlebarRetrieve("graph","ECharts Java");
	}

	@PostMapping("/graph")
	public String graphPost() {
		return handlebarRetrieve("graph","ECharts Java");
	}

	@GetMapping("/lines")
	public String linesGet() {
		return handlebarRetrieve("lines","ECharts Java");
	}

	@PostMapping("/lines")
	public String linesPost() {
		return handlebarRetrieve("lines","ECharts Java");
	}

	public static linesData getTopTen(linesData raw){
		linesData top = new linesData();
		Number[] rawTotal = raw.getTotalArray();
		List<Integer> ignore = new ArrayList<>();
		Number max = rawTotal[0];
		int maxIndex = 0;
		while(ignore.size() < 10) {
			for(int i = 1; i < rawTotal.length; i++) {
				if (rawTotal[i].intValue() > max.intValue() && !ignore.contains(i)) {
					max = rawTotal[i];
					maxIndex = i;
				}
			}
			max = 0;
			ignore.add(maxIndex);
		}
		for(int j = 0; j < ignore.size(); j++){
			top.names.add(raw.names.get(ignore.get(j)));
			top.additions.add(raw.additions.get(ignore.get(j)));
			top.deletions.add(raw.deletions.get(ignore.get(j)));
		}
		return top;
	}

	public static linesData readJSONData(ClassPathResource resource){
		linesData data = new linesData();
		try {
			String fullJSONFile = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
			Scanner scanner = new Scanner(fullJSONFile);
			while (scanner.hasNextLine()) {
				String useless = scanner.nextLine();
				String name = scanner.nextLine();
				String addedLines = scanner.nextLine();
				String deletedLines = scanner.nextLine();
				name = name.substring(name.indexOf("\"") + 1);
				name = name.substring(0, name.indexOf("\""));
				addedLines = addedLines.substring(addedLines.indexOf("\"") + 1);
				addedLines = addedLines.substring(0, addedLines.indexOf("\""));
				deletedLines = deletedLines.substring(deletedLines.indexOf("\"") + 1);
				deletedLines = deletedLines.substring(0, deletedLines.indexOf("\""));
				int added = Integer.parseInt(addedLines);
				int deleted = Integer.parseInt(deletedLines);
				int total = added + deleted;
				System.out.println(name);
				System.out.println(addedLines);
				System.out.println(deletedLines);
				data.names.add(name);
				data.additions.add(added);
				data.deletions.add(deleted);
				data.total.add(total);
				if(name.equals("yuuri")){
					break;
				}
			}
			scanner.close();

		} catch (IOException e) {
			return data;
		}
		return data;
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



	@GetMapping("/linesChanged")
	public ResponseEntity<String> linesChanged() {
		ClassPathResource staticDataResource = new ClassPathResource("AddDelData.json");
		linesData data = readJSONData(staticDataResource);
		linesData topTenData = getTopTen(data);
		String[] namesArray = topTenData.getNamesArray();
		Number[] additionsArray = topTenData.getAdditionsArray();
		Number[] deletionsArray = topTenData.getDeletionsArray();
		Bar bar = new Bar()
				.setLegend()
				.setTooltip("item")
				.addXAxis(namesArray)
				.addYAxis()
				.addSeries("Additions", additionsArray)
				.addSeries("Deletions", deletionsArray);
		Engine engine = new Engine();
		// return the full html of the echarts, used in iframes in your own template
		String json = engine.renderHtml(bar);
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
package com.metrics.project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

	public static int commitDataName  = 0;
	public static int commitDataYear  = 1;
	public static int commitDataMonth  = 2;
	public static int commitDataDay  = 3;
	public static int commitDataCommitCount = 4;
	public static int commitDataAdd = 5;
	public static int commitDataDel = 6;
	public static int commitSplitDataName  = 0;
	public static int commitSplitDataDate  = 1;
	public static int commitSplitDataCommitCount = 2;
	public static int commitSplitDataAdd = 3;
	public static int commitSplitDataDel = 4;
	public static int splitDateYear  = 0;
	public static int splitDateMonth  = 1;
	public static int splitDateDay = 2;

	static boolean textDataRead=false;
	ArrayList<String[]> commits = new ArrayList<>();
	String[] months;
	Number[] monthsCount;
	Number[] monthsAdd;
	Number[] monthsDel;

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

	@GetMapping("/graphAD")
	public String graphGetAD() {
		return handlebarRetrieve("graphAD","ECharts Java");
	}

	@PostMapping("/graphAD")
	public String graphPostAD() {
		return handlebarRetrieve("graphAD","ECharts Java");
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
				//System.out.println(name);
				//System.out.println(addedLines);
				//System.out.println(deletedLines);
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
		if(!textDataRead) readTextData();
		Line line = new Line()
				.addXAxis(new CategoryAxis()
						.setData(months)
						.setBoundaryGap(false))
				.addYAxis()
				.addSeries(new LineSeries()
						.setData(monthsCount)
						.setAreaStyle(new LineAreaStyle()));
		Engine engine = new Engine();
		// return the full html of the echarts, used in iframes in your own template
		String json = engine.renderHtml(line);
		return ResponseEntity.ok(json);
	}

	@GetMapping("/linechartAD")
	public ResponseEntity<String> getChartAD() {
		if(!textDataRead) readTextDataAD();
		Line line = new Line()
				.addXAxis(new CategoryAxis()
						.setData(months)
						.setBoundaryGap(false))
				.addYAxis()
				.addSeries(new LineSeries()
						.setData(monthsAdd)
						.setAreaStyle(new LineAreaStyle()))
				.addSeries(new LineSeries()
						.setData(monthsDel)
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

	public void readTextData()
	{
		try
		{
			FileReader fileReader = new FileReader("src/main/resources/TextData.txt");// Enter the entire path of the file if needed
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			boolean endOfFile = false;
			while(!endOfFile)
			{
				String commit = bufferedReader.readLine();
				if (commit != null)
				{
					String[] commitSplit = commit.split(",");
					String[] commitData = new String[7];
					commitData[commitDataName] = commitSplit[commitSplitDataName];
					commitData[commitDataCommitCount] = commitSplit[commitSplitDataCommitCount];
					commitData[commitDataAdd] = commitSplit[commitSplitDataAdd];
					commitData[commitDataDel] =commitSplit[commitSplitDataDel];
					String[] splitDate = commitSplit[commitSplitDataDate].split("-");
					commitData[commitDataYear] = splitDate[splitDateYear];
					commitData[commitDataMonth] = splitDate[splitDateMonth];
					commitData[commitDataDay] = splitDate[splitDateDay];
					commits.add(commitData);
					textDataRead=true;
				}
				else
				{
					endOfFile = true;
				}
			}
			bufferedReader.close();
			fileReader.close();
			yearMonth();
		} // End try

		catch (FileNotFoundException e)
		{
			months = new String[]{"e"};
			monthsCount = new Number[]{0};
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void yearMonth()
	{
		ArrayList<YMNode> yearMonth= new ArrayList<>();
		for(int i=0;i<commits.size();i++)
		{
			String[] line = commits.get(i);
			YMNode currentDate= new YMNode(line[commitDataYear],line[commitDataMonth],line[commitDataCommitCount]);
			if(yearMonth.isEmpty()) yearMonth.add(currentDate);
			else
			{
				boolean added=false;
				for(int j=0;(j<yearMonth.size())&&!added;j++)
				{
					YMNode compareNode=yearMonth.get(j);
					if(currentDate.YM==compareNode.YM)
					{
						yearMonth.set(j,new YMNode(currentDate.YM,currentDate.count+compareNode.count));
						added=true;
					}
					else if(currentDate.YM<compareNode.YM)
					{
						yearMonth.add(j,currentDate);
						added=true;
					}
				}
				if(!added) yearMonth.add(currentDate);
			}
			if(i==39)
			{
				//System.out.print("d");
			}
		}
		formats(yearMonth);
	}

	void formats(ArrayList<YMNode> yearMonth)
	{
		YMNode first=yearMonth.get(0);
		YMNode last= yearMonth.get(yearMonth.size()-1);
		//int size= (((last.YM/100-first.YM/100)-1)*12)+(11-first.YM%100)+(last.YM%100);
		int fullYears= (((last.YM/100-first.YM/100)-1)*12);
		int monFirst=13-(first.YM%100);
		int monLast=(last.YM%100);
		int size=fullYears+monFirst+monLast;
		months = new String[size];
		monthsCount = new Number[size];
		int monthFormat=first.YM;
		int j=0;
		for(int i=0;i<size;i++)
		{
			if(yearMonth.get(j).YM==monthFormat)
			{
				monthsCount[i]=yearMonth.get(j).count;
				if(j<yearMonth.size()-1)j++;
			}
			else monthsCount[i]=0;
			String monthFormatted=""+monthFormat++;
			if(monthFormat%100==13)monthFormat+=88;
			monthFormatted=monthFormatted.substring(0,4)+"-"+monthFormatted.substring(4,6);
			months[i]=monthFormatted;
		}
		//System.out.print("d");
	}

	class YMNode
	{
		int YM;
		int count;

		public YMNode(String year, String month, String count)
		{
			this.YM =(Integer.parseInt(year)*100)+Integer.parseInt(month);
			this.count=Integer.parseInt(count);
		}

		public YMNode(int YM, int count) {
			this.YM=YM;
			this.count=count;
		}
	}

	public void readTextDataAD()
	{
		try
		{
			FileReader fileReader = new FileReader("src/main/resources/TextData.txt");// Enter the entire path of the file if needed
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			boolean endOfFile = false;
			while(!endOfFile)
			{
				String commit = bufferedReader.readLine();
				if (commit != null)
				{
					String[] commitSplit = commit.split(",");
					String[] commitData = new String[7];
					commitData[commitDataName] = commitSplit[commitSplitDataName];
					commitData[commitDataCommitCount] = commitSplit[commitSplitDataCommitCount];
					commitData[commitDataAdd] = commitSplit[commitSplitDataAdd];
					commitData[commitDataDel] =commitSplit[commitSplitDataDel];
					String[] splitDate = commitSplit[commitSplitDataDate].split("-");
					commitData[commitDataYear] = splitDate[splitDateYear];
					commitData[commitDataMonth] = splitDate[splitDateMonth];
					commitData[commitDataDay] = splitDate[splitDateDay];
					commits.add(commitData);
					textDataRead=true;
				}
				else
				{
					endOfFile = true;
				}
			}
			bufferedReader.close();
			fileReader.close();
			yearMonthAD();
		} // End try

		catch (FileNotFoundException e)
		{
			months = new String[]{"e"};
			monthsCount = new Number[]{0};
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	void yearMonthAD()
	{
		ArrayList<YMADNode> yearMonthAD= new ArrayList<>();
		for(int i=0;i<commits.size();i++)
		{
			String[] line = commits.get(i);
			YMADNode currentDate=
					new YMADNode(line[commitDataYear],line[commitDataMonth],line[commitDataAdd],line[commitDataDel]);
			if(yearMonthAD.isEmpty()) yearMonthAD.add(currentDate);
			else
			{
				boolean added=false;
				for(int j=0;(j<yearMonthAD.size())&&!added;j++)
				{
					YMADNode compareNode=yearMonthAD.get(j);
					if(currentDate.YM==compareNode.YM)
					{
						yearMonthAD.set(j, new YMADNode(currentDate.YM,currentDate.add+compareNode.add,
								currentDate.del+compareNode.del));
						added=true;
					}
					else if(currentDate.YM<compareNode.YM)
					{
						yearMonthAD.add(j,currentDate);
						added=true;
					}
				}
				if(!added) yearMonthAD.add(currentDate);
			}
			if(i==39)
			{
				System.out.print("d");}
		}
		formatsAD(yearMonthAD);
	}

	void formatsAD(ArrayList<YMADNode> yearMonthAD)
	{
		YMADNode first=yearMonthAD.get(0);
		YMADNode last= yearMonthAD.get(yearMonthAD.size()-1);
		//int size= (((last.YM/100-first.YM/100)-1)*12)+(11-first.YM%100)+(last.YM%100);
		int fullYears= (((last.YM/100-first.YM/100)-1)*12);
		int monFirst=13-(first.YM%100);
		int monLast=(last.YM%100);
		int size=fullYears+monFirst+monLast;
		months = new String[size];
		monthsAdd = new Number[size];
		monthsDel = new Number[size];
		int monthFormat=first.YM;
		int j=0;
		for(int i=0;i<size;i++)
		{
			if(yearMonthAD.get(j).YM==monthFormat)
			{
				monthsAdd[i]=yearMonthAD.get(j).add;
				monthsDel[i]=-yearMonthAD.get(j).del;
				if(j<yearMonthAD.size()-1)j++;
			}
			else
			{
				monthsAdd[i]=0;
				monthsDel[i]=0;
			}
			String monthFormatted=""+monthFormat++;
			if(monthFormat%100==13)monthFormat+=88;
			monthFormatted=monthFormatted.substring(0,4)+"-"+monthFormatted.substring(4,6);
			months[i]=monthFormatted;
		}
		System.out.print("d");
	}

	class YMADNode
	{
		int YM;
		int add;
		int del;

		public YMADNode(String year, String month, String add, String del)
		{
			this.YM =(Integer.parseInt(year)*100)+Integer.parseInt(month);
			this.add=Integer.parseInt(add);
			this.del=Integer.parseInt(del);
		}

		public YMADNode(int YM, int add, int del) {
			this.YM=YM;
			this.add=add;
			this.del=del;
		}
	}
}
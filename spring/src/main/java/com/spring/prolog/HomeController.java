package com.spring.prolog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;
import com.spring.prolog.connection.DBConnection;
import com.spring.prolog.dao.DAO;
import com.spring.prolog.database.MyConstants;
import com.spring.prolog.dto.DTO;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/home")
	public String gotohome(Model model,
			@RequestParam(value = "name", required = false) String name) {
		
		return "home";
	}
	
	@RequestMapping("/signup")
	public String gotosignup(Model model,
			@RequestParam(value = "a", required = false) String a) {
		
		return "signup";
	}
	
	@RequestMapping("/nhome")
	public String gotologin(Model model,
			@RequestParam(value = "som", required = false) String som) {
		
		return "nhome";
	}
	
	@RequestMapping("/nhome2")
	public String gotologin2(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "nhome2";
		
		return returnStr;
	}
	
	@RequestMapping(value="/dashboard",method = RequestMethod.GET)
	public String gotoSIGNUP2(HttpServletRequest request, HttpServletResponse response)
	{
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "dashboard";
		
		return returnStr;
	}

	@RequestMapping(value = "/dashboard",method = RequestMethod.POST)
	public String gotosignup(@RequestParam(value = "file",required=false)MultipartFile file) {
		String fileName = file.getOriginalFilename();
		boolean flag = false;
		File defaultFile = null;
		
		if(fileName.equals("")){
			
		}
		else{
			File f = new File("templog.log");
			
			try {
				file.transferTo(f);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			DAO dao = new DAO();
			dao.initTable();
			try {
				dao.parseLog(f);
			} catch (IOException e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			} catch (GeoIp2Exception e) {
				// TODO 자동 생성된 catch 블록
				e.printStackTrace();
			}
		}
		
		return "dashboard";
	}
	
	@RequestMapping("/summary")
	public String gotomain(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "summary";
		
		return returnStr;
	}
	
	@RequestMapping("/country")
	public String gotocountry(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "country";
		
		return returnStr;
	}
	
	@RequestMapping("/time")
	public String gototime(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "time";
		
		return returnStr;
	}
	
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	public String gotoprint(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		if(id==null || id.equals("")){
			return "home";
		}
		
		DAO dao = new DAO();

        int rowCount = dao.getRowNum("select * from warningLog");
          
        File dbFile = new File(MyConstants.DATABASE_CITY_PATH);
        DatabaseReader reader = null;
		try {
			reader = new DatabaseReader.Builder(dbFile).build();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
    	String[] arr1 = new String[rowCount];	//모든 국가 저장
    	String[] arr2 = new String[rowCount];	//모든 날짜 저장
    	String[] arr3 = new String[rowCount];	//모든 IP 저장
    	String[] arr4 = new String[rowCount];	//모든 공격형태 저장
    	String[] arr5 = new String[rowCount];	//모든 시간 저장
    	
		try{
			int num1 = 0, num2 = 0, num3 = 0, num4 = 0, num5 = 0, num6 = 0;
			
			ArrayList<DTO> list = dao.getAllData("select * from warningLog");
			
			for(int i=0; i<list.size(); i++){
				arr2[num2++] = list.get(i).getDate();
				arr3[num3] = list.get(i).getClientIP();
				try{
			       	InetAddress ipAddress = InetAddress.getByName(arr3[num3]);
			        CityResponse r = reader.city(ipAddress);

			        Country country = r.getCountry();
			        arr1[num1++] = country.getIsoCode().toLowerCase();
			    }catch(AddressNotFoundException e){
			       	arr1[num1++] = "kr";
			    }
				num3++;
				arr4[num4++] = list.get(i).getProblem();
				arr5[num5++] = list.get(i).getTime();
			}
	    	
	    	for(int i=0; i<arr5.length; i++){
	    		String[] test = arr5[i].split(":");
	    		arr5[i] = test[0];
	    	}
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int countryCount = 1, dateCount = 1, clientIPCount = 1, attackCount = 1, timeCount = 1;	//다른 날짜, IP 개수, 공격형태
    	
    	for(int i=0; i<arr1.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr1[i].equals(arr1[j])){
    				break;
    			}
    			else c++;
    			
    			if(j==i-1){
    				if(c==j+1){
    					countryCount++;
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr2.length; i++){
    		int c = 0;
    		//arr2[i] = arr2[i].substring(5);
    		//arr2[i] = arr2[i].replace("-", "/");
    		for(int j=0; j<i; j++){
    			if(arr2[i].equals(arr2[j])){
    				break;
    			}
    			else c++;
    			
    			if(j == i - 1){
    				if(c == j + 1){
    					dateCount++;
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr3.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr3[i].equals(arr3[j])){
    				break;
    			}
    			else c++;
    			
    			if(j==i-1){
    				if(c==j+1){
    					clientIPCount++;
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr4.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr4[i].equals(arr4[j])){
    				break;
    			}
    			else c++;
    			
    			if(j==i-1){
    				if(c==j+1){
    					attackCount++;
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr5.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr5[i].equals(arr5[j])){
    				break;
    			}
    			else c++;
    			
    			if(j==i-1){
    				if(c==j+1){
    					timeCount++;
    				}
    			}
    		}
    	}
    	
    	//System.out.println(dateCount + " " + clientIPCount);
    	
    	String[] countryArr = new String[countryCount];	//겹치지 않는 나라
    	int[] countryValue = new int[countryCount];		// 나라에 해당되는 공격 횟수
    	double[] countryRate = new double[countryCount];
    	for(int i=0; i<countryValue.length; i++){
    		countryValue[i] = 0;
    	}
    	for(int i=0; i<countryRate.length; i++){
    		countryRate[i] = 0;
    	}
    	
    	String[] dateArr = new String[dateCount];	//겹치지 않는 날짜
    	int[] dateValue = new int[dateCount];		//날짜에 해당되는 공격 횟수
    	for(int i=0; i<dateValue.length; i++){
    		dateValue[i] = 0;
    	}
    	
    	String[] clientIPArr = new String[clientIPCount];	//겹치지 않는 IP
    	int[] clientIPValue = new int[clientIPCount];		//공격당한 아이피 횟수
    	double[] clientIPRate = new double[clientIPCount];		//IP 비율
    	for(int i=0; i<clientIPValue.length; i++){
    		clientIPValue[i] = 0;
    	}
    	for(int i=0; i<clientIPRate.length; i++){
    		clientIPRate[i] = 0;
    	}
    	
    	String[] attackArr = new String[attackCount];	//겹치지 않는 공격형태
    	int[] attackValue = new int[attackCount];
    	double[] attackRate = new double[attackCount];
    	for(int i=0; i<attackValue.length; i++){
    		attackValue[i] = 0;
    	}
    	for(int i=0; i<attackRate.length; i++){
    		attackRate[i] = 0;
    	}
    	
    	String[] timeArr = new String[timeCount];
    	int[] timeValue = new int[timeCount];
    	for(int i=0; i<timeValue.length; i++){
    		timeValue[i] = 0;
    	}
    	
    	countryArr[0] = arr1[0];
    	int k = 1;
    	for(int i=0; i<arr1.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr1[i].equals(arr1[j])){
    				break;
    			}
    			else c++;
    			
    			if(j == i - 1){
    				if(c == j + 1){
    					countryArr[k++] = arr1[i];
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr1.length; i++){
    		for(int j=0; j<countryArr.length; j++){
    			if(countryArr[j].equals(arr1[i])){
    				countryValue[j]++;
    			}
    		}
    	}
    	for(int i=0; i<countryRate.length; i++){
    		countryRate[i] = countryValue[i] * 100.0 /  arr1.length;
    		countryRate[i] = Math.round(countryRate[i] * 100d) / 100d;
    	}
    	
    	dateArr[0] = arr2[0];
    	k = 1;
    	for(int i=0; i<arr2.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr2[i].equals(arr2[j])){
    				break;
    			}
    			else c++;
    			
    			if(j == i - 1){
    				if(c == j + 1){
    					dateArr[k++] = arr2[i];
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr2.length; i++){
    		for(int j=0; j<dateArr.length; j++){
    			if(dateArr[j].equals(arr2[i])){
    				dateValue[j]++;
    			}
    		}
    	}

    	
    	clientIPArr[0] = arr3[0];
    	k = 1;
    	for(int i=0; i<arr3.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr3[i].equals(arr3[j])){
    				break;
    			}
    			else c++;
    			
    			if(j == i - 1){
    				if(c == j + 1){
    					clientIPArr[k++] = arr3[i];
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr3.length; i++){
    		for(int j=0; j<clientIPArr.length; j++){
    			if(clientIPArr[j].equals(arr3[i])){
    				clientIPValue[j]++;
    			}
    		}
    	}
    	
    	for(int i=0; i<clientIPRate.length; i++){
    		clientIPRate[i] = clientIPValue[i] * 100.0 /  arr3.length;
    		clientIPRate[i] = Math.round(clientIPRate[i] * 100d) / 100d;
    	}
    	
    	attackArr[0] = arr4[0];
    	k = 1;
    	for(int i=0; i<arr4.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr4[i].equals(arr4[j])){
    				break;
    			}
    			else c++;
    			
    			if(j == i - 1){
    				if(c == j + 1){
    					attackArr[k++] = arr4[i];
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr4.length; i++){
    		for(int j=0; j<attackArr.length; j++){
    			if(attackArr[j].equals(arr4[i])){
    				attackValue[j]++;
    			}
    		}
    	}
    	
    	for(int i=0; i<attackRate.length; i++){
    		attackRate[i] = attackValue[i] * 100.0 /  arr4.length;
    		attackRate[i] = Math.round(attackRate[i] * 100d) / 100d;
    	}
    	
    	timeArr[0] = arr5[0];
    	k = 1;
    	for(int i=0; i<arr5.length; i++){
    		int c = 0;
    		for(int j=0; j<i; j++){
    			if(arr5[i].equals(arr5[j])){
    				break;
    			}
    			else c++;
    			
    			if(j == i - 1){
    				if(c == j + 1){
    					timeArr[k++] = arr5[i];
    				}
    			}
    		}
    	}
    	
    	for(int i=0; i<arr5.length; i++){
    		for(int j=0; j<timeArr.length; j++){
    			if(timeArr[j].equals(arr5[i])){
    				timeValue[j]++;
    			}
    		}
    	}
    	
    	for(int i=0; i<dateArr.length; i++){
    		for(int j = i+1; j<dateArr.length; j++){
    			String a[] = dateArr[i].split("-");
    			String b[] = dateArr[j].split("-");
    			String date1 = a[0] + a[1] + a[2];
    			String date2 = b[0] + b[1] + b[2];
    			
    			int a1 = Integer.parseInt(date1);
    			int a2 = Integer.parseInt(date2);
    			
    			if(a1 > a2){
    				String temp1 = dateArr[i];
    				dateArr[i] = dateArr[j];
    				dateArr[j] = temp1;
    				
    				int temp2 = dateValue[i];
    				dateValue[i] = dateValue[j];
    				dateValue[j] = temp2;
    				
    			}
    		}
    	}
    	
    	for (int i = 0; i < countryRate.length; i++) {
            for (int j = i + 1; j < countryRate.length; j++) {
                if (countryRate[i] < countryRate[j]) {
                    double temp1 = countryRate[i];
                    countryRate[i] = countryRate[j];
                    countryRate[j] = temp1;
                    
                    String temp2 = countryArr[i];
                    countryArr[i] = countryArr[j];
                    countryArr[j] = temp2;
                }
            }
        }
    	
    	for (int i = 0; i < clientIPRate.length; i++) {
            for (int j = i + 1; j < clientIPRate.length; j++) {
                if (clientIPRate[i] < clientIPRate[j]) {
                    double temp1 = clientIPRate[i];
                    clientIPRate[i] = clientIPRate[j];
                    clientIPRate[j] = temp1;
                    
                    String temp2 = clientIPArr[i];
                    clientIPArr[i] = clientIPArr[j];
                    clientIPArr[j] = temp2;
                }
            }
        }
    	
    	int checkedLog = dao.getRowNum("select * from userLog");
     	int detectedLog = dao.getRowNum("select * from warningLog");
     	int dangerousLog = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
		
		try {
			File f = new File(request.getSession().getServletContext().getRealPath("/")+"/resources/report.pdf");
			if(f.exists())
			{
				f.delete();
			}
			// 파일 다운로드 설정
			response.setContentType("application/pdf");
			String fileName = URLEncoder.encode("로그 분석 보고서", "UTF-8"); // 파일명이 한글일 땐 인코딩 필요
			response.setHeader("Content-Transper-Encoding", "binary");
			response.setHeader("Content-Disposition", "inline; filename=" + fileName + ".pdf");
			 
			
			
	        Document document = new Document();
	        PdfWriter.getInstance(document, new FileOutputStream(f));
	        document.open();
	        BaseFont objBaseFont = BaseFont.createFont("font/MALGUN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	        Font font = new Font(objBaseFont, 10);
	        Font font1 = new Font(objBaseFont,10,Font.BOLD);
	        Font subtext = new Font(objBaseFont,6,Font.NORMAL);
	        Font subject = new Font(objBaseFont,13,Font.BOLD);
	        Font busubject = new Font(objBaseFont,12,Font.NORMAL);
	        Font content = new Font(objBaseFont,11,Font.BOLD);
	        Font result = new Font(objBaseFont,11,Font.NORMAL);
	        Font result2 =new Font(objBaseFont,12,Font.NORMAL);
	        Font menual =new Font(objBaseFont,8,Font.NORMAL);
	        Font font2 = new Font(objBaseFont,18,Font.BOLD);	     
	        
	        Chunk chunk = new Chunk("로그 분석 보고서\n\n", font);
	        Chapter chapter = new Chapter(new Paragraph(chunk), 1);
	        chapter.setNumberDepth(0);
	        Paragraph p1 = new Paragraph("PRO|LOG\n\n", font);
	        p1.setAlignment(Element.ALIGN_CENTER);
	        chapter.add(p1);
	        PdfPTable table = new PdfPTable(3);
	        table.setTotalWidth(160);
	        table.setLockedWidth(true);
	        PdfPCell cell;
	        cell = new PdfPCell(new Phrase("담당",font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        cell = new PdfPCell(new Phrase("팀장",font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        cell = new PdfPCell(new Phrase("사장",font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        table.addCell("\n\n\n");
	        table.addCell("\n\n\n");
	        table.addCell("\n\n\n");
	        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        
	        chapter.add(table);
	        
	        Paragraph p2 = new Paragraph("\n\n\n로그 분석 보고서\n\n", font2);
	        p2.setAlignment(Element.ALIGN_CENTER);
	        chapter.add(p2);
	        
	        Paragraph p3 = new Paragraph("────────────────────────────────────────────────\n\n총괄 보고서\n\n\n\n\n", font);
	        p3.setAlignment(Element.ALIGN_CENTER);
	        chapter.add(p3);
	        
	        
	        Date d = new Date();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        String t = sdf.format(d);
	        
	        Paragraph p4 = new Paragraph(t + "\n\n\n\n\n\n\n\n",font1);
	        p4.setAlignment(Element.ALIGN_CENTER);
	        chapter.add(p4);
	        
	        PdfPTable table2 = new PdfPTable(1);
	        table2.setTotalWidth(240);
	        table2.setLockedWidth(true);
	        Phrase ph = new Phrase("\n  <목차>\n\n",font);
	        ph.add(new Phrase("      1. 로그 분석 결과\n\n      2. 로그 세부 분석 사항\n\n          A. 로그 현황 분석\n\n          B. 차트 세부 분석\n\n", font));
	        cell = new PdfPCell(ph);
	        table2.addCell(cell);
	        
	        table2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        chapter.add(table2);
	        
	        Paragraph p5 = new Paragraph("\n\n\n\n\n\n\n\n\n이 문서는 사용자의 로그를 기반으로 분석한 자료입니다. 참고용으로 사용하시길 바랍니다\n\n-1-",font);
	        p5.setAlignment(Element.ALIGN_CENTER);
	        chapter.add(p5);
	        
	        
	        
	        
	        //////////////////////////////////////////////1페이지 끝.
	 
	        Chunk chunk2 = new Chunk("로그 분석 보고서\n\n", font);
	        Chapter chapter2 = new Chapter(new Paragraph(chunk2), 1);
	        chapter2.setNumberDepth(0);
	        Paragraph p6 = new Paragraph("   1. 로그 분석 결과\n\n\n",subject);
	        chapter2.add(p6);
	        
	        
	        PdfPTable table3 = new PdfPTable(1);
	        table3.setTotalWidth(470);
	        table3.setLockedWidth(true);
	        Phrase summery = new Phrase("\n전체 로그 중 총 " + detectedLog + "개의 위험이 탐지되었습니다.\n\n (탐지 로그/전체 로그 : " + detectedLog + "/" + checkedLog + ")\n\n 탐지 된 " + detectedLog + "개의 위험 중 심각한 위험이 " + dangerousLog + "개 발견되었습니다.\n\n 전문가의 조치가 즉시 필요합니다.\n\n PRO|LOG 홈페이지의 고객센터에서 도움을 받으시는 것을 권고합니다.\n\n",content);
	        cell = new PdfPCell(summery);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table3.addCell(cell);
	        chapter2.add(table3);
	        
	        Paragraph p7 = new Paragraph("\n   2. 로그 분석 세부 사항\n\n",subject);
	        chapter2.add(p7);
	        Paragraph p8 = new Paragraph("      A. 로그 현황 분석\n\n",busubject);
	        chapter2.add(p8);
	        
	        PdfPTable table4 = new PdfPTable(3);
	        table4.setWidthPercentage(100);
	        table4.getDefaultCell().setUseAscender(true);
	        table4.getDefaultCell().setUseDescender(true);
	        table4.setTotalWidth(470);
	        table4.setLockedWidth(true);
	        
	        cell = new PdfPCell(new Phrase("\n로그 현황\n", font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setColspan(3);
	        
	        table4.addCell(cell);
	        cell = new PdfPCell(new Phrase("\n전체 로그\n",font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table4.addCell(cell);
	        /*Paragraph number1=new Paragraph();
	        number1.add("탐지 로그¹");*/
	        cell = new PdfPCell(new Phrase("\n탐지 로그¹\n",font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table4.addCell(cell);
/*	        Paragraph number2=new Paragraph();
	        number2.add("위험 로그 a²");*/
	        cell = new PdfPCell(new Phrase("\n위험 로그²\n",font1));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table4.addCell(cell);
	        table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	        table4.addCell("\n" + checkedLog + "\n");
	        table4.addCell("\n" + detectedLog + "\n");
	        table4.addCell("\n" + dangerousLog + "\n");
	        chapter2.add(table4);
	        
	        String str;
	        if(dateArr[0].equals(dateArr[dateArr.length - 1])){
	        	str = "\n        ▶   로그 수집 기간 : " + dateArr[0] + "\n        ▶   로그 출처\n          -IP : 125.141.149.11\n          -PORT : 8889\n          -운영체제 : Microsoft Internet Information Services 6.0\n\n";
	        }
	        else{
	        	str = "\n        ▶   로그 수집 기간 : " + dateArr[0] + " ~ " + dateArr[dateArr.length - 1] + "\n        ▶   로그 출처\n          -IP : 125.141.149.11\n          -PORT : 8889\n          -운영체제 : Microsoft Internet Information Services 6.0\n\n";
	        }
	        
	        Paragraph p9 = new Paragraph(str, menual);
	        chapter2.add(p9);
	        
	        float[] columnWidths = {5, 0.3f, 2, 4};
	        PdfPTable table5 = new PdfPTable(columnWidths);
	        table5.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table5.setTotalWidth(500);
	        table5.setLockedWidth(true);
	        
	        String str1 = "√ 로그 분석 결과 공격이 많이 들어온 국가는 ";
	        int max1 = 4;
	        if(countryArr.length < 4) max1 = countryArr.length;
	        
	        for(int i=0; i<max1; i++){
	        	Locale loc = new Locale("", countryArr[i]);
	        	if(i != max1 - 1){
	        		str1 += loc.getDisplayCountry() + "(" + countryRate[i] + "%), ";
	        	}
	        	else str1 += loc.getDisplayCountry() + "(" + countryRate[i] + "%) 순입니다.\n\n";
	        }
	        
	        String str2 = "√ 로그 분석 결과 공격이 많이 들어온 IP는 ";
	        int max2 = 4;
	        if(clientIPArr.length < 4) max2 = clientIPArr.length;
	        for(int i=0; i<max2; i++){
	        	InetAddress ipAddress = InetAddress.getByName(clientIPArr[i]);
    	        CityResponse r = reader.city(ipAddress);

    	        Country country = r.getCountry();
    	        String c = country.getIsoCode().toLowerCase();
	        	
	        	Locale loc = new Locale("", c);
	        	if(i != max2 - 1){
	        		str2 += clientIPArr[i] + "(" + loc.getDisplayCountry() + "), ";
	        	}
	        	else str2 +=clientIPArr[i] + "(" + loc.getDisplayCountry() + ") 순입니다.\n\n";
	        }
	        
	        //Phrase res = new Phrase("\n -로그 분석 결과 공격이 많이 들어온 국가는 대한민국(80.54%), 미국(14.07%), 인도네시아(5.39%)순 입니다.\n\n -로그 분석 결과 공격이 많이 들어온 IP는 124.2.44.105(한국), 121.11.222.33(한국), 221.39.150.195(미국)순 입니다.\n\n",result);
	        Paragraph res = new Paragraph(str1 + str2, result);
	        res.setIndentationLeft(8);
	        res.setLeading(0, 2);
	        cell=new PdfPCell();
	        cell.setRowspan(9);
	        cell.addElement(res);
	        table5.addCell(cell);
	        
	        cell=new PdfPCell();
	        cell.setBorderWidth(0);
	        cell.setRowspan(9);
	        table5.addCell(cell);
	        
	        Paragraph name = new Paragraph("공격 빈도가 높은 국가 및  IP\n\n",font1);
	        name.setAlignment(Element.ALIGN_CENTER);
	        cell =new PdfPCell();
	        cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(name);
	        table5.addCell(cell);
	        
	        Paragraph country= new Paragraph("국가",font1);
	        country.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setRowspan(4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.addElement(country);
	        table5.addCell(cell);
	        
	        String co[] = new String[4];
	        for(int i=0; i<max1; i++){
	        	int j = i+1;
	        	Locale loc = new Locale("", countryArr[i]);
	        	co[i] = "  " + j + ". " + loc.getDisplayCountry() + " (" + countryRate[i] + "%)";
	        }
	        for(int i=countryArr.length; i<4; i++){
	        	co[i] = " ";
	        }
	        
	        String cl[] = new String[4];
	        for(int i=0; i<max2; i++){
	        	int j = i+1;
	        	cl[i] = "  " + j + ". " + clientIPArr[i] + " (" + clientIPRate[i] + "%)";
	        }
	        for(int i=clientIPArr.length; i<4; i++){
	        	cl[i] = " ";
	        }
	        
	        Paragraph country1 = new Paragraph(co[0], font1);
	        country1.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(country1);
	        table5.addCell(cell);
	        
	        Paragraph country2 = new Paragraph(co[1], font1);
	        country2.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(country2);
	        table5.addCell(cell);
	        
	        Paragraph country3 = new Paragraph(co[2], font1);
	        country3.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(country3);
	        table5.addCell(cell);
	        
	        Paragraph country4 = new Paragraph(co[3],font1);
	        country4.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(country4);
	        table5.addCell(cell);
	        
	        Paragraph IP= new Paragraph("IP",font1); 
	        IP.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setRowspan(4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.addElement(IP);
	        table5.addCell(cell);
	        
	        Paragraph IP1 = new Paragraph(cl[0], font1);
	        IP1.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(IP1);
	        table5.addCell(cell);
	        
	        Paragraph IP2 = new Paragraph(cl[1], font1);
	        IP2.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(IP2);
	        table5.addCell(cell);
	        
	        Paragraph IP3 = new Paragraph(cl[2], font1);
	        IP3.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(IP3);
	        table5.addCell(cell);
	        
	        Paragraph IP4 = new Paragraph(cl[3], font1);
	        IP4.setAlignment(Element.ALIGN_CENTER);
	        cell = new PdfPCell();
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.addElement(IP4);
	        table5.addCell(cell);
	        
	        chapter2.add(table5);

	        Paragraph line= new Paragraph("\n──────────────────\n ¹ 탐지 로그 : 전체 로그 중 위험 요소가 탐지 된 로그\n ² 위험 로그 : 탐지 로그 중 위험도가 높은 로그 (위험 2단계)\n",menual);
	        chapter2.add(line);
	        
	        Paragraph page2 = new Paragraph("\n-2-",font);
	        page2.setAlignment(Element.ALIGN_CENTER);
	        chapter2.add(page2);
	    ////////////////////////////////////////2페이지 끝
	        
	        Chunk chunk3 = new Chunk("로그 분석 보고서\n\n\n", font);
	        Chapter chapter3 = new Chapter(new Paragraph(chunk3), 1);
	        chapter3.setNumberDepth(0);
	       
	        Paragraph p10 = new Paragraph("      B. 차트 세부 분석\n\n",busubject);
	        chapter3.add(p10);
	        
	        Paragraph p13 = new Paragraph("\n",font);
	        chapter3.add(p13);
	        
	        float[] columnWidths2 = {4,0.3f,4};
	        PdfPTable table6 = new PdfPTable(columnWidths2);
	        table6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table6.setTotalWidth(500);
	        table6.setLockedWidth(true);
	        
	        Image image = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"\\resources\\1.png");
	        Image image2 = Image.getInstance(request.getSession().getServletContext().getRealPath("/")+"\\resources\\2.png");
	        System.out.println(request.getSession().getServletContext().getRealPath("/")+"\\resources\\2.png");
	        
	        image.scaleAbsolute(230, 150);
	        image2.scaleAbsolute(230, 150);
	        
	        cell = new PdfPCell(image,false);
	        cell.setBorderWidth(0);
	        table6.addCell(cell);
	        
	        cell =new PdfPCell();
	        cell.setBorderWidth(0);
	        table6.addCell(cell);
	        
	        cell = new PdfPCell(image2,false);
	        cell.setBorderWidth(0);
	        table6.addCell(cell);
	        
	        chapter3.add(table6);
	        
	        Paragraph p11 = new Paragraph("\n\n\n",busubject);
	        chapter3.add(p11);
	        
	        int attackV[] = new int[6];
	        try{
	        	for(int i=0; i<6; i++){
	        		int j = i+1;
	        		attackV[i] = dao.getRowNum("select * from warningLog where AttackLevel='" + j +"'");
	        	}
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        
	        
	        float[] columnWidths3 = {1,1.5f,0.8f,4,2,1};
	        PdfPTable table7 = new PdfPTable(columnWidths3);
	        table7.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.setTotalWidth(500);
	        table7.setLockedWidth(true);
	        
	        Paragraph level= new Paragraph("예상피해\n레벨",font1);
	        level.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(level);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack = new Paragraph("공격 유형",font1);
	        attack.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel = new Paragraph("위험도",font1);
	        risklevel.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk = new Paragraph("예상 피해",font1);
	        risk.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risk);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger = new Paragraph("위험 단계",font1);
	        danger.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre = new Paragraph("빈도수",font1);
	        fre.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph level1 = new Paragraph("상",font1);
	        level1.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(level1);
	        cell.setRowspan(3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack1 = new Paragraph("SQL Injection", font);
	        attack1.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel1 = new Paragraph("6",font);
	        risklevel1.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk1 = new Paragraph("개인(DB)정보 노출,\n2차 공격 발생 가능성\n",font);
	        risk1.setAlignment(Element.ALIGN_LEFT);
	        cell=new PdfPCell();
	        cell.addElement(risk1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger1 = new Paragraph("위험2단계¹",font);
	        danger1.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre1 = new Paragraph("" + attackV[5], font);//변수
	        fre1.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre1);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack2 = new Paragraph("Directory\nListing",font);
	        attack2.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel2 = new Paragraph("5",font);
	        risklevel2.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk2 = new Paragraph("개인 정보 탈취,\n웹 서버 공격\n",font);
	        risk2.setAlignment(Element.ALIGN_LEFT);
	        cell=new PdfPCell();
	        cell.addElement(risk2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger2 = new Paragraph("위험2단계",font);
	        danger2.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre2 = new Paragraph("" + attackV[4],font);//변수
	        fre2.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack3 = new Paragraph("LFI&RFI",font);
	        attack3.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel3 = new Paragraph("4",font);
	        risklevel3.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk3 = new Paragraph("공격 컴퓨터에\n악성 코드 발생\n",font);
	        risk3.setAlignment(Element.ALIGN_LEFT);
	        cell=new PdfPCell();
	        cell.addElement(risk3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger3 = new Paragraph("위험2단계",font);
	        danger3.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre3 = new Paragraph("" + attackV[3],font);//변수
	        fre3.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph level2 = new Paragraph("중",font1);
	        level2.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(level2);
	        cell.setRowspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack4 = new Paragraph("Scanning",font);
	        attack4.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel4 = new Paragraph("3",font);
	        risklevel4.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk4 = new Paragraph("개인 정보 수집,\n추가 공격 가능성\n",font);
	        risk4.setAlignment(Element.ALIGN_LEFT);
	        cell=new PdfPCell();
	        cell.addElement(risk4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger4 = new Paragraph("위험1단계²",font);
	        danger4.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre4 = new Paragraph("" + attackV[2],font);//변수
	        fre4.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack5 = new Paragraph("Web CGI",font);
	        attack5.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack5);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel5 = new Paragraph("2",font);
	        risklevel5.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel5);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk5 = new Paragraph("웹 쿠키 정보 노출,\n거짓 페이지 생성 후 개인 정보 탈취\n",font);
	        risk5.setAlignment(Element.ALIGN_LEFT);
	        cell=new PdfPCell();
	        cell.addElement(risk5);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger5 = new Paragraph("위험1단계",font);
	        danger5.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger5);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre5 = new Paragraph("" + attackV[1],font);//변수
	        fre5.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre5);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        

	        Paragraph level3 = new Paragraph("하\n",font1);
	        level3.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(level3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph attack6 = new Paragraph("Pattern Block\n",font);
	        attack6.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(attack6);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risklevel6 = new Paragraph("1",font);
	        risklevel6.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(risklevel6);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph risk6 = new Paragraph("시스템 과부하 및 서비스 성능 저하\n\n",font);
	        risk6.setAlignment(Element.ALIGN_LEFT);
	        cell=new PdfPCell();
	        cell.addElement(risk6);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph danger6 = new Paragraph("위험1단계",font);
	        danger6.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(danger6);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        
	        Paragraph fre6 = new Paragraph("" + attackV[0],font);//변수
	        fre6.setAlignment(Element.ALIGN_CENTER);
	        cell=new PdfPCell();
	        cell.addElement(fre6);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table7.addCell(cell);
	        chapter3.add(table7);
	        
	        
	        Paragraph p12 = new Paragraph("\n",font);
	        chapter3.add(p12);
	        
	        PdfPTable table8 = new PdfPTable(1);
	        table8.setTotalWidth(500);
	        table8.setLockedWidth(true);
	        table8.setHorizontalAlignment(Element.ALIGN_CENTER);
	        
	        
	        int top = 0, middle = 0, bottom = 0;
	        
	        bottom = dao.getRowNum("select * from warningLog where AttackLevel='1'");
	        middle = dao.getRowNum("select * from warningLog where AttackLevel='2' or AttackLevel='3'");
	        top = dao.getRowNum("select * from warningLog where AttackLevel='4' or AttackLevel='5' or AttackLevel='6'");
	        
	        int account = top + middle + bottom;
	        double topP = 0, middleP = 0, bottomP = 0;
	        topP = top * 100.0 / account;
	        topP = Math.round(topP * 100d) / 100d;
	        middleP = middle * 100.0 / account;
	        middleP = Math.round(middleP * 100d) / 100d;
	        bottomP = bottom * 100.0 / account;
	        bottomP = Math.round(bottomP * 100d) / 100d;
	        topP = Double.parseDouble(String.format("%.1f", topP));
	        middleP = Double.parseDouble(String.format("%.1f", middleP));
	        bottomP = Double.parseDouble(String.format("%.1f", bottomP));
	        
	        int riskNum1 = bottom + middle;
	        
	        Paragraph p14=new Paragraph("   √  전체 로그 " + checkedLog + "개 중 위험 2단계 " + dangerousLog + "개, 위험 1 단계 " + riskNum1 + "개가 발견되었습니다.\n   √  예상 피해 정도는 상 " + topP + "%, 중 " + middleP + "% 그리고 하 " + bottomP + "% 입니다.\n\n",font);
	        cell=new PdfPCell();
	        cell.addElement(p14);
	        table8.addCell(cell);
	        chapter3.add(table8);
	        
	        Paragraph line2= new Paragraph("\n\n\n\n\n\n\n\n──────────────────\n ¹ 위험 2단계 : 위험 로그 중 위험도가 4 이상인 로그\n ² 위험 1단계 : 위험 로그 중 위험도가 4 미만인 로그\n",menual);
	        chapter3.add(line2);
	        
	        Paragraph page3 = new Paragraph("\n-3-",font);
	        page3.setAlignment(Element.ALIGN_CENTER);
	        chapter3.add(page3);
	        
	        
	        document.add(chapter);
	        document.add(chapter2);
	        document.add(chapter3);
	        document.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return "print";
	}
	
	@RequestMapping(value = "/print", method = RequestMethod.POST)
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	    String strFile = request.getParameter("bin_data");
	    strFile = strFile.replace(' ', '+');
	    System.out.println("\n***"+strFile);
	    byte[] decoded = DatatypeConverter.parseBase64Binary(strFile);
	    try (OutputStream stream = new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"\\resources\\1.png")) {
	        stream.write(decoded);
	    }
	    
	    String strFile2 = request.getParameter("bin_data2");
	    strFile2 = strFile2.replace(' ', '+');
	    System.out.println("\n***"+strFile2);
	    byte[] decoded2 = DatatypeConverter.parseBase64Binary(strFile2);
	    try (OutputStream stream2 = new FileOutputStream(request.getSession().getServletContext().getRealPath("/")+"\\resources\\2.png")) {
	        stream2.write(decoded2);
	    }
	    
	}
	
	@RequestMapping("/table")
	public String gotosome(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "table";
		
		return returnStr;
	}
	
	@RequestMapping("/others")
	public String gotoothers(HttpServletRequest request, HttpServletResponse response) {
		String returnStr = check(request, response);
		if(returnStr.equals("ok")) returnStr = "others";
		
		return returnStr;
	}
	
	@RequestMapping("/logout")
	public String gotologout(Model model,
			@RequestParam(value = "num", required = false) String num) {
		return "logout";
	}
	
	@RequestMapping("/alert")
	public String gotoalert(Model model,
			@RequestParam(value = "num", required = false) String num) {
		return "alert";
	}
	
	public String check(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("id");
		if(id==null || id.equals("")){
			return "home";
		}
		else {
			String dbEmail = id.replace("@", "");
	  	  	String dbN[] = dbEmail.split("\\.");
	  	  	DAO dao = new DAO();
	  	  	int rowNum = dao.getRowNum("select * from warningLog");
	  	  	if(rowNum == 0) return "nhome";
		}
		
		return "ok";
	}
}

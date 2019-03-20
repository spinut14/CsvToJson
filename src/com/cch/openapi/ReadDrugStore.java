package com.cch.openapi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadDrugStore {

	// data.go.kr service key 
	final static String column = "8cQtg0ZoBt%2BGTwAcDoViaOnFUhumlsIdvRk3In%2BqDC1l9pCq0ePPV9WD2JpehssWuS5SCP6LtOLPtvF54R4u5Q%3D%3D";
	final static String url = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		
		// Call REST API
		StringBuilder sb = requestDrugStoreList(1, 20);
		if(null == sb) {
			System.exit(0);
		}else {
			System.out.println("sb.toString()["+sb.toString() + "]");
		}

		// parsing XML Data
		DrugStoreVO drgVO = parseXml(sb.toString());
		if(null == drgVO) {
			System.exit(0);
		}
		
		// make Json Document
		
		
	}
	
	private static DrugStoreVO parseXml(String xml) {
		DrugStoreVO rtnVO = new DrugStoreVO();
		
		try {
			// 1. XML 파싱 객체 생성
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			
			// 2. XML 문자열은 InputStream으로 변환
	        InputStream is = new ByteArrayInputStream(xml.getBytes());
	        
	        // 3. 파싱 시작
			Document document = documentBuilder.parse(is);

			// 4. 최상위 노드
			Element root = document.getDocumentElement();
			
			System.out.println("class name: " + root.getNodeName());
			
			NodeList items = root.getChildNodes();	// 자식노드 목록
			System.out.println("list cnt " + items.getLength());
			for(int i=0; i<items.getLength(); i++) {
				Node body = items.item(i);	
				System.out.println("body : " +body.getNodeName());
				if("body".equals(body.getNodeName())) {
					
					NodeList items2 = body.getChildNodes();
					for(int j=0; j<items2.getLength(); j++) {
						Node items3 = items2.item(j);
						System.out.println("items3 : " +items3.getNodeName());
					}
					
				}else {
					continue;
				}
			}
			 
			
			return rtnVO;
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ie) {
			ie.printStackTrace();
		}
		return null;
	}
	
	private static StringBuilder requestDrugStoreList(int pageNo, int dataCnt){
		StringBuilder rtnStrBd = new StringBuilder();
		StringBuilder urlBuilder = new StringBuilder(url);	

		try {
			urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + "8cQtg0ZoBt%2BGTwAcDoViaOnFUhumlsIdvRk3In%2BqDC1l9pCq0ePPV9WD2JpehssWuS5SCP6LtOLPtvF54R4u5Q%3D%3D"); /*공공데이터포털에서 받은 인증키*/
			urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(Integer.toString(pageNo), "UTF-8")); /*페이지번호*/
			urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(Integer.toString(dataCnt), "UTF-8")); /*한 페이지 결과 수*/
			System.out.println(urlBuilder.toString());
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");

			BufferedReader rd;
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <=300) {
				System.out.println("Response code: " + conn.getResponseCode()); 
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}

			String line;
			while((line = rd.readLine()) != null) {
				rtnStrBd.append(line);
			}

			rd.close();
			conn.disconnect();
			return rtnStrBd;

			}catch(UnsupportedEncodingException ue) {
				ue.printStackTrace();
				return null;
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		
	}

	/**
	 * getter for Column list
	 * @return
	 */
	public static List<String> getColList(){
		List<String> rtn = new ArrayList<String>();
		String[] colArr = column.split(",");
		int len = colArr.length;
		for(int i=0; i<len; i++){
			rtn.add(colArr[i]);
		}
		return rtn;
	}
}

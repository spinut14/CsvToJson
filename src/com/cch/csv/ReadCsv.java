package com.cch.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class ReadCsv {
	
	final static String column = "storeno,storename,branchname,bizarealcd,bizarealname,"
			+ "bizareamcd,bizareamname,bizareascd,bizareasname,stdindustrycd,stdindustryname,"
			+ "citycd,cityname,citycountycd,citycountyname,hjdongcd,hjdongname,bjdongcd,bjdongname,"
			+ "lotnocd,sitedstcd,sitedstname,lotmainno,lotsubno,logaddr,roadnamecd,roadname,buildingmainno,"
			+ "buildingsubno,buildingmngno,buildingname,roadnameaddr,oldzipcd,newzipcd,donginf,"
			+ "floorinf,familyinf,long,lat";
	public static int dataCnt = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// args validate
		// args[1] : 숫자값임
		int loadCnt = -1;
		try {
			loadCnt = Integer.parseInt(args[1]);
		}catch(NumberFormatException ne) {
			System.out.println("Second Argument need to be Integer");
			System.out.println("Program will terminate...");
			System.exit(9);
			// ne.printStackTrace();
		}
		
        BufferedReader br = null;
		// get column name list
		List<String> colList = getColList();
		FileWriter fis = null;
		try {
			fis = new FileWriter("//home//spinut//Documents//elk//ES_Data//ES_data_201812_1_test.json");
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
        
		
        try{
       		// CSV 파일의 디렉토리에서 파일의 목록을 추출한다.
    	  	// CSV 지정은 args로 넘어온다.
        	File dir = new File(args[0]);
        	File[] fileList = dir.listFiles();
        	
        	// File의 갯수만큼 반복 실행
        	for(int i=0; i<fileList.length; i++) {
        		// 디렉토리에 있는 파일 정보를 Assign한다.
        		File file = fileList[i];     
        		if(file.isFile()) {
        			System.out.println("File Name ["+file.getName()+"]");
        			// File을 읽어 드린다.
        			br = Files.newBufferedReader(Paths.get(args[0]+"//"+file.getName()));        			
        			// Json 문서 작성 Method 호출
        			System.out.println("["+file.getName() + "] ---- File writing Start...");
       				writeJsonData(br, colList, fis, loadCnt);
       				System.out.println("["+file.getName() + "] ---- File writing End...");
        		}else{
        			continue;
        		}
        	}
        	
        	fis.flush();
            fis.close();
            br.close();
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }catch(Exception e) {
        	e.printStackTrace();
        }finally {
        	System.out.println("["+dataCnt + "]Data loaded ");
        	System.out.println("Program is terminated...");
        }
	}

	private static void writeJsonData(BufferedReader br, List<String> colList, FileWriter fis, int loadCnt) throws Exception {
		String line = "";
		int j = 0;
		JSONObject json = null;
		while((line = br.readLine()) != null){
        	// first line은 컬럼명이므로 skip
        	if(j==0) {
        		j++;
        		continue;
        	}
        	json = new JSONObject();
        	// data를 읽어와 쉼표를 기준으로 split한다. 특수문자를 제외해서 split 처리
            String array[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            int col = array.length;
            String data = "";
            for(int i=0; i<col; i++) {
            	data = array[i];
            	data = data.replaceAll("\"", "");
            	// json data 입력
            	json.put(colList.get(i), data);            	
            }
            dataCnt++;
            fis.write(json.toString());
            fis.write("\n");
            // Data건수 지정 (-1이면 모두 읽기)
            if(loadCnt != -1) {
	            if(j>=loadCnt) {
	            	break;
	            }
            }
            j++;
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

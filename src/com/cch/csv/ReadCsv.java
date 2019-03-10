package com.cch.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// get column name list
		List<String> colList = getColList();

        BufferedReader br = null;
        
        try{
        		// 읽을 파일의 위치 및 파일명
            br = Files.newBufferedReader(Paths.get("//home//spinut//Documents//elk//ES_Data//ES_data_201812_1.csv"));
            Charset.forName("UTF-8");
            String line = "";
            JSONObject json = null;
            // json 파일로 저장할 파일의 위치 및 파일명
            FileWriter fis = new FileWriter("//home//spinut//Documents//elk//ES_Data//ES_data_201812_1.json");
            int j=0;
            System.out.println("Data Reading...");
            while((line = br.readLine()) != null){
            	// first line 무시(컬럼명)
            	if(j==0) {
            		j++;
            		continue;
            	}
            	json = new JSONObject();
                //CSV 1행을 저장하는 리스트
                //List<String> tmpList = new ArrayList<String>();
            			// 특수문자를 제외해서 split 처리
                String array[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                int col = array.length;
                String data = "";
                for(int i=0; i<col; i++) {
                	data = array[i];
                	data = data.replaceAll("\"", "");
                	// json data 입력
                	json.put(colList.get(i), data);
                }
                fis.write(json.toString());
                fis.write("\n");
                // Data건수가 많아 건수 지정함
                if(j>=150000) {
                	break;
                }
                j++;
            }
            
            System.out.println("Process Complete.");
            System.out.println("Data Count : [" + j + "]");
            fis.flush();
            fis.close();
            
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
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

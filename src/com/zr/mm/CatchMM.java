package com.zr.mm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CatchMM {
//	public static void main(String[] args) {
//			String first_url = "https://www.duodia.com/daxuexiaohua/";
//			try {
//				Document doc = Jsoup.connect(first_url).get();
//			    Elements  els = doc.select("#main > div.row > article > div > a.thumbnail-container > img");
//			    for (Element el : els) {
//					String  mm_Name = el.attr("alt");
//					String  mm_area = mm_Name.substring(0,2);
//					String  mm_ImgUrl = el.attr("src");
//					//saveToFile(mm_ImgUrl, filePath);
//					String file_path = "D:\\mm\\"+mm_area;
//					String img_path = "D:\\mm\\"+mm_area+"\\"+mm_Name+".jpg";
//					saveToFile(mm_ImgUrl, file_path,img_path);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	}
	public static void main(String[] args) {
				getUrl("https://www.duodia.com/daxuexiaohua/");
	}
	
	
	public static void   getUrl(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements  els = doc.select("#main > div.area-pagination.archive-pagination > div > a[title=下一页]");
			if(els.size()==1) {
				//拿到当页的数据,获取名字和路径以及对应的url  存入对应的盘符中
				Elements  imgels = doc.select("#main > div.row > article > div > a.thumbnail-container > img");
			    for (Element el : imgels) {
					String  mm_Name = el.attr("alt");
					String  mm_area = mm_Name.substring(0,2);
					String  mm_ImgUrl = el.attr("src");
					//saveToFile(mm_ImgUrl, filePath);
					String file_path = "D:\\mm\\"+mm_area;
					String img_path = "D:\\mm\\"+mm_area+"\\"+mm_Name+".jpg";
					saveToFile(mm_ImgUrl, file_path,img_path);
				}
				//获取下一页
				String newPath = els.get(0).attr("href");
				System.out.println("已经爬取====>"+newPath+"完毕");
				getUrl(newPath);

			}else {
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public static void saveToFile(String destUrl,String filePath, String imgPath) {  
		FileOutputStream fos = null;  
		BufferedInputStream bis = null;  
		HttpURLConnection httpUrl = null;  
		URL url = null;  
		int BUFFER_SIZE = 1024;  
		byte[] buf = new byte[BUFFER_SIZE];  
		int size = 0;  
		try {  
			url = new URL(destUrl);  
			httpUrl = (HttpURLConnection) url.openConnection();  
			httpUrl.connect();  
			bis = new BufferedInputStream(httpUrl.getInputStream()); 
			File file= new File(filePath); 
			if(file.exists()) {
				fos = new FileOutputStream(imgPath);  
				while ((size = bis.read(buf)) != -1) {   
					fos.write(buf, 0, size);  
				}  
				fos.flush(); 
			}else {
				file.mkdirs();
				fos = new FileOutputStream(imgPath);  
				while ((size = bis.read(buf)) != -1) {   
					fos.write(buf, 0, size);  
				}  
				fos.flush();  
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) { 
			e.printStackTrace();
		} finally {  
			try {  
				fos.close();  
				bis.close();  
				httpUrl.disconnect();  
			} catch (IOException e) {  
			} catch (NullPointerException e) {  
			}  
		}  
	} 

}

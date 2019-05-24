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
	public static void main(String[] args) {
		         try {
					int pageCount = getPageCount("https://www.duodia.com/daxuexiaohua/");
					int   count = 1;
					//根据页数总数做递归的终止条件
					getUrl("https://www.duodia.com/daxuexiaohua/", pageCount, count);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	
	/**
	 * 根据路径页面总数，计数 递归获取图片信息
	 * @param url  路径
	 * @param pageCount  页面总数
	 * @param count  起始计数
	 */
	public static void   getUrl(String url,int pageCount,int count) {
		try {
			if(count<=pageCount){
			Document doc = Jsoup.connect(url).get();
			Elements  els = doc.select("#main > div.area-pagination.archive-pagination > div > a[title=下一页]");
			
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
			    System.out.println("已经爬取第"+count+"页");
			    count++;
				String newPath = "https://www.duodia.com/daxuexiaohua/list_"+count+".html";
				getUrl(newPath,pageCount,count);

			}else {
				System.out.println("数据提取完毕");
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取页面的总数
	 * @param path  根据页面的规则，获取总共页数
 	 * @return
	 * @throws IOException
	 */
	public static int getPageCount(String path) throws IOException {
		  String url = "https://www.duodia.com/daxuexiaohua/";
		  String  counturlsel = "#pagination-8510 > option:nth-child(11)";
		  Document  doc = Jsoup.connect("https://www.duodia.com/daxuexiaohua/").get();
		  String fpage =doc.select("#pagination-8510 > option:nth-child(11)").get(0).text();
		  String str=fpage.trim();
		  String str2="";
		  if(str != null && !"".equals(str)){
		  for(int i=0;i<str.length();i++){
		  if(str.charAt(i)>=48 && str.charAt(i)<=57){
		  str2+=str.charAt(i);
		  }
		  }

		  }
		return Integer.parseInt(str2);
	}
	
	
	/**
	 * 存储文件
	 * @param destUrl  原位置
	 * @param filePath 区域分类
	 * @param imgPath  目标位置
	 */
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

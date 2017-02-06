package com.wmzz.imageSpider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class ImageSpider {
	
	private static final String URL = "http://image.baidu.com/";

	public static void main( String[] args ) {
		// TODO Auto-generated method stub
		try {
			ImageGet();
        }
        catch( Exception e ) {
        	e.printStackTrace();
        }

	}
	
	/**
	 * 爬取图片的链接
	 */
	@SuppressWarnings( "resource" )
    public static void ImageGet(){
		try{
			//关闭日志打印，否则会打印出很多东西，这些东西并不影响程序的运行
			java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
		    //java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
			//模拟浏览器
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled( false );
			webClient.getOptions().setJavaScriptEnabled( true );
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			//设置Ajax异步处理控制器即启用Ajax支持
			webClient.setAjaxController( new NicelyResynchronizingAjaxController() );
			HtmlPage page = webClient.getPage( URL );
			HtmlTextInput textInput = page.getElementByName( "word" );
			//搜索框的输入内容
			textInput.setValueAttribute( "张学友" );
			//模拟点击
			HtmlSubmitInput submitInput = (HtmlSubmitInput)page.getByXPath( "//*[@id=\"homeSearchForm\"]/span[2]/input" ).get( 0 );
			HtmlPage imagePage = submitInput.click();
			//获取每一页
			List<HtmlListItem> li  = (List<HtmlListItem>)imagePage.getByXPath( "//*[@id=\"imgid\"]/div/ul/li" );
			System.out.println(li.size());
			for(int i=0;i < li.size();i ++){
				String url = li.get( i ).getAttribute( "data-objurl" ).toString();
				System.out.println(url);
				String[] fileName = url.trim().split( "\\." );
				saveFile(li.get( i ).getAttribute( "data-objurl" ).toString(),"D:/1",i+"."+fileName[fileName.length-1]);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param url
	 * @param path
	 * 保存图片
	 */
	public static void saveFile(String url,String path,String fileName){
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try{
			File file = new File( path );
			if(!file.exists()){
				file.mkdir();
			}
			URLConnection connection = new URL(url).openConnection();
			inputStream = new BufferedInputStream( connection.getInputStream() );
			outputStream = new BufferedOutputStream( new FileOutputStream( path + "/" + fileName ) );
			int num = 0;
			while((num = inputStream.read()) != -1){
				outputStream.write( num );
			}
			outputStream.flush();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
	            outputStream.close();
	            inputStream.close();
            }
            catch( Exception e2 ) {
	            e2.printStackTrace();
            }
		}
		
	}
	
	
	
	/**
	 * get请求
	 */
	/*public static void requestGet(){
		HttpGet get = null;
		try {
	        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	        get = new HttpGet( URL );*/
	        //get.setHeader( "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8" );
	        //get.setHeader( "Accept-Encoding","gzip, deflate, sdch" );
	       // get.setHeader( "Accept-Language","zh-CN,zh;q=0.8" );
	        /*CloseableHttpResponse response = httpClient.execute( get );
	        //如果是200，则解释网页
	        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        	HttpEntity entity = response.getEntity();
	        	String jsonStr = EntityUtils.toString( entity, "utf-8" );
	        	System.out.println(jsonStr);
	        	Map<Integer, Object> map = parseHtml( jsonStr );
	        }
	        
        }
        catch( Exception e ) {
	        // TODO: handle exception
        }
	}*/
	
	/*public static Map<Integer, Object> parseHtml(String jsonStr) throws Exception{
		Map<Integer,Object> map = new HashMap<Integer, Object>();
		try{
			Document document = Jsoup.parse( jsonStr );
			Elements li = document.getElementsByClass( "imgitem" );
			//System.out.println(li.toString());
			
		}
		catch(Exception e){
			throw e;
		}
		return map;
		
	}*/

}

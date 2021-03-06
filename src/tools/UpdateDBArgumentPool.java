package tools;

import java.util.ArrayList;
import java.util.List;

import dao.ArgumentDao;
import dao.jdbc.JdbcArgumentDao;

import entity.Argument;

 /*
  * 获取所指向url的输出流(指所有内容) 
  * @urlString url路径  如:http://www.baidu.com
          *返回的String 则为html代码
  * */
 
public class UpdateDBArgumentPool {
	 static List<Argument> argumentPool= new ArrayList<Argument>();


	public void setArgumentPool(List<Argument> argumentPool) {
		this.argumentPool = argumentPool;
	}
	public static   String getHtml(String urlString) {  
		  try {
		   StringBuffer html = new StringBuffer();  
		   java.net.URL url = new java.net.URL(urlString);  //根据 String 表示形式创建 URL 对象。
		   java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();// 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
		   java.io.InputStreamReader isr = new java.io.InputStreamReader(conn.getInputStream());//返回从此打开的连接读取的输入流。
		   java.io.BufferedReader br = new java.io.BufferedReader(isr);//创建一个使用默认大小输入缓冲区的缓冲字符输入流。
		   
		   String temp;
	
		   int index=0;
		   
		   Argument argument= new Argument();
		   while ((temp = br.readLine()) != null) {  //按行读取输出流
			  
			   if (temp.indexOf("GRE")!=-1||temp.indexOf("argument statement")!=-1) continue;
		    if(!temp.trim().equals("")){
		     html.append(temp).append("\n");  //读完每行后换行
		     if(temp.indexOf("<p>")==0){
		    	  
		    	 argument.setPassage(SentenceTrim(argument.getPassage()+temp));
		    	 
		    //	 System.out.println(index+"   "+argument);
		     }else if (temp.indexOf("<p class=\"\">")==0){	  
		    	 argument.setQuestion(SentenceTrim(temp));
		    	
		    	
		    	 argumentPool.add(argument);
		    	 System.out.println(argumentPool.size()+"   :    "+argumentPool.get(index));
		    	 index++;
		    	 argument= new Argument();
		     }
		    }
		   }
		    br.close();   //关闭
		    isr.close();  //关闭
		   return html.toString();   //返回此序列中数据的字符串表示形式。
		  } catch (Exception e) {
		   e.printStackTrace();
		   return null;
		  }
		 }
	
	 private static void updateDBArgumentPool(){
		 ArgumentDao dao=new JdbcArgumentDao();
		 dao.UpdateDBArgumentPool(argumentPool);
	 }
	 

	
	public static String  SentenceTrim(String sentence){
		String temp[];
		
		if(sentence.indexOf("null")>-1){
			temp=sentence.split("null");
			sentence="";
			for(int i=0;i<temp.length;i++){
				sentence=sentence+temp[i];
			}
		}
	
		if(sentence.indexOf("</p>")>-1){
			temp=sentence.split("</p>");
			sentence="";
			for(int i=0;i<temp.length;i++){
				sentence=sentence+temp[i];
			}
		}
		if(sentence.indexOf("<p>")>-1){
			temp=sentence.split("<p>");
			sentence="";
			for(int i=0;i<temp.length;i++){
				sentence=sentence+temp[i];
			}
		}
		
		if(sentence.indexOf("<p class=\"\">")>-1){
			temp=sentence.split("<p class=\"\">");
			sentence="";
			for(int i=0;i<temp.length;i++){
				sentence=sentence+temp[i];
			}
		}
		
		
		return sentence;
		
	}
	public static void main(String[] args) {
	try{
		getHtml("http://www.ets.org/gre/revised_general/prepare/analytical_writing/argument/pool");
		 updateDBArgumentPool();
		 System.out.println("done" +argumentPool.size());
	}catch(Exception e){
		  e.printStackTrace();
	}
	}
}

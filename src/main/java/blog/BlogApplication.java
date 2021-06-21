package blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.*;

@SpringBootApplication @MapperScan("blog.mapper")
public class BlogApplication implements WebMvcConfigurer
{
	public static void main(String[] args) throws IOException {

//		String commandStr = "g++ C:\\Users\\dell\\Desktop\\hello.c";
//		BufferedReader br;
//		BufferedWriter bw;
//		Process p1 = Runtime.getRuntime().exec(commandStr);
//		Process p = Runtime.getRuntime().exec("a.exe");
//
//		bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//		bw.write("1 2 3");
//		bw.close();
//		br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		String line = null;
//		StringBuilder sb = new StringBuilder();
//		while ((line = br.readLine()) != null) {
//			sb.append(line + "\n");
//		}
//
//		System.out.println(sb.toString());
		SpringApplication.run(BlogApplication.class, args);
	}
}
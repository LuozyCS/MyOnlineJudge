package judge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication @MapperScan("judge.mapper")
public class JudgeApplication
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
		SpringApplication.run(JudgeApplication.class, args);
	}
}
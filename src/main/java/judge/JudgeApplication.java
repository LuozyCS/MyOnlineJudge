package judge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication @MapperScan("judge.mapper")
public class JudgeApplication
{
	public static void main(String[] args) throws IOException {
//		//创建cpp文件的地址，到myoj下，src外面
////		File file = new File("lzy.cpp");
////		file.createNewFile();
//
////		编译的地址
//		//  .\\能在src外面  即同cpp同一位置生成exe文件
//		//  .\\a.exe也是和cpp在同一目录
//		String commandStr = "g++ .\\hello.c";
//		BufferedReader br;
//		BufferedWriter bw;
//		Process p1 = Runtime.getRuntime().exec(commandStr);
//		Process p = Runtime.getRuntime().exec(".\\a.exe");
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
//        System.err.println(sb.toString());
		SpringApplication.run(JudgeApplication.class, args);
	}
}
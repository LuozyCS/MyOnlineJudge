package judge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication @MapperScan("judge.mapper")
public class JudgeApplication
{
	public static void main(String[] args) throws IOException {
//		String commandStr = "g++ -o hello.exe hello.c ";
//
//				//> .\\\\debug.txt 2>&1"
//
//			Process p = Runtime.getRuntime().exec(commandStr);


		SpringApplication.run(JudgeApplication.class, args);
	}
}
package blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication @MapperScan("blog.mapper")
public class BlogApplication implements WebMvcConfigurer
{
	public static void main(String[] args)
	{
		SpringApplication.run(BlogApplication.class, args);
	}
}
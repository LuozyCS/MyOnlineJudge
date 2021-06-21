//package blog;
//
//import com.microsoft.sqlserver.jdbc.SQLServerException;
//import lombok.Data;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Data
//class DatabaseUser
//{
//	private String name, password;
//}
//
//@Component
//@SpringBootTest
//@ConfigurationProperties(prefix = "spring.datasource")
//class BlogApplicationTests
//{
////	@Autowired
////	private DataSourceProperties dataSourceProperties;
////
//////	@Resource(name = "spring.datasource.users")
////	@Qualifier("users")
////	private DatabaseUser[] databaseUsers;
//////	@ConfigurationProperties(prefix = "spring.datasource.users"
////	void setUsers(DatabaseUser[] databaseUsers)
////	{
////		System.err.println("set users");
////		this.databaseUsers = databaseUsers;
////	}
////
////	@Test
////	void contextLoads() throws SQLException
////	{
////		Connection connection = null;
//////		for (int i = users.length - 1, lower = i - 1;i > lower;--i)
//////		{
//////			dataSourceProperties.setUsername(users[i].getName());
//////			dataSourceProperties.setPassword(users[i].getPassword());
//////			try
//////			{
//////				connection = dataSourceProperties.initializeDataSourceBuilder().build().getConnection();
//////			} catch (SQLServerException exception)
//////			{
//////				--lower;
//////			}
//////			System.out.println(connection);
//////		}
////		for (DatabaseUser user : databaseUsers)
////		{
////			dataSourceProperties.setUsername(user.getName());
////			dataSourceProperties.setPassword(user.getPassword());
////			boolean success = true;
////			try
////			{
////				connection = dataSourceProperties.initializeDataSourceBuilder().build().getConnection();
////			} catch (SQLServerException exception)
////			{
////				success = false;
////			}
////			if (success)
////				break;
////		}
////		assert connection != null;
////		PreparedStatement statement = connection.prepareStatement("select * from student");
////		ResultSet resultSet = statement.executeQuery();
////		while (resultSet.next())
////			System.out.println(resultSet.getNString("name").trim());
////		statement.close();
////		connection.close();
////	}
//
//    @Autowired private JdbcTemplate jdbcTemplate;
//	@Test
//	void contextLoads() throws SQLException
//	{
//	    System.out.println(jdbcTemplate.queryForList("select * from article"));
//	}
//}

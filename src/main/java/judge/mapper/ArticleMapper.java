package judge.mapper;

import judge.dataTransferObject.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface ArticleMapper
{
    List<Article> getAllExceptContent();
    String getContent(final int articleId);
    String getTitle(final int articleId);
    List<Article> getAllExceptContent_ByPublisherId(final int publisherId);
    void deleteArticle(final int articleId);
    int getPublisher(final int articleId);
    void insertArticle(Article newArticle);
    void updateArticle(Article modifyArticle);
}
//@MappedJdbcTypes({JdbcType.VARCHAR}) @MappedTypes({Date.class})
//class MyDateTypeHandler extends BaseTypeHandler
//{
//    @Override
//    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException
//    {
//
//    }
//
//    @Override
//    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException
//    {
//        return null;
//    }
//
//    @Override
//    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException
//    {
//        return null;
//    }
//
//    @Override
//    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException
//    {
//        return null;
//    }
//}
package judge.mapper;

import judge.dataTransferObject.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface CommentMapper
{
    List<Comment> getComments(@Param("article_id")final int articleId, @Param("origin_id")final int originId);
    default List<Comment> getDirectComments(int articleId)
    {
        return getComments(articleId, -1);
    }
    int publishComment(@Param("article_id")final int articleId, @Param("publisher_id")final int publisherId
            , @Param("origin_id")final int originId, @Param("parent_id")final int parentId
            , @Param("content")final String content);
    int getOriginId(@Param("article_id")final int articleId, @Param("id")final int id);
    void clearComment(@Param("article_id")final int articleId, @Param("id")final int id);
}

package judge.mapper;

import judge.dataTransferObject.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface CommentMapper
{
    List<Comment> getComments(@Param("problem_id")final int problemId, @Param("origin_id")final int originId);
    default List<Comment> getDirectComments(int problemId)
    {
        return getComments(problemId, -1);
    }
    int publishComment(@Param("problem_id")final int problemId, @Param("publisher_id")final int publisherId
            , @Param("origin_id")final int originId, @Param("parent_id")final int parentId
            , @Param("content")final String content);
    int getOriginId(@Param("problem_id")final int problemId, @Param("id")final int id);
    void clearComment(@Param("problem_id")final int problemId, @Param("id")final int id);
}

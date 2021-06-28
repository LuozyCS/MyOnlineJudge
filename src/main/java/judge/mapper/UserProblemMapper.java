package judge.mapper;

import judge.dataTransferObject.UserProblem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface UserProblemMapper {
    //通过id获得某个用户的所有提交历史
    List<UserProblem> getAllByUserId(final int user_id);
    //通过题号获得某个题的所有用户的提交历史
    List<UserProblem> getAllByProblemId(final int problemId);
    //插入一个新的提交历史
    void insertUserProblem(@Param("user_id")final int user_id,@Param("problem_id")final int problem_id,
                           @Param("pass")final int pass,
                           @Param("pass_time")final int pass_time,
                           @Param("content")final String content);


    //根据用户id，问题id，当前时间获得提交内容
    String getSubmitContent(@Param("user_id")final int user_id, @Param("problem_id")final int problem_id,
                            @Param("when")final Timestamp when);

}

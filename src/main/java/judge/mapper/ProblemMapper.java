package judge.mapper;

import judge.dataTransferObject.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface ProblemMapper
{
    //获取所有的problem
    List<Problem> getAllExceptContent();
    String getContent(final int problemId);
    String getTitle(final int problemId);
    List<Problem> getAllExceptContent_ByPublisherId(final int publisherId);
    int getDifficulty(final int problemId);

    void deleteProblem(final int problemId);
    int getPublisher(final int problemId);

    void insertProblem(Problem newProblem);
    void updateProblem(Problem modifyProblem);
    //获取某个发布者的草稿箱中的内容
//    List<Problem> getDraft(@Param("publisher_id")final int publisher_id,
//                  @Param("state")final int state);
}

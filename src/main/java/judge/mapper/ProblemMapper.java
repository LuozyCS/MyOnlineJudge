package judge.mapper;

import judge.dataTransferObject.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface ProblemMapper
{
    List<Problem> getAllExceptContent();
    String getContent(final int problemId);
    String getTitle(final int problemId);
    List<Problem> getAllExceptContent_ByPublisherId(final int publisherId);
    void deleteProblem(final int problemId);
    int getPublisher(final int problemId);
    void insertProblem(Problem newProblem);
    void updateProblem(Problem modifyProblem);
}

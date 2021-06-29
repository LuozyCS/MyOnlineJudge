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

    //原来没有这个题目
    /*
    *
    * */
    void insertProblem(Problem newProblem);

    //更新
    /*
    *
    * */
    void updateProblem(Problem modifyProblem);


    //获得某个题目的状态
    int getState(int problemId);

//    //获得某个具体状态对应的所有题目
//    List<Problem> getProblemsByState(int state);
//
//    //获取某个发布者的草稿箱中的内容
//    List<Problem> getProblemsByStateAndPublisherId(@Param("publisher_id")final int publisher_id,@Param("state")final int state);

    //废弃
    void abandonProblem(int problemId);




}

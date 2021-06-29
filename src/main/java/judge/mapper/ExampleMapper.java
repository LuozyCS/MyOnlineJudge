package judge.mapper;

import judge.dataTransferObject.Example;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Mapper
@Repository
public interface ExampleMapper {
    //通过problemId得到所有的样例
    List<Example> getAllById(final int problemId);
    //通过题号和样例号确定一个输入数据
    Example getInputByIdAndExampleId(@Param("problemId")final int problemId, @Param("exampleId")final int exampleId);
    Example getOutputByIdAndExampleId(@Param("problemId")final int problemId,@Param("exampleId")final int exampleId);
    //获取某个题号的第一个输入/输出数据
    Example getFirstInputById(final int problemId);
    Example getFirstOutputById(final int problemId);
    //删除样例
    void deleteExample(@Param("problemId")final int problemId);
    //增加样例
    void insertExample(@Param("input")final Example input);
    //更新样例
    void updateExample(@Param("example")final Example example);
}

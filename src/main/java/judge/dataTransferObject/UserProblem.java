package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserProblem {

    private int user_id;//主键
    private int problem_id;//主键
    private int pass;
    private int pass_time;//主键

    private Timestamp when;

    //hashcode()待定
    @Override public int hashCode()
    {
        return user_id | problem_id | pass_time;
    }
    @Override public boolean equals(Object object)
    {
        return object instanceof UserProblem //同时挡住null
                && user_id == ((UserProblem)object).user_id
                && problem_id == ((UserProblem)object).problem_id
                && pass_time == ((UserProblem)object).pass_time;
    }

//这个数据库里的数据都是系统写入的，不需要判断异常
//    public String getErrorMessage(){
//        StringBuffer result = new StringBuffer();
//        if (content == null || content.isEmpty())
//            result.append("评论不能为空\n");
//        else {
//            if (content.length() > 500)
//                result.append("评论不能超过500个字符\n");
//        }
//        return result.length() == 0 ? null : result.substring(0,result.length() - 1);
//    }

}

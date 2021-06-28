package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data public class Problem
{
    private int id;
    private String title, content;
    private Timestamp publishTime;
    private User publisher;
    private int difficulty;
    @Override public int hashCode()
    {
        return id;
    }
    @Override public boolean equals(Object object)
    {
        return object instanceof Problem //同时挡住null; Article null?
                && id == ((Problem)object).id;
    }
    public String getErrorMessages(){
        StringBuffer result = new StringBuffer();

        if (title != null ){
            if (title.length() > 300) {
                result.append("题目标题不能超过300个字符\n");
            }
        }

        if (content != null ){
            if (content.length() > 10000) {
                result.append("题目内容不能超过10000个字符\n");
            }
        }

        return result.length() == 0 ? null : result.substring(0,result.length() - 1);
    }

}

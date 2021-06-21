package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data public class Comment
{
    private Problem problem;
    private int id;
    private User publisher;
    private String content;
    private Timestamp publishTime;
    private Comment parent;
    @Override public int hashCode()
    {
        return id;
    }
    @Override public boolean equals(Object object)
    {
        return object instanceof Comment //同时挡住null
                && id == ((Comment)object).id
                && problem.getId() == ((Comment)object).problem.getId();
    }

    public boolean equalsInTheSameProblem(Comment comment)
    {
        return comment != null && id == comment.id;
    }

    public String getErrorMessage(){
        StringBuffer result = new StringBuffer();
        if (content == null || content.isEmpty())
            result.append("评论不能为空\n");
        else {
            if (content.length() > 500)
                result.append("评论不能超过500个字符\n");
        }
        return result.length() == 0 ? null : result.substring(0,result.length() - 1);
    }
}

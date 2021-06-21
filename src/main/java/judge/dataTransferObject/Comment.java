package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data public class Comment
{
    private Article article;
    private int id;
    private User publisher;
    private String content;
    private Timestamp publishTime;
    private Comment parent;
    @Override public int hashCode()
    {
        return id;//(article.getId() << 24) | (id & 0x00ffffff);
    }
    @Override public boolean equals(Object object)
    {
        return object instanceof Comment //同时挡住null
                && id == ((Comment)object).id
                && article.getId() == ((Comment)object).article.getId();
    }

    public boolean equalsInTheSameArticle(Comment comment)
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

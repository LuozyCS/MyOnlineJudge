package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Example {
    private int id;
    private int IO;
    private int example_id;
    private String content;

    @Override
    public void setAll(int id,int IO,int example_id,String content){
        this.id=id;
        this.IO=IO;
        this.example_id=example_id;
        this.content=content;
        return;
    }
//    @Override public int hashCode()
//    {
//        return id;
//    }

//    @Override public boolean equals(Object object)
//    {
//        return object instanceof Example
//                && id == ((Example)object).id && IO == ((Example)object).IO && content == ((Example)object).content;
//    }

//    public String getErrorMessages(){
//        StringBuffer result = new StringBuffer();
//
//        if (content != null ){
//            if (content.length() > 4000) {
//                result.append("题目内容不能超过4000个字符\n");
//            }
//        }
//
//        if (content == null ){
//                result.append("题目内容不能为空\n");
//        }
//
//        return result.length() == 0 ? null : result.substring(0,result.length() - 1);
//    }

}

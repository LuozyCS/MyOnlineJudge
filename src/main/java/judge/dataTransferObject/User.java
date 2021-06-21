package judge.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;//不会提供无参构造！
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data
public class User
{
    private int id;
//    @Size(min = 1,max = 2, message = "用户名不能为空，也不能超过32个英文字符")
//    @NotBlank
//    @NotEmpty
//    @NotNull
    private String username;
    private String email, pwd;
    @Override public int hashCode()
    {
        return id;
    }
    @Override public boolean equals(Object object)
    {
        return object instanceof User //同时挡住null
                && id == ((User)object).id;
    }
    public String getErrorMessages()
    {
        StringBuffer result = new StringBuffer();
        if (username == null || username.isEmpty())
            result.append("用户名不能为空\n");
        else {
            if (username.charAt(username.length() - 1) <= ' ')
                result.append("用户名不能以空白字符结尾\n");
            if (username.length() > 32)
                result.append("用户名不能超过32个字符\n");
        }

        if (email != null && !email.isEmpty()) {
            if (email.length() > 50)
                result.append("邮箱长度超过50个字符");
            if (!(email.matches("\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}")))
                result.append("邮箱不符合规范\n");
        }
        else
            result.append("邮箱不能为空\n");

        if (pwd == null || pwd.isEmpty())
            result.append("密码不能为空\n");
        else {
            if (pwd.charAt(pwd.length() - 1) <= ' ')
                result.append("密码不能以空白字符结尾\n");
            if (pwd.length() > 20)
                result.append("密码不能超过20个字符\n");
        }
        return result.length() == 0 ? null : result.substring(0,result.length() - 1);
    }
}

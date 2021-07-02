package judge.dataTransferObject;

import judge.mapper.UserProblemMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;

@Component
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

    private static UserProblemMapper userProblemMapper;
    @Autowired
    void setUserProblemMapper(UserProblemMapper userProblemMapper) {
        UserProblem.userProblemMapper = userProblemMapper;
    }

    public ArrayList<Integer> notPassAverageSubmit(int problemId){
        /*
            每道题未通过的人平均提交次数
        */
       ArrayList<Integer> tempAll= userProblemMapper.whoNotPass(problemId);//取出的是全部写了这题的人
       ArrayList<Integer> tempPass=userProblemMapper.whoPass(problemId);
       for(int i=0;i<tempAll.size();i++){
           for(int eachPass:tempPass){
               if(tempAll.get(i)==eachPass){
                   tempAll.remove(i);
                   break;
               }
           }
       }
        if(tempAll.size()==0)return null;
        ArrayList<Integer> notPassUsers=tempAll;

        int notPassSubmit=0;
        for(int xid:notPassUsers){
            notPassSubmit+=userProblemMapper.submitTimes(problemId,xid);
        }
        int sumNotPassSubmit=notPassSubmit;
        int sumNotPassUserCount=notPassUsers.size();
        ArrayList<Integer> temp=new ArrayList<>();
        temp.add(sumNotPassSubmit);
        temp.add(sumNotPassUserCount);
        temp.add(notPassSubmit/notPassUsers.size());
        return temp;
    }

    public ArrayList<Integer> passAverageSubmit(int problemId){
         /*
            每道题通过的人平均提交次数，只截至到第一次AC时的提交次数
         */
        if(userProblemMapper.whoPass(problemId).size()==0)return null;
        ArrayList<Integer> passUsers=userProblemMapper.whoPass(problemId);
        int passSubmit=0;
        for (int xid:passUsers){
            ArrayList<UserProblem> temp= (ArrayList<UserProblem>) userProblemMapper.submitRecordsWhenPass(problemId,xid);
            int i;
            for(i=0;i<temp.size();i++){
                if (temp.get(i).getPass()==0)break;//截止到第一次AC时所有的提交次数为i+1
            }
            passSubmit=passSubmit+i+1;
        }
        ArrayList<Integer> temp=new ArrayList<>();
        int sumPassSubmit=passSubmit;
        int sumPassUserCount=passUsers.size();
        temp.add(sumPassSubmit);
        temp.add(sumPassUserCount);
        temp.add(passSubmit/passUsers.size());
        return temp;
    }


}

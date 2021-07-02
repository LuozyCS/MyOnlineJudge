package judge.dataTransferObject;

import lombok.Data;

@Data
public class AllProblemList {
    private Problem problem;
    private String passRate;
    private int passUser;
    private int doUser;
}

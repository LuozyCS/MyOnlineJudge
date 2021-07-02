package judge.dataTransferObject;

import lombok.Data;

@Data
public class ProblemRank {
    private int problemId;
    private String title;
    private int count;
    private String publisher;
}

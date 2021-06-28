package judge.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryList {
    private UserProblem userProblem;
    private String problemTitle;
    private int difficulty;
}

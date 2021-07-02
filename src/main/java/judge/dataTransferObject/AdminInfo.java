package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdminInfo {
    private int id;
    private String title;
    private int difficulty;
    private int userCount;
    private double passRate;
    private int passCount;
}

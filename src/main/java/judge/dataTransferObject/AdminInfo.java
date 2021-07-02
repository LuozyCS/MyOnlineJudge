package judge.dataTransferObject;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdminInfo {
    private int id;
    private String title;
    private String difficulty;
    private int userCount;
    private String passRate;
    private int passCount;
    private Timestamp publishTime;
    private int state;
    private String passAverageSubmit;
    private String notPassAverageSubmit;
}

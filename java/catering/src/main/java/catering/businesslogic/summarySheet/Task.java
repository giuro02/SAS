package catering.businesslogic.summarySheet;

import catering.businesslogic.recipe.KitchenJob;
import catering.businesslogic.shift.*;
import catering.businesslogic.user.User;
import catering.persistence.BatchUpdateHandler;
import catering.persistence.PersistenceManager;
import catering.persistence.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {

    private int id;
    private int estimatedTime;
    private String portions;
    private String readyPortions;
    private KitchenJob job;
    private User cook;
    private KitchenShift shift;

    private Task() {

    }

    public Task(KitchenJob kj) {
        this.id = 0;
        this.job = kj;
        this.readyPortions = null;
        this.portions = null;
        this.cook=null;
        this.estimatedTime=-1;
        this.shift=null;
    }

    public int getId() {
        return id;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public String getPortions() {
        return portions;
    }

    public String getReadyPortions() {
        return readyPortions;
    }

    public Shift getShift() {
        return shift;
    }

    public KitchenJob getJob() {
        return job;
    }

    public User getCook() {
        return cook;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public void setReadyPortions(String readyPortions) {
        this.readyPortions = readyPortions;
    }

    public void setCook(User cook) {
        this.cook = cook;
    }

    public void setShift(KitchenShift shift) {
        this.shift= shift;
    }

    public void assignShift(/*DTOShiftAssignment*/ Shift objShiftAssignment) {
        if(objShiftAssignment.getCook()!=null)
            this.setCook(objShiftAssignment.getCook());

        if(objShiftAssignment.getShift()!=null) {

            if(this.shift!=null)
                this.shift.removeTask(this);

            objShiftAssignment.getShift().addTask(this);
            this.setShift(objShiftAssignment.getShift());
        }

        if(objShiftAssignment.getEstimatedTime()!=null)
            this.setEstimatedTime(objShiftAssignment.getEstimatedTime());

        if(objShiftAssignment.getPortions()!=null)
            this.setPortions(objShiftAssignment.getPortions());

        if(objShiftAssignment.getPreparedPortions()!=null)
            this.setReadyPortions(objShiftAssignment.getPreparedPortions());
    }

    public boolean removeShift() {
        boolean ret = false;
        if (shift != null) {
            ret = this.shift.removeTask(this);
        }
        if (ret == true) {
            this.shift = null;
            this.cook = null;
            this.estimatedTime = -1;
            this.portions = null;
            this.readyPortions = null;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "lavoro in cucina: " + job +
                (shift != null ? "\n\tturno: " + shift : "") +
                (cook != null ? "\n\tcuoco: " + cook : "") +
                (estimatedTime != -1 ? "\n\ttempo stimato: " + estimatedTime : "") +
                (portions != null ? "\n\tporzioni: " + portions : "") +
                (readyPortions != null ? "\n\tporzioni già pronte: " + readyPortions : "") + "\n";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static void saveAllNewTasks(int sheetId, List<Task> tasks, int pos) {
        String taskInsert = "INSERT INTO catering.Tasks (sheet_id, job_id, position) VALUES (?, ?, ?);";
        PersistenceManager.executeBatchUpdate(taskInsert, tasks.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, sheetId);
                ps.setInt(2, tasks.get(batchCount).job.getId());
                ps.setInt(3, pos + batchCount);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                tasks.get(count).id = rs.getInt(1);
            }
        });
    }

    public static void deleteTask(int sheetId, Task task, int pos) {
        String taskDelete = "DELETE FROM Tasks WHERE id = " + task.id;
        PersistenceManager.executeUpdate(taskDelete);

        String updateSuccTasks = "UPDATE Tasks SET position = (position - 1) WHERE sheet_id = " +
                sheetId +
                " AND position > " +
                pos + ";";
        PersistenceManager.executeUpdate(updateSuccTasks);
    }

    public static void updateTask(Task task) {
        String taskUpdate = "UPDATE Tasks SET shift_id = " + task.shift.getId() +
                ", cook_id = " + task.cook.getId() +
                ", portions = '" + task.portions +
                "', ready_portions = '" + task.readyPortions +
                "', estimated_time = " + task.estimatedTime +
                " WHERE id = " + task.id;
        PersistenceManager.executeUpdate(taskUpdate);
    }

    public static void deleteAssignment(int id) {
        String assignmentDelete = "UPDATE Tasks SET shift_id = NULL WHERE id = " + id;
        PersistenceManager.executeUpdate(assignmentDelete);
    }

    public static ArrayList<Task> loadTaskFor(int shift_id) {
        ArrayList<Task> result = new ArrayList<>();
        String query = "SELECT * FROM Tasks WHERE shift_id = " + shift_id + "";
        PersistenceManager.executeQuery(query, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                Task task = new Task();
                task.id = rs.getInt("id");
                task.estimatedTime = rs.getInt("estimated_time");
                task.portions = rs.getString("portions");
                task.readyPortions = rs.getString("ready_portions");
                int job_id = rs.getInt("job_id");
                task.job = KitchenJob.loadKitchenJobById(job_id);
                int cook_id = rs.getInt("cook_id");
                task.cook = User.loadUserById(cook_id);
                task.shift = null;
                result.add(task);
            }
        });
        return result;
    }
}


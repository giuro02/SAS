package catering.businesslogic.summarySheet;
import catering.businesslogic.shift.Shift;

import java.util.ArrayList;

public class KitchenTaskEventReceiver  {
    public void updateTaskCreated(SummarySheet sheet, ArrayList<Task> tasks);
    public void updateSheetCreated(SummarySheet sheet);
    public void updateOrderedTasks(SummarySheet sheet);
    public void updateTaskAssignment(Task task, Shift shift);
    public void updateTaskAssignmentDeleted(Task task); //Shift shift);
    public void updateTaskDeleted(SummarySheet sheet, Task task, int pos); //int pos
    public void taskInfoModified(Task task);

    public void updateRegenerateSheet(SummarySheet currentSheet) {
    }
}

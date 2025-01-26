package catering.persistence;

import catering.businesslogic.menu.Menu;
import catering.businesslogic.menu.MenuItem;
import catering.businesslogic.menu.Section;
import catering.businesslogic.shift.Shift;
import catering.businesslogic.summarySheet.KitchenTaskEventReceiver;
import catering.businesslogic.summarySheet.SummarySheet;
import catering.businesslogic.summarySheet.Task;

import java.sql.Array;
import java.util.ArrayList;

public class SheetPersistence implements KitchenTaskEventReceiver {

    @Override
    public void updateTaskCreated(SummarySheet sheet, Task task) {
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task);
        Task.saveAllNewTasks(sheet.getId(), taskList,sheet.getTaskListSize() );
    }

    @Override
    public void updateSheetCreated(SummarySheet s) {
        SummarySheet.saveNewSheet(s);
    }

    @Override
    public void updateOrderedTasks(SummarySheet sheet) {
        SummarySheet.saveTasksOrder(sheet);
    }

    @Override
    public void updateTaskAssignment(Task task) {
        Task.updateTask(task);
    }

    @Override
    public void updateTaskAssignmentDeleted(Task task) {
        Task.deleteAssignment(task.getId());
    }

    @Override
    public void updateTaskDeleted(SummarySheet sheet, Task task, int pos) {
        Task.deleteTask(sheet.getId(),task,sheet.getTaskList().indexOf(task));
    }

    @Override
    public void updateTaskInfoModified(Task task) {
        Task.updateTask(task);
    }

}

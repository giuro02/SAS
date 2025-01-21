package catering.businesslogic.summarySheet;

import catering.businesslogic.shift.Shift;

public class KitchenTaskManager {
    private void notifyTaskCreated(ArrayList<Task> tasks) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskCreated(this.currentSheet, tasks);
        }
    }

    private void notifySheetCreated(SummarySheet sheet) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateSheetCreated(sheet);
        }
    }

    private void notifyOrderedTasks(SummarySheet sheet) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateOrderedTasks(sheet);
        }
    }

    private void notifyTaskAssigned(Task task, Shift shift) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAssignment(task);
        }
    }

    private void notifyTaskAssignmentDeleted(Task task, Shift shift) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAssignmentDeleted(task);
        }
    }

    private void notifyTaskInfoModified(Task task) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateRegenerateSheet(this.currentSheet);
        }
    }

    private void notifyTaskDeleted(SummarySheet sheet, Task task /*int pos*/) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskDeleted(sheet, task, pos);
        }
    }
}
}

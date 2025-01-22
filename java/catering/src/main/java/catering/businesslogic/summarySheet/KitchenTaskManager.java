package catering.businesslogic.summarySheet;

import catering.businesslogic.recipe.KitchenJob;
import catering.businesslogic.user.*;
import catering.businesslogic.shift.*;
import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import java.util.ArrayList;

public class KitchenTaskManager{
    private SummarySheet currentSheet;
    private ArrayList<KitchenTaskEventReceiver> eventReceivers;

    public KitchenTaskManager() {
        eventReceivers = new ArrayList<>();
    }

    public SummarySheet getCurrentSheet(){
        return this.currentSheet;
    }

    public void addEventReceiver(KitchenTaskEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(KitchenTaskEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }


    public SummarySheet createNewSheet(ServiceInfo service) throws UseCaseLogicException{
        User user = CatERing.getInstance().getUserManager().getCurrentUser();
        if(!user.isChef()||!user.equals(service.getHandler()))
            throw new UseCaseLogicException();

        SummarySheet sheet= new SummarySheet(service, user);
        this.notifySheetCreated(sheet);

        this.currentSheet=sheet;

        return sheet;
    }

    public SummarySheet getOtherSummarySheet(SummarySheet sheet) throws UseCaseLogicException, SheetException{
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        if(!this.currentSheet.isOwner(CatERing.getInstance().getUserManager().getCurrentUser()))
            throw new SheetException();

        this.currentSheet = sheet;

        return currentSheet;
    }

    public ArrayList<Task> createTask(KitchenJob kj) throws  UseCaseLogicException{

        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        ArrayList<Task> createdTasks=this.currentSheet.addTask(kj);

        this.notifyTaskCreated(createdTasks);

        return createdTasks;
    }

    public void deleteTask(Task task) throws UseCaseLogicException {
        if (this.currentSheet == null)
            throw new UseCaseLogicException();
        int pos = currentSheet.removeTask(task);

        this.notifyTaskDeleted(currentSheet, task, pos);
    }

    public SummarySheet orderTask(Task task, int pos) throws UseCaseLogicException{ //TODO come ordiniamo?
        if(this.currentSheet==null)
            throw new UseCaseLogicException();
        if(getTaskList)
        this.currentSheet.setTask(task, pos);

        this.notifyOrderedTasks(this.currentSheet);

        return this.currentSheet;
    }

    public ArrayList<KitchenShift> getShiftBoard() throws UseCaseLogicException{
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        return this.currentSheet.isOwner(CatERing.getInstance().getShiftManager().getShiftBoard());

    }

    public void assignTask(Task task, KitchenShift shift, User cook, Integer estimatedTime, String portions, String preparedPortions) throws UseCaseLogicException, SheetException {

        if(this.currentSheet==null || !this.currentSheet.containsTask(task))
            throw new UseCaseLogicException();

        if((estimatedTime!=null && shift!=null && !shift.enoughTime(estimatedTime)) || (cook != null && !cook.isCook()) || (shift!=null && cook!=null && !cook.isAvaliable(shift)))
            throw new SheetException();

        DTOShiftAssignment dto= new DTOShiftAssignment(shift, cook, estimatedTime, portions, preparedPortions);
        task.assignShift(dto);

        this.notifyTaskAssigned(task);
    }

    public void deleteAssignment(Task task) throws UseCaseLogicException {
        if(this.currentSheet==null || !this.currentSheet.containsTask(task) || task.getShift() == null)
            throw new UseCaseLogicException();

        task.removeShift();

        this.notifyTaskAssignmentDeleted(task);
    }



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

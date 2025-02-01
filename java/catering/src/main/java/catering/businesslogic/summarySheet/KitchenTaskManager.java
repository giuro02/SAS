package catering.businesslogic.summarySheet;

import catering.businesslogic.recipe.KitchenJob;
import catering.businesslogic.user.*;
import catering.businesslogic.shift.*;
import catering.businesslogic.CatERing;
import catering.businesslogic.UseCaseLogicException;
import catering.businesslogic.event.ServiceInfo;
import java.util.ArrayList;

public class KitchenTaskManager{
    public void setCurrentSheet(SummarySheet currentSheet) {
        this.currentSheet = currentSheet;
    }

    public ArrayList<KitchenTaskEventReceiver> getEventReceivers() {
        return eventReceivers;
    }

    public void setEventReceivers(ArrayList<KitchenTaskEventReceiver> eventReceivers) {
        this.eventReceivers = eventReceivers;
    }

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
        System.out.println(user.toString());
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

    public Task createTask(KitchenJob kj) throws  UseCaseLogicException{

        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        Task createdTasks=this.currentSheet.addTask(kj);

        this.notifyTaskCreated(createdTasks);

        return createdTasks;
    }

    public void deleteTask(Task task) throws UseCaseLogicException {
        if (this.currentSheet == null)
            throw new UseCaseLogicException();
        int pos = currentSheet.removeTask(task);

        /*int i = 0;
        for(i=pos; i < currentSheet.getTaskList().size() && i!=-1; i++) {
            //aggiorno le posizioni indietro di uno -- passando sulla lista dei task dal turno associato al task
            if(i!=0)
                currentSheet.setTask(currentSheet.getTaskList().get(i), i-1);
        }*/

        this.notifyTaskDeleted(currentSheet, task, pos);
    }

    public void orderTask(Task task, int pos) throws UseCaseLogicException{
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        this.currentSheet.setTask(task, pos);

        this.notifyOrderedTasks(this.currentSheet);

        //return this.currentSheet;
    }
//forse il nome deve essere cambiato in getShifts non shiftBoard che non la tocchiamo
    public ArrayList<KitchenShift> getShiftBoard() throws UseCaseLogicException{//TODO come recuperiamo shiftboard
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        return this.currentSheet.getService().getKitchenShifts(); //come ha fatto ste, passando da service, tanto penso che
    }



    public void assignTask(Task task, KitchenShift shift) throws UseCaseLogicException, SheetException {
//assegno il task (che è un compito di cucina) al kitchenShift
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        if(!this.currentSheet.containsTask(task))
            throw new SheetException();

        if(shift.enoughTime(task.getEstimatedTime() == -1 ? 0 : task.getEstimatedTime() )) //assegno il task allo shift solo se lo shift ha abbastanza tempo a disposizione
            task.assignShift(shift);
        else {
            //System.out.println("Impossibile assegnare il task" +task + "tempo insufficente");
            System.out.println("\u001B[31mImpossibile assegnare il task: " + task + " TEMPO INSUFFICENTE\u001B[0m");
            System.exit(500);
        }
        this.notifyTaskAssigned(task, shift);
    }

    public void modifyInfoTaskAssignment(Task task, int cook, Integer estimatedTime, String portions, String preparedPortions) throws UseCaseLogicException, SheetException {
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        if(!this.currentSheet.containsTask(task))
            throw new SheetException();

        task.modifyTaskInfo(cook, estimatedTime, portions, preparedPortions);

        this.notifyTaskInfoModified(task);
    }

    public void deleteAssignment(Task task, KitchenShift shift) throws UseCaseLogicException, SheetException {
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        if(!this.currentSheet.containsTask(task))
            throw new SheetException();

        //if(shift.getTasksList().size()  )
        task.removeShift(shift); //glielo metto dentro se teniamo che un task può avere più shifts

        this.notifyTaskAssignmentDeleted(task, shift);
    }

    private void notifyTaskCreated(Task task) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskCreated(this.currentSheet, task);
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
            //er.updateTaskAssignment(task, shift);
        }
    }

    private void notifyTaskAssignmentDeleted(Task task, Shift shift) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskAssignmentDeleted(task);
        }
    }

    private void notifyTaskInfoModified(Task task) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskInfoModified(task);
        }
    }

    private void notifyTaskDeleted(SummarySheet sheet, Task task, /*int pos*/int pos) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskDeleted(sheet, task, pos);
        }
    }
}


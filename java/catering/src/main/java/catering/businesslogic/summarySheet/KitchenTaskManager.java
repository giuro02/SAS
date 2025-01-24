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

    public void deleteTask(Task task, KitchenShift shift) throws UseCaseLogicException {
        if (this.currentSheet == null)
            throw new UseCaseLogicException();
        int pos = currentSheet.removeTask(task, shift);

        int i = 0;
        for(i=pos; i < task.getShift().getTasksList().size(); i++) {
            //aggiorno le posizioni indietro di uno -- passando sulla lista dei task dal turno associato al task
            currentSheet.setTask(task.getShift().getTasksList().get(i), i-1);
        }

        this.notifyTaskDeleted(currentSheet, task, pos);
    }

    public SummarySheet orderTask(Task task, int pos) throws UseCaseLogicException{ //TODO come ordiniamo?
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        this.currentSheet.setTask(task, pos);

        this.notifyOrderedTasks(this.currentSheet);

        return this.currentSheet;
    }

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

        if(shift.enoughTime(task.getEstimatedTime())) //assegno il task allo shift solo se lo shift ha abbastanza tempo a disposizione
            task.assignShift(shift);

        this.notifyTaskAssigned(task, shift);
    }

    //TODO da finire
    public void modifyInfoTaskAssignment(Task task, User cook, Integer estimatedTime, String portions, String preparedPortions) throws UseCaseLogicException, SheetException {
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        if(!this.currentSheet.containsTask(task))
            throw new SheetException();

        this.notifyTaskInfoModified(task);
    }

    public void deleteAssignment(Task task, KitchenShift shift) throws UseCaseLogicException, SheetException {
        if(this.currentSheet==null)
            throw new UseCaseLogicException();

        if(!this.currentSheet.containsTask(task))
            throw new SheetException();
        //MA SE NOI NON ABBIAMO SOLO UN TASK PER UN TURNO NON HA SENSO ELIMINARE IL TURNO A MENO CHE IL TASK
        //CHE STIAMO RIMUOVENDO NON SIA L'ULTIMO DEL TURNO (UN TURNO SENZA TASK NON CREDO ABBIA SENSO DI
        //ESISTERE)
        //if(shift.getTasksList().size()  )
        task.removeShift(shift, task); //glielo metto dentro se teniamo che un task può avere più shifts

        this.notifyTaskAssignmentDeleted(task, shift);
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
            er.updateTaskAssignment(task, shift);
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

    private void notifyTaskDeleted(SummarySheet sheet, Task task, /*int pos*/int pos) {
        for (KitchenTaskEventReceiver er : this.eventReceivers) {
            er.updateTaskDeleted(sheet, task, pos);
        }
    }
}


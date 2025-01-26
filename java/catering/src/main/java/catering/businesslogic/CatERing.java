package catering.businesslogic;

import catering.businesslogic.event.EventManager;
import catering.businesslogic.menu.MenuManager;
import catering.businesslogic.recipe.RecipeManager;
import catering.businesslogic.summarySheet.KitchenTaskEventReceiver;
import catering.businesslogic.summarySheet.KitchenTaskManager;
import catering.businesslogic.user.UserManager;
import catering.persistence.MenuPersistence;
import catering.persistence.SheetPersistence;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;

    private MenuPersistence menuPersistence;
    private KitchenTaskManager kitchenTaskMgr;
    private KitchenTaskEventReceiver kitchenTaskPersistence;



    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        menuPersistence = new MenuPersistence();
        kitchenTaskMgr = new KitchenTaskManager();

        menuMgr.addEventReceiver(menuPersistence);
        kitchenTaskPersistence = new SheetPersistence();
        kitchenTaskMgr = new KitchenTaskManager();
        kitchenTaskMgr.addEventReceiver(kitchenTaskPersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public KitchenTaskManager getKitchenTaskManager() { return kitchenTaskMgr; }
}

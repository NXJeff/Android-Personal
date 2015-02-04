package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "merlin");
        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");
        new DaoGenerator().generateAll(schema, args[0]);

        //Reminder
        Entity reminder = schema.addEntity("Reminder");
        reminder.addIdProperty();
        reminder.addStringProperty("name");
        reminder.addStringProperty("description");
        reminder.addDateProperty("reminderTime");
        reminder.addStringProperty("repeatType");
        reminder.addStringProperty("dayToRepeat");
        reminder.addBooleanProperty("status");

    }
}
package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "merlin.model.raw");
//        Entity box = schema.addEntity("Box");
//        box.addIdProperty();
//        box.addStringProperty("name");
//        box.addIntProperty("slots");
//        box.addStringProperty("description");


        //Reminder
        Entity reminder = schema.addEntity("BaseReminder");
        reminder.setTableName("Reminder");
        reminder.addIdProperty();
        reminder.addStringProperty("subject");
        reminder.addStringProperty("description");
        reminder.addDateProperty("fromDate");
        reminder.addDateProperty("toDate");
        reminder.addDateProperty("atTime");

        reminder.addIntProperty("repeatType");
        reminder.addStringProperty("reminderType");
        reminder.addIntProperty("customRepeatMode");

        reminder.addIntProperty("color");
        reminder.addIntProperty("repeatEveryNDay");
        reminder.addIntProperty("dosesInTotal");
        reminder.addIntProperty("dosesPerDay");
        reminder.addBooleanProperty("enabled");
        reminder.addStringProperty("status");


        //end
        new DaoGenerator().generateAll(schema, args[0]);
    }
}

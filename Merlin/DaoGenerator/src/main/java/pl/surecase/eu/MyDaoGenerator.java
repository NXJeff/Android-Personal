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
        Entity reminder = schema.addEntity("Reminder");
        reminder.setTableName("reminder");
        reminder.addIdProperty();
        reminder.addStringProperty("entityId"); //Unique ID for each item of this entity
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

        Entity meal = schema.addEntity("Meal");
        meal.setTableName("meal");
        meal.addIdProperty();
        meal.addStringProperty("entityId");
        meal.addStringProperty("subject");
        meal.addStringProperty("description");
        meal.addStringProperty("mealType");
        meal.addDateProperty("date");
        meal.addStringProperty("status");
        meal.addIntProperty("color");

        Entity imageHolder = schema.addEntity("ImageHolder");
        imageHolder.setTableName("image_holder");
        imageHolder.addIdProperty();
        imageHolder.addStringProperty("entityId");
        imageHolder.addStringProperty("path");



        //end
        new DaoGenerator().generateAll(schema, args[0]);
    }
}

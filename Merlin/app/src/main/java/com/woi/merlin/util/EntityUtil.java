package com.woi.merlin.util;

import android.content.Context;
import android.util.Log;

import com.woi.merlin.enumeration.EntityType;

import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.Query;
import merlin.model.raw.BaseMealDao;
import merlin.model.raw.BaseReminder;
import merlin.model.raw.BaseReminderDao;
import merlin.model.raw.DaoSession;

/**
 * Created by YeekFeiTan on 3/4/2015.
 */
public class EntityUtil {

    public static String generateEntityUniqueID(Context context, EntityType type) {
        DaoSession daoSession = DbUtil.setupDatabase(context);
        boolean isUnique = false;
        List entityItems = null;
        String ID = null;

        do {
            ID = generateUUID();
            Log.d("EntityUtil", "ID generated: " + ID);
            Query query = null;

            switch (type) {
                case MEAL:
                    BaseMealDao mealDao = daoSession.getBaseMealDao();
                    query = mealDao.queryBuilder().where(BaseMealDao.Properties.EntityId.eq(ID)).build();
                    entityItems = query.list();
                    break;

                case REMINDER:
                    BaseReminderDao reminderDao = daoSession.getBaseReminderDao();
                    query = reminderDao.queryBuilder().where(BaseReminderDao.Properties.EntityId.eq(ID)).build();
                    entityItems = query.list();
                    break;
            }

            if (entityItems != null && !entityItems.isEmpty()) {
                isUnique = false;
            } else {
                isUnique = true;
            }

        } while (isUnique == false);

        return ID;
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}

package woi.woi.okay.model.notes;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by YeekFeiTan on 11/26/2015.
 */
public class Note  extends SugarRecord<Note> {

    String note;
    Date date;

    public Note() {
    }

    public Note(String note, Date date) {
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

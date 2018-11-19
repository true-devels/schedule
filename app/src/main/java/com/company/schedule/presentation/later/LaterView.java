package com.company.schedule.presentation.later;


import com.company.schedule.model.data.base.Note;

import java.util.List;

public interface LaterView {
     void setTodayNotes(List<Note> newNotes);
     void setWeekNotes(List<Note> newNotes);
     void setMonthNotes(List<Note> newNotes);
}

package com.example.todoapp.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insertTask(TaskEntry taskEntry);

    @Update
    void updateTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Query("SELECT * FROM taskentry")
    public List<TaskEntry> listTask();

    @Query("SELECT * FROM taskentry WHERE uid = :id")
    public TaskEntry getTaskById(int id);
}

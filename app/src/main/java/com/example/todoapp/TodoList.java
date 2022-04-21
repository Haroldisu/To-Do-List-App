package com.example.todoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todoapp.Room.TaskDatabase;
import com.example.todoapp.Room.TaskEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link TodoList#newInstance} factory method to
// * create an instance of this fragment.
// */
public class TodoList extends Fragment {

    private Button mButton;

    private FirebaseAuth mAuth;
    private View view;
    private TaskDatabase db;

    private Activity mActivity = getActivity();
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private List<TaskEntry> tasksList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        // Set the RecyclerView to its corresponding view
        mRecyclerView = view.findViewById(R.id.recyclerViewTasks);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list


        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new TaskAdapter(tasksList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<TaskEntry> tasks = mAdapter.getTasks();
                        db.getTaskDAO().deleteTask(tasks.get(position));
                        uiUpdate();
                    }
                }).start();
            }
        }).attachToRecyclerView(mRecyclerView);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        db = TaskDatabase.getInstance(getContext());

        mButton =  view.findViewById(R.id.btn_addnewtask);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_todoList2_to_newTask);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setTasks(db.getTaskDAO().listTask());
            }
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.listoptions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())){
            case R.id.item_setting:
                navigateToSetting();
                return true;
            case R.id.item_date:
                navigateToDatePage();
                return true;
            case R.id.item_logout:
                firebaseLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void navigateToSetting() {
        Navigation.findNavController(view).navigate(R.id.action_todoList2_to_detailProfile);
    }
    private void navigateToDatePage() {
        Navigation.findNavController(view).navigate(R.id.action_todoList2_to_datePage);
    }
    private void firebaseLogout() {
        mAuth.signOut();

        Toast.makeText(getActivity(), "成功登出!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    void uiUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TaskEntry> tasks = db.getTaskDAO().listTask();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        mAdapter.setTasks(tasks);
                    }
                });
            }
        }).start();

    }
}


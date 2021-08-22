package apps.vaishu.todos;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import apps.vaishu.todos.Adapter.MyAdapter;
import apps.vaishu.todos.Model.Todo;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private LinearLayout noToDosView;
    private Switch markCompletedSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(this);
        recyclerView.setAdapter(adapter);

        // initialize and add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // On Click Add, Go to Add to do page
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TodoActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //Initialize no todos view
        noToDosView = findViewById(R.id.no_records_view);
        noToDosView.setVisibility(adapter.getTodos().size() == 0 ? View.VISIBLE : View.INVISIBLE);

        // Set up on scroll listener to show/hide the fab icon when scrolling
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });

        // toggle and filter results
        markCompletedSwitch = (Switch) findViewById(R.id.showCompleted);
        markCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    markCompletedSwitch.setText("Show All");
                }else{
                    markCompletedSwitch.setText("Show Completed");
                }
                adapter.getFilter().filter("null," + isChecked);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String id = data.getStringExtra("TODOID");
                String title = data.getStringExtra("TODOTITLE");
                String notes = data.getStringExtra("TODONOTES");
                String pri = data.getStringExtra("TODOPRI");
                Boolean done = data.getBooleanExtra("TODODONE", false);
                Boolean isDelete = data.getBooleanExtra("ISDELETE", false);
                String dueDate = data.getStringExtra("DUEDATE");
                if(id == null){
                    //Log.v("ss", "add");
                    Todo todo =  new Todo(title,
                            notes, Integer.parseInt(pri), false, dueDate);
                    adapter.addTodo(todo);
                    Toast.makeText(getApplicationContext(), "Todo added!", Toast.LENGTH_SHORT).show();

                }else{
                    if(isDelete){
                        adapter.removeTodo(Integer.parseInt(id));
                        //Log.v("ss", "remove " +  isDelete);
                        Toast.makeText(getApplicationContext(), "Todo deleted!", Toast.LENGTH_SHORT).show();
                    }else{
                        //Log.v("ss", "update " +  done);
                        Todo todo =  new Todo(Integer.parseInt(id), title,
                                notes, Integer.parseInt(pri), done, dueDate);
                        adapter.updateTodo(todo);
                        Toast.makeText(getApplicationContext(), "Todo updated!", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText + "," +  markCompletedSwitch.isChecked());
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
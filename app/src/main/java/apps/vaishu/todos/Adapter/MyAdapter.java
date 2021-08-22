package apps.vaishu.todos.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import apps.vaishu.todos.Model.Todo;
import apps.vaishu.todos.R;
import apps.vaishu.todos.TodoActivity;

import android.util.Log;

import static android.app.Activity.RESULT_OK;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<Todo> todos = new ArrayList<>();
    private List<Todo> filteredTodos = new ArrayList<>();;

    public MyAdapter(Context context){
        this.context = context;
        this.initTodos();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = filteredTodos.get(position);
        holder.title.setText(todo.getTitle());
        holder.notes.setText(todo.getNotes());
        if(Integer.toString(todo.getPriority()).equals("3")){
            holder.priority.setText("HIGH");
            holder.priority.setTextColor(ContextCompat.getColor(context, R.color.red));
        }else if(Integer.toString(todo.getPriority()).equals("2")){
            holder.priority.setText("MEDIUM");
            holder.priority.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else if(Integer.toString(todo.getPriority()).equals("1")){
            holder.priority.setText("LOW");
            holder.priority.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
        holder.date.setText(todo.getDate());
        holder.doneBtn.setVisibility(todo.getComplete() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return filteredTodos.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if(constraint == null || constraint.length() == 0){
                    filterResults.count = todos.size();
                    filterResults.values = todos;

                }else{
                    String filter = constraint.toString();
//                    System.out.println("print filter: " + " " + filter + filter.split("|")[0] + " " + filter.split("|")[1]);
                    String[] res= filter.split(",");
                    String searchTerm = res[0];
                    String showCompletedFlag = res[1];
                    //System.out.println("one: " + searchTerm.toLowerCase() + " two " + showCompletedFlag);
                    List<Todo> result = new ArrayList<>();
                    for(Todo todo:todos){
                        Boolean flag = true;
                        if(!searchTerm.equals("null")){
                            flag = todo.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
                            //System.out.println("comparing with " +  todo.getTitle() + " and " + searchTerm.toLowerCase() + " flag " + flag);
                        }

                        if(!showCompletedFlag.equals("null") && showCompletedFlag.equals("true")){
                            flag = flag && todo.getComplete().toString().equals(showCompletedFlag);
                            //System.out.println("comparing with " +  todo.getComplete() + " and " + showCompletedFlag + " flag " + flag);
                        }

                        if(flag){
                            //System.out.println("found " +  todo.getTitle());
                            result.add(todo);
                        }
                        filterResults.count = result.size();
                        filterResults.values = result;
                    }
                }
                return filterResults;
            }



            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredTodos = (List<Todo>) results.values;
                Collections.sort(filteredTodos, new Comparator<Todo>() {
                    @Override
                    public int compare(Todo lhs, Todo rhs) {
                        return rhs.getPriority() - lhs.getPriority();
                    }
                });
                notifyDataSetChanged();

            }
        };
        return filter;

    }

    public int getFilteredTodoIndex(int id){
        for(int index= 0; index< filteredTodos.size(); index++){
            if(filteredTodos.get(index).getId() == id){
                return index;
            }
        }
        return -1;
    }

    public int getTodoIndex(int id){
        for(int index= 0; index< todos.size(); index++){
            if(todos.get(index).getId() == id){
                return index;
            }
        }
        return -1;
    }

    public Todo addTodo(Todo todo){
        todo.setId(filteredTodos.size()+1);
        filteredTodos.add(todo);
        todos.add(todo);
        this.sortTodos();
        notifyDataSetChanged();
        Log.v("tt" , " size  " + filteredTodos.size());
        return todo;
    }

    public Todo updateTodo(Todo todo){
        // Log.v("id", String.valueOf(getFilteredTodoIndex(todo.getId())));
        filteredTodos.set(getFilteredTodoIndex(todo.getId()), todo);
        todos.set(getTodoIndex(todo.getId()), todo);
        this.sortTodos();
        notifyDataSetChanged();
        //Log.v("pp", todo.toString() + " size " + filteredTodos.size());
        return todo;
    }

    public void removeTodo(int id){
        //Log.v("removed " , " index  " + getFilteredTodoIndex(id));
        filteredTodos.remove(getFilteredTodoIndex(id));
        todos.remove(getTodoIndex(id));
        this.sortTodos();
        notifyDataSetChanged();
        //Log.v("removed " , " size  " + filteredTodos.size());
    }

    public void initTodos(){
        todos.add(new Todo(1,"Assignment 1", "Text and Data mining course", 1, true, "23-10-2020"));
        todos.add(new Todo(2,"Homework 1", "Android mobile apps course", 3, false, "03-11-2020"));
        todos.add(new Todo(3,"Assignment 2", "Text and Data mining course", 3, true, "24-10-2020"));
        todos.add(new Todo(4,"Homework 2", "Android mobile apps course", 2, false, "02-11-2020"));
        todos.add(new Todo(5,"Assignment 3", "Text and Data mining course", 1, true, "23-10-2020"));
        todos.add(new Todo(6,"Homework 3", "Android mobile apps course", 3, false, "01-11-2020"));
        todos.add(new Todo(7,"Assignment 4", "Text and Data mining course", 3, true, "25-10-2020"));
        todos.add(new Todo(8,"Homework 4", "Android mobile apps course", 1, false, "09-11-2020"));
        todos.add(new Todo(9,"Final Project", "Text and Data mining course", 3, true, "29-10-2020"));
        todos.add(new Todo(10,"Final Project", "Android mobile apps course", 1, false, "10-11-2020"));
        this.filteredTodos = new ArrayList<>(todos);
        this.sortTodos();
        for(Todo todo: filteredTodos){
            Log.v("all", todo.toString());
        }
    }

    public List<Todo> getTodos(){
        return this.filteredTodos;
    }

    public List<Todo> sortTodos(){
        Collections.sort(filteredTodos, new Comparator<Todo>() {
            @Override
            public int compare(Todo lhs, Todo rhs) {
                return rhs.getPriority() - lhs.getPriority();
            }
        });
        return this.filteredTodos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public TextView notes;
        public TextView priority;
        public TextView date;
        public ImageButton doneBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.title);
            notes = (TextView) itemView.findViewById(R.id.notes);
            priority = (TextView) itemView.findViewById(R.id.priority);
            date = (TextView) itemView.findViewById(R.id.txtDate);
            doneBtn = (ImageButton) itemView.findViewById(R.id.imgDone);
        }

        @Override
        public void onClick(View v) {
            // Get position of the row clicked
            int position = getAdapterPosition();
            Todo todo = filteredTodos.get(position);
            // Toast.makeText(context, todo.getTitle(), Toast.LENGTH_LONG).show();

             // Start Details activity here.
            Intent intent = new Intent(context, TodoActivity.class);
            intent.putExtra("TODODONE", todo.getComplete());
            intent.putExtra("TODOID", todo.getId().toString());
            intent.putExtra("TODOTITLE", todo.getTitle().toString());
            intent.putExtra("TODONOTES", todo.getNotes().toString());
            intent.putExtra("TODOPRI", todo.getPriority().toString());
            intent.putExtra("DUEDATE", todo.getDate().toString());
            ((Activity) context).startActivityForResult(intent, 1);
        }

    }
}

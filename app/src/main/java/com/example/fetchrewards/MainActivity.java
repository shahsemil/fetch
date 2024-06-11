package com.example.fetchrewards;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            itemList = new ArrayList<>();
            itemAdapter = new ItemAdapter(itemList);
            recyclerView.setAdapter(itemAdapter);

            fetchItems();
        } else {
            Toast.makeText(this, "RecyclerView is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchItems() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Item>>() {}.getType();
                        List<Item> items = gson.fromJson(response.toString(), listType);
                        if (items != null) {
                            processItems(items);
                        } else {
                            Toast.makeText(MainActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void processItems(List<Item> items) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getName() != null && !item.getName().trim().isEmpty()) {
                filteredItems.add(item);
            }
        }

        Collections.sort(filteredItems, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int listIdComparison = Integer.compare(o1.getListId(), o2.getListId());
                if (listIdComparison == 0) {
                    return o1.getName().compareTo(o2.getName());
                }
                return listIdComparison;
            }
        });

        itemList.clear();
        itemList.addAll(filteredItems);
        itemAdapter.notifyDataSetChanged();
    }
}

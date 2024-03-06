package com.example.xyz2_friend;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import com.example.xyz2_friend.adapter.FriendAdapter;
import com.example.xyz2_friend.model.Friend;
public class MainActivity extends AppCompatActivity {
    public static final String FILENAME = "roman.dat";
    public static final String MODE = "mode";
    public static final String FRIEND = "friend";
    public static final String FRIENDS = "friends";
    public static final String POSITION = "position";
    public static final int ADD_MODE = 0;
    public static final int VIEW_MODE = 1;
    public static final int EDIT_MODE = 2;
    private final List<Friend> mList = new ArrayList<>();
    private FriendAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar1));
        if (getListFromInternalStorage() != null) {
            mList.addAll(getListFromInternalStorage());
        }
        if (savedInstanceState != null) {
            mList.clear();
            //noinspection unchecked
            mList.addAll((Collection<? extends Friend>)
                    Objects.requireNonNull(savedInstanceState.getSerializable(MainActivity.FRIENDS)));
        }
        mAdapter = new FriendAdapter(mList, this);
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(this::onItemLongClick);
        listView.setOnItemClickListener(this::onItemClick);
        findViewById(R.id.fab).setOnClickListener(this::addItem);
    }
    private void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, AddAndViewActivity.class);
        intent.putExtra(MainActivity.MODE, MainActivity.VIEW_MODE);
        intent.putExtra(MainActivity.FRIEND, mList.get(i));
        startActivity(intent);
    }
    private void addItem(View view) {
        Intent intent = new Intent(this, AddAndViewActivity.class);
        intent.putExtra(MainActivity.MODE, MainActivity.ADD_MODE);
        resultLauncher.launch(intent);
    }
    private boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        CharSequence[] items = {"Edit", "Delete"};
        int[] checked = {-1};
        new AlertDialog.Builder(this)
                .setTitle("Your Action")
                .setSingleChoiceItems(items, checked[0], (dialogInterface, i1) -> checked[0] = i1)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", (dialogInterface, i1) -> {
                    switch (checked[0]) {
                        case 0: //edit
                            Intent intent = new Intent(this, AddAndViewActivity.class);
                            intent.putExtra(MainActivity.MODE, MainActivity.EDIT_MODE);
                            intent.putExtra(MainActivity.FRIEND, mList.get(i));
                            intent.putExtra(MainActivity.POSITION, i);
                            resultLauncher.launch(intent);
                            break;
                        case 1: //delete
                            new AlertDialog.Builder(this)
                                    .setTitle("Confirm")
                                    .setMessage("Delete " + mList.get(i).toString() + "?")
                                    .setNegativeButton("Cancel", null)
                                    .setPositiveButton("Yes", (dialogInterface1, i2) -> {
                                        mList.remove(i);
                                        mAdapter.notifyDataSetChanged();
                                    }).show();
                            break;
                    }
                }).show();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mi_about) {
            new AlertDialog.Builder(this)
                    .setTitle("Info")
                    .setMessage(R.string.about_msg)
                    .setPositiveButton("OK", null).show();
        } else if (item.getItemId() == R.id.mi_exit) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm")
                    .setMessage("Close App?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", (dialogInterface, i) -> finish()).show();
        }
        return super.onOptionsItemSelected(item);
    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    if (result.getData().getIntExtra(MainActivity.MODE, -1) == MainActivity.ADD_MODE) {
                        mList.add((Friend) result.getData().getSerializableExtra(MainActivity.FRIEND));
                        mAdapter.notifyDataSetChanged();
                    } else if (result.getData().getIntExtra(MainActivity.MODE, -1) == MainActivity.EDIT_MODE) {
                        int pos = result.getData().getIntExtra(MainActivity.POSITION, -1);
                        mList.set(pos, (Friend) result.getData().getSerializableExtra(MainActivity.FRIEND));
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MainActivity.FRIENDS, (Serializable) mList);
    }
    public void saveListToInternalStorage(List<Friend> ls) {
        try (FileOutputStream fos = openFileOutput(MainActivity.FILENAME, MODE_PRIVATE)) {
            String json = new Gson().toJson(ls);
            fos.write(json.getBytes());
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Friend> getListFromInternalStorage() {
        File file = new File(getFilesDir(), MainActivity.FILENAME);
        if(!file.exists())
            return null;
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = openFileInput(MainActivity.FILENAME)) {
            int read;
            while ((read = fis.read()) != -1) {
                sb.append((char) read);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String json = sb.toString();
        return new Gson().fromJson(json, new TypeToken<ArrayList<Friend>>() {
        }.getType());
    }
    @Override
    protected void onStop() {
        super.onStop();
        saveListToInternalStorage(mList);
    }
}
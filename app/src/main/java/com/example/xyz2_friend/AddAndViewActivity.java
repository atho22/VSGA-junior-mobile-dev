package com.example.xyz2_friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.example.xyz2_friend.model.Friend;
public class AddAndViewActivity extends AppCompatActivity {
    private TextInputEditText tieID, tieName, tieAddress, tiePhone;
    private Button btClear, btSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_view);
        setSupportActionBar(findViewById(R.id.toolbar2));
        tieID = findViewById(R.id.tie_id);
        tieName = findViewById(R.id.tie_name);
        tieAddress = findViewById(R.id.tie_address);
        tiePhone = findViewById(R.id.tie_phone);
        btClear = findViewById(R.id.bt_clear);
        btSave = findViewById(R.id.bt_save);
        btClear.setOnClickListener(this::clearForm);
        btSave.setOnClickListener(this::saveForm);
    }
    private void saveForm(View view) {
        if (tieID.getText().toString().isEmpty() || tieName.getText().toString().isEmpty()) {
            Snackbar.make(view, "id and name cannot empties", Snackbar.LENGTH_SHORT).show();
            return;
        }
        assert getIntent() != null;
        Friend friend = new Friend(
                Integer.parseInt(tieID.getText().toString()),
                tieName.getText().toString(),
                tieAddress.getText().toString(),
                tiePhone.getText().toString());
        if (getIntent().getIntExtra(MainActivity.MODE, -1) == MainActivity.ADD_MODE) {
            Intent intent = new Intent();
            intent.putExtra(MainActivity.MODE, MainActivity.ADD_MODE);
            intent.putExtra(MainActivity.FRIEND, friend);
            setResult(RESULT_OK, intent);
        } else if (getIntent().getIntExtra(MainActivity.MODE, -1) == MainActivity.EDIT_MODE)
        {
            int pos = getIntent().getIntExtra(MainActivity.POSITION, -1);
            Intent intent = new Intent();
            intent.putExtra(MainActivity.MODE, MainActivity.EDIT_MODE);
            intent.putExtra(MainActivity.POSITION, pos);
            intent.putExtra(MainActivity.FRIEND, friend);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
    private void clearForm(View view) {
        tieID.setText("");
        tieName.setText("");
        tieAddress.setText("");
        tiePhone.setText("");
    }
    @Override
    protected void onStart() {
        super.onStart();
        assert getIntent() != null;
        assert getSupportActionBar() != null;
        if (getIntent().getIntExtra(MainActivity.MODE, -1) == MainActivity.ADD_MODE) {
            getSupportActionBar().setTitle("Add Friend");
        } else if (getIntent().getIntExtra(MainActivity.MODE, -1) == MainActivity.VIEW_MODE)
        { getSupportActionBar().setTitle("View Friend");
            Friend friend = (Friend) getIntent().getSerializableExtra(MainActivity.FRIEND);
            assert friend != null;
            tieID.setText(String.valueOf(friend.getId()));
            tieName.setText(friend.getName());
            tieAddress.setText(friend.getAddress());
            tiePhone.setText(friend.getPhone());
            tieID.setFocusable(false);
            tieName.setFocusable(false);
            tieAddress.setFocusable(false);
            tiePhone.setFocusable(false);
            btClear.setVisibility(View.GONE);
            btSave.setVisibility(View.GONE);
        } else if (getIntent().getIntExtra(MainActivity.MODE, -1) == MainActivity.EDIT_MODE)
        {
            getSupportActionBar().setTitle("Edit Mode");
            Friend friend = (Friend) getIntent().getSerializableExtra(MainActivity.FRIEND);
            assert friend != null;
            tieID.setText(String.valueOf(friend.getId()));
            tieName.setText(friend.getName());
            tieAddress.setText(friend.getAddress());
            tiePhone.setText(friend.getPhone());
            tieID.setFocusable(false);
            btClear.setVisibility(View.GONE);
        }
    }
}
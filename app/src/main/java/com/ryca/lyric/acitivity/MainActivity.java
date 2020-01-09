package com.ryca.lyric.acitivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ryca.lyric.R;
import com.ryca.lyric.api.Api;
import com.ryca.lyric.api.ServiceGenerator;
import com.ryca.lyric.fragment.SingerFragment;
import com.ryca.lyric.model.AdminMessage;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "CLIENT";
    ImageView account;
    SharedPreferences prefs;
    private FragmentManager fragmentManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        SingerFragment fragment = new SingerFragment(MainActivity.this);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();


        account = findViewById(R.id.account);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        account.setOnClickListener(v -> showDialogRegister());
        findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStar();
            }
        });

        findViewById(R.id.admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminMessage();
            }
        });
    }

    private void showAdminMessage() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_admin_msg);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView msg = dialog.findViewById(R.id.admin_msg);

        Api service = null;
        try {
            service = ServiceGenerator.createService(Api.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Call<AdminMessage> call = service.getAdminMessage();
        call.enqueue(new Callback<AdminMessage>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<AdminMessage> call, Response<AdminMessage> response) {
                try {
                    if (response.code() == 200) {
                        String m = response.body().getMsg();
                        msg.setText(m);
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<AdminMessage> call, Throwable t) {
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showDialogRegister() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name_email);
        dialog.setCancelable(true);

        EditText name = dialog.findViewById(R.id.name);
        EditText email = dialog.findViewById(R.id.email);
        FloatingActionButton reg = dialog.findViewById(R.id.fab);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String name1 = prefs.getString("name", null);
        String city1 = prefs.getString("email", null);
        if (name1 != null)
            name.setText(name1);
        if (city1 != null)
            email.setText(city1);

        reg.setOnClickListener(v -> {
            if (name.length() < 3) {
                Toasty.warning(MainActivity.this, getString(R.string.error_name), Toasty.LENGTH_LONG).show();
                return;
            }

            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("name", name.getText().toString().trim().replaceAll("\\s+", " "));
            editor.putString("email", email.getText().toString().trim().replaceAll("\\s+", " "));
            editor.apply();
            Toasty.success(MainActivity.this, getString(R.string.done), Toasty.LENGTH_LONG).show();
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void showDialogStar() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_star);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
}

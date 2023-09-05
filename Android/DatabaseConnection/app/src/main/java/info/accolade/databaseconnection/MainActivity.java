package info.accolade.databaseconnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import info.accolade.databaseconnection.model.RegisterModel;
import info.accolade.databaseconnection.utils.ApiClient;
import info.accolade.databaseconnection.utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText ed_name;
    String name;
    ProgressDialog progressDialog;

    TextView manage, navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_name = findViewById(R.id.name);
        btnSubmit = findViewById(R.id.submit);
        manage = findViewById(R.id.manage);
        navigation = findViewById(R.id.navigation);

        //progress dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Your work in progress.");
        progressDialog.setCancelable(false);

        //button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = ed_name.getText().toString().trim();

                _checkValidation();
            }
        });

        //page redirection
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ManageStudent.class));
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            }
        });
    }

    //validation check
    private void _checkValidation() {
        if(name.isEmpty()) {
            ed_name.setError("Kindly enter name..!");
            ed_name.requestFocus();
        } else {
            progressDialog.show();
            _sendData();
        }
    }

    //sending data to server
    private void _sendData() {
        ApiInterface apiInterface  = ApiClient.getApiClient().create(ApiInterface.class);
        Call<RegisterModel> modalCall = apiInterface.getRegisterResponse(name);
        modalCall.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(@NonNull Call<RegisterModel> call, @NonNull Response<RegisterModel> response) {
                progressDialog.dismiss();
                try {
                    if(response.isSuccessful())
                    {
                        if(response.body()!=null)
                        {
                            if(response.body().getSuccess())
                            {
                                Log.e("Insertion success", "");
                                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Log.e("Insertion error", "unable to insert on database");
                                showAlertDialog(response.body().getMessage());
                            }
                        }
                        else
                        {
                            Log.e("Empty body response", "");
                            showAlertDialog("Unable to process your request, Kindly try after sometimes.");
                        }
                    }
                    else
                    {
                        Log.e("Un-success response", "");
                        showAlertDialog("Unable to process your request, Kindly try after sometimes.");
                    }
                }
                catch (Exception e)
                {
                    Log.e("Exception", e.getMessage());
                    showAlertDialog("Unable to process your request, Kindly try after sometimes.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterModel> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e("Throwable", "" + t);
                if (t instanceof SocketTimeoutException) {
                    showAlertDialog("Unable to connect server, Kindly try after sometimes.");
                }
                else if (t instanceof ConnectException) {
                    showAlertDialog("Unable to connect server, make sure that Wi-Fi or mobile data is turned on.");
                }
                else {
                    showAlertDialog("Unable to process your request, Kindly try after sometimes.");
                }

            }
        });
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alertDialog, int which) {
                progressDialog.show();
                _sendData();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alertDialog, int which) {
                alertDialog.cancel();
            }
        });
        builder.show();
    }
}
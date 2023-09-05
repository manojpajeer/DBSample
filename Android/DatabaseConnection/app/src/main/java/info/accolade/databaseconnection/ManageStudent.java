package info.accolade.databaseconnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import info.accolade.databaseconnection.adapter.StudentAdapter;
import info.accolade.databaseconnection.model.StudentModel;
import info.accolade.databaseconnection.utils.ApiClient;
import info.accolade.databaseconnection.utils.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageStudent extends AppCompatActivity {

    ApiInterface apiInterface;
    ProgressDialog progressDialog;

    ArrayList<StudentModel> studentModels = new ArrayList<>();
    private StudentAdapter studentAdapter;
    RecyclerView recyclerView;
    StudentAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student);

        recyclerView = findViewById(R.id.studentRecycler);

        //progress bar
        progressDialog = new ProgressDialog(ManageStudent.this);
        progressDialog.setMessage("Your work in progress.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        _loadData();
    }

    //connection to server
    private void _loadData() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<StudentModel>> artModal = apiInterface.getStudentResponse();
        artModal.enqueue(new Callback<List<StudentModel>>() {
            @Override
            public void onResponse(Call<List<StudentModel>> call, Response<List<StudentModel>> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            studentModels = new ArrayList<>(response.body());

                            listener = new StudentAdapter.RecyclerViewClickListener() {
                                @Override
                                public void onClick(View v, int position) {

                                }
                            };
                            studentAdapter = new StudentAdapter(ManageStudent.this, studentModels, listener);
                            recyclerView.setAdapter(studentAdapter);
                        }
                        else
                        {
                            Log.e("Empty body response", "");
                            showAlertDialog("Unable to process your request, Kindly try after sometimes..");
                        }
                    }
                    else
                    {
                        Log.e("Un-success response", "");
                        showAlertDialog("Unable to process your request, Kindly try after sometimes..");
                    }
                }
                catch (Exception e)
                {
                    Log.e("Exception", e.getMessage());
                    showAlertDialog("Unable to process your request, Kindly try after sometimes..");
                }
            }

            @Override
            public void onFailure(Call<List<StudentModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Throwable", "" + t);
                if (t instanceof SocketTimeoutException) {
                    showAlertDialog("Unable to connect server, Kindly try after sometimes..");
                }
                else if (t instanceof ConnectException) {
                    showAlertDialog("Unable to connect server, make sure that Wi-Fi or mobile data is turned on..");
                }
                else {
                    showAlertDialog("Unable to process your request, Kindly try after sometimes..");
                }

            }
        });
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageStudent.this);
        builder.setTitle("Alert");
        builder.setMessage(message);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alertDialog, int which) {
                progressDialog.show();
                _loadData();
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
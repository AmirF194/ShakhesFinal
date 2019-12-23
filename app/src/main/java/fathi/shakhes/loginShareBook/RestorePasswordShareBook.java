package fathi.shakhes.loginShareBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import shakhes.R;


public class RestorePasswordShareBook extends AppCompatActivity {
    private EditText etEmail;
    private Button btnRestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.restore_password_sharebook);

        initUI();
    }

    private void initUI() {

//       if( EmptyNot(etEmail) ){
//           btnRestore = (Button) findViewById(R.id.btnRestore);
//           btnRestore.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View view) {
//                //   onRestorePasswordButtonClicked();
//               }
//           });
//
//       }

    }

    private void onRestorePasswordButtonClicked() {
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError(getString(R.string.essential_filed));
            return;
        }
//        Backendless.UserService.restorePassword(etEmail.getText().toString(),
//                new AsyncCallback<Void>() {
//                    @Override
//                    public void handleResponse(Void response) {
//                        Toast.makeText(RestorePasswordShareBook.this,
//                                R.string.restore_email_sent,
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void handleFault(BackendlessFault fault) {
//                        Toast.makeText(RestorePasswordShareBook.this,
//                                getString(R.string.restore_email_fail),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
    public Boolean EmptyNot(EditText editText) {
        if (editText.getText().toString().equals("")) {
            editText.setError(getString(R.string.essential_filed));
            return false;
        } else {
            return true;
        }
    }

    public void  onBackPressed() {
        Intent myIntent = new Intent(RestorePasswordShareBook.this, LoginShareBook.class);
        startActivity(myIntent);
        finish();
    }

}
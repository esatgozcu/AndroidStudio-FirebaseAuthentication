package com.example.esatgozcu.firebaseauthenticationislemleri2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;import android.content.Intent;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class KayitSayfasi extends AppCompatActivity {
    private EditText kayitkullaniciadi;
    private EditText kayitsifre;
    private Button kayitbuton;

    private FirebaseAuth mAuth;
    private String userName;
    private String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kayit_sayfasi);


        kayitkullaniciadi=(EditText)findViewById(R.id.kayitkullaniciadi);
        kayitsifre=(EditText)findViewById(R.id.kayitsifre);
        kayitbuton=(Button)findViewById(R.id.kayitbuton);

        //Firabase kimlik doğrulama refaransını oluşturuyoruz
        mAuth = FirebaseAuth.getInstance();

        //Kayıt ol butonuna tıklanınca..
        kayitbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = kayitkullaniciadi.getText().toString();
                userPassword = kayitsifre.getText().toString();
                if(userName.isEmpty() || userPassword.isEmpty()){

                    //Eğer boş alan bıraktıysa hatayı yazdırıyoruz
                    Toast.makeText(getApplicationContext(),"Boş Alan Bırakmayınız !",Toast.LENGTH_SHORT).show();

                }else{

                    kayıt();
                }
            }
        });

    }
    private void kayıt() {

        // addOnCompleteListener ile işlemin başarılı olup olmadığını kontrol ediyoruz

        mAuth.createUserWithEmailAndPassword(userName,userPassword)
                .addOnCompleteListener(KayitSayfasi.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Bağlantı başarılı olursa MainActivity sayfasına yönlendiriyor
                            Intent i = new Intent(KayitSayfasi.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            //Her hangi bir hata olursa hatayı yazdırıyoruz
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}

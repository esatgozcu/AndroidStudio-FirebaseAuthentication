package com.example.esatgozcu.firebaseauthenticationislemleri2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText kullaniciadi;
    private EditText sifre;
    private Button girisbuton;
    private Button kayitbuton;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private String userName;
    private String userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kullaniciadi=(EditText)findViewById(R.id.kullaniciadi);
        sifre=(EditText)findViewById(R.id.sifre);
        girisbuton=(Button)findViewById(R.id.girisbuton);
        kayitbuton=(Button)findViewById(R.id.kayitbuton);

        //Oturum açık olup olmadığını anlamak için firebaseUser değişkenine değer döndürüyoruz
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //Session açık olup olmadığını kontrol ediyor
        if(firebaseUser != null){

            //Açıksa ProfilSayfasına gidiyor
            Intent i = new Intent(MainActivity.this,ProfilSayfasi.class);
            startActivity(i);
            finish();
        }

        girisbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = kullaniciadi.getText().toString();
                userPassword = sifre.getText().toString();
                if(userName.isEmpty() || userPassword.isEmpty()){

                    //Kullanıcı Adı ve Şifre alanlarının girilip girilimediği kontrol ediliyor
                    Toast.makeText(getApplicationContext(),"Boş Alan Bırakmayınız!",Toast.LENGTH_SHORT).show();

                }else{

                    Giris();
                }
            }
        });

        kayitbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,KayitSayfasi.class);
                startActivity(intent);
            }
        });
    }
    private void Giris() {

        mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(MainActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //Kayıt başarılı olursa profil sayfasına gidiyoruz
                            Intent i = new Intent(MainActivity.this,ProfilSayfasi.class);
                            startActivity(i);
                            finish();

                        }
                        else{
                            // Eğer Hata ile karşılaşırsak hata mesajını bastırıyoruz
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
}

package com.example.esatgozcu.firebaseauthenticationislemleri2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilSayfasi extends AppCompatActivity {

    private TextView kullaniciaditxt;
    private Button maildegistir;
    private Button sifredegistir;
    private Button cikisyap;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authListener;
    private String str;

    
    @Override
    public void onBackPressed() {
        //Telefondan geri tuşu basıldığında..
        Toast.makeText(ProfilSayfasi.this, "Geri Tuşu Pasif Hale Getirildi", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_sayfasi);

        //FirebaseAuth nesnesini getInstance ile çağırıyoruz
        auth = FirebaseAuth.getInstance();
        //Giriş yapmış kullanıcıları çağırıyoruz
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //Değişiklik yapılıp yapılmadığı dinleniyor
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) { //Değişiklik olduğunuda
                    finish();
                }
            }
        };

        kullaniciaditxt = (TextView) findViewById(R.id.kullaniciaditxt);
        maildegistir = (Button) findViewById(R.id.maildegistir);
        sifredegistir = (Button) findViewById(R.id.sifredegistir);
        cikisyap = (Button) findViewById(R.id.cikisyap);

        //Textview'e giriş yapan kullanıcının mail adresini yazdırıyoruz
        kullaniciaditxt.setText(auth.getCurrentUser().getEmail());

        cikisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutFunc();
            }
        });

        maildegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "Lütfen yeni e-posta adresini giriniz.";
                changeEmailOrPasswordFunc(str, true);
            }
        });

        sifredegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "Lütfen yeni şifreyi giriniz.";
                changeEmailOrPasswordFunc(str, false);
            }
        });



    }
    private void signOutFunc() {

        //Session(oturum) kapatılıyor uygulama yeniden başladığında tekrardan giriş yapılması gerekecek
        auth.signOut();
    }

    private void changeEmailOrPasswordFunc(String title, final boolean option) {

        AlertDialog.Builder builder = new AlertDialog.Builder(
                ProfilSayfasi.this);
        final EditText edit = new EditText(ProfilSayfasi.this);
        builder.setPositiveButton(getString(R.string.change_txt), null);
        builder.setNegativeButton(getString(R.string.close_txt), null);

        //LayoutParams layoutların özelliklerini (en,boy,yazının konumu vb.) kod üzerinde değişiklik yapma imkanı sağlar
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        edit.setLayoutParams(lp);
        //Fonksiyonun ikinci parametresi true veya false olmasına göre textin tipini değiştiriyoruz
        //True ise birinci kısım çalışıyor ve normal yazılar görünür şekilde yazılıyor
        //False ise ikinci kısım çalışıyor ve şifre olduğu için yazılar nokta şeklinde gözükecek
        if (!option) {
            edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        builder.setTitle(title);
        builder.setView(edit);

        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (edit.getText().toString().isEmpty()) {

                            edit.setError("Lütfen ilgili alanı doldurunuz!");

                        } else {

                            //İkinci parametre true ise
                            if (option) {
                                //Mail değişiyor
                                changeEmail();

                            //İkinci parametre false ise
                            } else {
                                //Şifre değişiyor
                                changePassword();

                            }
                        }

                    }
                });
            }

            private void changePassword() {

                //updatePassword methodu ile şifreyi güncelliyoruz
                firebaseUser.updatePassword(edit.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Başarılı ise
                                    Toast.makeText(ProfilSayfasi.this, "Şifre değiştirildi.", Toast.LENGTH_LONG).show();
                                    signOutFunc();
                                } else {
                                    //Başarısız olursa hatayı bastırıyoruz
                                    edit.setText("");
                                    Toast.makeText(ProfilSayfasi.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }

            private void changeEmail() {

                //updateEmail methodu ile mail adresiniz güncelliyoruz
                firebaseUser.updateEmail(edit.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                //Başarılı olursa
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfilSayfasi.this, "E-posta değiştirildi.", Toast.LENGTH_LONG).show();
                                    signOutFunc();

                                } else {
                                    //Başarısız olursa hata mesajını bastırıyoruz
                                    edit.setText("");
                                    Toast.makeText(ProfilSayfasi.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        mAlertDialog.show();
    }
}

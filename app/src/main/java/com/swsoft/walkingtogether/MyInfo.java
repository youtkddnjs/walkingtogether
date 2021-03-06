package com.swsoft.walkingtogether;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;



public class MyInfo extends AppCompatActivity {

    TextView nickname;
    CircleImageView profileImage;
    Button myinfoprofile_imageedit;
    Uri imageURI;

    boolean isFirst = true;
    boolean isChanged = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        Toolbar myinfoToolbar = findViewById(R.id.myinfoToolbar);
        setSupportActionBar(myinfoToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nickname = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.myinfoprofile_image);


        nickname.setText(LoginUserInfo.nickname);
        Glide.with(MyInfo.this).load(LoginUserInfo.profileURL).into(profileImage);

        //????????? ?????? ?????? ??????
        myinfoprofile_imageedit = findViewById(R.id.myinfoprofile_imageedit);
        myinfoprofile_imageedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); //???????????? ?????? ?????????
                resultLauncher.launch(intent);
            }
        }); //????????? ?????? ?????? ??????
        if(KakaoLoginUserInfo.loginway){
            myinfoprofile_imageedit.setClickable(false);
            myinfoprofile_imageedit.setText("????????? ?????????");
        }


        //?????? ?????? ????????? roomlist??? ??????
        Button confirmmyinfo = findViewById(R.id.confirm_myinfo);
        confirmmyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isChanged){
                    saveData();
                    Intent intent = new Intent(getApplicationContext(), RoomList.class);
                    startActivity(intent);
                    finish();

                }else{
                    Intent intent = new Intent(getApplicationContext(), RoomList.class);
                    startActivity(intent);
                    finish();
                }
            }
        }); //????????????



    }//oncreate


    //????????? ????????? ???????????? ???????????? ??????
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            //??????????????? ????????? ???????????? ActivityResult result??? ?????? ??????????????? ????????? Intent??????
            if(result.getResultCode() == RESULT_OK){
                //????????? ???????????? ????????? ????????? Intent ????????????
                Intent intent = result.getData();
                imageURI = intent.getData(); // ????????? ?????????????????? ?????? uri ?????? ?????????

                Glide.with(MyInfo.this).load(imageURI).into(profileImage);
                isChanged = true;
            }
        }
    }); // resultLauncher

    void saveData(){
        if(imageURI == null) return;
        //?????????????????? ??????????????? ???????????? ?????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = "IMG_" + sdf.format(new Date()) + ".png";
        //FirebaseStorage ?????? ?????? ??? ?????? ??????
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        //profileImage ?????? ?????? ?????? ???????????? ?????? ??????????????? ????????????????????? ???????????? ????????????
        StorageReference imgRef =firebaseStorage.getReference("profileImage/"+fileName);
        //?????? ?????????
        UploadTask uploadTask = imgRef.putFile(imageURI);
        //???????????? ???????????? ?????? ?????? ??????
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //???????????? ????????? ???????????? ????????? ????????????
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //firebase ???????????? ???????????? ?????? ???????????? ???????????? ?????? url??? ???????????? ????????????
                        LoginUserInfo.profileURL = uri.toString();

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference profilesRef = firebaseDatabase.getReference("member/"+ LoginUserInfo.id);

                        profilesRef.child("ProFile").setValue(LoginUserInfo.profileURL);

                        //SharedPreferences??? ????????????
                        SharedPreferences pref = getSharedPreferences("account", MODE_PRIVATE);
                        //???????????? ????????? ??????
                        SharedPreferences.Editor editor = pref.edit();
                        
                        //????????????
                        editor.putString("profile", LoginUserInfo.profileURL);

                        //?????? ?????? ??????
                        editor.commit();
                    } //onSuccess
                }); //imgRef.getDownloadUrl().addOnSuccessListener
            } // onSuccess
        }); //????????? ?????????
    } //saveData

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), RoomList.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    } //onPotionsItemSelected


    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RoomList.class);
        startActivity(intent);
        super.onBackPressed();
    } //onBackPressed


} //main

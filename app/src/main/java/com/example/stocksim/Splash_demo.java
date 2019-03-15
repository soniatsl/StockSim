package com.example.stocksim;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;


    public class Splash_demo extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_demo);
            try {
                VideoView videoView = new VideoView(this);
                setContentView(videoView);
                Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome);
                videoView.setVideoURI(path);
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        jump();
                    }
                });
                videoView.start();
            }
            catch (Exception e) {
                jump();
            }
        }


        private void jump() {
            if (isFinishing())
                return;
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


}

package com.spaceshooter.user.shooter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.user.newshooter.R;
import com.spaceshooter.user.shooter.MainActivity;
import com.spaceshooter.user.shooter.SpaceShooterView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FinalActivity extends Activity {
    final String FILENAME = "file";
    MediaPlayer mPlayer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        mPlayer1 = MediaPlayer.create(this, R.raw.end);
        mPlayer1.start();

        TextView tw = (TextView) findViewById(R.id.textView);
        TextView tw1 = (TextView) findViewById(R.id.textView1);
        tw.setText("           "+ SpaceShooterView.score);
        if (readFile() < SpaceShooterView.score)
            writeFile();
        tw1.setText(""+readFile());
        SpaceShooterView.score = 0;
        SpaceShooterView.lives = 3;
    }
    int readFile() {
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            Integer bestScore = null;
            while ((str = br.readLine()) != null) {
                bestScore = Integer.valueOf(str);
                Log.d("Okey", "" + bestScore);
            }
            return bestScore;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    void writeFile() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            // пишем данные
            bw.write(""+SpaceShooterView.score);
            // закрываем поток
            bw.close();
            Log.d("Ok", "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStartButton2(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mPlayer1.stop();
        //this.finish();
    }

}

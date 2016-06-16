package com.example.user.newshooter;

/**
 * Created by user on 6/8/2016.
 */

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

// SpaceInvadersActivity is the entry point to the game.
// It will handle the lifecycle of the game by calling
// methods of spaceInvadersView when prompted to so by the OS.
public class SpaceShooterActivity extends Activity {

    // spaceInvadersView will be the view of the game
    // It will also hold the logic of the game
    // and respond to screen touches as well
    SpaceShooterView spaceShooterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Initialize gameView and set it as the view
        spaceShooterView = new SpaceShooterView(this, size.x, size.y);
        setContentView(spaceShooterView);

    }
    // Вызывается в начале "видимого" состояния.
    @Override
    public void onStart(){
        super.onStart();
        // Примените к UI все необходимые изменения, так как
        // Активность теперь видна на экране.
    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        spaceShooterView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        spaceShooterView.pause();
    }
    @Override
    public void onStop(){
        // "Замораживает" пользовательский интерфейс, потоки
        // или операции, которые могут подождать, пока Активность
        // не отображается на экране. Сохраняйте все введенные
        // данные и изменения в UI так, как будто после вызова
        // этого метода процесс должен быть закрыт.
        super.onStop();
    }

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }
}
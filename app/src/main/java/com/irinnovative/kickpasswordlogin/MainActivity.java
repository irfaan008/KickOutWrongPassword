package com.irinnovative.kickpasswordlogin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/*
    @author Irfan Raza
    @email irfaan.aa@gmail.com
    @website http://irinnovative.com
*/

public class MainActivity extends AppCompatActivity {

    EditText etPassword, etDummy;
    Button btnLogin;
    private LinearLayout llParent;
    private ImageView ivLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etDummy = (EditText) findViewById(R.id.etDummy);
        ivLock = (ImageView) findViewById(R.id.ivLock);
        llParent = (LinearLayout) findViewById(R.id.llParent);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Hide cursor/focus from edit text after click
                removeFocus();

                String password = etPassword.getText().toString();
                etPassword.setText("");

                validatePassword(password);
            }
        });
    }

    private void validatePassword(String password) {
        //  TODO Perform password validation here and if failed then proceed with below

        int passLength = password.length();
        playHangingAnimation(ivLock);

        //  Generate number of dots (imageview) similar to length of input password
        for (int i = 0; i < passLength; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.ic_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(3, 0, 3, 0);
            imageView.setLayoutParams(params);
            llParent.addView(imageView);

            //  Play animation for each and every dots separately
            playKickOutAnimation(imageView, i);
        }
    }

    private void playKickOutAnimation(final View view, int i) {
        //  Parameters are startPositionX,endPositionX, startPositionY, endPositionY
        //  Intentionally changed position of X at runtime based on i(index of dot) to give elastic effect
        //  Play around these numbers and let community know if any combination is giving better result :)
        Animation animation = new TranslateAnimation(-20 + i * 5, 400, 0, 0);

        //  Intentionally changed duration at runtime based on i(index of dot) to give elastic effect
        animation.setDuration(700 + i * 20);

        //  To give kick out effect. Read about all Inter polator here - http://cogitolearning.co.uk/?p=1078
        animation.setInterpolator(new AnticipateInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //  Remove dots from screen once animation has been stopped
                llParent.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private void playHangingAnimation(View v) {
        int pivot = Animation.RELATIVE_TO_SELF;
        //  Parameter defines how many times the cycle should happen
        CycleInterpolator interpolator = new CycleInterpolator(2.5f);
        //  Parameters are fromDegree,toDegree,positionXType(from self in this case),positionX,positionYType,positionY
        //  Play around these values to get to know the behaviour more closely
        RotateAnimation animation = new RotateAnimation(0, 20, pivot, 0.47f, pivot, 0.05f);
        animation.setStartOffset(100);
        animation.setDuration(900);
        animation.setRepeatCount(0);// Animation.INFINITE
        animation.setInterpolator(interpolator);
        v.startAnimation(animation);
    }

    private void removeFocus() {
        etPassword.clearFocus();
        etDummy.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etDummy.getApplicationWindowToken(), 0);
    }
}

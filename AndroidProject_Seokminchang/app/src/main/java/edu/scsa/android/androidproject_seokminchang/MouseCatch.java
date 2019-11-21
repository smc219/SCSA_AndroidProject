package edu.scsa.android.androidproject_seokminchang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MouseCatch extends Activity {
    FrameLayout f;
    FrameLayout.LayoutParams params;
    int score=0;   //잡은 쥐 개수를 저장할 변수
    int delay=1200;  // 게임 속도 조절
    static boolean threadEndFlag=true; // 쓰레드 끄기
    MouseTask mt;				// 쓰레드 구현
    int clearScore = 30;
    int myWidth;  // 내 폰의 너비
    int myHeight; // 내 폰의 높이
    int imgWidth=150;  //그림 크기
    int imgHeight=150;//그림 크기

    //캐릭터 구분용
    public final int MOUSE_ID = 0;
    public final int CAT_ID = 1;
    public final int MOUSE2_ID = 2;

    long starttime;
    int totscore;
    int ototscore;
    Timer to; // 시간 초과시 게임 종료.
    Random r=new Random();  // 이미지 위치를 랜덤하게 발생시킬 객체
    TextView scoreBoard; // 점수 표시
    SoundPool pool;   // 소리
    int liveMouse;    // 소리
    MediaPlayer mp;   // 소리

    int x=200;        //시작위치
    int y=200;        //시작위치
    ImageView[] imgs; // 이미지들을 담아 놓을 배열

    int level=1;      // 게임 레벨
    int nums=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_catch);
        f=(FrameLayout) findViewById(R.id.frame);
        params=new FrameLayout.LayoutParams(1, 1);

        //디스플레이 크기 체크
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myWidth=metrics.widthPixels;
        myHeight=metrics.heightPixels;
        Log.d("game","My Window "+myWidth+" : "+myHeight);

        //사운드 셋팅
        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        liveMouse = pool.load(this, R.raw.mouse_scream, 1);
        mp=MediaPlayer.create(this, R.raw.bgm);
        mp.setLooping(true);

        scoreBoard = findViewById(R.id.scoreBoard);


        init(8);

    }
    public void init(final int nums){
        //초기화
        int cat = (level <= 4) ? level : 4;
        int mouse2 = r.nextInt(nums / 4) + 2;
        Log.e("ranN", "mouse2 : "+mouse2);
        score=0;
        threadEndFlag=true;
        this.nums=nums;
        totscore = ototscore;
        to = new Timer();
        delay=(int)(delay*(10-level)/10.);

        f.removeAllViews();

        f.addView(scoreBoard, 400, 100);
        scoreBoard.setText("STAGE : " + level);

        //이미지 담을 배열 생성과 이미지 담기
        imgs=new ImageView[nums+cat];
        for(int i=0; i<nums-mouse2; i++){
            ImageView iv=new ImageView(this);
            iv.setImageResource(R.drawable.running_mouse_trans);  // 이미지 소스 설정
            f.addView(iv, params);  // 화면에 표시
            imgs[i]=iv;     // 배열에 담기
            iv.setOnClickListener(h);  // 이벤트 등록
            iv.setId(MOUSE_ID);

        }
        for(int i=nums-mouse2; i<nums; i++){
            ImageView iv=new ImageView(this);
            iv.setImageResource(R.drawable.mouse2);  // 이미지 소스 설정
            f.addView(iv, params);  // 화면에 표시
            imgs[i]=iv;     // 배열에 담기
            iv.setOnClickListener(m2Catch);  // 이벤트 등록
            iv.setId(MOUSE2_ID);

        }

        for(int i = nums; i < nums+cat; i++)
        {
            ImageView iv=new ImageView(this);
            iv.setImageResource(R.drawable.cat);  // 이미지 소스 설정
            f.addView(iv, params);  // 화면에 표시
            imgs[i]=iv;     // 배열에 담기
            iv.setOnClickListener(minus);  // 이벤트 등록
            iv.setId(CAT_ID);
        }


        mt=new MouseTask();  //일정 간격으로 이미지 위치를 바꿀 쓰레드 실행
        mt.execute();
        starttime = System.currentTimeMillis();
        to.schedule(getTt(), 5000);
        to.schedule(new leftTime(), 0, 500);



    }
    protected void onResume() {
        super.onResume();
        mp.start();
    };
    protected void onPause() {
        super.onPause();
        mp.stop();
    };

    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        mt.cancel(true);
        threadEndFlag = false;

    };
    View.OnClickListener  h=new View.OnClickListener() {
        public void onClick(View v) {   // 쥐를 잡았을 때
            score+=5;
            totscore += 5;
            ImageView iv=(ImageView)v;


            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            iv.setVisibility(View.INVISIBLE);          // 이미지(쥐) 제거

            Toast.makeText(MouseCatch.this, "Die...."+score, Toast.LENGTH_LONG).show();
            if(score>=clearScore){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);
                to.cancel();
                AlertDialog.Builder dia=new AlertDialog.Builder(MouseCatch.this);
                dia.setMessage("이번 스테이지를 클리어했습니다.\n 다음 스테이지로 넘어가겠습니까?");
                dia.setCancelable(false);
                dia.setPositiveButton("네", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        level++;
                        ototscore = totscore;
                        to.cancel();
                        if(nums < 10) init(nums * 2);
                        if(clearScore < 60) clearScore *= 2;
                        else init(nums);
                    }
                });
                dia.setNegativeButton("아니오", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog d = dia.create();
                d.setCanceledOnTouchOutside(false);
                d.show();


            }

        }
    };
    View.OnClickListener  minus=new View.OnClickListener() {
        public void onClick(View v) {   // 쥐를 잡았을 때
            score-=10;
            totscore -= 10;
            ImageView iv=(ImageView)v;

            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            iv.setVisibility(View.INVISIBLE);          // 이미지(쥐) 제거

            Toast.makeText(MouseCatch.this, "Poor cat...."+score, Toast.LENGTH_LONG).show();
            if(score>=clearScore){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);
                to.cancel();
                AlertDialog.Builder dia=new AlertDialog.Builder(MouseCatch.this);
                dia.setMessage("이번 스테이지를 클리어했습니다.\n 다음 스테이지로 넘어가겠습니까?");
                dia.setPositiveButton("네", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        level++;
                        ototscore = totscore;
                        if(nums < 10) init(nums * 2);
                        if(clearScore < 60) clearScore *= 2;
                        else init(nums);
                    }
                });
                dia.setNegativeButton("아니오", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }

        }
    };

    View.OnClickListener  m2Catch=new View.OnClickListener() {
        public void onClick(View v) {   // 쥐를 잡았을 때
            score+=10;
            totscore += 10;
            ImageView iv=(ImageView)v;

            //pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            iv.setVisibility(View.INVISIBLE);          // 이미지(쥐) 제거

            Toast.makeText(MouseCatch.this, "Die...."+score, Toast.LENGTH_LONG).show();
            if(score>=clearScore){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);
                to.cancel();
                AlertDialog.Builder dia=new AlertDialog.Builder(MouseCatch.this);
                dia.setMessage("이번 스테이지를 클리어했습니다.\n 다음 스테이지로 넘어가겠습니까?");
                dia.setPositiveButton("네", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        level++;
                        ototscore = totscore;
                        if(nums < 10) init(nums * 2);
                        if(clearScore < 60) clearScore *= 2;
                        else init(nums);
                    }
                });
                dia.setNegativeButton("아니오", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }

        }
    };

    // 쥐 위치 이동하여 다시 그리기
    public void update(){
        if(!threadEndFlag) return;
        Log.d("game", "update:");
        for(ImageView img:imgs){
            x=r.nextInt(myWidth-imgWidth);
            y=r.nextInt(myHeight-imgHeight);

            img.layout(x, y, x+imgWidth, y+imgHeight);
            img.invalidate();
        }

    }
    // 일정 시간 간격으로 쥐를 다시 그리도록 update()를 호출하는 쓰레드
    class MouseTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {// 다른 쓰레드
            while(threadEndFlag){
                //다른 쓰레드에서는 UI를 접근할 수 없으므로
                publishProgress();	//자동으로 onProgressUpdate() 가 호출된다.
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            update();
        }
    };//end MouseTask

    // begin timerTask
    public TimerTask getTt() {
        TimerTask fin = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        threadEndFlag=false;
                        to.cancel();
                        mt.cancel(true);
                        AlertDialog.Builder dia=new AlertDialog.Builder(MouseCatch.this);
                        dia.setMessage("Time Over\n 당신의 점수는 " + totscore + "점입니다.");
                        dia.setPositiveButton("다시하기", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                init(nums);
                            }
                        });
                        dia.setNegativeButton("나가기", new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        dia.setCancelable(false);
                        dia.show();

                    }
                });

            }
        };
        return fin;
    }
    class leftTime extends TimerTask {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - starttime;
            int seconds = (int) (millis / 1000);
            //int minutes = seconds / 60;
            //seconds     = seconds % 60;
            Log.e("timerLeft",  "Left Time : " + String.format("%02d", seconds));

        }
    }
    //end timerTask



}// end MainActivity
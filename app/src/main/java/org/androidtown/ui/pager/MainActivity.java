package org.androidtown.ui.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 뷰페이저를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout buttonLayout;
    ImageView[] indexButtons;
    LinearLayout.LayoutParams params;

    Button loginButton;
    Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, Splash.class));

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "로그인 버튼", Toast.LENGTH_LONG).show();

            }
        });

        joinButton = (Button) findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "가입 버튼", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), JoinwithPhoneNumber.class);
                startActivity(intent);
                finish();
            }
        });

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;

        // 뷰페이저 객체를 참조하고 어댑터를 설정합니다.
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





    }


    /**
     * 뷰페이저를 위한 어댑터 정의
     */
    public class ViewPagerAdapter extends PagerAdapter {
        // sample names
        private String[] names = { "John", "Mike", "Sean", "Yang", "Shin"};
        // sample image resource ids
        private int[] resIds = {R.drawable.registration_intro_img_01
                , R.drawable.registration_intro_img_02
                , R.drawable.registration_intro_img_03
                , R.drawable.registration_intro_img_04
                , R.drawable.registration_intro_img_05};

        // sample call numbers
        private String[] callNumbers = {"010-7777-1234"
                , "010-7788-1234"
                , "010-7799-1234"
                , "010-7700-1234"
                , "298374092"};

        /**
         * Context 객체
         */
        private Context mContext;
        private int mCurrentIndex;

        /**
         * 초기화
         *
         * @param context
         */
        public ViewPagerAdapter( Context context ) {
            mContext = context;


            indexButtons = new ImageView[5];
            for(int i = 0; i < 5; i++) {
                indexButtons[i] = new ImageView(mContext);

                if (i == 0) {
                    indexButtons[i].setImageResource(R.drawable.registration_intro_img_paging_on);
                } else {
                    indexButtons[i].setImageResource(R.drawable.registration_intro_img_paging_off);
                }

                indexButtons[i].setPadding(0, 200, 10, 10);
                buttonLayout.addView(indexButtons[i], params);
            }

        }

        /**
         * 페이지 갯수
         */
        public int getCount() {
            return names.length;
            //return 4;
        }

        /**
         * 뷰페이저가 만들어졌을 때 호출됨
         */
        public Object instantiateItem(ViewGroup container, int position) {
            // create a instance of the page and set data
            PersonPage page = new PersonPage(mContext);
            page.setNameText(names[position]);
            page.setImage(resIds[position]);
            //page.setCallNumber(callNumbers[position]);

            // 컨테이너에 추가
            container.addView(page);

            /////////// Added by Devin : start /////////////////
            if(mCurrentIndex > position )
            {
                mCurrentIndex = position + 1;
            }else if(mCurrentIndex < position)
            {
                mCurrentIndex = position - 1;
            }
            updateIndexes();
            /////////// Added by Devin : end /////////////////

            return page;
        }

        /**
         * Called to remove the page
         */
        public void destroyItem(ViewGroup container, int position, Object view) {

            if(mCurrentIndex > position )
            {
                mCurrentIndex = position + 2;
            }else if(mCurrentIndex < position)
            {
                mCurrentIndex = position - 2;
            }
            updateIndexes();

            container.removeView((View)view);
        }

        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }


        private void updateIndexes() {
            for(int i = 0; i < 5; i++) {
                if (i == mCurrentIndex) {
                    indexButtons[i].setImageResource(R.drawable.registration_intro_img_paging_on);
                } else {
                    indexButtons[i].setImageResource(R.drawable.registration_intro_img_paging_off);
                }
            }
        }



    }


}

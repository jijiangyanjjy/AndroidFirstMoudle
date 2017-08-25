package com.zhiyuan3g.androidfirstmoudle;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyuan3g.androidfirstmoudle.db.ProvinceDB;
import com.zhiyuan3g.androidfirstmoudle.entity.ProvinceEntity;
import com.zhiyuan3g.androidfirstmoudle.entity.WeatherEntity;
import com.zhiyuan3g.androidfirstmoudle.fragment.ProvinceFragment;
import com.zhiyuan3g.androidfirstmoudle.utils.ContractUtils;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpCallBack;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpUtils;

import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_bg)
    ImageView imgBg;
    @BindView(R.id.tv_wendu)
    TextView tvWendu;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_two_one)
    TextView tvTwoOne;
    @BindView(R.id.tv_two_two)
    TextView tvTwoTwo;
    @BindView(R.id.tv_two_three)
    TextView tvTwoThree;
    @BindView(R.id.tv_two_four)
    TextView tvTwoFour;
    @BindView(R.id.tv_three_one)
    TextView tvThreeOne;
    @BindView(R.id.tv_three_two)
    TextView tvThreeTwo;
    @BindView(R.id.tv_three_three)
    TextView tvThreeThree;
    @BindView(R.id.tv_three_four)
    TextView tvThreeFour;
    @BindView(R.id.tv_one_one)
    TextView tvOneOne;
    @BindView(R.id.tv_one_two)
    TextView tvOneTwo;
    @BindView(R.id.tv_one_three)
    TextView tvOneThree;
    @BindView(R.id.tv_one_four)
    TextView tvOneFour;

    private FragmentManager fragmentManager;

    private boolean isOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用代码实现沉浸式
        //判断SDK版本号
        if(Build.VERSION.SDK_INT >= 21){
            View dView = getWindow().getDecorView();
            dView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //生成数据库
        Connector.getDatabase();
        //初始化主界面数据
        initView();
        //初始化ToolBar
        initToolBar();
        //给drawerLayout添加打开或关闭监听
        drawerLayout.addDrawerListener(this);
        //根据你的sp表单状态，看看是否需要网络请求
        initSp();
        //请求背景图片
        initTimeSp();
    }

    public void getWeather(WeatherEntity weatherEntity) {

        try {
            tvTitle.setText(weatherEntity.getHeWeather().get(0).getBasic().getCity());
            tvWendu.setText(weatherEntity.getHeWeather().get(0).getNow().getTmp() + "℃");
            tvState.setText(weatherEntity.getHeWeather().get(0).getNow().getCond().getTxt());
            tvOneOne.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(0).getDate());
            tvOneTwo.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(0).getCond().getTxt_d() + "/" + weatherEntity.getHeWeather().get(0).getDaily_forecast().get(0).getCond().getTxt_n());
            tvOneThree.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(0).getTmp().getMax());
            tvOneFour.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(0).getTmp().getMin());

            tvTwoOne.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(1).getDate());
            tvTwoTwo.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(1).getCond().getTxt_d() + "/" + weatherEntity.getHeWeather().get(0).getDaily_forecast().get(1).getCond().getTxt_n());
            tvTwoThree.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(1).getTmp().getMax());
            tvTwoFour.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(1).getTmp().getMin());

            tvThreeOne.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(2).getDate());
            tvThreeTwo.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(2).getCond().getTxt_d() + "/" + weatherEntity.getHeWeather().get(0).getDaily_forecast().get(2).getCond().getTxt_n());
            tvThreeThree.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(2).getTmp().getMax());
            tvThreeFour.setText(weatherEntity.getHeWeather().get(0).getDaily_forecast().get(2).getTmp().getMin());
        } catch (Exception e) {
            e.printStackTrace();
            tvTitle.setText("— —");
            tvWendu.setText("——");
            tvState.setText("——");
        }


    }

    private void initTimeSp() {
        SharedPreferences sp = getSharedPreferences("cool", MODE_PRIVATE);
        String time = sp.getString("time", null);
        String url = sp.getString("url", null);
        initImageBg();
    }

    //获取时间格式
    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        return time;
    }

    private void initImageBg() {
        OkHttpUtils.sendRequestGETMethod(this, ContractUtils.URL_IMAGE, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                SharedPreferences sp = getSharedPreferences("cool", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("url", result);
                ed.putString("time", getTime());
                ed.apply();
                Glide.with(MainActivity.this).load(result).into(imgBg);
            }

            @Override
            public void Failure(String failure) {

            }
        });
    }

    public void close() {
        drawerLayout.closeDrawers();
    }

    private void initSp() {
        SharedPreferences sp = getSharedPreferences("cool", MODE_PRIVATE);
        boolean isOk = sp.getBoolean("isOk", false);
        if (!isOk) {
            initHttp();
        }
    }

    private void initHttp() {
        OkHttpUtils.sendRequestGETMethod(this, ContractUtils.URL_PROVINCE, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                //如果成功存储数据到数据库
                Gson gson = new Gson();
                //通过Gson转换格式
                List<ProvinceEntity> provinceEntityList = gson.fromJson(result, new TypeToken<List<ProvinceEntity>>() {
                }.getType());
                for (ProvinceEntity entity : provinceEntityList) {
                    ProvinceDB provinceDB = new ProvinceDB();
                    provinceDB.setName(entity.getName());
                    provinceDB.setId(entity.getId());
                    //存储数据
                    provinceDB.save();
                }
                //每次加载完数据需要存储SP文件状态
                SharedPreferences sp = getSharedPreferences("cool", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("isOk", true);
                //提交Sp文件
                ed.apply();
            }

            @Override
            public void Failure(String failure) {

            }
        });
    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
    }

    private void initToolBar() {
        //设置ToolBar生效
        setSupportActionBar(toolBar);
        //通过获取ActionBar，设置导航键，隐藏标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    //针对主界面的home按钮添加监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //打开侧滑栏
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    //侧滑栏打开监听（完全打开后才会调用）
    @Override
    public void onDrawerOpened(View drawerView) {

    }

    //侧滑栏关闭监听（完全关闭后才会调用）
    @Override
    public void onDrawerClosed(View drawerView) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, new ProvinceFragment());
        transaction.commit();
        isOk = true;
    }

    //当你的手是拖拽弹出或点击自动弹出时，状态值分别是1和2（0是静止状态又叫闲置状态）
    //当在1和2时，我们应该去加载新的碎片。为了防止再次做无用功，使用boolean值控制流程
    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState > 0 && isOk == true) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, new ProvinceFragment());
            transaction.commit();
            isOk = false;
        }
    }
}

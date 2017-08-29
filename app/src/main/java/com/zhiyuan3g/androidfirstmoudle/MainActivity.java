package com.zhiyuan3g.androidfirstmoudle;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyuan3g.androidfirstmoudle.db.CityDB;
import com.zhiyuan3g.androidfirstmoudle.db.CountryDB;
import com.zhiyuan3g.androidfirstmoudle.db.ProvinceDB;
import com.zhiyuan3g.androidfirstmoudle.db.WeatherDB;
import com.zhiyuan3g.androidfirstmoudle.entity.ProvinceEntity;
import com.zhiyuan3g.androidfirstmoudle.entity.WeatherEntity;
import com.zhiyuan3g.androidfirstmoudle.fragment.ProvinceFragment;
import com.zhiyuan3g.androidfirstmoudle.utils.ContractUtils;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpCallBack;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener, SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.air)
    TextView air;
    @BindView(R.id.comf)
    TextView comf;
    @BindView(R.id.cw)
    TextView cw;
    @BindView(R.id.drsg)
    TextView drsg;
    @BindView(R.id.flu)
    TextView flu;
    @BindView(R.id.sport)
    TextView sport;
    @BindView(R.id.trav)
    TextView trav;
    @BindView(R.id.uv)
    TextView uv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private FragmentManager fragmentManager;

    private boolean isOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用代码实现沉浸式
        //判断SDK版本号
        if (Build.VERSION.SDK_INT >= 21) {
            View dView = getWindow().getDecorView();
            dView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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
        initImageBg();
        //读取数据库缓存天气信息
        initDB();
    }

    private void initDB() {
        List<WeatherDB> all = DataSupport.findAll(WeatherDB.class);
        if (!all.isEmpty()) {
            tvTitle.setText(all.get(0).getTitle());
            tvWendu.setText(all.get(0).getWendu());
            tvState.setText(all.get(0).getState());
            tvOneOne.setText(all.get(0).getOneOne());
            tvOneTwo.setText(all.get(0).getOneTwo());
            tvOneThree.setText(all.get(0).getOneThree());
            tvOneFour.setText(all.get(0).getOneFour());

            tvTwoOne.setText(all.get(0).getTwoOne());
            tvTwoTwo.setText(all.get(0).getTwoTwo());
            tvTwoThree.setText(all.get(0).getTwoThree());
            tvTwoFour.setText(all.get(0).getTwoFour());

            tvThreeOne.setText(all.get(0).getThreeOne());
            tvThreeTwo.setText(all.get(0).getThreeTwo());
            tvThreeThree.setText(all.get(0).getThreeThree());
            tvThreeFour.setText(all.get(0).getThreeFour());

            air.setText("空气指数：" + all.get(0).getAir());
            comf.setText("舒适度指数：" + all.get(0).getComf());
            cw.setText("洗车指数：" + all.get(0).getCw());
            drsg.setText("穿衣指数：" + all.get(0).getDrsg());
            flu.setText("感冒指数：" + all.get(0).getFlu());
            sport.setText("运动指数：" + all.get(0).getSport());
            trav.setText("旅游指数：" + all.get(0).getTrav());
            uv.setText("紫外线指数：" + all.get(0).getUv());
        }
    }

    public void getWeather(WeatherEntity weatherEntity) {
        try {
            DataSupport.deleteAll(WeatherDB.class);

            WeatherDB weatherDB = new WeatherDB();

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

            air.setText("空气指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getAir().getTxt());
            comf.setText("舒适度指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getComf().getTxt());
            cw.setText("洗车指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getCw().getTxt());
            drsg.setText("穿衣指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getDrsg().getTxt());
            flu.setText("感冒指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getFlu().getTxt());
            sport.setText("运动指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getSport().getTxt());
            trav.setText("旅游指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getTrav().getTxt());
            uv.setText("紫外线指数：" + weatherEntity.getHeWeather().get(0).getSuggestion().getUv().getTxt());

            weatherDB.setTitle(tvTitle.getText().toString());
            weatherDB.setWendu(tvWendu.getText().toString());
            weatherDB.setState(tvState.getText().toString());
            weatherDB.setOneOne(tvOneOne.getText().toString());
            weatherDB.setOneTwo(tvOneTwo.getText().toString());
            weatherDB.setOneThree(tvOneThree.getText().toString());
            weatherDB.setOneFour(tvOneFour.getText().toString());
            weatherDB.setTwoOne(tvTwoOne.getText().toString());
            weatherDB.setTwoTwo(tvTwoTwo.getText().toString());
            weatherDB.setTwoThree(tvTwoThree.getText().toString());
            weatherDB.setTwoFour(tvTwoFour.getText().toString());
            weatherDB.setThreeOne(tvThreeOne.getText().toString());
            weatherDB.setThreeTwo(tvThreeTwo.getText().toString());
            weatherDB.setThreeThree(tvThreeThree.getText().toString());
            weatherDB.setThreeFour(tvThreeFour.getText().toString());
            weatherDB.setAir(air.getText().toString());
            weatherDB.setComf(comf.getText().toString());
            weatherDB.setCw(cw.getText().toString());
            weatherDB.setDrsg(drsg.getText().toString());
            weatherDB.setFlu(flu.getText().toString());
            weatherDB.setSport(sport.getText().toString());
            weatherDB.setTrav(trav.getText().toString());
            weatherDB.setUv(uv.getText().toString());
            weatherDB.save();
        } catch (Exception e) {
            e.printStackTrace();
            tvTitle.setText("— —");
            tvWendu.setText("——");
            tvState.setText("——");
        }
    }

    private void initImageBg() {
        SharedPreferences sp = getSharedPreferences("cool", MODE_PRIVATE);
        String url = sp.getString("url", null);
        if (url != null) {
            Glide.with(MainActivity.this).load(url).into(imgBg);
        }
        OkHttpUtils.sendRequestGETMethod(this, ContractUtils.URL_IMAGE, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                SharedPreferences sp = getSharedPreferences("cool", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("url", result);
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
        swipeRefreshLayout.setOnRefreshListener(this);
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

    @Override
    public void onRefresh() {
        if(tvTitle.getText().toString().contains("—")){
            Toast.makeText(this, "请选择一个城市", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }else{
            List<CountryDB> countryDBs = DataSupport.where("name = ?", tvTitle.getText().toString()).find(CountryDB.class);
            String weather_id = countryDBs.get(0).getWeather_id();
            OkHttpUtils.sendRequestGETMethod(this, ContractUtils.URL_WEATHER+weather_id, new OkHttpCallBack() {
                @Override
                public void Success(String result) {
                    Gson gson = new Gson();
                    WeatherEntity weatherEntity = gson.fromJson(result, WeatherEntity.class);
                    getWeather(weatherEntity);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void Failure(String failure) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }
}

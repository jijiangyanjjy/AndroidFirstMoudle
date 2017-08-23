package com.zhiyuan3g.androidfirstmoudle;

import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyuan3g.androidfirstmoudle.db.ProvinceDB;
import com.zhiyuan3g.androidfirstmoudle.entity.ProvinceEntity;
import com.zhiyuan3g.androidfirstmoudle.fragment.ProvinceFragment;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpCallBack;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpUtils;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;

    private boolean isOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void initSp() {
        SharedPreferences sp = getSharedPreferences("cool",MODE_PRIVATE);
        boolean isOk = sp.getBoolean("isOk", false);
        if(!isOk){
            initHttp();
        }
    }

    private void initHttp() {
        OkHttpUtils.sendRequestGETMethod(this, "http://guolin.tech/api/china", new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                //如果成功存储数据到数据库
                Gson gson = new Gson();
                List<ProvinceEntity> provinceEntityList = gson.fromJson(result,new TypeToken<List<ProvinceEntity>>(){}.getType());
                for (ProvinceEntity entity : provinceEntityList){
                    ProvinceDB provinceDB = new ProvinceDB();
                    provinceDB.setName(entity.getName());
                    provinceDB.setId(entity.getId());
                    provinceDB.save();
                }
                SharedPreferences sp = getSharedPreferences("cool",MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putBoolean("isOk",true);
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
        return true;
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

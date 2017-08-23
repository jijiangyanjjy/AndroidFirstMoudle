package com.zhiyuan3g.androidfirstmoudle.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyuan3g.androidfirstmoudle.R;
import com.zhiyuan3g.androidfirstmoudle.adapter.ProvinceAdapter;
import com.zhiyuan3g.androidfirstmoudle.db.ProvinceDB;
import com.zhiyuan3g.androidfirstmoudle.entity.ProvinceEntity;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkkkk on 2017/8/22.
 */

public class ProvinceFragment extends Fragment {
    @BindView(R.id.province_toolBar)
    Toolbar provinceToolBar;
    @BindView(R.id.province_recyclerView)
    RecyclerView provinceRecyclerView;

    private ProvinceAdapter provinceAdapter;
    private List<ProvinceEntity> listProvince;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ButterKnife.bind(this, view);
        //初始化ToolBar
        initToolBar();
        //展示列表
        initView();
        return view;
    }

    private void initView() {
        //通过LitePal查询数据
        List<ProvinceDB> all = DataSupport.findAll(ProvinceDB.class);
        //把数据传到适配器
        provinceAdapter = new ProvinceAdapter(getActivity(),all);
        //创建布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        provinceRecyclerView.setLayoutManager(manager);
        //设置适配器
        provinceRecyclerView.setAdapter(provinceAdapter);
    }

    private void initToolBar() {
        //因为此setSupportActionBar方法只存在Activity中，所以需要强制转换。
        ((AppCompatActivity)getActivity()).setSupportActionBar(provinceToolBar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null){
            //隐藏原有标题栏
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}

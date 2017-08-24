package com.zhiyuan3g.androidfirstmoudle.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhiyuan3g.androidfirstmoudle.MainActivity;
import com.zhiyuan3g.androidfirstmoudle.R;
import com.zhiyuan3g.androidfirstmoudle.adapter.CountryAdapter;
import com.zhiyuan3g.androidfirstmoudle.base.MyApp;
import com.zhiyuan3g.androidfirstmoudle.db.CountryDB;
import com.zhiyuan3g.androidfirstmoudle.db.ProvinceDB;
import com.zhiyuan3g.androidfirstmoudle.utils.ContractUtils;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpCallBack;
import com.zhiyuan3g.androidfirstmoudle.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class CountryFragment extends Fragment {
    @BindView(R.id.tv_province_title)
    TextView tvProvinceTitle;
    @BindView(R.id.province_toolBar)
    Toolbar provinceToolBar;
    @BindView(R.id.province_recyclerView)
    RecyclerView provinceRecyclerView;

    private FragmentManager fragmentManager;
    private int id,provinceId;
    private List<CountryDB> countryDBs;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        provinceId = bundle.getInt("provinceId");
        String name = bundle.getString("name");
        initToolBar(name);
        initFindDB();
        return view;
    }

    private void initFindDB() {
        final List<CountryDB> countryDBs = DataSupport.where("cityCode = ?", String.valueOf(id)).find(CountryDB.class);
        if(countryDBs.isEmpty()){
            initHttp();
        }else{
            CountryAdapter countryAdapter = new CountryAdapter(getActivity(),countryDBs);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            provinceRecyclerView.setLayoutManager(manager);
            provinceRecyclerView.setAdapter(countryAdapter);

            countryAdapter.setOnItemClick(new CountryAdapter.OnItemClick() {
                @Override
                public void itemClick(int position) {
                    ((MainActivity)getActivity()).close();
                    progressInit();
                    //请求接口
                    OkHttpUtils.sendRequestGETMethod(getActivity(), ContractUtils.URL_WEATHER + countryDBs.get(position).getWeather_id(), new OkHttpCallBack() {
                        @Override
                        public void Success(String result) {
                            Gson sGson = new Gson();
                            progressDialog.dismiss();
                        }

                        @Override
                        public void Failure(String failure) {
                            progressDialog.dismiss();
                            Toast.makeText(MyApp.getContext(), failure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void progressInit(){
        //设置dialog标题内容
        progressDialog.setTitle("提示");
        progressDialog.setMessage("请等待...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    private void initHttp() {
        progressInit();

        OkHttpUtils.sendRequestGETMethod(getActivity(), ContractUtils.URL_CITY+provinceId+"/"+id, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                try {
                    //此处使用源生解析，因为Gson会自动生成id，会覆盖数据中的id字段
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        //给实体类赋值
                        CountryDB countryDB = new CountryDB();
                        countryDB.setCityCode(id);
                        countryDB.setName(jsonObject.getString("name"));
                        countryDB.setCountryCode(jsonObject.getInt("id"));
                        countryDB.setWeather_id(jsonObject.getString("weather_id"));
                        countryDB.save();
                        //把这个实体类添加进入集合
                        countryDBs.add(countryDB);
                    }
                    CountryAdapter countryAdapter = new CountryAdapter(getActivity(),countryDBs);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    provinceRecyclerView.setLayoutManager(manager);
                    provinceRecyclerView.setAdapter(countryAdapter);
                    progressDialog.dismiss();

                    countryAdapter.setOnItemClick(new CountryAdapter.OnItemClick() {
                        @Override
                        public void itemClick(int position) {
                            ((MainActivity)getActivity()).close();
                            //请求接口
                            progressInit();
                            OkHttpUtils.sendRequestGETMethod(getActivity(), ContractUtils.URL_WEATHER + countryDBs.get(position).getWeather_id(), new OkHttpCallBack() {
                                @Override
                                public void Success(String result) {
                                    Gson sGson = new Gson();
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void Failure(String failure) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MyApp.getContext(), failure, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(String failure) {
                progressDialog.dismiss();
            }
        });
    }

    private void initToolBar(String name) {
        progressDialog = new ProgressDialog(getActivity());

        countryDBs = new ArrayList<>();
        //这行代码会让你的Fragment中的toolbar生效
        setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();
        //因为此setSupportActionBar方法只存在Activity中，所以需要强制转换。
        ((AppCompatActivity)getActivity()).setSupportActionBar(provinceToolBar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar!=null){
            //隐藏原有标题栏
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvProvinceTitle.setText(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                CityFragment cityFragment = new CityFragment();
                List<ProvinceDB> provinceDBs = DataSupport.where("id = ?", String.valueOf(provinceId)).find(ProvinceDB.class);
                int cityCode = provinceDBs.get(0).getId();
                String name = provinceDBs.get(0).getName();
                Bundle bundle = new Bundle();
                bundle.putInt("id",cityCode);
                bundle.putString("name",name);
                cityFragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container,cityFragment);
                transaction.commit();
                break;
        }
        //return 的值此处为false，因为返回true的话。意外以后的toolbar点击事件不再往下传递。
        return super.onOptionsItemSelected(item);
    }
}

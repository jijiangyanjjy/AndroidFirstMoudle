package com.zhiyuan3g.androidfirstmoudle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuan3g.androidfirstmoudle.R;
import com.zhiyuan3g.androidfirstmoudle.db.CityDB;
import com.zhiyuan3g.androidfirstmoudle.db.ProvinceDB;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kkkkk on 2017/8/23.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{

    private Context context;
    private List<CityDB> cityDBList;

    private OnItemClick onItemClick;

    public CityAdapter(Context context, List<CityDB> cityDBList) {
        this.context = context;
        this.cityDBList = cityDBList;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void itemClick(int position);
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_public_name, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick != null){
                    onItemClick.itemClick(viewHolder.getAdapterPosition());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(cityDBList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cityDBList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_public_name)
        TextView tv_name;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
    }
}

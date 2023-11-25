package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class DiaryFragment extends Fragment {

    static class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
        private List<Diary> mdiarylist;
        SharedPreferences preferences;
        class ViewHolder extends RecyclerView.ViewHolder {
            View diaryView;
            TextView information;
            TextView content;

            public ViewHolder(View view) {
                super(view);
                diaryView=view;
                information = (TextView) view.findViewById(R.id.saying_);
                content = (TextView) view.findViewById(R.id.content_);
            }
        }

        public DiaryAdapter(List<Diary> diaryList) {
            mdiarylist = diaryList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_diary, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.diaryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences=v.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    int position=holder.getAdapterPosition();
                    Diary diary=mdiarylist.get(position);
                    Intent intent=new Intent(v.getContext(),EditDiary.class);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putInt("id_diary", diary.getId());
                    editor.commit();
                    //Toast.makeText(v.getContext(), "你按了"+position, Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(intent);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Diary diary_ = mdiarylist.get(position);
            holder.information.setText(diary_.getTime()+"  "+diary_.getWeather()+"  "+diary_.getMood());
            holder.content.setText(diary_.getContent());
        }

        @Override
        public int getItemCount() {
            return mdiarylist.size();
        }
    }
}


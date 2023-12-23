package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;


public class DiaryFragment extends Fragment {

    static class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {
        private List<Diary> mdiarylist;
        private Diary diary1;
        SharedPreferences preferences;
        public interface ChangeList{
            View Change(View v,ViewGroup p);
        }

        public ChangeList changeList;

        class ViewHolder extends RecyclerView.ViewHolder {
            View diaryView;
            ImageView diary_image;
            TextView information;
            TextView content;

            public ViewHolder(View view) {
                super(view);
                diaryView = view;
                diary_image = view.findViewById(R.id.diary_image);
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
            if (changeList != null) {
                Log.v("6","6");
                view=changeList.Change(view, parent);
            }
            final ViewHolder holder = new ViewHolder(view);
            holder.diaryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences = v.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                    int position = holder.getAdapterPosition();
                    Diary diary = mdiarylist.get(position);
                    Intent intent = new Intent(v.getContext(), EditDiary.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("id_diary", diary.getId());
                    editor.commit();
                    //Toast.makeText(v.getContext(), "你按了"+position, Toast.LENGTH_SHORT).show();
                    v.getContext().startActivity(intent);
                }

            });
            holder.diaryView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition();
                    diary1 = mdiarylist.get(position);
                    showMoodMenu(v);
                    return true;
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Diary diary_ = mdiarylist.get(position);
            holder.information.setText(diary_.getTime() + "  " + diary_.getWeather() + "  " + diary_.getMood());
            holder.content.setText(diary_.getContent());
            if (diary_.getImage_file() != "1") {
                String imagePath = diary_.getImage_file();
//                Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
//                // 设置截取的矩形区域
//                int left = 0;    // 截取区域的左边界
//                int top = 0;     // 截取区域的上边界
//                int width = 1000;   // 截取区域的宽度
//                int height = 800;  // 截取区域的高度
//                Bitmap croppedBitmap = Bitmap.createBitmap(originalBitmap, left, top, width, height);
                File imageFile = new File(imagePath);
                Glide.with(holder.itemView.getContext())
                        .load(imageFile)
                        .transform(new RoundedCorners(100))
//                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(500)))
                        .into(holder.diary_image);
            }
        }

        @Override
        public int getItemCount() {
            return mdiarylist.size();
        }

        private void showMoodMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.delete_menu, popupMenu.getMenu());
            popupMenu.setGravity(Gravity.END);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // 处理菜单项的点击事件
                    int itemId = item.getItemId();
                    if (itemId == R.id.delete) {
//                        diary1.delete();
                        diary1.setIf_delete(1);
                        diary1.save();
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }
}


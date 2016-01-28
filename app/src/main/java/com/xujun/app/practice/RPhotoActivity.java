package com.xujun.app.practice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xujun.app.adapter.MenuItemAdapter;
import com.xujun.app.model.PhotoEntity;
import com.xujun.app.widget.MenuPopupWindow;
import com.xujun.util.DateUtil;
import com.xujun.util.ImageUtils;
import com.xujun.util.L;
import com.xujun.util.URLs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xujunwu on 15/9/6.
 */
public class RPhotoActivity extends BaseActivity{

    @ViewInject(R.id.llHeader)
    private LinearLayout    mHeader;

    @ViewInject(R.id.gridview)
    private GridView mListView;

    private ItemAdapter mAdapter;

    private List<PhotoEntity>   items=new ArrayList<PhotoEntity>();

    private MenuPopupWindow mMenuPopupWindow;

    private BitmapUtils         bitmapUtils;

    /***
     * 弹出菜单事作
     */
    private AdapterView.OnItemClickListener mMenuItemListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_list);
        ViewUtils.inject(this);

        bitmapUtils=new BitmapUtils(this);

        mHeadTitle.setText(getText(R.string.resume_head_title_2));
        mHeadBtnRight.setText(getText(R.string.btn_add));
        mHeadBtnRight.setOnClickListener(this);
        initHeadBackView();
        hideSearchEditView();
        initHeadView();

    }

    private void initHeadView(){
//        View head=getLayoutInflater().inflate(R.layout.head_r_photo,null);
//        head.findViewById(R.id.btnCamera).setOnClickListener(this);
//        head.findViewById(R.id.btnUpload).setOnClickListener(this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 0);
//        mHeader.addView(head, lp);
        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
//        mListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,false,true));
    }

    private void showMenuPopupWindow(){
        mMenuPopupWindow=new MenuPopupWindow(this);
        mMenuPopupWindow.showAsDropDown(mHeadBtnRight);
        mMenuPopupWindow.getListView().setAdapter(new MenuItemAdapter(mContext, AppConfig.getAppConfig(mContext).getPhotoMenu()));
        mMenuPopupWindow.getListView().setOnItemClickListener(mMenuItemListener);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppConfig.REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(ImageMSelectActivity.EXTRA_RESULT);
                // do your logic ....
                L.e("=======>"+path.toString());
                int i=0;
                for (String str :path){
                    i++;
                    PhotoEntity entity=new PhotoEntity();
                    entity.setFilePath(str);
                    entity.setFileName(DateUtil.getFileNameForDate() + "-" + i);
                    entity.setStatus(0);
                    entity.setIsUpload(0);
                    insertPhotoEntity(entity);
                }
                loadData();
            }
        }
    }

    private void insertPhotoEntity(PhotoEntity entity){
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            db.save(entity);
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnCamera:{
                break;
            }
            case R.id.btnUpload:{

                break;
            }
            case R.id.btnHeadRight:{
                Intent intent = new Intent(mContext, ImageMSelectActivity.class);
                // whether show camera
                intent.putExtra(ImageMSelectActivity.EXTRA_SHOW_CAMERA, true);
                // max select image amount
                intent.putExtra(ImageMSelectActivity.EXTRA_SELECT_COUNT, 9);
                // select mode (ImageMSelectActivity.MODE_SINGLE OR ImageMSelectActivity.MODE_MULTI)
                intent.putExtra(ImageMSelectActivity.EXTRA_SELECT_MODE, ImageMSelectActivity.MODE_MULTI);
                startActivityForResult(intent, AppConfig.REQUEST_IMAGE);
                break;
            }
            default:
                super.onClick(view);
        }
    }

    @Override
    public void loadData() {
        try{
            DbUtils db=DbUtils.create(this,AppConfig.DB_NAME);
            List<PhotoEntity> list=db.findAll(PhotoEntity.class);
            if (list!=null){
                items.clear();
                items.addAll(list);
            }
            mAdapter.notifyDataSetChanged();
        }catch (DbException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parserHttpResponse(String result) {

    }

    class ItemAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemView    holder;
            if (convertView==null){
                holder=new ItemView();
                convertView=View.inflate(mContext,R.layout.item_r_photo,null);
                holder.icon=(ImageView)convertView.findViewById(R.id.ivIcon);
                holder.time=(TextView)convertView.findViewById(R.id.tvTime);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            PhotoEntity entity=items.get(position);
            if (entity.getStatus()==0){
                bitmapUtils.display(holder.icon,entity.getFilePath());
            }else{
                bitmapUtils.display(holder.icon, URLs.IMAGE_URL+entity.getImageUrl());
            }
            if (entity.getFileName()!=null){
                holder.time.setText(entity.getFileName());
            }
            return convertView;
        }
    }

    private class ItemView {
        ImageView       icon;
        TextView        time;
    }
}

package com.xujun.app.practice;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.xujun.app.model.InputInfo;
import com.xujun.app.model.InputList;
import com.xujun.app.model.ResumeLife;
import com.xujun.util.JsonUtil;
import com.xujun.util.StringUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujunwu on 15/10/8.
 */
public class MyActivity extends BaseActivity{

    @ViewInject(R.id.list)
    private ListView        mListView;

    private ItemAdapter         mAdapter;

    private List<InputInfo>     items=new ArrayList<InputInfo>();

    private boolean isEdit=false;

    private ViewHolder mEditHolder;
    private DialogPlus mDialog;
    private TextView     tvEditTitle;
    private FormEditText etContent;


    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            InputInfo   item=items.get(i);
            if (item!=null&&isEdit){
                if (item.getId()==1){

                }else {
                    showEditDialog(item);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_list);
        ViewUtils.inject(this);
        mHeadTitle.setText(getText(R.string.tab_my));

        initHeadBackView();
        hideSearchEditView();
        mHeadBtnRight.setText(getText(R.string.btn_edit));
        mHeadBtnRight.setOnClickListener(this);
        init();
    }

    private void init(){
        mAdapter=new ItemAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(onItemClickListener);
        View view=getLayoutInflater().inflate(R.layout.edit_single_content,null);
        mEditHolder=new ViewHolder(view);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        etContent=(FormEditText)view.findViewById(R.id.etContent);
        tvEditTitle=(TextView)view.findViewById(R.id.tvTitle);
    }

    OnClickListener clickDoneListener=new OnClickListener() {
        @Override
        public void onClick(DialogPlus dialog, View view) {
            switch (view.getId()){
                case R.id.btnSave:{
                    if (etContent!=null&&etContent.getText().toString()!=null){
                    }
                    dialog.dismiss();
                    break;
                }
            }
        }
    };

    private void showEditDialog(InputInfo info){
        mDialog=DialogPlus.newDialog(this).setContentHolder(mEditHolder).setCancelable(false)
                .setGravity(Gravity.CENTER).setOnClickListener(clickDoneListener)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT).create();
        tvEditTitle.setText(info.getTitle());
        etContent.setText("");
        etContent.setHint("请输入"+info.getTitle());
        etContent.setTag(info);
        mDialog.show();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnHeadRight:{
                isEdit=!isEdit;
                if (isEdit){
                    showCroutonMessage("点击可编辑");
                    mHeadBtnRight.setText(getString(R.string.btn_done));
                }else{
                    mHeadBtnRight.setText(getString(R.string.btn_edit));
                }
                break;
            }
            default:{
                super.onClick(view);
            }
        }
    }

    @Override
    public void loadData() {
        try {
            InputList list=(InputList)JsonUtil.ObjFromJson(getFromAssets("my_info.json"),InputList.class);
            if (list!=null&&list.getRoot()!=null){
                items.clear();
                items.addAll(list.getRoot());
            }
            mAdapter.notifyDataSetChanged();;
        }catch (Exception e){
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
                convertView=View.inflate(mContext,R.layout.item_input,null);
                holder.linearLayoutAvatar=(LinearLayout)convertView.findViewById(R.id.avatar);
                holder.linearLayout=(LinearLayout)convertView.findViewById(R.id.content);
                holder.empty=convertView.findViewById(R.id.empty);
                holder.avatar=(ImageView)convertView.findViewById(R.id.ivIcon);
                holder.title=(TextView)convertView.findViewById(R.id.tvItemTitle);
                holder.value=(TextView)convertView.findViewById(R.id.tvItemValue);
                convertView.setTag(holder);
            }else{
                holder=(ItemView)convertView.getTag();
            }
            holder.empty.setVisibility(View.GONE);
            holder.linearLayoutAvatar.setVisibility(View.GONE);
            holder.linearLayout.setVisibility(View.VISIBLE);
            InputInfo item=items.get(position);
            if (item!=null){
                holder.title.setText(item.getTitle());
                if(item.getType()==10){
                    holder.empty.setVisibility(View.VISIBLE);
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.linearLayoutAvatar.setVisibility(View.GONE);
                }
                if (item.getType()==1){
                    holder.linearLayout.setVisibility(View.GONE);
                    holder.linearLayoutAvatar.setVisibility(View.VISIBLE);
                    holder.empty.setVisibility(View.GONE);
                }
                if (mMember!=null){
                    if (item.getValue().equals("mobile")&& !StringUtil.isEmpty(mMember.getMobile())){
                        holder.value.setText(mMember.getMobile());
                    }
                    if (item.getValue().equals("email")&& !StringUtil.isEmpty(mMember.getEmail())){
                        holder.value.setText(mMember.getEmail());
                    }
                }
            }
            return convertView;
        }
    }

    private class ItemView {
        LinearLayout linearLayoutAvatar;
        LinearLayout linearLayout;
        ImageView       avatar;
        View            empty;
        TextView        title;
        TextView        value;
    }
}

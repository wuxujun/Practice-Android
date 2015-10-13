package com.xujun.app.practice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.xujun.multiimage.MultiImageSelectorFragment;
import com.xujun.app.practice.R;
import com.xujun.util.SystemBarTintManager;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by xujunwu on 15/10/2.
 */
public class ImageMSelectActivity extends SherlockFragmentActivity implements MultiImageSelectorFragment.Callback {

    /** 最大图片选择次数，int类型，默认9 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多选 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显示 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private View            actionbarLayout;

    private Button mSubmitButton;
    private int mDefaultCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_image_multi);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            SystemBarTintManager tintManager=new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.action_bar_color);
        }

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if(mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);

        actionbarLayout= LayoutInflater.from(this).inflate(R.layout.default_actionbar,null);
        actionbarLayout.findViewById(R.id.llHeadSearch).setVisibility(View.GONE);
        actionbarLayout.findViewById(R.id.tvHeadTitle).setVisibility(View.VISIBLE);
        ((TextView)actionbarLayout.findViewById(R.id.tvHeadTitle)).setText("照片选择");
        getActionBar().setCustomView(actionbarLayout);


        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, SherlockFragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();

        // 返回按钮
        actionbarLayout.findViewById(R.id.ibHeadBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) actionbarLayout.findViewById(R.id.btnHeadRight);
        if(resultList == null || resultList.size()<=0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }else{
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            mSubmitButton.setEnabled(true);
        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultList != null && resultList.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }


    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if(resultList.size() > 0){
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
        }else{
            mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
        }
        // 当为选择图片时候的状态
        if(resultList.size() == 0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if(imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}

package com.xujun.util;

import android.app.Activity;
import android.content.Intent;

import com.xujun.app.practice.MRequestActivity;
import com.xujun.app.practice.MyActivity;
import com.xujun.app.practice.RHonorActivity;
import com.xujun.app.practice.RInfoActivity;
import com.xujun.app.practice.RLangActivity;
import com.xujun.app.practice.RLifeActivity;
import com.xujun.app.practice.RPhotoActivity;
import com.xujun.app.practice.RWorkActivity;
import com.xujun.app.practice.ShakeActivity;

/**
 * Created by xujunwu on 15/4/20.
 */
public class UIHelper {

    public static void refreshActionBarMenu(Activity activity){
        activity.invalidateOptionsMenu();
    }

    public static void openShake(Activity activity){
        Intent intent = new Intent(activity, ShakeActivity.class);
        activity.startActivity(intent);
    }

    public static void openResumeLife(Activity activity){
        Intent intent = new Intent(activity, RLifeActivity.class);
        activity.startActivity(intent);
    }

    public static void openResumeWork(Activity activity){
        Intent intent = new Intent(activity, RWorkActivity.class);
        activity.startActivity(intent);
    }

    public static void openResumeInfo(Activity activity){
        Intent intent = new Intent(activity, RInfoActivity.class);
        activity.startActivity(intent);
    }

    public static void openResumeHonor(Activity activity){
        Intent intent = new Intent(activity, RHonorActivity.class);
        activity.startActivity(intent);
    }

    public static void openResumePhoto(Activity activity){
        Intent intent = new Intent(activity, RPhotoActivity.class);
        activity.startActivity(intent);
    }

    public static void openResumeLang(Activity activity){
        Intent intent=new Intent(activity, RLangActivity.class);
        activity.startActivity(intent);
    }

    public static void openMy(Activity activity){
        Intent intent=new Intent(activity, MyActivity.class);
        activity.startActivity(intent);
    }

    public static void openMyRequest(Activity activity){
        Intent intent=new Intent(activity, MRequestActivity.class);
        activity.startActivity(intent);
    }
}

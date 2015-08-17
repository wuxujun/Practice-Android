package com.xujun.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xujunwu on 15/2/10.
 */
public class AppUtil {

    public static boolean checkEmail(String email) {
        try {
            String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if (isMatched) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobilePhoneNumber(String number) {
        String regx = "^(13[0-9]|15[0-9]|18[0-9]|14[5|7])\\d{8}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(number);
        return matcher.find();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || str.trim().equals("null");
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static void showLongMessage(Context mContext, CharSequence text) {
        if (text != null && text.length() > 0) {
            Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
        }
    }

    public static void showShortMessage(Context mContext, CharSequence text) {
        if (text != null && text.length() > 0) {
            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float desWidth, float desHeight) {
        if (bitmap == null) {
            return null;
        }
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        if (bmpWidth > desWidth || bmpHeight > desHeight) {
            Matrix matrix = new Matrix();

            float scalFactor = Math.min(desWidth / bmpWidth, desHeight / bmpHeight);
            matrix.postScale(scalFactor, scalFactor);

            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
            bitmap.recycle();
            bitmap = resizeBitmap;
        }
        return bitmap;
    }

    public static String TimeStampToString(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sdf.format(new Date(timeStamp));
        return date;
    }

    /**
     * 在屏幕中央显示loading
     */
    public static ProgressDialog showProgress(Activity activity) {
        ProgressDialog window = showProgress(activity, "  请稍候...");
        return window;
    }

    public static ProgressDialog showProgress(Activity activity, String hintText) {
        Activity mActivity = null;
        if (activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);

        // 默认可取消的模式，并在取消之时�?知用户，线程仍在后台加载�?
        window.setCancelable(true);
        window.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                AppUtil.showShortMessage(finalActivity, "自动转入后台加载");
            }
        });
        return window;
    }

    // 根据内容，自动设置ListView的高度，解决ScrollView和ListView的滚动冲突问题
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }

    public static String getRealPathFromURI(Uri uri, Activity activity) {
        String path = null;
        if (uri.getScheme().equals("content")) {
            String[] proj = { MediaStore.MediaColumns.DATA };
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } else {
            path = uri.getPath();
        }
        return path;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }

    public static void printLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void printLog(String msg) {
        Log.e("test", msg);
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 判断包含中文
    public static boolean isChineseStr(String pValue) {
        for (int i = 0; i < pValue.length(); i++) {
            if ((int) pValue.charAt(i) > 256)
                return true;
        }
        return false;
    }

    public static long getDirectorySize(File file, long totalSize) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    totalSize += files[i].length();
                } else {
                    getDirectorySize(files[i], totalSize);
                }
            }
        } else {
            totalSize += file.length();
        }
        return totalSize;
    }

    public static void deleteFileInDirectory(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                } else {
                    deleteFileInDirectory(files[i]);
                }
            }
        } else {
            file.delete();
        }
    }

    public static String displayFileSize(long fileSize) {
        // 取小数点后两位，注意除的是100.0，如是100则无法取得预期效果
        double num = 0;
        if (fileSize < 1024) {
            return fileSize + "B";
        } else if (fileSize < 1024 * 1024 && fileSize > 1024) {
            num = (int) (fileSize * 100 / 1024) / 100.0;
            return num + "KB";
        } else if (fileSize > 1024 * 1024) {
            num = (int) (fileSize * 100 / (1024 * 1024)) / 100.0;
            return num + "M";
        }
        return "";
    }

    public static Activity getActivityContext(Activity activity) {
        if (activity.getParent() != null) {
            if (activity.getParent().getParent() != null)
                return activity.getParent().getParent();
            return activity.getParent();
        } else {
            return activity;
        }
    }


    /**
     * 获得SD卡根目录
     */
    public static String getSDPath() {
        if (isSDPresent()) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 应用程序文件在SD卡中的根目录
     */
    public static String getAppSDPath() {
        String path = getSDPath() + "/yoca/";
        File tmp = new File(path);
        if(!tmp.exists())
            tmp.mkdirs();
        return tmp.getAbsolutePath();
    }

    /**
     * sd卡中的cache目录
     */
    public static String getAppSDCacheDir() {
        String outfileDir = getAppSDPath() + File.separator + "cache";
        // 如果不存在，则建立个目录
        File tmp = new File(outfileDir);
        if (!tmp.exists()) {
            tmp.mkdirs();
        }
        return tmp.getAbsolutePath();
    }

    /**
     * 检测SD卡状态
     */
    public static boolean isSDPresent() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 检测WIFI情况
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获得IP地址
     */
    public String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            AppUtil.printLog("ip error", ex.toString());
        }
        return "";
    }

    /**
     * 检测是否联网
     */
    public static boolean isNetwork(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isAvailable()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity)
    {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }



    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal)
    {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }


    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable()
    {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize()
    {
        if (isSDCardEnable())
        {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath)
    {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath()))
        {
            filePath = getSDCardPath();
        } else
        {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}

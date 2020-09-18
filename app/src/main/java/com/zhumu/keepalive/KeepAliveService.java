package com.zhumu.keepalive;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeepAliveService extends Service {
    ScheduledExecutorService executors = Executors.newSingleThreadScheduledExecutor();
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        executors.schedule(new Runnable() {
            @Override
            public void run() {
                getProcessInfo(KeepAliveService.this);
            }
        }, 5000, TimeUnit.MILLISECONDS);

    }

    /**
     * 获取正在运行的进程信息
     *
     * @return
     */
    public static ArrayList<ProcessInfo> getProcessInfo(Context context) {
        // 存放全部信息
        ArrayList<ProcessInfo> infos = new ArrayList<ProcessInfo>();


        // 获取活动管理器
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);


        PackageManager packageManager = context.getPackageManager();


        // 获取正在运行的进程信息
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am
                .getRunningAppProcesses();
        // 遍历所有 进程
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            // 进程名字 一般和包名一样
            String processName = runningAppProcessInfo.processName;
            // 进程的id
            int pid = runningAppProcessInfo.pid;


            Drawable icon = null;
            String name = null;
            long memSize = 0;
            boolean isSys = false;
            try {
                // 获取一个应用的ApplicationInfo 对象
                ApplicationInfo applicationInfo = packageManager
                        .getApplicationInfo(processName, 0);
                icon = applicationInfo.loadIcon(packageManager);// 图片
                name = applicationInfo.loadLabel(packageManager).toString();// 名字


                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    // 是系统进程
                    isSys = true;
                } else {
                    // 是用户进程
                    isSys = false;
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                // 包名:am
                // 系统进程
                // 给一些默认值
                name = processName;
                icon = context.getResources().getDrawable(
                        R.mipmap.ic_launcher);
                // 认为是系统进程
                isSys = true;
            }


            int[] pids = new int[]{pid};
            // 获取指定pid的进程内存信息 这里可以获取多个
            android.os.Debug.MemoryInfo[] processMemoryInfo = am
                    .getProcessMemoryInfo(pids);
            // 获取内存大小
            memSize = processMemoryInfo[0].getTotalPss() * 1024;


            ProcessInfo info = new ProcessInfo(name, memSize, isSys, processName);


            infos.add(info);
        }


//        String EQUIPMENT_MASTER_PACKAGE = "com.sgcc.pda.mdrh.task.eos";//设备主人制
//        String EQUIPMENT_MASTER_ACTIVITY = EQUIPMENT_MASTER_PACKAGE + ".ui.ac.MainActivity";//设备主人制
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        ComponentName cn = new ComponentName("com.facecompare.zhumu", "com.facecompare.zhumu.main.compare.MainActivity");
//        intent.setComponent(cn);
//        context.startActivity(intent);

//        Intent intent = new Intent();
//        intent.setClassName(packageName, activityName);
//        mContext.startActivity(intent);
        return infos;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }
}
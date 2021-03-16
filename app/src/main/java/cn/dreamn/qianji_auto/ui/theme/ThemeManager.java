package cn.dreamn.qianji_auto.ui.theme;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.tencent.mmkv.MMKV;
import com.zhengsr.skinlib.ZSkin;
import com.zhengsr.skinlib.utils.ZUtils;

import java.io.File;

import cn.dreamn.qianji_auto.ui.utils.StatusBarUtil;
import cn.dreamn.qianji_auto.ui.views.IconView;
import cn.dreamn.qianji_auto.ui.views.IconViewDelegate;
import cn.dreamn.qianji_auto.ui.views.SuperText;
import cn.dreamn.qianji_auto.ui.views.SuperTextDelegate;

public class ThemeManager {
    private Context mContext;
    private MMKV mmkv;
    public ThemeManager(Context context){
        mContext=context;
        mmkv=MMKV.defaultMMKV();
    }
    public static void init(){
        ZSkin.addDelegate(IconView.class,new IconViewDelegate());
        ZSkin.addDelegate(SuperText.class,new SuperTextDelegate());
    }
    public void setTheme(){
        replaceInApp(mmkv.getString("theme","default"));
    }

    private void replace(String skinName){
        if(skinName.equals("default"))return;
        String path =  mContext.getFilesDir().getAbsolutePath();
        String name = skinName+".skin";
        String assetName = "skin/"+skinName+".skin";
//直接改变了
        File file = new File(path,name);
        //如果不存在，则从 assets copy 过去
        if (!file.exists()) {
            ZUtils.copyAssetFileToStorage(mContext,assetName,path,name);
        }

        //立刻加载
        ZSkin.loadSkin(file.getAbsolutePath());
    }
    private void replaceInApp(String skinName){
        if(skinName.equals("default"))return;
        //立刻加载
        ZSkin.loadSkinByPrefix(skinName);
    }

    public void setStatusBar(Activity activity, View view, int color){
        int color2=getColor(activity,color);
        if(isLightColor(color2)){
            StatusBarUtil.setDarkMode(activity);
        }else{
            StatusBarUtil.setLightMode(activity);
        }

        view.setBackgroundColor(color2);
        StatusBarUtil.setPaddingTop(mContext,view);
        StatusBarUtil.setColor(activity,color2);
    }

    public static int getColor(Context activity,int Color){
        if (ZSkin.isLoadSkin()){
            Color = ZSkin.getColor(Color);
        }
        return activity.getColor(Color);
    }
    public boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        // It's a dark color
        return darkness < 0.5; // It's a light color（true)
    }
}
package com.example.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import pickerview.TimePickerBuilder;

import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import pickerview.TimePickerView;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Pickerview extends ReactContextBaseJavaModule {
    //    public static String maxDate = "";
//    public static String minDate = "";
//    public static TimePickerPopWin timePickerPopWin;
    public static String Datetype = "";
    private final ReactApplicationContext _reactContext;
    public static Date minDate = null;
    public static Date maxDate = null;
    public static Context context;

    public Pickerview(ReactApplicationContext reactContext) {
        super(reactContext);
        _reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "DatePicker";
    }

//    @ReactMethod
//    public void openDate(String data, final Promise promise) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String datachoose = sdf.format(new Date());
//        int hour = Calendar.getInstance().get(Calendar.HOUR) - 1;
//        int minute = Calendar.getInstance().get(Calendar.MINUTE);
//
//        try {
//            this.type = "date";
//            JSONObject object = new JSONObject(data);
//            String type = object.optString("type", "");
//
//            if (type.equals("datetime")) {
//                this.type = type;
//                int date_hour = object.optInt("hour", -1);
//                int date_minute = object.optInt("minute", -1);
//                if (date_hour != -1) {
//                    hour = date_hour;
//                }
//                if (date_minute != -1) {
//                    minute = date_minute;
//                }
//            }
//            if (type.equals("time")) {
//                openTime(data, promise);
//                return;
//            }
//            String passdate = object.optString("date");
//            if (passdate != null && passdate != "") {
//                datachoose = passdate.substring(0, 10);
//            }
//            //最大值
//            String maxDate = object.optString("maxDate", "");
//            Pickerview.maxDate = maxDate.substring(0, 10);
//            //最小值
//            String minDate = object.optString("minDate", "");
//            Pickerview.minDate = minDate.substring(0, 10);
//
//        } catch (Exception e) {
//        }
//
//    }

    public static Date getW3cTimeConvertString2Date(String date, String timeZone) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINESE);
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date parse = format.parse(date);
        return parse;
    }

    @ReactMethod
    public void openDate(String data, final Promise promise) {
        Log.i("pvTime", data);
        context = getCurrentActivity();
        try {
            JSONObject object = new JSONObject(data);
            //显示类型
            String type = object.optString("type", "");
            Datetype = type;
            boolean[] timeType = new boolean[]{true, true, true, true, true, false};
            if (type.equals("date")) {
                timeType = new boolean[]{true, true, true, false, false, false};
            }
            if (type.equals("time")) {
                timeType = new boolean[]{false, false, false, true, true, false};
            }
            //初始时间
            Calendar calendar = null;
            String initdate = object.optString("date", "null");
            if (!initdate.equals("null") && initdate != null) {
                SimpleDateFormat sdf = null;
                if (type.equals("date")) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }
                if (type.equals("datetime")) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                }
                if (sdf != null) {
                    Date date = sdf.parse(initdate);
                    calendar = Calendar.getInstance();
                    calendar.setTime(date);
                }
            }
            //最大,最小值
            minDate = null;
            maxDate = null;
            String initMin = object.optString("minDate", "null");
            String initMax = object.optString("maxDate", "null");

            try {
                if (!initMin.equals("null") && initMin != null) {
                    minDate = getW3cTimeConvertString2Date(initMin, "UTC");
                }
                if (!initMax.equals("null") && initMax != null) {
                    maxDate = getW3cTimeConvertString2Date(initMax, "UTC");
                }
            } catch (Exception e) {

            }


            //时间选择器
            TimePickerView pvTime = new TimePickerBuilder(getCurrentActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
//                    Log.i("pvTime", date.toString());
                    try {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        JSONObject data = new JSONObject();
                        data.put("year", cal.get(Calendar.YEAR));
                        data.put("month", cal.get(Calendar.MONTH));
                        data.put("day", cal.get(Calendar.DATE));
                        data.put("hour", cal.get(Calendar.HOUR_OF_DAY));
                        data.put("minute", cal.get(Calendar.MINUTE));
                        data.put("action", 200);
                        promise.resolve(data.toString());
                    } catch (Exception e) {
                    }
                }
            }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                @Override
                public void onTimeSelectChanged(Date date) {
                    Log.i("pvTime", "onTimeSelectChanged");
                }
            })
                    .setType(timeType)
                    .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitText("完成")//确认按钮文字
                    .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                    .build();

            Dialog mDialog = pvTime.getDialog();
            if (mDialog != null) {

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM);

                params.leftMargin = 0;
                params.rightMargin = 0;
                pvTime.getDialogContainerLayout().setLayoutParams(params);

                Window dialogWindow = mDialog.getWindow();
                if (dialogWindow != null) {
                    dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                    dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                }
            }
            if (calendar != null) {
                pvTime.setDate(calendar);
            }
            pvTime.show();

        } catch (Exception e) {

        }
    }
}

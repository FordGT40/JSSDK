package com.wisdom.jsinterfacelib.handler;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.smallbuer.jsbridge.core.BridgeHandler;
import com.smallbuer.jsbridge.core.CallBackFunction;
import com.wisdom.jsinterfacelib.model.BaseModel;
import com.wisdom.jsinterfacelib.model.ContactModel;
import com.wisdom.jsinterfacelib.utils.avoidonresult.AvoidOnResult;

import java.util.List;

/**
 *
 * 打开通讯录
 */
public class OpenContactHandler extends BridgeHandler {
    @Override
    public void handler(final Context context, String data, final CallBackFunction function) {
        LogUtils.i("接到的json:" + data);
        try {
            final FragmentActivity activity = ((AppCompatActivity) context);
            //检查是否具有权限
            PermissionX.init(activity)
                    .permissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request(new RequestCallback() {
                        @Override
                        public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                            BaseModel baseModel = null;
                            if (allGranted) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.PICK");
                                intent.addCategory("android.intent.category.DEFAULT");
                                intent.setType("vnd.android.cursor.dir/phone_v2");
                                //打开系统通讯录，并监听回调
                                new AvoidOnResult(activity)
                                        .startForResult(intent, new AvoidOnResult.Callback() {
                                            @Override
                                            public void onActivityResult(int resultCode, Intent data) {
                                                ContactModel contactModel = getContactContent(context, resultCode, data);
                                                BaseModel baseModel;
                                                if (contactModel != null) {
                                                    baseModel = new BaseModel("通讯录获取成功", 0, contactModel);
                                                } else {
                                                    baseModel = new BaseModel("通讯录获取失败", -1, contactModel);
                                                }
                                                function.onCallBack(GsonUtils.toJson(baseModel));
                                            }
                                        });
                            } else {
                                //权限未通过
                                baseModel = new BaseModel("通讯录获取失败", -1, "用户权限被拒");
                                function.onCallBack(GsonUtils.toJson(baseModel));
                            }

                        }
                    });
        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    /**
     * 根据通讯录返回的内容解析姓名和电话
     *
     * @param data
     * @return
     */
    private ContactModel getContactContent(Context context, int resultCode, Intent data) {
        ContactModel contactModel = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri,
                            new String[]{"display_name", "data1"}, null, null, null);
                }
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                }
                cursor.close();
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum = phoneNum.replaceAll(" ", "");
                    contactModel = new ContactModel();
                    contactModel.setName(contactName);
                    contactModel.setPhoneNum(phoneNum);
                }
            }

        }
        return contactModel;
    }

}

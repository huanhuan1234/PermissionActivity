package hhh.bawei.com.permissionactivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Huangminghuan on 2017/6/23.
 */
//处理权限问题
@RuntimePermissions
public class PermissionActivity1 extends Activity {

    @BindView(R.id.btn_camear)
    Button btnCamear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premission_activity);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_camear)
    public void onClick() {



        PermissionActivity1PermissionsDispatcher.tocamWithCheck(this);



    }



    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onDenied(){

        Toast.makeText(this, "onDenied", Toast.LENGTH_SHORT).show();

    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void onNeverAskAgain(){
        Toast.makeText(this, "onNeverAskAgain", Toast.LENGTH_SHORT).show();

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);

    }



    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(final PermissionRequest request){


        new AlertDialog.Builder(this).setTitle("title")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // 请求授权

                        request.proceed();

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                request.cancel();
            }
        }).create().show();

    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void tocam(){
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        PermissionActivity1PermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);

    }
}

package blm.newandroid.cn.weblm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.widget.LoginButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AuthInfo authInfo;
    private Button login, share;
    private SsoHandler mSsoHandler;
    private LoginButton mLoginBtnDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);  //软件认证
        initViews();
    }

    private void initViews() {
        login = (Button) findViewById(R.id.login);
        share = (Button) findViewById(R.id.share);
        mLoginBtnDefault = (LoginButton) findViewById(R.id.login_button_default);


        login.setOnClickListener(this);
        share.setOnClickListener(this);
        mLoginBtnDefault.setStyle(1);
        mLoginBtnDefault.setWeiboAuthInfo(authInfo, new AuthListener(this)); // 为按钮设置授权认证信息

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                mSsoHandler = new SsoHandler(MainActivity.this, authInfo);
                mSsoHandler.authorize(new AuthListener(MainActivity.this));
                break;
            case R.id.share:
                startActivity(new Intent(MainActivity.this,MyShareWeibo.class));
                break;
        }

    }
    //返回值,暂时感觉用不到
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            //resultCode=0表示没有登录,=-1表示登录了账号
            Log.e("自己的按钮被点击了", requestCode + " "+resultCode + "");
            if (resultCode==0){
                Log.i("null","没有登录");
            }else if (resultCode==-1){
                Log.i("success","已经登录");
                startActivity(new Intent(MainActivity.this,MeActivity.class));
            }
        }
//        if (mLoginBtnDefault!=null){
//            mLoginBtnDefault.onActivityResult(requestCode, resultCode, data);
//            //resultCode=0表示没有登录,=-1表示登录了账号
//            Log.e("一键登录按钮被点击了",requestCode + " "+resultCode + "" );
//            if (resultCode==0){
//                Log.i("null","没有登录");
//            }else if (resultCode==-1){
//                Log.i("success","已经登录");
//                startActivity(new Intent(MainActivity.this,MeActivity.class));
//            }
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("cc",AccessTokenKeeper.readAccessToken(MainActivity.this).getUid());
    }
}

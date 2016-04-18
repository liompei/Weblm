package blm.newandroid.cn.weblm;

import android.content.Context;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by BLM on 2016/4/16.
 */
public class AuthListener implements WeiboAuthListener {
    private Context context;
    private Oauth2AccessToken mAccessToken;


    public AuthListener(Context context) {
        this.context = context;
    }

    @Override
    public void onComplete(Bundle bundle) {
        // 从 Bundle 中解析 Token

        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mAccessToken.isSessionValid()) {
            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(context, mAccessToken);

        } else {
            // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
            String code = bundle.getString("code", "");
            System.out.println(code);
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {

    }

    @Override
    public void onCancel() {

    }
}

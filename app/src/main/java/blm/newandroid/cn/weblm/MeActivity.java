package blm.newandroid.cn.weblm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.widget.LoginoutButton;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MeActivity extends AppCompatActivity {

    private UsersAPI mUsersAPI;
    private TextView textView, aaa;
    private ImageView imageView;
    private LoginoutButton loginoutButton;
    private AuthInfo authInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
       initViews();



    }


    private void initViews(){
        textView = (TextView) findViewById(R.id.text_name);
        aaa = (TextView) findViewById(R.id.location);
        imageView = (ImageView) findViewById(R.id.img_t);
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, AccessTokenKeeper.readAccessToken(this));
        long uid = Long.parseLong(AccessTokenKeeper.readAccessToken(this).getUid());
        mUsersAPI.show(uid, mListenermListener);
        authInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);  //软件认证
        //注销,不知道对不对
        loginoutButton= (LoginoutButton) findViewById(R.id.loginOut);
        loginoutButton.setWeiboAuthInfo(authInfo,new AuthListener(this));
        loginoutButton.setLogoutInfo(AccessTokenKeeper.readAccessToken(this), new RequestListener() {
            @Override
            public void onComplete(String s) {
                AccessTokenKeeper.clear(MeActivity.this);
                finish();
            }

            @Override
            public void onWeiboException(WeiboException e) {

            }
        });
    }



    //实现异步请求接口回调，并在回调中直接解析 User 信息
    private RequestListener mListenermListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
            if (!TextUtils.isEmpty(s)) {  //如果不为空
                User user = User.parse(s);

                String name = user.name;
                textView.setText(name);

                Log.i("aaa", String.valueOf(user.bi_followers_count));
//                aaa.setText(user.bi_followers_count);

                String uri = user.avatar_hd;
                Log.i("dsadsad", uri);
                new ImageLoadTask().execute(uri);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };


    public class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            String uri = params[0];  //获取传递过来的uri
            Bitmap bitmap = null;
            try {
                URL url = new URL(uri);
                InputStream inputStream = url.openConnection().getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                bufferedInputStream.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}

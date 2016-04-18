package blm.newandroid.cn.weblm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

public class MyShareWeibo extends AppCompatActivity implements View.OnClickListener, IWeiboHandler.Response {

    private IWeiboShareAPI mWeiboShareAPI;
    private EditText share_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share_weibo);
        //分享注册
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        mWeiboShareAPI.registerApp(); // 将应用注册到微博客户端
        share_id= (EditText) findViewById(R.id.share_id);
        findViewById(R.id.share_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        sendMultiMessage(true, false, false, false, false, false);
    }

    //微博分享的内容
    private TextObject getTextObj() {
        String content = share_id.getText().toString();
        TextObject textObject = new TextObject();
        textObject.text = content;
        return textObject;
    }

    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                  boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        mWeiboShareAPI.sendRequest(this, request); //发送请求消息到微博，唤起微博分享界面
        Log.d("点击分享按钮", "点击分享按钮");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("开始获取返回信息", "开始获取返回信息");
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        Log.d("获取返回信息成功", "获取返回信息成功");
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Log.d("分享成功", "分享成功");
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Log.d("分享取消", "分享取消");
                Toast.makeText(this, "分享取消", Toast.LENGTH_SHORT).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Log.d("分享失败", "分享失败");
                Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

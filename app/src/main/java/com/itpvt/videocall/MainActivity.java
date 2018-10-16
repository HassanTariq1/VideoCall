package com.itpvt.videocall;

import android.Manifest;
import android.media.MediaCas;
import android.media.tv.TvInputService;
import android.opengl.Matrix;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Session.SessionListener, Publisher.PublisherListener{

    private static String APi="46205402";
    private static String SESSION="1_MX40NjIwNTQwMn5-MTUzOTY3NDUyODMyNX5ibDVkNTd1N0ttS3Fzd3lJVzJkVURGUFh-fg";
    private static String TOKEN="T1==cGFydG5lcl9pZD00NjIwNTQwMiZzaWc9MWMyNzNlYTBhMGMzN2ZkMDQwZWZmOTU5MWE4MTFhNjQ0ZmJiYjk0YjpzZXNzaW9uX2lkPTFfTVg0ME5qSXdOVFF3TW41LU1UVXpPVFkzTkRVeU9ETXlOWDVpYkRWa05UZDFOMHR0UzNGemQzbEpWekprVlVSR1VGaC1mZyZjcmVhdGVfdGltZT0xNTM5Njc0NjA1Jm5vbmNlPTAuODA1OTI2OTMwNjE3NjEmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTUzOTcyMTQyMSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==";
    private static String LOG_TAG= MainActivity.class.getSimpleName();
    private static final   int RC_Setings=128;

    private Session session;

    private FrameLayout subscribe;
    private FrameLayout publish;

    private Publisher publisher;

    private Subscriber subscriber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestPermission();
        publish=(FrameLayout)findViewById(R.id.publish);
        subscribe=(FrameLayout) findViewById(R.id.subscribe);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


    @AfterPermissionGranted(RC_Setings)
    private  void requestPermission(){


        String[] perm= {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

        if(EasyPermissions.hasPermissions(this,perm)){

            session= new Session.Builder(this,APi,SESSION).build();
session.setSessionListener(this);
session.connect(TOKEN);
        }
        else{

            EasyPermissions.requestPermissions(this,"App use for Camera",RC_Setings);

        }
    }

    @Override
    public void onConnected(Session session) {


        publisher= new Publisher.Builder(this).build();
        publisher.setPublisherListener(this);
        publish.addView(publisher.getView());
        session.publish(publisher);
    }

    @Override
    public void onDisconnected(Session session) {

    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {


        if(subscriber == null){


            subscriber= new Subscriber.Builder(this,stream).build();
            session.subscribe(subscriber);
            subscribe.addView(subscriber.getView());
        }

    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {

        if(subscriber!=null){


            subscriber=null;
            subscribe.removeAllViews();

        }

    }

    @Override
    public void onError(Session session, OpentokError opentokError) {

    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {

    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {

    }
}

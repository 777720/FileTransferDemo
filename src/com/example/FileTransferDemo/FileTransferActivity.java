package com.example.FileTransferDemo;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class FileTransferActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    /*
	 * 测试android客户端与PC服务器之间的socket交互， 客户端：把用户输入的信息发送给服务端
	 */


            /*
						 * 指定Server的IP地址，此地址为局域网地址，如果是使用WIFI上网，则为PC机的WIFI IP地址
						 * 在ipconfig查看到的IP地址如下： Ethernet adapter 无线网络连接:
						 * Connection-specific DNS Suffix . : IP Address. . . . . . . .
						 * . . . . : 192.168.123.1
						 */
    private static final String TAG = "Socket_Android";
    private String IP = "192.168.123.1";
    private int PORT= 51706;
    /* 声明控件 */

    private EditText ed1;
    private TextView tv1;
    private Button send;
    private Handler myHandler;
    private String tvText="";
    private Button getFile;
    private ClientSocketHelper cs=null;
    private static final String FILE_PATH = Environment.getExternalStorageDirectory()+"/????????/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ed1 = (EditText) findViewById(R.id.ed1);
        tv1 = (TextView) findViewById(R.id.tv1);
        send = (Button) findViewById(R.id.send);
        getFile=(Button)findViewById(R.id.getFile);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSend();

            }
        });
        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilesButton();
            }
        });


        myHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                tvText=(String)msg.obj;
                tv1.append("\n"+tvText);
            }
        };




    }
    public void onClickSend(){
        // TODO Auto-generated method stub
        Log.d("onclick", "onclick!!!!!!" );

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
               // setTitle("测试连接！");
                Socket socket = null;


                try {
                    InetAddress serverAdd = InetAddress.getByName(IP);
                    Log.d("TCP", "connect.....");

                    // server的IP和端口建立socket对象
                    socket = new Socket(serverAdd,PORT);
                    String message = "---Test_Socket_Android---";

                    Log.d("TCP", "sending：" + message);
                    // 将信息通过这个对象发送给server
                    PrintWriter out = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream())),
                            true);
                    // 将用户输入的信息发送给server
                    String toServer = ed1.getText().toString();
                    Log.d(TAG, "toServer:" + toServer);
                    out.println(toServer);
                    out.flush();
                    // 接受服务器信息
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    // 得到服务器信息
                    String mes = in.readLine();
                    Log.d(TAG, "FromServer:" + mes);
                    // 在页面上进行显示   ,但是通过handler发送      tv1.setText(msg);
                    Message msg=new Message();
                    msg.obj=mes;
                    myHandler.sendMessage(msg);

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();

    }


    private void getMessage(){
        if(cs==null){
            return;
        }

        try {
            DataInputStream inputStream=null;
            inputStream=cs.getMessageStream();
            String savePath=FILE_PATH;
            int bufferSize=8192;//缓冲区长度
            byte[] buf=new byte[bufferSize];
            int passLen=0;
            long len=0;
            File file=new File(savePath);
            if (!file.exists()){
                file.mkdir();
                Log.d(TAG,"文件创建完毕！");
            }


            savePath+=inputStream.readUTF();
            Log.d(TAG,"savePath:"+savePath);
            DataOutputStream fileOut=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
           // DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(new FileOutputStream(savePath))));
            len=inputStream.readLong();
            Log.d(TAG,"文件长度："+len);
            Log.d(TAG,"开始接收文件！");
            while(true){
                int read=0;
                if (inputStream!=null){
                    read=inputStream.read(buf);
                }
                passLen+=read;//进度显示
                if(read==-1){
                    break;
                }
                Log.d("Android-Client","文件接收了"+(passLen*100/len)+"%/n");
                fileOut.write(buf,0,read);
            }
            Log.d(TAG,"文件接收完成"+savePath);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void sendMessage(){
        if (cs==null){
            return;
        }
        cs.sendMessage("Windows");
    }
    private boolean createConnection(){
        cs=new ClientSocketHelper(IP,PORT);
        cs.createConnection();
        Log.d(TAG, "连接服务器成功:");
        return true;

    }

    public void getFilesButton(){
        Log.d(TAG,"onclick!!!!!!!!");
       new Thread(new Runnable() {
           @Override
           public void run() {
               if(createConnection()){

                   sendMessage();
                   Log.d(TAG,"开始！getMessage()");
                   getMessage();
               }

           }
       }).start();

    }








}

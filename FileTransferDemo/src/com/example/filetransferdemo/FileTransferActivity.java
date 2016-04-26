package com.example.filetransferdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FileTransferActivity extends Activity {
	/*
	 * 测试android客户端与PC服务器之间的socket交互， 客户端：把用户输入的信息发送给服务端
	 */
	private static final String TAG = "Socket_Android";
	private String IP = "192.168.123.1";
	/* 声明控件 */
	private EditText ed1;
	private TextView tv1;
	private Button send;
	private Handler myHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_transfer);
		ed1 = (EditText) findViewById(R.id.ed1);
		tv1 = (TextView) findViewById(R.id.tv1);
		send = (Button) findViewById(R.id.send);

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setTitle("测试连接！");
						Socket socket = null;

						/*
						 * 指定Server的IP地址，此地址为局域网地址，如果是使用WIFI上网，则为PC机的WIFI IP地址
						 * 在ipconfig查看到的IP地址如下： Ethernet adapter 无线网络连接:
						 * Connection-specific DNS Suffix . : IP Address. . . . . . . .
						 * . . . . : 192.168.123.1
						 */
						try {
							InetAddress serverAdd = InetAddress.getByName(IP);
							Log.d("TCP", "connect.....");

							// server的IP和端口建立socket对象
							socket = new Socket(serverAdd, 51706);
							String message = "---Test_Socket_Android---";

							Log.d("TCP", "sending：" + message);
							// 将信息通过这个对象发送给server
							PrintWriter out = new PrintWriter(new BufferedWriter(
									new OutputStreamWriter(socket.getOutputStream())),
									true);
							// 将用户输入的信息发送给server
							String toServer = ed1.getText().toString();
							Log.d(TAG, "toServer:" + toServer);
							out.println(out);
							out.flush();
							// 接受服务器信息
							BufferedReader in = new BufferedReader(
									new InputStreamReader(socket.getInputStream()));
							// 得到服务器信息
							String msg = in.readLine();
							Log.d(TAG, "FromServer:" + msg);
							// 在页面上进行显示   ,但是通过handler发送      tv1.setText(msg);
							Message mes=new Message();
							mes.obj=msg;
							myHandler.sendMessage(mes);
							
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
				
			}
		});
		myHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				tv1.setText((String)msg.obj);
			}
		};
		
		
	
	
	}
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	public static final String SERVERIP = "192.168.123.1";
	public static final String SERVERPORT = "51706";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("s:Connecting.......");
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(51706);
			while (true) {
				// 等待接受客户端的请求
				Socket client = serverSocket.accept();
				System.out.println("s:Receiving");

				// 接受客户端消息
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

				// 发送给客户端消息
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),
						true);
				System.out.println("S:11111111");

				// 读取客户端的信息
				String str = in.readLine();
				System.out.println("server message is:" + str);
				System.out.println("S:222222");
				if (str != null) {
					// 设置返回信息，把客户端接受到的信息再返回给客户端
					out.println("server message is:" + str);
					out.flush();

					// 保存到文件中

					File file = new File("D://android.txt");
					FileOutputStream fout = new FileOutputStream(file);
					byte[] b = str.getBytes();
					for (int i = 0; i < b.length; i++) {
						fout.write(b[i]);
					}
					System.out.println("S:Received:" + str + " ");
				} else {
					System.out.println("Not Receiver anything from client");

				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Thread desktopServerThread = new Thread(new Server());
		desktopServerThread.start();
	}

}

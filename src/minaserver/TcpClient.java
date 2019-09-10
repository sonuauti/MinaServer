/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minaserver;

import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class TcpClient extends IoHandlerAdapter {
    /** The connector */
    private IoConnector connector;

    /** The session */
    private static IoSession session;

    private boolean received = false;

    /**
     * Create the UdpClient's instance
     */
    
    public  TcpClient() {
        connector = new NioSocketConnector();
        connector.setHandler(this);
        connector.getFilterChain().addLast("codec",   
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")))); 
        ConnectFuture connFuture = connector.connect(new InetSocketAddress("localhost", TcpServer.PORT));
        connFuture.awaitUninterruptibly();
        session = connFuture.getSession();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void exceptionCaught(IoSession session, Throwable cause) throws Exception {
            cause.printStackTrace();
    }

    /**
     * {@inheritDoc}
     */
    
    @Override
    public  void messageReceived(IoSession session, Object message) throws Exception {
        received = true;
        //long diff = System.currentTimeMillis()-Long.valueOf(message.toString());
        System.out.println("Rec: "+message+" from session "+session.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void messageSent(IoSession session, Object message) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionClosed(IoSession session) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionCreated(IoSession session) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionOpened(IoSession session) throws Exception {
    }

    /**
     * The main method : instanciates a client, and send N messages. We sleep 
     * between each K messages sent, to avoid the server saturation.
     * @param args
     * @throws Exception
     */
    public static  void main(String[] args) throws Exception {
        //TcpClient client = new TcpClient();
        System.out.println("Client Started");    
        
        //test for infinite messages
        //client.infiniteHiTest();
        
        //test for 10 lac messages
        //client.definedForTest(1000000);
        
        //5000 messages with 45 sec delay
        //client.sendDataInDelay(5000,45000);
        
        
       Socket clientSocket = new Socket("localhost", TcpServer.PORT); 
       DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
       
       outToServer.writeUTF("HI from non mina");
       outToServer.flush();
              
        
        
      
        
        //client.connector.dispose(true);
        
    }
    
    private void sendDataInDelay(int limit,int delayinMili){
        try{
            for(int i=0;i<limit;i++){
               session.write(i);
               Thread.sleep(delayinMili);
           }
        }catch(Exception er){
            System.out.println(er.getMessage());
        }
    }
    
    private void infiniteHiTest(){
    
        while(true){
            session.write("HI");
            //Thread.sleep(100);
        }           
    }
    
    private void definedForTest(int ulimit){
           for(int i=0;i<ulimit;i++){
               session.write(i);
           }
    }
}
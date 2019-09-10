/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minaserver;

/**
 *
 * @author cis
 */    
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * An TCP server used for performance tests.
 * 
 * It does nothing fancy, except receiving the messages, and counting the number of
 * received messages.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class TcpServer extends IoHandlerAdapter {
    /** The listening port (check that it's not already in use) */
    public static final int PORT = 9010;

    /** The number of message to receive */
    public static final int MAX_RECEIVED = 100000;

    /** The starting point, set when we receive the first message */
    //private static long t0;

    /** A counter incremented for every  message */
    private AtomicInteger nbReceived = new AtomicInteger(0);

    /**
     * {@inheritDoc}
     */
    @Override
    public  void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        session.close(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void messageReceived(IoSession session, Object message) throws Exception {

        String str = message.toString();
        
        if( str.trim().equalsIgnoreCase("diemina") ) {
            session.close();
            return;
        }
        
        //Date date = new Date();
        session.write(str);
        System.out.println("Message rec "+str+" on "+session.getId());
        
       
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionClosed(IoSession session) throws Exception {
        System.out.println("Session closed...");

        //Reinitialize the counter and expose the number of received messages
        //System.out.println("Nb message received : " + nbReceived.get());
        //nbReceived.set(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionCreated(IoSession session) throws Exception {
        System.out.println("Session created...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        
         System.out.println( "IDLE " + session.getIdleCount( status ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public  void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
        
    }

    /**
     * Create the TCP server
     */
    public TcpServer() throws IOException {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setHandler(this);

        // The logger, if needed. Commented atm
        //DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
        //chain.addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8")))); 
        acceptor.bind(new InetSocketAddress(PORT));
        

        System.out.println("Server started...");
    }

    /**
     * The entry point.
     */
    public static  void main(String[] args) throws IOException {
        TcpServer tcpServer = new TcpServer();
    }
}
    


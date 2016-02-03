//package com.czp.opensrource.dlog;
//
//import java.io.File;
//import java.util.Collection;
//import java.util.Scanner;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
//import org.apache.log4j.Logger;
//
//import com.czp.code.dlog.LogDispatch;
//import com.czp.code.dlog.LogProducer;
//import com.czp.code.dlog.monitor.FileMonitor;
//import com.czp.code.dlog.monitor.IFileListener;
//import com.czp.code.dlog.monitor.SimpleFileListener;
//import com.czp.code.dlog.view.WebViewer;
//
//public class AppTest extends TestCase {
//    
//    public AppTest(String testName) {
//        super(testName);
//    }
//    
//    public static Test suite() {
//        return new TestSuite(AppTest.class);
//    }
//    
//    public void testWebView() {
//        LogDispatch view = new LogDispatch("127.0.0.1:9092", new String[] {"log_main_INFO"}, "webp");
//        WebViewer handler = new WebViewer(8080);
//        handler.start();
//        view.start();
//        view.stop();
//    }
//    
//    public static void xtestSender() {
//        Logger log = Logger.getLogger(AppTest.class);
//        Scanner sc = new Scanner(System.in);
//        String line = null;
//        while (!(line = sc.nextLine()).equals("exit")) {
//            for (int i = 0; i < 10000; i++) {
//                log.info(line + i);
//            }
//        }
//        sc.close();
//    }
//    
//    public static void main(String[] args)
//        throws Exception {
//        final LogProducer producer = new LogProducer("127.0.0.1:9092");
//        IFileListener l = new SimpleFileListener() {
//            
//            @Override
//            public void onChange(File file, String line) {
//                producer.asynSend("log_main_INFO", "", line + "\n");
//                System.out.println(line);
//            }
//            
//        };
//        Collection<String> paths = new CopyOnWriteArraySet<String>();
//        paths.add("G:\\tomcat\\logs");
//        FileMonitor fm = new FileMonitor(paths, "gbk", l, 100);
//        fm.start();
//        System.in.read();
//        fm.stop();
//        producer.close();
//    }
//}

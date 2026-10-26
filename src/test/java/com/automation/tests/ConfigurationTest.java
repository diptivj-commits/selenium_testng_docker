package com.automation.tests;
import com.automation.config.Config;
import com.automation.support.PracticeServer;
import org.testng.annotations.Test;
import java.net.*;
import java.nio.charset.StandardCharsets;
import static org.testng.Assert.*;
public class ConfigurationTest {
 @Test public void executionModeIsSupported(){assertTrue(java.util.Set.of("local","remote").contains(Config.get("run.mode")));}
 @Test public void waitIsPositive(){assertTrue(Config.positiveInt("wait.seconds")>0);}
 @Test public void practicePageIsServedOverHttp()throws Exception {
  try(PracticeServer server=new PracticeServer("127.0.0.1",0)) {
   HttpURLConnection connection=(HttpURLConnection)URI.create("http://127.0.0.1:"+server.port()+"/").toURL().openConnection();
   connection.setConnectTimeout(5000);connection.setReadTimeout(5000);
   try{assertEquals(connection.getResponseCode(),200);try(var in=connection.getInputStream()){assertTrue(new String(in.readAllBytes(),StandardCharsets.UTF_8).contains("Docker Login Practice"));}}finally{connection.disconnect();}
  }
 }
}

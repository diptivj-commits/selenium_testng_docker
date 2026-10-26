package com.automation.base;
import com.automation.config.Config;
import com.automation.support.PracticeServer;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;
import org.testng.annotations.*;
import java.net.URI;
import java.nio.file.*;
import java.time.Duration;
public class BaseTest {
 protected WebDriver driver;
 private PracticeServer server;
 @BeforeSuite(alwaysRun=true)
 public void startPracticePage()throws Exception {
  server=new PracticeServer(Config.get("fixture.bind"),Config.positiveInt("fixture.port"));
  System.out.println("Practice server listening on port "+server.port());
 }
 @BeforeMethod(alwaysRun=true)
 public void startBrowser()throws Exception {
  ChromeOptions options=new ChromeOptions();options.addArguments("--window-size=1440,900");
  if(Boolean.parseBoolean(Config.get("headless")))options.addArguments("--headless=new");
  switch(Config.get("run.mode")) {
   case "remote":driver=new RemoteWebDriver(URI.create(Config.get("remote.url")).toURL(),options);break;
   case "local":driver=new ChromeDriver(options);break;
   default:throw new IllegalArgumentException("run.mode must be local or remote");
  }
  driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Config.positiveInt("page.load.seconds")));
  driver.get(Config.get("app.base.url"));
 }
 @AfterMethod(alwaysRun=true)
 public void stopBrowser(ITestResult result) {
  if(driver==null)return;
  try {
   if(!result.isSuccess())try {
    Path folder=Paths.get("reports","screenshots");Files.createDirectories(folder);
    Path file=Files.createTempFile(folder,result.getName()+"-",".png");
    Files.write(file,((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES));
    Reporter.log("Failure screenshot: "+file.toAbsolutePath(),true);
   }catch(Exception e){System.err.println("Screenshot failed: "+e.getMessage());}
  }finally{try{driver.quit();}finally{driver=null;}}
 }
 @AfterSuite(alwaysRun=true)
 public void stopPracticePage(){if(server!=null)server.close();}
}

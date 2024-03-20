package uk.co.asepstrath.bank;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import io.jooby.test.JoobyTest;
import io.jooby.test.MockContext;
import io.jooby.test.MockResponse;
import io.jooby.test.MockRouter;
import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.co.asepstrath.bank.services.HelperMethods;

import javax.activation.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


@JoobyTest(App.class)
public class DataSourceTest {

    static OkHttpClient client = new OkHttpClient();

    @Test
    public void loginPageConfirm() {
        MockRouter mockRouter = new MockRouter(new App());

        assertEquals("login.hbs", mockRouter.get("/bank").value().toString());

    }

    @Test
    public void loginTest(int serverPort,String bal,HashMap<String, String> replace){
        String user = "Miss Lavina Waelchi";
        if(bal == null)
            bal = "4069.61";
        String types = "PAYMENT";

        if(replace == null) {
            replace = new HashMap<>();
            replace.put("{{name}}", user);
            replace.put("{{bal}}", bal);
            replace.put("{{amount1}}", "200.0");
            replace.put("{{amount2}}", "209.0");
            replace.put("{{amount3}}", "84.0");
            replace.put("{{type1}}", types);
            replace.put("{{type2}}", types);
            replace.put("{{type3}}", types);
        }

        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/home").post(RequestBody.create(MediaType.parse("text/plain; charset=utf-8"),user)).build();

        try (Response rsp = client.newCall(request).execute()) {
            String expectedRsp = new String(Files.readAllBytes(Paths.get("src/main/resources/views/home.hbs"))).trim();
            expectedRsp = HelperMethods.replacePlaceholders(expectedRsp,replace);


            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void managerTest(int serverPort){
        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/home").post(RequestBody.create(MediaType.parse("text/plain; charset=utf-8"),"Manager")).build();

        try (Response rsp = client.newCall(request).execute()) {
            String expectedRsp = new String(Files.readAllBytes(Paths.get("src/main/resources/views/managerView.hbs"))).trim();



            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void withdrawalTest(int serverPort){
        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/withdrawals/Miss Lavina Waelchi").build();
        String expectedRsp = "2015.44";

        try (Response rsp = client.newCall(request).execute()) {



            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void paymentsCalculationTest(int serverPort){
        String expectedRsp = "16413.0";
        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/payments/Miss Lavina Waelchi").build();

        try (Response rsp = client.newCall(request).execute()) {



            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void depositsTest(int serverPort){
        String expectedRsp = "22000.0";
        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/deposits/Miss Lavina Waelchi").build();

        try (Response rsp = client.newCall(request).execute()) {



            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void transfersTest(int serverPort){
        String expectedRsp = "18552.6";
        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/transfers/Miss Lavina Waelchi").build();

        try (Response rsp = client.newCall(request).execute()) {



            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void noRoundUp(int serverPort){
        String expectedRsp = "0.0";
        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/collectRoundUps/Miss Lavina Waelchi").build();

        try (Response rsp = client.newCall(request).execute()) {



            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals(expectedRsp,rsp.body().string().trim());
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }

    @Test
    public void addingSomeMoney(int serverPort){
        String user = "Miss Lavina Waelchi";
        String bal = "4569.61";
        String types = "PAYMENT";
        String amount = "500.0";

        HashMap<String, String> replace = new HashMap<>();
        replace.put("{{name}}",user);
        replace.put("{{bal}}",bal);
        replace.put("{{amount1}}","200.0");
        replace.put("{{amount2}}","209.0");
        replace.put("{{amount3}}","84.0");
        replace.put("{{type1}}",types);
        replace.put("{{type2}}",types);
        replace.put("{{type3}}",types);

        String reqContext = "amount=" + amount + "&name=" + user;

        Request request = new Request.Builder().url("http://localhost:" + serverPort+"/bank/testAddMoney").post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),reqContext)).build();

        try (Response rsp = client.newCall(request).execute()) {
            String expectedRsp = new String(Files.readAllBytes(Paths.get("src/main/resources/views/home.hbs"))).trim();
            expectedRsp = HelperMethods.replacePlaceholders(expectedRsp,replace);


            assertEquals(StatusCode.OK.value(), rsp.code());
            assertEquals("success",rsp.body().string().trim());
            loginTest(serverPort,bal,replace);
        }
        catch (Exception e){
            Assertions.fail(e);
        }
    }
}

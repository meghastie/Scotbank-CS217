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
    public void test(int serverPort){
        MockRouter mockRouter= new MockRouter(new App());

        String user = "Miss Lavina Waelchi";
        String bal = "4069.61";
        String types = "DEPOSIT";

        HashMap<String, String> replace = new HashMap<>();
        replace.put("{{name}}",user);
        replace.put("{{bal}}",bal);
        replace.put("{{amount1}}","2750.0");
        replace.put("{{amount2}}","2750.0");
        replace.put("{{amount3}}","2750.0");
        replace.put("{{type1}}",types);
        replace.put("{{type2}}",types);
        replace.put("{{type3}}",types);

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
}

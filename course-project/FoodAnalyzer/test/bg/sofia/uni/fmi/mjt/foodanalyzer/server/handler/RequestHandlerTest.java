package bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler;

import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodException;
import bg.sofia.uni.fmi.mjt.foodanalyzer.server.handler.exception.request.FoodNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestHandlerTest {

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private CompletableFuture<HttpResponse<String>> asyncResponseMock;

    @Mock
    private HttpResponse<String> httpsResponseMock;

    @Test(expected = FoodNotFoundException.class)
    public void testHandleSearchRequestWhenCodeIs404()
            throws ExecutionException, InterruptedException, FoodException {
        RequestHandler requestHandler = new RequestHandler(httpClientMock);
        when(httpClientMock.sendAsync(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(asyncResponseMock);
        when(asyncResponseMock.get()).thenReturn(httpsResponseMock);
        when(httpsResponseMock.statusCode()).thenReturn(404);
        requestHandler.handleSearchRequest("raffaello treat");
    }

    @Test(expected = FoodException.class)
    public void testHandleSearchRequestWhenCodeIsNot200()
            throws ExecutionException, InterruptedException, FoodException {
        RequestHandler requestHandler = new RequestHandler(httpClientMock);
        when(httpClientMock.sendAsync(Mockito.any(HttpRequest.class),
                ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(asyncResponseMock);
        when(asyncResponseMock.get()).thenReturn(httpsResponseMock);
        when(httpsResponseMock.statusCode()).thenReturn(504);
        requestHandler.handleSearchRequest("raffaello treat");
    }
}

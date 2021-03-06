package controllers;

import javax.inject.Inject;

import actors.WebSocketActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.*;
import play.cache.*;
import views.html.*;
import utils.CacheTestHelper;
import java.io.IOException;


public class HomeController extends Controller {
	
	private final ActorSystem actorSystem;
    private final Materializer materializer;
    
    @Inject
    public HomeController(ActorSystem actorSystem, Materializer materializer, SyncCacheApi cache) {
        this.actorSystem = actorSystem;
        this.materializer = materializer;
    }

    public WebSocket socket() {
        return WebSocket.Text.accept(request ->
                ActorFlow.actorRef(WebSocketActor::props,
                    actorSystem, materializer
                )
        );
    }

    public Result index() {

        return ok(index.render());
    }

    public Result cacheTester(){
        String cacheType = request().getQueryString("cacheType");
        return ok(cacheTester.render(cacheType));
    }

    public Result getTestSuite(){
        return ok(testSuite.render(""));
    }

    public Result add(){
        String body = request().body().asText();
        return ok(body);
    }

    public Result res(){
        String query = request().getQueryString("a");
        response().setHeader("X-Header",query);
        return ok(query);
    }

    public Result getTestCases() throws IOException{
        CacheTestHelper.buildTestCases();
        return ok("Ok");
    }



}

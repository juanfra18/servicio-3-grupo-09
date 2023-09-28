package API;


import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import java.util.List;

public class ProcesarDatosController implements Handler {
  public ProcesarDatosController(){
    super();
  }

  @Override
  public void handle(Context ctx) throws Exception {
    String requestBody = ctx.body();
    Gson gson = new Gson();
    JsonRequest request = gson.fromJson(requestBody, JsonRequest.class);

    EntidadesConMayorImpacto ranking = new EntidadesConMayorImpacto();
    List<Long> entidadesProcesadas = ranking.armarRanking(request.getEntidades(),request.getIncidentes(),request.getComunidades(),request.getCNF());

    JsonResponse jsonResponse = new JsonResponse();
    jsonResponse.setEntidades(entidadesProcesadas);

    ctx.status(200).result("(OK) Datos procesados con éxito").json(gson.toJson(jsonResponse));
  }
}

package API;

import io.javalin.Javalin;

public class WebApp {
  public static void main(String[] args) {
    Integer port = Integer.parseInt(
        System.getProperty("port", "8080"));
    Javalin app = Javalin.create().start(port);
    app.post("/ranking",
        new ProcesarDatosController()); //cuando haga el post que ya devuelva, un solo controller
  }
}


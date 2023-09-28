package API;

import domain.APIComunidad;
import domain.APIEntidad;
import domain.APIIncidente;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JsonRequest {
    private List<APIEntidad> entidades;
    private List<APIIncidente> incidentes;
    private List<APIComunidad> comunidades;
    private Integer CNF;

    public JsonRequest() {
        this.entidades = new ArrayList<>();
        this.incidentes = new ArrayList<>();
        this.comunidades = new ArrayList<>();
    }
}


/* En este caso se le envía un json con las 3 listas (entidades, incidentes y comunidades)

    {
    "entidades": [
        {
           //entidad1
        },
        {
            //entidad2
        }
    ],
    "incidentes": [
        {
            // incidente1
        },
        {
            // incidente2
        }
    ],
    "comunidades": [
        {
            // comunidad1
        },
        {
            // comunidadN
        }
    ]
}
*/
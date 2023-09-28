package domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class APIComunidad extends APIGenerica{
  private List<APIIncidente> incidentes;
  private List<APIMiembroDeComunidad> miembros;

  public boolean incidenteEsDeComunidad(APIIncidente incidente) {
    return incidentes.stream().anyMatch(i -> i.getId() == incidente.getId());
  }
}

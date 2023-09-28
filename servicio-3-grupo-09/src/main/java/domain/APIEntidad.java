package domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class APIEntidad extends APIGenerica{
  private List<APIEstablecimiento> establecimientos;
}

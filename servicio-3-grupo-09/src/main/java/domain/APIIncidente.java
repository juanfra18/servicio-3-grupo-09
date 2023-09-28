package domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@Setter
@Getter
public class APIIncidente extends APIGenerica{
  private String horaApertura; //Formato 'yyyy-MM-ddThh:mm:ss' ISO LOCAL DATE TIME
  private String horaCierre; //Formato 'yyyy-MM-ddThh:mm:ss' ISO LOCAL DATE TIME
  private APIEstablecimiento establecimiento;
  private APIServicio servicio;

  public Long tiempoDeCierre(){
    return ChronoUnit.MINUTES.between(LocalDateTime.parse(this.horaApertura), LocalDateTime.parse(this.horaCierre));
  }
  public boolean cerrado(){
    return this.horaCierre != null;
  }
}

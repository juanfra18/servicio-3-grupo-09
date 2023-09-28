package API;

import domain.*;

import java.util.*;

public class EntidadesConMayorImpacto {
    /* Servicio 3: cálculo de ranking de impacto de incidentes
Este servicio permite calcular el ranking semanal previsto en la entrega anterior con el criterio de mayor
 grado de impacto de las problemáticas considerando que algunas comunidades tienen mayor cantidad de miembros
 y por lo tanto les afecta de mayor medida el no funcionamiento de ese servicio.
Para cada entidad se calcula su nivel de impacto considerando la sumatoria de tiempos de resolución de incidentes +
la cantidad de incidentes no resueltos
multiplicado por un coeficiente de incidentes no resueltos (CNF), expresado en la siguiente ecuación

Σ (t resolución de incidente) + Cantidad de incidentes no resueltos * CNF

A la ecuación anterior se la debe multiplicar por la cantidad de miembros afectados para obtener el resultado final.
Con los valores obtenidos se realiza un ordenamiento de las entidades.
Requerimientos detallados
Se deberán persistir las entidades del modelo planteado. Para ello se debe utilizar un ORM.
Se deberá implementar el servicio que tuviera asignado el grupo

     */
    // nivelDeImpacto = Σ (t resolución de incidente) + Cantidad de incidentes no resueltos * CNF
    // total = nivelDeImpacto * cantMiembros (miembros.afectados.size())
    // total -> Armas el ranking

    protected int[] obtenerValoresPorEntidad(List<APIEntidad> entidades, List<APIIncidente> incidentes, List<APIComunidad> comunidades, Integer CNF) {
        int[] tiempoDeResolucion = new int[entidades.size()];
        int[] cantIncidentesNoResueltos = new int[entidades.size()];
        int[] impactoDeIncidentes = new int[entidades.size()];
        int[] cantMiembrosAfectados = new int[entidades.size()];

        for(APIIncidente incidente: incidentes)
        {
            //Buscamos la primer entidad con ese incidente:
            Optional<APIEntidad> entidadConIncidente = entidades.stream().filter(entidad -> entidad.getEstablecimientos().stream().anyMatch(establecimiento -> establecimiento.getId() == incidente.getEstablecimiento().getId())).findFirst();
            if (entidadConIncidente.isPresent()) {
                if(incidente.cerrado())
                {
                    tiempoDeResolucion[entidades.indexOf(entidadConIncidente.get())] += incidente.tiempoDeCierre(); //se acumula el tiempo de resolucion de incidentes en minutos
                    //System.out.println(incidente.tiempoDeCierre());
                }
                else{
                    //System.out.println(1);
                    cantIncidentesNoResueltos[entidades.indexOf(entidadConIncidente.get())]++;
                    //No se consideran como en el primer ranking 2 incidentes con mas de 24 horas de diferencia como 2 incidentes distintos
                }
                List<APIComunidad> comunidadesAfectadas = comunidades.stream().filter(comunidad -> comunidad.incidenteEsDeComunidad(incidente)).toList();
                //System.out.println(comunidadesAfectadas.size());
                List<APIMiembroDeComunidad> MiembrosRepetidos = comunidadesAfectadas.stream().flatMap(comunidad -> comunidad.getMiembros().stream()).toList();
                List<APIMiembroDeComunidad> MiembrosDeComunidadesAfectados = new ArrayList<>();
                for(APIMiembroDeComunidad miembroDeComunidad: MiembrosRepetidos)
                {
                    if(!MiembrosDeComunidadesAfectados.stream().anyMatch(m -> miembroDeComunidad.getId() == m.getId()) && miembroDeComunidad.afectadoPor(incidente)) //A cada uno de los miemmbros lo agrega a las comunidades afectadas
                    {
                        MiembrosDeComunidadesAfectados.add(miembroDeComunidad);
                        //System.out.println("hola");
                    }
                }

                cantMiembrosAfectados[entidades.indexOf(entidadConIncidente.get())]+= MiembrosDeComunidadesAfectados.size(); //Cantidad de miembrosAfectados
            }
        }

        for(int i = 0; i < entidades.size();i++) //Σ (t resolución de incidente) + Cantidad de incidentes no resueltos * CNF * cantMiembros (miembros.afectados.size())
        {
            impactoDeIncidentes[i] = (tiempoDeResolucion[i] + cantIncidentesNoResueltos[i] * CNF) * cantMiembrosAfectados[i];
            /*
            System.out.println("Tiempo resolución: " + tiempoDeResolucion[i]);
            System.out.println("Cantidad incidentes no resueltos: " + cantIncidentesNoResueltos[i]);
            System.out.println("Cantidad de miembros afectados: " + cantMiembrosAfectados[i]);
             */
        }

        return impactoDeIncidentes;
    }

    public List<Long> armarRanking(List<APIEntidad> entidades, List<APIIncidente> incidentes, List<APIComunidad> comunidades, Integer CNF){
        int[] contadorAux;
        contadorAux = obtenerValoresPorEntidad(entidades,incidentes,comunidades,CNF);
        return ordenarEntidades(entidades,contadorAux);
    }

    protected List<Long> ordenarEntidades(List<APIEntidad> entidades, int[] auxiliar){
        List<APIEntidad> entidadesOrdenadas = new ArrayList<>(entidades);
        Collections.sort(entidadesOrdenadas, new Comparator<APIEntidad>() {
            @Override
            public int compare(APIEntidad entidad1, APIEntidad entidad2) {
                int index1 = entidades.indexOf(entidad1);
                int index2 = entidades.indexOf(entidad2);
                return Integer.compare(auxiliar[index2], auxiliar[index1]);
            }
        });
        List<Long> returnValue = new ArrayList<>();
        entidadesOrdenadas.forEach(e -> returnValue.add(e.getId()));
        return returnValue;
    }
}

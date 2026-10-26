package br.com.heroisdeplantao.servico;
import org.springframework.stereotype.Component;
@Component public class AgendamentoRegra {
 public boolean existeConflito(long novoPaciente,long novoMedico,String novaData,String novaHora,long pacienteExistente,long medicoExistente,String dataExistente,String horaExistente,String status){
 return "Agendada".equals(status)&&novaData.equals(dataExistente)&&novaHora.equals(horaExistente)&&(novoPaciente==pacienteExistente||novoMedico==medicoExistente);
 }
}

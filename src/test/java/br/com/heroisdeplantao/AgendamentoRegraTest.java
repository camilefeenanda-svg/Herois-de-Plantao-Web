package br.com.heroisdeplantao;
import br.com.heroisdeplantao.servico.AgendamentoRegra;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class AgendamentoRegraTest {private final AgendamentoRegra r=new AgendamentoRegra();
 @Test void mesmoMedicoConflita(){assertTrue(r.existeConflito(1,2,"2027-01-20","14:30",3,2,"2027-01-20","14:30","Agendada"));}
 @Test void mesmoPacienteConflita(){assertTrue(r.existeConflito(1,2,"2027-01-20","14:30",1,4,"2027-01-20","14:30","Agendada"));}
 @Test void consultaCanceladaNaoConflita(){assertFalse(r.existeConflito(1,2,"2027-01-20","14:30",1,2,"2027-01-20","14:30","Cancelada"));}
 @Test void horarioDiferenteNaoConflita(){assertFalse(r.existeConflito(1,2,"2027-01-20","14:30",1,2,"2027-01-20","15:30","Agendada"));}
 @Test void dataDiferenteNaoConflita(){assertFalse(r.existeConflito(1,2,"2027-01-20","14:30",1,2,"2027-01-21","14:30","Agendada"));}
 @Test void pacientesMedicosDistintosNaoConflitam(){assertFalse(r.existeConflito(1,2,"2027-01-20","14:30",3,4,"2027-01-20","14:30","Agendada"));}
}

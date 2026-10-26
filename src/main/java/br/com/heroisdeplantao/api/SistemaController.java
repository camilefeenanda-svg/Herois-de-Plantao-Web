package br.com.heroisdeplantao.api;
import br.com.heroisdeplantao.servico.AgendamentoRegra;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
@RestController @RequestMapping("/api") public class SistemaController {
 private final JdbcTemplate jdbc;private final AgendamentoRegra regra;
 public SistemaController(JdbcTemplate jdbc,AgendamentoRegra regra){this.jdbc=jdbc;this.regra=regra;}
 private static String valor(Map<String,Object> d,String k){return Objects.toString(d.get(k),"").trim();}
 private static String necessario(Map<String,Object> d,String k){String v=valor(d,k);if(v.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Campo obrigatório: "+k);return v;}
 private static long id(Map<String,Object> d,String k){try{return Long.parseLong(necessario(d,k));}catch(NumberFormatException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ID inválido: "+k);}}
 private long inserir(String sql,Object... args){GeneratedKeyHolder kh=new GeneratedKeyHolder();jdbc.update(con->{PreparedStatement ps=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);for(int i=0;i<args.length;i++)ps.setObject(i+1,args[i]);return ps;},kh);return Objects.requireNonNull(kh.getKey()).longValue();}
 private void existe(String tabela,long id){Integer n=jdbc.queryForObject("SELECT COUNT(*) FROM "+tabela+" WHERE id=?",Integer.class,id);if(n==null||n==0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Registro relacionado não encontrado");}
 private List<Map<String,Object>> listar(String tabela){return jdbc.queryForList("SELECT * FROM "+tabela+" ORDER BY id DESC");}
 @GetMapping("/{tipo}") public List<Map<String,Object>> get(@PathVariable String tipo){return listar(tabela(tipo));}
 private String tabela(String tipo){if(!Set.of("pacientes","medicos","consultas","convenios","prontuarios").contains(tipo))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Recurso inexistente");return tipo;}
 @PostMapping("/pacientes") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> paciente(@RequestBody Map<String,Object> d){String nome=necessario(d,"nome"),cpf=necessario(d,"cpf"),nas=necessario(d,"nascimento");if(!cpf.matches("[0-9]{11}"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"CPF deve conter 11 dígitos");try{LocalDate.parse(nas);}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data inválida");}long id=inserir("INSERT INTO pacientes(nome,cpf,nascimento,telefone,email,alergias) VALUES(?,?,?,?,?,?)",nome,cpf,nas,valor(d,"telefone"),valor(d,"email"),valor(d,"alergias"));return Map.of("id",id);}
 @PostMapping("/medicos") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> medico(@RequestBody Map<String,Object> d){long id=inserir("INSERT INTO medicos(nome,crm,especialidade,email) VALUES(?,?,?,?)",necessario(d,"nome"),necessario(d,"crm"),necessario(d,"especialidade"),valor(d,"email"));return Map.of("id",id);}
 @PostMapping("/consultas") @ResponseStatus(HttpStatus.CREATED) public synchronized Map<String,Object> consulta(@RequestBody Map<String,Object> d){long p=id(d,"paciente"),m=id(d,"medico");existe("pacientes",p);existe("medicos",m);String data=necessario(d,"data"),hora=necessario(d,"hora");try{LocalDate.parse(data);LocalTime.parse(hora);}catch(Exception e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Data ou hora inválida");}
 List<Map<String,Object>> existentes=jdbc.queryForList("SELECT paciente,medico,data,hora,status FROM consultas WHERE data=? AND hora=? AND status='Agendada'",data,hora);
 for(Map<String,Object> c:existentes){if(regra.existeConflito(p,m,data,hora,((Number)c.get("PACIENTE")).longValue(),((Number)c.get("MEDICO")).longValue(),(String)c.get("DATA"),(String)c.get("HORA"),(String)c.get("STATUS")))throw new ResponseStatusException(HttpStatus.CONFLICT,"Conflito de horário para médico ou paciente");}
 long id=inserir("INSERT INTO consultas(paciente,medico,data,hora,status) VALUES(?,?,?,?,'Agendada')",p,m,data,hora);return Map.of("id",id);}
 @PatchMapping("/consultas/{id}/cancelar") public Map<String,Object> cancelar(@PathVariable long id){int n=jdbc.update("UPDATE consultas SET status='Cancelada' WHERE id=? AND status='Agendada'",id);if(n==0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Consulta inexistente ou já cancelada");return Map.of("status","Cancelada");}
 @PostMapping("/convenios") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> convenio(@RequestBody Map<String,Object> d){long p=id(d,"paciente");existe("pacientes",p);long id=inserir("INSERT INTO convenios(paciente,nome,tipo,contato) VALUES(?,?,?,?)",p,necessario(d,"nome"),necessario(d,"tipo"),valor(d,"contato"));return Map.of("id",id);}
 @PostMapping("/prontuarios") @ResponseStatus(HttpStatus.CREATED) public Map<String,Object> prontuario(@RequestBody Map<String,Object> d){long p=id(d,"paciente");existe("pacientes",p);long id=inserir("INSERT INTO prontuarios(paciente,diagnostico,descricao) VALUES(?,?,?)",p,necessario(d,"diagnostico"),necessario(d,"descricao"));return Map.of("id",id);}
 @ExceptionHandler(DataIntegrityViolationException.class) @ResponseStatus(HttpStatus.CONFLICT) public Map<String,String> integridade(DataIntegrityViolationException e){return Map.of("erro","Registro duplicado ou relacionamento inválido");}
}

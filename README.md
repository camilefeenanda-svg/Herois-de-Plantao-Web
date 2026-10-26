# Heróis de Plantão — Etapa 9 (Senac)
**Camile Fernanda de Lima Rodrigues**

Aplicação Java Web com Spring Boot REST, Spring JDBC e H2 persistente, integrada ao front-end HTML/CSS/JS da Etapa 8. Os wireframes originais foram mantidos.

## Requisitos
Java 17 ou superior, Apache Maven 3.9+ (ou suporte Maven no NetBeans), internet na primeira compilação para baixar dependências.

## Executar no NetBeans
1. Arquivo > Abrir projeto > selecionar esta pasta (contém `pom.xml`).
2. Aguardar baixar as dependências Maven.
3. Executar `HeroisDePlantaoApplication.java` ou `spring-boot:run`.
4. Abrir `http://localhost:8080/login.html` ou `http://localhost:8080/` no navegador.
5. O login é APENAS DEMONSTRATIVO. Não há segurança/autenticação de verdade.

## Testes
No NetBeans escolher **Test Project**, ou `mvn test`. O teste `AgendamentoRegraTest` cobre a lógica de conflito de horários. Faça também os testes manuais descritos em `Documentacao/Plano_e_Evidencias_Testes.docx`.

## Banco de dados
O banco H2 é persistido em `dados/heroisdeplantao.mv.db` (gerado quando o programa inicia). Schema criado em `src/main/resources/schema.sql`. Dados demo apenas — não utilizar dados médicos verdadeiros. Não subir a pasta `dados/` ao GitHub.

## API
`GET /api/pacientes|medicos|consultas|convenios|prontuarios`; `POST` nas mesmas rotas; `PATCH /api/consultas/{id}/cancelar`.

## Versionamento e bugtracking
Crie um repositório **novo** para a Etapa 9 (diferente do repositório da Etapa 6/7). Publique os arquivos do projeto, sem a pasta `dados` nem `target`. Use **GitHub Issues** para registrar bugs reais detectados durante testes. Não há repositório novo publicado automaticamente nem issues reais criadas neste pacote.

## Limitações
Projeto de demonstração acadêmica. H2 local, sem controle de acesso real, perfis de usuários, proteção de dados de saúde, nem transações clínicas certificadas. O formulário de login não autentica: apenas navega para a aplicação.

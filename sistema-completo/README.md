## 🛠️ Como Adicionar o MySQL Connector ao Projeto (Build Path)

Para que o sistema consiga se conectar ao banco de dados MySQL, é necessário adicionar o driver do conector (`.jar`) às bibliotecas do projeto. Siga o passo a passo de acordo com a sua IDE:

### No Eclipse (Recomendado)
1. Faça o download do arquivo `mysql-connector-j-9.7.0.jar` (ou a versão utilizada pelo seu banco).
2. No Eclipse, clique com o **botão direito** em cima do nome do projeto na barra lateral (*Package Explorer*).
3. Vá até a opção **Build Path** e depois clique em **Configure Build Path...**
4. Na janela que se abrir, selecione a aba **Libraries** no menu superior.
5. Se você estiver utilizando o Java 9 ou superior, clique sobre a categoria **Classpath**. (Se estiver em versões anteriores, pule este passo).
6. Clique no botão **Add External JARs...** no canto direito da tela.
7. Navegue pelas pastas do seu computador, selecione o arquivo do conector (`.jar`) e clique em **Abrir**.
8. Clique no botão **Apply and Close** no canto inferior direito para salvar.

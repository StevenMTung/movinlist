![Movinlist_capa](https://github.com/user-attachments/assets/063e5b5a-1c58-4688-9410-4ee068501813)

<h1>Descrição do projeto</h1>

O MovinList é um aplicativo que auxilia as pessoas a mobiliar suas casas. No MovinList é cadastrado os cômodos da casa e para cada cômodo, é possível criar uma lista de produtos (móveis, eletrodomésticos, etc.) que precisam ser comprados.  

Além disso, há a possibilidade de marcar quais os produtos de sua lista já foram adquiridos. É possível monitorar os gastos financeiros feitos com produtos já comprados e com os que ainda serão comprados.

Outra característica interessante é que o MovinList pode ser acessado em mais de um dispositivo de forma simultânea, possibilitando que profissionais como arquitetos ou decoradores possam mobiliar a casa de forma colaborativa com os seus clientes.


<h1>Funcionalidades</h1>

- `Cadastro de usuários`: Realizar cadastro de usuários com e-mail e senha.
  
- `Autenticação do usuário`: Acessar a conta cadastrada do usuário, assim como todas as informações salvas nela, de forma simultânea, através de qualquer dispositivo Android que fizer a autenticação correta de e-mail e senha.
  
- `Cadastro dos cômodos`: Cadastrar os cômodos da casa que precisam ser mobiliados.

- `Criar lista de produtos pendentes`: Para cada cômodo cadastrado é possível criar uma lista dos produtos que precisam ser comprados. É possível colocar informações como fotos, nomes, marca dos produtos, descrição, preço unitário e quantidades a serem compradas.

- `Criar lista de produtos comprados`: Criar lista de produtos comprados conforme são adquiridos pelo usuário.

- `Edição e remoção de cômodos`: Editar o nome dos cômodos criados e removê-los se necessário.

- `Edição e remoção de produtos`: Editar os produtos criados e removê-los se necessário.

- `Relatório financeiro`: Monitorar os gastos financeiros das listas de produtos pendentes, comprados e total.

- `Edição e remoção da conta de usuários`: Editar informações como nome e senha da conta do usuário e permitir a remoção da conta junto com todas as informações salvas nela.

<h3>Cadastro do usuário:</h3>

![SignUp_gif](https://github.com/user-attachments/assets/f7df1e04-7c08-47c2-b0fa-f37502b22813)

<h3>Login do usuário e cadastro de cômodos:</h3>

![SignIn_CreateHouseArea_gif](https://github.com/user-attachments/assets/85d3dbca-4bde-495f-bd56-ba911c211c04)

<h3>Criação da lista de produtos do cômodo Cozinha:</h3>

![CreateProduct_gif](https://github.com/user-attachments/assets/dbb1480e-c7e4-42c0-99dd-d951fd896189)

<h3>Adicionando produtos à lista de produtos comprados:</h3>

![AddProductsToPurchasedList_gif](https://github.com/user-attachments/assets/901e3ba2-e5af-40b0-88dc-fa6fe0709b65)

<h3>Navengando entre as telas de Lista de Produtos Pendentes, Lista de Produtos Comprados, Relatório Financeiro:</h3>

![Navigate_Screens_gif](https://github.com/user-attachments/assets/ba5b5c0d-9754-47d5-8b52-690b89ec2e0c)


<h1>Técnicas e tecnologias utilizadas</h1>

O App foi desenvolvido com as seguintes tecnologias:

- `Hilt`: Injeção de dependência
- `Jetpack Compose`: Implementação da interface de usuário
- `ViewModel e uiState`: Gerenciamento de Estados de tela
- `Navigation com NavHost`: Navegações entre telas por grafos hospedados em um NavHost
- `Coroutines e Flow`: Rodar as operações de forma assíncrona e reativas
- `Firebase Authentication`: Cadastro e atenticação de usuários no app
- `Firebase Firestore`: Armazenamento de entidades do app
- `Firebase Cloud Storage`: Armazenamento de imagens do app
- `CameraX`: Acesso à câmera para captura de fotos
- `Storage Access Framework (SAF)`: Acesso às fotos do armazenamento compartilhado do dispositivo móvel
- `Coil`: Upload de imagens
       
<h1>Acesso ao projeto</h1>

Você pode acessar o [código fonte do projeto](https://github.com/StevenMTung/movinlist) ou [baixá-lo](https://github.com/StevenMTung/movinlist/archive/refs/heads/main.zip).

<h1>Abrir e rodar o projeto</h1> 

Após baixar o projeto, você pode abrir com o `Android Studio`. Para isso, na tela de launcher clique em:

- `Open an Existing Project` (ou alguma opção similar);
- Procure o local onde o projeto está e o selecione (Caso o projeto seja baixado via zip, é necessário extraí-lo antes de procurá-lo);
- Por fim clique em `OK`.

O `Android Studio` deve executar algumas tasks do *Gradle* para configurar o projeto, aguarde até finalizar. Ao finalizar as tasks, você pode executar o App 🏆 

<h1>Autor</h1>

 [<img loading="lazy" src="https://avatars.githubusercontent.com/u/134224337?v=4" width=115><br><sub>Steven Marc Tung</sub>](https://github.com/StevenMTung)
| :---: | 

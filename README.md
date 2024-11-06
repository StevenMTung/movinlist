![Movinlist_logo](https://github.com/user-attachments/assets/2cde0dbe-3fd8-4482-974a-685c8aecf462)

<h1>Descrição do projeto</h1>

O MovinList é um aplicativo que auxilia as pessoas a mobiliar as suas casas. No MovinList é cadastrado os cômodos da casa e para cada cômodo é possível criar uma lista de produtos (móveis, eletrodomésticos, etc...) que precisam ser comprados.  

Além disso, há a possibilidade de marcar quais os produtos da lista que já foram adquiridos e monitorar os gastos financeiros dos produtos que precisam ser comprados e dos que já foram comprados.

Outra característica interessante é que o MovinList pode ser acessado em mais de um dispositivo de forma simultânea, possibilitando que profissionais como arquitetos ou decoradores possam mobiliar a casa de forma colaborativa com os seus clientes.

<h1>Funcionalidades</h1>

- `Cadastro de usuários`: Realizar cadastro de usuários com e-mail e senha.
  
- `Autenticação do usuário`: Acessar a conta cadastrada do usuário, assim como todas as informações salvas nela, de forma simultânea através de qualquer dispositivo Android que fizer a autenticação correta de e-mail e senha.
  
- `Cadastro dos cômodos`: Cadastrar os cômodos da casa que precisam ser mobiliados.

- `Criar lista de produtos pendentes`: Para cada cômodo cadastrado é possível criar uma lista dos produtos que precisam ser comprados, com informações de foto, nome, marca do produto, descrição, preço unitário e quantidade.

- `Criar lista de produtos comprados`: Criar lista de produtos comprados conforme são adquiridos pelo usuário.

- `Edição e remoção de cômodos`: Editar o nome dos cômodos criados e possibilitar remoção deles.

- `Edição e remoção de produtos`: Editar as informações dos produtos criados e possibilitar a remoção deles.

- `Relatório financeiro`: Monitorar os gastos financeiros por cômodo das listas de produtos pendentes, comprados e total.

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
- `ViewModel e uiState`: Gerenciamento de Estados
- `Navigation com NavHost`: Navegações entre telas por grafos hospedados em um NavHost
- `Coroutines e Flow`: Rodar as operações de forma assíncrona e reativas
- `Firebase Authentication`: Cadastro e atenticação de usuários no app
- `Firebase Firestore`: Armazenamento de entidades do app
- `Firebase Cloud Storage`: Armazenamento de imagens do app
- `CameraX`: Acesso à câmera para captura de fotos
- `Storage Access Framework (SAF)`: Acesso às fotos do armazenamento compartilhado do dispositivo móvel
       

![Movinlist_capa](https://github.com/user-attachments/assets/063e5b5a-1c58-4688-9410-4ee068501813)

<h1>Project description</h1>

MovinList is an app that helps people furnish their homes. In MovinList, users can register the rooms of the house, and for each room, they can create a list of products (furniture, appliances, etc.) 
that need to be purchased.

In addition, users can mark which products on their list have already been acquired. It's possible to track financial expenses for both purchased and pending items.

Another interesting feature is that MovinList can be accessed from multiple devices simultaneously, allowing professionals such as architects or interior designers to collaborate with their clients in furnishing the home.


<h1>Features</h1>

- `Register users`: Register users with email and password.
  
- `Autenticação do usuário`: Acessar a conta cadastrada do usuário, assim como todas as informações salvas nela, de forma simultânea, através de qualquer dispositivo Android que fizer a autenticação correta de e-mail e senha.
  
- `Room registration`: Register the rooms in the house that need to be furnished.

- `Create pending product list`: For each registered room, it is possible to create a list of products that need to be purchased. You can add information such as photos, product names, brand, description, unit price, and quantities to be purchased.

- `Create purchased product list`: Create a list of purchased products as they are acquired by the user.

- `Edit and remove rooms`: Edit the names of created rooms and remove them if necessary.

- `Edit and remove products`: Edit created products and remove them if necessary.

- `Financial report`: Monitor financial expenses from the pending, purchased, and total product lists.

- `Edit and delete user account`: Edit information such as name and password of the user account, and allow the deletion of the account along with all saved data.

<h3>User registration:</h3>

![SignUp_gif](https://github.com/user-attachments/assets/f7df1e04-7c08-47c2-b0fa-f37502b22813)

<h3>User login and room registration:</h3>

![SignIn_CreateHouseArea_gif](https://github.com/user-attachments/assets/85d3dbca-4bde-495f-bd56-ba911c211c04)

<h3>Creation of the product list for the Kitchen:</h3>

![CreateProduct_gif](https://github.com/user-attachments/assets/dbb1480e-c7e4-42c0-99dd-d951fd896189)

<h3>Adding products to the purchased products list:</h3>

![AddProductsToPurchasedList_gif](https://github.com/user-attachments/assets/901e3ba2-e5af-40b0-88dc-fa6fe0709b65)

<h3>Navigating between the Pending Products List, Purchased Products List, Financial Report screens.</h3>

![Navigate_Screens_gif](https://github.com/user-attachments/assets/ba5b5c0d-9754-47d5-8b52-690b89ec2e0c)


<h1>Techniques and technologies used.</h1>

The app was developed with the following technologies:

- `Hilt`: Dependecy Injection
- `Jetpack Compose`: User interface implementation
- `ViewModel e uiState`: Screen state management
- `Navigation com NavHost`:  Navigation between screens via graphs hosted in a NavHost
- `Coroutines e Flow`:  Running operations asynchronously and reactively
- `Firebase Authentication`: User registration and authentication in the app
- `Firebase Firestore`:  Storing app entities
- `Firebase Cloud Storage`: Storing app images
- `CameraX`: Camera access for photo capture
- `Storage Access Framework (SAF)`: Access to photos from the device's shared storage
- `Coil`: Image upload
       
<h1>Access to the Project</h1>

You can access the [project source code](https://github.com/StevenMTung/movinlist) or [download it](https://github.com/StevenMTung/movinlist/archive/refs/heads/main.zip).

<h1>Open and Run the Project</h1> 

After downloading the project, you can open it with `Android Studio`. To do this, on the launcher screen click on:

- `Open an Existing Project` (or a similar option);
- Find the location where the project is and select it (If the project was downloaded as a zip, you will need to extract it before locating it);
- Finally, click `OK`.

`Android Studio` will execute some Gradle tasks to set up the project, wait for them to finish. Once the tasks are completed, you can run the app 🏆 

<h1>Author</h1>

 [<img loading="lazy" src="https://avatars.githubusercontent.com/u/134224337?v=4" width=115><br><sub>Steven Marc Tung</sub>](https://github.com/StevenMTung)
| :---: | 

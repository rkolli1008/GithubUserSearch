# GithubUserSearch
Functional Requirements
_______________________
1.The app should accept a github user's id as input and display the specified user's avatar and name.
2.For each public repository owned by the user, the name and description are shown in a scrollable list.
3.When a repository is selected, a card pops up with information about when it was last updated, how many stars it has, and how many times it has  been forked.

## Architecture

![architecture](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)
1. Data layer: This layer is responsible for fetching data from a remote source or a local database. In this implementation, data is fetched from the GitHub API using Retrofit and converted using Moshi. Data is also cached in a Room database for offline access. By separating the data layer, the application can easily switch to a different data source without affecting the rest of the application.

2. Repository layer: This layer acts as a bridge between the data layer and the presentation layer. It contains the business logic of the application, such as filtering and sorting the data. In this implementation, there is a single repository class that handles fetching data from the data source and applying the business logic. By using a repository pattern, the data layer is decoupled from the presentation layer, making the code more modular and testable.

3. ViewModel layer: This layer is responsible for providing data to the UI and handling user interactions. It observes changes in the repository and provides the data to the UI as LiveData. In this implementation, there is a single ViewModel class that provides the list of repositories to the UI. By using ViewModel, data is preserved across configuration changes, such as orientation changes, and leaks are avoided by avoiding references to the UI.

4. UI layer: This layer contains the user interface of the application. In this implementation, Jetpack Compose is used to build the UI. There is a single composable function that displays a list of repositories in a RecyclerView. There is also a custom view for displaying repository details in a card view pop up. By using Compose, the UI is built in a declarative way, making it easier to reason about and maintain.

By organizing code into separate layers, the application can achieve a number of benefits:

1. Separation of concerns: Each layer is responsible for a specific task, making the code more modular and easier to understand.

2. Testability: By separating code into layers, each layer can be tested in isolation, making tests more focused and easier to write.

3. Reusability: By decoupling code, the same components can be reused across different parts of the application or even in other applications.

4. Maintainability: By organizing code into layers, changes can be made to one layer without affecting the other layers, making the code more maintainable and easier to update.

Overall, this implementation follows best practices for building Android applications, such as using Jetpack components, following the SOLID principles, and organizing code into layers.

## Specs & Open-source libraries
- MVVM Architecture
- Jetpack Components (Lifecycle, LiveData, ViewModel)
- Compose UI Toolkit
- Hilt
- [Retrofit2 & Moshi](https://github.com/square/retrofit) for constructing the REST API

Known Issues: Compose tests are failing at the moment.



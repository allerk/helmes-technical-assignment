# How to run?
- Prepare .env (check application.yml)
- Use either docker compose or manually using maven command
- Sectors data can be initialized if ```INIT_DATA=true``` in env file
  - or if you already imported db dump you need to disable ```INIT_DATA=false``` in .env file and set ```liquibase:enabled:false``` in application.yml
  - .dump file is in the parent folder
- Basically only created data in .dump file is Sectors. Users might be created in a form

## Tools:
- Maven
- Java 17
- Spring MVC
- Thymeleaf
- Postgres 
- liquibase
- mapstruct
- lombok
- docker

### Build image:
```
docker build -t helmes-technical-assignment-app:latest .
```
### Or you can just
```
docker-compose up --build
```

## About the solution
### Why this technical stack?
I chose this technical stack based on my experience during the Kuehne + Nagel internship, because I feel it the most comfortable for me to work with.

### What was done
I decided not to use a separate UI framework to build the web app. Instead, I chose Thymeleaf to handle server-side rendering directly with Java and Spring.

While it would be possible to use even plain Java servlets without any additional abstractions, since I am already use Spring, Thymeleaf offers a natural fit.
It integrates seamlessly with Spring and provides numerous convenient features, enabling efficient and maintainable development.

For example:
- Model binding, form handling, and validation without extra boilerplate code
- Reduces repetitive code, simplifies error handling
- Allows to conditionally render content directly in templates

I organized the application following a layered architecture: controllers, services, repositories and models. Where every layer is
responsible for its specific concern. Used many abstractions and inheritance to reduce code duplicates. This separation of responsibilities improves maintainability and scalability.
I aimed to use SOLID, DRY, KISS and YAGNI principles.

Provided initializer that can be turned on by enabling it based on environmental variable. Additionally, the project structure is carefully organized into separate packages, making it clear and intuitive to navigate.

Implemented unit tests using TDD approach for bll (business logic layer) to test that application logic works as expected.

In other words, I tried to demonstrate industry-standard Spring practices and demonstrate a solid understanding of how to design, structure, and test a modern web application.

I know that some aspects of my solution might rise some questions, and I'll be glad to answer them.

I mostly aimed to demonstrate my knowledge based on the requirements. I understand that I could add dozens of additional functionality to demonstrate even more,
but then the YAGNI principle comes to mind, do I really need it to be expanded even more and more?
Most of my thoughts were about expanding the UI part, e.g. to display a list of users where you can pick and edit its data (i.e. in a TODO apps),
but in case it's more about the backend, I decided to get rid of it and allow this by typing the url in "edit/{:id}".

### Approach
Literally in a few words:
1. MVC controllers that binds to the model objects
2. HTML code in resources/templates that displays it
3. 2 routes: index and edit. The first one is for user creating, the second one is for editing.
4. Errors also displayed in the UI

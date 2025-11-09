# **Student filtering system**
**Stack**:
- **Backend**: Java (21) Springboot.
- **Frontend**: Javascript, HTML & CSS.

## How does it work?
The system in general works using API REST to communicate between frontend (axios) with the backend, configuring the CORS correctly to allow the communication.
This allows the authorities with access to the system to filter students by their modality of degree, date, faculty, career or even with the name, having total control of the students degrees.

## How can I set it up?
To set it up, you must have installed the next requirements:
- Java JDK (version 21 or greather)
- An IDE of your choice (Integrated Development Enviroment)

Once you got the requirements, you got to clone the repository in your machine.

After this, you can run the main class on the route "" and, this way, you got the backend set up done.

## Incoming improvements (issues)
- **Pagination**: When fetching large quantity of students, our website performance can be really affected by the large amount of data we have to show. With **pagination** we can solve this problem.
- **Frontend modulation**: Due to the lack of time of this project, the frontend team made everything into just 3 files but we are going to modulate the whole project.
- **Database Type**: This project was made with SQL Server but we are going to migrate it to MongoDB because it allows us to store large quantity of information and has more flexibility.

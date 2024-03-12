<h1 align="center">Member Management System</h1>


This system, which I developed as the final project for my Software Development Technician career, originates from a practical need in the healthcare sector. It was born out of the specific requirements conveyed to me by two HR professionals working within a healthcare institution. With their insights and needs in mind, I crafted a solution tailored to the unique demands of managing human resources in a hospital clinic environment. This project serves as a testament to my ability to translate real-world challenges into effective software solutions, demonstrating my skills and expertise gained throughout my academic journey. The main parts of the system are:

<h3>Employee Profile Management:</h3>
This part allows the HR personnel to maintain detailed personal information and employment records for each employee, including their ID number, name, date of birth, hire date, contact details, sector assignment, and document numbers (ID, Social Security, etc.). It also facilitates tracking of employee contract statuses (active, expired, or converted to permanent).

<h3>Address Management:</h3>
The system stores and manages the residential addresses of employees, indicating whether a particular location is within the service area of the medical audit service provided by the clinic.

<h3>Attendance and Leave Tracking:</h3>
This component enables the HR staff to record employee attendance, absences, and various types of leaves, such as sick leave, exam leave, family leave, and bereavement leave. It integrates with an existing time clock system called "Cronos21" to import attendance data.

</h3>Vacation Calculation:</h3>
Based on the employee's hire date and years of service, the system automatically calculates the number of vacation days they are entitled to each year, following specific rules defined by the clinic's policies.

</h3>Daily Notices:</h3>
The system generates a daily report that includes information on long-term employee leaves, upcoming retirements, employees on contract, and new mothers eligible for lactation hours.

<h3>Reporting and Filtering:</h3>
The software provides filtering and reporting capabilities, allowing HR personnel to generate reports in PDF or Excel format based on various criteria, such as employee sector or specific data points.

<h3>User Roles and Access Control:</h3>
Depending on their roles within the clinic, employees will have different levels of access to the system's features and data.

## Technologies used:
The system is developed in Java, utilizing the "Spring Boot" framework, adapted for use with FXML views and their controllers in JavaFX. Additionally, Spring Data JPA will be employed for data persistence in a PostgreSQL database. Furthermore, JasperReports library will be incorporated for PDF printing functionality.

This adaptation was chosen to harness the powerful capabilities of the "Spring Boot" framework, particularly its seamless movement of data between different components of the system. While "Spring Boot" is commonly associated with web applications, its versatile features make it suitable for diverse project types, including desktop applications. By integrating "Spring Boot" with JavaFX, the system benefits from enhanced data flow and communication between frontend and backend components. This streamlined approach not only accelerates development but also simplifies maintenance and ensures the scalability of the desktop application.

Moreover, leveraging Spring Data JPA simplifies database operations by providing powerful abstractions and reducing boilerplate code. Integrating JasperReports facilitates seamless generation of PDF documents, enhancing the system's reporting capabilities. Overall, this combination of technologies ensures a modern, efficient, and scalable solution for the development of the desktop application.
## Images:
Here are some screenshots of the working system. The data is censored.
<div style="display: flex; flex-wrap: wrap;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/eacc1808-4bd4-4d46-97e9-81ef0dfe7ddb" alt="login" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/9fe36de3-d7ce-4722-b489-a46c4cd87028" alt="new_member" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/207e1894-a077-4e2e-bc7f-3db68b828f7c" alt="member_view" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/c99face8-3d39-41e8-8b1a-ef5e8987b103" alt="memeber_filter" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/e1020b1d-1fbf-48d1-8e64-35d8705cd960" alt="test_member_data" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/c285712d-66e4-48e0-85eb-e2305498ee6c" alt="secondary_members_group_of_testmember" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/1b929ee0-9878-4fb0-82af-51039729e080" alt="alerts_change_of_categories" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/f308cbdf-3717-4584-b330-586f6b783628" alt="debt_collectors" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/c1d8db8b-09ea-4960-bdd8-bca810650106" alt="categories" style="width: 150px; margin: 5px;">
    <img src="https://github.com/aleberh23/member-management-system/assets/158856472/a751d009-7bc2-41f7-8b6c-72b11b14b4e7" alt="change_of_category_alert" style="width: 150px; margin: 5px;">
</div>



## Installation instructions:
Before proceeding with the installation steps, you must have the following prerequisites installed:

- JDK 20 (for WINDOWS 64 bits: https://download.oracle.com/java/20/archive/jdk-20.0.2_windows-x64_bin.exe)
- PostgreSQL server running on localhost:5432 (for WINDOWS 64 bits: https://sbp.enterprisedb.com/getfile.jsp?fileid=1258893)

  <h3>Instalation Steps:</h3>

1. Go to the final release: https://github.com/aleberh23/member-management-system/releases/tag/final.
2. Download the .zip file called: "release.1.1.final".
3. Extract the compressed file.
4. Inside the folder 'database' you have the test database backup, and a .bat script, here you have two options.
    - Execute the script, this will automatically create the DB called 'bomberos_socios', then executes the command pg_restore to restore the backup to the new DB, and finally changes the password for the user 'postgres' to 'postgres'.
    - Do this process manually, by creating the DB "bomberos_socios" in pg_admin or by command line, restoring the DB backup called "bomberos_socios_ghubtest.sql", and finally changing the password of postgres user to 'postgres'.
5. Open the .exe file of the system located in the root directory of the .zip archive.
6. Login screen should appear after about 10 seconds, the user is 'test' and the password '123'.
7. Now the system is ready to use!


<h1 align="center">Member Management System</h1>


This system, which I developed as the final project for my Software Development Technician career, originates from a practical need in the healthcare sector. It was born out of the specific requirements conveyed to me by two HR professionals working within a healthcare institution. With their insights and needs in mind, I crafted a solution tailored to the unique demands of managing human resources in a hospital clinic environment. This project serves as a testament to my ability to translate real-world challenges into effective software solutions, demonstrating my skills and expertise gained throughout my academic journey. The main parts of the system are:

<h3>Virtual Employee Profile Management:</h3>
This integral component serves as a comprehensive repository for each employee, housing essential details such as their name, email, phone number, birth date, date of admission, assigned sector, etc. Within this section, the system further categorizes information into four distinct subsections:

- **Contracts:** Provides insight into the various contracts associated with the employee, including details on their status (active, expired, or converted to permanent).
- **Family Charges:** Contains pertinent information concerning the family members of the employee.
- **Addresses:** Offers a centralized location for storing and managing different addresses associated with the employee, ensuring accurate record-keeping.
- **Licenses:** Tracks all licenses acquired or anticipated by the employee, encompassing a range of categories such as vacations, exams, family member sickness, marriage, and more. This section provides visibility into the employee's licensure status, aiding in efficient management of time-off requests and related administrative tasks.

<h3>Employee Absences Management:</h3>
This integral feature empowers HR staff to meticulously track employee absences and leaves of various types, including sick leave, exam leave, family leave, and bereavement leave. The system streamlines the process by providing a dedicated section where HR personnel can effortlessly log absences for specific employees on particular days. These absences are seamlessly reflected in the daily report, ensuring accurate and up-to-date records of employee attendance and leave usage.

<h3>Vacation Entitlement Verification:</h3>
This component encompasses a class that serves as a proof of the vacation days allotted to each employee within a specific period. The system automatically calculates these days based on the employee's hire date and years of service, adhering to the clinic's policies. However, prior to generating an ordinary license for an employee, HR personnel must utilize this class to verify the entitlement of vacation days. This verification ensures that the employee's vacation allocation is accurate and up-to-date before proceeding with the issuance of the license.

<h3>Daily Report Generation:</h3>
In this component, HR employees generate a daily report to capture vital information pertinent to daily operations. The report is generated each day and encompasses details such as employees with active contracts for that date, employees currently on various types of licenses, absences recorded for the day, and employees with scheduled retirement dates within the remainder of the year. This comprehensive report facilitates effective management of workforce dynamics and aids in planning and decision-making processes within the HR department. This component not only facilitates the daily generation of reports but also ensures accessibility to previous and subsequent daily reports at any time. HR employees can consult earlier or later daily reports as needed, with the system automatically updating them to reflect any new information

<h3>Comprehensive Reporting Capabilities:</h3>
In this feature set, the software offers extensive reporting functionalities, empowering HR personnel to generate detailed PDF reports efficiently. Users can produce comprehensive reports containing the complete data file of an employee, encompassing personal information, labor records, contact details, addresses, family charges, active and completed contracts, as well as licenses.
Additionally, the system facilitates the generation of PDF reports specifically tailored to individual daily summaries selected by the user. This capability ensures that HR personnel can swiftly access and share precise information, optimizing decision-making processes and enhancing overall efficiency within the department.

<h3>Streamlined Data Filtering:</h3>
This component simplifies data management by providing basic filtering capabilities for the lists of employees, contracts, and licenses. Users can easily apply filters to these lists to locate specific information swiftly. This functionality enhances efficiency by allowing HR personnel to quickly find relevant employees, contracts, or licenses within the system.

<h3>User Roles and Access Control:</h3>
The system implements two distinct roles to manage access and privileges effectively. 
The first role, designated as "HR Personnel," is tailored for employees within the HR department. Users assigned to this role possess complete access to all features and data within the system, enabling them to perform comprehensive HR tasks.

The second role, named "Entrance Desk," is specifically designed for employees tasked with managing attendance and absences. Users in this role have limited access, restricted solely to the functionality required for generating absences. This streamlined access ensures that entrance desk personnel can efficiently carry out their responsibilities without unnecessary access to other system features.

## Technologies used:

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



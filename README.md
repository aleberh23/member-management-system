<h1 align="center">Member Management System</h1>

This system was designed and constructed for a specific client, wich had specific requirements about how the management of members was to be done.
During the interview process and requirements identification, i found that the system has five axes, these are:
- Registration/Withdrawal/Modification/Reading of members.
- Linking member with category, of which certain conditions must be met to belong (detailed below)
- Linking members with family groups (adherent members).
- Manual generation of debts.
- Members legal book.

As "secondary points" we have:
- Impression of main members fee receipt, collection control for collectors, members legal book by year.
- Category change suggestion alerts.

<h3>Main members:</h3>
These are the ones that paid the month bill, this type of member can have adherent members associated and debts in their name.

<h3>Adherent members:</h3>
These are the family group of the main member. The main member can have many adherent member.

<h3>Categories:</h3>
The client asked for 6 categories, each with a monthly fee and a set of conditions that the main member must meet to belong. (It is important to clarify that the user can select any category, but the system will display alerts with suggestions for change if the conditions are not met or if there is a more suitable category for the main member's profile.) These are:

- **Individual | Individual :**  The main member must not have any additional members.
- **Grupo Familiar (Menores) | Family Group (Minors) :** The main member must have at least one adherent member of type "Hijo/a" (son/daughter) who is a minor.
- **Grupo Familiar (Mayores) | Family Group (Majors):** The main member must have at least one adherent member of type "Hijo/a" (son/daughter) who is 18 years old or older.
- **Grupo Familiar (Más de un Mayor) | Family Group (Multiple Majors) :** The main member must have more than one adherent member of type "Hijo/a" (son/daughter) who is 18 years old or older.
- **Comercio | Business :** The main member must be the owner of a business or the business itself.
- **Hogar de Ancianos | Nursing Home :** The main member must be the representative of a nursing home or the nursing home itself.

<h3>Debts:</h3>
These are manually generated by the user, they represent the late payment by a main member of one or more monthly fees.

<h3>Legal Book:</h3>
This is a list of some specific data of the main members, ordered by the date of admission. 

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



## Instrucciones de Instalación

Para instalar y ejecutar el proyecto, sigue estos pasos:

1. Clona el repositorio: `git clone https://github.com/tu-usuario/tu-proyecto.git`
2. Ingresa al directorio del proyecto: `cd tu-proyecto`
3. Instala las dependencias: `npm install` (o el comando correspondiente para tu proyecto)
4. Ejecuta la aplicación: `npm start` (o el comando correspondiente para tu proyecto)

Asegúrate de tener las herramientas necesarias instaladas antes de seguir estos pasos.

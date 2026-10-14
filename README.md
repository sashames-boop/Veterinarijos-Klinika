Kuriama sistema, kurioje gyvūnų savininkai gali registruoti savo augintinius, ieškoti gyvūnų priežiūros paslaugų, jas rezervuoti, susirašinėti su paslaugų teikėjais ir palikti atsiliepimus. Paslaugų teikėjai valdo savo paslaugas, darbo laiką ir rezervacijas. Administratoriai prižiūri visą sistemą.

1. Sistemą sudaro [0.5]:
    - Desktop programa administratoriams ir paslaugų įmonių vadovams. Paslaugos - tiek veterinarijos klinika, tiek priežiūros salonai, tiek gyvūnų "auklės" ir pan. Paslaugos labai įvairios.
    - Web backend, teikiantis REST API Android programėlei.
    - Android programėlė gyvūnų savininkams ir prižiūrėtojams.
    - Bendra reliacinė duomenų bazė. Desktop ir web backend prie DB jungiasi naudojant Hibernate/JPA.

2. Sistema turi autentifikuoti vartotojus pagal prisijungimo vardą ir slaptažodį. Slaptažodis duomenų bazėje saugomas tik užšifruoto hash pavidalu, pavyzdžiui, naudojant BCrypt. [0.25]

3. Kiekvienas vartotojas mato tik jo rolei ir veiksmams reikalingus duomenis [0.25].

    - Paprastas vartotojas negali matyti kitų vartotojų rezervacijų ar asmeninės informacijos.
    - Paslaugų teikėjas mato tik jam arba jo įmonei priklausančias rezervacijas.
    - Užblokuotas vartotojas negali prisijungti arba kurti naujų rezervacijų.

4. Sistema turi bent keturis vartotojų tipus [0.5]:

    - Gyvūno savininkas – registruoja gyvūnus ir užsako paslaugas.
    - Gyvūnų prižiūrėtojas – vykdo priežiūros paslaugas.
    - Paslaugų įmonės vadovas – valdo įmonės paslaugas, darbuotojus ir rezervacijas.
    - Administratorius – prižiūri visą sistemą.

5. Administratorius gali peržiūrėti visus sistemos duomenis ir juos valdyt t.y. CRUD operacijos visos įmanomos. [0.5]

6. Paslaugų paieška ir filtravimas [1]:

Gyvūnų savininkai gali ieškoti paslaugų bent pagal šiuos požymius:

     - paslaugos tipą;
     - kainos intervalą;
     - miestą arba atstumą;
     - datą ir laiką;
     - gyvūno rūšį;
     - gyvūno dydį;
     - paslaugos trukmę;
     - paslaugų teikėjo įvertinimą;
     - paslaugos teikimo vietą;
     - paslaugos prieinamumą.

Rezultatus galima rikiuoti pagal kainą, atstumą, įvertinimą arba populiarumą.

7. Paslaugų įmonės vadovas gali [1]:

    - sukurti paslaugą;
    - redaguoti paslaugos informaciją;
    - keisti kainą;
    - nustatyti, kokiems gyvūnams paslauga teikiama;
    - priskirti prižiūrėtojus;
    - nustatyti darbo laiką;
    - pažymėti paslaugą kaip laikinai neteikiamą;
    - peržiūrėti paslaugos rezervacijų istoriją.

Paslaugos negalima pašalinti, jeigu ji turi aktyvių ar istorinių rezervacijų. Tokiu atveju ji tik pažymima kaip neaktyvi.

8. Turi būti įgyvendintas kalendorius, kuris rodo laisvus laikus. Prižiūrėtojai arba įmonių vadovai gali nustatyti [1]:

    - darbo dienas;
    - darbo valandas;
    - pertraukas;
    - atostogų laikotarpius;
    - laiką, kuriuo paslaugos neteikiamos;
    - didžiausią vienu metu prižiūrimų gyvūnų skaičių.

9. Rezervacijos turi būsenas ir jos kinta [0.25]

10. Susirašinėjimas prie rezervacijos. Prie kiekvienos rezervacijos gali susirašinėti [1]:

    - gyvūno savininkas;
    - rezervacijai priskirtas prižiūrėtojas;
    - paslaugų įmonės vadovas.

Žinutė turi - siuntėją, gavėją arba rezervaciją, tekstą, išsiuntimo laiką, perskaitymo laiką, redagavimo būseną. Neperskaitytos žinutės turi būti vizualiai išskirtos.

11. Vykdydamas paslaugą prižiūrėtojas gali [0.5]:

    - pažymėti paslaugos pradžios ir pabaigos laiką;
    - pridėti atliktų veiksmų aprašymą;
    - pridėti gyvūno nuotraukų;
    - vedžiojimo atveju įrašyti nueitą atstumą;
    - pažymėti, ar gyvūnas buvo pamaitintas;
    - užregistruoti incidentą arba problemą.

12. Po užbaigtos rezervacijos [0.5]:

    - klientas gali įvertinti prižiūrėtoją arba įmonę;
    - prižiūrėtojas gali įvertinti klientą ir gyvūno elgesį;
    - vertinimas pateikiamas nuo 1 iki 5;
    - galima pridėti tekstinį komentarą;
    - vienas dalyvis gali pateikti tik vieną atsiliepimą tai pačiai rezervacijai;
    - atsiliepimą galima pateikti tik dalyvavus įvykdytoje rezervacijoje.
    - bendras vartotojo arba įmonės įvertinimas apskaičiuojamas iš visų aktyvių atsiliepimų.

13.  Prie paslaugų teikėjo profilio galima palikti viešą klausimą arba komentarą [1].

    - Paslaugų teikėjas ir kiti vartotojai gali atsakyti į komentarą.
    - Atsakymai gali turėti kitus atsakymus.
    - Komentarų gylis neribojamas.
    - Rekursinis ryšys realizuojamas naudojant nuorodą į tėvinį komentarą.
    - Komentaro savininkas gali redaguoti savo komentarą.
    - Administratorius gali pašalinti netinkamus komentarus.

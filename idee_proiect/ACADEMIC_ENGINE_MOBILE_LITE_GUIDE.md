# Academic Engine Mobile Lite - Ghid de implementare in Android Studio

## Scop

`Academic Engine Mobile Lite` este o aplicatie Android nativa in Java, inspirata direct din proiectul de licenta `academic-engine`.

Aplicatia nu trebuie sa reimplementeze backend-ul real, agentii LangGraph sau tot frontend-ul web. Varianta usoara si potrivita pentru disciplina de Dispozitive Mobile este o versiune mobila simplificata care permite:

- vizualizarea unor sesiuni de cercetare
- afisarea rezultatelor academice
- salvarea articolelor favorite
- rating local pentru articole
- statistici pe rezultate
- harta cu institutii sau tari asociate articolelor
- setari locale inspirate din `academic-engine`

Datele pot veni dintr-un fisier `JSON` remote care simuleaza/exporta rezultate produse de `academic-engine`.

## De ce are legatura cu licenta

Aplicatia pastreaza conceptele centrale din `academic-engine`:

- `research threads`
- intrebari de cercetare
- moduri `research` si `discuss`
- rezultate academice
- articole stiintifice
- autori
- surse academice
- citari
- abstracte
- salvare locala si analiza a rezultatelor

La prezentare poti spune:

```text
Aplicatia Android este un companion mobil pentru Academic Engine. Ea nu ruleaza agentii direct pe telefon, ci afiseaza, salveaza si analizeaza rezultate de cercetare provenite din Academic Engine sau dintr-un export JSON compatibil cu acesta.
```

Aceasta explicatie este mai buna decat o aplicatie generica de evenimente, deoarece pastreaza domeniul principal al licentei: cautare academica si analiza de rezultate stiintifice.

## Varianta recomandata

Pentru proiectul de Android, recomand varianta fara backend live la inceput:

- autentificare locala in `SQLite`
- date remote din `research_results.json`
- cache local in `SQLite`
- favorite si rating local
- grafice din date locale
- harta pe baza coordonatelor din JSON
- setari in `SharedPreferences`

Backend-ul real `academic-engine` poate ramane optional.

## De ce nu trebuie backend-ul in Android Studio

Android Studio contine doar aplicatia mobila.

Backend-ul, daca il folosesti, ruleaza separat:

- local in terminal, pe `http://10.0.2.2:8000` din emulator
- sau pe un server extern

Pentru varianta usoara, nu ai nevoie de backend. Un fisier `JSON` public este suficient pentru cerinta de parsare remote.

## Functionalitati recomandate

Aplicatia ar trebui sa permita:

- creare cont local
- autentificare locala
- dashboard cu optiuni principale
- afisare lista de thread-uri de research
- afisare lista de articole pentru un thread
- cautare si filtrare articole
- detalii articol
- salvare articol favorit
- rating articol
- vizualizare pe harta a institutiilor/tarilor
- grafice despre rezultate
- setari locale

## Activitati recomandate

Foloseste aceste activitati:

1. `LoginActivity`
2. `RegisterActivity`
3. `DashboardActivity`
4. `ThreadListActivity`
5. `ResearchResultsActivity`
6. `PaperDetailsActivity`
7. `MapActivity`
8. `AnalyticsActivity`
9. `SettingsActivity`

Ai nevoie de minim 5, dar 8-9 activitati arata mai bine si sunt usor de justificat.

## Flux recomandat

```text
LoginActivity
   -> DashboardActivity
      -> ThreadListActivity
         -> ResearchResultsActivity
            -> PaperDetailsActivity
               -> MapActivity

DashboardActivity
   -> AnalyticsActivity

DashboardActivity
   -> SettingsActivity
```

## Cum bifeaza cerintele minime

### 1. Minim 5 activitati legate intre ele

Activitati demonstrabile:

- `LoginActivity`
- `RegisterActivity`
- `DashboardActivity`
- `ThreadListActivity`
- `ResearchResultsActivity`
- `PaperDetailsActivity`
- `MapActivity`
- `AnalyticsActivity`
- `SettingsActivity`

### 2. Controale vizuale simple

Foloseste:

- `TextView` pentru titluri, abstracte, surse, citari
- `EditText` pentru login, register si search
- `Spinner` pentru mod `research/discuss`, sursa, sortare
- `Button` pentru actiuni si navigare
- `CheckBox` pentru favorite only sau recent papers only
- `ProgressBar` pentru loading
- `RatingBar` pentru rating articol
- `Switch` pentru auto-save sau dark mode

### 3. Controale vizuale complexe

Foloseste:

- `ListView` pentru thread-uri
- `ListView` pentru articole
- `GridView` pentru dashboard
- `DatePickerDialog` pentru filtrare dupa an/data

### 4. Custom adapter pentru ListView

Implementeaza:

- `ThreadAdapter extends BaseAdapter`
- `PaperAdapter extends BaseAdapter`

Cel mai important pentru cerinta este `PaperAdapter`.

### 5. SharedPreferences

Salveaza:

- userul logat
- modul implicit: `auto`, `research`, `discuss`
- planner implicit: `auto`, `v1`, `v2`
- sortare implicita
- sursa preferata
- dark mode
- auto-save results

### 6. SQLite

Foloseste `SQLiteOpenHelper` pentru:

- useri locali
- thread-uri de research
- articole cache
- favorite
- rating-uri
- istoric accesari

### 7. Parsarea JSON remote

Aplicatia incarca un fisier `research_results.json` remote.

Acest fisier simuleaza datele venite din `academic-engine`.

### 8. Google Maps

Afiseaza markere pentru:

- universitati
- institutii
- tari asociate articolelor
- centre de cercetare

Coordonatele se pun direct in JSON pentru simplitate.

### 9. Grafice 2D

In `AnalyticsActivity`, afiseaza:

- articole pe ani
- articole pe surse
- top articole dupa citari
- distributia articolelor favorite pe surse

## Structura recomandata a proiectului Android

```text
com.example.academicenginemobilelite
|
|-- activities/
|   |-- LoginActivity.java
|   |-- RegisterActivity.java
|   |-- DashboardActivity.java
|   |-- ThreadListActivity.java
|   |-- ResearchResultsActivity.java
|   |-- PaperDetailsActivity.java
|   |-- MapActivity.java
|   |-- AnalyticsActivity.java
|   |-- SettingsActivity.java
|
|-- adapters/
|   |-- DashboardAdapter.java
|   |-- ThreadAdapter.java
|   |-- PaperAdapter.java
|
|-- database/
|   |-- AppDatabaseHelper.java
|
|-- models/
|   |-- User.java
|   |-- ResearchThread.java
|   |-- PaperItem.java
|   |-- InstitutionLocation.java
|
|-- network/
|   |-- ApiClient.java
|   |-- ApiService.java
|
|-- utils/
|   |-- PreferencesManager.java
|   |-- DateUtils.java
|
|-- res/layout/
|   |-- activity_login.xml
|   |-- activity_register.xml
|   |-- activity_dashboard.xml
|   |-- activity_thread_list.xml
|   |-- activity_research_results.xml
|   |-- activity_paper_details.xml
|   |-- activity_map.xml
|   |-- activity_analytics.xml
|   |-- activity_settings.xml
|   |-- item_dashboard.xml
|   |-- item_thread.xml
|   |-- item_paper.xml
```

## Ce trebuie sa faca fiecare activitate

### 1. LoginActivity

Rol:

- autentificare locala
- pornirea aplicatiei dupa login

Controale:

- `TextView`
- `EditText` email
- `EditText` parola
- `Button` login
- `Button` register
- `ProgressBar`

Logica:

- verifica userul in `SQLite`
- salveaza userul logat in `SharedPreferences`
- deschide `DashboardActivity`

### 2. RegisterActivity

Rol:

- creare cont local

Controale:

- `EditText` nume
- `EditText` email
- `EditText` parola
- `Button` create account

Logica:

- valideaza campurile
- verifica email duplicat
- insereaza user in `SQLite`

### 3. DashboardActivity

Rol:

- punct principal de navigare

Control complex:

- `GridView`

Carduri recomandate:

- `Research Threads`
- `Saved Papers`
- `Map`
- `Analytics`
- `Settings`

### 4. ThreadListActivity

Rol:

- afiseaza thread-uri de research

Controale:

- `ListView`
- `Button` refresh/import
- `ProgressBar`

Ce afiseaza:

- titlul thread-ului
- query-ul initial
- modul: `research` sau `discuss`
- data ultimei actualizari
- numar de articole

### 5. ResearchResultsActivity

Rol:

- afiseaza articolele pentru un thread

Controale:

- `EditText` search
- `Spinner` source filter
- `Spinner` sort by
- `Button` filter date
- `CheckBox` favorites only
- `ProgressBar`
- `ListView`

Ce face:

- incarca articole din JSON sau SQLite
- filtreaza dupa sursa, an, favorite
- deschide detalii articol

### 6. PaperDetailsActivity

Rol:

- afiseaza detalii complete despre un articol

Controale:

- `TextView` titlu
- `TextView` autori
- `TextView` an
- `TextView` sursa
- `TextView` citation count
- `TextView` abstract
- `RatingBar`
- `Button` save favorite
- `Button` open map

Logica:

- salveaza rating
- adauga/scoate favorite
- deschide `MapActivity` pentru institutia articolului

### 7. MapActivity

Rol:

- afiseaza locatii academice asociate articolelor

Ce poate afisa:

- universitati
- centre de cercetare
- tari
- institutii principale ale autorilor

Recomandare:

- foloseste coordonate din JSON, nu geocoding automat

### 8. AnalyticsActivity

Rol:

- grafice pe baza datelor din aplicatie

Grafice recomandate:

- `BarChart`: articole pe ani
- `PieChart`: articole pe surse
- `BarChart`: top articole dupa citari
- optional `LineChart`: articole salvate in timp

### 9. SettingsActivity

Rol:

- setari locale

Controale:

- `Spinner` default mode
- `Spinner` default planner
- `Spinner` default sort
- `Switch` auto-save
- `CheckBox` recent papers only
- `Button` save

## Model JSON remote

Fisierul `research_results.json` poate avea structura:

```json
{
  "threads": [
    {
      "thread_id": "thread_ai_education",
      "title": "AI in Education",
      "query": "How is AI used in personalized education?",
      "mode": "research",
      "planner": "v2",
      "updated_at": "2026-04-20",
      "papers": [
        {
          "id": "paper_001",
          "title": "Personalized Learning with Artificial Intelligence",
          "authors": ["A. Smith", "M. Ionescu"],
          "year": 2024,
          "source": "OpenAlex",
          "doi": "10.1000/example",
          "url": "https://example.com/paper",
          "abstract": "This paper studies the use of AI in adaptive education systems.",
          "citation_count": 128,
          "institution": "University of Bucharest",
          "country": "Romania",
          "lat": 44.4355,
          "lng": 26.1023
        }
      ]
    }
  ]
}
```

## SQLite

### Tabele recomandate

#### `users`

- `id`
- `name`
- `email`
- `password`

#### `research_threads`

- `thread_id`
- `title`
- `query`
- `mode`
- `planner`
- `updated_at`

#### `papers`

- `id`
- `thread_id`
- `title`
- `authors`
- `year`
- `source`
- `doi`
- `url`
- `abstract`
- `citation_count`
- `institution`
- `country`
- `lat`
- `lng`

#### `favorites`

- `id`
- `user_id`
- `paper_id`
- `saved_at`

#### `ratings`

- `id`
- `user_id`
- `paper_id`
- `rating`

#### `history`

- `id`
- `user_id`
- `paper_id`
- `opened_at`

## SharedPreferences

Chei recomandate:

- `logged_user_email`
- `default_mode`
- `default_planner`
- `default_sort`
- `auto_save_results`
- `recent_papers_only`
- `dark_mode`
- `last_thread_id`

## Dependinte recomandate

Pentru Android Studio:

- Retrofit
- Gson Converter
- Google Maps SDK for Android
- MPAndroidChart

## Implementare pas cu pas

### Etapa 1 - Proiect Android

1. creezi proiect nou in Android Studio
2. alegi `Java`
3. alegi `Empty Views Activity`
4. creezi pachetele
5. declari activitatile in manifest
6. adaugi `INTERNET`

### Etapa 2 - Layout-uri si navigare

1. creezi XML-urile
2. adaugi controalele
3. faci navigarea intre ecrane

### Etapa 3 - SQLite si SharedPreferences

1. creezi `AppDatabaseHelper`
2. creezi tabelele
3. implementezi login/register
4. creezi `PreferencesManager`

### Etapa 4 - JSON remote

1. creezi `research_results.json`
2. il publici pe GitHub Raw sau alt URL public
3. configurezi Retrofit
4. parsezi datele
5. salvezi thread-uri si papers in SQLite

### Etapa 5 - Liste si adaptere

1. creezi `ThreadAdapter`
2. creezi `PaperAdapter`
3. legi `ListView`-urile de date

### Etapa 6 - Detalii, favorite si rating

1. implementezi `PaperDetailsActivity`
2. salvezi favorite
3. salvezi rating
4. salvezi history

### Etapa 7 - Maps

1. configurezi Google Maps
2. citesti coordonate din SQLite
3. afisezi markere pentru institutii

### Etapa 8 - Analytics

1. citesti date din SQLite
2. agregi dupa an, sursa si citari
3. afisezi grafice cu MPAndroidChart

## Ce adaugi pentru nota 10

- mod offline din SQLite
- filtre multiple: sursa, an, citari, favorite
- sortare dupa citari si an
- ecran `Saved Papers`
- export BibTeX simplificat pentru un articol
- markere colorate dupa sursa
- 3 grafice diferite
- stari `loading`, `empty`, `error`
- validari bune la login/register
- design coerent cu tema academica
- explicatie clara ca aplicatia este companion mobil pentru `academic-engine`

## Demo recomandat

La prezentare arata:

1. register si login local
2. dashboard cu `GridView`
3. lista de thread-uri
4. import/refresh din JSON remote
5. lista de papers cu `PaperAdapter`
6. filtre si sortare
7. detalii articol
8. favorite si rating
9. harta cu institutii
10. grafice analytics
11. setari salvate

## Concluzie

`Academic Engine Mobile Lite` este cea mai buna varianta daca aplicatia trebuie sa aiba legatura cu licenta.

Este suficient de simpla pentru Android Studio, dar pastreaza clar identitatea proiectului:

- research threads
- articole stiintifice
- surse academice
- citari
- analiza locala
- vizualizare pe harta

Aceasta abordare este mai potrivita decat o aplicatie generica sau una doar despre evenimente academice.

# Queries

In the following examples for valid requests including mandatory fields are listed.

Results are listed in descending order according to last time of change, thus the most recent item is the first entry in the result.

## System Monitoring

1. alle Fakten

   `{"session": 0, "query": "facts"}`

2. alle Ereignisse (der letzten 15 Minuten)

   `{"session": 0, "query": "events"}`

3. alle Modelle (der letzten 15 Minuten)

   `{"session": 0, "query": "models"}`

## Domain Knowledge

### Course

1. alle Kurse

   `{"session": 0, "query": "courses"}`

#### Member

1. alle Teilnehmer im Kurs

   `{"session": 0, "query": "members", "course": "7"}`

2. bestimmter Teilnehmer

   `{"session": 0, "query": "member", "course": "7", "id": "2"}`

#### Gruppen

1. alle Gruppen eines Teilnehmers

   `{"session": 0, "query": "groups_member", "course": 7, "id": 2}`

2. alle Gruppen im Kurs

   `{"session": 0, "query": "groups", "course": "7"}`

3. bestimmte Gruppe

   `{"session": 0, "query": "group", "course": 7, "id": 8}`

#### Inhalte

1. alle Inhalte eines Teilnehmers

   `{"session": 0, "query": "contents_member", "course": 7, "id": 2}`

2. alle <u>klassifizierten</u> Inhalte eines Teilnehmers

   `{"session": 0, "query": "contents!member", "course": 7, "id": 2}`

3. alle Inhalte einer Gruppe

   `{"session": 0, "query": "contents_group", "course": 7, "id": 8}`

4. alle <u>klassifizierten</u> Inhalte einer Gruppe

   `{"session": 0, "query": "contents!group", "course": 7, "id": 8}`

5. alle Inhalte in einem Kurs

   `{"session": 0, "query": "contents", "course": 7}`

6. bestimmter Inhalt

   `{"session": 0, "query": "content", "course": 7, "id": 86}`

7. alle klassifizierten Inhalte im Kurs

   `{"session": 0, "query": "content!", "course": 7, "id": 86}`

#### Seiten

1. alle Seiten eines Teilnehmers

   `{"session": 0, "query": "pages_member", "course": 7, "id": 2}`

2. alle <u>klassifizierten</u> Seiten eines Teilnehmers

   `{"session": 0, "query": "pages!member", "course": 7, "id": 2}`

3. alle Seiten einer Gruppe

   `{"session": 0, "query": "pages_group", "course": 7, "id": 8}`

4. alle <u>klassifizierten</u> Seiten einer Gruppe

   `{"session": 0, "query": "pages!group", "course": 7, "id": 8}`

5. alle Seiten im Kurs

   `{"session": 0, "query": "pages", "course": 7}`

6. bestimmte Seite

   `{"session": 0, "query": "page", "course": 7, "id": 75}`

7. alle Seiten eines Wikis

   `{"session": 0, "query": "pages_wiki", "course": 7, "id": 22}`

#### Beiträge

1. alle Beiträge eines Teilnehmers

   `{"session": 0, "query": "posts_member", "course": 7, "id": 2}`

2. alle <u>klassifizierten</u> Beiträge eines Teilnehmers

   `{"session": 0, "query": "posts!member", "course": 7, "id": 2}`

3. alle Beiträge einer Gruppe

   `{"session": 0, "query": "posts_group", "course": 7, "id": 8}`

4. alle <u>klassifizierten</u> Beiträge einer Gruppe

   `{"session": 0, "query": "posts!group", "course": 7, "id": 8}`

5. alle Beiträge im Kurs

   `{"session": 0, "query": "posts", "course": 7}`

6. bestimmter Beitrag

   `{"session": 0, "query": "post", "course": 7, "id": 71}`

7. alle Beiträge in einem Forums

   `{"session": 0, "query": "posts_forum", "course": 7, "id": 16}`

8. alle Beiträge einer Diskussion

   `{"session": 0, "query": "posts_discussion", "course": 7, "id": 35}`

#### Rollen

1. alle Rollen eines Teilnehmers

   `{"session": 0, "query": "roles_member", "course": 7, "id": 2}`

2. alle Rollen im Kurs

   `{"session": 0, "query": "roles", "course": 7}`

3. bestimmte Rolle

   `{"session": 0, "query": "role", "course": 7, "id": 11}`

#### Gruppierungen

1. alle Gruppierungen eines Teilnehmers

   `{"session": 0, "query": "groupings_member", "course": 7, "id": 2}`

2. alle Gruppierungen einer Gruppe

   `{"session": 0, "query": "groupings_group", "course": 7, "id": 8}`

3. alle Gruppierungen in einem Kurs

   `{"session": 0, "query": "groupings", "course": 7}`

4. bestimmte Gruppierung

   `{"session": 0, "query": "grouping", "course": 7, "id": 5}`

#### Diskussionen

1. alle Diskussionen einer Gruppe

   `{"session": 0, "query": "discussions_group", "course": 7, "id": 8}`

2. alle Diskussionen, in denen ausschliesslich diese Gruppe aktiv ist

   `{"session": 0, "query": "discussions:group", "course": 7, "id": 8}`

3. alle Diskussionen im Kurs

   `{"session": 0, "query": "discussions", "course": 7}`

4. bestimmte Diskussion

   `{"session": 0, "query": "discussion", "course": 7, "id": 35}`

5. alle Diskussionen im Forum

   `{"session": 0, "query": "discussions_forum", "course": 7, "id": 16}`

#### Forum

1. alle Foren im Kurs

   `{"session": 0, "query": "forums", "course": 7}`

2. bestimmtes Forum

   `{"session": 0, "query": "forum", "course": 7, "id": 16}`

3. alle Foren einer Gruppe

   `{"session": 0, "query": "forums_group", "course": 7, "id": 8}`

4. alle Foren, in denen ausschliesslich diese Gruppe aktiv ist

   `{"session": 0, "query": "forums:group", "course": 7, "id": 8}`

#### Wiki

1. alle Wikis im Kurs

   `{"session": 0, "query": "wikis", "course": 7}`

2. bestimmtes Wiki

   `{"session": 0, "query": "wiki", "course": 7, "id": 22}`

3. alle Wikis einer Gruppe

   `{"session": 0, "query": "wikis_group", "course": 7, "id": 8}`

4. alle Wikis, in denen ausschliesslich diese Gruppe aktiv ist

   `{"session": 0, "query": "wikis:group", "course": 7, "id": 8}`

#### Module

1. alle Module im Kurs

   `{"session": 0, "query": "modules", "course": 7}`

2. bestimmtes Modul

   `{"session": 0, "query": "module", "course": 7, "id": 12}`

### Awareness

#### Beteiligung

1. alle Selbsteinschätzungen im Kurs

   `{"session": 0, "query": "assessments", "course": 7}`

2. alle Selbsteinschätzungen eines Teilnehmers

   `{"session": 0, "query": "assessments_member", "course": 7, "id": 2}`

3. alle Selbsteinschätzungen innerhalb einer Gruppe

   `{"session": 0, "query": "assessments_group", "course": 7, "id": 8}`

4. die aktuellsten Selbsteinschätzungen innerhalb einer Gruppe

   `{"session": 0, "query": "assessments:group", "course": 7, "id": 8}`

#### Todos

1. alle Todos im Kurs

   `{"session": 0, "query": "todo", "course": 7}`

2. alle persönlichen Todos eines Teilnehmers

   `{"session": 0, "query": "todos_member", "course": 7, "id": 2}`

3. alle kollektiven Todos in einer Gruppe

   `{"session": 0, "query": "todos_group", "course": 7, "id": 8}`

#### Verfügbarkeit

1. alle Verfügbarkeiten im Kurs

   `{"session": 0, "query": "availabilities", "course": 7}`

2. die Verfügbarkeit in einer Gruppe

   `{"session": 0, "query": "availability", "course": 7, "id": 8}`

#### Stimmung

1. alle Stimmungen im Kurs

   `{"session": 0, "query": "moods", "course": 7}`

2. die Stimmung in einer Gruppe

   `{"session": 0, "query": "mood", "course": 7, "id": 8}`

### Inceptions

#### Task

1. alle Aufgaben im Kurs

   `{"session": 0, "query": "tasks", "course": 7}`

2. bestimmte Aufgabe

   `{"session": 0, "query": "task", "course": 7, "id": 13}`

3. alle Aufgaben einer Gruppe

   `{"session": 0, "query": "tasks_group", "course": 7, "id": 8}`

### Prompts

#### Preparation | Coordination @ Group

1. Koordinationsumfang (bei Aufgabenstart) für eine bestimmte Gruppe

   Metrik: 0 = "perfekt oder Warm-up", 0.25 = "ok" , 0.5 = "naja", 0.75 = "puhhh", 1 = "Totenkopf!"

   `{"session": 0, "query": "preparation", "course": 7, "id": 3}`

2. alle Koordinationsumfänge im Kurs zum Aufgabenstart

   `{"session": 0, "query": "preparations", "course": 7}`

#### Display | Messaging @ Member

1. Hinweise (akutell nur  "assessment") für einen bestimmten Nutzer

   Metrik ist Dringlichkeit: 0 = blockieren, 0.5 = freigeben,  1 = forcieren

   `{"session": 0, "query": "display", "course": 7, "id": 3}`

2. alle Hinweise im Kurs

   `{"session": 0, "query": "displays", "course": 7}`


### Yet Not Used (dismissed)

#### Participation | Imbalance  @ Group

1. Arbeitsverteilung (in Foren und Wikis) für eine bestimmte Gruppe

   Metrik ist der normalisierte Gini-Koeffizient: 0 = Gleichverteilung, 1 = Ungleichverteilung

   `{"session": 0, "query": "participation", "course": 7, "id": 3}`

2. alle Arbeitsverteilungen im Kurs

   `{"session": 0, "query": "participations", "course": 7}`

#### Response | Latency @ Group

1. Latenzen (für Foren) für eine bestimmte Gruppe

   Metrik ist die vergangene Zeit: 0 = keine Latenz, 1 = Latenzüberschreitung

   `{"session": 0, "query": "response", "course": 7, "id": 3}`

2. alle Latenzen im Kurs

   `{"session": 0, "query": "responses", "course": 7}`


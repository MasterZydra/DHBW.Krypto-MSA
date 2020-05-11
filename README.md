# DHBW.Krypto-MSA

## CQL (Cryptographic Query Language)
- **show algorithm**  
Die in Komponenten gekapselten Algorithmen werden angezeigt.
Die Meldung im Ausgabebereich der GUI wird dynamisch aus den Dateinamen der jar im Verzeichnis components ermittelt; shift | rsa
- **encrypt message "[message]" using [algorithm] and keyfile [filename]**  
Die zu dem Algorithmus korrespondierende Komponente [algorithm].jar wird dynamisch geladen und die Meldung mit dem key verschlüsselt.  
Die verschlüsselte Meldung wird im Ausgabebereich der GUI angezeigt.
- **decrypt message "[message]" using [algorithm] and keyfile [filename]**  
Die zu dem Algorithmus korrespondierende Komponente [algorithm].jar wird dynamisch geladen und die Meldung mit dem key entschlüsselt.  
Die entschlüsselte Meldung wird im Ausgabebereich der GUI angezeigt.
- **crack encrypted message "[message]" using [algorithm]**  
Die zu dem Algorithmus korrespondierende Komponente [algorithm]\_cracker.jar wird dynamisch geladen und versucht innerhalb von maximal 30 Sekunden die Meldung zu entschlüsseln. Wurde die Meldung innerhalb der Zeitvorgabe entschlüsselt, wird die entschlüsselte Meldung im Ausgabebereich der GUI angezeigt.  
Wurde die Meldung nicht innerhalb der Zeitvorgabe entschlüsselt, erfolgt die Meldung "_cracking encrypted message "[message]" failed_" im Ausgabebereich der GUI.
- **register participant [name] with type [normal | intruder]**
- **create channel [name] from [participant01] to [participant02]**
- **show channel**
- **drop channel [name]**
- **intrude channel [name] by [participant]**
- **send message "[message]" from [participant01] to [participant02] using [algorithm] and keyfile [name]**

## Aufgabenstellung
- **Programmiersprache**  
Java 11 (LTS)
- **IDE**  
IntelliJ IDEA Community
- **Implementierung** einer technisch einwandfrei lauffähigen Applikation. Kommunikation der Nachricht "morpheus".
- Nutzung der **camelCase-Notation**, um die Lesbarkeit zu vereinfachen.
- Verwendung geeigneter englischer Begriffe für Namen und Bezeichnungen.
- **Zulässige externe Bibliotheken**: JavaFX, Google Guava, HSQLDB
- **Erstellung einer vollständigen 7-Zip-Datei und Upload in Moodle.**
- **Zeitansatz**: 50 Stunden
- **Abgabetermin**: Sonntag, 18.10.2020
- **Bewertung**: 50 Punkte

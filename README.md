# DHBW.Krypto-MSA

![Screenshot](img/ScreenshotCrack.png)

## Beispiel anhand der Nachricht "morpheus"
**Encrypt/Decrypt with RSA**  
`encrypt message "morpheus" using RSA and keyfile rsa32`  
`decrypt message "JbkPFt+y+j8=" using RSA and keyfile rsa32`  
`crack encrypted message "JbkPFt+y+j8=" using RSA and keyfile rsa32`  
Cracking fails due to timeout

**Crackable RSA example**  
`crack encrypted message "BqGfopSO" using RSA and keyfile rsa24`

**Encrypt/Decrypt with Shift**  
`encrypt message "morpheus" using shift and keyfile shift13`  
`decrypt message "zbecurhf" using shift and keyfile shift13`  
`crack encrypted message "zbecurhf" using shift`

## Anmerkungen zur Implementierung
Um die Anwendung funktionsfähig zu bekommen, sind Abweichungen von der Aufgabenstellung notwendig.

**Eventbus**  
Damit der Eventbus korrekt funktioniert, musste die `failureaccess-1.0.1.jar` ergänzt werden, da sonst immer nur Fehlermeldungen angezeigt wurden.

**Cracker**  
Der RSA Cracker benötigt eine `File` für den öffentlichen Schlüssel. Um beim Arbeiten mit der Komponente keine Fallunterscheidung vornehmen zu müssen, wurde das Interface für den Shift Cracker auch um `File` erweitert. Das übergebene Objekt wird jedoch nicht verarbeitet.

Schnittstelle RSA Cracker:  
`String decrypt(String encryptedMessage, File publicKeyfile);`

Schnittstelle Shift Cracker:  
`String decrypt(String encryptedMessage, File file);`

Der CQL Befehl wurde erweitert mit der Option eine Datei zu übergeben:
`crack encrypted message "[message]" using [algorithm] [and keyfile [filename]]`

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

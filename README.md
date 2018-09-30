# ListViewAPI

Hvordan bruke applikasjonen:

- Blå tekst betyr at teksten er trykkbar og vil utføre en funksjon.

- Trykk på last inn fler for å laste in flere sider fra API. Man kan laste inn opptil 100 sider.

- Tilbake til toppen får listen til å peke på den første/øverste raden. 

- Trykk på firmanavn hvis du vil sortere listen etter firmanavn. Sorterer stigende/synkende hver gang man trykker på den.

- Try på orgnummer for å sortere listen etter organisasjonsnummer. Sorterer stigende/synkende hver gang man trykker på den.

- Hvis man ikke har internett-tilkobling blir man varslet gjennom en textview som ellers informerer hvor mange sider av APIet
som er lastet inn.
Søk-knappen oppe til høyre søker etter selskapene i listen som starter med det man skriver. Hvis man vil at det skal søke på 
selskap som inneholder ordene man har kan man endre det i MainActivity.java i linje 234 og linje 238 ved å bruke .contains()
istedenfor .startsWith(). 

-Trykk på en rad for å få opp mer informasjon om selskapet.

-Hjemmeside og adresse kan trykkes på. Hvis de har en gyldig hjemmeside og/eller adresse så vil de åpnes av telefonen dersom en 
applikasjon som kan utføre oppgaven er tilgjengelig(En browser for hjemmeside, google maps for adresse).

- Tilbake knappen sender bruker tilbake til listen.

- Hvis man vil søke mer om selskapet kan man trykke "Google søk organisasjonen". Da åpnes en browser og det blir utført en google
søk på selskapets navn.

Kjente feil:
- Søkfeltet nullstilles ved orientasjonsendring.

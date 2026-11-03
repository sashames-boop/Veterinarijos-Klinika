@startuml
class Admin {
    -username: String
    -pass: String
}

class Klientas {
    -vardas: String
    -pavarde: String
    -tel: int
    +uzregistravimas(): void
    +palikti_ivertinimas(): String
    +atsakyti_i_kom(): String
    +redaguoti_kom(): String
}

class Augintinis {
    -tipas: String
    -vardas: String
    -amzius: int
    -svoris: double
    +gauti_tyr_rez(): void
}

class Veterinaras {
    -vardas: String
    -pavarde: String
    -specialybe: String
    -val_ikainis: double
    +atlikti_tyr(): void
    +patvirtinti_vizita(): bool
    +viz_start_stop(): void
    +prideti_veiksmu_apr(): String //+ nueitas atstumas; ar buvo pamaitintas
    +prideti_nuotr(): void
    +uzreg_incindeta(): void
    +palitkti_ivertinima(): int
}

class Klinika {
    -pavadinimas: String
    -adresas: String
    -laisvu_vietu_sk: int
    -laisvu_vizitu_laikas: LocalTime
    +sukurti_paslauga(): void
    +redaguoti_pasl(): void //+ keisti kaina; kokiems gyvunams teikiama paslauga
    +perziureti_ist(): void
    +ar_klientas_atvyko(): bool
    +atsakyti_i_kom(): String
    +redaguoti_kom(): String
    +priskirti_priziur(): void
    +nustatyti_darbo_laika(): LocalTime
}

Klientas "1"--"1..*" Augintinis // KLientas gali tureti kelis augintinius; AUgintinis turi tik viena KLientas
Augintinis "1"--"0..*" Klinika // Augintinis gali priklausyti nuo 0 iki daug kliniku; Klinika turi tik viena tam tikra Augintinis
Veterinaras "1"--"0..*" Klinika // 
@enduml
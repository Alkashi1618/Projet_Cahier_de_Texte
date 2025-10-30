# 📘 Cahier de Texte Numérisé - Institut TDSI (UCAD)

> Système de numérisation et de gestion académique pour l’Institut TDSI (Université Cheikh Anta Diop de Dakar)

---

## 📋 Description du Projet

Le **Système de numérisation des cahiers de texte** vise à moderniser la gestion académique au sein de l’Institut TDSI.  
Cette application web **J2EE** permet de gérer les cours, les présences, les justificatifs d’absence et les rapports pédagogiques, tout en assurant la traçabilité et la fiabilité des données académiques.

---

## 🏗️ Architecture

### 🧰 Stack Technique
| Couche | Technologie |
|:--|:--|
| **Backend** | Java 11, Servlets J2EE |
| **Serveur d’application** | Apache Tomcat 9.0 |
| **Base de données** | MySQL (via phpMyAdmin) |
| **Frontend** | JSP, HTML, CSS |
| **Architecture** | MVC (Model-View-Controller) |

### 📂 Structure du Projet

CahierDeTEXTE/
├── src/main/java/com/cahiertexte/
│ ├── controller/ # Servlets (contrôleurs)
│ ├── dao/ # Data Access Objects
│ ├── model/ # Entités métier
│ ├── service/ # Logique métier
│ └── util/ # Utilitaires (hashage, etc.)
├── src/main/webapp/
│ └── views/ # Pages JSP
└── build/ # Classes compilées


---

## 👥 Acteurs et Rôles

| Acteur | Responsabilités principales |
|:--|:--|
| **Responsable de Formation** | Gestion des utilisateurs, matières, validation des justificatifs, supervision globale |
| **Responsable de Classe** | Planification, saisie du cahier de texte, gestion des présences |
| **Professeur** | Validation des cours, saisie des cahiers de texte, statistiques |
| **Étudiant** | Consultation du planning, justification d’absences, suivi des statistiques |

---

## 🗄️ Modèle Conceptuel de Données (MCD)

### 🔑 Rôles Utilisateurs
`RESPONSABLE_FORMATION`, `RESPONSABLE_CLASSE`, `PROFESSEUR`, `ETUDIANT`

### 📚 Classes
`CI_M1`, `CI_M2`, `MCS_M1`, `MCS_M2`

### 🧭 Statuts
**Cours :** PLANIFIE, REALISE, ANNULE, RATTRAPAGE  
**Présence :** PRESENT, ABSENT, RETARD_ACCEPTE, RETARD_REFUSE  
**Justificatif :** EN_ATTENTE, VU_RESPONSABLE_CLASSE, ACCEPTE, REFUSE  
**Type de justificatif :** MEDICAL, ADMINISTRATIF, FAMILIAL, AUTRE

---

## 🚀 Fonctionnalités Principales

### 📘 Gestion des Cours
- Planification des cours
- Saisie du cahier de texte (contenu, objectifs, travaux, ressources)
- Validation par le professeur
- Calcul du volume horaire réalisé/restant

### 👨‍🏫 Gestion des Présences
- Initialisation automatique
- Calcul du taux de présence
- Détection des étudiants critiques (≥3 absences)
- Top 5 des absences par classe

### 📎 Gestion des Justificatifs
- Soumission et validation hiérarchique
- Upload de fichiers justificatifs
- Historique complet

### 📊 Tableaux de Bord
- Dashboards personnalisés selon le rôle
- Statistiques et alertes en temps réel

---

## 🔒 Sécurité

- **Hashage SHA-256** des mots de passe  
- **Sessions HTTP sécurisées (2h)**  
- **Contrôle d’accès par rôle**  
- **Redirection automatique selon les permissions**

---

## ⚙️ Installation et Déploiement

### 🧱 Prérequis
- Java JDK 11+
- Apache Tomcat 9.0
- MySQL Server
- Eclipse IDE ou IntelliJ IDEA (plugin J2EE)

### 🗃️ Configuration de la Base de Données

```sql
CREATE DATABASE Cahier_de_Texte CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Dans DatabaseConnection.java :

private static final String HOST = "localhost";
private static final int PORT = 3306;
private static final String DATABASE = "Cahier_de_Texte";
private static final String USERNAME = "root";
private static final String PASSWORD = "";

🚀 Déploiement sur Tomcat

Via Eclipse :

    Clic droit sur le projet → Run As → Run on Server

    Sélectionner Tomcat 9.0

Manuellement :

mvn package
cp target/CahierDeTEXTE.war %CATALINA_HOME%\webapps\
%CATALINA_HOME%\bin\startup.bat

Accès à l’application :

    http://localhost:8080/CahierDeTEXTE

🔐 Configuration HTTPS (optionnelle)

Un certificat SSL auto-signé a été généré pour activer HTTPS sur Tomcat :

keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 365

Configuration ajoutée dans conf/server.xml :

<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150" SSLEnabled="true">
    <SSLHostConfig>
        <Certificate certificateKeystoreFile="C:\Tomcat\apache-tomcat-9.0.106\keystore.jks"
                     certificateKeystorePassword="motdepasse" type="RSA" />
    </SSLHostConfig>
</Connector>

Accès sécurisé :

https://localhost:8443

🧪 Données de Test

Mot de passe par défaut :
password123 → SHA-256 : ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f

Exemples d’utilisateurs :

INSERT INTO users (username, password, nom, prenom, email, role) VALUES
('admin.formation', '<hash>', 'Diop', 'Amadou', 'admin@tdsi.sn', 'RESPONSABLE_FORMATION'),
('resp.ci.m1', '<hash>', 'Ndiaye', 'Fatou', 'resp.cim1@tdsi.sn', 'RESPONSABLE_CLASSE'),
('prof.samb', '<hash>', 'Samb', 'Moussa', 'prof.samb@tdsi.sn', 'PROFESSEUR'),
('etud.fall', '<hash>', 'Fall', 'Aissatou', 'fall@etudiant.tdsi.sn', 'ETUDIANT');

🧩 Points d’Extension (Roadmap)

    📄 Export PDF des rapports

    📬 Notifications e-mail

    📱 API REST pour version mobile

    🕒 Génération automatique d’emplois du temps

    🧾 Module d’évaluations

    🕵️ Historique des modifications

🎯 Workflow Typique
🗓️ Scénario 1 : Planification d’un Cours

    Le responsable de classe se connecte

    Crée un cours → statut PLANIFIE

📝 Scénario 2 : Saisie du Cahier de Texte

    Le professeur saisit les détails du cours

    Le cours devient REALISE puis est validé

🚷 Scénario 3 : Gestion d’une Absence

    L’étudiant soumet un justificatif

    Le responsable de formation le valide ou le rejette

📞 Contact

Projet TDSI - Université Cheikh Anta Diop de Dakar
Année académique : 2024-2025
🧑‍💻 Réalisé par : [Ton Nom / Groupe de Projet]
📝 Licence

Projet académique - Institut TDSI
© 2024-2025 Tous droits réservés.
